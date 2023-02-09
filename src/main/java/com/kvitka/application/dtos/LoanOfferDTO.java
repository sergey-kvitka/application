package com.kvitka.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanOfferDTO {
    private Long applicationId;
    private BigDecimal requestedAmount;
    private BigDecimal totalAmount;
    private Integer term;
    private BigDecimal monthlyPayment;
    private BigDecimal rate;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;

    public boolean isEmpty() {
        return !(applicationId == null) &&
                requestedAmount == null &&
                totalAmount == null &&
                term == null &&
                monthlyPayment == null &&
                rate == null &&
                isInsuranceEnabled == null &&
                isSalaryClient == null;
    }
}
