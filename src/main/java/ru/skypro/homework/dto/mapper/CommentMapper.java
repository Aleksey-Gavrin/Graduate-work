package ru.skypro.homework.dto.mapper;

import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.CommentsDTO;
import ru.skypro.homework.dto.CreateOrUpdateCommentDTO;
import ru.skypro.homework.model.CommentModel;

import java.util.List;
import java.util.stream.Collectors;

public class CommentMapper {

    public CommentDTO mapCommentModelToCommentDTO (CommentModel commentModel) {
        CommentDTO properties = new CommentDTO();
        properties.setAuthor(commentModel.getAd().getUser().getId());
        properties.setAuthorImage(commentModel.getAd().getUser().getImage());
        properties.setAuthorFirstName(commentModel.getAd().getUser().getFirstName());
        properties.setCreatedAt(commentModel.getCreatedAt().getTime());
        properties.setPk(commentModel.getId());
        properties.setText(commentModel.getText());
        return properties;
    }

    public CommentsDTO mapListCommentModelToCommentsDTO (List<CommentModel> commentsList) {
        List<CommentDTO> dtoList;
        dtoList = commentsList.stream()
                .map(this::mapCommentModelToCommentDTO)
                .collect(Collectors.toUnmodifiableList());
        CommentsDTO properties = new CommentsDTO();
        properties.setCount(dtoList.size());
        properties.setResults(dtoList);
        return properties;
    }

    public CreateOrUpdateCommentDTO mapCommentModelToCreateOrUpdateCommentDTO (CommentModel commentModel) {
        CreateOrUpdateCommentDTO properties = new CreateOrUpdateCommentDTO();
        properties.setText(commentModel.getText());
        return properties;
    }

    public CommentModel mapCreateOrUpdateCommentDTOToCommentModel (CommentModel commentModel, CreateOrUpdateCommentDTO properties) {
        commentModel.setText(properties.getText());
        return commentModel;
    }
}
