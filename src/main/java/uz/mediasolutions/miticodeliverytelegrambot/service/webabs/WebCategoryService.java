package uz.mediasolutions.miticodeliverytelegrambot.service.webabs;

import uz.mediasolutions.miticodeliverytelegrambot.manual.ApiResult;
import uz.mediasolutions.miticodeliverytelegrambot.payload.CategoryWebDTO;

import java.util.List;

public interface WebCategoryService {
    ApiResult<List<CategoryWebDTO>> get(String chatId);
}
