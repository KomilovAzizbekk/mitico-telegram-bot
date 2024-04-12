package uz.mediasolutions.miticodeliverytelegrambot.controller.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RestController;
import uz.mediasolutions.miticodeliverytelegrambot.controller.abs.ProductController;
import uz.mediasolutions.miticodeliverytelegrambot.manual.ApiResult;
import uz.mediasolutions.miticodeliverytelegrambot.payload.ProductDTO;
import uz.mediasolutions.miticodeliverytelegrambot.payload.ProductResDTO;
import uz.mediasolutions.miticodeliverytelegrambot.service.abs.ProductService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class ProductControllerImpl implements ProductController {

    private final ProductService productService;

    @Override
    public ApiResult<Page<ProductResDTO>> getAll(int page, int size, String search, boolean active) {
        return productService.getAll(page, size, search, active);
    }

    @Override
    public ApiResult<ProductResDTO> getById(Long id) {
        return productService.getById(id);
    }

    @Override
    public ApiResult<?> add(ProductDTO dto) {
        return productService.add(dto);
    }

    @Override
    public ApiResult<?> edit(Long id, ProductDTO dto) throws IOException {
        return productService.edit(id, dto);
    }

    @Override
    public ApiResult<?> delete(Long id) throws IOException {
        return productService.delete(id);
    }
}
