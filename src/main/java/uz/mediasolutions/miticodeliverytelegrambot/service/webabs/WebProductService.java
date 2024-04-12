package uz.mediasolutions.miticodeliverytelegrambot.service.webabs;

import uz.mediasolutions.miticodeliverytelegrambot.manual.ApiResult;
import uz.mediasolutions.miticodeliverytelegrambot.payload.ProductWebDTO;

import java.util.List;

public interface WebProductService {

    ApiResult<List<ProductWebDTO>> getAllByCategoryId(String chatId, Long categoryId);

}
