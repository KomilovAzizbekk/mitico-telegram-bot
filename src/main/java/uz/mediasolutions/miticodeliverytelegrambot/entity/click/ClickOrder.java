package uz.mediasolutions.miticodeliverytelegrambot.entity.click;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.mediasolutions.miticodeliverytelegrambot.entity.template.AbsLong;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

/**
 * BU CLICKDAN KELGAN CLICK PREPARE OBJECTNI SAQLASH UCHUN
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "click_order")
public class ClickOrder extends AbsLong {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ClickInvoice invoice;

    @ManyToOne(fetch = FetchType.LAZY)
    private Payment payment;

    /**
     * 
     * ID of transaction (iteration) in CLICK system, i.e. attempt to make a payment.
     */
    @Column(name = "transaction_id", nullable = false)
    private Integer transactionId;

    /**
     * ID of the service
     */
    @Column(name = "service_id", nullable = false)
    private Integer serviceId;

    /**
     * Payment ID in CLICK system. Displayed to the customer in SMS when paying.
     */
    @Column(name = "paydoc_id", nullable = false)
    private Long paydocId;

    /**
     * Order ID (for online shopping) / personal account / login in the billing of the supplier
     */
    @Column(name = "merchant_transaction_id", nullable = false)
    private String merchantTransactionId;

    /**
     * Payment Amount (in soums)
     */
    @Column(name = "amount", nullable = false)
    private Double amount;


    @Column(name = "action", nullable = false)
    private Integer action;

    /**
     * Status code about completion of payment. 0 – successfully. In case of an error returns an error code.
     */
    @Column(name = "error_code")
    private Integer error;

    /**
     * Identification of the code to complete the payment.
     */
    @Column(name = "error_note")
    private String errorNote;
}
