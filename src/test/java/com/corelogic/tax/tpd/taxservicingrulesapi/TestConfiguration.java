package com.corelogic.tax.tpd.taxservicingrulesapi;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import com.corelogic.tax.tpd.taxservicingrulesapi.services.DroolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import java.io.FileNotFoundException;

@Configuration
public class TestConfiguration  {
    @Autowired
    DataSource dataSource;

    @Value("${app.datasource.main.schemad}")
    String initSchemaResource;

    @Autowired
    DroolService droolService;
    @PostConstruct
    public void initialize() throws FileNotFoundException {
        System.out.println("TestConfiguration:initialize");
        Resource initSchema = new ClassPathResource(initSchemaResource);
        DatabasePopulator databasePopulator = new ResourceDatabasePopulator(initSchema);
        DatabasePopulatorUtils.execute(databasePopulator, dataSource);
        droolService.doEntryForDroolFile(null);
    }
}