package com.app.service;

import com.app.exceptions.MyException;
import com.app.model.entities.Appointment;
import com.app.model.entities.AppointmentStatus;
import com.app.model.entities.Patient;
import com.app.model.entities.Prescription;
import com.app.repository.AppointmentRepository;
import com.app.repository.PatientRepository;
import com.app.repository.PrescriptionRepository;
import com.app.utils.FileManager;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {
    private PatientRepository patientRepository;
    private AppointmentRepository appointmentRepository;
    private FileManager fileManager;
    private PrescriptionRepository prescriptionRepository;

    public PatientService(PatientRepository patientRepository, AppointmentRepository appointmentRepository, FileManager fileManager, PrescriptionRepository prescriptionRepository) {
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.fileManager = fileManager;
        this.prescriptionRepository = prescriptionRepository;
    }

    public void updatePatient(Patient patient) {
        try {
            if (patient.getPhoto() == null || patient.getPhoto().equals("")) {
                String filename = fileManager.addFile(patient.getMultipartFile());
                patient.setPhoto(filename);
            } else
                fileManager.updateFile(patient.getMultipartFile(), patient.getPhoto());
            patientRepository.save(patient);

        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException("Service update patient exception", LocalDateTime.now());
        }
    }

    public Patient getPatient(Long id) {
        try {
            return patientRepository.findById(id).orElseThrow(NullPointerException::new);
        } catch (Exception e) {
            throw new MyException("Service get patient exception", LocalDateTime.now());
        }
    }

    public Patient getPatientEmail(String email) {
        try {
            return patientRepository.findByEmail(email);
        } catch (Exception e) {
            throw new MyException("Service get patient exception", LocalDateTime.now());
        }
    }

    public void registerNewAppointment(Appointment appointment) {
        try {

            appointment.setAppointmentStatus(AppointmentStatus.RESERVED);
            appointmentRepository.save(appointment);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException("Service register new appointment", LocalDateTime.now());
        }
    }

    public Appointment getAppointmentById(Long id) {
        try {
            return appointmentRepository.findById(id).orElseThrow(NullPointerException::new);
        } catch (Exception e) {
            throw new MyException("Service get appointment by id exception", LocalDateTime.now());
        }
    }

    public List<Appointment> getAppointmentsByDoctorId(Long id, LocalDate date) {
        try {
            return appointmentRepository.findByDoctor_Id(id).stream().filter(x -> x.getAppointmentStatus().equals(AppointmentStatus.NEW) && x.getAppointmentStartDate().toLocalDate().equals(date)).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException("Service get appointmts exception", LocalDateTime.now());
        }
    }

    public List<Appointment> getAppointmentsByPatientId(Long id) {
        try {

            return appointmentRepository.findByPatient_Id(id).stream().collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException("Service get appointmts exception", LocalDateTime.now());
        }
    }

    public List<Prescription> getPatientPrescriptions(Long id) {
        try {
            return prescriptionRepository.getAllByPatient_Id(id).stream().collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException("Service get prescriptions by id exception", LocalDateTime.now());
        }
    }

    public String getPrescriptionById(Long id) {
        try {
            return prescriptionRepository.getOne(id).getFileName();
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException("Service get prescription by id exception", LocalDateTime.now());
        }
    }
}