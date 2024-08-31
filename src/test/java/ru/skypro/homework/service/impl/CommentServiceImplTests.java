package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import ru.skypro.homework.dto.CreateOrUpdateCommentDTO;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.mapper.CommentMapper;
import ru.skypro.homework.exception.EntityModelNotFoundException;
import ru.skypro.homework.model.AdModel;
import ru.skypro.homework.model.CommentModel;
import ru.skypro.homework.model.UserModel;
import ru.skypro.homework.repository.CommentModelRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.service.UserService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = CommentServiceImpl.class)
@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTests {

    @MockBean
    CommentModelRepository commentModelRepository;
    @MockBean
    AdService adService;
    @MockBean
    UserService userService;
    @SpyBean
    CommentMapper commentMapper;

    @Autowired
    CommentService commentService;

    UserModel testUserModel;
    AdModel testAdModel;
    CommentModel testComment;

    @BeforeEach
    void init() {
        testUserModel = new UserModel("test@test.com", "testPassword", "testFirstName",
                "testLastName", "+7(000)000-00-00", Role.ADMIN);
        testUserModel.setId(1);

        testAdModel = new AdModel(10000, "testTitle", "testDescription");
        testAdModel.setId(1);

        testComment = new CommentModel("testCommentText");
    }

    @Test
    void createComment() {
        CreateOrUpdateCommentDTO testProperties = new CreateOrUpdateCommentDTO();
        testProperties.setText("testCommentText");
        when(userService.findUserByUserName(testUserModel.getEmail())).thenReturn(testUserModel);
        when(adService.findAdById(testAdModel.getId())).thenReturn(testAdModel);

        CommentModel createdComment = commentService.createComment(1, testProperties, "test@test.com");
        testComment.setCreatedAt(createdComment.getCreatedAt());

        Mockito.verify(userService, times(1)).findUserByUserName("test@test.com");
        Mockito.verify(adService, times(1)).findAdById(1);
        Mockito.verify(commentModelRepository, times(1)).save(createdComment);
        assertEquals(testAdModel, createdComment.getAd());
        assertEquals(testUserModel, createdComment.getUser());
        assertEquals(testComment, createdComment);
    }

    @Test
    void findByIdPositive() {
        when(commentModelRepository.findById(1)).thenReturn(Optional.of(testComment));

        CommentModel actualCommentModel = commentService.findById(1);

        assertEquals(testComment, actualCommentModel);
    }

    @Test
    void findByIdNegative() {
        assertThrows(EntityModelNotFoundException.class, ()->
                commentService.findById(0));
    }

    @Test
    void updateComment() {
        CreateOrUpdateCommentDTO testProperties = new CreateOrUpdateCommentDTO();
        testProperties.setText("updatedCommentText");
        when(commentModelRepository.findById(1)).thenReturn(Optional.of(testComment));

        CommentModel updatedComment = commentService.updateComment(1, testProperties);

        assertEquals("updatedCommentText", updatedComment.getText());
        assertEquals(testComment.getCreatedAt(), updatedComment.getCreatedAt());
        verify(commentModelRepository, times(1)).save(updatedComment);
    }

    @Test
    void deleteComment() {
        when(commentModelRepository.findById(1)).thenReturn(Optional.of(testComment));

        commentService.deleteComment(1);

        verify(commentModelRepository, times(1)).delete(testComment);
    }

    @Test
    void getAllComments() {
        CommentModel testComment2 = new CommentModel("testCommentText2");
        CommentModel testComment3 = new CommentModel("testCommentText3");
        List<CommentModel> adComments = List.of(testComment, testComment2, testComment3);
        testAdModel.setComments(adComments);
        when(adService.findAdById(testAdModel.getId())).thenReturn(testAdModel);

        List<CommentModel> result = commentService.getAllComments(testAdModel.getId());

        assertEquals(3, result.size());
    }
}
