package uz.mediasolutions.miticodeliverytelegrambot.payload.payme;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Account {

    private Long order;

}
