package uz.mediasolutions.miticodeliverytelegrambot.entity.click;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.mediasolutions.miticodeliverytelegrambot.entity.template.AbsLong;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "payment")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Payment extends AbsLong implements Cloneable {

    // QAYSI INVOICEGA TEGISHLI EKANLIGI
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "invoice_id")
    private ClickInvoice invoice;

    // INVOICENI UMUMIY SUMMASI. { discountPrice } DAGI SUMMA
    private Double invoicePrice;

    // QANCHA PUL TO'LANGANI
    private Double paidAmount;

    // TO'LANGAN PULDAN QOLGAN SUMMA.
    // AGAR QOLDIQ QQOLGAN BO'LSA YOZILADI QOLDIQ QOLMASA 0 BO'LADI
    private Double leftoverAmount;

    private Date date = new Date();

    private Double percent = 0D;

    private Boolean cancelled;

    private String cancelDescription;

    private String dateTime;

    public Payment(ClickInvoice invoice, Double invoicePrise, Double paidAmount, Double leftoverAmount, UUID cashierId, Date date) {
        this.invoice = invoice;
        this.paidAmount = paidAmount;
        this.leftoverAmount = leftoverAmount;
        this.date = date;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
