package com.corelogic.tax.tpd.taxservicingrulesapi.rules;

import com.corelogic.tax.tpd.taxservicingrulesapi.configuration.ClientsMappingConfiguration;
import com.corelogic.tax.tpd.taxservicingrulesapi.configuration.DroolsConfiguration;
import com.corelogic.tax.tpd.taxservicingrulesapi.data.repository.DroolRepository;
import com.corelogic.tax.tpd.taxservicingrulesapi.listener.RulesAgendaListener;
import com.corelogic.tax.tpd.taxservicingrulesapi.model.*;
import com.corelogic.tax.tpd.taxservicingrulesapi.services.DroolService;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.extension.ExtendWith;

import lombok.Builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DroolsConfiguration.class, ClientsMappingConfiguration.class})
@TestPropertySource(properties = {
    "drools.rules.classpath=src/main/resources/rules",
})
@SpringBootTest
@ActiveProfiles("test")
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@ComponentScan(basePackages = {"com.corelogic.tax.tpd.taxservicingrulesapi.services"})
class TestOneRuleBase {
    @Autowired
    DroolService droolService;
    @MockBean
    DroolRepository droolRepository;

    @Autowired
    @Qualifier("clientId2clientTypeMapping")
    Map<String, String> clientTypeMapping;
    @Autowired
    KieContainer kieContainer;
    public static final TaxFactLBDResults RULE_DOES_NOT_APPLY = TaxFactLBDResults.builder().build();

    public static TaxFactLBDResults DO_NOT_PAY(TaxFactLBDRuleInfo ruleInfo) {
        return TaxFactLBDResults.builder()
            .taxFactLBDDecisionStatus(TaxFactLBDDecisionStatus.DO_NOT_PAY)
            .rulesApplied(asList(ruleInfo))
            .build();
    }
    
    public static TaxFactLBDResults DO_NOT_PAY(String reason, TaxFactLBDRuleInfo ruleInfo) {
        return TaxFactLBDResults.builder()
            .taxFactLBDDecisionStatus(TaxFactLBDDecisionStatus.DO_NOT_PAY)
            .rulesApplied(asList(ruleInfo))
            .reason(reason)
            .build();
    }

    public static TaxFactLBDResults DO_NOT_PAY(String reason, String notes, TaxFactLBDRuleInfo ruleInfo) {
        return TaxFactLBDResults.builder()
            .taxFactLBDDecisionStatus(TaxFactLBDDecisionStatus.DO_NOT_PAY)
            .reason(reason)
            .notes(notes)
            .rulesApplied(asList(ruleInfo))
            .build();
    }

    public static TaxFactLBDResults DO_NOT_PAY(String reason, boolean rollDueDate, TaxFactLBDRuleInfo ruleInfo) {
        return TaxFactLBDResults.builder()
            .taxFactLBDDecisionStatus(TaxFactLBDDecisionStatus.DO_NOT_PAY)
            .reason(reason)
            .rollDueDate(rollDueDate)
            .rulesApplied(asList(ruleInfo))
            .build();
    }

    public static TaxFactLBDResults DO_NOT_PAY(String reason, String notes, boolean rollDueDate, TaxFactLBDRuleInfo ruleInfo) {
        return TaxFactLBDResults.builder()
            .taxFactLBDDecisionStatus(TaxFactLBDDecisionStatus.DO_NOT_PAY)
            .reason(reason)
            .notes(notes)
            .rollDueDate(rollDueDate)
            .rulesApplied(asList(ruleInfo))
            .build();
    }

    public static TaxFactLBDResults MANUAL_REVIEW(TaxFactLBDRuleInfo ruleInfo) {
        return TaxFactLBDResults.builder()
            .taxFactLBDDecisionStatus(TaxFactLBDDecisionStatus.NO_DECISION)
            .rulesApplied(asList(ruleInfo))
            .isManualReview(true).build();
    }

    public static TaxFactLBDResults MANUAL_REVIEW(String notes, TaxFactLBDRuleInfo ruleInfo) {
        return TaxFactLBDResults.builder()
            .taxFactLBDDecisionStatus(TaxFactLBDDecisionStatus.NO_DECISION)
            .notes(notes)
            .rulesApplied(asList(ruleInfo))
            .isManualReview(true).build();
    }

    public static TaxFactLBDResults MANUAL_REVIEW(String reason, String notes, TaxFactLBDRuleInfo ruleInfo) {
        return TaxFactLBDResults.builder()
            .taxFactLBDDecisionStatus(TaxFactLBDDecisionStatus.NO_DECISION)
            .reason(reason)
            .notes(notes)
            .rulesApplied(asList(ruleInfo))
            .isManualReview(true).build();
    }

    public static TaxFactLBDResults MANUAL_REVIEW(String reason, String notes, TaxFactLBDRuleInfo ruleInfo, boolean rollDueDate) {
        return TaxFactLBDResults.builder()
            .taxFactLBDDecisionStatus(TaxFactLBDDecisionStatus.NO_DECISION)
            .reason(reason)
            .notes(notes)
            .rulesApplied(asList(ruleInfo))
            .rollDueDate(rollDueDate)
            .isManualReview(true).build();
    }
    public static TaxFactLBDResults MANUAL_REVIEW(String reason, String overrideType, String notes, TaxFactLBDRuleInfo ruleInfo) {
        return TaxFactLBDResults.builder()
                .taxFactLBDDecisionStatus(TaxFactLBDDecisionStatus.NO_DECISION)
                .reason(reason)
                .payOverride(true)
                .overrideType(overrideType)
                .notes(notes)
                .rulesApplied(asList(ruleInfo))
                .isManualReview(true).build();
    }

    public static TaxFactLBDResults MANUAL_REVIEW(String reason, String notes, boolean rollDueDate, TaxFactLBDRuleInfo ruleInfo, BigDecimal bkTaxLineAmountUpdate) {
        return TaxFactLBDResults.builder()
                .taxFactLBDDecisionStatus(TaxFactLBDDecisionStatus.NO_DECISION)
                .reason(reason)
                .notes(notes)
                .rollDueDate(rollDueDate)
                .rulesApplied(asList(ruleInfo))
                .bkTaxLineAmountUpdate(bkTaxLineAmountUpdate)
                .isManualReview(true)
                .build();
    }

    public static TaxFactLBDResults PAY(String reason, String overrideType, String notes, TaxFactLBDRuleInfo ruleInfo) {
        return TaxFactLBDResults.builder()
            .taxFactLBDDecisionStatus(TaxFactLBDDecisionStatus.PAY)
            .reason(reason)
            .payOverride(true)
            .overrideType(overrideType)
            .notes(notes)
            .rulesApplied(asList(ruleInfo))
            .isManualReview(false).build();
    }

    public static TaxFactLBDResults PAY(TaxFactLBDRuleInfo ruleInfo) {
        return TaxFactLBDResults.builder()
            .taxFactLBDDecisionStatus(TaxFactLBDDecisionStatus.PAY)
            .reason(null)
            .notes(null)
            .rulesApplied(asList(ruleInfo))
            .isManualReview(false).build();
    }

    public static TaxFactLBDResults PAY(String reason, String notes, TaxFactLBDRuleInfo ruleInfo) {
        return TaxFactLBDResults.builder()
            .taxFactLBDDecisionStatus(TaxFactLBDDecisionStatus.PAY)
            .reason(reason)
            .notes(notes)
            .rulesApplied(asList(ruleInfo))
            .isManualReview(false).build();
    }

    public static TaxFactLBDResults PAY(String reason, TaxFactLBDRuleInfo ruleInfo) {
        return TaxFactLBDResults.builder()
            .taxFactLBDDecisionStatus(TaxFactLBDDecisionStatus.PAY)
            .rulesApplied(asList(ruleInfo))
            .reason(reason)
            .build();
    }

    public static TaxFactLBDResults BATCH(String paymentBatchCode, String batchReason, TaxFactLBDRuleInfo ruleInfo) {
        return TaxFactLBDResults.builder()
            .batchReason(batchReason)
            .paymentBatchCode(paymentBatchCode)
            .rulesApplied(asList(ruleInfo))
            .build();
    }

    public void runOneRule(TaxFactLBDRuleInfo ruleInfo, TaxFactLBD taxFactLBD) {
        KieSession kieSession = kieContainer.newKieSession();

        // Trace rule name to log
        RulesAgendaListener listener = new RulesAgendaListener();
        kieSession.addEventListener(listener);

        // Choose set of rules
        kieSession.getAgenda().getAgendaGroup(clientTypeMapping.get(taxFactLBD.getClientId())).setFocus();

        //insert the doc as a fact into rules-engine
        kieSession.insert(taxFactLBD);
        //fire all the rules
        kieSession.fireAllRules(match -> match.getRule().getName().equals(ruleInfo.getName()));

        //stop the rules engine - no memory leak - held outside scope of method as a singleton inside Kie.
        //Needs to be explicitly destroyed
        kieSession.dispose();
        kieSession.destroy();
    }

    @Builder
    public static class TestObject {

        String displayName;
        TaxFactLBDFlags flags;
        TaxFactLBDResults expectedResult;
    }

    public void checkTest(TestObject data, TaxFactLBDRuleInfo ruleInfo, String clientId) {
        TaxFactLBD taxFactLBD = TaxFactLBD
            .builder()
            .clientId(clientId)
            .flags(data.flags)
            .build();

        runOneRule(ruleInfo, taxFactLBD);

        assertThat(taxFactLBD.getResult()).isEqualTo(data.expectedResult);
    }

    public Stream<DynamicTest> createTests(List<TestObject> ids, String ruleName, String clientId) {
        TaxFactLBDRuleInfo ruleInfo = TaxFactLBDRuleInfo.builder().name(ruleName).build();
        return ids.stream().map(data -> DynamicTest.dynamicTest(data.displayName, () -> checkTest(data, ruleInfo, clientId)));
    }
    public static TaxFactLBDResults DO_NOT_PAY(String reason, String notes, boolean rollDueDate, TaxFactLBDRuleInfo ruleInfo, BigDecimal bkTaxLineAmountUpdate) {
        return TaxFactLBDResults.builder()
                .taxFactLBDDecisionStatus(TaxFactLBDDecisionStatus.DO_NOT_PAY)
                .reason(reason)
                .notes(notes)
                .rollDueDate(rollDueDate)
                .rulesApplied(asList(ruleInfo))
                .bkTaxLineAmountUpdate(bkTaxLineAmountUpdate)
                .build();
    }
    public static TaxFactLBDResults DO_NOT_PAY(String reason, String notes, TaxFactLBDRuleInfo ruleInfo, BigDecimal bkTaxLineAmountUpdate) {
        return TaxFactLBDResults.builder()
                .taxFactLBDDecisionStatus(TaxFactLBDDecisionStatus.DO_NOT_PAY)
                .reason(reason)
                .notes(notes)
                .rulesApplied(asList(ruleInfo))
                .bkTaxLineAmountUpdate(bkTaxLineAmountUpdate)
                .build();
    }
    public static TaxFactLBDResults DO_NOT_PAY(String reason, String notes, boolean rollDueDate, TaxFactLBDRuleInfo ruleInfo, BigDecimal bkTaxLineAmountUpdate, String bkBillCodeUpdate) {
        return TaxFactLBDResults.builder()
                .taxFactLBDDecisionStatus(TaxFactLBDDecisionStatus.DO_NOT_PAY)
                .reason(reason)
                .notes(notes)
                .rollDueDate(rollDueDate)
                .rulesApplied(asList(ruleInfo))
                .bkTaxLineAmountUpdate(bkTaxLineAmountUpdate)
                .bkBillCodeUpdate(bkBillCodeUpdate)
                .build();
    }
    public static TaxFactLBDResults DO_NOT_PAY(String reason, String notes, TaxFactLBDRuleInfo ruleInfo, BigDecimal bkTaxLineAmountUpdate, String bkBillCodeUpdate) {
        return TaxFactLBDResults.builder()
                .taxFactLBDDecisionStatus(TaxFactLBDDecisionStatus.DO_NOT_PAY)
                .reason(reason)
                .notes(notes)
                .rulesApplied(asList(ruleInfo))
                .bkTaxLineAmountUpdate(bkTaxLineAmountUpdate)
                .bkBillCodeUpdate(bkBillCodeUpdate)
                .build();
    }
}
