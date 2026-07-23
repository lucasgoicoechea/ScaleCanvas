package com.scalecanvas.scenario.domain;

public record DeploymentProfile(
        ServerType serverType,
        CloudProvider cloudProvider,
        DeploymentService deploymentService,
        GatewayType gatewayType,
        LoadBalancerType loadBalancerType,
        int minimumUnitMemoryMb,
        int minimumUnitCpuCount,
        ServiceTopology serviceTopology) {
}
