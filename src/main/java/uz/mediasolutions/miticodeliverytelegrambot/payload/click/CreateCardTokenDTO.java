package uz.mediasolutions.miticodeliverytelegrambot.payload.click;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCardTokenDTO {

    @JsonProperty("service_id")
    private int serviceId;

    @JsonProperty("card_number")
    private String cardNumber;

    @JsonProperty("expire_date")
    private long expireDate;

    @JsonProperty("temporary")
    private byte temporary;

}
