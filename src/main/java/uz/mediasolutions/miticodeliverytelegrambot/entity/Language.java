package uz.mediasolutions.miticodeliverytelegrambot.entity;

import lombok.*;
import uz.mediasolutions.miticodeliverytelegrambot.enums.LanguageName;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "languages")
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    private LanguageName name;

}
