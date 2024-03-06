package com.corelogic.tax.tpd.taxservicingrulesapi.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(value = "inactive")
@Data
public class InactiveRulesConfiguration {
    List<String> rules;
}
