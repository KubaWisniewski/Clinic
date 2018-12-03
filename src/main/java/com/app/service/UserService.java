package com.app.service;

import com.app.model.entities.Doctor;
import com.app.model.entities.Patient;
import com.app.model.security.Role;
import com.app.model.security.User;
import com.app.repository.DoctorRepository;
import com.app.repository.PatientRepository;
import com.app.utils.FileManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private BCryptPasswordEncoder passwordEncoder;
    private PatientRepository patientRepository;
    private FileManager fileManager;
    private DoctorRepository doctorRepository;


    public UserService(BCryptPasswordEncoder passwordEncoder, PatientRepository patientRepository, FileManager fileManager, DoctorRepository doctorRepository) {
        this.passwordEncoder = passwordEncoder;
        this.patientRepository = patientRepository;
        this.fileManager = fileManager;
        this.doctorRepository = doctorRepository;
    }

    public void registerPatient(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        user.setRole(Role.PATIENT);
        String filename = fileManager.addFile(user.getMultipartFile());
        user.setPhoto(filename);
        patientRepository.save((Patient) user);
    }

    public void registerDoctor(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        user.setRole(Role.DOCTOR);
        String filename = fileManager.addFile(user.getMultipartFile());
        user.setPhoto(filename);
        doctorRepository.save((Doctor) user);

    }
}
