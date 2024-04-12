package uz.mediasolutions.miticodeliverytelegrambot.mapper;

import uz.mediasolutions.miticodeliverytelegrambot.entity.*;
import uz.mediasolutions.miticodeliverytelegrambot.payload.*;

import java.util.List;

public interface UniversalMapper {

    List<VariationWebDTO> toVariationWebDTOList(List<Variation> variations, String chatId);

    VariationWebDTO toVariationWebDTO(Variation variation, String chatId);

    MeasureUnitWebDTO toMeasureUnitDTO(MeasureUnit measureUnit, String chatId);

    Product2WebDTO toProduct2WebDTO(Product product, String chatId);

    OrderProductResDTO toOrderProductResDTO(OrderProducts product, String chatId);

    List<OrderProductResDTO> toOrderProductResDTOlist(List<OrderProducts> orderProducts, String chatId);

    float totalPrice(List<OrderProducts> orderProducts);

    List<OrderProducts> toOrderProductsEntityList(List<OrderProductDTO> dtoList);

    OrderProducts toOrderProductsEntity(OrderProductDTO dto);

    OrderWebDTO toOrderWebDTO(Order order, String chatId);

    List<OrderWebDTO> toOrderWebDTOList(List<Order> orders, String chatId);

    BranchWebDTO toBranchWebDTO(Branch branch, String chatId);

}
