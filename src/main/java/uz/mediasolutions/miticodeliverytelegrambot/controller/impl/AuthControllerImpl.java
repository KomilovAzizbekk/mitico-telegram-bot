package uz.mediasolutions.miticodeliverytelegrambot.controller.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import uz.mediasolutions.miticodeliverytelegrambot.controller.abs.AuthController;
import uz.mediasolutions.miticodeliverytelegrambot.manual.ApiResult;
import uz.mediasolutions.miticodeliverytelegrambot.payload.SignInDTO;
import uz.mediasolutions.miticodeliverytelegrambot.payload.TokenDTO;
import uz.mediasolutions.miticodeliverytelegrambot.service.abs.AuthService;

@RestController
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;

    @Override
    public ApiResult<TokenDTO> signIn(SignInDTO dto) {
        return authService.signIn(dto);
    }
}
