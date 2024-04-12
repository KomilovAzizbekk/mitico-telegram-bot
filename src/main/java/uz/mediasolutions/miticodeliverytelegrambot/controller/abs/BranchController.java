package uz.mediasolutions.miticodeliverytelegrambot.controller.abs;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import uz.mediasolutions.miticodeliverytelegrambot.manual.ApiResult;
import uz.mediasolutions.miticodeliverytelegrambot.payload.BranchDTO;
import uz.mediasolutions.miticodeliverytelegrambot.payload.CategoryDTO;
import uz.mediasolutions.miticodeliverytelegrambot.payload.LocationDTO;
import uz.mediasolutions.miticodeliverytelegrambot.utills.constants.Rest;

import javax.validation.Valid;

@RequestMapping(BranchController.BRANCH)
public interface BranchController {

    String BRANCH = Rest.BASE_PATH + "branches/";

    String GET_ALL = "get-all";

    String ACTIVE = "active";

    String GET_BY_ID = "get/{id}";

    String ADD = "add";

    String EDIT = "edit/{id}";

    String EDIT_LOCATION = "edit-loc/{id}";

    String DELETE = "delete/{id}";

    @GetMapping(ACTIVE)
    ApiResult<?> hasActive();

    @GetMapping(GET_ALL)
    ApiResult<Page<BranchDTO>> getAll(@RequestParam(defaultValue = Rest.DEFAULT_PAGE_NUMBER) int page,
                                      @RequestParam(defaultValue = Rest.DEFAULT_PAGE_SIZE) int size,
                                      @RequestParam(defaultValue = "null", required = false) String search);


    @GetMapping(GET_BY_ID)
    ApiResult<BranchDTO> getById(@PathVariable Long id);

    @PostMapping(ADD)
    ApiResult<?> add(@RequestBody @Valid BranchDTO branchDTO);

    @PutMapping(EDIT)
    ApiResult<?> edit(@PathVariable Long id, @RequestBody @Valid BranchDTO branchDTO);

    @PutMapping(EDIT_LOCATION)
    ApiResult<?> editLocation(@PathVariable Long id, @RequestBody @Valid LocationDTO locationDTO);

    @DeleteMapping(DELETE)
    ApiResult<?> delete(@PathVariable Long id);


}
