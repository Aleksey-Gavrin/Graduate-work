package ru.skypro.homework.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "comments")
public class CommentModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Timestamp createdAt;
    private String text;

    @ManyToOne
    @JoinColumn(name = "ad_id")
    private AdModel ad;

    public CommentModel() {
    }
}
