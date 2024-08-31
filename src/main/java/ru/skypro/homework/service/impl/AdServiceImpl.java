package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.CreateOrUpdateAdDTO;
import ru.skypro.homework.dto.mapper.AdMapper;
import ru.skypro.homework.exception.EntityModelNotFoundException;
import ru.skypro.homework.model.AdModel;
import ru.skypro.homework.model.UserModel;
import ru.skypro.homework.repository.AdModelRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.utils.ValidationUtils;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Slf4j
public class AdServiceImpl implements AdService {

    private final AdModelRepository adModelRepository;
    private final AdMapper adMapper;
    private final UserService userService;
    private final ValidationUtils validationUtils;

    @Value("${images.dir.path:./data}")
    private String imageDir;
    @Value("${images.download.url}")
    private String imageDownloadURL;

    public AdServiceImpl(AdModelRepository adModelRepository, AdMapper adMapper, UserService userService, ValidationUtils validationUtils) {
        this.adModelRepository = adModelRepository;
        this.adMapper = adMapper;
        this.userService = userService;
        this.validationUtils = validationUtils;
    }

    @Override
    public AdModel createAd(String username, CreateOrUpdateAdDTO properties) throws EntityModelNotFoundException {
        AdModel creatingAd = adMapper.mapCreateOrUpdateAdDTOToAdModel(new AdModel(), properties);
        UserModel author = userService.findUserByUserName(username);
        creatingAd.setUser(author);
        log.info("Ad successfully created with the following parameters: {}", properties);
        return adModelRepository.save(creatingAd);
    }

    public void setImageToAd(AdModel existingAd, MultipartFile file) throws IOException {

        if (file.getOriginalFilename() != null) {
            Path filePath = Path.of(imageDir, "image" + existingAd.getId() + "." + validationUtils.getFileExtension(file));
            Files.deleteIfExists(filePath);
            try {
                Files.createDirectory(filePath.getParent());
            } catch (FileAlreadyExistsException exception) {
                log.info("Directory already exists");
            }

            try (InputStream is = file.getInputStream();
                 OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                 BufferedInputStream bis = new BufferedInputStream(is, 1024);
                 BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
            ) {
                bis.transferTo(bos);
                log.info("Image file saved in system directory " + imageDir + ". File name is 'image"
                        + existingAd.getId() + ".jpg'");
            }
        }
        existingAd.setImage(imageDownloadURL + existingAd.getId());
        adModelRepository.save(existingAd);
    }

    @Override
    public AdModel findAdById(int id) throws EntityModelNotFoundException {
        return adModelRepository.findById(id).orElseThrow(() ->
                new EntityModelNotFoundException("Ad model with id = " + id + " not found"));
    }

    @Override
    public AdModel updateAd(int id, CreateOrUpdateAdDTO properties) throws EntityModelNotFoundException {
        AdModel existingAd = findAdById(id);
        adMapper.mapCreateOrUpdateAdDTOToAdModel(existingAd, properties);
        adModelRepository.save(existingAd);
        log.info("Ad model with id = " + id + " successfully updated with parameters: {}", properties);
        return existingAd;
    }

    @Override
    public void deleteAd(int id) throws EntityModelNotFoundException {
        AdModel existingAd = findAdById(id);
        adModelRepository.delete(existingAd);
        log.info("Ad model with id = " + id + " successfully deleted from DB");
    }

    @Override
    public List<AdModel> getAllAds() {
        return adModelRepository.findAll();
    }
}
