package uz.mediasolutions.miticodeliverytelegrambot.controller.abs;

import net.minidev.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.mediasolutions.miticodeliverytelegrambot.payload.payme.PaycomRequestForm;
import uz.mediasolutions.miticodeliverytelegrambot.utills.constants.Rest;

@RequestMapping(PaymeController.PAYME)
public interface PaymeController {

    String PAYME = Rest.BASE_PATH + "payme/";

    @PostMapping
    JSONObject post(@RequestBody PaycomRequestForm requestForm,
                    @RequestHeader(value = "Authorization", required = false) String authorization);
}
