package com.scalecanvas.rules;

import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class RuleCatalog {
    private final List<ArchitectureRule> rules;

    public RuleCatalog(List<ArchitectureRule> rules) {
        this.rules = rules.stream()
                .sorted(Comparator.comparing(ArchitectureRule::id))
                .toList();
    }

    public List<ArchitectureRule> rules() {
        return rules;
    }
}
