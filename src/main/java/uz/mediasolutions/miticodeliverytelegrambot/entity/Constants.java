package uz.mediasolutions.miticodeliverytelegrambot.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "constants")
public class Constants {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "min_order_price", nullable = false)
    private float minOrderPrice;

    @Column(name = "min_delivery_price")
    private float minDeliveryPrice;

    @Column(name = "price_per_kilometer")
    private float pricePerKilometer;

    @Column(name = "radius_free_delivery")
    private float radiusFreeDelivery;

    @Column(name = "min_order_price_for_free_delivery")
    private float minOrderPriceForFreeDelivery;

    @Column(name = "bot_working")
    private Integer botWorking;

}
