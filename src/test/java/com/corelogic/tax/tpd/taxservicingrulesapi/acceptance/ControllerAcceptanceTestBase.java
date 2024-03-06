package com.corelogic.tax.tpd.taxservicingrulesapi.acceptance;

import com.corelogic.tax.tpd.taxservicingrulesapi.model.*;
import com.corelogic.tax.tpd.taxservicingrulesapi.model.TaxFactLBDFlags;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.extension.ExtendWith;

import lombok.Builder;
import lombok.Data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
public class ControllerAcceptanceTestBase {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    protected static String CLIENT_ID;
    protected static boolean MANUAL_REVIEW = true;

    @Builder
    @Data
    public static class CheckResultArgs {

        private TaxFactLBDFlags requestFlags;
        private TaxFactLBDDecisionStatus decisionStatus;
        @Builder.Default
        private Boolean isManualReview = false;
        private List<TaxFactLBDRuleInfo> ruleNames;
        private String ruleReason;
        private String overrideType;
        private Boolean payOverride;
        private String notes;
        private String paymentBatchCode;
        private String batchReason;
        private boolean rollDueDate;
        private BigDecimal bkTaxLineAmountUpdate;
        private String bkBillCodeUpdate;
    }

    public void checkResult(CheckResultArgs arg) throws Exception {
        // GIVEN
        TaxFactLBD request = TaxFactLBD
            .builder()
            .clientId(CLIENT_ID)
            .flags(arg.getRequestFlags())
            .build();

        // WHEN
        ResultActions autoDecisionDefaultAction = mockMvc.perform(patch("/api/autoDecision/taxFactLoanBoarding")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(mapper.writeValueAsString(request)))
            .andExpect(status().is2xxSuccessful());

        // THEN
        TaxFactLBD autoTaxFactLBD = mapper.readValue(
            autoDecisionDefaultAction.andReturn().getResponse().getContentAsString(),
            TaxFactLBD.class);

        assertThat(autoTaxFactLBD.getResult()).isEqualToComparingFieldByFieldRecursively(TaxFactLBDResults
            .builder()
            .taxFactLBDDecisionStatus(arg.getDecisionStatus())
            .isManualReview(arg.getIsManualReview())
            .rulesApplied(arg.getRuleNames())
            .reason(arg.getRuleReason())
            .payOverride(arg.getPayOverride())
            .overrideType(arg.getOverrideType())
            .rollDueDate(arg.isRollDueDate())
            .notes(arg.notes)
            .paymentBatchCode(arg.paymentBatchCode)
            .batchReason(arg.batchReason)
            .bkTaxLineAmountUpdate(arg.bkTaxLineAmountUpdate)
            .bkBillCodeUpdate(arg.bkBillCodeUpdate)
            .build());
    }

    public void checkResult(TaxFactLBDFlags requestFlags, TaxFactLBDDecisionStatus decisionStatus, Boolean isManualReview,
                            List<TaxFactLBDRuleInfo> ruleNames, String ruleReason, String overrideType, Boolean payOverride, String notes) throws Exception {
        checkResult(CheckResultArgs
            .builder()
            .requestFlags(requestFlags)
            .decisionStatus(decisionStatus)
            .isManualReview(isManualReview)
            .ruleNames(ruleNames)
            .ruleReason(ruleReason)
            .overrideType(overrideType)
            .payOverride(payOverride)
            .notes(notes)
            .build());
    }

    public void checkResult(TaxFactLBDFlags requestFlags, TaxFactLBDDecisionStatus decisionStatus, Boolean isManualReview,
                            List<TaxFactLBDRuleInfo> ruleNames, String ruleReason, String overrideType, Boolean payOverride) throws Exception {
        checkResult(CheckResultArgs
            .builder()
            .requestFlags(requestFlags)
            .decisionStatus(decisionStatus)
            .isManualReview(isManualReview)
            .ruleNames(ruleNames)
            .ruleReason(ruleReason)
            .overrideType(overrideType)
            .payOverride(payOverride)
            .build());
    }

    public void checkResult(TaxFactLBDFlags requestFlags, TaxFactLBDDecisionStatus decisionStatus, Boolean isManualReview,
                            List<TaxFactLBDRuleInfo> rulesApplied) throws Exception {
        checkResult(CheckResultArgs
            .builder()
            .requestFlags(requestFlags)
            .decisionStatus(decisionStatus)
            .isManualReview(isManualReview)
            .ruleNames(rulesApplied)
            .build());
    }

    public void checkResult(TaxFactLBDFlags requestFlags, TaxFactLBDDecisionStatus decisionStatus, Boolean isManualReview,
                            List<TaxFactLBDRuleInfo> rulesApplied, String ruleReason) throws Exception {
        checkResult(CheckResultArgs
            .builder()
            .requestFlags(requestFlags)
            .decisionStatus(decisionStatus)
            .isManualReview(isManualReview)
            .ruleNames(rulesApplied)
            .ruleReason(ruleReason)
            .build());
    }

    public void checkResult(TaxFactLBDFlags requestFlags, TaxFactLBDDecisionStatus decisionStatus, Boolean isManualReview,
                            List<TaxFactLBDRuleInfo> rulesApplied, String ruleReason, String notes) throws Exception {
        checkResult(CheckResultArgs
            .builder()
            .requestFlags(requestFlags)
            .decisionStatus(decisionStatus)
            .isManualReview(isManualReview)
            .ruleNames(rulesApplied)
            .ruleReason(ruleReason)
            .notes(notes)
            .build());
    }

    public void checkResult(TaxFactLBDFlags requestFlags, TaxFactLBDDecisionStatus decisionStatus,
                            List<TaxFactLBDRuleInfo> rulesApplied) throws Exception {
        checkResult(CheckResultArgs
            .builder()
            .requestFlags(requestFlags)
            .decisionStatus(decisionStatus)
            .ruleNames(rulesApplied)
            .build());
    }

    public void checkResult(TaxFactLBDFlags requestFlags, TaxFactLBDDecisionStatus decisionStatus,
                            List<TaxFactLBDRuleInfo> rulesApplied, String reason) throws Exception {
        checkResult(CheckResultArgs
            .builder()
            .requestFlags(requestFlags)
            .decisionStatus(decisionStatus)
            .ruleNames(rulesApplied)
            .ruleReason(reason)
            .isManualReview(false)
            .build());
    }

    public void checkResult(TaxFactLBDFlags requestFlags, TaxFactLBDDecisionStatus decisionStatus,
                            List<TaxFactLBDRuleInfo> rulesApplied, String reason, String notes) throws Exception {
        checkResult(CheckResultArgs
            .builder()
            .requestFlags(requestFlags)
            .decisionStatus(decisionStatus)
            .ruleNames(rulesApplied)
            .ruleReason(reason)
            .isManualReview(false)
            .notes(notes)
            .build());
    }

    public void checkResult(TaxFactLBDFlags requestFlags, TaxFactLBDDecisionStatus decisionStatus,
                            List<TaxFactLBDRuleInfo> rulesApplied, String reason, String notes, Boolean rollDueDate) throws Exception {
        checkResult(CheckResultArgs
            .builder()
            .requestFlags(requestFlags)
            .decisionStatus(decisionStatus)
            .ruleNames(rulesApplied)
            .ruleReason(reason)
            .isManualReview(false)
            .notes(notes)
            .rollDueDate(rollDueDate)
            .build());
    }

    public void checkResultRollDueDate(TaxFactLBDFlags requestFlags, TaxFactLBDDecisionStatus decisionStatus, Boolean isManualReview,
                                       List<TaxFactLBDRuleInfo> rulesApplied, String reason, String notes, Boolean rollDueDate) throws Exception {
        checkResult(CheckResultArgs
            .builder()
            .requestFlags(requestFlags)
            .decisionStatus(decisionStatus)
            .ruleNames(rulesApplied)
            .ruleReason(reason)
            .isManualReview(isManualReview)
            .notes(notes)
            .rollDueDate(rollDueDate)
            .build());
    }
    public void checkResult(TaxFactLBDFlags requestFlags, TaxFactLBDDecisionStatus decisionStatus,
                            List<TaxFactLBDRuleInfo> rulesApplied, String reason, String notes, BigDecimal bkTaxLineAmountUpdate) throws Exception {
        checkResult(CheckResultArgs
                .builder()
                .requestFlags(requestFlags)
                .decisionStatus(decisionStatus)
                .ruleNames(rulesApplied)
                .ruleReason(reason)
                .isManualReview(false)
                .notes(notes)
                .bkTaxLineAmountUpdate(bkTaxLineAmountUpdate)
                .build());
    }
    public void checkResult(TaxFactLBDFlags requestFlags, TaxFactLBDDecisionStatus decisionStatus,
                            List<TaxFactLBDRuleInfo> rulesApplied, String reason, Boolean isManualReview, String notes, Boolean rollDueDate, BigDecimal bkTaxLineAmountUpdate) throws Exception {
        checkResult(CheckResultArgs
                .builder()
                .requestFlags(requestFlags)
                .decisionStatus(decisionStatus)
                .ruleNames(rulesApplied)
                .ruleReason(reason)
                .isManualReview(isManualReview)
                .notes(notes)
                .rollDueDate(rollDueDate)
                .bkTaxLineAmountUpdate(bkTaxLineAmountUpdate)
                .build());
    }
    public void checkResult(TaxFactLBDFlags requestFlags, TaxFactLBDDecisionStatus decisionStatus,
                            List<TaxFactLBDRuleInfo> rulesApplied, String reason, String notes, Boolean rollDueDate, BigDecimal bkTaxLineAmountUpdate, String bkBillCodeUpdate) throws Exception {
        checkResult(CheckResultArgs
                .builder()
                .requestFlags(requestFlags)
                .decisionStatus(decisionStatus)
                .ruleNames(rulesApplied)
                .ruleReason(reason)
                .isManualReview(false)
                .notes(notes)
                .rollDueDate(rollDueDate)
                .bkTaxLineAmountUpdate(bkTaxLineAmountUpdate)
                .bkBillCodeUpdate(bkBillCodeUpdate)
                .build());
    }
    public void checkResult(TaxFactLBDFlags requestFlags, TaxFactLBDDecisionStatus decisionStatus,
                            List<TaxFactLBDRuleInfo> rulesApplied, String reason, String notes, BigDecimal bkTaxLineAmountUpdate, String bkBillCodeUpdate) throws Exception {
        checkResult(CheckResultArgs
                .builder()
                .requestFlags(requestFlags)
                .decisionStatus(decisionStatus)
                .ruleNames(rulesApplied)
                .ruleReason(reason)
                .isManualReview(false)
                .notes(notes)
                .bkTaxLineAmountUpdate(bkTaxLineAmountUpdate)
                .bkBillCodeUpdate(bkBillCodeUpdate)
                .build());
    }
}
