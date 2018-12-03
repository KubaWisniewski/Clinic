package com.app.repository;

import com.app.model.entities.Perscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerscriptionRepository extends JpaRepository<Perscription, Long> {
}
