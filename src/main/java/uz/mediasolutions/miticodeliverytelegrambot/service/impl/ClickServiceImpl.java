package uz.mediasolutions.miticodeliverytelegrambot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.mediasolutions.miticodeliverytelegrambot.entity.*;
import uz.mediasolutions.miticodeliverytelegrambot.entity.click.ClickInvoice;
import uz.mediasolutions.miticodeliverytelegrambot.entity.click.ClickOrder;
import uz.mediasolutions.miticodeliverytelegrambot.entity.click.Payment;
import uz.mediasolutions.miticodeliverytelegrambot.exceptions.ClickException;
import uz.mediasolutions.miticodeliverytelegrambot.exceptions.ClickExceptionNoRollBack;
import uz.mediasolutions.miticodeliverytelegrambot.exceptions.RestException;
import uz.mediasolutions.miticodeliverytelegrambot.mapper.ClickOrderMapper;
import uz.mediasolutions.miticodeliverytelegrambot.payload.click.*;
import uz.mediasolutions.miticodeliverytelegrambot.repository.*;
import uz.mediasolutions.miticodeliverytelegrambot.service.abs.ClickService;
import uz.mediasolutions.miticodeliverytelegrambot.service.webimpl.TgService;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static uz.mediasolutions.miticodeliverytelegrambot.enums.InvoiceStatusEnum.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClickServiceImpl implements ClickService {

    private final ClickOrderMapper clickOrderMapper;
    private final ClickOrderRepository clickOrderRepository;
    private final PaymentRepository paymentRepository;
    private final ConstantsRepository constantsRepository;
    private final TgUserRepository tgUserRepository;
    private final TgService tgService;
    private final ClickInvoiceRepository clickInvoiceRepository;

    @Value("${click.base.url}")
    private String clickBaseUrl;

    @Value("${click.service.id}")
    private String clickServiceId;

    @Value("${click.merchant.id}")
    private String clickMerchantId;

    @Value("${click.merchant.user.id}")
    private Integer clickMerchantUserId;

    @Value("${click.secret.key}")
    private String clickSecretKey;

    private String generateDigest(long millisecond) {
        try {
            String input = millisecond + clickSecretKey;

            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            byte[] hashBytes = sha1.digest(input.getBytes(StandardCharsets.UTF_8));

            // Convert the byte array to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            String sha1Hash = hexString.toString();
            return sha1Hash;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String generateAuthHeader() {
        long currentSecond = System.currentTimeMillis() / 1000;
        String digest = generateDigest(currentSecond);
        return clickMerchantUserId + ":" + digest + ":" + currentSecond;
    }

    @Override
    public HttpEntity<ResClickOrderDTO> create(Double amount, String chatId) {
        TgUser tgUser = tgUserRepository.findByChatId(chatId);
        log.info("User {} , Amount {}", tgUser, amount);

        Constants constants = constantsRepository.findById(1L).orElseThrow(
                () -> RestException.restThrow("CONSTANTS NOT FOUND", HttpStatus.BAD_REQUEST));

        if (amount < constants.getMinOrderPrice()) throw RestException.restThrow("MIN ORDER PRICE = " +
                constants.getMinOrderPrice(), HttpStatus.BAD_REQUEST);
        ClickInvoice invoice = ClickInvoice.builder()
                .status(PENDING)
                .user(tgUser)
                .userId(tgUser.getId())
                .price(amount)
                .leftoverAmount(amount)
                .paidAmount(0D)
                .build();
        clickInvoiceRepository.save(invoice);

        ResClickOrderDTO resClickOrderDTO = new ResClickOrderDTO(
                clickMerchantId,
                clickServiceId,
                invoice.getId().toString(),
                amount
        );
        log.info("Generated Invoice Id: {}, Amount: {}, ", invoice.getId(), invoice.getPaidAmount());
        return ResponseEntity.ok(resClickOrderDTO);
    }

    @Override
    public HttpEntity<?> createInvoice(ClickInvoiceDTO dto, String chatId) {
        try {
            ResClickOrderDTO clickOrderDTO = create(Double.valueOf(dto.getAmount()), chatId).getBody();
            String authHeader = generateAuthHeader();

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
            headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            headers.set("Auth", authHeader);
            System.out.println("authHeader => " + authHeader);

            assert clickOrderDTO != null;
            ClickCreateInvoiceDTO createInvoiceDto = new ClickCreateInvoiceDTO(
                    clickServiceId, dto.getAmount(), dto.getPhoneNumber(), clickOrderDTO.getTransactionParam());
            HttpEntity<ClickCreateInvoiceDTO> requestEntity = new HttpEntity<>(createInvoiceDto, headers);

            CreateInvoiceResponseDTO response = restTemplate.postForObject(clickBaseUrl + "invoice/create",
                    requestEntity, CreateInvoiceResponseDTO.class);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal server error!");
        }
    }

    @Override
    @Transactional(noRollbackFor = ClickException.class)
    public ClickOrderDTO prepareMethod(ClickOrderDTO clickDTO) {
        log.info("preparePayment clickDTO: {}", clickDTO);

        checkClickSignKeyPrepare(clickDTO);

        ClickInvoice invoice = getInvoiceByInvoiceNumber(clickDTO.getMerchant_trans_id());
        log.info("preparePayment invoice {}", invoice);

        checkInvoiceStatus(invoice);

        checkParameterAmount(invoice, clickDTO);

        ClickOrder clickOrder = clickOrderMapper.toEntity(clickDTO);
        clickOrder.setInvoice(invoice);
        clickOrderRepository.save(clickOrder);

        clickDTO.setMerchant_prepare_id(clickOrder.getId());


        ClickOrderDTO clickOrderDTO = new ClickOrderDTO(
                clickDTO.getClick_trans_id(),
                clickDTO.getMerchant_trans_id(),
                clickDTO.getMerchant_prepare_id(),
                0,
                clickDTO.getError_note()
        );

        log.info("preparePayment clickOrderDTO {}", clickOrderDTO);
        return clickOrderDTO;

    }

    @Override
    public ClickOrderDTO completeMethod(ClickOrderDTO clickDTO) throws TelegramApiException {
        log.info("completePayment clickDTO: {}", clickDTO);

        checkClickSignKeyComplete(clickDTO);

        ClickOrder clickOrder = getClickOrder(clickDTO.getMerchant_prepare_id());
        log.info("completePayment clickOrder {}", clickOrder);

        // Click send us about cancel this transaction | Action = 1 and Error = -5017 | see click documentation
        if (Objects.equals(clickDTO.getAction(), 1) && Objects.equals(clickDTO.getError(), -5017)) {
            log.info("Click send us about cancel this transaction | Action = 1 and Error = -5017 | see click documentation ");
            cancelTransaction(clickOrder, clickDTO);
        }
        checkTransactionCancelled(clickOrder);

        clickOrder.setAmount(clickOrder.getAmount());
        clickOrder.setAction(clickDTO.getAction());
        clickOrder.setError(clickDTO.getError());
        clickOrder.setErrorNote(clickDTO.getError_note());
        clickOrderRepository.save(clickOrder);

        //ORDER BO'YICHA TEKSHIRIHSNI BOSHLAYMIZ
        ClickInvoice invoice = clickOrder.getInvoice();
        log.info("completePayment invoice: {}", invoice);


        checkInvoiceStatus(invoice);

        checkParameterAmount(invoice, clickDTO);


        double invoicePaymentAmount = clickDTO.getAmount();

        double leftoverAmount = invoice.getLeftoverAmount() - invoicePaymentAmount;

        invoice.setLeftoverAmount(leftoverAmount);

        if (leftoverAmount == 0) {
            invoice.setStatus(PAID);
        }

        if (leftoverAmount > 0) {
            invoice.setStatus(PAYING);
        }

        double paidAmount = invoice.getPaidAmount() + (invoicePaymentAmount);

        invoice.setPaidAmount(paidAmount);

        clickInvoiceRepository.save(invoice);
        log.info("completePayment invoice {}", invoice);


        Payment payment = createAndSavePayment(invoice, invoicePaymentAmount);
        log.info("completePayment payment {}", payment);

        clickOrder.setPayment(payment);
        clickOrderRepository.save(clickOrder);
        log.info("completePayment clickOrder {}", clickOrder);

        tgService.execute(tgService.whenSendOrderToChannelClick(invoice.getUser().getChatId()));
        tgService.execute(tgService.whenSendOrderToUserClick(invoice.getUser().getChatId()));

        return new ClickOrderDTO(
                clickDTO.getClick_trans_id(),
                clickDTO.getMerchant_trans_id(),
                0,
                clickDTO.getMerchant_prepare_id(),
                clickDTO.getMerchant_prepare_id(),
                clickDTO.getError_note()
        );
    }


    @Override
    public ClickOrderDTO getInfo(ClickOrderDTO clickDTO) {
        log.info("getInfo clickDTO: {}", clickDTO);

        ClickInvoice invoice = clickInvoiceRepository.findById(Long.valueOf(clickDTO.getMerchant_trans_id()))
                .orElseThrow(() -> ClickException.invoiceDoesNotExist("Subscriber not found"));

        ClickOrderParamDTO param = new ClickOrderParamDTO();

        TgUser user = invoice.getUser();

        param.setFullName(user.getName());
        param.setPhoneNum(user.getPhoneNumber());
        param.setInvoice(String.valueOf(invoice.getId()));
        param.setAmount(invoice.getLeftoverAmount());

        clickDTO.setParams(param);
        clickDTO.setAction(0);
        clickDTO.setError(0);
        clickDTO.setError_note("SUCCESS");
        return clickDTO;
    }

//    @Override
//    @Transactional
//    public HttpEntity<?> createInvoice(ClickInvoiceDTO dto) {
//        ResClickOrderDTO clickOrderDTO = create(dto.getAmount().doubleValue()).getBody();
//
//
////        log.info("Token created {}", token);
//        Gson gson = new Gson();
//        assert clickOrderDTO != null;
//        ClickCreateInvoiceDTO invoice = new ClickCreateInvoiceDTO(
//                clickServiceId,
//                dto.getAmount(),
//                dto.getPhoneNumber(),
//                clickOrderDTO.getTransactionParam());
//        log.info("Invoice {}", gson.toJson(invoice));
//
//        String authHeader = generateAuthHeader();
//        log.info("Token {}", authHeader);
//        RestTemplate restTemplate = new RestTemplate();
//        CreateInvoiceResponseDTO responseDTO = restTemplate.postForObject(clickBaseUrl, invoice, CreateInvoiceResponseDTO.class);
//        log.info("Create invoice response {}", responseDTO);
//
//        return ResponseEntity.ok(responseDTO);
//    }

    public Payment createAndSavePayment(ClickInvoice invoice, Double invoicePaymentAmount) {

        Payment payment = new Payment();
        payment.setInvoice(invoice);
        payment.setInvoicePrice(invoice.getPrice());
        payment.setPaidAmount(invoicePaymentAmount);
        payment.setLeftoverAmount(invoice.getLeftoverAmount());
        payment.setDate(new Date());
        payment.setPercent(0d);
        payment.setCancelled(Boolean.FALSE);

        paymentRepository.save(payment);
        log.info("createAndSavePayment studentExamMQ payment {}", payment);

        return payment;
    }

    public void cancelTransaction(ClickOrder clickOrder, ClickOrderDTO clickDTO) {
        log.info("cancelTransaction clickOrder {}, clickDTO {} ", clickOrder, clickDTO);

        Payment payment = clickOrder.getPayment();
        log.info("cancelTransaction payment {} ", payment);

        payment.setCancelled(Boolean.TRUE);

        ClickInvoice invoice = payment.getInvoice();
        log.info("cancelTransaction invoice {} ", invoice);

        double paidAmount = invoice.getPaidAmount() - payment.getPaidAmount();
        double leftAmount = invoice.getLeftoverAmount() + payment.getPaidAmount();

        invoice.setPaidAmount(paidAmount);
        invoice.setLeftoverAmount(leftAmount);

        clickInvoiceRepository.save(invoice);
        log.info("cancelTransaction invoice {} ", invoice);
        paymentRepository.save(payment);
        log.info("cancelTransaction payment {} ", payment);

        clickOrder.setPayment(payment);
        clickOrder.setAmount(clickOrder.getAmount());
        clickOrder.setAction(clickDTO.getAction());
        clickOrder.setError(clickDTO.getError());
        clickOrder.setErrorNote(clickDTO.getError_note());
        clickOrderRepository.save(clickOrder);
        log.info("cancelTransaction clickOrder {} ", clickOrder);

        throw ClickExceptionNoRollBack.transactionCancelled("Transaction cancelled");
    }

    public ClickOrder getClickOrder(Long merchantPrepareId) {
        log.info("getClickOrder merchantPrepareId {}", merchantPrepareId);
        return clickOrderRepository.findById(merchantPrepareId)
                .orElseThrow(() -> ClickException.transactionDoesNotExist("Transaction does not exist"));
    }

    public void checkParameterAmount(ClickInvoice invoice, ClickOrderDTO clickDTO) {
        log.info("checkParameterAmount invoice {} , clickDTO {} ", invoice, clickDTO);

        if (invoice.getLeftoverAmount() < ((double) clickDTO.getAmount()))
            throw ClickException.incorrectPaymentAmount("Incorrect parameter amount");
    }

    public void checkClickSignKeyPrepare(ClickOrderDTO clickDTO) {
        log.info("checkClickSignKey clickDTO {}", clickDTO);
        //SECURE KEY BILAN BIRGALIKDA MD5 YASALAYAPTI
        String signKey = clickDTO.generatePrepareSignString(clickSecretKey);

        log.info("checkClickSignKey signKey {}", signKey);
        if (!Objects.equals(clickDTO.getSign_string(), signKey))
            throw ClickException.signCheckFailed("Signature verification error");
    }

    public ClickInvoice getInvoiceByInvoiceNumber(String merchantTransId) {
        return clickInvoiceRepository.findById(Long.valueOf(merchantTransId)).orElseThrow(() -> RestException.notFound("INVOICE"));
    }

    public void checkInvoiceStatus(ClickInvoice invoice) {
        log.info("checkInvoiceStatus invoice {}", invoice);

        if (!List.of(PENDING, PAYING).contains(invoice.getStatus())) {
            throw ClickException.transactionCancelled("Transactions cancelled");
        } else if (Objects.equals(invoice.getStatus(), PAID)) {
            throw ClickException.alreadyPaid("Already paid");
        }
    }

    public void checkClickSignKeyComplete(ClickOrderDTO clickDTO) {
        log.info("checkClickSignKey clickDTO {}", clickDTO);
        //SECURE KEY BILAN BIRGALIKDA MD5 YASALAYAPTI
        String signKey = clickDTO.generateCompleteSignString(clickSecretKey);

        log.info("checkClickSignKey signKey {}", signKey);
        if (!Objects.equals(clickDTO.getSign_string(), signKey))
            throw ClickException.signCheckFailed("Signature verification error");
    }

    public void checkTransactionCancelled(ClickOrder clickOrder) {
        log.info("completePayment clickOrder {}", clickOrder);

        if (Objects.nonNull(clickOrder.getPayment()) && Objects.equals(clickOrder.getPayment().getCancelled(), Boolean.TRUE)) {
            throw ClickException.transactionCancelled("Transaction cancelled");
        }
    }
}

