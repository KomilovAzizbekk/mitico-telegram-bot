package uz.mediasolutions.miticodeliverytelegrambot.manual;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorData {

    //USERGA BORADIGAN XABAR
    private String errorMsg;

    //QAYSI FIELD XATO EKANLIGI
    private String fieldName;

    private Integer fieldValue;

    //XATOLIK KODI
    private Integer errorCode;

    public ErrorData(String errorMsg, Integer errorCode) {
        this.errorMsg = errorMsg;
        this.errorCode = errorCode;
    }

    public ErrorData(String errorMsg, Integer fieldValue, Integer errorCode) {
        this.errorMsg = errorMsg;
        this.fieldValue = fieldValue;
        this.errorCode = errorCode;
    }

    public ErrorData(String errorMsg, String fieldName, Integer errorCode) {
        this.errorMsg = errorMsg;
        this.fieldName = fieldName;
        this.errorCode = errorCode;
    }

}