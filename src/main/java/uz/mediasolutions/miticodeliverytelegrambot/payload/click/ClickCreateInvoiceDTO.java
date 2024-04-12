package uz.mediasolutions.miticodeliverytelegrambot.payload.click;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClickCreateInvoiceDTO {

    @JsonProperty(value = "service_id")
    private String serviceId;

    @NotNull
    @JsonProperty(value = "amount")
    private Float amount;

    @JsonProperty(value = "phone_number")
    private String phoneNumber;

    @JsonProperty(value = "merchant_trans_id")
    private String merchantTransId;
}
