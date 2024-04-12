package uz.mediasolutions.miticodeliverytelegrambot.payload.click;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import uz.mediasolutions.miticodeliverytelegrambot.exceptions.ClickException;

import java.io.Serializable;

/**
 * This class not documented :(
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClickOrderDTO implements Serializable {

    /**
     * ID of transaction (iteration) in CLICK system, i.e. attempt to make a payment.
     */
    @JsonProperty(value = "click_trans_id")
    private Long click_trans_id;

    /**
     * ID of the service
     */
    @JsonProperty(value = "service_id")
    private Integer service_id;

    /**
     * Payment ID in CLICK system. Displayed to the customer in SMS when paying.
     */
    @JsonProperty(value = "click_paydoc_id")
    private Long click_paydoc_id;

    /**
     * Order ID (for online shopping) / personal account / login in the billing of the supplier
     */
    @JsonProperty(value = "merchant_trans_id")
    private String merchant_trans_id;

    /**
     * Payment ID in the billing system of the supplier
     */
    @JsonProperty(value = "merchant_prepare_id")
    private Long merchant_prepare_id;

    /**
     * For Response. Transaction ID to complete the payment in the billing system (may be NULL)
     */
    @JsonProperty(value = "merchant_confirm_id")
    private Long merchant_confirm_id;

    /**
     * Payment Amount (in soums)
     */
    @JsonProperty(value = "amount")
    private Float amount;

    /**
     * An action to perform. 1 – for Comlete
     */
    @JsonProperty(value = "action")
    private Integer action;

    /**
     * Status code about completion of payment. 0 – successfully. In case of an error returns an error code.
     */
    @JsonProperty(value = "error")
    private Integer error;

    /**
     * Identification of the code to complete the payment.
     */
    @JsonProperty(value = "error_note")
    private String error_note;

    /**
     * Payment date. Format is «YYYY-MM-DD HH:mm:ss»
     */
    @JsonProperty(value = "sign_time")
    private String sign_time;

    /**
     * TestString confirming the authenticity of the submitted query. MD5 hash from the following options: <br/>
     * <PRE>
     * md5(click_trans_id + service_id + SECRET_KEY * + merchant_trans_id + amount + action + sign_time)
     * </PRE>
     * <b>SECRET_KEY – </b>a unique string issued to a supplier when connecting.
     */
    @JsonProperty(value = "sign_string")
    private String sign_string;

    @JsonProperty(value = "params")
    private ClickOrderParamDTO params;

    public ClickOrderDTO(ClickException ex) {
        this.error = ex.getErrorCode();
        this.error_note = ex.getErrorNote();
    }

    public String generatePrepareSignString(String secretKey) {
        return DigestUtils.md5Hex(
                String.valueOf(this.click_trans_id) +
                        String.valueOf(service_id) +
                        String.valueOf(secretKey) +
                        String.valueOf(merchant_trans_id) +
                        String.valueOf(Math.round(amount)) +
                        String.valueOf(action) +
                        String.valueOf(sign_time));
    }

    public String generateCompleteSignString(String secretKey) {
        return DigestUtils.md5Hex(
                String.valueOf(this.click_trans_id) +
                        String.valueOf(service_id) +
                        String.valueOf(secretKey) +
                        String.valueOf(merchant_trans_id) +
                        String.valueOf(merchant_prepare_id) +
                        String.valueOf(Math.round(amount)) +
                        String.valueOf(action) +
                        String.valueOf(sign_time));
    }

    public ClickOrderDTO(Long click_trans_id, String merchant_trans_id, Long merchant_prepare_id, Integer error, String error_note) {
        this.click_trans_id = click_trans_id;
        this.merchant_trans_id = merchant_trans_id;
        this.merchant_prepare_id = merchant_prepare_id;
        this.error = error;
        this.error_note = error_note;
    }

    public ClickOrderDTO(Long click_trans_id, String merchant_trans_id, Integer error, Long merchant_confirm_id, Long merchant_prepare_id, String error_note) {
        this.click_trans_id = click_trans_id;
        this.merchant_trans_id = merchant_trans_id;
        this.merchant_confirm_id = merchant_confirm_id;
        this.merchant_prepare_id = merchant_prepare_id;
        this.error = error;
        this.error_note = error_note;
    }

    public ClickOrderDTO(Integer action, Integer error, String error_note, ClickOrderParamDTO params) {
        this.action = action;
        this.error = error;
        this.error_note = error_note;
        this.params = params;
    }
}
