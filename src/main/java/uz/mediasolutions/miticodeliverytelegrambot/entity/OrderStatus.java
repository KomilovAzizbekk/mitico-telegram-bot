package uz.mediasolutions.miticodeliverytelegrambot.entity;

import lombok.*;
import uz.mediasolutions.miticodeliverytelegrambot.enums.OrderStatusName;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "order_status")
public class OrderStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    private OrderStatusName name;

}
