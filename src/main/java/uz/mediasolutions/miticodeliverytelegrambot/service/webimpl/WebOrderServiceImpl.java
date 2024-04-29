package uz.mediasolutions.miticodeliverytelegrambot.service.webimpl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.mediasolutions.miticodeliverytelegrambot.entity.*;
import uz.mediasolutions.miticodeliverytelegrambot.enums.OrderStatusName;
import uz.mediasolutions.miticodeliverytelegrambot.enums.StepName;
import uz.mediasolutions.miticodeliverytelegrambot.exceptions.RestException;
import uz.mediasolutions.miticodeliverytelegrambot.manual.ApiResult;
import uz.mediasolutions.miticodeliverytelegrambot.mapper.UniversalMapper;
import uz.mediasolutions.miticodeliverytelegrambot.payload.OrderProductDTO;
import uz.mediasolutions.miticodeliverytelegrambot.payload.OrderWebDTO;
import uz.mediasolutions.miticodeliverytelegrambot.repository.*;
import uz.mediasolutions.miticodeliverytelegrambot.service.webabs.WebOrderService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WebOrderServiceImpl implements WebOrderService {

    private final OrderRepository orderRepository;
    private final TgUserRepository tgUserRepository;
    private final UniversalMapper universalMapper;
    private final OrderStatusRepository orderStatusRepository;
    private final TgService tgService;
//    private final BasketRepository basketRepository;
    private final OrderProductRepository orderProductRepository;
    private final MakeService makeService;
    private final ConstantsRepository constantsRepository;

    @Override
    public ApiResult<List<OrderWebDTO>> getAll(String chatId) {
        if (tgUserRepository.existsByChatId(chatId)) {
            List<Order> orders = orderRepository.findAllByUserChatIdOrderByCreatedAtDesc(chatId);

            orders.removeIf(order -> order.getOrderStatus().getName().equals(OrderStatusName.NOT_COMPLETE));

            List<OrderWebDTO> dtoList = universalMapper.toOrderWebDTOList(orders, chatId);
            return ApiResult.success(dtoList);
        } else {
            throw RestException.restThrow("USER ID NOT FOUND", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ApiResult<OrderWebDTO> getById(String chatId, Long id) {
        if (tgUserRepository.existsByChatId(chatId)) {
            Order order = orderRepository.findById(id).orElseThrow(
                    () -> RestException.restThrow("ID NOT FOUND", HttpStatus.BAD_REQUEST));
            OrderWebDTO orderWebDTO = universalMapper.toOrderWebDTO(order, chatId);
            return ApiResult.success(orderWebDTO);
        } else {
            throw RestException.restThrow("USER ID NOT FOUND", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ApiResult<?> add(String chatId, List<OrderProductDTO> dtoList) throws TelegramApiException {
        if (tgUserRepository.existsByChatId(chatId)) {
            TgUser tgUser = tgUserRepository.findByChatId(chatId);
            List<OrderProducts> orderProducts = universalMapper.toOrderProductsEntityList(dtoList);
            List<OrderProducts> saveAll = orderProductRepository.saveAll(orderProducts);

            float productPrice = universalMapper.totalPrice(saveAll);
            Constants constants = constantsRepository.findById(1L).orElseThrow(
                    () -> RestException.restThrow("CONSTANTS NOT FOUND", HttpStatus.BAD_REQUEST));

            Order.OrderBuilder builder = Order.builder();
            builder.orderStatus(orderStatusRepository.findByName(OrderStatusName.NOT_COMPLETE));
            builder.user(tgUser);
            builder.orderProducts(saveAll);
            builder.price(productPrice);
            builder.totalPrice(productPrice); //DELIVERY PRICE SHOULD BE ADDED
            if (productPrice < constants.getMinOrderPrice()) {
                return ApiResult.error("ORDER PRICE SHOULD BE HIGHER THAN",
                        (int) constants.getMinOrderPrice(), 200);
//                throw RestException.restThrow("ORDER PRICE SHOULD BE HIGHER THAN " +
//                        constants.getMinOrderPrice(), HttpStatus.BAD_REQUEST);
            }

            Order order = builder.build();
            orderRepository.save(order);
            if (tgUser.getName() != null && tgUser.getPhoneNumber() != null) {
                makeService.setUserStep(chatId, StepName.IS_DELIVERY);
                tgService.execute(makeService.whenIsDelivery(chatId));
            } else if (tgUser.getName() == null) {
                makeService.setUserStep(chatId, StepName.ORDER_REGISTER_NAME);
                tgService.execute(makeService.whenOrderRegName(chatId));
            } else {
                makeService.setUserStep(chatId, StepName.ORDER_REGISTER_PHONE);
                tgService.execute(makeService.whenOrderRegPhone(chatId));
            }
            return ApiResult.success("SAVED SUCCESSFULLY");
        } else {
            throw RestException.restThrow("USER ID NOT FOUND", HttpStatus.BAD_REQUEST);
        }
    }
}
