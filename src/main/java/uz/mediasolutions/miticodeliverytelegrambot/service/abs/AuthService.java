package uz.mediasolutions.miticodeliverytelegrambot.service.abs;

import uz.mediasolutions.miticodeliverytelegrambot.entity.User;
import uz.mediasolutions.miticodeliverytelegrambot.manual.ApiResult;
import uz.mediasolutions.miticodeliverytelegrambot.payload.SignInDTO;
import uz.mediasolutions.miticodeliverytelegrambot.payload.TokenDTO;

public interface AuthService {

    ApiResult<TokenDTO> signIn(SignInDTO signInDTO);

    TokenDTO generateToken(User user);

    User checkUsernameAndPasswordAndEtcAndSetAuthenticationOrThrow(String username, String password);


}
