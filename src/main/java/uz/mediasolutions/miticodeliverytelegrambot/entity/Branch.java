package uz.mediasolutions.miticodeliverytelegrambot.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import uz.mediasolutions.miticodeliverytelegrambot.entity.template.AbsLong;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalTime;

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
@Table(name = "branches")
public class Branch extends AbsLong {

    @Column(name = "name_uz", unique = true)
    private String nameUz;

    @Column(name = "name_ru", unique = true)
    private String nameRu;

    @Column(name = "lon")
    private Double lon;

    @Column(name = "lat")
    private Double lat;

    @Column(name = "address_uz")
    private String addressUz;

    @Column(name = "address_ru")
    private String addressRu;

    @Column(name = "opening_time")
    private LocalTime openingTime;

    @Column(name = "closing_time")
    private LocalTime closingTime;

    @Column(name = "closes_after_mn")
    private boolean closesAfterMn;

    @Column(name = "active")
    private boolean active;

}
