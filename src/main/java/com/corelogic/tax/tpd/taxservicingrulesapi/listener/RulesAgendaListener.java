package com.corelogic.tax.tpd.taxservicingrulesapi.listener;

import lombok.extern.slf4j.Slf4j;
import org.drools.core.event.DefaultAgendaEventListener;
import org.kie.api.definition.rule.Rule;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.runtime.rule.Match;

@Slf4j
public class RulesAgendaListener extends DefaultAgendaEventListener {

    @Override
    public void afterMatchFired(AfterMatchFiredEvent event) {
        Match match = event.getMatch();
        Rule rule = match.getRule();

        log.info("Rule fired: " + rule.getName());
    }
}
