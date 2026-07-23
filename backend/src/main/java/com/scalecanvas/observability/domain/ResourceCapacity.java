package com.scalecanvas.observability.domain;

import java.time.Instant;
import java.util.Optional;

public record ResourceCapacity(
        Double cpuCapacityCores,
        Integer gpuCapacityCount,
        Long gpuMemoryBytes,
        Long memoryCapacityBytes,
        Long storageCapacityBytes,
        Integer iopsLimit,
        Long throughputBytesPerSecondLimit,
        String source,
        Instant timestamp) {

    public ResourceCapacity {
        cpuCapacityCores = Optional.ofNullable(cpuCapacityCores).map(Double::doubleValue).orElse(null);
    }
}
