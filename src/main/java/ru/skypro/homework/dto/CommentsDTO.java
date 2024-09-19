package ru.skypro.homework.dto;

import lombok.Data;

import java.util.List;

@Data
public class CommentsDTO {

    private int count;
    private List<CommentDTO> results;
}
