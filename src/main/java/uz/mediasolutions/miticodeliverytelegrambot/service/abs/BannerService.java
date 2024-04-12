package uz.mediasolutions.miticodeliverytelegrambot.service.abs;

import org.springframework.data.domain.Page;
import uz.mediasolutions.miticodeliverytelegrambot.manual.ApiResult;
import uz.mediasolutions.miticodeliverytelegrambot.payload.BannerDTO;

public interface BannerService {

    ApiResult<Page<BannerDTO>> getAll(int page, int size);

    ApiResult<BannerDTO> get(Long id);

    ApiResult<?> upload(BannerDTO dto);

    ApiResult<?> delete(Long id);

}
