package com.scalecanvas.scenario.api;

import java.math.BigDecimal;
import java.util.List;

public record ScalingMatrixResponse(
        String serverType,
        String cloudProvider,
        int minimumUnitMemoryMb,
        int minimumUnitCpuCount,
        List<String> services,
        int maxReplicas,
        List<MatrixCell> matrix) {
}
