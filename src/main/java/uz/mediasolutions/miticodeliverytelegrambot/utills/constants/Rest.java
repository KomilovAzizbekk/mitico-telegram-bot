package uz.mediasolutions.miticodeliverytelegrambot.utills.constants;

import uz.mediasolutions.miticodeliverytelegrambot.controller.abs.*;
import uz.mediasolutions.miticodeliverytelegrambot.controller.web.abs.*;

import java.util.HashMap;

public interface Rest {

    String[] OPEN_PAGES_FOR_ALL_METHOD = {
            //swagger paths
            "/swagger-ui/**",
            "/api/mail/**",
            "/v2/api-docs",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            AuthController.AUTH + "**",
            LanguageController.LANGUAGE + "**",
            FileImageController.IMAGE + "get/**",
            "/favicon.ico",
            WebBannerController.BANNER_WEB + "**",
            WebCategoryController.CATEGORY_WEB + "**",
            WebProductController.PRODUCT_WEB + "**",
            WebVariationController.VARIATION_WEB + "**",
            WebOrderController.ORDER_WEB + "**",
            ClickController.CLICK + "**",
            PaymeController.PAYME + "**",
            BranchController.BRANCH + BranchController.ACTIVE
    };

    String AUTHORIZATION_HEADER = "Authorization";
    String BASE_PATH = "/api/";
    String TYPE_TOKEN = "Bearer ";
    String PHONE_NUMBER_REGEX = "^[+][0-9]{9,15}$";
    String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    String PASSWORD_REGEX = "^(?=\\S+$)+.{6,}$";
    String DEFAULT_PAGE_NUMBER = "0";
    String DEFAULT_PAGE_SIZE = "10";

    String FILE_PATH = "files";
    String COOKIE_KEY = "FESSONLITEOPARES";

    // Rest Error codes
    int INCORRECT_USERNAME_OR_PASSWORD = 3001;
    int PHONE_EXISTS = 3002;
    int EXPIRED = 3003;
    int ACCESS_DENIED = 3004;
    int NOT_FOUND = 3005;
    int INVALID = 3006;
    int REQUIRED = 3007;

    HashMap<String, Integer> errors = new HashMap<>();
    int SERVER_ERROR = 3008;
    int CONFLICT = 3009;
    int NO_ITEMS_FOUND = 3011;
    int CONFIRMATION = 3012;
    int USER_NOT_ACTIVE = 3013;
    int JWT_TOKEN_INVALID = 3014;
}
