package com.app.controllers;

import com.app.model.entities.Appointment;
import com.app.model.entities.Doctor;
import com.app.model.entities.Gender;
import com.app.model.entities.Patient;
import com.app.repository.PatientRepository;

import com.app.service.DoctorService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("doctor")
public class DoctorController {
    private DoctorService service;

    public DoctorController(DoctorService service) {
        this.service = service;
    }

    @GetMapping("/panel")
    public String doctorPanel(Model model, @AuthenticationPrincipal UserDetails currentUser) {
        Doctor doctor = service.getDoctorEmail(currentUser.getUsername());
        model.addAttribute("doctor", doctor);
        return "doctor/panel";
    }

    @GetMapping("/edit/{id}")
    public String editDoctor(@PathVariable Long id, Model model) {
        model.addAttribute("doctor", service.getDoctor(id));
        model.addAttribute("genders", Gender.values());
        return "/doctor/edit";
    }

    @PostMapping("/edit")
    public String editDoctorPost(@ModelAttribute Doctor doctor) {
        service.updateDoctor(doctor);
        return "redirect:/doctor/panel";
    }

    @GetMapping("/details/{id}")
    public String detailsDoctor(@PathVariable Long id, Model model) {
        model.addAttribute("doctor", service.getDoctor(id));
        return "/doctor/details";
    }

    @GetMapping("/allAppointments")
    public String allApointemts(Model model, @AuthenticationPrincipal UserDetails currentUser) {
        Doctor doctor = service.getDoctorEmail(currentUser.getUsername());
        model.addAttribute("appointments", service.getAppointments(doctor.getId()));
        model.addAttribute("doctor", doctor);
        return "/doctor/allAppointments";
    }

    @GetMapping("/addNewAppointment")
    public String addAppointment(Model model, @AuthenticationPrincipal UserDetails currentUser) {
        Doctor doctor = service.getDoctorEmail(currentUser.getUsername());
        model.addAttribute("appointment", new Appointment());
        model.addAttribute("doctor", doctor);
        return "/doctor/addNewAppointment";
    }

    @PostMapping("/addNewAppointment")
    public String addAppointmentPost(@ModelAttribute Appointment appointment, @AuthenticationPrincipal UserDetails currentUser) {
        Doctor doctor = service.getDoctorEmail(currentUser.getUsername());
        appointment.setDoctor(doctor);
        service.addNewAppoitnment(appointment);
        return "redirect:/doctor/panel";
    }

    @PostMapping("/acceptAppointment")
    public String acceptAppointment(@RequestParam Long id) {
        service.setAppointmentStatus(id);
        return "redirect:/doctor/panel";
    }

    @GetMapping("/searchForm")
    public String searchForm(Model model, @AuthenticationPrincipal UserDetails currentUser) {
        Doctor doctor = service.getDoctorEmail(currentUser.getUsername());
        model.addAttribute("doctor", doctor);
        return "/doctor/searchForm";
    }

    @GetMapping("/search")
    public String search(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName, Model model, @AuthenticationPrincipal UserDetails currentUser) {
        Doctor doctor = service.getDoctorEmail(currentUser.getUsername());
        model.addAttribute("doctor", doctor);
        model.addAttribute("patients", service.getPatientsByFirstNameAndLastName(firstName, lastName));
        return "/doctor/search";
    }
}
