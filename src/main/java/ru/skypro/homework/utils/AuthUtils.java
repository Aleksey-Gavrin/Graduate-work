package ru.skypro.homework.utils;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.model.AdModel;
import ru.skypro.homework.model.CommentModel;
import ru.skypro.homework.model.UserModel;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.service.UserService;

@Component
public class AuthUtils {

    private final UserService userService;
    private final AdService adService;
    private final CommentService commentService;

    public AuthUtils(UserService userService, AdService adService, CommentService commentService) {
        this.userService = userService;
        this.adService = adService;
        this.commentService = commentService;
    }

    public boolean isAccessToAdGranted(int adId, Authentication authentication) {
        UserModel user = userService.findUserByUserName(authentication.getName());
        if (user.getRole().equals(Role.ADMIN)) {
            return true;
        } else {
            AdModel adModel = adService.findAdById(adId);
            return adModel.getUser().getEmail().equals(authentication.getName());
        }
    }

    public boolean isAccessToCommentGranted(int commentId, Authentication authentication) {
        UserModel user = userService.findUserByUserName(authentication.getName());
        if (user.getRole().equals(Role.ADMIN)){
            return true;
        } else {
            CommentModel commentModel = commentService.findById(commentId);
            return commentModel.getUser().getEmail().equals(authentication.getName());
        }
    }
}
