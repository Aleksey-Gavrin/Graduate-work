package ru.skypro.homework.service;

import ru.skypro.homework.dto.CreateOrUpdateCommentDTO;
import ru.skypro.homework.model.CommentModel;

import java.util.List;

public interface CommentService {

    CommentModel createComment(int adId, CreateOrUpdateCommentDTO properties, String username);

    CommentModel findById(int id);

    CommentModel updateComment(int id, CreateOrUpdateCommentDTO properties);

    void deleteComment(int id);

    List<CommentModel> getAllComments(int adId);
}
