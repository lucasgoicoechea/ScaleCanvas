package com.scalecanvas.scenario.api;

import com.scalecanvas.scenario.api.dto.ScenarioRequest;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/golden-masters")
public class GoldenMasterController {

    @GetMapping
    public List<ScenarioRequest> list() {
        return List.of(
                buildSaaSBaseline(),
                buildEcommerceGrowth(),
                buildBankingApi(),
                buildSearchPlatform(),
                buildMobileSocial(),
                buildDocumentAi(),
                buildMarketplace(),
                buildIotIngestion(),
                buildInternalCrud(),
                buildStreamingMetadata()
        );
    }

    private ScenarioRequest buildSaaSBaseline() {
        return new ScenarioRequest(
                "SaaS baseline",
                "Standard multi-tenant SaaS with moderate traffic and standard SLOs",
                com.scalecanvas.scenario.domain.ProductType.SAAS_B2B,
                new ScenarioRequest.WorkloadRequest(
                        50000L,
                        12000L,
                        600L,
                        new BigDecimal("90"),
                        new BigDecimal("540"),
                        new BigDecimal("6"),
                        new BigDecimal("82"),
                        new BigDecimal("18"),
                        4096L,
                        1800000L,
                        2,
                        new BigDecimal("15")
                ),
                new ScenarioRequest.DataRequest(
                        new BigDecimal("90"),
                        new BigDecimal("7"),
                        24,
                        new BigDecimal("45"),
                        new BigDecimal("30"),
                        new BigDecimal("320000")
                ),
                new ScenarioRequest.QualityRequest(
                        85,
                        260,
                        750,
                        new BigDecimal("99.9"),
                        60,
                        15,
                        com.scalecanvas.scenario.domain.ConsistencyLevel.READ_YOUR_WRITES,
                        com.scalecanvas.scenario.domain.GeographicScope.MULTI_COUNTRY
                ),
                new ScenarioRequest.OrganizationRequest(
                        6,
                        com.scalecanvas.scenario.domain.MaturityLevel.MEDIUM,
                        4,
                        true,
                        com.scalecanvas.scenario.domain.MaturityLevel.MEDIUM,
                        com.scalecanvas.scenario.domain.MaturityLevel.MEDIUM,
                        com.scalecanvas.scenario.domain.BudgetBand.MODERATE
                ),
                new ScenarioRequest.DeploymentRequest(
                        com.scalecanvas.scenario.domain.ServerType.CONTAINER,
                        com.scalecanvas.scenario.domain.CloudProvider.AWS,
                        com.scalecanvas.scenario.domain.DeploymentService.EKS,
                        com.scalecanvas.scenario.domain.GatewayType.ALB,
                        com.scalecanvas.scenario.domain.LoadBalancerType.NLB,
                        4096,
                        2,
                        new ScenarioRequest.ServiceTopologyRequest(
                                5,
                                2,
                                2,
                                List.of(
                                        new ScenarioRequest.ServiceCapacityRequest("api", 1100L, 2048, 1, 3, com.scalecanvas.scenario.domain.ServiceBinding.SHARED),
                                        new ScenarioRequest.ServiceCapacityRequest("worker", 350L, 1024, 1, 2, com.scalecanvas.scenario.domain.ServiceBinding.SHARED)
                                )
                        )
                )
        );
    }

    private ScenarioRequest buildEcommerceGrowth() {
        return new ScenarioRequest(
                "E-commerce growth",
                "High-read e-commerce catalog with flash-sale bursts",
                com.scalecanvas.scenario.domain.ProductType.ECOMMERCE,
                new ScenarioRequest.WorkloadRequest(
                        250000L,
                        80000L,
                        5000L,
                        new BigDecimal("220"),
                        new BigDecimal("1800"),
                        new BigDecimal("8.18"),
                        new BigDecimal("94"),
                        new BigDecimal("6"),
                        2048L,
                        1200000L,
                        8,
                        new BigDecimal("25")
                ),
                new ScenarioRequest.DataRequest(
                        new BigDecimal("220"),
                        new BigDecimal("12"),
                        12,
                        new BigDecimal("120"),
                        new BigDecimal("45"),
                        new BigDecimal("900000")
                ),
                new ScenarioRequest.QualityRequest(
                        70,
                        220,
                        650,
                        new BigDecimal("99.95"),
                        30,
                        5,
                        com.scalecanvas.scenario.domain.ConsistencyLevel.EVENTUAL,
                        com.scalecanvas.scenario.domain.GeographicScope.GLOBAL
                ),
                new ScenarioRequest.OrganizationRequest(
                        18,
                        com.scalecanvas.scenario.domain.MaturityLevel.HIGH,
                        12,
                        true,
                        com.scalecanvas.scenario.domain.MaturityLevel.HIGH,
                        com.scalecanvas.scenario.domain.MaturityLevel.HIGH,
                        com.scalecanvas.scenario.domain.BudgetBand.FLEXIBLE
                ),
                new ScenarioRequest.DeploymentRequest(
                        com.scalecanvas.scenario.domain.ServerType.CONTAINER,
                        com.scalecanvas.scenario.domain.CloudProvider.AWS,
                        com.scalecanvas.scenario.domain.DeploymentService.EKS,
                        com.scalecanvas.scenario.domain.GatewayType.CLOUDFLARE,
                        com.scalecanvas.scenario.domain.LoadBalancerType.ALB,
                        8192,
                        4,
                        new ScenarioRequest.ServiceTopologyRequest(
                                10,
                                6,
                                6,
                                List.of(
                                        new ScenarioRequest.ServiceCapacityRequest("storefront", 4200L, 4096, 2, 6, com.scalecanvas.scenario.domain.ServiceBinding.DEDICATED),
                                        new ScenarioRequest.ServiceCapacityRequest("catalog", 2800L, 6144, 2, 5, com.scalecanvas.scenario.domain.ServiceBinding.DEDICATED),
                                        new ScenarioRequest.ServiceCapacityRequest("cart", 900L, 2048, 1, 3, com.scalecanvas.scenario.domain.ServiceBinding.SHARED),
                                        new ScenarioRequest.ServiceCapacityRequest("payment", 180L, 1024, 1, 2, com.scalecanvas.scenario.domain.ServiceBinding.DEDICATED),
                                        new ScenarioRequest.ServiceCapacityRequest("search", 1200L, 8192, 4, 3, com.scalecanvas.scenario.domain.ServiceBinding.DEDICATED)
                                )
                        )
                )
        );
    }

    private ScenarioRequest buildBankingApi() {
        return new ScenarioRequest(
                "Banking API",
                "Regulated banking backend with strict latency and consistency requirements",
                com.scalecanvas.scenario.domain.ProductType.BANKING_API,
                new ScenarioRequest.WorkloadRequest(
                        1200000L,
                        250000L,
                        8000L,
                        new BigDecimal("180"),
                        new BigDecimal("1400"),
                        new BigDecimal("7.77"),
                        new BigDecimal("70"),
                        new BigDecimal("30"),
                        8192L,
                        5000000L,
                        48,
                        new BigDecimal("35")
                ),
                new ScenarioRequest.DataRequest(
                        new BigDecimal("1800"),
                        new BigDecimal("4"),
                        120,
                        new BigDecimal("900"),
                        new BigDecimal("60"),
                        new BigDecimal("450000")
                ),
                new ScenarioRequest.QualityRequest(
                        45,
                        140,
                        400,
                        new BigDecimal("99.99"),
                        15,
                        0,
                        com.scalecanvas.scenario.domain.ConsistencyLevel.STRONG,
                        com.scalecanvas.scenario.domain.GeographicScope.MULTI_COUNTRY
                ),
                new ScenarioRequest.OrganizationRequest(
                        35,
                        com.scalecanvas.scenario.domain.MaturityLevel.HIGH,
                        3,
                        true,
                        com.scalecanvas.scenario.domain.MaturityLevel.HIGH,
                        com.scalecanvas.scenario.domain.MaturityLevel.MEDIUM,
                        com.scalecanvas.scenario.domain.BudgetBand.FLEXIBLE
                ),
                new ScenarioRequest.DeploymentRequest(
                        com.scalecanvas.scenario.domain.ServerType.CONTAINER,
                        com.scalecanvas.scenario.domain.CloudProvider.AZURE,
                        com.scalecanvas.scenario.domain.DeploymentService.APP_SERVICE,
                        com.scalecanvas.scenario.domain.GatewayType.NLB,
                        com.scalecanvas.scenario.domain.LoadBalancerType.NLB,
                        8192,
                        4,
                        new ScenarioRequest.ServiceTopologyRequest(
                                14,
                                9,
                                8,
                                List.of(
                                        new ScenarioRequest.ServiceCapacityRequest("accounts", 1600L, 8192, 4, 6, com.scalecanvas.scenario.domain.ServiceBinding.DEDICATED),
                                        new ScenarioRequest.ServiceCapacityRequest("ledger", 900L, 12288, 4, 5, com.scalecanvas.scenario.domain.ServiceBinding.DEDICATED),
                                        new ScenarioRequest.ServiceCapacityRequest("notifications", 600L, 2048, 2, 4, com.scalecanvas.scenario.domain.ServiceBinding.SHARED),
                                        new ScenarioRequest.ServiceCapacityRequest("fraud", 220L, 4096, 2, 3, com.scalecanvas.scenario.domain.ServiceBinding.DEDICATED)
                                )
                        )
                )
        );
    }

    private ScenarioRequest buildSearchPlatform() {
        return new ScenarioRequest(
                "Search platform",
                "Document and product search with heavy indexing and read traffic",
                com.scalecanvas.scenario.domain.ProductType.SEARCH_PLATFORM,
                new ScenarioRequest.WorkloadRequest(
                        900000L,
                        180000L,
                        12000L,
                        new BigDecimal("350"),
                        new BigDecimal("2600"),
                        new BigDecimal("7.43"),
                        new BigDecimal("92"),
                        new BigDecimal("8"),
                        512L,
                        500000L,
                        36,
                        new BigDecimal("45")
                ),
                new ScenarioRequest.DataRequest(
                        new BigDecimal("3400"),
                        new BigDecimal("18"),
                        36,
                        new BigDecimal("2100"),
                        new BigDecimal("25"),
                        new BigDecimal("2400000")
                ),
                new ScenarioRequest.QualityRequest(
                        60,
                        180,
                        500,
                        new BigDecimal("99.95"),
                        20,
                        15,
                        com.scalecanvas.scenario.domain.ConsistencyLevel.EVENTUAL,
                        com.scalecanvas.scenario.domain.GeographicScope.GLOBAL
                ),
                new ScenarioRequest.OrganizationRequest(
                        22,
                        com.scalecanvas.scenario.domain.MaturityLevel.HIGH,
                        8,
                        true,
                        com.scalecanvas.scenario.domain.MaturityLevel.HIGH,
                        com.scalecanvas.scenario.domain.MaturityLevel.HIGH,
                        com.scalecanvas.scenario.domain.BudgetBand.FLEXIBLE
                ),
                new ScenarioRequest.DeploymentRequest(
                        com.scalecanvas.scenario.domain.ServerType.CONTAINER,
                        com.scalecanvas.scenario.domain.CloudProvider.GCP,
                        com.scalecanvas.scenario.domain.DeploymentService.CLOUD_RUN,
                        com.scalecanvas.scenario.domain.GatewayType.CLOUDFLARE,
                        com.scalecanvas.scenario.domain.LoadBalancerType.NLB,
                        16384,
                        8,
                        new ScenarioRequest.ServiceTopologyRequest(
                                9,
                                5,
                                7,
                                List.of(
                                        new ScenarioRequest.ServiceCapacityRequest("ingest", 1500L, 16384, 8, 12, com.scalecanvas.scenario.domain.ServiceBinding.DEDICATED),
                                        new ScenarioRequest.ServiceCapacityRequest("indexer", 900L, 24576, 8, 8, com.scalecanvas.scenario.domain.ServiceBinding.DEDICATED),
                                        new ScenarioRequest.ServiceCapacityRequest("query", 3600L, 8192, 4, 10, com.scalecanvas.scenario.domain.ServiceBinding.DEDICATED),
                                        new ScenarioRequest.ServiceCapacityRequest("suggest", 1200L, 4096, 2, 5, com.scalecanvas.scenario.domain.ServiceBinding.SHARED)
                                )
                        )
                )
        );
    }

    private ScenarioRequest buildMobileSocial() {
        return new ScenarioRequest(
                "Mobile social",
                "Mobile-first social feed with media uploads and high async traffic",
                com.scalecanvas.scenario.domain.ProductType.MOBILE_APPLICATION,
                new ScenarioRequest.WorkloadRequest(
                        400000L,
                        160000L,
                        18000L,
                        new BigDecimal("280"),
                        new BigDecimal("2200"),
                        new BigDecimal("7.86"),
                        new BigDecimal("78"),
                        new BigDecimal("22"),
                        6144L,
                        12000000L,
                        12,
                        new BigDecimal("55")
                ),
                new ScenarioRequest.DataRequest(
                        new BigDecimal("7500"),
                        new BigDecimal("22"),
                        6,
                        new BigDecimal("6200"),
                        new BigDecimal("15"),
                        new BigDecimal("3800000")
                ),
                new ScenarioRequest.QualityRequest(
                        95,
                        280,
                        820,
                        new BigDecimal("99.9"),
                        45,
                        60,
                        com.scalecanvas.scenario.domain.ConsistencyLevel.EVENTUAL,
                        com.scalecanvas.scenario.domain.GeographicScope.GLOBAL
                ),
                new ScenarioRequest.OrganizationRequest(
                        26,
                        com.scalecanvas.scenario.domain.MaturityLevel.MEDIUM,
                        7,
                        true,
                        com.scalecanvas.scenario.domain.MaturityLevel.MEDIUM,
                        com.scalecanvas.scenario.domain.MaturityLevel.HIGH,
                        com.scalecanvas.scenario.domain.BudgetBand.FLEXIBLE
                ),
                new ScenarioRequest.DeploymentRequest(
                        com.scalecanvas.scenario.domain.ServerType.CONTAINER,
                        com.scalecanvas.scenario.domain.CloudProvider.AWS,
                        com.scalecanvas.scenario.domain.DeploymentService.ECS,
                        com.scalecanvas.scenario.domain.GatewayType.KONG,
                        com.scalecanvas.scenario.domain.LoadBalancerType.NLB,
                        8192,
                        4,
                        new ScenarioRequest.ServiceTopologyRequest(
                                11,
                                6,
                                8,
                                List.of(
                                        new ScenarioRequest.ServiceCapacityRequest("feed", 3800L, 6144, 2, 8, com.scalecanvas.scenario.domain.ServiceBinding.SHARED),
                                        new ScenarioRequest.ServiceCapacityRequest("media", 1400L, 16384, 4, 10, com.scalecanvas.scenario.domain.ServiceBinding.SERVERLESS),
                                        new ScenarioRequest.ServiceCapacityRequest("chat", 900L, 2048, 2, 6, com.scalecanvas.scenario.domain.ServiceBinding.SHARED),
                                        new ScenarioRequest.ServiceCapacityRequest("ads", 600L, 3072, 2, 4, com.scalecanvas.scenario.domain.ServiceBinding.DEDICATED)
                                )
                        )
                )
        );
    }

    private ScenarioRequest buildDocumentAi() {
        return new ScenarioRequest(
                "Document AI metadata",
                "Document ingestion pipeline with async processing and object storage",
                com.scalecanvas.scenario.domain.ProductType.DOCUMENT_AI,
                new ScenarioRequest.WorkloadRequest(
                        32000L,
                        8500L,
                        700L,
                        new BigDecimal("140"),
                        new BigDecimal("960"),
                        new BigDecimal("6.86"),
                        new BigDecimal("40"),
                        new BigDecimal("60"),
                        256000L,
                        25000000L,
                        160,
                        new BigDecimal("75")
                ),
                new ScenarioRequest.DataRequest(
                        new BigDecimal("1200"),
                        new BigDecimal("26"),
                        84,
                        new BigDecimal("980"),
                        new BigDecimal("20"),
                        new BigDecimal("1400000")
                ),
                new ScenarioRequest.QualityRequest(
                        110,
                        340,
                        950,
                        new BigDecimal("99.9"),
                        90,
                        60,
                        com.scalecanvas.scenario.domain.ConsistencyLevel.READ_YOUR_WRITES,
                        com.scalecanvas.scenario.domain.GeographicScope.COUNTRY
                ),
                new ScenarioRequest.OrganizationRequest(
                        10,
                        com.scalecanvas.scenario.domain.MaturityLevel.MEDIUM,
                        4,
                        false,
                        com.scalecanvas.scenario.domain.MaturityLevel.LOW,
                        com.scalecanvas.scenario.domain.MaturityLevel.MEDIUM,
                        com.scalecanvas.scenario.domain.BudgetBand.MODERATE
                ),
                new ScenarioRequest.DeploymentRequest(
                        com.scalecanvas.scenario.domain.ServerType.SERVERLESS,
                        com.scalecanvas.scenario.domain.CloudProvider.GCP,
                        com.scalecanvas.scenario.domain.DeploymentService.CLOUD_RUN,
                        com.scalecanvas.scenario.domain.GatewayType.CLOUDFLARE,
                        com.scalecanvas.scenario.domain.LoadBalancerType.CLB,
                        2048,
                        1,
                        new ScenarioRequest.ServiceTopologyRequest(
                                7,
                                4,
                                5,
                                List.of(
                                        new ScenarioRequest.ServiceCapacityRequest("upload", 500L, 2048, 1, 20, com.scalecanvas.scenario.domain.ServiceBinding.SERVERLESS),
                                        new ScenarioRequest.ServiceCapacityRequest("ocr", 300L, 4096, 2, 30, com.scalecanvas.scenario.domain.ServiceBinding.SERVERLESS),
                                        new ScenarioRequest.ServiceCapacityRequest("index", 700L, 2048, 1, 15, com.scalecanvas.scenario.domain.ServiceBinding.SERVERLESS)
                                )
                        )
                )
        );
    }

    private ScenarioRequest buildMarketplace() {
        return new ScenarioRequest(
                "Marketplace",
                "Two-sided marketplace with buyers, sellers, and search-heavy browsing",
                com.scalecanvas.scenario.domain.ProductType.MARKETPLACE,
                new ScenarioRequest.WorkloadRequest(
                        180000L,
                        65000L,
                        4000L,
                        new BigDecimal("170"),
                        new BigDecimal("1450"),
                        new BigDecimal("8.53"),
                        new BigDecimal("88"),
                        new BigDecimal("12"),
                        6144L,
                        3000000L,
                        18,
                        new BigDecimal("20")
                ),
                new ScenarioRequest.DataRequest(
                        new BigDecimal("560"),
                        new BigDecimal("10"),
                        18,
                        new BigDecimal("310"),
                        new BigDecimal("35"),
                        new BigDecimal("710000")
                ),
                new ScenarioRequest.QualityRequest(
                        90,
                        240,
                        680,
                        new BigDecimal("99.95"),
                        30,
                        10,
                        com.scalecanvas.scenario.domain.ConsistencyLevel.READ_YOUR_WRITES,
                        com.scalecanvas.scenario.domain.GeographicScope.MULTI_COUNTRY
                ),
                new ScenarioRequest.OrganizationRequest(
                        14,
                        com.scalecanvas.scenario.domain.MaturityLevel.MEDIUM,
                        6,
                        true,
                        com.scalecanvas.scenario.domain.MaturityLevel.MEDIUM,
                        com.scalecanvas.scenario.domain.MaturityLevel.MEDIUM,
                        com.scalecanvas.scenario.domain.BudgetBand.MODERATE
                ),
                new ScenarioRequest.DeploymentRequest(
                        com.scalecanvas.scenario.domain.ServerType.CONTAINER,
                        com.scalecanvas.scenario.domain.CloudProvider.AWS,
                        com.scalecanvas.scenario.domain.DeploymentService.EKS,
                        com.scalecanvas.scenario.domain.GatewayType.ALB,
                        com.scalecanvas.scenario.domain.LoadBalancerType.NLB,
                        4096,
                        2,
                        new ScenarioRequest.ServiceTopologyRequest(
                                8,
                                5,
                                5,
                                List.of(
                                        new ScenarioRequest.ServiceCapacityRequest("catalog", 2400L, 4096, 2, 5, com.scalecanvas.scenario.domain.ServiceBinding.DEDICATED),
                                        new ScenarioRequest.ServiceCapacityRequest("orders", 700L, 2048, 1, 4, com.scalecanvas.scenario.domain.ServiceBinding.DEDICATED),
                                        new ScenarioRequest.ServiceCapacityRequest("payments", 320L, 2048, 1, 3, com.scalecanvas.scenario.domain.ServiceBinding.DEDICATED),
                                        new ScenarioRequest.ServiceCapacityRequest("search", 1100L, 6144, 2, 4, com.scalecanvas.scenario.domain.ServiceBinding.DEDICATED)
                                )
                        )
                )
        );
    }

    private ScenarioRequest buildIotIngestion() {
        return new ScenarioRequest(
                "IoT ingestion",
                "High-volume device ingress with fan-out and cold retention",
                com.scalecanvas.scenario.domain.ProductType.IOT_INGESTION,
                new ScenarioRequest.WorkloadRequest(
                        800000L,
                        720000L,
                        2500L,
                        new BigDecimal("520"),
                        new BigDecimal("3400"),
                        new BigDecimal("6.54"),
                        new BigDecimal("20"),
                        new BigDecimal("80"),
                        256L,
                        65536L,
                        96,
                        new BigDecimal("85")
                ),
                new ScenarioRequest.DataRequest(
                        new BigDecimal("4200"),
                        new BigDecimal("31"),
                        6,
                        new BigDecimal("3800"),
                        new BigDecimal("10"),
                        new BigDecimal("12000000")
                ),
                new ScenarioRequest.QualityRequest(
                        130,
                        420,
                        1100,
                        new BigDecimal("99.9"),
                        120,
                        120,
                        com.scalecanvas.scenario.domain.ConsistencyLevel.EVENTUAL,
                        com.scalecanvas.scenario.domain.GeographicScope.GLOBAL
                ),
                new ScenarioRequest.OrganizationRequest(
                        12,
                        com.scalecanvas.scenario.domain.MaturityLevel.LOW,
                        2,
                        false,
                        com.scalecanvas.scenario.domain.MaturityLevel.LOW,
                        com.scalecanvas.scenario.domain.MaturityLevel.MEDIUM,
                        com.scalecanvas.scenario.domain.BudgetBand.MODERATE
                ),
                new ScenarioRequest.DeploymentRequest(
                        com.scalecanvas.scenario.domain.ServerType.CONTAINER,
                        com.scalecanvas.scenario.domain.CloudProvider.AWS,
                        com.scalecanvas.scenario.domain.DeploymentService.ECS,
                        com.scalecanvas.scenario.domain.GatewayType.NLB,
                        com.scalecanvas.scenario.domain.LoadBalancerType.NLB,
                        2048,
                        1,
                        new ScenarioRequest.ServiceTopologyRequest(
                                6,
                                3,
                                4,
                                List.of(
                                        new ScenarioRequest.ServiceCapacityRequest("ingest", 5800L, 2048, 1, 16, com.scalecanvas.scenario.domain.ServiceBinding.SHARED),
                                        new ScenarioRequest.ServiceCapacityRequest("router", 3400L, 4096, 2, 12, com.scalecanvas.scenario.domain.ServiceBinding.SHARED),
                                        new ScenarioRequest.ServiceCapacityRequest("archive", 1200L, 1024, 1, 8, com.scalecanvas.scenario.domain.ServiceBinding.SHARED)
                                )
                        )
                )
        );
    }

    private ScenarioRequest buildInternalCrud() {
        return new ScenarioRequest(
                "Internal CRUD",
                "Small internal tooling with low traffic and simple operational needs",
                com.scalecanvas.scenario.domain.ProductType.INTERNAL_CRUD,
                new ScenarioRequest.WorkloadRequest(
                        2200L,
                        650L,
                        80L,
                        new BigDecimal("12"),
                        new BigDecimal("55"),
                        new BigDecimal("4.58"),
                        new BigDecimal("75"),
                        new BigDecimal("25"),
                        1024L,
                        512000L,
                        1,
                        new BigDecimal("10")
                ),
                new ScenarioRequest.DataRequest(
                        new BigDecimal("18"),
                        new BigDecimal("3"),
                        24,
                        new BigDecimal("10"),
                        new BigDecimal("80"),
                        new BigDecimal("15000")
                ),
                new ScenarioRequest.QualityRequest(
                        120,
                        320,
                        900,
                        new BigDecimal("99.5"),
                        240,
                        180,
                        com.scalecanvas.scenario.domain.ConsistencyLevel.READ_YOUR_WRITES,
                        com.scalecanvas.scenario.domain.GeographicScope.LOCAL
                ),
                new ScenarioRequest.OrganizationRequest(
                        3,
                        com.scalecanvas.scenario.domain.MaturityLevel.LOW,
                        1,
                        false,
                        com.scalecanvas.scenario.domain.MaturityLevel.LOW,
                        com.scalecanvas.scenario.domain.MaturityLevel.LOW,
                        com.scalecanvas.scenario.domain.BudgetBand.MINIMAL
                ),
                new ScenarioRequest.DeploymentRequest(
                        com.scalecanvas.scenario.domain.ServerType.VM,
                        com.scalecanvas.scenario.domain.CloudProvider.ON_PREM,
                        com.scalecanvas.scenario.domain.DeploymentService.VM_MANUAL,
                        com.scalecanvas.scenario.domain.GatewayType.NGINX,
                        com.scalecanvas.scenario.domain.LoadBalancerType.NLB,
                        1024,
                        1,
                        new ScenarioRequest.ServiceTopologyRequest(
                                2,
                                0,
                                0,
                                List.of(
                                        new ScenarioRequest.ServiceCapacityRequest("admin", 50L, 1024, 1, 1, com.scalecanvas.scenario.domain.ServiceBinding.SHARED),
                                        new ScenarioRequest.ServiceCapacityRequest("reports", 10L, 512, 1, 1, com.scalecanvas.scenario.domain.ServiceBinding.SHARED)
                                )
                        )
                )
        );
    }

    private ScenarioRequest buildStreamingMetadata() {
        return new ScenarioRequest(
                "Streaming metadata",
                "Content metadata service for a streaming platform",
                com.scalecanvas.scenario.domain.ProductType.STREAMING_METADATA,
                new ScenarioRequest.WorkloadRequest(
                        600000L,
                        220000L,
                        9000L,
                        new BigDecimal("260"),
                        new BigDecimal("1900"),
                        new BigDecimal("7.31"),
                        new BigDecimal("90"),
                        new BigDecimal("10"),
                        2048L,
                        2000000L,
                        24,
                        new BigDecimal("30")
                ),
                new ScenarioRequest.DataRequest(
                        new BigDecimal("1100"),
                        new BigDecimal("14"),
                        48,
                        new BigDecimal("600"),
                        new BigDecimal("35"),
                        new BigDecimal("1600000")
                ),
                new ScenarioRequest.QualityRequest(
                        75,
                        210,
                        580,
                        new BigDecimal("99.95"),
                        20,
                        10,
                        com.scalecanvas.scenario.domain.ConsistencyLevel.READ_YOUR_WRITES,
                        com.scalecanvas.scenario.domain.GeographicScope.MULTI_COUNTRY
                ),
                new ScenarioRequest.OrganizationRequest(
                        16,
                        com.scalecanvas.scenario.domain.MaturityLevel.MEDIUM,
                        9,
                        true,
                        com.scalecanvas.scenario.domain.MaturityLevel.MEDIUM,
                        com.scalecanvas.scenario.domain.MaturityLevel.HIGH,
                        com.scalecanvas.scenario.domain.BudgetBand.FLEXIBLE
                ),
                new ScenarioRequest.DeploymentRequest(
                        com.scalecanvas.scenario.domain.ServerType.CONTAINER,
                        com.scalecanvas.scenario.domain.CloudProvider.AWS,
                        com.scalecanvas.scenario.domain.DeploymentService.EKS,
                        com.scalecanvas.scenario.domain.GatewayType.CLOUDFLARE,
                        com.scalecanvas.scenario.domain.LoadBalancerType.NLB,
                        6144,
                        4,
                        new ScenarioRequest.ServiceTopologyRequest(
                                7,
                                4,
                                5,
                                List.of(
                                        new ScenarioRequest.ServiceCapacityRequest("metadata", 2600L, 4096, 2, 6, com.scalecanvas.scenario.domain.ServiceBinding.DEDICATED),
                                        new ScenarioRequest.ServiceCapacityRequest("recommendations", 1800L, 8192, 4, 6, com.scalecanvas.scenario.domain.ServiceBinding.DEDICATED),
                                        new ScenarioRequest.ServiceCapacityRequest("encoding", 500L, 12288, 4, 8, com.scalecanvas.scenario.domain.ServiceBinding.SERVERLESS),
                                        new ScenarioRequest.ServiceCapacityRequest("cdn-origin", 1200L, 2048, 2, 4, com.scalecanvas.scenario.domain.ServiceBinding.SHARED)
                                )
                        )
                )
        );
    }
}
