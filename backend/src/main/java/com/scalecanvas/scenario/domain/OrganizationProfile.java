package com.scalecanvas.scenario.domain;

public record OrganizationProfile(
        int teamSize,
        MaturityLevel operationsMaturity,
        int deploymentFrequencyPerWeek,
        boolean onCallAvailable,
        MaturityLevel observabilityMaturity,
        MaturityLevel cloudExperience,
        BudgetBand budgetBand) {
}
