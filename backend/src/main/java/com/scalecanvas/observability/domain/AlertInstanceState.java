package com.scalecanvas.observability.domain;

public enum AlertInstanceState {
    PENDING,
    FIRING_WARNING,
    FIRING_CRITICAL,
    ACKNOWLEDGED,
    RESOLVED,
    SUPPRESSED,
    UNKNOWN
}
