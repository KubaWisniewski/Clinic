package com.app.controllers;

import com.app.model.entities.Gender;
import com.app.model.entities.Patient;
import com.app.service.UserService;
import com.app.validators.PatientValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.stream.Collectors;

@Controller
public class UserController {
    private UserService userService;
    private PatientValidator patientValidator;

    public UserController(UserService userService, PatientValidator patientValidator) {
        this.userService = userService;
        this.patientValidator = patientValidator;
    }

    @InitBinder
    private void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.setValidator(patientValidator);
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("patient", new Patient());
        model.addAttribute("genders", Gender.values());
        model.addAttribute("errors", new HashMap<>());
        return "security/register";
    }

    @PostMapping("/register")
    public String registerPOST(
            @Valid @ModelAttribute Patient patient,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("patient", patient);
            model.addAttribute("genders", Gender.values());
            model.addAttribute("errors", bindingResult
                    .getFieldErrors()
                    .stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getCode, (v1, v2) -> v1 + ", " + v2)));
            return "security/register";
        }

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
