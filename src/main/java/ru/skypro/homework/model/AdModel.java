package ru.skypro.homework.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * Модель для хранения и обработки сущностей "объявление"
 */
@Getter
@Setter
@Entity
@Table(name = "ads")
public class AdModel {
    /**
     * Идентификатор объявления. Генерируется на уровне базы данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * Ссылка на эндпоинт, по которому доступна загрузка изображения из объявления. Генерируется в процессе обработки
     * запроса на добавление объявления в методе setImageToAd() в сервисе {@link ru.skypro.homework.service.AdService}
     */
    private String image;
    /**
     * Стоимость товара в объявлении, указанная автором
     */
    private Integer price;
    /**
     * Заголовок объявления, указанный автором
     */
    private String title;
    /**
     * Текстовое описание объявления, указанное автором
     */
    private String description;
    /**
     * Ссылка на сущность пользователя {@link UserModel}, который является автором объявления
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserModel user;
    /**
     * Список сущностей - комментариев {@link CommentModel}, которые связаны с объявлением. Для извлечения из базы данных
     * комментариев при обращении к сущности объявления используется тип извлечения LAZY для нивелирования эффекта N+1
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ad")
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
