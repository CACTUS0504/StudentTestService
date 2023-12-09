package org.example.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.example.entity.NewUser;
import org.example.entity.Role;
import org.example.request.LoginRequest;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    // По умолчанию создаётся роль студента, т.к обладает наименьшими правами
    @PostMapping("/registration")
    public String register(@RequestBody NewUser newUser){
        return userService.createUser(newUser.login, newUser.password, List.of(Role.STUDENT));
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody NewUser newUser){
        String token = null;
        try {
            token = userService.login(newUser.login, newUser.password);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body("Wrong login/password");
        }
        return ResponseEntity.ok().body(new LoginRequest(token));
    }

    @PostMapping("/change-password")
    public ResponseEntity changePassword(@RequestParam("password") String password){
        try {
            userService.changePassword(password);
            return ResponseEntity.ok().body("Password changed");
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Error changing password");
        }
    }

    @GetMapping("/roles")
    public ResponseEntity roles(@RequestHeader("Authorization") String token){
        HashMap<String, Object> response = new HashMap<>();
        String login = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        response.put("id", userService.getByLogin(login).getId());
        response.put("roles",userService.getRoles());
        return ResponseEntity.ok().body(response);
    }
}
