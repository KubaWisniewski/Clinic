package com.app.repository;

import com.app.model.entities.Perscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerscriptionRepository extends JpaRepository<Perscription, Long> {
    List<Perscription> getAllByPatient_Id(Long id);
}
