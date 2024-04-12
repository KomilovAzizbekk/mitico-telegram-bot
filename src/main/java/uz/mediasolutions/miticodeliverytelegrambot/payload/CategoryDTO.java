package uz.mediasolutions.miticodeliverytelegrambot.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDTO {

    private Long id;

    private Long number;

    private String nameUz;

    private String nameRu;

    private String descriptionUz;

    private String descriptionRu;

    private boolean active;

    private String imageUrl;

}
