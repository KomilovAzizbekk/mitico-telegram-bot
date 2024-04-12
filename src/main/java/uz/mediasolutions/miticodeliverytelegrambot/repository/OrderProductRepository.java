package uz.mediasolutions.miticodeliverytelegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.mediasolutions.miticodeliverytelegrambot.entity.OrderProducts;

public interface OrderProductRepository extends JpaRepository<OrderProducts, Long> {


}
