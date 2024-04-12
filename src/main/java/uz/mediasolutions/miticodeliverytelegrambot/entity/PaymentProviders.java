package uz.mediasolutions.miticodeliverytelegrambot.entity;

import lombok.*;
import uz.mediasolutions.miticodeliverytelegrambot.enums.ProviderName;
import uz.mediasolutions.miticodeliverytelegrambot.enums.StepName;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "payment_providers")
public class PaymentProviders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    private ProviderName name;

    @Column(name = "active")
    private boolean active;

}
