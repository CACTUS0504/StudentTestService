package org.example.service;

import org.example.entity.Role;
import org.example.entity.User;

import java.util.List;

public interface TableService {
    String createUser(String login, String password, List<Role> roles);
    String login(String login, String password);

    List<Role> getRoles();

    Boolean CheckRole(Role role);
    void addRole(Long userId, Role role);

    void changePassword(String newPassword);

    User getByLogin(String login);
}
