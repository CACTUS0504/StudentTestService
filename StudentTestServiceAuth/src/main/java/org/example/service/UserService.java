package org.example.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.example.entity.Role;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements TableService{
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Override
    public String createUser(String login, String password, List<Role> roles) {
        User user = userRepository.findByLogin(login);
        String encodedPassword = passwordEncoder.encode(password);
        String token = null;

        if (user == null) {
            userRepository.save(User.builder().login(login).password(encodedPassword).roles(roles).build());

            token = JWT.create()
                    .withSubject(login)
                    .withExpiresAt(new Date(System.currentTimeMillis() + 50000000L))
                    .sign(Algorithm.HMAC512("SECRET KEY"));
            return token;
        } else {
            throw new IllegalArgumentException("The user already exists");
        }
    }

    @Override
    public String login(String login, String password){
        User user = userRepository.findByLogin(login);
        if (user != null && passwordEncoder.matches(password, user.getPassword())){
            String token = JWT.create()
                    .withSubject(login)
                    .withExpiresAt(new Date(System.currentTimeMillis() + 50000000L))
                    .sign(Algorithm.HMAC512("SECRET KEY"));
            return token;
        } else {
            throw new IllegalArgumentException("Wrong login/password");
        }
    }

    @Override
    public List<Role> getRoles() {;
        User user = userRepository.findByLogin(
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal()
                        .toString());

        if (user != null) {
            return user.getRoles();
        } else {
            throw new IllegalArgumentException("The required user doesn't exist");
        }
    }

    @Override
    public Boolean CheckRole(Role role) {
        User user = userRepository.findByLogin(
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal()
                        .toString());

        if (user != null) {
            return user.getRoles().contains(role);
        } else {
            throw new IllegalArgumentException("The required user doesn't exist");
        }
    }

    @Override
    public void addRole(Long userId, Role role) {
        User currentUser = userRepository.findByLogin(
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal()
                        .toString());

        Optional<User> targetUser = userRepository.findById(userId);

        if (currentUser.getRoles().contains(Role.ADMIN)){
            if (targetUser.isPresent()){
                User newUser = targetUser.get();
                newUser.getRoles().add(role);
                userRepository.save(newUser);
            } else {
                throw new IllegalArgumentException("The required user doesn't exist");
            }
        } else {
            throw new IllegalArgumentException("Authenticated user doesn't have rights to add roles");
        }
    }

    @Override
    public void changePassword(String newPassword) {
        User user = userRepository.findByLogin(
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal()
                        .toString());

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public User getByLogin(String login) {
        return userRepository.findByLogin(login);
    }


}
