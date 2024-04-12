package uz.mediasolutions.miticodeliverytelegrambot.controller.web.abs;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uz.mediasolutions.miticodeliverytelegrambot.manual.ApiResult;
import uz.mediasolutions.miticodeliverytelegrambot.payload.VariationWebDTO;
import uz.mediasolutions.miticodeliverytelegrambot.utills.constants.Rest;

import java.util.List;

@RequestMapping(WebVariationController.VARIATION_WEB)
public interface WebVariationController {

    String VARIATION_WEB = Rest.BASE_PATH + "variation-web/";

    String GET_BY_PRODUCT = "get/{productId}";


    @GetMapping(GET_BY_PRODUCT)
    ApiResult<List<VariationWebDTO>> getAllByProductId(@RequestParam("user_id") String chatId,
                                                                 @PathVariable Long productId);

}
