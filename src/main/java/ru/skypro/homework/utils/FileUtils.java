package ru.skypro.homework.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Вспомогательный класс, содержащий методы для загрузки и удаления файлов из локального хранилища
 */
@Component
@Slf4j
public class FileUtils {

    @Value("${images.dir.path:./data}")
    private String imageDir;

    /**
     * Загружает файл из локального хранилища и отдает его в теле http - ответа с подходящими заголовками
     * @param filePath - путь к загружаемому файлу в локальной директории в формате строки
     * @param fileName - имя загружаемого файла включая его расширение в формате строки
     * @param response - формируемый методом http - ответ
     */
    public void downloadFile(String filePath, String fileName, HttpServletResponse response) {
        Path path = Path.of(filePath, fileName);
        File avatarFile = new File(String.valueOf(path));
        try (InputStream is = Files.newInputStream(path);
             OutputStream os = response.getOutputStream();
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024)) {

            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            response.setContentLength((int) avatarFile.length());
            response.setStatus(200);
            bis.transferTo(bos);


        } catch (IOException exception) {
        response.setStatus(500);
        }
    }

    /**
     * Удаляет из локального хранилища файл - изображение, связанный с объявлением по идентификатору объявления
     * @param adId - идентификатор объявления, целое положительное число
     */
    public void deleteImageFile(int adId) {
        Path filePath = Path.of(imageDir, "image" + adId + ".jpg");
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException exception) {
            log.error(exception.getMessage());
        }
    }
}
