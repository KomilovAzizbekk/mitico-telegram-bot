package uz.mediasolutions.miticodeliverytelegrambot.controller.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import uz.mediasolutions.miticodeliverytelegrambot.controller.abs.LanguageController;
import uz.mediasolutions.miticodeliverytelegrambot.entity.LanguagePs;
import uz.mediasolutions.miticodeliverytelegrambot.manual.ApiResult;
import uz.mediasolutions.miticodeliverytelegrambot.payload.TranslateDto;
import uz.mediasolutions.miticodeliverytelegrambot.service.abs.LanguageServicePs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class LanguageControllerImpl implements LanguageController {

    private final LanguageServicePs languageServicePs;

    @Override
    public ApiResult<Page<LanguagePs>> getAllPageable(int page, int size, String search) {
        return languageServicePs.getAllPaginated(page, size, search);
    }

    @Override
    public ResponseEntity<Map<String, String>> getAllByLang(String language) {
        return languageServicePs.getAllByLanguage(language);
    }

    @Override
    public ApiResult<?> createTranslation(TranslateDto dto) {
        return languageServicePs.createTranslation(dto);
    }

    @Override
    public ApiResult<?> createMainKey(List<TranslateDto> dtos) {
        return languageServicePs.createMainText(dtos);
    }

    @Override
    public ApiResult<?> createKey(HashMap<String, String> dto) {
        return languageServicePs.createKey(dto);
    }
}
