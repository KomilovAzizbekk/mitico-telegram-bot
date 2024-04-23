package uz.mediasolutions.miticodeliverytelegrambot.entity;

import lombok.*;
import uz.mediasolutions.miticodeliverytelegrambot.entity.template.AbsDate;
import uz.mediasolutions.miticodeliverytelegrambot.entity.template.AbsDateDeleted;
import uz.mediasolutions.miticodeliverytelegrambot.entity.template.AbsLong;
import uz.mediasolutions.miticodeliverytelegrambot.enums.StepName;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "order_products")
public class OrderProducts extends AbsDateDeleted {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Variation variation;

    @Column(name = "count")
    private Integer count;

}
