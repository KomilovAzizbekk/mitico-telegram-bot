package uz.mediasolutions.miticodeliverytelegrambot.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MeasureUnitWebDTO {

    private Long id;

    private String name;

}
