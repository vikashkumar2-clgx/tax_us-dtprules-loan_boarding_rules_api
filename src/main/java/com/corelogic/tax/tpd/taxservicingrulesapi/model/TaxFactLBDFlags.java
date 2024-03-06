package com.corelogic.tax.tpd.taxservicingrulesapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaxFactLBDFlags {
    private String pendingApportionmentFlag;
    private String amtPrcsFlgDelq;
    private String amtPrcsFlgPaid;
    private String taxExemptCd;
    private String stateCd;
    private String agencyId;
    private String agencyName;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate loanOrigDt;
    private BigDecimal princpalBal;
    private String arrsFlag;
    private String disbursementStop;
    private String analysisStop;
    private String foreclosureStop;
    private String invstCd;
    private String invstCategoryCd;
    private BigDecimal taxAmt; // Disbursement Amount
    @JsonProperty(value = "MSPStatus")
    private String mspStatus;
    private String escrowInd;
    private String loTypeCode;

    private BigDecimal bklnEscrowBalance;
    private BigDecimal bklnEscrowADVBalance;
    private String bkLoanServicerManualCode;
    private String bklnCustOrigOffcCode;
    private String custAgencyId;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate installmentDate;
    private String installmentNumber;
    private String tarNumber;
    private String parcelId;
    private List<TaxFactLBDPayResponse> taxFactLBDPayResponse;
    private int lienRejectedCount;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate dueDate;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate clExpectedDueDate;
    private String term;
    private String bkSpclHndlCd;
    @Builder.Default
    private List<String> autoDoNotPayStates = new ArrayList<>();
    private String payoffStop;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate economicLossDate;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate businessDaysDate;
    private String reportingType;
    private BigDecimal duplicateBillFee;
    private String acceptsPostmark;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate tarRunDate;

    private String bkLoanProcessFlag;
    private BigDecimal installmentAmount;
    private String newLoanCd;
    private String statePaymentOption;
}
