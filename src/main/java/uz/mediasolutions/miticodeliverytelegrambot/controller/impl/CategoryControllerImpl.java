package uz.mediasolutions.miticodeliverytelegrambot.controller.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RestController;
import uz.mediasolutions.miticodeliverytelegrambot.controller.abs.CategoryController;
import uz.mediasolutions.miticodeliverytelegrambot.manual.ApiResult;
import uz.mediasolutions.miticodeliverytelegrambot.payload.CategoryDTO;
import uz.mediasolutions.miticodeliverytelegrambot.service.abs.CategoryService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class CategoryControllerImpl implements CategoryController {

    private final CategoryService categoryService;

    @Override
    public ApiResult<Page<CategoryDTO>> getAll(int page, int size, String search, boolean active) {
        return categoryService.getAll(page, size, search, active);
    }

    @Override
    public ApiResult<CategoryDTO> getById(Long id) {
        return categoryService.getById(id);
    }

    @Override
    public ApiResult<?> add(CategoryDTO categoryDTO) {
        return categoryService.add(categoryDTO);
    }

    @Override
    public ApiResult<?> edit(Long id, CategoryDTO categoryDTO) throws IOException {
        return categoryService.edit(id, categoryDTO);
    }

    @Override
    public ApiResult<?> delete(Long id) throws IOException {
        return categoryService.delete(id);
    }
}
