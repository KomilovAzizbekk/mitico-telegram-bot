package uz.mediasolutions.miticodeliverytelegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.mediasolutions.miticodeliverytelegrambot.entity.payme.OrderTransaction;
import uz.mediasolutions.miticodeliverytelegrambot.enums.TransactionState;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface OrderTransactionRepository extends JpaRepository<OrderTransaction, Long> {
    Optional<OrderTransaction> findByPaycomId(String id);

    List<OrderTransaction> findAllByStateAndCreateTimeBetween(TransactionState transactionState, Timestamp timestamp, Timestamp timestamp1);
}
