package uz.mediasolutions.miticodeliverytelegrambot.service.webabs;

import uz.mediasolutions.miticodeliverytelegrambot.manual.ApiResult;
import uz.mediasolutions.miticodeliverytelegrambot.payload.BannerDTO;

import java.util.List;

public interface WebBannerService {
    ApiResult<List<BannerDTO>> get();
}
