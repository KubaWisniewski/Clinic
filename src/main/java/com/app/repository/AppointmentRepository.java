package com.app.repository;

import com.app.model.entities.Appointment;
import com.app.model.entities.Doctor;
import org.apache.tomcat.jni.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment,Long> {
    List<Appointment> findByDoctor_Id(Long id);
}
