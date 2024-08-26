package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.CreateOrUpdateAdDTO;
import ru.skypro.homework.model.AdModel;

import java.io.IOException;
import java.util.List;

public interface AdService {

    AdModel createAd(String username, CreateOrUpdateAdDTO properties);

    void setImageToAd(AdModel existingAd, MultipartFile file) throws IOException;

    AdModel findAdById(int id);

    AdModel updateAd(int id, CreateOrUpdateAdDTO properties);

    void deleteAd(int id);

    List<AdModel> getAllAds();
}
