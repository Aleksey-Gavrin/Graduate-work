package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.CreateOrUpdateAdDTO;
import ru.skypro.homework.model.AdModel;

import java.io.IOException;
import java.util.List;

/**
 * Сервис, содержащий бизнес-логику по обработке сущности "объявление"
 */
public interface AdService {
    /**
     * Добавляет новое объявление в базу данных
     * @param username - адрес электронной почты пользователя для идентификации его в системе
     * @param properties - DTO сущности "объявление", содержащий необходимый для создания нового объявления набор полей
     * @return созданное объявление
     */
    AdModel createAd(String username, CreateOrUpdateAdDTO properties);

    /**
     * Добавляет изображение к существующему объявлению
     * @param existingAd - модель существующего объявления. Не может быть null
     * @param file - файл изображения
     * @throws IOException - в случае ошибки чтения-записи файла изображения
     */
    void setImageToAd(AdModel existingAd, MultipartFile file) throws IOException;

    /**
     * Находит объявление в базе данных по идентификатору
     * @param id - идентификатор объявления, целое положительное число
     * @return найденное объявление
     */
    AdModel findAdById(int id);

    /**
     * Обновляет содержимое в существующем объявлении по его идентификатору
     * @param id - идентификатор объявления, целое положительное число
     * @param properties - DTO сущности "объявление", содержащий необходимый для обновления объявления набор полей
     * @return обновленное объявление
     */
    AdModel updateAd(int id, CreateOrUpdateAdDTO properties);

    /**
     * Удаляет объявление из базы данных по его идентификатору
     * @param id - идентификатор объявления, целое положительное число
     */
    void deleteAd(int id);

    /**
     * Получение всех объявлений из базы данных
     * @return список, содержащий модели объявлений
     */
    List<AdModel> getAllAds();
}
