package com.kvitka.application.controllers;

import com.kvitka.application.dtos.LoanApplicationRequestDTO;
import com.kvitka.application.dtos.LoanOfferDTO;
import com.kvitka.application.services.impl.PreScoreServiceImpl;
import com.kvitka.application.services.impl.RestTemplateService;
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
    private final PreScoreServiceImpl preScoreService;

    @PostMapping
    public List<LoanOfferDTO> application(@RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO) {
        log.info("[@PostMapping] application method called. Argument: {}", loanApplicationRequestDTO);
        preScoreService.preScore(loanApplicationRequestDTO);

        String applicationDealURL = dealURL + '/' + applicationDealEndpoint;
        log.info("Sending POST request on \"{}\" (request body: {})", applicationDealURL, loanApplicationRequestDTO);
        ResponseEntity<LoanOfferDTO[]> loanOffersResponse = restTemplateService.postForEntity(
                applicationDealURL, loanApplicationRequestDTO, LoanOfferDTO[].class);
        log.info("POST request on \"{}\" sent successfully!", applicationDealURL);
        List<LoanOfferDTO> loanOffers = List.of(Objects.requireNonNull(loanOffersResponse.getBody()));
        log.info("Response from POST request on \"{}\" is: {}", applicationDealURL, loanOffers);

        log.info("[@PostMapping] application method returns value: {}", loanOffers);
        return loanOffers;
    }

    @PutMapping("offer")
    public void offer(LoanOfferDTO loanOfferDTO) {
        log.info("[@PutMapping(offer)] offer method called. Argument: {}", loanOfferDTO);
        String offerDealURL = dealURL + '/' + offerDealEndpoint;
        log.info("Sending PUT request on \"{}\" (request body: {})", offerDealURL, loanOfferDTO);
        restTemplateService.put(offerDealURL, loanOfferDTO);
        log.info("PUT request on \"{}\" sent successfully!", offerDealURL);
        log.info("[@PutMapping(offer)] offer method finished.");
    }
}
