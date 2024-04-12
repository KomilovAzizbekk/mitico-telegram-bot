package uz.mediasolutions.miticodeliverytelegrambot.payload.payme;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Transactions {

    private String id;

    private Timestamp time;

    private Integer amount;

    private Account account;

    @JsonProperty(value = "cancel_time")
    private Long cancelTime;

    @JsonProperty(value = "create_time")
    private Long createTime;

    @JsonProperty(value = "perform_time")
    private Long performTime;

    private String transaction;

    private Integer state;

    private Integer reason;

}
