package com.scalecanvas.observability.domain;

public enum ResourceLifecycleState {
    HEALTHY,
    WARNING,
    CRITICAL,
    UNKNOWN,
    STALE,
    NO_CAPACITY,
    DISABLED
}
