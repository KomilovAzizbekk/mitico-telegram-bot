package uz.mediasolutions.miticodeliverytelegrambot.service.webabs;

import uz.mediasolutions.miticodeliverytelegrambot.manual.ApiResult;
import uz.mediasolutions.miticodeliverytelegrambot.payload.VariationWebDTO;

import java.util.List;

public interface WebVariationService {

    ApiResult<List<VariationWebDTO>> getAllByProductId(String chatId, Long productId);
}
