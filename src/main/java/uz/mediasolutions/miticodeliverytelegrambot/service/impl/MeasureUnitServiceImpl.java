package uz.mediasolutions.miticodeliverytelegrambot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uz.mediasolutions.miticodeliverytelegrambot.entity.MeasureUnit;
import uz.mediasolutions.miticodeliverytelegrambot.exceptions.RestException;
import uz.mediasolutions.miticodeliverytelegrambot.manual.ApiResult;
import uz.mediasolutions.miticodeliverytelegrambot.mapper.MeasureUnitMapper;
import uz.mediasolutions.miticodeliverytelegrambot.payload.MeasureUnitDTO;
import uz.mediasolutions.miticodeliverytelegrambot.repository.MeasureUnitRepository;
import uz.mediasolutions.miticodeliverytelegrambot.service.abs.MeasureUnitService;

@Service
@RequiredArgsConstructor
public class MeasureUnitServiceImpl implements MeasureUnitService {

    private final MeasureUnitRepository measureUnitRepository;
    private final MeasureUnitMapper measureUnitMapper;

    @Override
    public ApiResult<Page<MeasureUnitDTO>> getAll(int page, int size, String name) {
        Pageable pageable = PageRequest.of(page, size);
        if (!name.equals("null")) {
            Page<MeasureUnit> measureUnits = measureUnitRepository.findAllByNameRuContainsIgnoreCaseOrNameUzContainsIgnoreCaseOrderByCreatedAtDesc(
                    name, name, pageable);
            Page<MeasureUnitDTO> dtos = measureUnits.map(measureUnitMapper::toDTO);
            return ApiResult.success(dtos);
        }
        Page<MeasureUnit> measureUnits = measureUnitRepository.findAllByOrderByCreatedAtDesc(pageable);
        Page<MeasureUnitDTO> dtos = measureUnits.map(measureUnitMapper::toDTO);
        return ApiResult.success(dtos);
    }

    @Override
    public ApiResult<MeasureUnitDTO> getById(Long id) {
        MeasureUnit measureUnit = measureUnitRepository.findById(id).orElseThrow(
                () -> RestException.restThrow("ID NOT FOUND", HttpStatus.BAD_REQUEST));
        MeasureUnitDTO dto = measureUnitMapper.toDTO(measureUnit);
        return ApiResult.success(dto);
    }

    @Override
    public ApiResult<?> add(MeasureUnitDTO dto) {
        if (measureUnitRepository.existsByNameUzOrNameRu(dto.getNameUz(), dto.getNameRu())) {
            throw RestException.restThrow("NAME ALREADY EXISTED", HttpStatus.BAD_REQUEST);
        } else {
            MeasureUnit entity = measureUnitMapper.toEntity(dto);
            measureUnitRepository.save(entity);
            return ApiResult.success("SAVED SUCCESSFULLY");
        }
    }

    @Override
    public ApiResult<?> edit(Long id, MeasureUnitDTO dto) {
        MeasureUnit measureUnit = measureUnitRepository.findById(id).orElseThrow(
                    () -> RestException.restThrow("ID NOT FOUND", HttpStatus.BAD_REQUEST));
        measureUnit.setNameUz(dto.getNameUz());
        measureUnit.setNameRu(dto.getNameRu());
        measureUnitRepository.save(measureUnit);
        return ApiResult.success("EDITED SUCCESSFULLY");
    }

    @Override
    public ApiResult<?> delete(Long id) {
        try {
            measureUnitRepository.deleteById(id);
            return ApiResult.success("DELETED SUCCESSFULLY");
        } catch (Exception e) {
            throw RestException.restThrow("CANNOT DELETE", HttpStatus.CONFLICT);
        }
    }
}
