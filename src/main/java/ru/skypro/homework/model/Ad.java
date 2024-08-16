package ru.skypro.homework.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ad {

    private Integer author;
    private String image;
    private Integer pk;
    private Integer price;
    private String title;

    public Ad() {
    }

    public Ad(Integer author, String image, Integer pk, Integer price, String title) {
        this.author = author;
        this.image = image;
        this.pk = pk;
        this.price = price;
        this.title = title;
    }
}
