package uz.mediasolutions.miticodeliverytelegrambot.controller.abs;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import uz.mediasolutions.miticodeliverytelegrambot.manual.ApiResult;
import uz.mediasolutions.miticodeliverytelegrambot.payload.ProductDTO;
import uz.mediasolutions.miticodeliverytelegrambot.payload.ProductResDTO;
import uz.mediasolutions.miticodeliverytelegrambot.utills.constants.Rest;

import javax.validation.Valid;
import java.io.IOException;

@RequestMapping(ProductController.PRODUCT)
public interface ProductController {

    String PRODUCT = Rest.BASE_PATH + "products/";

    String GET_ALL = "get-all";

    String GET_BY_ID = "get/{id}";

    String ADD = "add";

    String EDIT = "edit/{id}";

    String DELETE = "delete/{id}";

    @GetMapping(GET_ALL)
    ApiResult<Page<ProductResDTO>> getAll(@RequestParam(defaultValue = Rest.DEFAULT_PAGE_NUMBER) int page,
                                          @RequestParam(defaultValue = Rest.DEFAULT_PAGE_SIZE) int size,
                                          @RequestParam(defaultValue = "null", required = false) String search,
                                          @RequestParam(defaultValue = "false") boolean active);

    @GetMapping(GET_BY_ID)
    ApiResult<ProductResDTO> getById(@PathVariable Long id);

    @PostMapping(ADD)
    ApiResult<?> add(@RequestBody @Valid ProductDTO dto);

    @PutMapping(EDIT)
    ApiResult<?> edit(@PathVariable Long id, @RequestBody @Valid ProductDTO dto) throws IOException;

    @DeleteMapping(DELETE)
    ApiResult<?> delete(@PathVariable Long id) throws IOException;

}
