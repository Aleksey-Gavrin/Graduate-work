package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.model.CommentModel;

public interface CommentModelRepository extends JpaRepository<CommentModel, Integer> {
}
