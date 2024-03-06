package com.corelogic.tax.tpd.taxservicingrulesapi.services;

import com.corelogic.tax.tpd.taxservicingrulesapi.data.entities.DroolEntity;
import com.corelogic.tax.tpd.taxservicingrulesapi.data.repository.DroolRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DroolService {
    @Value("${drools.rules.classpath}")
    public String rulesPath;

    @Autowired
    private DroolRepository droolRepository;

    public List<DroolEntity> doEntryForDroolFile(String clientIdParam) throws FileNotFoundException {
        File[] files = ResourceUtils.getFile(rulesPath).listFiles();
        List<DroolEntity> drools = new ArrayList<>();
        if (files != null) {
            FileFilter filter = new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.getName().endsWith(".drl");
                }
            };
            List<File> fileList = new ArrayList<>();

            if(clientIdParam!=null && !clientIdParam.trim().equals("")){
                fileList = Arrays.stream(files).filter(file -> file.getName().equals(clientIdParam)).collect(Collectors.toList());
            }else{
                fileList = Arrays.stream(files).collect(Collectors.toList());
            }
            fileList.forEach(file -> {
                if (file.isDirectory()) {
                    try {
                        String clientId = file.getName();
                        File[] droolFiles = ResourceUtils.getFile(file.getAbsolutePath()).listFiles(filter);
                        if (droolFiles != null) {
                            Arrays.stream(droolFiles).forEach(droolFile -> {
                                try {
                                    InputStream is = new FileInputStream(droolFile);
                                    byte[] content = new byte[is.available()];
                                    is.read(content);
                                    is.close();

                                    DroolEntity existingEntity = droolRepository.findByFileName(droolFile.getName());

                                    DroolEntity entity = DroolEntity.builder()
                                            .id(UUID.randomUUID())
                                            .clientId(clientId)
                                            .ruleId(UUID.randomUUID())
                                            .fileName(droolFile.getName())
                                            .fileContent(content)
                                            .isEnabled(true)
                                            .lastUpdatedTs(ZonedDateTime.now()).build();

                                    if(existingEntity!=null) {
                                        entity.setId(existingEntity.getId());
                                    }
                                    droolRepository.save(entity);
                                    //List<DroolEntity> drools_ = droolRepository.findAll();
                                    drools.add(entity);
                                    log.info("Drool Entry for file {} created successfully", droolFile.getName());

                                } catch (Exception e) {
                                    log.info("Error in Drool Entry for file {} : {}", droolFile.getName(), e.getMessage());
                                }
                            });
                        }
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
        return drools;
    }

    public List<DroolEntity> findAllByIsEnabled(boolean isEnabled){
        return droolRepository.findAllByIsEnabled(isEnabled);
    }
}
