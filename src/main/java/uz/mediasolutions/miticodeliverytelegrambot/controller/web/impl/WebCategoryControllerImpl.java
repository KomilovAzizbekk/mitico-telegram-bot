package uz.mediasolutions.miticodeliverytelegrambot.controller.web.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import uz.mediasolutions.miticodeliverytelegrambot.controller.web.abs.WebCategoryController;
import uz.mediasolutions.miticodeliverytelegrambot.manual.ApiResult;
import uz.mediasolutions.miticodeliverytelegrambot.payload.CategoryWebDTO;
import uz.mediasolutions.miticodeliverytelegrambot.service.webabs.WebCategoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WebCategoryControllerImpl implements WebCategoryController {

    private final WebCategoryService categoryWebService;

    @Override
    public ApiResult<List<CategoryWebDTO>> get(String chatId) {
        return categoryWebService.get(chatId);
    }
}
