package uz.mediasolutions.miticodeliverytelegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.mediasolutions.miticodeliverytelegrambot.entity.click.ClickOrder;

public interface ClickOrderRepository extends JpaRepository<ClickOrder, Long> {
}