package uz.mediasolutions.miticodeliverytelegrambot.payload;

import lombok.Data;

@Data
public class TranslateDto {

    private Long id;

    private String key;

    private String textUz;

    private String textRu;
}