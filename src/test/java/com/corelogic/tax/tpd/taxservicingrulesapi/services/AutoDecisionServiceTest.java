package com.corelogic.tax.tpd.taxservicingrulesapi.services;

import com.corelogic.tax.tpd.taxservicingrulesapi.listener.RulesAgendaListener;
import java.util.HashMap;
import java.util.Map;

import com.corelogic.tax.tpd.taxservicingrulesapi.model.TaxFactLBD;
import com.corelogic.tax.tpd.taxservicingrulesapi.model.TaxFactLBDResults;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.Agenda;
import org.kie.api.runtime.rule.AgendaGroup;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class AutoDecisionServiceTest {

    @Mock
    public KieContainer kieContainer;

    @Mock
    KieSession kieSession;

    private AutoDecisionService subject;
    private Map<String, String> clientTypeMapping = new HashMap<>();
    private String clientId = "some client id";
    private String clientType = "some client type";
    private Agenda agenda = Mockito.mock(Agenda.class);
    private AgendaGroup agendaGroup = Mockito.mock(AgendaGroup.class);
    private TaxFactLBD taxFactLBDToBePassed = TaxFactLBD.builder()
        .clientId(clientId)
        .result(TaxFactLBDResults.builder()
            .build())
        .build();


    @BeforeEach
    void setUp() {
        subject = new AutoDecisionService(kieContainer, clientTypeMapping);

        clientTypeMapping.put(clientId, clientType);

        when(kieContainer.newKieSession()).thenReturn(kieSession);
        when(kieSession.insert(any())).thenReturn(null);
        when(kieSession.getAgenda()).thenReturn(agenda);
        when(agenda.getAgendaGroup(clientType)).thenReturn(agendaGroup);

        doNothing().when(agendaGroup).setFocus();
        doNothing().when(kieSession).dispose();
        doNothing().when(kieSession).destroy();
    }

    @Test
    @DisplayName("will run drools engine rules to apply decision to passed in taxFact, no rules fired")
    void applyDecision_NoRulesFired() {
        when(kieSession.fireAllRules()).thenReturn(-1);

        subject.applyDecision(taxFactLBDToBePassed);

        verify(kieContainer).newKieSession();
        verify(kieSession).insert(taxFactLBDToBePassed);
        verify(kieSession).fireAllRules();
        verify(kieSession).dispose();
        verify(kieSession).destroy();
        verify(kieSession).getAgenda();
        verify(kieSession).addEventListener(any(RulesAgendaListener.class));
        verify(agenda).getAgendaGroup(clientType);
        verify(agendaGroup).setFocus();

        verifyNoMoreInteractions(kieSession);
        verifyNoMoreInteractions(agenda);
        verifyNoMoreInteractions(agendaGroup);
    }

    @Test
    @DisplayName("will run drools engine rules to apply decision to passed in taxFact, one rule fired")
    void applyDecision_oneRuleFired() {

        when(kieSession.fireAllRules()).thenReturn(1);

        subject.applyDecision(taxFactLBDToBePassed);

        verify(kieContainer).newKieSession();
        verify(kieSession).insert(taxFactLBDToBePassed);
        verify(kieSession).fireAllRules();
        verify(kieSession).dispose();
        verify(kieSession).destroy();
        verify(kieSession).getAgenda();
        verify(kieSession).addEventListener(any(RulesAgendaListener.class));
        verify(agenda).getAgendaGroup(clientType);
        verify(agendaGroup).setFocus();

        verifyNoMoreInteractions(kieSession);
        verifyNoMoreInteractions(agenda);
        verifyNoMoreInteractions(agendaGroup);
    }

    @Test
    void applyDecisionForUnknownClientThrowException() {
        TaxFactLBD taxFactLBD = TaxFactLBD.builder().clientId("unknown client id").build();

        ThrowableAssert.ThrowingCallable functionCall = () -> subject.applyDecision(taxFactLBD);

        assertThatThrownBy(functionCall).isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Unknown client id: unknown client id. Can not calculate client type");
    }
}
