package com.scalecanvas.scenario.domain;

import java.util.UUID;

public record ArchitectureScenario(
        UUID id,
        String name,
        String description,
        ProductType productType,
        WorkloadProfile workload,
        DataProfile data,
        QualityProfile quality,
        OrganizationProfile organization,
        DeploymentProfile deployment) {
}
