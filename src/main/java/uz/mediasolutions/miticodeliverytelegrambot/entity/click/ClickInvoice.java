package uz.mediasolutions.miticodeliverytelegrambot.entity.click;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import uz.mediasolutions.miticodeliverytelegrambot.entity.TgUser;
import uz.mediasolutions.miticodeliverytelegrambot.entity.template.AbsLong;
import uz.mediasolutions.miticodeliverytelegrambot.enums.InvoiceStatusEnum;

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
@Table(name = "click_invoices")
public class ClickInvoice extends AbsLong {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private TgUser user;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "some_id")
    private String someId  ;

    private Double price = 0d;

    private Double paidAmount = 0d;

    // INVOICE NING HOLATI
    @Column(name = "invoice_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private InvoiceStatusEnum status;

    // TO'LANGAN PULDAN QOLGAN SUMMA.
    // AGAR QOLDIQ QQOLGAN BO'LSA YOZILADI QOLDIQ QOLMASA 0 BO'LADI
    @Column(name = "left_amount")
    private Double leftoverAmount = 0d;

    private String type;

}
