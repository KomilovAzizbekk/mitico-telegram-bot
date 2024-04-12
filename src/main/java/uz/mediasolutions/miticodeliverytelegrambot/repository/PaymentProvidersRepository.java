package uz.mediasolutions.miticodeliverytelegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.mediasolutions.miticodeliverytelegrambot.entity.PaymentProviders;
import uz.mediasolutions.miticodeliverytelegrambot.enums.ProviderName;

public interface PaymentProvidersRepository extends JpaRepository<PaymentProviders, Long> {

    PaymentProviders findByName(ProviderName name);

}
