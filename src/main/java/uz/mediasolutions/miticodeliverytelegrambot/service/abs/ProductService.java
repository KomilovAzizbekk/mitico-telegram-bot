package uz.mediasolutions.miticodeliverytelegrambot.service.abs;

import org.springframework.data.domain.Page;
import uz.mediasolutions.miticodeliverytelegrambot.manual.ApiResult;
import uz.mediasolutions.miticodeliverytelegrambot.payload.ProductDTO;
import uz.mediasolutions.miticodeliverytelegrambot.payload.ProductResDTO;

import java.io.IOException;

public interface ProductService {
    ApiResult<Page<ProductResDTO>> getAll(int page, int size, String search, boolean active);

    ApiResult<ProductResDTO> getById(Long id);

    ApiResult<?> add(ProductDTO dto);

    ApiResult<?> edit(Long id, ProductDTO dto) throws IOException;

    ApiResult<?> delete(Long id) throws IOException;

}
