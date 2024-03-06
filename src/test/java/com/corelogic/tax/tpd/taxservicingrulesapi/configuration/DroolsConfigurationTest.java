package com.corelogic.tax.tpd.taxservicingrulesapi.configuration;

import com.corelogic.tax.tpd.taxservicingrulesapi.model.TaxFactLBD;
import com.corelogic.tax.tpd.taxservicingrulesapi.model.TaxFactLBDFlags;
import com.corelogic.tax.tpd.taxservicingrulesapi.model.TaxFactLBDResults;
import org.kie.api.KieBase;
import org.kie.api.definition.KiePackage;
import org.kie.api.definition.rule.Rule;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.corelogic.tax.tpd.taxservicingrulesapi.model.TaxFactLBDDecisionStatus.NO_DECISION;
import static com.corelogic.tax.tpd.taxservicingrulesapi.model.TaxFactLBDDecisionStatus.PAY;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DroolsConfiguration.class)
@TestPropertySource(properties = {
        "drools.rules.classpath=rules"
})
@SpringBootTest
@ActiveProfiles("test")
@Slf4j
class DroolsConfigurationTest {

    @Autowired
    public KieContainer kieContainer;

    @Test
    void testIfRuleFires() {
        KieSession kieSession = kieContainer.newKieSession();
        TaxFactLBD taxFactLBDToApplyRuleOn = TaxFactLBD.builder()
            .flags(TaxFactLBDFlags.builder()
                .build())
            .result(TaxFactLBDResults.builder()
                .taxFactLBDDecisionStatus(NO_DECISION)
                .build())
            .build();

        kieSession.getAgenda().getAgendaGroup("ditech").setFocus();
        //insert the doc into rules-engine
        kieSession.insert(taxFactLBDToApplyRuleOn);
        //fire all the rules
        kieSession.fireAllRules();
        //stop the rules engine - no memory leak - held outside scope of method as a singleton inside Kie.
        //Needs to be explicitly destroyed
        kieSession.dispose();
        kieSession.destroy();

        //assert what the rule does.
        assertThat(taxFactLBDToApplyRuleOn.getResult().getTaxFactLBDDecisionStatus()).isEqualTo(PAY);
    }

    @Test
    void whenRuleIsDisabledItIsNotLoadedIntoKieFileSystem() {
        for (String kieBaseName : kieContainer.getKieBaseNames()) {
            KieBase kieBase = kieContainer.getKieBase(kieBaseName);
            for (KiePackage kiePackage : kieBase.getKiePackages()) {
                List<String> ruleNames = kiePackage.getRules().stream().map(Rule::getName).collect(Collectors.toList());
                    assertThat(ruleNames).isNotNull();
                    assertThat(ruleNames).doesNotContain("RUSHMORE_0022_MR_DISBURSEMENTSTOP_K");
            }
        }
    }

}
