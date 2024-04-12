package uz.mediasolutions.miticodeliverytelegrambot.service.abs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FileImageService {

    ResponseEntity<?> upload(MultipartFile file);

    ResponseEntity<?> get(String imageName);

}
