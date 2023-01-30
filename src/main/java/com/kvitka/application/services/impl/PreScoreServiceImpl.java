package com.kvitka.application.services.impl;

import com.kvitka.application.dtos.LoanApplicationRequestDTO;
import com.kvitka.application.exceptions.PreScoreException;
import com.kvitka.application.services.PreScoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

@Slf4j
@Service
public class PreScoreServiceImpl implements PreScoreService {

    private static final int MIN_TERM = 6;
    private static final int MIN_AGE = 18;

    private static final String ONLY_LETTERS_AND_LENGTH_MISMATCH_MESSAGE =
            "must only contain latin letters and its length must be in range from 2 to 30 characters";

    private static final Pattern ONLY_LETTERS_AND_LENGTH =
            Pattern.compile("^[a-z]{2,30}", Pattern.CASE_INSENSITIVE);

    private static final String EMAIL_REGEX = "[\\w.]{2,50}@[\\w.]{2,20}";
    private static final String PASSWORD_SERIES_REGEX = "[0-9]{4}";
    private static final String PASSWORD_NUMBER_REGEX = "[0-9]{6}";

    @Override
    public void preScore(LoanApplicationRequestDTO loanApplicationRequestDTO) throws PreScoreException {
        log.info("PreScoring started. Data to preScore: {}", loanApplicationRequestDTO);

        String firstName = loanApplicationRequestDTO.getFirstName();
        String lastName = loanApplicationRequestDTO.getLastName();
        String middleName = loanApplicationRequestDTO.getMiddleName();

        log.info("PreScoring... checking first name, last name and middle name " +
                "(firstName: {}, lastName: {}, middleName: {})", firstName, lastName, middleName);

        validateNamePattern(firstName, "first name",
                ONLY_LETTERS_AND_LENGTH, ONLY_LETTERS_AND_LENGTH_MISMATCH_MESSAGE);

        validateNamePattern(lastName, "last name",
                ONLY_LETTERS_AND_LENGTH, ONLY_LETTERS_AND_LENGTH_MISMATCH_MESSAGE);

        if (middleName != null && !middleName.isEmpty()) {
            validateNamePattern(loanApplicationRequestDTO.getMiddleName(),
                    "middle name", ONLY_LETTERS_AND_LENGTH,
                    ONLY_LETTERS_AND_LENGTH_MISMATCH_MESSAGE + " (if it's present)");
        }

        Integer term = loanApplicationRequestDTO.getTerm();
        log.info("PreScoring... checking term (value: {})", term);
        if (term < MIN_TERM) {
            throw new PreScoreException(String.format("Term must not be less than %d months.", MIN_TERM));
        }

        int age = calculateCurrentAge(loanApplicationRequestDTO.getBirthdate());
        log.info("PreScoring... checking age (value: {})", age);
        if (age < MIN_AGE) {
            throw new PreScoreException(String.format("The minimum age is %d years.", MIN_AGE));
        }

        String email = loanApplicationRequestDTO.getEmail();
        log.info("PreScoring... checking email (email: {}, pattern: {})", email, EMAIL_REGEX);
        if (!Pattern.compile(EMAIL_REGEX).matcher(email).matches()) {
            throw new PreScoreException(String.format("Incorrect email format (%s).", email));
        }

        String passportSeries = loanApplicationRequestDTO.getPassportSeries();
        log.info("PreScoring... checking password series (password series: {}, pattern: {})",
                passportSeries, PASSWORD_SERIES_REGEX);
        if (!Pattern.compile(PASSWORD_SERIES_REGEX).matcher(passportSeries).matches()) {
            throw new PreScoreException(String.format("Passport series must contain %d digits.", 4));
        }

        String passportNumber = loanApplicationRequestDTO.getPassportNumber();
        log.info("PreScoring... checking password number (password number: {}, pattern: {})",
                passportNumber, PASSWORD_NUMBER_REGEX);
        if (!Pattern.compile(PASSWORD_NUMBER_REGEX).matcher(passportNumber).matches()) {
            throw new PreScoreException(String.format("Passport number must contain %d digits.", 6));
        }

        log.info("PreScoring: All checks passed successfully! PreScoring finished");
    }

    private void validateNamePattern(String value, String varName, @SuppressWarnings("all") Pattern pattern,
                                     String mismatchMessage) throws PreScoreException {
        varName = varName.trim();
        String varNameCapitalized = value.length() < 1 ? varName :
                varName.substring(0, 1).toUpperCase() + varName.substring(1);

        log.info("Validating name pattern ({}: {}, pattern: {})", varNameCapitalized, value, pattern);

        if (!pattern.matcher(value).matches()) throw new PreScoreException(String.format("%s %s.",
                varNameCapitalized, mismatchMessage.trim()));
    }

    private int calculateCurrentAge(LocalDate birthdate) {
        log.info("Age calculation started (birthdate: {})", birthdate);
        int age = Period.between(birthdate, LocalDate.now()).getYears();
        log.info("Age calculation finished (result: {} (years))", age);
        return age;
    }
}
