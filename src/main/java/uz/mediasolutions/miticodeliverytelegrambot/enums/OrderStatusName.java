package uz.mediasolutions.miticodeliverytelegrambot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderStatusName {

    NOT_COMPLETE("Tugallanmagan", "Не завершен"),
    PENDING("Tasdiqlanmagan", "Не подтверждено"),
    ACCEPTED("Tasdiqlangan", "Подтвержденный"),
    REJECTED("Rad etilgan", "Отклоненный"),
//    PAID("To'langan", "Оплаченный"),
    DELIVERED("Yetkazib berilgan", "Доставленный");

    private final String nameUz;

    private final String nameRu;

}
