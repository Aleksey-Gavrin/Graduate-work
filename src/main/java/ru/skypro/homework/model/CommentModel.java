package ru.skypro.homework.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Модель для хранения и обработки сущностей "комментарий"
 */
@Getter
@Setter
@Entity
@Table(name = "comments")
public class CommentModel {
    /**
     * Идентификатор комментария. Генерируется на уровне базы данных
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * Время создания комментария. Генерируется автоматически
     */
    private Timestamp createdAt;
    /**
     * Текстовое содержание комментария, заданное автором
     */
    private String text;
    /**
     * Ссылка на сущность объявления {@link AdModel},к которому привязан комментарий. При удалении из базы данных объявления
     * автоматически удаляются также все связанные с ним комментарии
     */
    @ManyToOne
    @JoinColumn(name = "ad_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private AdModel ad;
    /**
     * Ссылка на сущность пользователя {@link UserModel}, который является автором комментария
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserModel user;

    public CommentModel() {
    }

    public CommentModel(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentModel that = (CommentModel) o;
        return Objects.equals(getCreatedAt(), that.getCreatedAt()) && Objects.equals(getText(), that.getText());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCreatedAt(), getText());
    }
}
