package ru.skypro.homework.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
@Slf4j
public class FileUtils {

    @Value("${images.dir.path:./data}")
    private String imageDir;

    public void downloadFile(String filePath, String fileName, HttpServletResponse response) {
        Path path = Path.of(filePath, fileName);
        File avatarFile = new File(String.valueOf(path));
        try (InputStream is = Files.newInputStream(path);
             OutputStream os = response.getOutputStream();
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024)) {

            response.setContentType("image/jpeg");
            response.setContentLength((int) avatarFile.length());
            response.setStatus(200);
            bis.transferTo(bos);


        } catch (IOException exception) {
        response.setStatus(500);
        }
    }

    public void deleteImageFile(int adId) {
        Path filePath = Path.of(imageDir, "image" + adId + ".jpg");
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException exception) {
            log.error(exception.getMessage());
        }
    }
}
