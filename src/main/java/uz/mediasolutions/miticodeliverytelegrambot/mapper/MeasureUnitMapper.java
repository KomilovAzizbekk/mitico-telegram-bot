package uz.mediasolutions.miticodeliverytelegrambot.mapper;

import org.mapstruct.Mapper;
import uz.mediasolutions.miticodeliverytelegrambot.entity.MeasureUnit;
import uz.mediasolutions.miticodeliverytelegrambot.payload.MeasureUnitDTO;

@Mapper(componentModel = "spring")
public interface MeasureUnitMapper {

    MeasureUnitDTO toDTO(MeasureUnit measureUnit);

    MeasureUnit toEntity(MeasureUnitDTO measureUnitDTO);

}
