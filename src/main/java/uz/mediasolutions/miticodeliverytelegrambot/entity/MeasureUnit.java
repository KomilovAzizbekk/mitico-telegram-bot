package uz.mediasolutions.miticodeliverytelegrambot.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import uz.mediasolutions.miticodeliverytelegrambot.entity.template.AbsDate;
import uz.mediasolutions.miticodeliverytelegrambot.entity.template.AbsDateDeleted;
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
@Where(clause = "deleted=false")
@SQLDelete(sql = "UPDATE measure_units SET deleted=true WHERE id=?")
@Table(name = "measure_units")
public class MeasureUnit extends AbsDateDeleted {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name_uz", unique = true)
    private String nameUz;

    @Column(name = "name_ru", unique = true)
    private String nameRu;

}
