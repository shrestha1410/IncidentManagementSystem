package com.incident.repository;

import com.incident.entity.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IncidentRepository extends JpaRepository<Incident ,Long> {
    @Query("SELECT i FROM Incident i WHERE i.userId.id = :userId")
    List<Incident> findByUserId(Long userId);
    @Query("SELECT i FROM Incident i WHERE i.id = :incidentId")
    Optional<Incident> findById(String incidentId);
}
