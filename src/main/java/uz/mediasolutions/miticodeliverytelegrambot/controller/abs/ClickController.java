package uz.mediasolutions.miticodeliverytelegrambot.controller.abs;

import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.mediasolutions.miticodeliverytelegrambot.payload.click.ClickInvoiceDTO;
import uz.mediasolutions.miticodeliverytelegrambot.payload.click.ClickOrderDTO;
import uz.mediasolutions.miticodeliverytelegrambot.payload.click.CreateCardTokenDTO;
import uz.mediasolutions.miticodeliverytelegrambot.utills.constants.Rest;

@RequestMapping(ClickController.CLICK)
public interface ClickController {

    String CLICK = Rest.BASE_PATH + "click/merchant/";
    String CREATE_INVOICE = "create/invoice";
    String STATUS_INVOICE = "status/invoice/{serviceId}/{invoiceId}";
    String PAYMENT_STATUS = "payment/status_by_mti/{serviceId}/{merchantTransId}";
    String PAYMENT_REVERSAL = "payment/reversal/{serviceId}/{paymentId}";
    String CREATE_CARD_TOKEN = "card_token/request";
    String CREATE = "create";
    String PREPARE = "prepare";
    String COMPLETE = "complete";
    String GET_INFO = "get-info";

    @PostMapping(CREATE_INVOICE)
    HttpEntity<?> createInvoice(@RequestBody ClickInvoiceDTO dto,
                                @RequestParam("chatId") String chatId);

    @PostMapping(CREATE)
    HttpEntity<?> create(@RequestParam("amount") Double amount,
                         @RequestParam("chatId") String chatId);

    @RequestMapping(method = RequestMethod.POST, value = PREPARE)
    @ResponseBody
    ClickOrderDTO prepareMethod(@ModelAttribute ClickOrderDTO clickDTO) throws TelegramApiException;

    @RequestMapping(method = RequestMethod.POST, value = COMPLETE)
    @ResponseBody
    ClickOrderDTO completeMethod(@ModelAttribute ClickOrderDTO clickDTO) throws TelegramApiException;

    @RequestMapping(method = RequestMethod.POST, value = GET_INFO)
    @ResponseBody
    ClickOrderDTO getInfo(@ModelAttribute ClickOrderDTO clickDTO);
}
