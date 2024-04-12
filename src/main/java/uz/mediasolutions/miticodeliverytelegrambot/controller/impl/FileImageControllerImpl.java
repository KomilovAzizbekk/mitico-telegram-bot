package uz.mediasolutions.miticodeliverytelegrambot.controller.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import uz.mediasolutions.miticodeliverytelegrambot.controller.abs.FileImageController;
import uz.mediasolutions.miticodeliverytelegrambot.service.abs.FileImageService;

@RestController
@RequiredArgsConstructor
public class FileImageControllerImpl implements FileImageController {

    private final FileImageService fileImageService;

    @Override
    public ResponseEntity<?> get(String imageName) {
        return fileImageService.get(imageName);
    }

    @Override
    public ResponseEntity<?> upload(MultipartFile file) {
        return fileImageService.upload(file);
    }
}
