package com.scalecanvas.scenario.domain;

import java.util.List;

public record ServiceTopology(
        int totalServices,
        int microservicesCount,
        int scalableServicesCount,
        List<ServiceCapacity> services) {
}
