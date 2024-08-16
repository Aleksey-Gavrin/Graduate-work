package ru.skypro.homework.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Comment {

    private Integer author;
    private String authorImage;
    private String authorFirstName;
    private Long createdAt;
    private Integer pk;
    private String text;

    public Comment() {
    }

    public Comment(Integer author, String authorImage, String authorFirstName, Long createdAt, Integer pk, String text) {
        this.author = author;
        this.authorImage = authorImage;
        this.authorFirstName = authorFirstName;
        this.createdAt = createdAt;
        this.pk = pk;
        this.text = text;
    }
}
