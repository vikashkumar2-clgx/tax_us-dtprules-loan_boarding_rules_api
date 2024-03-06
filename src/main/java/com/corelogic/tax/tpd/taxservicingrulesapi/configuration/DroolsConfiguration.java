package com.corelogic.tax.tpd.taxservicingrulesapi.configuration;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

import com.corelogic.tax.tpd.taxservicingrulesapi.data.entities.DroolEntity;
import com.corelogic.tax.tpd.taxservicingrulesapi.data.repository.DroolRepository;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.runtime.KieContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = {InactiveRulesConfiguration.class})
@Slf4j
public class DroolsConfiguration {

    @Value("${drools.rules.classpath}")
    public String rulesPath;

    @Autowired
    InactiveRulesConfiguration inactiveRulesConfiguration;

    @Autowired
    DroolRepository droolRepository;

    @Bean
    @RefreshScope
    public KieContainer kieContainer(KieServices kieServices, KieModule kieModule) {
        return kieServices.newKieContainer(kieModule.getReleaseId());
    }

    @Bean
    @RefreshScope
    public KieModule kieModule(KieServices kieServices) throws IOException {
        KieFileSystem kieFileSystem = createKieFileSystemAndLoadRulesFromDatabase(kieServices);
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll();

        // Check if the drl files has syntax/compilation errors
        Results results = kieBuilder.getResults();
        if( results.hasMessages( Message.Level.ERROR ) ){
            throw new IllegalStateException( results.getMessages().toString() );
        }

        return kieBuilder.getKieModule();
    }

    @Bean
    @RefreshScope
    public KieServices kieServices() {
        return KieServices.Factory.get();
    }

    private KieFileSystem createKieFileSystemAndLoadRulesFromDatabase(KieServices kieServices) throws IOException{
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();

        List<DroolEntity> drools =  droolRepository.findAllByIsEnabled(true);
        drools =  drools.stream().filter(drool -> {
            return inactiveRulesConfiguration.getRules().indexOf(drool.getFileName()) == -1;
        }).collect(Collectors.toList());

        if(drools!=null && !drools.isEmpty()){
            for (DroolEntity drool : drools) {
                kieFileSystem.write("src/main/resources/rules/"+drool.getFileName(), drool.getFileContent());
            }
        }
        return kieFileSystem;
    }
}
