package com.jamatalks.citizen.repo;

import com.jamatalks.citizen.entity.Citizen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CitizenRepository extends JpaRepository<Citizen, Long> {

    Optional<Citizen> findByIdentificationNumber(String identificationNumber);
}
