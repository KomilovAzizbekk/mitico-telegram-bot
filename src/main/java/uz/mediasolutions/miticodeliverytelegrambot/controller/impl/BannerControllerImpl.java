package uz.mediasolutions.miticodeliverytelegrambot.controller.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RestController;
import uz.mediasolutions.miticodeliverytelegrambot.controller.abs.BannerController;
import uz.mediasolutions.miticodeliverytelegrambot.manual.ApiResult;
import uz.mediasolutions.miticodeliverytelegrambot.payload.BannerDTO;
import uz.mediasolutions.miticodeliverytelegrambot.service.abs.BannerService;

@RestController
@RequiredArgsConstructor
public class BannerControllerImpl implements BannerController {

    private final BannerService bannerService;

    @Override
    public ApiResult<Page<BannerDTO>> getAll(int page, int size) {
        return bannerService.getAll(page, size);
    }

    @Override
    public ApiResult<BannerDTO> get(Long id) {
        return bannerService.get(id);
    }

    @Override
    public ApiResult<?> upload(BannerDTO dto) {
        return bannerService.upload(dto);
    }

    @Override
    public ApiResult<?> delete(Long id) {
        return bannerService.delete(id);
    }
}
