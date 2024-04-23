package uz.mediasolutions.miticodeliverytelegrambot.payload.payme.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CancelTransactionResult {

    private String transaction;

    @JsonProperty(value = "cancel_time")
    private long cancelTime;

    private Integer state;

}
