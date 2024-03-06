package com.corelogic.tax.tpd.taxservicingrulesapi.data.repository;

import com.corelogic.tax.tpd.taxservicingrulesapi.data.entities.DroolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DroolRepository extends JpaRepository<DroolEntity, Long> {

    List<DroolEntity> findAllByIsEnabled(boolean isEnabled);

    DroolEntity findByFileName(String fileName);
}
