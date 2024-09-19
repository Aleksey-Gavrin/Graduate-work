package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.model.CommentModel;
/**
 * Репозиторий для хранения сущностей "комментарий"
 */
public interface CommentModelRepository extends JpaRepository<CommentModel, Integer> {
}
