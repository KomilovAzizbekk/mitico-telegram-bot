package uz.mediasolutions.miticodeliverytelegrambot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import uz.mediasolutions.miticodeliverytelegrambot.exceptions.RestException;
import uz.mediasolutions.miticodeliverytelegrambot.manual.ApiResult;
import uz.mediasolutions.miticodeliverytelegrambot.service.abs.FileImageService;
import uz.mediasolutions.miticodeliverytelegrambot.utills.constants.Rest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileImageServiceImpl implements FileImageService {

    @Value("${upload-dir}")
    private String uploadDir;

    private final Environment environment;

    public ResponseEntity<?> upload(MultipartFile file) {
        // Check if the file is empty
        if (file.isEmpty()) {
            throw RestException.restThrow("FILE IS EMPTY", HttpStatus.BAD_REQUEST);
        }

        // Normalize the file name to prevent directory traversal
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        fileName = UUID.randomUUID() + fileName.substring(fileName.lastIndexOf('.'));

        // Copy the file to the target location
        Path targetPath = Path.of(uploadDir).resolve(fileName);
        try {
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok(constructImageUrl(fileName));
    }

    @Override
    public ResponseEntity<?> get(String imageName) {
        try {
            // Load the image file as a resource
            Path imagePath = Paths.get(uploadDir).resolve(imageName);
            Resource resource = new UrlResource(imagePath.toUri());

            // Check if the resource exists
            if (resource.exists()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + resource.getFilename())
                        .contentType(MediaType.IMAGE_JPEG) // Adjust the MediaType according to your image type
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    private String constructImageUrl(String fileName) {
        String baseUrl = environment.getProperty("server.base-url", "https://hamdi-usta-backend.medias.uz" + Rest.BASE_PATH);
        return baseUrl + "images/get/" + fileName;
    }
}
