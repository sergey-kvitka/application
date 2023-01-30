package com.kvitka.application.controllers;

import com.kvitka.application.dtos.LoanApplicationRequestDTO;
import com.kvitka.application.dtos.LoanOfferDTO;
import com.kvitka.application.services.RestTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("application")
public class ApplicationRestController {

    @Value("${rest.deal.url}")
    private String dealURL;
    @Value("${rest.deal.endpoints.application}")
    private String applicationDealEndpoint;
    @Value("${rest.deal.endpoints.offer}")
    private String offerDealEndpoint;

    private final RestTemplateService restTemplateService;

    @PostMapping
    public List<LoanOfferDTO> application(@RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO) {
        ResponseEntity<LoanOfferDTO[]> loanOffersResponse = restTemplateService.postForEntity(
                dealURL + '/' + applicationDealEndpoint,
                loanApplicationRequestDTO, LoanOfferDTO[].class);

        return List.of(Objects.requireNonNull(loanOffersResponse.getBody()));
    }

    @PutMapping("offer")
    public void offer(LoanOfferDTO loanOfferDTO) {
        restTemplateService.put(dealURL + '/' + offerDealEndpoint, loanOfferDTO);
    }
}
