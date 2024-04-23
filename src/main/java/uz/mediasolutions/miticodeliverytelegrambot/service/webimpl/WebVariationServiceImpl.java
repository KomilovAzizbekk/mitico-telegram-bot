package uz.mediasolutions.miticodeliverytelegrambot.service.webimpl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uz.mediasolutions.miticodeliverytelegrambot.entity.MeasureUnit;
import uz.mediasolutions.miticodeliverytelegrambot.entity.Product;
import uz.mediasolutions.miticodeliverytelegrambot.entity.TgUser;
import uz.mediasolutions.miticodeliverytelegrambot.entity.Variation;
import uz.mediasolutions.miticodeliverytelegrambot.enums.LanguageName;
import uz.mediasolutions.miticodeliverytelegrambot.exceptions.RestException;
import uz.mediasolutions.miticodeliverytelegrambot.manual.ApiResult;
import uz.mediasolutions.miticodeliverytelegrambot.mapper.UniversalMapper;
import uz.mediasolutions.miticodeliverytelegrambot.payload.MeasureUnitWebDTO;
import uz.mediasolutions.miticodeliverytelegrambot.payload.Product2WebDTO;
import uz.mediasolutions.miticodeliverytelegrambot.payload.VariationWebDTO;
import uz.mediasolutions.miticodeliverytelegrambot.repository.TgUserRepository;
import uz.mediasolutions.miticodeliverytelegrambot.repository.VariationRepository;
import uz.mediasolutions.miticodeliverytelegrambot.service.webabs.WebVariationService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WebVariationServiceImpl implements WebVariationService {

    private final VariationRepository variationRepository;
    private final TgUserRepository tgUserRepository;
    private final UniversalMapper universalMapper;

    @Override
    public ApiResult<List<VariationWebDTO>> getAllByProductId(String chatId, Long productId) {
        if (tgUserRepository.existsByChatId(chatId)) {
            List<Variation> variations = variationRepository.findAllByProductIdAndActiveIsTrueOrderByNumberAsc(productId);
            List<VariationWebDTO> variationWebDTOList = universalMapper.toVariationWebDTOList(variations, chatId);
            return ApiResult.success(variationWebDTOList);
        } else {
            throw RestException.restThrow("USER ID NOT FOUND", HttpStatus.BAD_REQUEST);
        }
    }

}
