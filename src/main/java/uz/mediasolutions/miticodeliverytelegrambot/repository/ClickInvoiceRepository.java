package uz.mediasolutions.miticodeliverytelegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.mediasolutions.miticodeliverytelegrambot.entity.click.ClickInvoice;

public interface ClickInvoiceRepository extends JpaRepository<ClickInvoice, Long> {

}
