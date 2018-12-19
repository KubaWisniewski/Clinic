package com.app.controllers;

import com.app.model.entities.*;
import com.app.service.DoctorService;
import com.app.service.EmailService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("doctor")
public class DoctorController {
    private DoctorService service;
    private EmailService emailService;

    public DoctorController(DoctorService service, EmailService emailService) {
        this.service = service;
        this.emailService = emailService;
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
        model.addAttribute("appointments", service.getAppointmentsByDoctorIdAndStatus(doctor.getId()));
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

    @GetMapping("/patientDetails/{id}")
    public String patientDetails(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails currentUser) {
        Doctor doctor = service.getDoctorEmail(currentUser.getUsername());
        model.addAttribute("doctor", doctor);
        model.addAttribute("patient", service.getPatientById(id));
        model.addAttribute("appointments", service.getAppointmentsByPatientId(id));
        return "/doctor/patientDetails";
    }

    @PostMapping("/appointmentRemove")
    public String removeAppointment(@RequestParam Long id) {
        service.removeAppointment(id);
        return "redirect:/doctor/panel";
    }

    @GetMapping("/appointmentDetails/{id}")
    public String appointmentDetail(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails currentUser) {
        Doctor doctor = service.getDoctorEmail(currentUser.getUsername());
        model.addAttribute("doctor", doctor);
        model.addAttribute("appointment", service.getAppointmentById(id));
        return "/doctor/appointmentDetails";
    }

    @GetMapping("/appointmentEdit/{id}")
    public String appointmentEdit(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails currentUser) {
        Doctor doctor = service.getDoctorEmail(currentUser.getUsername());
        Appointment appointment = service.getAppointmentById(id);
        model.addAttribute("doctor", doctor);
        model.addAttribute("appointment", appointment);
        model.addAttribute("patient", appointment.getPatient());
        model.addAttribute("status", AppointmentStatus.values());
        return "/doctor/appointmentEdit";
    }

    @PostMapping("/appointmentEdit")
    public String appointmentEditPost(@ModelAttribute Appointment appointment) {
        appointment.setDoctor(service.getAppointmentById(appointment.getId()).getDoctor());
        appointment.setPatient(service.getAppointmentById(appointment.getId()).getPatient());

        emailService.sendEmail(service.getPatientById(service.getAppointmentById(appointment.getId()).getPatient().getId()).getEmail(), "Your appointment: " + appointment.getAppointmentStartDate(), "Hello!\n Your appointment from " + appointment.getAppointmentStartDate() + "Description: " + appointment.getDescription());
        service.updateAppointment(appointment);
        return "redirect:/doctor/panel";
    }

    @GetMapping("/addPrescription/{id}")
    public String addPrescription(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails currentUser) {
        Doctor doctor = service.getDoctorEmail(currentUser.getUsername());
        model.addAttribute("doctor", doctor);
        Patient patient = service.getPatientById(id);
        Prescription prescription = new Prescription();
        prescription.setPatient(patient);
        model.addAttribute("prescription", prescription);
        return "/doctor/addPrescription";
    }

    @PostMapping("/addPrescription")
    public String addPrescriptionPost(@ModelAttribute Prescription prescription, @AuthenticationPrincipal UserDetails currentUser) {
        Doctor doctor = service.getDoctorEmail(currentUser.getUsername());
        prescription.setPatient(service.getPatientById(prescription.getPatient().getId()));
        prescription.setDate(LocalDate.now());
        service.addPrescription(prescription, doctor);
        return "redirect:/doctor/panel";
    }

    @PostMapping("/addNewAppointments")
    public String addAppointmentsPost(@RequestParam("date") String date, @RequestParam("time") String time, @AuthenticationPrincipal UserDetails currentUser) {
        Doctor doctor = service.getDoctorEmail(currentUser.getUsername());
        service.addNewAppointments(doctor, LocalDate.parse(date), Integer.parseInt(time));
        return "redirect:/doctor/panel";
    }
}
