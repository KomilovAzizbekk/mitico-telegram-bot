package uz.mediasolutions.miticodeliverytelegrambot.controller.abs;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.mediasolutions.miticodeliverytelegrambot.manual.ApiResult;
import uz.mediasolutions.miticodeliverytelegrambot.payload.BannerDTO;
import uz.mediasolutions.miticodeliverytelegrambot.utills.constants.Rest;

import javax.validation.Valid;

@RequestMapping(BannerController.BANNER)
public interface BannerController {

    String BANNER = Rest.BASE_PATH + "banner/";

    String GET_ALL = "get-all";

    String GET = "/{id}";

    String UPLOAD = "upload";

    String DELETE = "delete/{id}";  

    @GetMapping(GET_ALL)
    ApiResult<Page<BannerDTO>> getAll(@RequestParam(defaultValue = Rest.DEFAULT_PAGE_NUMBER) int page,
                                      @RequestParam(defaultValue = Rest.DEFAULT_PAGE_SIZE) int size);


    @GetMapping(GET)
    ApiResult<BannerDTO> get(@PathVariable Long id);

    @PostMapping(UPLOAD)
    ApiResult<?> upload(@RequestBody @Valid BannerDTO dto);

    @DeleteMapping(DELETE)
    ApiResult<?> delete(@PathVariable Long id);

}
