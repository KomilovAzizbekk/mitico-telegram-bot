package uz.mediasolutions.miticodeliverytelegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.mediasolutions.miticodeliverytelegrambot.entity.OrderStatus;
import uz.mediasolutions.miticodeliverytelegrambot.enums.OrderStatusName;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long> {

    OrderStatus findByName(OrderStatusName name);

}
