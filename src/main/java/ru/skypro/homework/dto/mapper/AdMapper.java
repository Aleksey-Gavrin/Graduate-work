package ru.skypro.homework.dto.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.AdDTO;
import ru.skypro.homework.dto.AdsDTO;
import ru.skypro.homework.dto.CreateOrUpdateAdDTO;
import ru.skypro.homework.dto.ExtendedAdDTO;
import ru.skypro.homework.model.AdModel;

import java.util.List;
import java.util.stream.Collectors;
@Component
public class AdMapper {

    public AdDTO mapAdModelToAdDTO (AdModel adModel) {
        AdDTO properties = new AdDTO();
        properties.setAuthor(adModel.getUser().getId());
        properties.setImage(adModel.getImage());
        properties.setPk(adModel.getId());
        properties.setPrice(adModel.getPrice());
        properties.setTitle(adModel.getTitle());
        return properties;
    }

    public AdsDTO mapListAdModelToAdsDTO (List<AdModel> ads) {
        List<AdDTO> dtoList;
        dtoList = ads.stream()
                .map(this::mapAdModelToAdDTO)
                .collect(Collectors.toUnmodifiableList());
        AdsDTO properties = new AdsDTO();
        properties.setCount(dtoList.size());
        properties.setResults(dtoList);
        return properties;
    }

    public AdModel mapCreateOrUpdateAdDTOToAdModel (AdModel adModel, CreateOrUpdateAdDTO properties) {
        adModel.setTitle(properties.getTitle());
        adModel.setPrice(properties.getPrice());
        adModel.setDescription(properties.getDescription());
        return adModel;
    }

    public ExtendedAdDTO mapAdModelToExtendedAdDTO (AdModel adModel) {
        ExtendedAdDTO properties = new ExtendedAdDTO();
        properties.setPk(adModel.getId());
        properties.setAuthorFirstName(adModel.getUser().getFirstName());
        properties.setAuthorLastName(adModel.getUser().getLastName());
        properties.setDescription(adModel.getDescription());
        properties.setEmail(adModel.getUser().getEmail());
        properties.setImage(adModel.getImage());
        properties.setPhone(adModel.getUser().getPhone());
        properties.setPrice(adModel.getPrice());
        properties.setTitle(adModel.getTitle());
        return properties;
    }
}
