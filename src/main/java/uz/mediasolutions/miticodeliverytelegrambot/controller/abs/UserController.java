package uz.mediasolutions.miticodeliverytelegrambot.controller.abs;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import uz.mediasolutions.miticodeliverytelegrambot.manual.ApiResult;
import uz.mediasolutions.miticodeliverytelegrambot.payload.TgUserDTO;
import uz.mediasolutions.miticodeliverytelegrambot.utills.constants.Rest;

@RequestMapping(UserController.USER)
public interface UserController {

    String USER = Rest.BASE_PATH + "users/";

    String GET_ALL = "get-all";

    String GET_BY_ID = "get/{id}";

    String BAN = "ban/{id}";

    String UNBAN = "unban/{id}";


    @GetMapping(GET_ALL)
    ApiResult<Page<TgUserDTO>> getAll(@RequestParam(defaultValue = Rest.DEFAULT_PAGE_NUMBER) int page,
                                      @RequestParam(defaultValue = Rest.DEFAULT_PAGE_SIZE) int size,
                                      @RequestParam(defaultValue = "null", required = false) String search);

    @GetMapping(GET_BY_ID)
    ApiResult<TgUserDTO> getById(@PathVariable Long id);

    @PutMapping(BAN)
    ApiResult<?> banUser(@PathVariable Long id);

    @PutMapping(UNBAN)
    ApiResult<?> unbanUser(@PathVariable Long id);

}
