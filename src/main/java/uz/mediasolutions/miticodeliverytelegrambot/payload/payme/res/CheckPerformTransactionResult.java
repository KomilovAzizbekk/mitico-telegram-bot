package uz.mediasolutions.miticodeliverytelegrambot.payload.payme.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckPerformTransactionResult {

    private boolean allow;

}
