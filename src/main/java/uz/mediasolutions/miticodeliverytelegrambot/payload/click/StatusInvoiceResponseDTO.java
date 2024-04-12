package uz.mediasolutions.miticodeliverytelegrambot.payload.click;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusInvoiceResponseDTO {

    @JsonProperty("error_code")
    private Integer errorCode;

    @JsonProperty("error_note")
    private String errorNote;

    @JsonProperty("invoice_status")
    private Long invoiceStatus;

    @JsonProperty("invoice_status_note")
    private String invoiceStatusNote;

}
