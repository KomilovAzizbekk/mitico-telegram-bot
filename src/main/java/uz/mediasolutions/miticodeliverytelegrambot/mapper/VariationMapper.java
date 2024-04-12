package uz.mediasolutions.miticodeliverytelegrambot.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.mediasolutions.miticodeliverytelegrambot.entity.Variation;
import uz.mediasolutions.miticodeliverytelegrambot.payload.VariationDTO;
import uz.mediasolutions.miticodeliverytelegrambot.payload.VariationResDTO;

@Mapper(componentModel = "spring")
public interface VariationMapper {

    VariationResDTO toDTO(Variation variation);

    @Mapping(source = "measureUnitId", target = "measureUnit.id")
    @Mapping(source = "productId", target = "product.id")
    Variation toEntity(VariationDTO dto);

}
