package org.example.clientapp.controller;

import lombok.RequiredArgsConstructor;
import org.example.clientapp.controller.payload.NewUserPayload;
import org.example.clientapp.entity.User;
import org.example.clientapp.service.KeycloakService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {

    private final KeycloakService keycloakService;

    @GetMapping("add")
    public String getNewProductPage() {
        return "user/add";
    }

    @PostMapping("add")
    public String registerUser(NewUserPayload userPayload, Model model) {
        User user = User.builder()
                .username(userPayload.name())
                .firstName(userPayload.name())
                .lastName(userPayload.name())
                .email(userPayload.email())
                .enabled(true)
                .emailVerified(true)
                .credentials(List.of(
                        User.Credential.builder()
                                .type("password")
                                .value(userPayload.password())
                                .isTemporary(false)
                                .build()
                ))
                .build();

        keycloakService.createUser(user);
        return "redirect:/index";
    }
}
