package uz.mediasolutions.miticodeliverytelegrambot.controller.web.abs;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uz.mediasolutions.miticodeliverytelegrambot.manual.ApiResult;
import uz.mediasolutions.miticodeliverytelegrambot.payload.CategoryWebDTO;
import uz.mediasolutions.miticodeliverytelegrambot.utills.constants.Rest;

import java.util.List;

@RequestMapping(WebCategoryController.CATEGORY_WEB)
public interface WebCategoryController {

    String CATEGORY_WEB = Rest.BASE_PATH + "category-web/";

    String GET = "get";

    @GetMapping(GET)
    ApiResult<List<CategoryWebDTO>> get(@RequestParam("user_id") String chatId);

}
