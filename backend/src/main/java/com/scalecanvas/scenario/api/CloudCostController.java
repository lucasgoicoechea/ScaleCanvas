package com.scalecanvas.scenario.api;

import com.scalecanvas.scenario.api.dto.CloudPriceItem;
import com.scalecanvas.scenario.api.dto.ScenarioRequest;
import com.scalecanvas.scenario.application.CloudCostService;
import com.scalecanvas.scenario.application.ScenarioService;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/scenarios")
public class CloudCostController {

    private final CloudCostService cloudCostService;
    private final ScenarioService scenarioService;

    public CloudCostController(CloudCostService cloudCostService, ScenarioService scenarioService) {
        this.cloudCostService = cloudCostService;
        this.scenarioService = scenarioService;
    }

    @GetMapping("/{id}/cloud-cost")
    public List<CloudPriceItem> estimate(@PathVariable UUID id) {
        ScenarioRequest request = scenarioService.get(id);
        return cloudCostService.estimate(request);
    }
}
