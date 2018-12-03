package com.app.service;

import com.app.exceptions.MyException;
import com.app.model.entities.Appointment;
import com.app.model.entities.AppointmentStatus;
import com.app.model.entities.Doctor;
import com.app.model.entities.Patient;
import com.app.repository.AppointmentRepository;
import com.app.repository.DoctorRepository;
import com.app.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorService {
    private DoctorRepository doctorRepository;
    private AppointmentRepository appointmentRepository;
    private PatientRepository patientRepository;

    public DoctorService(DoctorRepository doctorRepository, AppointmentRepository appointmentRepository, PatientRepository patientRepository) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
    }

    public void updateDoctor(Doctor doctor) {
        try {
            doctorRepository.save(doctor);
        } catch (Exception e) {
            throw new MyException("Service update doctor exception", LocalDateTime.now());
        }
    }

    public Doctor getDoctor(Long id) {
        try {
            return doctorRepository.findById(id).orElseThrow(NullPointerException::new);
        } catch (Exception e) {
            throw new MyException("Service get doctor exception", LocalDateTime.now());
        }
    }

    public Doctor getDoctorEmail(String email) {
        try {
            return doctorRepository.findByEmail(email);
        } catch (Exception e) {
            throw new MyException("Service get doctor exception", LocalDateTime.now());
        }
    }

    public List<Appointment> getAppointments(Long id) {
        try {
            System.out.println(id);
            return appointmentRepository.findByDoctor_Id(id).stream().collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException("Service get appointmts exception", LocalDateTime.now());
        }


    }

    public List<Doctor> getAllDoctors() {
        try {
            return doctorRepository
                    .findAll()
                    .stream()
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException("Service get all doctors exception", LocalDateTime.now());
        }
    }

    public void addNewAppoitnment(Appointment appointment) {
        try {
            appointment.setAppointmentStatus(AppointmentStatus.NEW);
            appointmentRepository.save(appointment);
        } catch (Exception e) {
            throw new MyException("Service add new appointment exception", LocalDateTime.now());

        }
    }

    public void setAppointmentStatus(Long id) {
        try {
            Appointment appointment = appointmentRepository.getOne(id);
            appointment.setAppointmentStatus(AppointmentStatus.ACCEPTED);
            appointmentRepository.save(appointment);

        } catch (Exception e) {
            throw new MyException("Service set appointment status exception", LocalDateTime.now());
        }
    }

    public List<Patient> getPatientsByFirstNameAndLastName(String firstName, String lastName) {
        try {
            return patientRepository.findByFirstNameAndLastName(firstName, lastName);
        } catch (Exception e) {
            throw new MyException("Service search patients exception", LocalDateTime.now());
        }
    }
}
