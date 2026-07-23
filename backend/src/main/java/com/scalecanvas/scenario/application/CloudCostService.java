package com.scalecanvas.scenario.application;

import com.scalecanvas.scenario.api.dto.CloudPriceItem;
import com.scalecanvas.scenario.api.dto.ScenarioRequest;
import com.scalecanvas.scenario.domain.CloudPricingCatalog;
import com.scalecanvas.scenario.domain.CloudProvider;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CloudCostService {

    private final CloudPricingCatalog catalog;

    public CloudCostService(CloudPricingCatalog catalog) {
        this.catalog = catalog;
    }

    @Transactional(readOnly = true)
    public List<CloudPriceItem> estimate(ScenarioRequest request) {
        String region = switch (request.deployment().cloudProvider()) {
            case AWS -> "us-east-1";
            case GCP -> "us-east-1";
            case AZURE -> "eastus";
            default -> "default";
        };

        return request.deployment().serviceTopology().services().stream()
                .map(service -> {
                    BigDecimal computeUnitCost = catalog.unitCost(request.deployment().cloudProvider(), "COMPUTE", region);
                    BigDecimal storageUnitCost = catalog.unitCost(request.deployment().cloudProvider(), "STORAGE", region);
                    BigDecimal computeSubtotal = computeUnitCost.multiply(BigDecimal.valueOf(service.memoryMb() / 256))
                            .multiply(BigDecimal.valueOf(service.replicas()))
                            .setScale(2, RoundingMode.HALF_UP);
                    BigDecimal storageEstimate = request.data().currentStorageGb()
                            .multiply(request.data().monthlyGrowthPercentage().divide(BigDecimal.valueOf(100)))
                            .add(storageUnitCost)
                            .setScale(2, RoundingMode.HALF_UP);
                    BigDecimal monthlyTotal = computeSubtotal.add(storageEstimate)
                            .setScale(2, RoundingMode.HALF_UP);
                    BigDecimal yearlyTotal = monthlyTotal.multiply(BigDecimal.valueOf(12))
                            .setScale(2, RoundingMode.HALF_UP);
                    String driver = switch (request.deployment().serverType()) {
                        case SERVERLESS -> "Serverless granular billing";
                        case CONTAINER -> "Container node reservation";
                        case VM -> "VM reservation plus ingress";
                        case BARE_METAL -> "Hardware amortization";
                    };
                    return new CloudPriceItem(
                            service.serviceName(),
                            request.deployment().cloudProvider().name(),
                            "COMPUTE_STORAGE",
                            region,
                            computeUnitCost,
                            BigDecimal.valueOf(service.replicas()),
                            monthlyTotal,
                            yearlyTotal,
                            driver);
                })
                .toList();
    }
}
