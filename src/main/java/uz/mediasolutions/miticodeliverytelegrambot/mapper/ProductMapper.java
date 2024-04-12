package uz.mediasolutions.miticodeliverytelegrambot.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.mediasolutions.miticodeliverytelegrambot.entity.Product;
import uz.mediasolutions.miticodeliverytelegrambot.payload.ProductDTO;
import uz.mediasolutions.miticodeliverytelegrambot.payload.ProductResDTO;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductResDTO toDTO(Product product);

    @Mapping(source = "categoryId", target = "category.id")
    Product toEntity(ProductDTO productDTO);

}
