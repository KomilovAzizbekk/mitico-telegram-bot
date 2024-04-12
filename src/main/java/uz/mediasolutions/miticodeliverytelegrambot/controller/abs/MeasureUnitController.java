package uz.mediasolutions.miticodeliverytelegrambot.controller.abs;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import uz.mediasolutions.miticodeliverytelegrambot.manual.ApiResult;
import uz.mediasolutions.miticodeliverytelegrambot.payload.BranchDTO;
import uz.mediasolutions.miticodeliverytelegrambot.payload.LocationDTO;
import uz.mediasolutions.miticodeliverytelegrambot.payload.MeasureUnitDTO;
import uz.mediasolutions.miticodeliverytelegrambot.utills.constants.Rest;

import javax.validation.Valid;

@RequestMapping(MeasureUnitController.MEASURE_UNIT)
public interface MeasureUnitController {

    String MEASURE_UNIT = Rest.BASE_PATH + "measure-unit/";

    String GET_ALL = "get-all";

    String GET_BY_ID = "get/{id}";

    String ADD = "add";

    String EDIT = "edit/{id}";

    String DELETE = "delete/{id}";

    @GetMapping(GET_ALL)
    ApiResult<Page<MeasureUnitDTO>> getAll(@RequestParam(defaultValue = Rest.DEFAULT_PAGE_NUMBER) int page,
                                           @RequestParam(defaultValue = Rest.DEFAULT_PAGE_SIZE) int size,
                                           @RequestParam(defaultValue = "null", required = false) String search);


    @GetMapping(GET_BY_ID)
    ApiResult<MeasureUnitDTO> getById(@PathVariable Long id);

    @PostMapping(ADD)
    ApiResult<?> add(@RequestBody @Valid MeasureUnitDTO measureUnitDTO);

    @PutMapping(EDIT)
    ApiResult<?> edit(@PathVariable Long id, @RequestBody @Valid MeasureUnitDTO measureUnitDTO);

    @DeleteMapping(DELETE)
    ApiResult<?> delete(@PathVariable Long id);


}
