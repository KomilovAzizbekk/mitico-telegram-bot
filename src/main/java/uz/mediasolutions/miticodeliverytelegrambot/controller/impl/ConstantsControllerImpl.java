package uz.mediasolutions.miticodeliverytelegrambot.controller.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RestController;
import uz.mediasolutions.miticodeliverytelegrambot.controller.abs.ConstantsController;
import uz.mediasolutions.miticodeliverytelegrambot.entity.Constants;
import uz.mediasolutions.miticodeliverytelegrambot.manual.ApiResult;
import uz.mediasolutions.miticodeliverytelegrambot.payload.BannerDTO;
import uz.mediasolutions.miticodeliverytelegrambot.service.abs.ConstantsService;

@RestController
@RequiredArgsConstructor
public class ConstantsControllerImpl implements ConstantsController {

    private final ConstantsService constantsService;

    @Override
    public ApiResult<Constants> get() {
        return constantsService.get();
    }

    @Override
    public ApiResult<?> edit(Long id, Constants constants) {
        return constantsService.edit(id, constants);
    }
}
