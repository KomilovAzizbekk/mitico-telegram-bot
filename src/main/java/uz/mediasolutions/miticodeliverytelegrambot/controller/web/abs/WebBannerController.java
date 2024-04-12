package uz.mediasolutions.miticodeliverytelegrambot.controller.web.abs;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.mediasolutions.miticodeliverytelegrambot.manual.ApiResult;
import uz.mediasolutions.miticodeliverytelegrambot.payload.BannerDTO;
import uz.mediasolutions.miticodeliverytelegrambot.utills.constants.Rest;

import java.util.List;

@RequestMapping(WebBannerController.BANNER_WEB)
public interface WebBannerController {

    String BANNER_WEB = Rest.BASE_PATH + "banner-web/";

    String GET = "get";

    @GetMapping(GET)
    ApiResult<List<BannerDTO>> get();

}
