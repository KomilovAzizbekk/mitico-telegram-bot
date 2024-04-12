package uz.mediasolutions.miticodeliverytelegrambot.payload.click;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResClickOrderDTO {

    private String merchantId;
    private String serviceId;
    private String transactionParam;
    private Double amount;
    private String paymentUrl;

    public ResClickOrderDTO(String merchantId, String serviceId, String transactionParam, Double amount) {
        this.merchantId = merchantId;
        this.serviceId = serviceId;
        this.transactionParam = transactionParam;
        this.amount = amount;
    }

    public String getPaymentUrl() {
//        https://my.click.uz/services/pay?service_id=25996&merchant_id=29563196&amount=1000&transaction_param=19&return_url=http://incrm.uz&card_type=UZCARD

        return String.format("https://my.click.uz/services/pay?service_id=%s&merchant_id=%s&amount=%s&transaction_param=%s", serviceId, merchantId, amount, transactionParam);
    }


}
