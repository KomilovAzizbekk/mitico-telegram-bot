package uz.mediasolutions.miticodeliverytelegrambot.service.webimpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.mediasolutions.miticodeliverytelegrambot.entity.Banner;
import uz.mediasolutions.miticodeliverytelegrambot.manual.ApiResult;
import uz.mediasolutions.miticodeliverytelegrambot.mapper.BannerMapper;
import uz.mediasolutions.miticodeliverytelegrambot.payload.BannerDTO;
import uz.mediasolutions.miticodeliverytelegrambot.repository.BannerRepository;
import uz.mediasolutions.miticodeliverytelegrambot.service.webabs.WebBannerService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WebBannerServiceImpl implements WebBannerService {

    private final BannerRepository bannerRepository;
    private final BannerMapper bannerMapper;

    @Override
    public ApiResult<List<BannerDTO>> get() {
        List<Banner> banners = bannerRepository.findAllByOrderByNumberAsc();
        List<BannerDTO> dtoList = bannerMapper.toDTOList(banners);
        return ApiResult.success(dtoList);
    }
}
