package uz.mediasolutions.miticodeliverytelegrambot.payload.payme.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PerformTransactionResult {

    private Long transaction;

    private Date performTime;

    private Integer state;

}
