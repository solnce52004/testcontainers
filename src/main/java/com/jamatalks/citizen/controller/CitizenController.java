package com.jamatalks.citizen.controller;

import com.jamatalks.citizen.repo.CitizenRepository;
import com.jamatalks.citizen.domain.Fine;
import com.jamatalks.citizen.service.FineService;
import com.jamatalks.citizen.exception.CitizenNotFoundException;
import com.jamatalks.citizen.dto.CitizenStatisticDto;
import com.jamatalks.citizen.entity.Citizen;
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
