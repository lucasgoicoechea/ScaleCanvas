package com.scalecanvas.scenario.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CloudPricingCatalog {

    private final Map<CatalogKey, BigDecimal> prices;

    public CloudPricingCatalog(Map<CatalogKey, BigDecimal> prices) {
        this.prices = Map.copyOf(prices);
    }

    public static CloudPricingCatalog defaults() {
        Map<CatalogKey, BigDecimal> prices = Map.ofEntries(
                Map.entry(new CatalogKey(CloudProvider.AWS, "COMPUTE", "us-east-1"), new BigDecimal("24.50")),
                Map.entry(new CatalogKey(CloudProvider.AWS, "COMPUTE", "eu-west-1"), new BigDecimal("27.80")),
                Map.entry(new CatalogKey(CloudProvider.AWS, "COMPUTE", "sa-east-1"), new BigDecimal("31.20")),
                Map.entry(new CatalogKey(CloudProvider.AWS, "STORAGE", "us-east-1"), new BigDecimal("0.10")),
                Map.entry(new CatalogKey(CloudProvider.AWS, "STORAGE", "eu-west-1"), new BigDecimal("0.12")),
                Map.entry(new CatalogKey(CloudProvider.AWS, "STORAGE", "sa-east-1"), new BigDecimal("0.14")),
                Map.entry(new CatalogKey(CloudProvider.AWS, "BANDWIDTH", "us-east-1"), new BigDecimal("0.09")),

                Map.entry(new CatalogKey(CloudProvider.GCP, "COMPUTE", "us-east-1"), new BigDecimal("23.10")),
                Map.entry(new CatalogKey(CloudProvider.GCP, "COMPUTE", "europe-west1"), new BigDecimal("26.40")),
                Map.entry(new CatalogKey(CloudProvider.GCP, "COMPUTE", "southamerica-east1"), new BigDecimal("29.90")),
                Map.entry(new CatalogKey(CloudProvider.GCP, "STORAGE", "us-east-1"), new BigDecimal("0.09")),
                Map.entry(new CatalogKey(CloudProvider.GCP, "STORAGE", "europe-west1"), new BigDecimal("0.11")),
                Map.entry(new CatalogKey(CloudProvider.GCP, "STORAGE", "southamerica-east1"), new BigDecimal("0.13")),
                Map.entry(new CatalogKey(CloudProvider.GCP, "BANDWIDTH", "us-east-1"), new BigDecimal("0.08")),

                Map.entry(new CatalogKey(CloudProvider.AZURE, "COMPUTE", "eastus"), new BigDecimal("25.20")),
                Map.entry(new CatalogKey(CloudProvider.AZURE, "COMPUTE", "westeurope"), new BigDecimal("28.50")),
                Map.entry(new CatalogKey(CloudProvider.AZURE, "COMPUTE", "brazilsouth"), new BigDecimal("32.10")),
                Map.entry(new CatalogKey(CloudProvider.AZURE, "STORAGE", "eastus"), new BigDecimal("0.10")),
                Map.entry(new CatalogKey(CloudProvider.AZURE, "STORAGE", "westeurope"), new BigDecimal("0.12")),
                Map.entry(new CatalogKey(CloudProvider.AZURE, "STORAGE", "brazilsouth"), new BigDecimal("0.15")),
                Map.entry(new CatalogKey(CloudProvider.AZURE, "BANDWIDTH", "eastus"), new BigDecimal("0.08")),

                Map.entry(new CatalogKey(CloudProvider.ON_PREM, "COMPUTE", "default"), new BigDecimal("18.00")),
                Map.entry(new CatalogKey(CloudProvider.ON_PREM, "STORAGE", "default"), new BigDecimal("0.05")),
                Map.entry(new CatalogKey(CloudProvider.ON_PREM, "BANDWIDTH", "default"), new BigDecimal("0.04")),

                Map.entry(new CatalogKey(CloudProvider.HYBRID, "COMPUTE", "default"), new BigDecimal("21.00")),
                Map.entry(new CatalogKey(CloudProvider.HYBRID, "STORAGE", "default"), new BigDecimal("0.07")),
                Map.entry(new CatalogKey(CloudProvider.HYBRID, "BANDWIDTH", "default"), new BigDecimal("0.06"))
        );
        return new CloudPricingCatalog(prices);
    }

    public BigDecimal unitCost(CloudProvider provider, String serviceType, String region) {
        CatalogKey key = new CatalogKey(provider, serviceType, region);
        BigDecimal value = prices.get(key);
        if (value == null) {
            return BigDecimal.ZERO;
        }
        return value;
    }

    public record CatalogKey(CloudProvider provider, String serviceType, String region) {
        public CatalogKey {
            Objects.requireNonNull(provider, "provider");
            Objects.requireNonNull(serviceType, "serviceType");
            Objects.requireNonNull(region, "region");
        }
    }
}
