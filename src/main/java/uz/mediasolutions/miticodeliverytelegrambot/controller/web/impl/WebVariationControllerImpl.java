package uz.mediasolutions.miticodeliverytelegrambot.controller.web.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import uz.mediasolutions.miticodeliverytelegrambot.controller.web.abs.WebVariationController;
import uz.mediasolutions.miticodeliverytelegrambot.manual.ApiResult;
import uz.mediasolutions.miticodeliverytelegrambot.payload.VariationWebDTO;
import uz.mediasolutions.miticodeliverytelegrambot.service.webabs.WebVariationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WebVariationControllerImpl implements WebVariationController {

    private final WebVariationService variationWebService;

    @Override
    public ApiResult<List<VariationWebDTO>> getAllByProductId(String chatId, Long productId) {
        return variationWebService.getAllByProductId(chatId, productId);
    }
}
