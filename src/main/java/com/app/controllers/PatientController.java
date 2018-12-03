package com.app.controllers;

import com.app.model.entities.Appointment;
import com.app.model.entities.Doctor;
import com.app.model.entities.Gender;
import com.app.model.entities.Patient;
import com.app.service.DoctorService;
import com.app.service.PatientService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("patient")
public class PatientController {
    private PatientService service;
    private DoctorService doctorService;

    public PatientController(PatientService service, DoctorService doctorService) {
        this.service = service;
        this.doctorService = doctorService;
    }

    @GetMapping("/panel")
    public String patientPanel(Model model, @AuthenticationPrincipal UserDetails currentUser) {
        Patient patient = (Patient) service.getPatientEmail(currentUser.getUsername());
        model.addAttribute("patient", patient);
        return "patient/panel";
    }

    @GetMapping("/allDoctors")
    public String getAllDoctors(Model model, @AuthenticationPrincipal UserDetails currentUser) {
        model.addAttribute("doctors", doctorService.getAllDoctors());
        Patient patient = (Patient) service.getPatientEmail(currentUser.getUsername());
        model.addAttribute("patient", patient);
        return "patient/allDoctors";
    }

    @GetMapping("/edit/{id}")
    public String editPatient(@PathVariable Long id, Model model) {
        model.addAttribute("patient", service.getPatient(id));
        model.addAttribute("genders", Gender.values());
        return "patient/edit";
    }

    @PostMapping("/edit")
    public String editPatientPost(@ModelAttribute Patient patient) {
        service.updatePatient(patient);
        return "redirect:/";
    }

    @GetMapping("/details/{id}")
    public String detailsPatient(@PathVariable Long id, Model model) {
        model.addAttribute("patient", service.getPatient(id));
        return "patient/details";
    }

    @GetMapping("/searchAppointments")
    public String showappointments(Model model, @AuthenticationPrincipal UserDetails currentUser) {
        model.addAttribute("doctors", doctorService.getAllDoctors());
        Patient patient = (Patient) service.getPatientEmail(currentUser.getUsername());
        model.addAttribute("patient", patient);
        return "patient/searchAppointments";
    }

    @GetMapping("/showAppointments")
    public String showAppointmentsFromDoctor(@RequestParam("doctorId") Long doctorId, @RequestParam("date") String date, Model model, @AuthenticationPrincipal UserDetails currentUser) {
        model.addAttribute("appointments", service.getAppointmentsByDoctorId(doctorId, LocalDate.parse(date)));
        Patient patient = (Patient) service.getPatientEmail(currentUser.getUsername());
        model.addAttribute("patient", patient);
        return "patient/showAppointmentList";
    }

    @PostMapping("/registerAppointment")
    public String registerAppointmentPost(@RequestParam Long id, @AuthenticationPrincipal UserDetails currentUser ) {
        Appointment appointment=service.getAppointmentById(id);
        Patient patient = (Patient) service.getPatientEmail(currentUser.getUsername());
        appointment.setPatient(patient);
        service.registerNewAppointment(appointment);
        return "redirect:/";
    }
}
