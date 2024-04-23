package uz.mediasolutions.miticodeliverytelegrambot.payload.payme.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckTransactionResult {

    @JsonProperty(value = "create_time")
    private long createTime;

    @JsonProperty(value = "perform_time")
    private long performTime;

    @JsonProperty(value = "cancel_time")
    private long cancelTime;

    private String transaction;

    private Integer state;

    private Integer reason;
}
