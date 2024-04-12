package uz.mediasolutions.miticodeliverytelegrambot.controller.impl;

import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.RestController;
import uz.mediasolutions.miticodeliverytelegrambot.controller.abs.PaymeController;
import uz.mediasolutions.miticodeliverytelegrambot.payload.payme.PaycomRequestForm;
import uz.mediasolutions.miticodeliverytelegrambot.service.abs.PaymeService;

@RestController
@RequiredArgsConstructor
public class PaymeControllerImpl implements PaymeController {

    private final PaymeService paymeService;


    @Override
    public JSONObject post(PaycomRequestForm requestForm, String authorization) {
        return paymeService.payWithPayme(requestForm, authorization);
    }
}
