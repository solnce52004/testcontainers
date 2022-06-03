package com.talks.citizen.controller;

import com.talks.citizen.repo.CitizenRepository;
import com.talks.citizen.domain.Fine;
import com.talks.citizen.service.FineService;
import com.talks.citizen.exception.CitizenNotFoundException;
import com.talks.citizen.dto.CitizenStatisticDto;
import com.talks.citizen.entity.Citizen;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class CitizenController {

    private final CitizenRepository citizenRepository;
    private final FineService fineService;

    public CitizenController(CitizenRepository citizenRepository, FineService fineService) {
        this.citizenRepository = citizenRepository;
        this.fineService = fineService;
    }

    @GetMapping("/citizens/{identification}")
    @ResponseStatus(HttpStatus.OK)
    public CitizenStatisticDto getCitizenStatistic(@PathVariable String identification) {
        Citizen citizen = citizenRepository
                .findByIdentificationNumber(identification)
                .orElseThrow(() -> new CitizenNotFoundException("No such citizen"));
        BigDecimal finesTotal = fineService.getFines(citizen)
                .stream()
                .filter(fine -> !fine.isPaid())
                .map(Fine::getSum)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO)
                .stripTrailingZeros();

        CitizenStatisticDto citizenStatisticDto = new CitizenStatisticDto();
        citizenStatisticDto.firstName = citizen.getFirstName();
        citizenStatisticDto.lastName = citizen.getLastName();
        citizenStatisticDto.finesTotal = finesTotal;
        return citizenStatisticDto;
    }

}
