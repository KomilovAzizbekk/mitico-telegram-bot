package uz.mediasolutions.miticodeliverytelegrambot.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import uz.mediasolutions.miticodeliverytelegrambot.entity.*;
import uz.mediasolutions.miticodeliverytelegrambot.enums.LanguageName;
import uz.mediasolutions.miticodeliverytelegrambot.exceptions.RestException;
import uz.mediasolutions.miticodeliverytelegrambot.payload.*;
import uz.mediasolutions.miticodeliverytelegrambot.repository.ProductRepository;
import uz.mediasolutions.miticodeliverytelegrambot.repository.TgUserRepository;
import uz.mediasolutions.miticodeliverytelegrambot.repository.VariationRepository;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UniversalMapperImpl implements UniversalMapper {

    private final TgUserRepository tgUserRepository;
    private final VariationRepository variationRepository;
    private final ProductRepository productRepository;

    @Override
    public List<VariationWebDTO> toVariationWebDTOList(List<Variation> variations, String chatId) {
        if (variations == null) {
            return null;
        }

        List<VariationWebDTO> variationWebDTOS = new ArrayList<>();
        for (Variation variation : variations) {
            variationWebDTOS.add(toVariationWebDTO(variation, chatId));
        }
        return variationWebDTOS;
    }

    @Override
    public VariationWebDTO toVariationWebDTO(Variation variation, String chatId) {
        if (variation == null) {
            return null;
        }

        variation = variationRepository.getVariation(variation.getId());
        TgUser tgUser = tgUserRepository.findByChatId(chatId);

        VariationWebDTO.VariationWebDTOBuilder builder = VariationWebDTO.builder();
        builder.price(variation.getPrice());
        builder.measure(variation.getMeasure());
        builder.measureUnit(toMeasureUnitDTO(variation.getMeasureUnit(), chatId));
        builder.id(variation.getId());
        builder.product(toProduct2WebDTO(variation.getProduct(), chatId));
        if (tgUser.getLanguage().getName().equals(LanguageName.UZ)) {
            builder.name(variation.getNameUz());
        } else {
            builder.name(variation.getNameRu());
        }
        return builder.build();
    }

    @Override
    public MeasureUnitWebDTO toMeasureUnitDTO(MeasureUnit measureUnit, String chatId) {
        if (measureUnit == null) {
            return null;
        }

        TgUser tgUser = tgUserRepository.findByChatId(chatId);

        MeasureUnitWebDTO.MeasureUnitWebDTOBuilder builder = MeasureUnitWebDTO.builder();
        builder.id(measureUnit.getId());
        if (tgUser.getLanguage().getName().equals(LanguageName.UZ)) {
            builder.name(measureUnit.getNameUz());
        } else {
            builder.name(measureUnit.getNameRu());
        }
        return builder.build();
    }

    @Override
    public Product2WebDTO toProduct2WebDTO(Product product, String chatId) {
        if (product == null) {
            return null;
        }

        product = productRepository.getProduct(product.getId());

        TgUser tgUser = tgUserRepository.findByChatId(chatId);
        Product2WebDTO.Product2WebDTOBuilder builder = Product2WebDTO.builder();
        builder.imageUrl(product.getImageUrl());
        builder.id(product.getId());
        if (tgUser.getLanguage().getName().equals(LanguageName.UZ)) {
            builder.name(product.getNameUz());
            builder.description(product.getDescriptionUz());
        } else {
            builder.name(product.getNameRu());
            builder.description(product.getDescriptionRu());
        }
        return builder.build();
    }

    @Override
    public OrderProductResDTO toOrderProductResDTO(OrderProducts product, String chatId) {
        if (product == null) {
            return null;
        }

        OrderProductResDTO.OrderProductResDTOBuilder builder = OrderProductResDTO.builder();
        builder.id(product.getId());
        builder.count(product.getCount());
        builder.variation(toVariationWebDTO(product.getVariation(), chatId));
        return builder.build();
    }

    @Override
    public List<OrderProductResDTO> toOrderProductResDTOlist(List<OrderProducts> orderProducts, String chatId) {
        if (orderProducts == null) {
            return null;
        }

        List<OrderProductResDTO> orderProductDTOS = new ArrayList<>();
        for (OrderProducts product : orderProducts) {
            orderProductDTOS.add(toOrderProductResDTO(product, chatId));
        }
        return orderProductDTOS;
    }


    @Override
    public float totalPrice(List<OrderProducts> orderProducts) {
        if (orderProducts == null) {
            return 0;
        }
        float totalPrice = 0;

        for (OrderProducts orderProduct : orderProducts) {
            totalPrice += orderProduct.getCount() * orderProduct.getVariation().getPrice();
        }
        return totalPrice;
    }

    @Override
    public List<OrderProducts> toOrderProductsEntityList(List<OrderProductDTO> dtoList) {
        if (dtoList == null) {
            return null;
        }
        List<OrderProducts> orderProducts = new ArrayList<>();
        for (OrderProductDTO orderProductDTO : dtoList) {
            orderProducts.add(toOrderProductsEntity(orderProductDTO));
        }
        return orderProducts;
    }

    @Override
    public OrderProducts toOrderProductsEntity(OrderProductDTO dto) {
        if (dto == null) {
            return null;
        }

        Variation variation = variationRepository.findById(dto.getVariationId()).orElseThrow(
                () -> RestException.restThrow("VARIATION ID NOT FOUND", HttpStatus.BAD_REQUEST));

        OrderProducts.OrderProductsBuilder builder = OrderProducts.builder();
        builder.count(dto.getCount());
        builder.variation(variation);
        return builder.build();
    }

    @Override
    public OrderWebDTO toOrderWebDTO(Order order, String chatId) {
        if (order == null) {
            return null;
        }

        TgUser tgUser = tgUserRepository.findByChatId(chatId);

        OrderWebDTO.OrderWebDTOBuilder builder = OrderWebDTO.builder();
        builder.id(order.getId());
        builder.branchWebDTO(toBranchWebDTO(order.getBranch(), chatId));
        builder.userId(tgUser.getId());
        builder.lon(order.getLon());
        builder.lat(order.getLat());
        builder.paidSum(order.getPaidSum());
        builder.orderProducts(toOrderProductResDTOlist(order.getOrderProducts(), chatId));
        builder.price(order.getPrice());
        builder.comment(order.getComment());
        builder.deliveryPrice(order.getDeliveryPrice());
        builder.totalPrice(order.getTotalPrice());
        builder.createdAt(order.getCreatedAt().toLocalDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        if (tgUser.getLanguage().getName().equals(LanguageName.UZ)) {
            builder.status(order.getOrderStatus().getName().getNameUz());
            builder.paymentProviderName(order.getPaymentProviders().getName().getNameUz());
        } else {
            builder.status(order.getOrderStatus().getName().getNameRu());
            builder.paymentProviderName(order.getPaymentProviders().getName().getNameRu());
        }
        return builder.build();
    }

    @Override
    public List<OrderWebDTO> toOrderWebDTOList(List<Order> orders, String chatId) {
        if (orders == null) {
            return null;
        }

        List<OrderWebDTO> orderWebDTOList = new ArrayList<>();
        for (Order order : orders) {
            orderWebDTOList.add(toOrderWebDTO(order, chatId));
        }
        return orderWebDTOList;
    }

    @Override
    public BranchWebDTO toBranchWebDTO(Branch branch, String chatId) {
        if (branch == null) {
            return null;
        }
        TgUser tgUser = tgUserRepository.findByChatId(chatId);

        BranchWebDTO.BranchWebDTOBuilder builder = BranchWebDTO.builder();
        builder.id(branch.getId());
        builder.lat(branch.getLat());
        builder.lon(branch.getLon());
        builder.openingTime(branch.getOpeningTime());
        builder.closingTime(branch.getClosingTime());
        if (tgUser.getLanguage().getName().equals(LanguageName.UZ)) {
            builder.name(branch.getNameUz());
            builder.address(branch.getAddressUz());
        } else {
            builder.name(branch.getNameRu());
            builder.address(branch.getAddressRu());
        }
        return builder.build();
    }


}
