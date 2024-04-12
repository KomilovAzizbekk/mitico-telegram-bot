package uz.mediasolutions.miticodeliverytelegrambot.controller.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RestController;
import uz.mediasolutions.miticodeliverytelegrambot.controller.abs.BranchController;
import uz.mediasolutions.miticodeliverytelegrambot.manual.ApiResult;
import uz.mediasolutions.miticodeliverytelegrambot.payload.BranchDTO;
import uz.mediasolutions.miticodeliverytelegrambot.payload.LocationDTO;
import uz.mediasolutions.miticodeliverytelegrambot.service.abs.BranchService;

@RestController
@RequiredArgsConstructor
public class BranchControllerImpl implements BranchController {

    private final BranchService branchService;

    @Override
    public ApiResult<?> hasActive() {
        return branchService.hasActive();
    }

    @Override
    public ApiResult<Page<BranchDTO>> getAll(int page, int size, String search) {
        return branchService.getAll(page, size, search);
    }

    @Override
    public ApiResult<BranchDTO> getById(Long id) {
        return branchService.getById(id);
    }

    @Override
    public ApiResult<?> add(BranchDTO branchDTO) {
        return branchService.add(branchDTO);
    }

    @Override
    public ApiResult<?> edit(Long id, BranchDTO branchDTO) {
        return branchService.edit(id, branchDTO);
    }

    @Override
    public ApiResult<?> editLocation(Long id, LocationDTO locationDTO) {
        return branchService.editLocation(id, locationDTO);
    }

    @Override
    public ApiResult<?> delete(Long id) {
        return branchService.delete(id);
    }
}
