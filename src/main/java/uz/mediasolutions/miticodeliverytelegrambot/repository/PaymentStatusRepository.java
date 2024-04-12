package uz.mediasolutions.miticodeliverytelegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.mediasolutions.miticodeliverytelegrambot.entity.PaymentStatus;
import uz.mediasolutions.miticodeliverytelegrambot.enums.PaymentStatusName;

public interface PaymentStatusRepository extends JpaRepository<PaymentStatus, Long> {

    PaymentStatus findByName(PaymentStatusName name);

}
