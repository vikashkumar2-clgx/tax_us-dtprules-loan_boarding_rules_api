package com.corelogic.tax.tpd.taxservicingrulesapi.configuration;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ClientsMappingConfiguration {

    @Value("${client-type.mapping.rushmore}")
    String[] rushmoreClients;
    @Value("${client-type.mapping.ditech}")
    String[] ditechClients;
    @Value("${client-type.mapping.flagstar}")
    String[] flagstarClients;
    @Value("${client-type.mapping.servicemac}")
    String[] servicemacClients;
    @Value("${client-type.mapping.homepoint}")
    String[] homepointClients;
    @Value("${client-type.mapping.mt}")
    String[] mtClients;
    @Value("${client-type.mapping.planethome}")
    String[] planethomeClients;
    @Value("${client-type.mapping.phh}")
    String[] phhClients;
    @Value("${client-type.mapping.huntington}")
    String[] huntingtonClients;
    @Value("${client-type.mapping.tms}")
    String[] tmsClients;
    @Value("${client-type.mapping.loandepot}")
    String[] loandepotClients;
    @Value("${client-type.mapping.roundpoint}")
    String[] roundpointClients;
    @Value("${client-type.mapping.umpqua}")
    String[] umpquaClients;
    @Value("${client-type.mapping.bok}")
    String[] bokClients;
    @Value("${client-type.mapping.regions}")
    String[] regionsClients;
    @Value("${client-type.mapping.penfed}")
    String[] penFedClients;
    @Value("${client-type.mapping.7servicing}")
    String[] sevenServicingClients;

    @Bean(name = "clientId2clientTypeMapping")
    Map<String, String> clientId2clientTypeMapping() {
        Map<String, String> bean = new HashMap<>();

        addClientToBean(bean, rushmoreClients, "rushmore");
        addClientToBean(bean, ditechClients, "ditech");
        addClientToBean(bean, flagstarClients, "flagstar");
        addClientToBean(bean, servicemacClients, "servicemac");
        addClientToBean(bean, homepointClients, "homepoint");
        addClientToBean(bean, mtClients, "mt");
        addClientToBean(bean, planethomeClients, "planethome");
        addClientToBean(bean, phhClients, "phh");
        addClientToBean(bean, huntingtonClients, "huntington");
        addClientToBean(bean, tmsClients, "tms");
        addClientToBean(bean, loandepotClients, "loandepot");
        addClientToBean(bean, roundpointClients, "roundpoint");
        addClientToBean(bean, umpquaClients, "umpqua");
        addClientToBean(bean, bokClients, "bok");
        addClientToBean(bean, regionsClients, "regions");
        addClientToBean(bean, penFedClients, "penfed");
        addClientToBean(bean, sevenServicingClients, "7servicing");
        return bean;
    }

    private void addClientToBean(Map<String, String> bean, String[] clients, String clientType) {
        for (String client : clients) {
            if (bean.containsKey(client)) {
                log.warn("ClientID to ClientType mapping duplication of client id: " + client + " for " + clientType);
            }
            bean.put(client, clientType);
        }
    }
}
