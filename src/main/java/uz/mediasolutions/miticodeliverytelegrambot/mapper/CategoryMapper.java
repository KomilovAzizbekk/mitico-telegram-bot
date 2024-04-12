package uz.mediasolutions.miticodeliverytelegrambot.mapper;

import org.mapstruct.Mapper;
import uz.mediasolutions.miticodeliverytelegrambot.entity.Category;
import uz.mediasolutions.miticodeliverytelegrambot.payload.CategoryDTO;
import uz.mediasolutions.miticodeliverytelegrambot.payload.CategoryWebDTO;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDTO toDTO(Category category);

    Category toEntity(CategoryDTO categoryDTO);

}
