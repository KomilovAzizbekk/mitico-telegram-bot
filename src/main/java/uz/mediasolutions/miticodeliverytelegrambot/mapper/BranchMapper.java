package uz.mediasolutions.miticodeliverytelegrambot.mapper;

import org.mapstruct.Mapper;
import uz.mediasolutions.miticodeliverytelegrambot.entity.Branch;
import uz.mediasolutions.miticodeliverytelegrambot.payload.BranchDTO;

@Mapper(componentModel = "spring")
public interface BranchMapper {

    BranchDTO toDTO(Branch branch);

    Branch toEntity(BranchDTO branchDTO);

}
