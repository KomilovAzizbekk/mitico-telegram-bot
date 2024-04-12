package uz.mediasolutions.miticodeliverytelegrambot.controller.web.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import uz.mediasolutions.miticodeliverytelegrambot.controller.web.abs.WebProductController;
import uz.mediasolutions.miticodeliverytelegrambot.manual.ApiResult;
import uz.mediasolutions.miticodeliverytelegrambot.payload.ProductWebDTO;
import uz.mediasolutions.miticodeliverytelegrambot.service.webabs.WebProductService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WebProductControllerImpl implements WebProductController {

    private final WebProductService productWebService;

    @Override
    public ApiResult<List<ProductWebDTO>> getAllByCategoryId(String chatId, Long categoryId) {
        return productWebService.getAllByCategoryId(chatId, categoryId);
    }

}
