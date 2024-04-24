package uz.mediasolutions.miticodeliverytelegrambot.payload.payme;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Params {

    private String id;

    private Account account;

    private Long amount;

    private Long time;

    private Integer reason;

    private Long from;

    private Long to;
}
