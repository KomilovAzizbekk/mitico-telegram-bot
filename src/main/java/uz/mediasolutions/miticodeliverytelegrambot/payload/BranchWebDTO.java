package uz.mediasolutions.miticodeliverytelegrambot.payload;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BranchWebDTO {

    private Long id;

    private String name;

    private Double lon;

    private Double lat;

    private String address;

    private LocalTime openingTime;

    private LocalTime closingTime;

}
