package uz.mediasolutions.miticodeliverytelegrambot.service.impl;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Error;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.minidev.json.JSONObject;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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
        if (order.getTotalPrice() != requestForm.getParams().getAmount()) {
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
        Optional<OrderTransaction> optionalTransaction = transactionRepository.findByPaycomId(requestForm.getParams().getId());

        if (optionalTransaction.isEmpty()) {
            if (checkPerformTransaction(requestForm, response)) {

                Optional<Order> orderOptional = orderRepository.findById(requestForm.getParams().getAccount().getOrder());
                if (orderOptional.isEmpty()) {
                    response.setError(new JSONRPC2Error(
                            -31050,
                            "Order not found",
                            "order"));
                    return;
                }

                OrderTransaction newTransaction = new OrderTransaction(requestForm.getParams().getId(), new Timestamp(requestForm.getParams().getTime()),
                        TransactionState.STATE_IN_PROGRESS, orderOptional.get());
                transactionRepository.save(newTransaction);
                response.setResult(new CreateTransactionResult(newTransaction.getCreateTime(), newTransaction.getId(), newTransaction.getState().getCode()));
                return;
            }
        } else {
            OrderTransaction transaction = optionalTransaction.get();
            if (transaction.getState() == TransactionState.STATE_IN_PROGRESS) {
                if (System.currentTimeMillis() - transaction.getPaycomTime().getTime() > time_expired) {
                    response.setError(new JSONRPC2Error(-31008, "Unable to complete operation", "transaction"));
                } else {
                    response.setResult(new CreateTransactionResult(transaction.getCreateTime(), transaction.getId(), transaction.getState().getCode()));
                }
            } else {
                response.setError(new JSONRPC2Error(-31008, "Unable to complete operation", "transaction"));
            }
            return;
        }
        response.setError(new JSONRPC2Error(-31008, "Unable to complete operation", "transaction"));
    }


    @SneakyThrows
    public void performTransaction(PaycomRequestForm requestForm, JSONRPC2Response response) {
        Optional<OrderTransaction> optionalTransaction = transactionRepository.findByPaycomId(requestForm.getParams().getId());
        if (optionalTransaction.isPresent()) {
            OrderTransaction transaction = optionalTransaction.get();
            if (transaction.getState() == TransactionState.STATE_IN_PROGRESS) {
                if (System.currentTimeMillis() - transaction.getPaycomTime().getTime() > time_expired) {
                    transaction.setState(TransactionState.STATE_CANCELED);
                    transactionRepository.save(transaction);
                    response.setError(new JSONRPC2Error(-31008, "Unable to complete operation", "transaction"));
                } else {
                    transaction.setState(TransactionState.STATE_DONE);
                    transaction.setPerformTime(new Timestamp(new Date().getTime()));
                    transactionRepository.save(transaction);

                    tgService.execute(tgService.whenSendOrderToChannelClickOrPayme(transaction.getOrder().getUser().getChatId()));
                    tgService.execute(tgService.whenSendOrderToUser(transaction.getOrder().getUser().getChatId()));

                    response.setResult(new PerformTransactionResult(transaction.getId(), transaction.getPerformTime(), transaction.getState().getCode()));
                }
            } else if (transaction.getState() == TransactionState.STATE_DONE) {
                response.setResult(new PerformTransactionResult(transaction.getId(), transaction.getPerformTime(), transaction.getState().getCode()));
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
            if (transaction.getState() == TransactionState.STATE_IN_PROGRESS) {
                transaction.setState(TransactionState.STATE_CANCELED);
            } else if (transaction.getState() == TransactionState.STATE_DONE) {
                if (transaction.getOrder().getOrderStatus().getName().equals(OrderStatusName.ACCEPTED)) {
                    response.setError(new JSONRPC2Error(-31007,
                            "Unable to cancel transaction",
                            "transaction"));
                    return;
                } else {
                    transaction.setState(TransactionState.STATE_POST_CANCELED);
                }
            } else {
                transaction.setState(TransactionState.STATE_CANCELED);
            }
            transaction.setCancelTime(new Timestamp(new Date().getTime()));
            transaction.setReason(requestForm.getParams().getReason());
            transactionRepository.save(transaction);

            response.setResult(new CancelTransactionResult(transaction.getId(), transaction.getCancelTime(), transaction.getState().getCode()));
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
            response.setResult(new CheckTransactionResult(transaction.getCreateTime(), transaction.getPerformTime(), transaction.getCancelTime(),
                    transaction.getId(), transaction.getState().getCode(), transaction.getReason() != null ? transaction.getReason() : null));
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

        for (OrderTransaction orderTransaction : orderTransactions) {
            Transactions transactions = new Transactions(
                    orderTransaction.getPaycomId(),
                    orderTransaction.getPaycomTime(),
                    (int) orderTransaction.getOrder().getTotalPrice(),
                    new Account(orderTransaction.getOrder().getId()),
                    orderTransaction.getCreateTime().getTime(),
                    orderTransaction.getPerformTime().getTime(),
                    orderTransaction.getCancelTime().getTime(),
                    orderTransaction.getId().toString(),
                    orderTransaction.getState().getCode(),
                    orderTransaction.getReason()
            );
            transactionsList.add(transactions);
        }

        response.setResult(new GetStatementResult(transactionsList));
    }

    @SneakyThrows
    private boolean checkPaycomUserAuth(String basicAuth, JSONRPC2Response response) {

        if (basicAuth == null) {
            response.setError(new JSONRPC2Error(-32504,
                    "Error authentication",
                    "auth"));
            return false;
        }

        basicAuth = basicAuth.substring("Basic".length()).trim();

        byte[] decode = Base64.getDecoder().decode(basicAuth);

        basicAuth = new String(decode, Charset.defaultCharset());

        String[] credentials = basicAuth.split(":", 2);

        Optional<Client> optionalClient = clientRepository.findByPhoneNumber("Paycom");

        if (optionalClient.isPresent()) {
            Client client = optionalClient.get();
            if (passwordEncoder.matches(credentials[1], client.getPassword())) {

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(client, null, new ArrayList<>());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                response.setError(new JSONRPC2Error(-32504,
                        "Error authentication",
                        "auth"));
                return false;
            }
        }
        return true;
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
