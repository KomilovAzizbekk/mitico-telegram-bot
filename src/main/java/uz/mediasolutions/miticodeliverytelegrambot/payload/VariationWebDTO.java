package uz.mediasolutions.miticodeliverytelegrambot.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VariationWebDTO {

    private Long id;

    private String name;

    private Product2WebDTO product;

    private MeasureUnitWebDTO measureUnit;

    private float measure;

    private float price;

}
