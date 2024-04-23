package uz.mediasolutions.miticodeliverytelegrambot.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uz.mediasolutions.miticodeliverytelegrambot.entity.Order;
import uz.mediasolutions.miticodeliverytelegrambot.entity.Transaction;
import uz.mediasolutions.miticodeliverytelegrambot.enums.PaymentStatusName;
import uz.mediasolutions.miticodeliverytelegrambot.exceptions.RestException;
import uz.mediasolutions.miticodeliverytelegrambot.repository.OrderRepository;
import uz.mediasolutions.miticodeliverytelegrambot.repository.PaymentStatusRepository;
import uz.mediasolutions.miticodeliverytelegrambot.repository.TransactionRepository;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl {

    private final TransactionRepository transactionRepository;
    private final OrderRepository orderRepository;
    private final PaymentStatusRepository paymentStatusRepository;

    public Transaction createTransaction(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> RestException.restThrow("ORDER ID NOT FOUND", HttpStatus.BAD_REQUEST));
        Transaction transaction = Transaction.builder()
                .sum(order.getTotalPrice())
                .paymentProvider(order.getPaymentProviders())
                .order(order)
                .build();
        return transactionRepository.save(transaction);
    }

}
