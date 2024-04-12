package uz.mediasolutions.miticodeliverytelegrambot.mapper;

import org.mapstruct.Mapper;
import uz.mediasolutions.miticodeliverytelegrambot.entity.TgUser;
import uz.mediasolutions.miticodeliverytelegrambot.payload.TgUserDTO;

@Mapper(componentModel = "spring")
public interface TgUserMapper {

    TgUserDTO toDTO(TgUser user);

}
