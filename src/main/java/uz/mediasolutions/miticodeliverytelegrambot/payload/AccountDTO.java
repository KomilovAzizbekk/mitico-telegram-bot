package uz.mediasolutions.miticodeliverytelegrambot.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AccountDTO {

    @JsonProperty(value = "phone")
    private String phone;

}
