package com.corelogic.tax.tpd.taxservicingrulesapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

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
public class TaxFactLBD {
    private String clientId;
    private TaxFactLBDFlags flags;
    @Builder.Default
    private TaxFactLBDResults result = new TaxFactLBDResults();
}
