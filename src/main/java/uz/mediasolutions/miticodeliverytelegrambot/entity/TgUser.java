package uz.mediasolutions.miticodeliverytelegrambot.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
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
@Table(name = "tg_users")
public class TgUser extends AbsLong {

    @Column(name = "chat_id")
    private String chatId;

    @Column(name = "name")
    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "username")
    private String username;

    @ManyToOne(fetch = FetchType.LAZY)
    private Language language;

    @Column(name = "registered")
    private boolean registered;

    @Column(name = "admin")
    private boolean admin;

    @Column(name = "banned")
    private boolean banned;

    @ManyToOne(fetch = FetchType.LAZY)
    private Step step;

}
