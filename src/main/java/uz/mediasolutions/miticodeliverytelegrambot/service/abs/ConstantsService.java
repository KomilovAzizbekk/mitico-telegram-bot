package uz.mediasolutions.miticodeliverytelegrambot.service.abs;

import uz.mediasolutions.miticodeliverytelegrambot.entity.Constants;
import uz.mediasolutions.miticodeliverytelegrambot.manual.ApiResult;

public interface ConstantsService {
    ApiResult<Constants> get();

    ApiResult<?> edit(Long id, Constants constants);
}
