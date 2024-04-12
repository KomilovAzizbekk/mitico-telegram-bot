package uz.mediasolutions.miticodeliverytelegrambot.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import uz.mediasolutions.miticodeliverytelegrambot.entity.template.AbsLong;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode(callSuper = true)
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "orders")
public class Order extends AbsLong {

    @ManyToOne(fetch = FetchType.LAZY)
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    private OrderStatus orderStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    private TgUser user;

    @Column(name = "phone_number")
    private String phoneNumber;

    //USER'S LOCATION
    @Column(name = "lon")
    private Double lon;

    @Column(name = "lat")
    private Double lat;

    @OneToMany(fetch = FetchType.LAZY)
    private List<OrderProducts> orderProducts;

    @ManyToOne(fetch = FetchType.LAZY)
    private PaymentProviders paymentProviders;

    @Column(name = "comment", columnDefinition = "text")
    private String comment;

    @Column(name = "price")
    private float price;

    @Column(name = "delivery_price")
    private float deliveryPrice;

    @Column(name = "total_price")
    private float totalPrice;

    @Column(name = "paid_sum")
    private float paidSum = 0;

    @OneToOne(fetch = FetchType.LAZY)
    private Transaction transaction;

    @Column(name = "delivery")
    private boolean delivery;

}
