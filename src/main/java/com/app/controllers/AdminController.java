package com.app.controllers;

import com.app.model.entities.Doctor;
import com.app.model.entities.Gender;
import com.app.model.entities.Specialization;
import com.app.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin")
public class AdminController {
    private UserService service;

    public AdminController(UserService service) {
        this.service = service;
    }

    @GetMapping("/panel")
    public String patientPanel() {
        return "admin/panel";
    }

    @GetMapping("/doctorRegister")
    public String registerDoctor(Model model) {
        model.addAttribute("doctor", new Doctor());
        model.addAttribute("genders", Gender.values());
        model.addAttribute("specializations", Specialization.values());
        return "admin/doctorRegister";
    }

    @PostMapping("/doctorRegister")
    public String registerDoctorPOST(
            @ModelAttribute Doctor doctor) {
        service.registerDoctor(doctor);
        return "redirect:/admin/panel";
    }
}
