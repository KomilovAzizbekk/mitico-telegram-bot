package uz.mediasolutions.miticodeliverytelegrambot.service.abs;

import org.springframework.data.domain.Page;
import uz.mediasolutions.miticodeliverytelegrambot.manual.ApiResult;
import uz.mediasolutions.miticodeliverytelegrambot.payload.BranchDTO;
import uz.mediasolutions.miticodeliverytelegrambot.payload.LocationDTO;

public interface BranchService {
    ApiResult<Page<BranchDTO>> getAll(int page, int size, String name);

    ApiResult<BranchDTO> getById(Long id);

    ApiResult<?> add(BranchDTO branchDTO);

    ApiResult<?> edit(Long id, BranchDTO branchDTO);

    ApiResult<?> delete(Long id);

    ApiResult<?> editLocation(Long id, LocationDTO locationDTO);

    ApiResult<?> hasActive();

}
