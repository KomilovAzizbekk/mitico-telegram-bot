package uz.mediasolutions.miticodeliverytelegrambot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uz.mediasolutions.miticodeliverytelegrambot.entity.TgUser;
import uz.mediasolutions.miticodeliverytelegrambot.exceptions.RestException;
import uz.mediasolutions.miticodeliverytelegrambot.manual.ApiResult;
import uz.mediasolutions.miticodeliverytelegrambot.mapper.TgUserMapper;
import uz.mediasolutions.miticodeliverytelegrambot.payload.TgUserDTO;
import uz.mediasolutions.miticodeliverytelegrambot.repository.TgUserRepository;
import uz.mediasolutions.miticodeliverytelegrambot.service.abs.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final TgUserRepository tgUserRepository;
    private final TgUserMapper tgUserMapper;

    @Override
    public ApiResult<Page<TgUserDTO>> getAll(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size);
        if (!search.equals("null")) {
            Page<TgUser> tgUsers = tgUserRepository
                    .findAllByNameContainsIgnoreCaseOrUsernameContainsIgnoreCaseOrPhoneNumberContainsIgnoreCaseOrderByCreatedAtDesc(
                            search, search, search, pageable);
            Page<TgUserDTO> map = tgUsers.map(tgUserMapper::toDTO);
            return ApiResult.success(map);
        } else {
            Page<TgUser> tgUsers = tgUserRepository.findAllByOrderByCreatedAtDesc(pageable);
            Page<TgUserDTO> map = tgUsers.map(tgUserMapper::toDTO);
            return ApiResult.success(map);
        }
    }

    @Override
    public ApiResult<TgUserDTO> getById(Long id) {
        TgUser tgUser = tgUserRepository.findById(id).orElseThrow(
                () -> RestException.restThrow("ID NOT FOUND", HttpStatus.BAD_REQUEST));
        TgUserDTO dto = tgUserMapper.toDTO(tgUser);
        return ApiResult.success(dto);
    }

    @Override
    public ApiResult<?> banUser(Long id) {
        TgUser tgUser = tgUserRepository.findById(id).orElseThrow(
                () -> RestException.restThrow("ID NOT FOUND", HttpStatus.BAD_REQUEST));
        tgUser.setBanned(true);
        tgUserRepository.save(tgUser);
        return ApiResult.success("BANNED");
    }

    @Override
    public ApiResult<?> unbanUser(Long id) {
        TgUser tgUser = tgUserRepository.findById(id).orElseThrow(
                () -> RestException.restThrow("ID NOT FOUND", HttpStatus.BAD_REQUEST));
        tgUser.setBanned(false);
        tgUserRepository.save(tgUser);
        return ApiResult.success("UNBANNED");
    }

    @Override
    public ApiResult<?> admin(Long id, boolean admin) {
        TgUser user = tgUserRepository.findById(id).orElseThrow(
                () -> RestException.restThrow("ID NOT FOUND", HttpStatus.BAD_REQUEST));
        user.setAdmin(admin);
        tgUserRepository.save(user);
        return ApiResult.success("ADMIN RIGHTS EDITED");
    }
}
