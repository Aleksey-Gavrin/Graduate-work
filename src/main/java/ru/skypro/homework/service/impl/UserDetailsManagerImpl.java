package ru.skypro.homework.service.impl;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;
import ru.skypro.homework.model.UserModel;
import ru.skypro.homework.repository.UserModelRepository;

@Component
public class UserDetailsManagerImpl implements UserDetailsManager {

    private final UserModelRepository userModelRepository;
    private final PasswordEncoder encoder;

    public UserDetailsManagerImpl(UserModelRepository userModelRepository, PasswordEncoder encoder) {
        this.userModelRepository = userModelRepository;
        this.encoder = encoder;
    }

    @Override
    public void createUser(UserDetails userDetails) {
        UserModel creatingUser = new UserModel();
        creatingUser.setEmail(userDetails.getUsername());
        creatingUser.setPassword(userDetails.getPassword());
        userModelRepository.save(creatingUser);
    }

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) throws AccessDeniedException, UsernameNotFoundException {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        if (currentUser == null) {
            throw new AccessDeniedException(
                    "Can't change password as no Authentication object found in context for current user.");
        }
        String username = currentUser.getName();
        UserModel userToUpdate = userModelRepository.findOneByEmailIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("User = " + username + " not found"));
        userToUpdate.setPassword(encoder.encode(newPassword));
        userModelRepository.save(userToUpdate);
    }

    @Override
    public boolean userExists(String username) {
        return userModelRepository.findOneByEmailIgnoreCase(username).isPresent();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel existingUser = userModelRepository.findOneByEmailIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("User = " + username + " not found"));
        return User.builder()
                .username(existingUser.getEmail())
                .password(existingUser.getPassword())
                .roles(existingUser.getRole().name())
                .build();
    }
}
