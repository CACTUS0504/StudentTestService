package org.example.request;

import lombok.*;
import org.example.entity.Role;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class LoginRequest {
    private String token;
    private List<Role> roles;
}
