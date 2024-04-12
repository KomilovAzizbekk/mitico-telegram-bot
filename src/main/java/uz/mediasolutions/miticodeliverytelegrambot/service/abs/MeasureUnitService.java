package uz.mediasolutions.miticodeliverytelegrambot.service.abs;

import org.springframework.data.domain.Page;
import uz.mediasolutions.miticodeliverytelegrambot.manual.ApiResult;
import uz.mediasolutions.miticodeliverytelegrambot.payload.MeasureUnitDTO;

public interface MeasureUnitService {
    ApiResult<Page<MeasureUnitDTO>> getAll(int page, int size, String name);

    ApiResult<MeasureUnitDTO> getById(Long id);

    ApiResult<?> add(MeasureUnitDTO measureUnitDTO);

    ApiResult<?> edit(Long id, MeasureUnitDTO measureUnitDTO);

    ApiResult<?> delete(Long id);

}
