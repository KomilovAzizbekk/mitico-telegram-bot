package uz.mediasolutions.miticodeliverytelegrambot.service.webimpl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import uz.mediasolutions.miticodeliverytelegrambot.entity.*;
import uz.mediasolutions.miticodeliverytelegrambot.entity.click.ClickInvoice;
import uz.mediasolutions.miticodeliverytelegrambot.entity.payme.OrderTransaction;
import uz.mediasolutions.miticodeliverytelegrambot.enums.*;
import uz.mediasolutions.miticodeliverytelegrambot.exceptions.RestException;
import uz.mediasolutions.miticodeliverytelegrambot.payload.click.ResClickOrderDTO;
import uz.mediasolutions.miticodeliverytelegrambot.repository.*;
import uz.mediasolutions.miticodeliverytelegrambot.utills.constants.Message;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static uz.mediasolutions.miticodeliverytelegrambot.enums.InvoiceStatusEnum.PENDING;

@Service
@RequiredArgsConstructor
public class MakeService {

    private final LanguageRepositoryPs languageRepositoryPs;
    private final TgUserRepository tgUserRepository;
    private final StepRepository stepRepository;
    private final LanguageRepository languageRepository;
    private final OrderRepository orderRepository;
    private final PaymentProvidersRepository paymentProvidersRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final BranchRepository branchRepository;
    private final ConstantsRepository constantsRepository;
    private final ClickInvoiceRepository clickInvoiceRepository;
    private final OrderTransactionRepository transactionRepository;

    @Value("${click.service.id}")
    private String clickServiceId;

    @Value("${click.merchant.id}")
    private String clickMerchantId;

    @Value("${payme.merchant.id}")
    private String paymeMerchantId;

    public static final String SUGGEST_COMPLAINT_CHANNEL_ID = "-1002033941002";
    public static final String ORDER_CHANNEL_ID = "-1001839992131";
    public static final String LINK = "https://mitico-bot-web-app.netlify.app/";
    public static final String UZ = "UZ";
    public static final String RU = "RU";

    public String getMessage(String key, String language) {
        List<LanguagePs> allByLanguage = languageRepositoryPs.findAll();
        if (!allByLanguage.isEmpty()) {
            for (LanguagePs languagePs : allByLanguage) {
                for (LanguageSourcePs languageSourceP : languagePs.getLanguageSourcePs()) {
                    if (languageSourceP.getTranslation() != null &&
                            languageSourceP.getLanguage().equals(language) &&
                            languagePs.getKey().equals(key)) {
                        return languageSourceP.getTranslation();
                    }
                }
            }
        }
        return null;
    }

    public void setUserStep(String chatId, StepName stepName) {
        TgUser tgUser = tgUserRepository.findByChatId(chatId);
        tgUser.setStep(stepRepository.findByName(stepName));
        tgUserRepository.save(tgUser);
    }

    public StepName getUserStep(String chatId) {
        TgUser tgUser = tgUserRepository.findByChatId(chatId);
        return tgUser.getStep().getName();
    }

    private static boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "\\+998[1-9]\\d{8}";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(phoneNumber);

        return matcher.matches();
    }

    public String getChatId(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getChatId().toString();
        } else if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getMessage().getChatId().toString();
        }
        return "";
    }

    public String getUsername(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getFrom().getUserName();
        } else if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getFrom().getUserName();
        }
        return "";
    }

    public String getUserLanguage(String chatId) {
        if (tgUserRepository.existsByChatId(chatId)) {
            TgUser tgUser = tgUserRepository.findByChatId(chatId);
            return tgUser.getLanguage().getName().name();
        } else
            return "UZ";
    }

    public SendMessage whenStart(Update update) {
        String chatId = getChatId(update);
        SendMessage sendMessage = new SendMessage(chatId, getMessage(Message.LANG_SAME_FOR_2_LANG,
                getUserLanguage(chatId)));
        sendMessage.setReplyMarkup(forStart());
        TgUser tgUser = TgUser.builder().chatId(chatId)
                .admin(false)
                .registered(false)
                .banned(false)
                .username(getUsername(update))
                .chatId(chatId)
                .build();
        tgUserRepository.save(tgUser);
        setUserStep(chatId, StepName.MAIN_MENU);
        return sendMessage;
    }

    private ReplyKeyboardMarkup forStart() {
        String chatId = getChatId(new Update());

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rowList = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();

        KeyboardButton button1 = new KeyboardButton();
        KeyboardButton button2 = new KeyboardButton();

        button1.setText(getMessage(Message.UZBEK, getUserLanguage(chatId)));
        button2.setText(getMessage(Message.RUSSIAN, getUserLanguage(chatId)));

        row1.add(button1);
        row1.add(button2);

        rowList.add(row1);
        markup.setKeyboard(rowList);
        markup.setSelective(true);
        markup.setResizeKeyboard(true);
        return markup;
    }

    public SendMessage whenUz(Update update) {
        return getSendMessage(update, LanguageName.UZ);
    }

    public SendMessage whenRu(Update update) {
        return getSendMessage(update, LanguageName.RU);
    }

    public SendMessage getSendMessage(Update update, LanguageName languageName) {
        String chatId = getChatId(update);
        TgUser tgUser = tgUserRepository.findByChatId(chatId);
        Language language = languageRepository.findByName(languageName);
        tgUser.setLanguage(language);
        tgUserRepository.save(tgUser);
        SendMessage sendMessage = new SendMessage(getChatId(update),
                String.format(getMessage(Message.MENU_MSG, getUserLanguage(chatId)),
                        update.getMessage().getFrom().getFirstName()));
        sendMessage.setReplyMarkup(forMainMenu(chatId));
        setUserStep(chatId, StepName.CHOOSE_FROM_MAIN_MENU);
        return sendMessage;
    }

    public ReplyKeyboardMarkup forMainMenu(String chatId) {
        TgUser tgUser = tgUserRepository.findByChatId(chatId);

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rowList = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();

        KeyboardButton button1 = new KeyboardButton();
        KeyboardButton button2 = new KeyboardButton();
        KeyboardButton button3 = new KeyboardButton();
        KeyboardButton button4 = new KeyboardButton();

        button1.setText(getMessage(Message.MENU_WEBSITE, getUserLanguage(chatId)));
        button2.setText(getMessage(Message.MENU_SUG_COMP, getUserLanguage(chatId)));
        button3.setText(getMessage(Message.MENU_MY_ORDERS, getUserLanguage(chatId)));
        button4.setText(getMessage(Message.MENU_SETTINGS, getUserLanguage(chatId)));

        if (tgUser.getLanguage().getName().equals(LanguageName.UZ)) {
            button1.setWebApp(new WebAppInfo(LINK + chatId + "/" + UZ));
            button3.setWebApp(new WebAppInfo(LINK + "orders/" + chatId + "/" + UZ));
        } else {
            button1.setWebApp(new WebAppInfo(LINK + chatId + "/" + RU));
            button3.setWebApp(new WebAppInfo(LINK + "orders/" + chatId + "/" + RU));
        }


        row1.add(button1);
        row2.add(button2);
        row2.add(button3);
        row3.add(button4);

        rowList.add(row1);
        rowList.add(row2);
        rowList.add(row3);

        markup.setKeyboard(rowList);
        markup.setSelective(true);
        markup.setResizeKeyboard(true);
        return markup;
    }

    public List<Branch> getActiveBranches() {
        Sort sort = Sort.by(Sort.Order.asc("createdAt"));
        List<Branch> branches = branchRepository.findAllByActiveIsTrue(sort);
        List<Branch> activeBranches = new ArrayList<>();

        if (branches.isEmpty()) {
            return new ArrayList<>();
        } else {
            for (Branch branch : branches) {
                if (!branch.isClosesAfterMn()) {
                    if (branch.getOpeningTime().isBefore(LocalTime.now()) &&
                            branch.getClosingTime().isAfter(LocalTime.now())) {
                        activeBranches.add(branch);
                    }
                } else {
                    if (branch.getOpeningTime().isBefore(LocalTime.now())) {
                        if (branch.getClosingTime().isBefore(LocalTime.now())) {
                            activeBranches.add(branch);
                        }
                    } else {
                        if (branch.getClosingTime().isAfter(LocalTime.now())) {
                            activeBranches.add(branch);
                        }
                    }
                }
            }
            return activeBranches;
        }
    }

    public SendMessage whenSuggestComplaint(Update update) {
        String chatId = getChatId(update);

        setUserStep(chatId, StepName.SEND_SUGGESTION_COMPLAINT);
        SendMessage sendMessage =  new SendMessage(chatId, getMessage(Message.SEND_SUG_COMP,
                getUserLanguage(chatId)));
        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
        return sendMessage;
    }

    public SendMessage whenSendSuggestComplaint(Update update) {
        String chatId = getChatId(update);
        setUserStep(chatId, StepName.CHOOSE_FROM_MAIN_MENU);
        SendMessage sendMessage = new SendMessage(chatId, getMessage(Message.RESPONSE_SUG_COMP,
                getUserLanguage(chatId)));
        sendMessage.setReplyMarkup(forMainMenu(chatId));
        return sendMessage;
    }

    public SendMessage whenSendSuggestComplaintToChannel(Update update) {
        String chatId = getChatId(update);
        TgUser tgUser = tgUserRepository.findByChatId(chatId);
        String username;
        if (update.getMessage().getFrom().getUserName() != null) {
            username = String.format("<a href='%s' target='_blank'>",
                    "https://t.me/" + update.getMessage().getFrom().getUserName()) +
                    update.getMessage().getFrom().getFirstName() + "</a>";
        } else {
            username = update.getMessage().getFrom().getFirstName();
        }
        String phoneNumber = tgUser.getPhoneNumber();
        String text = update.getMessage().getText();

        SendMessage sendMessage = new SendMessage(SUGGEST_COMPLAINT_CHANNEL_ID,
                String.format(getMessage(Message.SUGGEST_COMPLAINT,
                getUserLanguage(chatId)), username, phoneNumber, text));
        sendMessage.enableHtml(true);
        return sendMessage;
    }

    public SendMessage whenSettings1(Update update) {
        String chatId = getChatId(update);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(getMessage(Message.SETTINGS, getUserLanguage(chatId)));
        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
        setUserStep(chatId, StepName.MENU_SETTINGS);
        return sendMessage;
    }

    public SendMessage whenSettings2(Update update) {
        String chatId = getChatId(update);
        TgUser tgUser = tgUserRepository.findByChatId(chatId);
        String language = getUserLanguage(chatId);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(
                String.format(getMessage(Message.USER_INFO, language),
                        tgUser.getName() != null ? tgUser.getName() : getMessage(Message.NOT_EXISTS, language),
                        tgUser.getPhoneNumber() != null ? tgUser.getPhoneNumber() : getMessage(Message.NOT_EXISTS, language),
                        tgUser.getLanguage().getId() == 1 ?
                                getMessage(Message.UZBEK, "UZ") :
                                getMessage(Message.RUSSIAN, "UZ")));
        sendMessage.setReplyMarkup(forSettings(update));
        return sendMessage;
    }

    private InlineKeyboardMarkup forSettings(Update update) {
        String chatId = getChatId(update);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        InlineKeyboardButton button3 = new InlineKeyboardButton();

        button1.setText(getMessage(Message.CHANGE_NAME, getUserLanguage(chatId)));
        button2.setText(getMessage(Message.CHANGE_PHONE_NUMBER, getUserLanguage(chatId)));
        button3.setText(getMessage(Message.CHANGE_LANGUAGE, getUserLanguage(chatId)));

        button1.setCallbackData("changeName");
        button2.setCallbackData("changePhone");
        button3.setCallbackData("changeLanguage");

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();

        row1.add(button1);
        row2.add(button2);
        row3.add(button3);

        rowsInline.add(row1);
        rowsInline.add(row2);
        rowsInline.add(row3);

        markupInline.setKeyboard(rowsInline);

        return markupInline;
    }

    public EditMessageText whenChangeName1(Update update) {
        String chatId = getChatId(update);
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        editMessageText.setText(getMessage(Message.ENTER_NAME, getUserLanguage(chatId)));
        setUserStep(chatId, StepName.CHANGE_NAME);
        return editMessageText;
    }

    public SendMessage whenChangeName2(Update update) {
        String chatId = getChatId(update);
        TgUser tgUser = tgUserRepository.findByChatId(chatId);
        tgUser.setName(update.getMessage().getText());
        if (tgUser.getPhoneNumber() != null) {
            tgUser.setRegistered(true);
        }
        tgUserRepository.save(tgUser);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(getMessage(Message.NAME_CHANGED, getUserLanguage(chatId)));
        sendMessage.setReplyMarkup(forMainMenu(chatId));
        setUserStep(chatId, StepName.CHOOSE_FROM_MAIN_MENU);
        return sendMessage;
    }

    public SendMessage whenChangePhoneNumber1(Update update) {
        String chatId = getChatId(update);
        SendMessage sendMessage = new SendMessage(chatId, getMessage(Message.ENTER_PHONE_NUMBER, getUserLanguage(chatId)));
        sendMessage.setReplyMarkup(forPhoneNumber(chatId));
        setUserStep(chatId, StepName.CHANGE_PHONE_NUMBER);
        return sendMessage;
    }

    public SendMessage whenChangePhoneNumber2(Update update) {
        String chatId = getChatId(update);
        TgUser tgUser = tgUserRepository.findByChatId(chatId);

        if (update.getMessage().hasText()) {
            if (isValidPhoneNumber(update.getMessage().getText())) {
                String phoneNumber = update.getMessage().getText();
                tgUser.setPhoneNumber(phoneNumber);
                tgUserRepository.save(tgUser);
                return executeChangePhoneNumber(update);
            } else {
                SendMessage sendMessage = new SendMessage(getChatId(update),
                        getMessage(Message.INCORRECT_PHONE_FORMAT, getUserLanguage(chatId)));
                sendMessage.setReplyMarkup(forPhoneNumber(chatId));
                setUserStep(chatId, StepName.INCORRECT_PHONE_FORMAT);
                return sendMessage;
            }
        } else {
            String phoneNumber = update.getMessage().getContact().getPhoneNumber();
            phoneNumber = phoneNumber.startsWith("+") ? phoneNumber : "+" + phoneNumber;
            tgUser.setPhoneNumber(phoneNumber);
            tgUserRepository.save(tgUser);
            return executeChangePhoneNumber(update);
        }
    }

    public SendMessage whenIncorrectPhoneFormat(Update update) {
        return whenChangePhoneNumber2(update);
    }

    public SendMessage whenIncorrectPhoneFormat1(Update update) {
        return whenIsDelivery1(update);
    }

    private ReplyKeyboardMarkup forPhoneNumber(String chatId) {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rowList = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();

        KeyboardButton button1 = new KeyboardButton();

        button1.setText(getMessage(Message.SHARE_PHONE_NUMBER, getUserLanguage(chatId)));
        button1.setRequestContact(true);

        row1.add(button1);

        rowList.add(row1);
        markup.setKeyboard(rowList);
        markup.setSelective(true);
        markup.setResizeKeyboard(true);
        return markup;
    }

    private SendMessage executeChangePhoneNumber(Update update) {
        String chatId = getChatId(update);

        TgUser tgUser = tgUserRepository.findByChatId(chatId);
        if (tgUser.getName() != null) {
            tgUser.setRegistered(true);
        }
        tgUserRepository.save(tgUser);

        SendMessage sendMessage = new SendMessage(chatId, getMessage(Message.PHONE_NUMBER_CHANGED,
                getUserLanguage(chatId)));
        sendMessage.setReplyMarkup(forMainMenu(chatId));
        setUserStep(chatId, StepName.CHOOSE_FROM_MAIN_MENU);
        return sendMessage;
    }

    public DeleteMessage deleteMessageForCallback(Update update) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(getChatId(update));
        deleteMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        return deleteMessage;
    }

    public SendMessage whenChangeLanguage1(Update update) {
        String chatId = getChatId(update);
        SendMessage sendMessage = new SendMessage(chatId,
                getMessage(Message.LANG_SAME_FOR_2_LANG, getUserLanguage(chatId)));
        sendMessage.setReplyMarkup(forStart());
        setUserStep(chatId, StepName.CHANGE_LANGUAGE);
        return sendMessage;
    }

    public SendMessage whenChangeLanguage2(Update update) {
        String chatId = getChatId(update);
        TgUser tgUser = tgUserRepository.findByChatId(chatId);

        if (update.getMessage().getText().equals(
                getMessage(Message.UZBEK, getUserLanguage(chatId)))) {
            tgUser.setLanguage(languageRepository.findByName(LanguageName.UZ));
        } else if (update.getMessage().getText().equals(
                getMessage(Message.RUSSIAN, getUserLanguage(chatId)))) {
            tgUser.setLanguage(languageRepository.findByName(LanguageName.RU));
        }
        tgUserRepository.save(tgUser);
        SendMessage sendMessage = new SendMessage(chatId,
                getMessage(Message.LANGUAGE_CHANGED, getUserLanguage(chatId)));
        sendMessage.setReplyMarkup(forMainMenu(chatId));
        setUserStep(chatId, StepName.CHOOSE_FROM_MAIN_MENU);
        return sendMessage;
    }

    public SendMessage whenIsDelivery(String chatId) {
        TgUser tgUser = tgUserRepository.findByChatId(chatId);
        tgUser.setRegistered(true);
        tgUserRepository.save(tgUser);

        SendMessage sendMessage = new SendMessage(chatId,
                getMessage(Message.IS_DELIVERY, getUserLanguage(chatId)));
        sendMessage.setReplyMarkup(forIsDelivery(chatId));
        setUserStep(chatId, StepName.ORDER_CHOOSE);
        return sendMessage;
    }

    private ReplyKeyboardMarkup forIsDelivery(String chatId) {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rowList = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();

        KeyboardButton button1 = new KeyboardButton();
        KeyboardButton button2 = new KeyboardButton();

        button1.setText(getMessage(Message.PICK_UP, getUserLanguage(chatId)));
        button2.setText(getMessage(Message.DELIVERY, getUserLanguage(chatId)));

        row1.add(button1);
        row2.add(button2);

        rowList.add(row1);
        rowList.add(row2);
        markup.setKeyboard(rowList);
        markup.setSelective(true);
        markup.setResizeKeyboard(true);
        return markup;
    }

    public SendMessage whenIsDelivery1(Update update) {
        String chatId = getChatId(update);
        TgUser tgUser = tgUserRepository.findByChatId(chatId);

        if (update.getMessage().hasText()) {
            if (isValidPhoneNumber(update.getMessage().getText())) {
                String phoneNumber = update.getMessage().getText();
                tgUser.setPhoneNumber(phoneNumber);
                tgUserRepository.save(tgUser);
                return executeIsDelivery(chatId);
            } else {
                SendMessage sendMessage = new SendMessage(getChatId(update),
                        getMessage(Message.INCORRECT_PHONE_FORMAT, getUserLanguage(chatId)));
                sendMessage.setReplyMarkup(forPhoneNumber(chatId));
                setUserStep(chatId, StepName.INCORRECT_PHONE_FORMAT_1);
                return sendMessage;
            }
        } else {
            String phoneNumber = update.getMessage().getContact().getPhoneNumber();
            phoneNumber = phoneNumber.startsWith("+") ? phoneNumber : "+" + phoneNumber;
            tgUser.setPhoneNumber(phoneNumber);
            tgUserRepository.save(tgUser);
            return executeIsDelivery(chatId);
        }
    }

    private SendMessage executeIsDelivery(String chatId) {
        TgUser tgUser = tgUserRepository.findByChatId(chatId);
        tgUser.setRegistered(true);
        tgUserRepository.save(tgUser);

        SendMessage sendMessage = new SendMessage(chatId,
                getMessage(Message.IS_DELIVERY, getUserLanguage(chatId)));
        sendMessage.setReplyMarkup(forIsDelivery(chatId));
        setUserStep(chatId, StepName.ORDER_CHOOSE);
        return sendMessage;
    }

    public SendMessage whenChosen(Update update) {
        String chatId = getChatId(update);
        String language = getUserLanguage(chatId);
        String text = update.getMessage().getText();

        List<Order> orderList = orderRepository.findAllByUserChatIdOrderByCreatedAtDesc(chatId);
        Order order = orderList.get(0);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        List<Branch> activeBranches = getActiveBranches();

        if (activeBranches.isEmpty()) {
            sendMessage.setText(getMessage(Message.NO_WORKING_BRANCH, language));
            sendMessage.setReplyMarkup(forSendLocation(chatId, activeBranches));
        } else {
            if (text.equals(getMessage(Message.DELIVERY, language))) {
                order.setDelivery(true);
                sendMessage.setText(getMessage(Message.SEND_LOCATION, language));
                sendMessage.setReplyMarkup(forSendLocation(chatId, activeBranches));
            } else if (text.equals(getMessage(Message.PICK_UP, language))) {
                order.setDelivery(false);
                sendMessage.setText(getMessage(Message.CHOOSE_BRANCH, language));
                sendMessage.setReplyMarkup(forChooseBranch(chatId, activeBranches));
            }
            setUserStep(chatId, StepName.CHOOSE_PAYMENT);
            orderRepository.save(order);
        }
        return sendMessage;
    }

    private ReplyKeyboardMarkup forChooseBranch(String chatId, List<Branch> activeBranches) {
        String language = getUserLanguage(chatId);

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardButton> keyboardButtons = new ArrayList<>();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        if (!activeBranches.isEmpty()) {
            if (language.equals(UZ)) {
                for (Branch branch : activeBranches)
                    keyboardButtons.add(new KeyboardButton(branch.getNameUz()));
            } else {
                for (Branch branch : activeBranches)
                    keyboardButtons.add(new KeyboardButton(branch.getNameRu()));
            }

        for (KeyboardButton keyboardButton : keyboardButtons) {
            row.add(keyboardButton);
            if (row.size() == 2) {
                keyboardRows.add(row);
                row = new KeyboardRow();
            }
        }

        if (!row.isEmpty()) {
            keyboardRows.add(row);
        }
        } else {
            KeyboardButton button = new KeyboardButton(getMessage(Message.BACK_TO_MENU, language));
            row.add(button);
            keyboardRows.add(row);
        }

        markup.setKeyboard(keyboardRows);
        markup.setSelective(true);
        markup.setResizeKeyboard(true);
        return markup;
    }

    private ReplyKeyboardMarkup forSendLocation(String chatId, List<Branch> activeBranches) {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rowList = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();

        KeyboardButton button1 = new KeyboardButton();

        if (!activeBranches.isEmpty()) {
            button1.setText(getMessage(Message.FOR_LOCATION, getUserLanguage(chatId)));
            button1.setRequestLocation(true);
        } else {
            button1.setText(getMessage(Message.BACK_TO_MENU, getUserLanguage(chatId)));
        }

        row1.add(button1);

        rowList.add(row1);
        markup.setKeyboard(rowList);
        markup.setSelective(true);
        markup.setResizeKeyboard(true);
        return markup;
    }

    private static final double EARTH_RADIUS_KM = 6371;

    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        double deltaLat = lat2Rad - lat1Rad;
        double deltaLon = lon2Rad - lon1Rad;

        double a = Math.pow(Math.sin(deltaLat / 2), 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.pow(Math.sin(deltaLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }

    public Branch findClosestBranch(List<Branch> branches, double lat, double lon) {
        if (branches == null || branches.isEmpty()) {
            return null;
        }

        Branch closestBranch = branches.get(0);
        double minDistance = calculateDistance(closestBranch.getLat(), closestBranch.getLon(),
                lat, lon);

        for (int i = 1; i < branches.size(); i++) {
            double distance = calculateDistance(branches.get(i).getLat(), branches.get(i).getLon(),
                    lat, lon);
            if (distance < minDistance) {
                minDistance = distance;
                closestBranch = branches.get(i);
            }
        }

        return closestBranch;
    }

    public SendMessage whenChoosePayment(Update update) {
        String chatId = getChatId(update);
        List<Order> orderList = orderRepository.findAllByUserChatIdOrderByCreatedAtDesc(chatId);
        Order order = orderList.get(0);
        if (update.getMessage().hasLocation()) {
            Location location = update.getMessage().getLocation();
            Double longitude = location.getLongitude();
            Double latitude = location.getLatitude();
            order.setLon(longitude);
            order.setLat(latitude);
            Branch closestBranch = findClosestBranch(branchRepository.findAllByActiveIsTrue(), latitude,
                    longitude);
            double distance = calculateDistance(closestBranch.getLat(), closestBranch.getLon(), latitude, longitude);

            DecimalFormat df = new DecimalFormat("#.##");
            String formattedValue = df.format(distance);
            distance = Double.parseDouble(formattedValue);

            Constants constants = constantsRepository.findById(1L).orElseThrow(
                    () -> RestException.restThrow("CONSTANTS NOT FOUND", HttpStatus.BAD_REQUEST));
            if (distance < constants.getRadiusFreeDelivery() ||
                    order.getPrice() > constants.getMinOrderPriceForFreeDelivery()) {
                order.setDeliveryPrice(0);
            } else {
                float deliveryPrice = (float) (constants.getMinDeliveryPrice() +
                                                        (distance - constants.getRadiusFreeDelivery()) * constants.getPricePerKilometer());
                order.setDeliveryPrice(deliveryPrice);
                order.setTotalPrice(order.getPrice() + deliveryPrice);
            }
        } else if (update.getMessage().hasText() &&
                branchRepository.existsByNameUzOrNameRu(update.getMessage().getText(), update.getMessage().getText())) {
            String text = update.getMessage().getText();
            Branch branch = branchRepository.findByNameUzOrNameRu(text, text);
            order.setBranch(branch);
        }
        orderRepository.save(order);
        SendMessage sendMessage = new SendMessage(chatId, getMessage(Message.CHOOSE_PAYMENT, getUserLanguage(chatId)));
        sendMessage.setReplyMarkup(forChoosePayment(chatId));
        setUserStep(chatId, StepName.LEAVE_COMMENT);
        return sendMessage;
    }

    private ReplyKeyboardMarkup forChoosePayment(String chatId) {

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rowList = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();

        KeyboardButton button1 = new KeyboardButton();
        KeyboardButton button2 = new KeyboardButton();
        KeyboardButton button3 = new KeyboardButton();

        button1.setText(getMessage(Message.CLICK, getUserLanguage(chatId)));
        button2.setText(getMessage(Message.PAYME, getUserLanguage(chatId)));
        button3.setText(getMessage(Message.CASH, getUserLanguage(chatId)));

        row1.add(button1);
        row1.add(button2);
        row2.add(button3);

        rowList.add(row1);
        rowList.add(row2);
        markup.setKeyboard(rowList);
        markup.setSelective(true);
        markup.setResizeKeyboard(true);
        return markup;
    }

    public SendMessage whenOrderRegName(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId, getMessage(Message.ENTER_NAME, getUserLanguage(chatId)));
        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
        setUserStep(chatId, StepName.ORDER_REGISTER_PHONE);
        return sendMessage;
    }

    public SendMessage whenOrderRegPhone(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId, getMessage(Message.ENTER_PHONE_NUMBER, getUserLanguage(chatId)));
        sendMessage.setReplyMarkup(forPhoneNumber(chatId));
        setUserStep(chatId, StepName.IS_DELIVERY);
        return sendMessage;
    }

    public SendMessage whenOrderRegPhone1(Update update) {
        String chatId = getChatId(update);
        String name = update.getMessage().getText();
        TgUser tgUser = tgUserRepository.findByChatId(chatId);
        tgUser.setName(name);
        tgUserRepository.save(tgUser);

        SendMessage sendMessage = new SendMessage(chatId, getMessage(Message.ENTER_PHONE_NUMBER, getUserLanguage(chatId)));
        sendMessage.setReplyMarkup(forPhoneNumber(chatId));
        setUserStep(chatId, StepName.IS_DELIVERY);
        return sendMessage;
    }

    public SendMessage whenLeaveComment(Update update) {
        String chatId = getChatId(update);
        String text = update.getMessage().getText();
        List<Order> orderList = orderRepository.findAllByUserChatIdOrderByCreatedAtDesc(chatId);
        Order order = orderList.get(0);
        if (text.equals(getMessage(Message.CLICK, getUserLanguage(chatId)))) {
            order.setPaymentProviders(paymentProvidersRepository.findByName(ProviderName.CLICK));
        } else if (text.equals(getMessage(Message.PAYME, getUserLanguage(chatId)))) {
            order.setPaymentProviders(paymentProvidersRepository.findByName(ProviderName.PAYME));
        } else if (text.equals(getMessage(Message.CASH, getUserLanguage(chatId)))) {
            order.setPaymentProviders(paymentProvidersRepository.findByName(ProviderName.CASH));
        }
        Order saved = orderRepository.save(order);
        SendMessage sendMessage = new SendMessage(chatId,
                String.format(getMessage(Message.ORDER_MSG_TO_USER, getUserLanguage(chatId)),
                        saved.getPrice(),
                        saved.getDeliveryPrice(),
                        saved.getTotalPrice()) + "\n\n" +
                        getMessage(Message.SEND_COMMENT, getUserLanguage(chatId)));
        sendMessage.setReplyMarkup(forSendComment(update));
        if (saved.getPaymentProviders().getName().equals(ProviderName.CASH)) {
            setUserStep(chatId, StepName.SEND_ORDER_TO_CHANNEL);
        } else {
            setUserStep(chatId, StepName.GO_TO_PAYMENT);
        }
        sendMessage.enableHtml(true);
        return sendMessage;
    }

    private ReplyKeyboardMarkup forSendComment(Update update) {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rowList = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();

        KeyboardButton button1 = new KeyboardButton();

        button1.setText(getMessage(Message.SKIP_COMMENT, getUserLanguage(getChatId(update))));

        row1.add(button1);

        rowList.add(row1);
        markup.setKeyboard(rowList);
        markup.setSelective(true);
        markup.setResizeKeyboard(true);
        return markup;
    }

    public SendMessage whenGoPayment(Update update) {
        String chatId = getChatId(update);
        String text = update.getMessage().getText();
        List<Order> orderList = orderRepository.findAllByUserChatIdOrderByCreatedAtDesc(chatId);
        Order order = orderList.get(0);

        if (!text.equals(getMessage(Message.SKIP_COMMENT, getUserLanguage(chatId)))) {
            order.setComment(text);
        }
        orderRepository.save(order);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        if (order.getPaymentProviders().getName().equals(ProviderName.CLICK)) {
            ResClickOrderDTO dto = createForTg((double) order.getTotalPrice(), chatId).getBody();
            assert dto != null;
            sendMessage.setText(String.format(getMessage(Message.FOR_PAYMENT, getUserLanguage(chatId)), dto.getTransactionParam()));
            sendMessage.setReplyMarkup(forGoPayment(update, dto.getPaymentUrl()));
        } else if (order.getPaymentProviders().getName().equals(ProviderName.PAYME)) {

            String paymentUrl = createPaymentUrl(order.getId(), (int) order.getTotalPrice());
            OrderTransaction newTransaction = new OrderTransaction(paymentUrl,
                    paymeMerchantId, new Timestamp(System.currentTimeMillis()),
                    TransactionState.STATE_IN_PROGRESS, order);
            OrderTransaction save = transactionRepository.save(newTransaction);

            sendMessage.setText(String.format(getMessage(Message.FOR_PAYMENT, getUserLanguage(chatId)), save.getId()));
            sendMessage.setReplyMarkup(forGoPayment(update, paymentUrl));
        }

        return sendMessage;
    }

    public String createPaymentUrl(Long orderId, Integer amount) {
        String formatUrl = "m=" + paymeMerchantId + ";ac.order_id=" + orderId + ";a=" + amount;
        String encoded = Base64.getEncoder().encodeToString(formatUrl.getBytes());
        return "https://checkout.paycom.uz/" + encoded;
    }


    public HttpEntity<ResClickOrderDTO> createForTg(Double amount, String chatId) {
        TgUser tgUser = tgUserRepository.findByChatId(chatId);

        Constants constants = constantsRepository.findById(1L).orElseThrow(
                () -> RestException.restThrow("CONSTANTS NOT FOUND", HttpStatus.BAD_REQUEST));

        if (amount < constants.getMinOrderPrice()) throw RestException.restThrow("MIN ORDER PRICE = " +
                constants.getMinOrderPrice(), HttpStatus.BAD_REQUEST);
        ClickInvoice invoice = ClickInvoice.builder()
                .status(PENDING)
                .user(tgUser)
                .userId(tgUser.getId())
                .price(amount)
                .leftoverAmount(amount)
                .paidAmount(0D)
                .build();
        clickInvoiceRepository.save(invoice);

        ResClickOrderDTO resClickOrderDTO = new ResClickOrderDTO(
                clickMerchantId,
                clickServiceId,
                invoice.getId().toString(),
                amount
        );
        return ResponseEntity.ok(resClickOrderDTO);
    }

    private InlineKeyboardMarkup forGoPayment(Update update, String url) {
        String chatId = getChatId(update);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        InlineKeyboardButton button1 = new InlineKeyboardButton();

        button1.setText(getMessage(Message.GO_PAYMENT, getUserLanguage(chatId)));

        button1.setUrl(url);

        List<InlineKeyboardButton> row1 = new ArrayList<>();

        row1.add(button1);

        rowsInline.add(row1);

        markupInline.setKeyboard(rowsInline);

        return markupInline;
    }


    public SendMessage whenSendOrderToChannel(Update update) {
        String chatId = getChatId(update);
        String language = getUserLanguage(chatId);
        List<Order> orderList = orderRepository.findAllByUserChatIdOrderByCreatedAtDesc(chatId);
        Order order = orderList.get(0);
        order.setOrderStatus(orderStatusRepository.findByName(OrderStatusName.PENDING));
        orderRepository.save(order);
        if (order.getPaymentProviders().getName().equals(ProviderName.CASH)) {
            String text = update.getMessage().getText();
            if (!text.equals(getMessage(Message.SKIP_COMMENT, getUserLanguage(chatId)))) {
                order.setComment(text);
                orderRepository.save(order);
            }
        }

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
                    getMessage(Message.IN_MAP, language));
        } else {
            assert branch != null;
            address = getMessage(Message.WITH_OWN, language) + " - " +
                    String.format("<a href=\"https://yandex.com/navi/?whatshere%%5Bzoom%%5D=18&whatshere%%5Bpoint%%5D=%f%%2C%f&lang=uz&from=navi\">%s</a>",
                    branch.getLon(),
                    branch.getLat(),
                    branchName);
        }
        String format = order.getUpdatedAt().toLocalDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(ORDER_CHANNEL_ID);
        sendMessage.setText(
                String.format(getMessage(Message.ORDER_MSG, language),
                        order.getId(),
                        order.getUser().getName(),
                        order.getUser().getChatId(),
                        order.getUser().getPhoneNumber(),
                        address,
                        format,
                        allOrderedProducts(chatId, order.getId()),
                        order.getComment() == null ? getMessage(Message.NO_INFO, language) : order.getComment(),
                        providerName,
                        order.getPrice(),
                        order.getDeliveryPrice(),
                        order.getTotalPrice(),
                        order.getPaidSum(),
                        orderStatus));
        sendMessage.setReplyMarkup(forSendOrderToChannel(chatId));
        sendMessage.enableHtml(true);
        return sendMessage;
    }

    String allOrderedProducts(String chatId, Long orderId) {
        String language = getUserLanguage(chatId);
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> RestException.restThrow("ID NOT FOUND", HttpStatus.BAD_REQUEST));
        List<OrderProducts> orderProducts = order.getOrderProducts();
        StringBuilder productsMessage = new StringBuilder();
        for (OrderProducts orderProduct : orderProducts) {
            float p = orderProduct.getCount() * orderProduct.getVariation().getPrice();
            if (language.equals("UZ")) {
                productsMessage.append("\n").append(String.format(getMessage(Message.PRODUCTS, language),
                        orderProduct.getVariation().getProduct().getNameUz() + " " +
                        orderProduct.getVariation().getNameUz(),
                        orderProduct.getVariation().getMeasure(),
                        orderProduct.getVariation().getMeasureUnit().getNameUz(),
                        orderProduct.getVariation().getPrice(),
                        orderProduct.getCount(),
                        orderProduct.getVariation().getPrice(),
                        p));
            } else {
                productsMessage.append("\n").append(String.format(getMessage(Message.PRODUCTS, language),
                        orderProduct.getVariation().getProduct().getNameRu() + " " +
                        orderProduct.getVariation().getNameRu(),
                        orderProduct.getVariation().getMeasure(),
                        orderProduct.getVariation().getMeasureUnit().getNameRu(),
                        orderProduct.getVariation().getPrice(),
                        orderProduct.getCount(),
                        orderProduct.getVariation().getPrice(),
                        p));
            }
        }
        return productsMessage.toString();
    }

    InlineKeyboardMarkup forSendOrderToChannel(String chatId) {
        List<Order> orderList = orderRepository.findAllByUserChatIdOrderByCreatedAtDesc(chatId);
        Order order = orderList.get(0);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        InlineKeyboardButton button2 = new InlineKeyboardButton();

        button1.setText(getMessage(Message.ACCEPT, getUserLanguage(chatId)));
        button2.setText(getMessage(Message.REJECT, getUserLanguage(chatId)));

        button1.setCallbackData("accept" + order.getId());
        button2.setCallbackData("reject" + order.getId());

        List<InlineKeyboardButton> row1 = new ArrayList<>();

        row1.add(button1);
        row1.add(button2);

        rowsInline.add(row1);

        markupInline.setKeyboard(rowsInline);

        return markupInline;
    }

    public EditMessageText whenAcceptOrRejectOrder(String data, Update update) {
        String chatId = getChatId(update);
        String language = getUserLanguage(chatId);
        Long id = Long.valueOf(data.substring(6));
        Order order = orderRepository.findById(id).orElseThrow(
                () -> RestException.restThrow("ID NOT FOUND", HttpStatus.BAD_REQUEST));
        if (data.startsWith("accept"))
            order.setOrderStatus(orderStatusRepository.findByName(OrderStatusName.ACCEPTED));
        else if (data.startsWith("reject"))
            order.setOrderStatus(orderStatusRepository.findByName(OrderStatusName.REJECTED));
        Order savedOrder = orderRepository.save(order);
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
                    getMessage(Message.IN_MAP, language));
        } else {
            assert branch != null;
            address = getMessage(Message.WITH_OWN, language) + " - " +
                    String.format("<a href=\"https://yandex.com/navi/?whatshere%%5Bzoom%%5D=18&whatshere%%5Bpoint%%5D=%f%%2C%f&lang=uz&from=navi\">%s</a>",
                    branch.getLon(),
                    branch.getLat(),
                    branchName);
        }

        String format = order.getUpdatedAt().toLocalDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(ORDER_CHANNEL_ID);
        editMessageText.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        editMessageText.enableHtml(true);
        editMessageText.setText(
                String.format(getMessage(Message.ORDER_MSG, language),
                        order.getId(),
                        order.getUser().getName(),
                        order.getUser().getChatId(),
                        order.getUser().getPhoneNumber(),
                        address,
                        format,
                        allOrderedProducts(chatId, order.getId()),
                        savedOrder.getComment() == null ? getMessage(Message.NO_INFO, language) : order.getComment(),
                        providerName,
                        order.getPrice(),
                        order.getDeliveryPrice(),
                        order.getTotalPrice(),
                        order.getPaidSum(),
                        orderStatus));
        return editMessageText;
    }

    public SendMessage whenSendResToUser(String data) {
        Long id = Long.valueOf(data.substring(6));
        Order order = orderRepository.findById(id).orElseThrow(
                () -> RestException.restThrow("ID NOT FOUND", HttpStatus.BAD_REQUEST));

        String chatId = order.getUser().getChatId();
        String language = getUserLanguage(chatId);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        if (order.getOrderStatus().getName().equals(OrderStatusName.ACCEPTED)) {
            sendMessage.setText(String.format(getMessage(Message.ACCEPTED, language), id));
        } else if (order.getOrderStatus().getName().equals(OrderStatusName.REJECTED)) {
            sendMessage.setText(String.format(getMessage(Message.REJECTED, language), id));
        }
        sendMessage.enableHtml(true);
        sendMessage.setReplyMarkup(forMainMenu(chatId));
        setUserStep(chatId, StepName.CHOOSE_FROM_MAIN_MENU);
        return sendMessage;
    }
}
