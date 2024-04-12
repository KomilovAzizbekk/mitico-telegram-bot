package uz.mediasolutions.miticodeliverytelegrambot.service.abs;

import org.springframework.data.domain.Page;
import uz.mediasolutions.miticodeliverytelegrambot.manual.ApiResult;
import uz.mediasolutions.miticodeliverytelegrambot.payload.VariationDTO;
import uz.mediasolutions.miticodeliverytelegrambot.payload.VariationResDTO;

public interface VariationService {
    ApiResult<Page<VariationResDTO>> getAll(int page, int size, String search);

    ApiResult<VariationResDTO> getById(Long id);

    ApiResult<?> add(VariationDTO dto);

    ApiResult<?> edit(Long id, VariationDTO dto);

    ApiResult<?> delete(Long id);

}
