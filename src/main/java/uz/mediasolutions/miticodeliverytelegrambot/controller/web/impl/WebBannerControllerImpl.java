package uz.mediasolutions.miticodeliverytelegrambot.controller.web.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import uz.mediasolutions.miticodeliverytelegrambot.controller.web.abs.WebBannerController;
import uz.mediasolutions.miticodeliverytelegrambot.manual.ApiResult;
import uz.mediasolutions.miticodeliverytelegrambot.payload.BannerDTO;
import uz.mediasolutions.miticodeliverytelegrambot.service.webabs.WebBannerService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WebBannerControllerImpl implements WebBannerController {

    private final WebBannerService bannerWebService;

    @Override
    public ApiResult<List<BannerDTO>> get() {
        return bannerWebService.get();
    }
}
