package uz.mediasolutions.miticodeliverytelegrambot.payload.click;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentStatusResponseDTO {

    @JsonProperty("error_code")
    private int errorCode;

    @JsonProperty("error_note")
    private String errorNote;

    @JsonProperty("payment_id")
    private long paymentId;

    @JsonProperty("merchant_trans_id")
    private String merchantTransId;

}
