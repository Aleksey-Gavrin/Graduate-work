package ru.skypro.homework.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "ads")
public class AdModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String image;
    private Integer price;
    private String title;
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserModel user;

    @OneToMany(mappedBy = "ad")
    @JsonIgnore
    private List<CommentModel> comments;

    public AdModel() {
    }

    public AdModel(Integer price, String title, String description) {
        this.price = price;
        this.title = title;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdModel adModel = (AdModel) o;
        return Objects.equals(getImage(), adModel.getImage()) && Objects.equals(getPrice(), adModel.getPrice())
                && Objects.equals(getTitle(), adModel.getTitle()) && Objects.equals(getDescription(),
                adModel.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getImage(), getPrice(), getTitle(), getDescription());
    }
}
