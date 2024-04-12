package uz.mediasolutions.miticodeliverytelegrambot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProviderName {

    PAYME("Payme", "Payme"),
    CLICK("Click", "Click"),
    CASH("Naqd", "Наличные");

    private final String nameUz;

    private final String nameRu;

}
