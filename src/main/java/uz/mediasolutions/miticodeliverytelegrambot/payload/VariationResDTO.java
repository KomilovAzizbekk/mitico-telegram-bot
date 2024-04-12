package uz.mediasolutions.miticodeliverytelegrambot.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VariationResDTO {

    private Long id;

    private Long number;

    private String nameUz;

    private String nameRu;

    private MeasureUnitDTO measureUnit;

    private float measure;

    private float price;

    private ProductDTO product;

}
