package ru.skypro.homework.service;

import ru.skypro.homework.dto.CreateOrUpdateCommentDTO;
import ru.skypro.homework.model.CommentModel;

import java.util.List;

/**
 * Сервис, содержащий бизнес-логику по обработке сущности "комментарий"
 */
public interface CommentService {

    /**
     * Добавляет новый комментарий в базу данных
     * @param adId - идентификатор объявления, к которому будет добавлен комментарий (целое положительное число)
     * @param properties - DTO сущности "комментарий", содержащий необходимый для создания нового комментария набор полей
     * @param username - адрес электронной почты пользователя - автора комментария для идентификации его в системе
     * @return созданный комментарий
     */
    CommentModel createComment(int adId, CreateOrUpdateCommentDTO properties, String username);

    /**
     * Находит в базе данных комментарий по его идентификатору
     * @param id - идентификатор комментария, целое положительное число
     * @return найденный комментарий
     */
    CommentModel findById(int id);

    /**
     * Обновляет содержимое существующего комментария по его идентификатору
     * @param id - идентификатор комментария, целое положительное число
     * @param properties - DTO сущности "комментарий", содержащий необходимый для обновления комментария набор полей
     * @return обновленный комментарий
     */
    CommentModel updateComment(int id, CreateOrUpdateCommentDTO properties);

    /**
     * Удаляет комментарий из базы данных по его идентификатору
     * @param id - идентификатор комментария, целое положительное число
     */
    void deleteComment(int id);

    /**
     * Получение всех комментариев объявления по его идентификатору
     * @param adId - идентификатор объявления, целое положительное число
     * @return список, содержащий модели комментариев
     */
    List<CommentModel> getAllComments(int adId);
}
