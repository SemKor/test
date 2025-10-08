package com.semkor.test.test.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/old")
public class MainOldController {

    private final String VALID_LOGIN = "user";
    private final String VALID_PASSWORD = "1234";

    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);
        return "home"; // home.html
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login"; // login.html
    }

    @PostMapping("/login")
    public String loginSubmit(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            Model model
    ) {
        if (VALID_LOGIN.equals(username) && VALID_PASSWORD.equals(password)) {
            session.setAttribute("username", username);
            return "redirect:/profile";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/profile")
    public String profile(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }
        model.addAttribute("username", username);
        return "profile"; // profile.html
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}