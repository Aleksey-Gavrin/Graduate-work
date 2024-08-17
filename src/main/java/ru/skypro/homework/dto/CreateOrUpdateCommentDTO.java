package ru.skypro.homework.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CreateOrUpdateCommentDTO {
    @NotBlank
    @Size(min = 8, max = 64, message = "Text length in comment should be between 8 and 64 symbols")
    private String text;
}
