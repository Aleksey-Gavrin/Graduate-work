package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.CommentsDTO;
import ru.skypro.homework.dto.CreateOrUpdateCommentDTO;
import ru.skypro.homework.dto.mapper.CommentMapper;
import ru.skypro.homework.exception.EntityModelNotFoundException;
import ru.skypro.homework.model.CommentModel;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.utils.AuthUtils;
import ru.skypro.homework.utils.ValidationUtils;

import java.util.List;

@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
public class CommentController {

    private final ValidationUtils validationUtils;
    private final CommentService commentService;
    private final CommentMapper mapper;
    private final AuthUtils authUtils;

    @Operation(
            summary = "Получение комментариев объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = CommentsDTO.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found"
                    )
            }, tags = "Комментарии"
    )
    @GetMapping("/{id}/comments")
    public ResponseEntity<CommentsDTO> getComments(@PathVariable int id) {
        try {
            List<CommentModel> comments = commentService.getAllComments(id);
            CommentsDTO commentsDTO = mapper.mapListCommentModelToCommentsDTO(comments);
            return new ResponseEntity<>(commentsDTO, HttpStatus.OK);
        } catch (EntityModelNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Добавление комментария к объявлению",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = CommentDTO.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found"
                    )
            }, tags = "Комментарии"
    )
    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentDTO> addComment(@PathVariable int id, @RequestBody CreateOrUpdateCommentDTO properties,
                                                 Authentication authentication) {
        validationUtils.validateRequest(properties);
        try {
            CommentModel commentModel = commentService.createComment(id, properties, authentication.getName());
            CommentDTO commentDTO = mapper.mapCommentModelToCommentDTO(commentModel);
            return new ResponseEntity<>(commentDTO, HttpStatus.OK);

        } catch (EntityModelNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Удаление комментария",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found"
                    )
            }, tags = "Комментарии"
    )
    @DeleteMapping("{adId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable int adId, @PathVariable int commentId, Authentication authentication) {
        if (!authUtils.isAccessToCommentGranted(commentId, authentication)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        try {
            commentService.deleteComment(commentId);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (EntityModelNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Обновление комментария",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = CommentDTO.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found"
                    )
            }, tags = "Комментарии"
    )
    @PatchMapping("{adId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable int adId, @PathVariable int commentId,
                                                    @RequestBody CreateOrUpdateCommentDTO properties,
                                                    Authentication authentication) {
        if (!authUtils.isAccessToCommentGranted(commentId, authentication)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        validationUtils.validateRequest(properties);
        try {
            CommentModel commentModel = commentService.updateComment(commentId, properties);
            CommentDTO commentDTO = mapper.mapCommentModelToCommentDTO(commentModel);
            return new ResponseEntity<>(commentDTO, HttpStatus.OK);

        } catch (EntityModelNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
