package uz.mediasolutions.miticodeliverytelegrambot.controller.web.abs;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uz.mediasolutions.miticodeliverytelegrambot.manual.ApiResult;
import uz.mediasolutions.miticodeliverytelegrambot.payload.ProductWebDTO;
import uz.mediasolutions.miticodeliverytelegrambot.utills.constants.Rest;

import java.util.List;

@RequestMapping(WebProductController.PRODUCT_WEB)
public interface WebProductController {

    String PRODUCT_WEB = Rest.BASE_PATH + "product-web/";

    String GET_BY_CATEGORY = "get/{categoryId}";


    @GetMapping(GET_BY_CATEGORY)
    ApiResult<List<ProductWebDTO>> getAllByCategoryId(@RequestParam("user_id") String chatId,
                                                      @PathVariable Long categoryId);

}
