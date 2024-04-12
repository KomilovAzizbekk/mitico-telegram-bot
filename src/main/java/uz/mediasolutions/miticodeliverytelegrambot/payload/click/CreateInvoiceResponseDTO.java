package uz.mediasolutions.miticodeliverytelegrambot.payload.click;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateInvoiceResponseDTO {

    @JsonProperty("error_code")
    private Integer errorCode;

    @JsonProperty("error_note")
    private String errorNote;

    @JsonProperty("invoice_id")
    private Long invoiceId;

}
