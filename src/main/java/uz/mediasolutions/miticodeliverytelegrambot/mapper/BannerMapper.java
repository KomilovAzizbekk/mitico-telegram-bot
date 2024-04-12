package uz.mediasolutions.miticodeliverytelegrambot.mapper;

import org.mapstruct.Mapper;
import uz.mediasolutions.miticodeliverytelegrambot.entity.Banner;
import uz.mediasolutions.miticodeliverytelegrambot.payload.BannerDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BannerMapper {

    BannerDTO toDTO(Banner banner);

    Banner toEntity(BannerDTO bannerDTO);

    List<BannerDTO> toDTOList(List<Banner> banners);

}
