package uz.mediasolutions.miticodeliverytelegrambot.payload.payme.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckTransactionResult {

    private Date createTime;

    private Date performTime;

    private Date cancelTime;

    private Long transaction;

    private Integer state;

    private Integer reason;

}
