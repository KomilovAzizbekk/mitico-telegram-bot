package uz.mediasolutions.miticodeliverytelegrambot.payload.click;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClickOrderParamDTO implements Serializable {
    @JsonProperty(value = "fullName")
    private String fullName;

    @JsonProperty(value = "phoneNum")
    private String phoneNum;

    @JsonProperty(value = "invoice")
    private String invoice;

    @JsonProperty(value = "amount")
    private Double amount;

    @JsonProperty(value = "price")
    private Double price;

    @JsonProperty(value = "leftAmount")
    private Double leftAmount;

    @JsonProperty(value = "merchant_trans_id")
    private String merchantTransId;

}
