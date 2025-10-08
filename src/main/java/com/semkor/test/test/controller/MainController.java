package com.semkor.test.test.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    // Главная страница
    @GetMapping("/")
    public String home(Authentication auth, Model model) {
        if (auth != null && auth.isAuthenticated()) {
            model.addAttribute("username", auth.getName());
        }
        return "home"; // home.html
    }

    // Форма логина (Spring Security сам обработает POST /login)
    @GetMapping("/login")
    public String login() {
        return "login"; // login.html
    }

    // Профиль пользователя
    @GetMapping("/profile")
    public String profile(Authentication auth, Model model) {
        model.addAttribute("username", auth.getName());
        return "profile"; // profile.html
    }

    @GetMapping ("/test")
    public String test() {
        return "test"; // login.html
    }
}
