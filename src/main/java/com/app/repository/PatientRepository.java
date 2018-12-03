package com.app.repository;

import com.app.model.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Patient findByEmail(String email);

    List<Patient> findByFirstNameAndLastName(String firstName, String LastName);
}
