package uz.mediasolutions.miticodeliverytelegrambot.entity.payme;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.mediasolutions.miticodeliverytelegrambot.entity.Order;
import uz.mediasolutions.miticodeliverytelegrambot.enums.TransactionState;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "order_transaction")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderTransaction {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String paycomId;

    private Timestamp paycomTime;

    private Timestamp createTime;

    private Timestamp performTime;

    private Timestamp cancelTime;

    private Integer reason;

    @Enumerated(EnumType.STRING)
    private TransactionState state;

    @JoinColumn(insertable = false, updatable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private Order order;

    @Column(name = "order_id")
    private Long orderId;


    public OrderTransaction(String paycomId, Timestamp paycomTime, Timestamp createTime, TransactionState transactionState, Long orderId) {
        this.paycomId = paycomId;
        this.paycomTime = paycomTime;
        this.createTime = createTime;
        this.state = transactionState;
        this.orderId = orderId;
    }
}
