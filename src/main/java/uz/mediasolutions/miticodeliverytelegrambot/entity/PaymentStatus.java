package uz.mediasolutions.miticodeliverytelegrambot.entity;

import lombok.*;
import uz.mediasolutions.miticodeliverytelegrambot.enums.PaymentStatusName;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "payment_status")
public class PaymentStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    private PaymentStatusName name;

}
