package uz.mediasolutions.miticodeliverytelegrambot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uz.mediasolutions.miticodeliverytelegrambot.entity.Branch;
import uz.mediasolutions.miticodeliverytelegrambot.exceptions.RestException;
import uz.mediasolutions.miticodeliverytelegrambot.manual.ApiResult;
import uz.mediasolutions.miticodeliverytelegrambot.mapper.BranchMapper;
import uz.mediasolutions.miticodeliverytelegrambot.payload.BranchDTO;
import uz.mediasolutions.miticodeliverytelegrambot.payload.LocationDTO;
import uz.mediasolutions.miticodeliverytelegrambot.repository.BranchRepository;
import uz.mediasolutions.miticodeliverytelegrambot.service.abs.BranchService;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;
    private final BranchMapper branchMapper;

    @Override
    public ApiResult<Page<BranchDTO>> getAll(int page, int size, String name) {
        Pageable pageable = PageRequest.of(page, size);
        if (!name.equals("null")) {
            Page<Branch> branches = branchRepository
                    .findAllByAddressRuContainsIgnoreCaseOrAddressUzContainsIgnoreCaseOrNameRuContainsIgnoreCaseOrNameUzContainsIgnoreCaseOrderByCreatedAtDesc(
                    name, name, name, name, pageable);
            Page<BranchDTO> dtos = branches.map(branchMapper::toDTO);
            return ApiResult.success(dtos);
        }
        Page<Branch> branches = branchRepository.findAllByOrderByCreatedAtDesc(pageable);
        Page<BranchDTO> dtos = branches.map(branchMapper::toDTO);
        return ApiResult.success(dtos);
    }

    @Override
    public ApiResult<BranchDTO> getById(Long id) {
        Branch branch = branchRepository.findById(id).orElseThrow(
                () -> RestException.restThrow("ID NOT FOUND", HttpStatus.BAD_REQUEST));
        BranchDTO dto = branchMapper.toDTO(branch);
        return ApiResult.success(dto);
    }

    @Override
    public ApiResult<?> add(BranchDTO branchDTO) {
        if (branchRepository.existsByNameUzOrNameRu(branchDTO.getNameUz(), branchDTO.getNameRu())) {
            throw RestException.restThrow("NAME ALREADY EXISTED", HttpStatus.BAD_REQUEST);
        } else {
            Branch entity = branchMapper.toEntity(branchDTO);
            branchRepository.save(entity);
            return ApiResult.success("SAVED SUCCESSFULLY");
        }
    }

    @Override
    public ApiResult<?> edit(Long id, BranchDTO branchDTO) {
        Branch branch = branchRepository.findById(id).orElseThrow(
                () -> RestException.restThrow("ID NOT FOUND", HttpStatus.BAD_REQUEST));

//        if (branchRepository.existsByNameUzOrNameRu(branchDTO.getNameUz(), branchDTO.getNameRu()) &&
//                !branchRepository.findByNameUzOrNameRu(branchDTO.getNameUz(), branchDTO.getNameRu())
//                        .equals(branch)) {
//            throw RestException.restThrow("NAME ALREADY EXISTED", HttpStatus.BAD_REQUEST);
//        } else {
            branch.setNameUz(branchDTO.getNameUz());
            branch.setNameRu(branchDTO.getNameRu());
            branch.setAddressUz(branchDTO.getAddressUz());
            branch.setAddressRu(branchDTO.getAddressRu());
            branch.setActive(branchDTO.isActive());
            branch.setClosingTime(branchDTO.getClosingTime());
            branch.setOpeningTime(branchDTO.getOpeningTime());
            branch.setClosesAfterMn(branchDTO.isClosesAfterMn());
            branchRepository.save(branch);
            return ApiResult.success("EDITED SUCCESSFULLY");
//        }
    }

    @Override
    public ApiResult<?> delete(Long id) {
        try {
            branchRepository.deleteById(id);
            return ApiResult.success("DELETED SUCCESSFULLY");
        } catch (Exception e) {
            throw RestException.restThrow("CANNOT DELETE", HttpStatus.CONFLICT);
        }
    }

    @Override
    public ApiResult<?> editLocation(Long id, LocationDTO locationDTO) {
        Branch branch = branchRepository.findById(id).orElseThrow(
                () -> RestException.restThrow("ID NOT FOUND", HttpStatus.BAD_REQUEST));
        branch.setLat(locationDTO.getLat());
        branch.setLon(locationDTO.getLon());
        branchRepository.save(branch);
        return ApiResult.success("EDITED SUCCESSFULLY");
    }

    @Override
    public ApiResult<?> hasActive() {
        Sort sort = Sort.by(Sort.Order.asc("createdAt"));
        List<Branch> branches = branchRepository.findAllByActiveIsTrue(sort);
        List<Branch> activeBranches = new ArrayList<>();

        if (branches.isEmpty()) {
            return ApiResult.success(false);
        } else {
            for (Branch branch : branches) {
                if (!branch.isClosesAfterMn()) {
                    if (branch.getOpeningTime().isBefore(LocalTime.now()) &&
                            branch.getClosingTime().isAfter(LocalTime.now())) {
                        activeBranches.add(branch);
                    }
                } else {
                    if (branch.getOpeningTime().isBefore(LocalTime.now())) {
                        if (branch.getClosingTime().isBefore(LocalTime.now())) {
                            activeBranches.add(branch);
                        }
                    } else {
                        if (branch.getClosingTime().isAfter(LocalTime.now())) {
                            activeBranches.add(branch);
                        }
                    }
                }
            }
            if (activeBranches.isEmpty()) {
                return ApiResult.success(false);
            } else {
                return ApiResult.success(true);
            }
        }
    }
}
