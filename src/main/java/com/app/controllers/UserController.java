package com.app.controllers;

import com.app.model.entities.Gender;
import com.app.model.entities.Patient;
import com.app.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("patient", new Patient());
        model.addAttribute("genders", Gender.values());
        return "security/register";
    }

    @PostMapping("/register")
    public String registerPOST(
            @ModelAttribute Patient patient) {
        userService.registerPatient(patient);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("error", "");
        return "security/loginForm";
    }

    @GetMapping("/login/error")
    public String loginError(Model model) {
        model.addAttribute("error", "LOGIN DATA ERROR");
        return "security/loginForm";
    }

    @GetMapping("/accessDenied")
    public String accessDenied() {
        return "security/accessDenied";
    }

}
