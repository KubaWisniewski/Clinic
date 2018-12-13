package com.app.validators;


import com.app.exceptions.MyException;
import com.app.model.entities.Patient;
import com.app.model.security.User;

import com.app.repository.PatientRepository;
import com.app.repository.UserRepository;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;
import java.util.Objects;

@Component
public class PatientValidator implements Validator {

    @Autowired
    private PatientRepository patientRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(Patient.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        try {
            Patient patient = (Patient) o;

            if (patientRepository.findByEmail(patient.getEmail()) != null) {
                errors.rejectValue("email", "EMAIL ALREADY EXISTS");
            }
            if (!Objects.equals(patient.getPassword(), patient.getPasswordConfirmation())) {
                errors.rejectValue("password", "PASSWORD AND PASSWORD CONFIRMATION MUST BE EQUAL");
            }
            if (!patient.getFirstName().matches("[A-Z][a-zA-Z]*")) {
                errors.rejectValue("firstName", "PATIENT NAME IS NOT CORRECT");
            }
            if (!patient.getLastName().matches("[A-Z][a-zA-Z]*")) {
                errors.rejectValue("lastName", "PATIENT LAST NAME IS NOT CORRECT");
            }
            if (!patient.getPhone().matches("\\\\+\\\\d{2}(\\\\s\\\\d{3}){3}")) {
                errors.rejectValue("phone", "PATIENT PHONE IS NOT CORRECT");
            }
            if (!EmailValidator.getInstance().isValid(patient.getEmail())) {
                errors.rejectValue("email", "EMAIL IS NOT VALID");
            }
            if (patientRepository.findByEmail(patient.getEmail()) != null) {
                errors.rejectValue("email", "EMAIL ALREADY REGISTERED");
            }

        } catch (Exception e) {
            throw new MyException("REGISTER VALIDATION EXCEPTION", LocalDateTime.now());
        }
    }
}