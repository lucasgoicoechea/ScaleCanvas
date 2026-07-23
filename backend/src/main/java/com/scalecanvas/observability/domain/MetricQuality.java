package com.scalecanvas.observability.domain;

public enum MetricQuality {
    MEASURED,
    DERIVED,
    DECLARED,
    ESTIMATED,
    MISSING,
    STALE
}
