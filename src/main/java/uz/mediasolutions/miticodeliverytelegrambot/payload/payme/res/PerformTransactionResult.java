package uz.mediasolutions.miticodeliverytelegrambot.payload.payme.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PerformTransactionResult {

    private String transaction;

    @JsonProperty(value = "perform_time")
    private long performTime;

    private Integer state;

}
