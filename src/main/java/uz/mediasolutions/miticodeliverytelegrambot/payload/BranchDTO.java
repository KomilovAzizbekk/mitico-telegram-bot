package uz.mediasolutions.miticodeliverytelegrambot.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

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
public class BranchDTO {

    private Long id;

    private String nameUz;

    private String nameRu;

    private Double lon;

    private Double lat;

    private String addressUz;

    private String addressRu;

    @JsonProperty(value = "openingTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @ApiModelProperty(required = true, example = "22:00")
    @Schema(name = "openingTime", format = "HH:mm", example = "22:00")
    private LocalTime openingTime;

    @JsonProperty(value = "closingTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @ApiModelProperty(required = true, example = "22:00")
    @Schema(name = "closingTime", format = "HH:mm", example = "22:00")
    private LocalTime closingTime;

    private boolean closesAfterMn;

    private boolean active;

}
