package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;
import ru.skypro.homework.exception.EntityModelNotFoundException;
import ru.skypro.homework.model.UserModel;
import ru.skypro.homework.repository.UserModelRepository;

@Component
@Slf4j
public class UserDetailsManagerImpl implements UserDetailsManager {

    private final UserModelRepository userModelRepository;
    private final PasswordEncoder encoder;

    public UserDetailsManagerImpl(UserModelRepository userModelRepository, PasswordEncoder encoder) {
        this.userModelRepository = userModelRepository;
        this.encoder = encoder;
    }

    @Override
    public void createUser(UserDetails userDetails) {

    }

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) throws EntityModelNotFoundException {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        String username = currentUser.getName();
        UserModel userToUpdate = userModelRepository.findOneByEmailIgnoreCase(username)
                .orElseThrow(() -> new EntityModelNotFoundException("User = " + username + " not found"));
        userToUpdate.setPassword(encoder.encode(newPassword));
        userModelRepository.save(userToUpdate);
        log.info("Password for user " + username + " successfully changed");
    }

    @Override
    public boolean userExists(String username) {
        return userModelRepository.findOneByEmailIgnoreCase(username).isPresent();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws EntityModelNotFoundException {
        UserModel existingUser = userModelRepository.findOneByEmailIgnoreCase(username)
                .orElseThrow(() -> new EntityModelNotFoundException("User = " + username + " not found"));
        return User.builder()
                .username(existingUser.getEmail())
                .password(existingUser.getPassword())
                .roles(existingUser.getRole().name())
                .build();
    }
}
