package com.corelogic.tax.tpd.taxservicingrulesapi;

import co.elastic.apm.attach.ElasticApmAttacher;

import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication(exclude = {ElasticsearchAutoConfiguration.class, ElasticsearchDataAutoConfiguration.class})
@EntityScan(value = {"com.corelogic.tax.tpd.taxservicingrulesapi.data.entities"})
public class TaxServicingRulesApiApplication {

	@Value("${kibana-feature-flag:false}")
	private static boolean kibanaFeatureFlag;

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("America/Los_Angeles"));
		if(kibanaFeatureFlag) {
			ElasticApmAttacher.attach();
		}
		SpringApplication.run(TaxServicingRulesApiApplication.class, args);
	}
}
