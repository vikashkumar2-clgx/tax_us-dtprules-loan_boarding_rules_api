package com.corelogic.tax.tpd.taxservicingrulesapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaxFactLBDResults {
    private TaxFactLBDDecisionStatus taxFactLBDDecisionStatus;
    @Builder.Default
    private boolean isManualReview = false;
    @Builder.Default
    private List<TaxFactLBDRuleInfo> rulesApplied = new ArrayList<>();
    private String reason;
    private Boolean payOverride;
    private String overrideType;
    private String notes;
    private boolean rollDueDate;
    private String paymentBatchCode;
    private String batchReason;
    private BigDecimal bkTaxLineAmountUpdate;
    private String bkBillCodeUpdate;
}
