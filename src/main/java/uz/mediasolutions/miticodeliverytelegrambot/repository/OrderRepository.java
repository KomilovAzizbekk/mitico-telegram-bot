package uz.mediasolutions.miticodeliverytelegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.mediasolutions.miticodeliverytelegrambot.entity.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByUserChatIdOrderByCreatedAtDesc(String chatId);

}
