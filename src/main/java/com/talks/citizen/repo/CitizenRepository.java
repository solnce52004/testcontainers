package com.talks.citizen.repo;

import com.talks.citizen.entity.Citizen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CitizenRepository extends JpaRepository<Citizen, Long> {

    Optional<Citizen> findByIdentificationNumber(String identificationNumber);
}
