package uz.mediasolutions.miticodeliverytelegrambot.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import uz.mediasolutions.miticodeliverytelegrambot.entity.template.AbsDate;
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
@Table(name = "categories")
public class Category extends AbsDate {

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

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;

    @Column(name = "image_url")
    private String imageUrl;

}
