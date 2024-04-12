package uz.mediasolutions.miticodeliverytelegrambot.service.abs;

import org.springframework.data.domain.Page;
import uz.mediasolutions.miticodeliverytelegrambot.manual.ApiResult;
import uz.mediasolutions.miticodeliverytelegrambot.payload.CategoryDTO;

import java.io.IOException;

public interface CategoryService {
    ApiResult<Page<CategoryDTO>> getAll(int page, int size, String name, boolean active);

    ApiResult<CategoryDTO> getById(Long id);

    ApiResult<?> add(CategoryDTO categoryDTO);

    ApiResult<?> edit(Long id, CategoryDTO categoryDTO) throws IOException;

    ApiResult<?> delete(Long id) throws IOException;

}
