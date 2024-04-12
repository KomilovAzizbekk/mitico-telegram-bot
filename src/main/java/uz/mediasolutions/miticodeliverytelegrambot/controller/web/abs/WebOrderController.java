package uz.mediasolutions.miticodeliverytelegrambot.controller.web.abs;

import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.mediasolutions.miticodeliverytelegrambot.manual.ApiResult;
import uz.mediasolutions.miticodeliverytelegrambot.payload.OrderProductDTO;
import uz.mediasolutions.miticodeliverytelegrambot.payload.OrderWebDTO;
import uz.mediasolutions.miticodeliverytelegrambot.utills.constants.Rest;

import javax.validation.Valid;
import java.util.List;

@RequestMapping(WebOrderController.ORDER_WEB)
public interface WebOrderController {

    String ORDER_WEB = Rest.BASE_PATH + "order-web/";

    String GET_ALL = "get-all";

    String GET_BY_ID = "get/{id}";

    String ADD = "add";

    @GetMapping(GET_ALL)
    ApiResult<List<OrderWebDTO>> getAll(@RequestParam("user_id") String chatId);

    @GetMapping(GET_BY_ID)
    ApiResult<OrderWebDTO> getById(@RequestParam("user_id") String chatId,
                                   @PathVariable Long id);

    @PostMapping(ADD)
    ApiResult<?> add(@RequestParam("user_id") String chatId,
                     @RequestBody @Valid List<OrderProductDTO> dtoList) throws TelegramApiException;


}
