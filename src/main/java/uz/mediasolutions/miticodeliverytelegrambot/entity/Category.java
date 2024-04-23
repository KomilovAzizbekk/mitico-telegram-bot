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
@SQLDelete(sql = "UPDATE categories SET deleted=true WHERE id=?")
@Table(name = "categories")
public class Category extends AbsDateDeleted {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number", nullable = false)
    private Long number;

    @Column(name = "name_uz", nullable = false, unique = true)
    private String nameUz;

    @Column(name = "name_ru", nullable = false, unique = true)
    private String nameRu;

    @Column(name = "description_uz", columnDefinition = "text")
    private String descriptionUz;

    @Column(name = "description_ru", columnDefinition = "text")
    private String descriptionRu;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "image_url")
    private String imageUrl;

}
