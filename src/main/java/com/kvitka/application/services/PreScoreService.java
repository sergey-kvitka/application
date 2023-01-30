package com.kvitka.application.services;

import com.kvitka.application.dtos.LoanApplicationRequestDTO;
import com.kvitka.application.exceptions.PreScoreException;

public interface PreScoreService {
    void preScore(LoanApplicationRequestDTO loanApplicationRequestDTO) throws PreScoreException;
}
