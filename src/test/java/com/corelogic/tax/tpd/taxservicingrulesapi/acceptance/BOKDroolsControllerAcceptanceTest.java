package com.corelogic.tax.tpd.taxservicingrulesapi.acceptance;

import com.corelogic.tax.tpd.taxservicingrulesapi.model.TaxFactLBDFlags;
import com.corelogic.tax.tpd.taxservicingrulesapi.model.TaxFactLBDPayResponse;
import com.corelogic.tax.tpd.taxservicingrulesapi.model.TaxFactLBDRuleInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import static com.corelogic.tax.tpd.taxservicingrulesapi.model.TaxFactLBDDecisionStatus.*;
import static java.util.Arrays.asList;

class BOKDroolsControllerAcceptanceTest extends ControllerAcceptanceTestBase {

    @BeforeEach
    void setUp() {CLIENT_ID = "0042532"; }

    @Test
    @DisplayName("BOK_0000_DEFAULT_PAY")
    void BOK_0000_DEFAULT_PAY() throws Exception{
        checkResult(TaxFactLBDFlags.builder().build(),
                PAY,
                asList(TaxFactLBDRuleInfo.builder().priority(1000).name("BOK_0000_DEFAULT_PAY").build()),
                "NRAP" );
    }

    @Test
    @DisplayName("BOK_0001_PAY_DENIAL_2")
    void BOK_0001_PAY_DENIAL_2() throws Exception {
        checkResult(TaxFactLBDFlags.builder()
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
                        .build(),
                DO_NOT_PAY,
                Arrays.asList(
                        TaxFactLBDRuleInfo.builder().priority(1000).name("BOK_0000_DEFAULT_PAY").build(),
                        TaxFactLBDRuleInfo.builder().priority(80).name("BOK_0001_PAY_DENIAL_2").build()
                ),
                "PD2",
                "Parcel 123 is being reported as $ 100.00 for payee 456 coolAgency with a 02/02/2020 due date." +
                        " Due to loan being rejected for payment by the servicing system, unable to pay current taxes due at this time. [CL DTP]"
        );
    }

}
