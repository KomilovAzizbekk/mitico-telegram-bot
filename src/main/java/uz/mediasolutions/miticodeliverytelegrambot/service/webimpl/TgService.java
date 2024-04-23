package uz.mediasolutions.miticodeliverytelegrambot.service.webimpl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.mediasolutions.miticodeliverytelegrambot.entity.Branch;
import uz.mediasolutions.miticodeliverytelegrambot.entity.Order;
import uz.mediasolutions.miticodeliverytelegrambot.entity.TgUser;
import uz.mediasolutions.miticodeliverytelegrambot.enums.LanguageName;
import uz.mediasolutions.miticodeliverytelegrambot.enums.OrderStatusName;
import uz.mediasolutions.miticodeliverytelegrambot.enums.StepName;
import uz.mediasolutions.miticodeliverytelegrambot.repository.BranchRepository;
import uz.mediasolutions.miticodeliverytelegrambot.repository.OrderRepository;
import uz.mediasolutions.miticodeliverytelegrambot.repository.OrderStatusRepository;
import uz.mediasolutions.miticodeliverytelegrambot.repository.TgUserRepository;
import uz.mediasolutions.miticodeliverytelegrambot.utills.constants.Message;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TgService extends TelegramLongPollingBot {

    private final MakeService makeService;
    private final TgUserRepository tgUserRepository;
    private final BranchRepository branchRepository;
    private final OrderRepository orderRepository;
    private final OrderStatusRepository orderStatusRepository;

    @Override
    public String getBotUsername() {
//        return "uygogo_bot";
//        return "sakaka_bot";
        return "mitico_uz_bot";
    }

    @Override
    public String getBotToken() {
//        return "5049026983:AAHjxVS4KdTmMLp4x_ir9khH4w1tB4h6pPQ";
//        return "6052104473:AAEscLILevwPMcG_00PYqAf-Kpb7eIUCIGg";
        return "7138374544:AAGUw_yhhSu-DPNs4rrnvt68v1JENDOQa7o";
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        String chatId = makeService.getChatId(update);
        TgUser tgUser = tgUserRepository.findByChatId(chatId);
        boolean existsByChatId = tgUserRepository.existsByChatId(chatId);

        System.out.println(update);

        if (existsByChatId && tgUser.isBanned()) {
            execute(new SendMessage(chatId, makeService.getMessage(Message.YOU_ARE_BANNED,
                    makeService.getUserLanguage(chatId))));
        } else if (update.hasMessage() && update.getMessage().hasText() &&
                update.getMessage().getText().equals("/start") &&
                !tgUserRepository.existsByChatId(chatId)) {
            execute(makeService.whenStart(update));
        } else if (update.hasMessage() && update.getMessage().hasText() &&
                update.getMessage().getText().equals("/start") &&
                tgUserRepository.existsByChatId(chatId)) {
            makeService.setUserStep(chatId, StepName.CHOOSE_FROM_MAIN_MENU);
            if (tgUser.getLanguage().getName().equals(LanguageName.UZ))
                execute(makeService.whenUz(update));
            else
                execute(makeService.whenRu(update));
        } else {
            if (update.hasMessage() && update.getMessage().hasText()) {
                String text = update.getMessage().getText();
                if (makeService.getUserStep(chatId).equals(StepName.MAIN_MENU) &&
                        text.equals(makeService.getMessage(Message.UZBEK, "UZ"))) {
                    execute(makeService.whenUz(update));
                } else if (makeService.getUserStep(chatId).equals(StepName.MAIN_MENU) &&
                        text.equals(makeService.getMessage(Message.RUSSIAN, "UZ"))) {
                    execute(makeService.whenRu(update));
                } else if (makeService.getUserStep(chatId).equals(StepName.CHOOSE_FROM_MAIN_MENU) &&
                        text.equals(makeService.getMessage(Message.MENU_SUG_COMP, makeService.getUserLanguage(chatId)))) {
                    execute(makeService.whenSuggestComplaint(update));
                } else if (makeService.getUserStep(chatId).equals(StepName.SEND_SUGGESTION_COMPLAINT)) {
                    execute(makeService.whenSendSuggestComplaint(update));
                    execute(makeService.whenSendSuggestComplaintToChannel(update));
                } else if (makeService.getUserStep(chatId).equals(StepName.CHOOSE_FROM_MAIN_MENU) &&
                        text.equals(makeService.getMessage(Message.MENU_SETTINGS, makeService.getUserLanguage(chatId)))) {
                    execute(makeService.whenSettings1(update));
                    execute(makeService.whenSettings2(update));
                } else if (makeService.getUserStep(chatId).equals(StepName.CHANGE_NAME)) {
                    execute(makeService.whenChangeName2(update));
                } else if (makeService.getUserStep(chatId).equals(StepName.CHANGE_PHONE_NUMBER)) {
                    execute(makeService.whenChangePhoneNumber2(update));
                } else if (makeService.getUserStep(chatId).equals(StepName.CHANGE_LANGUAGE)) {
                    execute(makeService.whenChangeLanguage2(update));
                } else if (makeService.getUserStep(chatId).equals(StepName.ORDER_REGISTER_PHONE)) {
                    execute(makeService.whenOrderRegPhone1(update));
                } else if (makeService.getUserStep(chatId).equals(StepName.IS_DELIVERY)) {
                    execute(makeService.whenIsDelivery1(update));
                } else if (makeService.getUserStep(chatId).equals(StepName.LEAVE_COMMENT) &&
                        (text.equals(makeService.getMessage(Message.CLICK, makeService.getUserLanguage(chatId))) ||
                                text.equals(makeService.getMessage(Message.PAYME, makeService.getUserLanguage(chatId))) ||
                                text.equals(makeService.getMessage(Message.CASH, makeService.getUserLanguage(chatId))))) {
                    execute(makeService.whenLeaveComment(update));
                } else if (makeService.getUserStep(chatId).equals(StepName.GO_TO_PAYMENT)) {
                    deleteMessage(update);
                    execute(makeService.whenGoPayment(update));
                } else if (makeService.getUserStep(chatId).equals(StepName.SEND_ORDER_TO_CHANNEL)) {
                    execute(makeService.whenSendOrderToChannel(update));
                    execute(whenSendOrderToUser(chatId));
                } else if (makeService.getUserStep(chatId).equals(StepName.ORDER_CHOOSE)) {
                    execute(makeService.whenChosen(update));
                } else if (makeService.getUserStep(chatId).equals(StepName.CHOOSE_PAYMENT) &&
                        branchRepository.existsByNameUzOrNameRu(text, text)) {
                    execute(makeService.whenChoosePayment(update));
                } else if (makeService.getUserStep(chatId).equals(StepName.CHOOSE_PAYMENT) &&
                        text.equals(makeService.getMessage(Message.BACK_TO_MENU, makeService.getUserLanguage(chatId)))) {
                    execute(makeService.getSendMessage(update,
                            Objects.equals(makeService.getUserLanguage(chatId), "UZ") ? LanguageName.UZ : LanguageName.RU));
                } else if (makeService.getUserStep(chatId).equals(StepName.INCORRECT_PHONE_FORMAT)) {
                    execute(makeService.whenIncorrectPhoneFormat(update));
                } else if (makeService.getUserStep(chatId).equals(StepName.INCORRECT_PHONE_FORMAT_1)) {
                    execute(makeService.whenIncorrectPhoneFormat1(update));
                }
            } else if (update.hasMessage() && update.getMessage().hasContact()) {
                if (makeService.getUserStep(chatId).equals(StepName.INCORRECT_PHONE_FORMAT)) {
                    execute(makeService.whenIncorrectPhoneFormat(update));
                } else if (makeService.getUserStep(chatId).equals(StepName.INCORRECT_PHONE_FORMAT_1)) {
                    execute(makeService.whenIncorrectPhoneFormat1(update));
                } else if (makeService.getUserStep(chatId).equals(StepName.CHANGE_PHONE_NUMBER)) {
                    execute(makeService.whenChangePhoneNumber2(update));
                } else if (makeService.getUserStep(chatId).equals(StepName.IS_DELIVERY)) {
                    execute(makeService.whenIsDelivery1(update));
                }
            } else if (update.hasMessage() && update.getMessage().hasLocation()) {
                if (makeService.getUserStep(chatId).equals(StepName.CHOOSE_PAYMENT)) {
                    execute(makeService.whenChoosePayment(update));
                }
            } else if (update.hasCallbackQuery()) {
                String data = update.getCallbackQuery().getData();
                if (data.equals("changeName")) {
                    execute(makeService.whenChangeName1(update));
                } else if (data.equals("changePhone")) {
                    execute(makeService.deleteMessageForCallback(update));
                    execute(makeService.whenChangePhoneNumber1(update));
                } else if (data.equals("changeLanguage")) {
                    execute(makeService.deleteMessageForCallback(update));
                    execute(makeService.whenChangeLanguage1(update));
                } else if (data.startsWith("accept") || data.startsWith("reject")) {
                    execute(makeService.whenAcceptOrRejectOrder(data, update));
                    execute(makeService.whenSendResToUser(data));
                }
            }
        }
    }

    public void deleteMessage(Update update) throws TelegramApiException {
        SendMessage sendMessageRemove = new SendMessage();
        sendMessageRemove.setChatId(update.getMessage().getChatId().toString());
        sendMessageRemove.setText(".");
        sendMessageRemove.setReplyMarkup(new ReplyKeyboardRemove(true));
        org.telegram.telegrambots.meta.api.objects.Message message = execute(sendMessageRemove);
        DeleteMessage deleteMessage = new DeleteMessage(update.getMessage().getChatId().toString(), message.getMessageId());
        execute(deleteMessage);
    }

    public SendMessage whenSendOrderToChannelClickOrPayme(String chatId) {
        String language = makeService.getUserLanguage(chatId);
        List<Order> orderList = orderRepository.findAllByUserChatIdOrderByCreatedAtDesc(chatId);
        Order order = orderList.get(0);
        order.setOrderStatus(orderStatusRepository.findByName(OrderStatusName.PENDING));
        order.setPaidSum(order.getTotalPrice());
        Order saved = orderRepository.save(order);

        String address;
        String branchName = null;
        String providerName;
        String orderStatus;
        Branch branch = order.getBranch();
        if (language.equals("UZ")) {
            if (order.getBranch() != null) {
                branchName = branch.getNameUz();
            }
            providerName = order.getPaymentProviders().getName().getNameUz();
            orderStatus = order.getOrderStatus().getName().getNameUz();
        } else {
            if (order.getBranch() != null) {
                branchName = branch.getNameRu();
            }
            providerName = order.getPaymentProviders().getName().getNameRu();
            orderStatus = order.getOrderStatus().getName().getNameRu();
        }
        if (order.isDelivery()) {
            address = String.format("<a href=\"https://yandex.com/navi/?whatshere%%5Bzoom%%5D=18&whatshere%%5Bpoint%%5D=%f%%2C%f&lang=uz&from=navi\">%s</a>",
                    order.getLon(),
                    order.getLat(),
                    makeService.getMessage(Message.IN_MAP, language));
        } else {
            assert branch != null;
            address = makeService.getMessage(Message.WITH_OWN, language) + " - " +
                    String.format("<a href=\"https://yandex.com/navi/?whatshere%%5Bzoom%%5D=18&whatshere%%5Bpoint%%5D=%f%%2C%f&lang=uz&from=navi\">%s</a>",
                            branch.getLon(),
                            branch.getLat(),
                            branchName);
        }
        String format = order.getUpdatedAt().toLocalDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(MakeService.ORDER_CHANNEL_ID);
        sendMessage.setText(
                String.format(makeService.getMessage(Message.ORDER_MSG, language),
                        order.getId(),
                        order.getUser().getName(),
                        order.getUser().getChatId(),
                        order.getUser().getPhoneNumber(),
                        address,
                        format,
                        makeService.allOrderedProducts(chatId, order.getId()),
                        order.getComment() == null ? makeService.getMessage(Message.NO_INFO, language) : order.getComment(),
                        providerName,
                        order.getPrice(),
                        order.getDeliveryPrice(),
                        order.getTotalPrice(),
                        saved.getPaidSum(),
                        orderStatus));
        sendMessage.setReplyMarkup(makeService.forSendOrderToChannel(chatId));
        sendMessage.enableHtml(true);
        return sendMessage;
    }

    public SendMessage whenSendOrderToUser(String chatId) {
        String language = makeService.getUserLanguage(chatId);

        List<Order> orderList = orderRepository.findAllByUserChatIdOrderByCreatedAtDesc(chatId);
        Order order = orderList.get(0);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(String.format(
                makeService.getMessage(Message.ORDER_PENDING, language), order.getId()));
        sendMessage.enableHtml(true);
        sendMessage.setReplyMarkup(makeService.forMainMenu(chatId));
        makeService.setUserStep(chatId, StepName.PENDING_ORDER);
        return sendMessage;
    }

    public List<String> branchNames(Update update) {
        String chatId = makeService.getChatId(update);
        List<Branch> branches = branchRepository.findAllByActiveIsTrue();
        List<String> branchNames = new ArrayList<>();
        String language = makeService.getUserLanguage(chatId);
        for (Branch branch : branches) {
            if (language.equals("UZ")) {
                branchNames.add(branch.getNameUz().trim());
            } else {
                branchNames.add(branch.getNameRu().trim());
            }
        }
        return branchNames;
    }
}
