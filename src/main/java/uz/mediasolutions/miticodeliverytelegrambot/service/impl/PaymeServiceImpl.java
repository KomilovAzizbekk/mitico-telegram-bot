package uz.mediasolutions.miticodeliverytelegrambot.service.impl;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Error;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.minidev.json.JSONObject;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.mediasolutions.miticodeliverytelegrambot.entity.Order;
import uz.mediasolutions.miticodeliverytelegrambot.entity.payme.Client;
import uz.mediasolutions.miticodeliverytelegrambot.entity.payme.OrderTransaction;
import uz.mediasolutions.miticodeliverytelegrambot.enums.OrderStatusName;
import uz.mediasolutions.miticodeliverytelegrambot.enums.TransactionState;
import uz.mediasolutions.miticodeliverytelegrambot.payload.payme.Account;
import uz.mediasolutions.miticodeliverytelegrambot.payload.payme.PaycomRequestForm;
import uz.mediasolutions.miticodeliverytelegrambot.payload.payme.Transactions;
import uz.mediasolutions.miticodeliverytelegrambot.payload.payme.res.*;
import uz.mediasolutions.miticodeliverytelegrambot.repository.ClientRepository;
import uz.mediasolutions.miticodeliverytelegrambot.repository.OrderRepository;
import uz.mediasolutions.miticodeliverytelegrambot.repository.OrderTransactionRepository;
import uz.mediasolutions.miticodeliverytelegrambot.service.abs.PaymeService;
import uz.mediasolutions.miticodeliverytelegrambot.service.webimpl.TgService;

import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymeServiceImpl implements PaymeService {
    private final long time_expired = 43_200_000L;

    private final OrderRepository orderRepository;
    private final OrderTransactionRepository transactionRepository;
    private final TgService tgService;
    private final PasswordEncoder passwordEncoder;
    private final ClientRepository clientRepository;


    public boolean checkPerformTransaction(PaycomRequestForm requestForm, JSONRPC2Response response) {

        //PAYCOMDAN ACOUNT FIELDI KELMASA
        if (requestForm.getParams().getAccount() == null) {
            response.setError(new JSONRPC2Error(
                    -31050,
                    "Account field not found",
                    "account"
            ));
            return false;
        }

        //PAYCOMDAN ACCOUNTDA ORDER FIELDI KELMASA
        if (requestForm.getParams().getAccount().getOrder() == null) {
            response.setError(new JSONRPC2Error(
                    -31050,
                    "Account order not found",
                    "order"
            ));
            return false;
        }

        //PAYCOMDAN AMOUNT FIELDI NULL YOKI 0 KELSA
        if (requestForm.getParams().getAmount() == null || requestForm.getParams().getAmount() == 0) {
            response.setError(new JSONRPC2Error(
                    -31001,
                    "Amount error or null",
                    "amount"));
            return false;
        }

        //PAYCOM DAN KELGAN ORDER ID ORQALI ORDERNI OLAMIZ
        Optional<Order> optionalOrder = orderRepository.findById(requestForm.getParams().getAccount().getOrder());

        //AGAR ORDER BO'LSA
        if (optionalOrder.isEmpty()) {
            response.setError(new JSONRPC2Error(
                    -31050,
                    "Order not found",
                    "order"));
            return false;
        }

        //ORDER SUM BILAN PAYCOMDAN KELGAN SUM TENGLIGI TEKSHIRILYAPTI
        Order order = optionalOrder.get();
        if (((int) order.getTotalPrice()) * 100 != requestForm.getParams().getAmount()) {
            response.setError(new JSONRPC2Error(
                    -31001,
                    "Wrong amount",
                    "amount"));
            return false;
        }


        //ORDER ALLAQACHON YAKUNLANAGAN BO'LSA
        if (order.getOrderStatus().getName().equals(OrderStatusName.ACCEPTED)) {
            response.setError(new JSONRPC2Error(
                    -31099,
                    "Order already finished",
                    "order"));
            return false;
        }

        response.setResult(new CheckPerformTransactionResult(true));
        return true;
    }


    public void createTransaction(PaycomRequestForm requestForm, JSONRPC2Response response) {

        Optional<Order> orderOptional = orderRepository.findById(requestForm.getParams().getAccount().getOrder());

        if (orderOptional.isEmpty()) {
            response.setError(new JSONRPC2Error(
                    -31050,
                    "Order not found",
                    "order"));
            return;
        }

        if (requestForm.getParams().getAmount() != ((int) orderOptional.get().getTotalPrice()) *100) {
            response.setError(new JSONRPC2Error(
                    -31001,
                    "Wrong amount",
                    "amount"));
            return;
        }

        Optional<OrderTransaction> optionalTransaction = transactionRepository.findByPaycomId(requestForm.getParams().getId());
        if (optionalTransaction.isEmpty()) {
            if (checkPerformTransaction(requestForm, response)) {
                if (transactionRepository.existsByOrderId(requestForm.getParams().getAccount().getOrder())) {
                    response.setError(new JSONRPC2Error(
                            -31099,
                            "Transaction is already processing",
                            "transaction"
                    ));
                    return;
                }
                OrderTransaction orderTransaction = new OrderTransaction(requestForm.getParams().getId(),
                        new Timestamp(requestForm.getParams().getTime()),
                        new Timestamp(requestForm.getParams().getTime()),
                        TransactionState.STATE_IN_PROGRESS, orderOptional.get().getId());
                transactionRepository.save(orderTransaction);
                response.setResult(new CreateTransactionResult(orderTransaction.getCreateTime().getTime(), String.valueOf(orderTransaction.getId()), orderTransaction.getState().getCode()));
                return;
            }
        } else {
            OrderTransaction transaction = optionalTransaction.get();
            if (transactionRepository.existsByPaycomIdAndOrderId(requestForm.getParams().getId(), orderOptional.get().getId())) {
                response.setResult(new CreateTransactionResult(transaction.getCreateTime().getTime(), String.valueOf(transaction.getId()), transaction.getState().getCode()));
                return;
            }
            if (transaction.getState() == TransactionState.STATE_IN_PROGRESS) {
                if (System.currentTimeMillis() - transaction.getPaycomTime().getTime() > time_expired) {
                    transaction.setState(TransactionState.STATE_CANCELED);
                    transaction.setReason(4);
                    transactionRepository.save(transaction);
                    response.setError(new JSONRPC2Error(-31008, "Unable to complete operation", "transaction"));
                } else {
                    response.setError(new JSONRPC2Error(
                            -31099,
                            "Transaction is already processing",
                            "transaction"
                    ));
                    return;
                }
            } else {
                response.setError(new JSONRPC2Error(-31008, "Unable to complete operation", "transaction"));
            }
            return;
        }
        response.setError(new JSONRPC2Error(-31008, "Unable to complete operation", "transaction"));
    }


    public void performTransaction(PaycomRequestForm requestForm, JSONRPC2Response response) {
        Optional<OrderTransaction> optionalTransaction = transactionRepository.findByPaycomId(requestForm.getParams().getId());
        if (optionalTransaction.isPresent()) {
            OrderTransaction transaction = optionalTransaction.get();
            if (transaction.getState() == TransactionState.STATE_IN_PROGRESS) {
                if (System.currentTimeMillis() - transaction.getPaycomTime().getTime() > time_expired) {
                    transaction.setState(TransactionState.STATE_CANCELED);
                    transactionRepository.save(transaction);
                    response.setError(new JSONRPC2Error(-31008,
                            "Unable to complete operation",
                            "transaction"));
                } else {
                    transaction.setState(TransactionState.STATE_DONE);
                    transaction.setPerformTime(new Timestamp(System.currentTimeMillis()));
                    transactionRepository.save(transaction);

                    try {
                        tgService.execute(tgService.whenSendOrderToUserPayme(transaction.getOrder().getUser().getChatId(),
                                transaction.getOrder()));
                        tgService.execute(tgService.whenSendOrderToChannelPayme(transaction.getOrder().getUser().getChatId(),
                                transaction.getOrder()));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    response.setResult(new PerformTransactionResult(String.valueOf(transaction.getId()), transaction.getPerformTime().getTime(), transaction.getState().getCode()));
                }
            } else if (transaction.getState() == TransactionState.STATE_DONE) {
                response.setResult(new PerformTransactionResult(String.valueOf(transaction.getId()), transaction.getPerformTime().getTime(), transaction.getState().getCode()));
            } else {
                response.setError(new JSONRPC2Error(-31008, "Unable to complete operation", "transaction"));
            }
        } else {
            response.setError(new JSONRPC2Error(-31003, "Transaction not found", "transaction"));
        }
    }

    public void cancelTransaction(PaycomRequestForm requestForm, JSONRPC2Response response) {
        Optional<OrderTransaction> optionalTransaction = transactionRepository.findByPaycomId(requestForm.getParams().getId());
        if (optionalTransaction.isPresent()) {
            OrderTransaction transaction = optionalTransaction.get();

            if (transaction.getState().equals(TransactionState.STATE_POST_CANCELED) ||
                    transaction.getState().equals(TransactionState.STATE_CANCELED)) {
                response.setResult(new CancelTransactionResult(String.valueOf(transaction.getId()),
                        transaction.getCancelTime().getTime(),
                        transaction.getState().getCode()));
                return;
            }

            if (transaction.getState() == TransactionState.STATE_IN_PROGRESS) {
                transaction.setState(TransactionState.STATE_CANCELED);
            } else if (transaction.getState() == TransactionState.STATE_DONE) {
                transaction.setState(TransactionState.STATE_POST_CANCELED);
            } else {
                transaction.setState(TransactionState.STATE_CANCELED);
            }
            transaction.setCancelTime(new Timestamp(new Date().getTime()));
            transaction.setReason(requestForm.getParams().getReason());
            transactionRepository.save(transaction);

            response.setResult(new CancelTransactionResult(String.valueOf(transaction.getId()),
                    transaction.getCancelTime().getTime(),
                    transaction.getState().getCode()));
        } else {
            response.setError(new JSONRPC2Error(-31003,
                    "Transaction not found",
                    "transaction"));
        }
    }


    public void checkTransaction(PaycomRequestForm requestForm, JSONRPC2Response response) {
        Optional<OrderTransaction> optionalTransaction = transactionRepository.findByPaycomId(requestForm.getParams().getId());
        if (optionalTransaction.isPresent()) {
            OrderTransaction transaction = optionalTransaction.get();
            response.setResult(new CheckTransactionResult(
                    transaction.getCreateTime() == null ? 0 : transaction.getCreateTime().getTime(),
                    transaction.getPerformTime() == null ? 0 : transaction.getPerformTime().getTime(),
                    transaction.getCancelTime() == null ? 0 : transaction.getCancelTime().getTime(),
                    String.valueOf(transaction.getId()), transaction.getState().getCode(), transaction.getReason() != null ? transaction.getReason() : null));
        } else {
            response.setError(new JSONRPC2Error(-31003,
                    "Transaction not found",
                    "transaction"));
        }
    }

    public void getStatement(PaycomRequestForm requestForm, JSONRPC2Response response) {
        List<Transactions> transactionsList = new ArrayList<>();
        List<OrderTransaction> orderTransactions = transactionRepository.findAllByStateAndCreateTimeBetween(TransactionState.STATE_DONE,
                new Timestamp(requestForm.getParams().getFrom()),
                new Timestamp(requestForm.getParams().getTo()));

        if (orderTransactions == null || orderTransactions.isEmpty()) {
            response.setResult(new GetStatementResult());
        } else {
            for (OrderTransaction orderTransaction : orderTransactions) {
                Transactions transactions = new Transactions(
                        orderTransaction.getPaycomId(),
                        orderTransaction.getPaycomTime(),
                        (long) orderTransaction.getOrder().getTotalPrice(),
                        new Account(orderTransaction.getOrder().getId()),
                        orderTransaction.getCancelTime() != null ? orderTransaction.getCancelTime().getTime() : 0,
                        orderTransaction.getCreateTime().getTime(),
                        orderTransaction.getPerformTime().getTime(),
                        orderTransaction.getId().toString(),
                        orderTransaction.getState().getCode(),
                        orderTransaction.getReason()
                );
                transactionsList.add(transactions);
            }

            response.setResult(new GetStatementResult(transactionsList));
        }
    }

    @SneakyThrows
    private boolean checkPaycomUserAuth(String basicAuth, JSONRPC2Response response) {

        if (basicAuth == null) {
            response.setError(new JSONRPC2Error(-32504,
                    "Error authentication",
                    "auth"));
            return true;
        }

        basicAuth = basicAuth.substring("Basic".length()).trim();

        byte[] decode = Base64.getDecoder().decode(basicAuth);

        basicAuth = new String(decode, Charset.defaultCharset());

        String[] credentials = basicAuth.split(":", 2);

        Optional<Client> optionalClient = clientRepository.findByPhoneNumber("Paycom");

        if (optionalClient.isPresent()) {
            Client client = optionalClient.get();
            if (!passwordEncoder.matches(credentials[1], client.getPassword())) {
                response.setError(new JSONRPC2Error(-32504,
                        "Error authentication",
                        "auth"));
                return true;
            }
        }
        return false;
    }

    @Override
    public JSONObject payWithPayme(PaycomRequestForm requestForm, String authorization) {

        JSONRPC2Response response = new JSONRPC2Response(requestForm.getId());

        //BASIC AUTH BO'SH BO'LSA YOKI XATO KELGAN BO'LSA ERROR RESPONSE BERAMIZ
        if (checkPaycomUserAuth(authorization, response)) {
            return response.toJSONObject();
        }

        //PAYCOM QAYSI METHODDA KELAYOTGANLIGIGA QARAB ISH BAJARAMIZ
        switch (requestForm.getMethod()) {
            case "CheckPerformTransaction":
                checkPerformTransaction(requestForm, response);
                break;
            case "CreateTransaction":
                createTransaction(requestForm, response);
                break;
            case "PerformTransaction":
                performTransaction(requestForm, response);
                break;
            case "CancelTransaction":
                cancelTransaction(requestForm, response);
                break;
            case "CheckTransaction":
                checkTransaction(requestForm, response);
                break;
            case "GetStatement":
                getStatement(requestForm, response);
                break;
        }

        return response.toJSONObject();
    }
}
