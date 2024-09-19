package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.utils.FileUtils;

import javax.servlet.http.HttpServletResponse;

@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/download")
public class FileDownloadController {

    private final FileUtils fileUtils;

    @Value("${avatars.dir.path:./data}")
    private String avatarsDir;
    @Value("${images.dir.path:./data}")
    private String imagesDir;

    public FileDownloadController(FileUtils fileUtils) {
        this.fileUtils = fileUtils;
    }

    @Operation(
            summary = "Загрузка аватара по id пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = {
                                    @Content(
                                            mediaType = "image/jpeg"
                                    )
                            }
                    )
            }, tags = "Загрузка файлов"
    )
    @GetMapping("/avatar/{id}")
    public void downloadAvatar(@PathVariable int id, HttpServletResponse response) {
        fileUtils.downloadFile(avatarsDir, "avatar" + id + ".jpg", response);
        }

    @Operation(
            summary = "Загрузка картинки по id объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = {
                                    @Content(
                                            mediaType = "image/jpeg"
                                    )
                            }
                    )
            }, tags = "Загрузка файлов"
    )
    @GetMapping("/image/{id}")
    public void downloadImage(@PathVariable int id, HttpServletResponse response) {
        fileUtils.downloadFile(imagesDir, "image" + id + ".jpg", response);
        }
}
