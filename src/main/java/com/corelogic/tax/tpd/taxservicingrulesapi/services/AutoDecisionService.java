package com.corelogic.tax.tpd.taxservicingrulesapi.services;

import com.corelogic.tax.tpd.taxservicingrulesapi.listener.RulesAgendaListener;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.corelogic.tax.tpd.taxservicingrulesapi.model.TaxFactLBD;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AutoDecisionService {

    KieContainer kieContainer;
    Map<String, String> clientTypeMapping;

    @Value("${autoDoNotPayStates.rushmore}")
    public List<String> rushmoreAutoDoNotPayStates;

    @Value("${autoDoNotPayStates.flagstar}")
    public List<String> flagstarAutoDoNotPayStates;

    @Value("${autoDoNotPayStates.servicemac}")
    public List<String> servicemacAutoDoNotPayStates;

    @Value("${autoDoNotPayStates.homepoint}")
    public List<String> homepointAutoDoNotPayStates;

    @Value("${autoDoNotPayStates.mt}")
    public List<String> mtAutoDoNotPayStates;

    @Value("${autoDoNotPayStates.planethome}")
    public List<String> planethomeAutoDoNotPayStates;

    @Value("${autoDoNotPayStates.tms}")
    public List<String> tmsAutoDoNotPayStates;

    @Value("${autoDoNotPayStates.huntington}")
    public List<String> huntingtonAutoDoNotPayStates;

    @Value("${autoDoNotPayStates.loandepot}")
    public List<String> loandepotAutoDoNotPayStates;

    @Value("${autoDoNotPayStates.roundpoint}")
    public List<String> roundpointAutoDoNotPayStates;

    @Autowired
    public AutoDecisionService(
        KieContainer kieContainer,
        @Qualifier("clientId2clientTypeMapping") Map<String, String> clientTypeMapping) {
        this.kieContainer = kieContainer;
        this.clientTypeMapping = clientTypeMapping;
    }

    public void applyDecision(TaxFactLBD taxFactLBD) {
        String clientType = this.clientTypeMapping.get(taxFactLBD.getClientId());
        if (clientType == null) {
            throw new NullPointerException("Unknown client id: " + taxFactLBD.getClientId() + ". Can not calculate client type");
        }
        if(taxFactLBD.getFlags() != null) {
            List<String> autoDoNotPayStates = Collections.emptyList();
            if (clientType.equals("rushmore")) {
                autoDoNotPayStates = rushmoreAutoDoNotPayStates;
            }
            else if (clientType.equals("flagstar")) {
                autoDoNotPayStates = flagstarAutoDoNotPayStates;
            }
            else if(clientType.equals("servicemac")){
                autoDoNotPayStates = servicemacAutoDoNotPayStates;
            }
            else if(clientType.equals("homepoint")){
                autoDoNotPayStates = homepointAutoDoNotPayStates;
            }
            else if(clientType.equals("mt")){
                autoDoNotPayStates = mtAutoDoNotPayStates;
            }
            else if(clientType.equals("planethome")){
                autoDoNotPayStates = planethomeAutoDoNotPayStates;
            }
            else if(clientType.equals("tms")){
                autoDoNotPayStates = tmsAutoDoNotPayStates;
            }
            else if(clientType.equals("huntington")){
                autoDoNotPayStates = huntingtonAutoDoNotPayStates;
            }
            else if(clientType.equals("loandepot")){
                autoDoNotPayStates = loandepotAutoDoNotPayStates;
            }
            else if(clientType.equals("roundpoint")){
                autoDoNotPayStates = roundpointAutoDoNotPayStates;
            }
            taxFactLBD.getFlags().setAutoDoNotPayStates(autoDoNotPayStates);
        }

        // Create session
        KieSession kieSession = kieContainer.newKieSession();

        // This is not thread safe object. Do not put it as a bean
        RulesAgendaListener listener = new RulesAgendaListener();

        kieSession.addEventListener(listener);

        // Choose set of rules
        kieSession.getAgenda().getAgendaGroup(clientType).setFocus();

        //insert the doc as a fact into rules-engine
        kieSession.insert(taxFactLBD);
        //fire all the rules
        kieSession.fireAllRules();

        //stop the rules engine - no memory leak - held outside scope of method as a singleton inside Kie.
        //Needs to be explicitly destroyed
        kieSession.dispose();
        kieSession.destroy();
    }
}
