package uz.mediasolutions.miticodeliverytelegrambot.controller.abs;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import uz.mediasolutions.miticodeliverytelegrambot.entity.Constants;
import uz.mediasolutions.miticodeliverytelegrambot.manual.ApiResult;
import uz.mediasolutions.miticodeliverytelegrambot.payload.BannerDTO;
import uz.mediasolutions.miticodeliverytelegrambot.utills.constants.Rest;

import javax.validation.Valid;

@RequestMapping(ConstantsController.CONSTANTS)
public interface ConstantsController {

    String CONSTANTS = Rest.BASE_PATH + "constants/";

    String GET = "get";


    String EDIT = "edit/{id}";

    @GetMapping(GET)
    ApiResult<Constants> get();


    @PutMapping(EDIT)
    ApiResult<?> edit(@PathVariable Long id, @RequestBody @Valid Constants constants);

}
