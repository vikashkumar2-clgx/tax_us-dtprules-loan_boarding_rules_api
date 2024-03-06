package com.corelogic.tax.tpd.taxservicingrulesapi;

import com.corelogic.tax.tpd.taxservicingrulesapi.controllers.DroolsController;
import com.corelogic.tax.tpd.taxservicingrulesapi.services.AutoDecisionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class TaxServicingRulesApiApplicationTests {

    @Autowired
    DroolsController controller;

    @Autowired
    AutoDecisionService service;

    @Test
    void contextLoads() {
        assertThat(controller).isNotNull();
        assertThat(service).isNotNull();
    }
}
