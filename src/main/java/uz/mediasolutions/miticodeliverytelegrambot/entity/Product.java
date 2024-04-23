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
@SQLDelete(sql = "UPDATE products SET deleted=true WHERE id=?")
@Table(name = "products")
public class Product extends AbsDateDeleted {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "number")
    private Long number;

    @Column(nullable = false, name = "name_uz", unique = true)
    private String nameUz;

    @Column(nullable = false, name = "name_ru", unique = true)
    private String nameRu;

    @Column(name = "description_uz", columnDefinition = "text")
    private String descriptionUz;

    @Column(name = "description_ru", columnDefinition = "text")
    private String descriptionRu;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "active")
    private boolean active;

}
