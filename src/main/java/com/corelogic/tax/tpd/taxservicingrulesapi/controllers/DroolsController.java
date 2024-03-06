package com.corelogic.tax.tpd.taxservicingrulesapi.controllers;

import com.corelogic.tax.tpd.taxservicingrulesapi.model.TaxFactLBD;
import com.corelogic.tax.tpd.taxservicingrulesapi.services.AutoDecisionService;
import com.corelogic.tax.tpd.taxservicingrulesapi.services.DroolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class DroolsController {

    AutoDecisionService autoDecisionService;
    @Autowired
    DroolService droolService;

    @Autowired
    public DroolsController(AutoDecisionService autoDecisionService) {
        this.autoDecisionService = autoDecisionService;
    }

    @PatchMapping("/autoDecision/taxFactLoanBoarding")
    public ResponseEntity<TaxFactLBD> createTaxFact(@RequestBody TaxFactLBD taxFactLBD) {
        autoDecisionService.applyDecision(taxFactLBD);
        return ResponseEntity.ok().body(taxFactLBD);
    }

    @GetMapping("/droolDataEntry")
    public ResponseEntity<String> droolDataEntry(@RequestParam(required = false) String clientId){
        String success = "";
        try {
            droolService.doEntryForDroolFile(clientId);
            success = "Drool Entry successfully";
        }catch(Exception ex){
            ex.printStackTrace();
            success = ex.getMessage();
        }
        return ResponseEntity.ok().body(success);
    }
}
