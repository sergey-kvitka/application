package com.kvitka.application.services.impl;

import com.kvitka.application.dtos.LoanApplicationRequestDTO;
import com.kvitka.application.exceptions.PreScoreException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Slf4j
class PreScoreServiceImplTest {

    @Autowired
    PreScoreServiceImpl preScoreService;

    Function<Integer, String> testName = n -> "test" + n;

    Map<String, LoanApplicationRequestDTO> unacceptableLoanApplicationRequests = new HashMap<>();

    // ? Data to test exceptions that may be thrown while preScoring
    {
        int n = 0;
        LoanApplicationRequestDTO lar1 = defaultLoanApplicationRequest();
        lar1.setFirstName("Z");
        unacceptableLoanApplicationRequests.put(testName.apply(++n), lar1);

        LoanApplicationRequestDTO lar2 = defaultLoanApplicationRequest();
        lar2.setFirstName("NameLongerThanAllowableLengthLL");
        unacceptableLoanApplicationRequests.put(testName.apply(++n), lar2);

        LoanApplicationRequestDTO lar3 = defaultLoanApplicationRequest();
        lar3.setFirstName("NameNotOnlyWithLetter1:')*-_=");
        unacceptableLoanApplicationRequests.put(testName.apply(++n), lar3);

        LoanApplicationRequestDTO lar4 = defaultLoanApplicationRequest();
        lar4.setLastName("Z");
        unacceptableLoanApplicationRequests.put(testName.apply(++n), lar4);

        LoanApplicationRequestDTO lar5 = defaultLoanApplicationRequest();
        lar5.setLastName("NameLongerThanAllowableLengthLL");
        unacceptableLoanApplicationRequests.put(testName.apply(++n), lar5);

        LoanApplicationRequestDTO lar6 = defaultLoanApplicationRequest();
        lar6.setLastName("NameNotOnlyWithLetter1:')*-_=");
        unacceptableLoanApplicationRequests.put(testName.apply(++n), lar6);

        LoanApplicationRequestDTO lar7 = defaultLoanApplicationRequest();
        lar7.setMiddleName("Z");
        unacceptableLoanApplicationRequests.put(testName.apply(++n), lar7);

        LoanApplicationRequestDTO lar8 = defaultLoanApplicationRequest();
        lar8.setMiddleName("NameLongerThanAllowableLengthLL");
        unacceptableLoanApplicationRequests.put(testName.apply(++n), lar8);

        LoanApplicationRequestDTO lar9 = defaultLoanApplicationRequest();
        lar9.setMiddleName("NameNotOnlyWithLetter1:')*-_=");
        unacceptableLoanApplicationRequests.put(testName.apply(++n), lar9);

        LoanApplicationRequestDTO lar10 = defaultLoanApplicationRequest();
        lar10.setEmail("tooLongBegginingOfEmailASDFGHJKLQWERTYUIOPZXCVB1234@ar.ar");
        unacceptableLoanApplicationRequests.put(testName.apply(++n), lar10);

        LoanApplicationRequestDTO lar11 = defaultLoanApplicationRequest();
        lar11.setEmail("name1name@too.longEmailPostfix12345");
        unacceptableLoanApplicationRequests.put(testName.apply(++n), lar11);

        LoanApplicationRequestDTO lar12 = defaultLoanApplicationRequest();
        lar12.setEmail("a@a");
        unacceptableLoanApplicationRequests.put(testName.apply(++n), lar12);

        LoanApplicationRequestDTO lar13 = defaultLoanApplicationRequest();
        lar13.setEmail("name1last1nameAgmail.com");
        unacceptableLoanApplicationRequests.put(testName.apply(++n), lar13);

        LoanApplicationRequestDTO lar14 = defaultLoanApplicationRequest();
        lar14.setTerm(5);
        unacceptableLoanApplicationRequests.put(testName.apply(++n), lar14);

        LoanApplicationRequestDTO lar15 = defaultLoanApplicationRequest();
        lar15.setBirthdate(LocalDate.now().minusYears(18).plusDays(1));
        unacceptableLoanApplicationRequests.put(testName.apply(++n), lar15);

        LoanApplicationRequestDTO lar16 = defaultLoanApplicationRequest();
        lar16.setPassportSeries("111");
        unacceptableLoanApplicationRequests.put(testName.apply(++n), lar16);

        LoanApplicationRequestDTO lar17 = defaultLoanApplicationRequest();
        lar17.setPassportSeries("11111");
        unacceptableLoanApplicationRequests.put(testName.apply(++n), lar17);

        LoanApplicationRequestDTO lar18 = defaultLoanApplicationRequest();
        lar18.setPassportSeries("11e1");
        unacceptableLoanApplicationRequests.put(testName.apply(++n), lar18);

        LoanApplicationRequestDTO lar19 = defaultLoanApplicationRequest();
        lar19.setPassportNumber("12345");
        unacceptableLoanApplicationRequests.put(testName.apply(++n), lar19);

        LoanApplicationRequestDTO lar20 = defaultLoanApplicationRequest();
        lar20.setPassportNumber("1234567");
        unacceptableLoanApplicationRequests.put(testName.apply(++n), lar20);

        LoanApplicationRequestDTO lar21 = defaultLoanApplicationRequest();
        lar21.setPassportNumber("123Q56");
        unacceptableLoanApplicationRequests.put(testName.apply(++n), lar21);
    }

    @Test
    @DisplayName("PreScoring exceptions test")
    void preScore() {
        LoanApplicationRequestDTO defaultLoanApplicationRequest = defaultLoanApplicationRequest();
        log.info("[PreScore exception test]: testing default loan application request (must not throw exceptions)");
        assertDoesNotThrow(() -> preScoreService.preScore(defaultLoanApplicationRequest));

        String allowableEmailExample = "allowableEmailMainPart.EvenWithSomeNumbers.12345@emailPostfix.nums12";
        defaultLoanApplicationRequest.setEmail(allowableEmailExample);
        log.info("[PreScore exception test]: testing application request with email \"{}\" (must not throw exceptions)",
                allowableEmailExample);
        assertDoesNotThrow(() -> preScoreService.preScore(defaultLoanApplicationRequest));

        log.info("[PreScore exception test]: testing application request with absent middle name " +
                "(must not throw exceptions)");
        defaultLoanApplicationRequest.setMiddleName(null);
        assertDoesNotThrow(() -> preScoreService.preScore(defaultLoanApplicationRequest));
        defaultLoanApplicationRequest.setMiddleName("");
        assertDoesNotThrow(() -> preScoreService.preScore(defaultLoanApplicationRequest));

        String[] exceptionMessages = new String[]{
                "first name", "first name", "first name",
                "last name", "last name", "last name",
                "middle name", "middle name", "middle name",
                "email", "email", "email", "email", "term", "age",
                "passport series", "passport series", "passport series",
                "passport number", "passport number", "passport number"
        };

        for (int i = 1; i <= 21; i++) {
            int testNumber = i;
            log.info("[PreScore exception test {}]: PreScoreException should be thrown with message about " +
                    "unacceptable {}", testNumber, exceptionMessages[testNumber - 1]);

            assertThrows(PreScoreException.class, () -> {
                try {
                    preScoreService.preScore(
                            unacceptableLoanApplicationRequests.get(
                                    testName.apply(testNumber)));
                } catch (PreScoreException e) {
                    log.info("[PreScore exception test {}]: {} caught. Message: \"{}\"", testNumber,
                            e.getClass().getSimpleName(), e.getMessage());
                    throw e;
                }
            });
        }
    }

    LoanApplicationRequestDTO defaultLoanApplicationRequest() {
        return new LoanApplicationRequestDTO(
                new BigDecimal(500_000),
                24,
                "Andrei",
                "Andreev",
                "Andreevich",
                "example@gmail.com",
                LocalDate.now().minusYears(30),
                "5735",
                "984510"
        );
    }
}