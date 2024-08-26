package ru.skypro.homework.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.exception.InvalidRequestException;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
public class ValidationUtils {

    private final Validator validator;

    public <T> void validateRequest(T request) {
        if (request != null) {
            Set<ConstraintViolation<T>> result = validator.validate(request);
            if (!result.isEmpty()) {
                String resultsOfValidation = result.stream()
                        .map(ConstraintViolation::getMessage)
                        .reduce((s1, s2) -> s1 + ". " + s2).orElse("");
                log.error("JSON request is not valid. Validation errors: " + resultsOfValidation);
                throw new InvalidRequestException(resultsOfValidation);
            }
        }
    }

    public void validateImageFile(MultipartFile image) {
        if (image != null) {
            String fileExtension = getFileExtension(image);
            if (image.getSize() > 5_000_000 || !fileExtension.equals("jpg")) {
                throw new InvalidRequestException("Invalid image size or format. Supported format : .jpg image under 5MB");
            }
        }

    }

    public String getFileExtension(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        if (originalFileName != null) {
            return originalFileName.substring(originalFileName.lastIndexOf('.') + 1);
        }
        return "";
    }
}
