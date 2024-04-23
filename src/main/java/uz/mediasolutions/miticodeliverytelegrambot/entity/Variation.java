package uz.mediasolutions.miticodeliverytelegrambot.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import uz.mediasolutions.miticodeliverytelegrambot.entity.template.AbsDate;
import uz.mediasolutions.miticodeliverytelegrambot.entity.template.AbsLong;

import javax.persistence.*;

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
@Table(name = "variations")
public class Variation extends AbsDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name_uz", nullable = false)
    private String nameUz;

    @Column(name = "name_ru", nullable = false)
    private String nameRu;

    @Column(name = "number", columnDefinition = "serial")
    private Long number;

    @Column(name = "active")
    private boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    private MeasureUnit measureUnit;

    @Column(name = "measure")
    private float measure;

    @Column(name = "price")
    private float price;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;
}
