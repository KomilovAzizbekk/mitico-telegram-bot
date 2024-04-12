package uz.mediasolutions.miticodeliverytelegrambot.service.webabs;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.mediasolutions.miticodeliverytelegrambot.manual.ApiResult;
import uz.mediasolutions.miticodeliverytelegrambot.payload.OrderProductDTO;
import uz.mediasolutions.miticodeliverytelegrambot.payload.OrderWebDTO;

import java.util.List;

public interface WebOrderService {
    ApiResult<List<OrderWebDTO>> getAll(String chatId);

    ApiResult<OrderWebDTO> getById(String chatId, Long id);

    ApiResult<?> add(String chatId, List<OrderProductDTO> dtoList) throws TelegramApiException;

}
