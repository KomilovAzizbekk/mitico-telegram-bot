package uz.mediasolutions.miticodeliverytelegrambot.service.abs;

import net.minidev.json.JSONObject;
import uz.mediasolutions.miticodeliverytelegrambot.payload.payme.PaycomRequestForm;

public interface PaymeService {

    JSONObject payWithPayme(PaycomRequestForm requestForm, String authorization);
}
