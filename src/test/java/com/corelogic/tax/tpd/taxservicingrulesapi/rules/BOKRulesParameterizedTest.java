package com.corelogic.tax.tpd.taxservicingrulesapi.rules;
import com.corelogic.tax.tpd.taxservicingrulesapi.data.entities.DroolEntity;
import com.corelogic.tax.tpd.taxservicingrulesapi.model.*;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;
import static java.util.Arrays.asList;

class BOKRulesParameterizedTest extends TestOneRuleBase {

    private static final String CLIENT_ID = "0042532";

    @BeforeEach
    void setUp() throws FileNotFoundException {
        List<DroolEntity> drools = droolService.doEntryForDroolFile(CLIENT_ID);
        Mockito.when(droolRepository.findAllByIsEnabled(true)).thenReturn(drools);
    }

    @Nested
    @DisplayName("BOK_0000_DEFAULT_PAY")
    class BOK_0000_DEFAULT_PAY {

        private static final String RULE_NAME = "BOK_0000_DEFAULT_PAY";

        @TestFactory
        @DisplayName("Parameterized Test")
        Stream<DynamicTest> check() {
            return createTests(asList(
                    TestOneRuleBase.TestObject.builder()
                            .displayName("Happy path default")
                            .flags(TaxFactLBDFlags.builder()
                                    .build())
                            .expectedResult(PAY(
                                    "NRAP",
                                    TaxFactLBDRuleInfo.builder()
                                            .name(RULE_NAME)
                                            .action(null)
                                            .priority(1000)
                                            .build()))
                            .build()
            ), RULE_NAME, CLIENT_ID);
        }
    }

    @Nested
    @DisplayName("BOK_0001_PAY_DENIAL_2")
    class BOK_0001_PAY_DENIAL_2 {

        private static final String RULE_NAME = "BOK_0001_PAY_DENIAL_2";

        @TestFactory
        @DisplayName("Parameterized test")
        Stream<DynamicTest> check() {
            return createTests(asList(
                    TestObject.builder()
                            .displayName("Happy path when second response is D")
                            .flags(TaxFactLBDFlags.builder()
                                    .taxFactLBDPayResponse(asList(
                                            TaxFactLBDPayResponse.builder()
                                                    .response("P")
                                                    .build(),
                                            TaxFactLBDPayResponse.builder()
                                                    .response("D")
                                                    .build()
                                    ))
                                    .parcelId("123")
                                    .taxAmt(BigDecimal.valueOf(100))
                                    .custAgencyId("456")
                                    .agencyName("coolAgency")
                                    .economicLossDate(LocalDate.of(2020, 02, 02))
                                    .build())
                            .expectedResult(DO_NOT_PAY(
                                    "PD2",
                                    "Parcel 123 is being reported as $ 100.00 for payee 456 coolAgency with a 02/02/2020 due date." +
                                            " Due to loan being rejected for payment by the servicing system, unable to pay current taxes due at this time. [CL DTP]",
                                    TaxFactLBDRuleInfo.builder()
                                            .name(RULE_NAME)
                                            .priority(80)
                                            .action(null)
                                            .build()
                            ))
                            .build(),
                    TestObject.builder()
                            .displayName("Sad path when response is not second time D")
                            .flags(TaxFactLBDFlags.builder()
                                    .taxFactLBDPayResponse(asList(
                                            TaxFactLBDPayResponse.builder()
                                                    .response("D")
                                                    .build(),
                                            TaxFactLBDPayResponse.builder()
                                                    .response("P")
                                                    .build()
                                    ))
                                    .build())
                            .expectedResult(RULE_DOES_NOT_APPLY)
                            .build(),
                    TestObject.builder()
                            .displayName("Sad path when response size is greater than 2")
                            .flags(TaxFactLBDFlags.builder()
                                    .taxFactLBDPayResponse(asList(
                                            TaxFactLBDPayResponse.builder()
                                                    .response("D")
                                                    .build(),
                                            TaxFactLBDPayResponse.builder()
                                                    .response("P")
                                                    .build(),
                                            TaxFactLBDPayResponse.builder()
                                                    .response("D")
                                                    .build()
                                    ))
                                    .build())
                            .expectedResult(RULE_DOES_NOT_APPLY)
                            .build(),
                    TestObject.builder()
                            .displayName("Sad path when response size is less than 2")
                            .flags(TaxFactLBDFlags.builder()
                                    .taxFactLBDPayResponse(asList(
                                            TaxFactLBDPayResponse.builder()
                                                    .response("D")
                                                    .build()
                                    ))
                                    .build())
                            .expectedResult(RULE_DOES_NOT_APPLY)
                            .build(),
                    TestObject.builder()
                            .displayName("Sad path when second response is null")
                            .flags(TaxFactLBDFlags.builder()
                                    .taxFactLBDPayResponse(asList(
                                            TaxFactLBDPayResponse.builder()
                                                    .response("D")
                                                    .build(),
                                            TaxFactLBDPayResponse.builder()
                                                    .response(null)
                                                    .build()
                                    ))
                                    .build())
                            .expectedResult(RULE_DOES_NOT_APPLY)
                            .build()
            ), RULE_NAME, CLIENT_ID);
        }
    }
}