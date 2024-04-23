package uz.mediasolutions.miticodeliverytelegrambot.payload.payme.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTransactionResult {

    @JsonProperty(value = "create_time")
    private long createTime;

    private String transaction;

    private Integer state;

}
