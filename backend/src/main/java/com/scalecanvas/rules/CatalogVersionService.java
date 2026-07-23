package com.scalecanvas.rules;

import com.scalecanvas.rules.api.dto.CatalogVersionSummary;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.stereotype.Service;

@Service
public class CatalogVersionService {
    private final List<CatalogVersionEntry> versions = new CopyOnWriteArrayList<>();
    private volatile CatalogVersionEntry active;

    public CatalogVersionService() {
        register("builtin", "1.0.0", "Built-in catalog", "classpath:rules/builtin", true);
    }

    public synchronized void register(String id, String version, String name, String source, boolean active) {
        CatalogVersionEntry entry = new CatalogVersionEntry(id, version, name, source, Instant.now(), active);
        this.versions.add(entry);
        if (active) {
            this.active = entry;
        }
    }

    public List<CatalogVersionSummary> listVersions() {
        return versions.stream()
                .sorted(Comparator.comparing(CatalogVersionEntry::createdAt).reversed())
                .map(entry -> new CatalogVersionSummary(
                        entry.id(),
                        entry.version(),
                        entry.name(),
                        entry.source(),
                        entry.createdAt(),
                        active != null && active.id().equals(entry.id())))
                .toList();
    }

    public CatalogVersionSummary activeVersion() {
        if (active == null && !versions.isEmpty()) {
            active = versions.get(0);
        }
        if (active == null) {
            return null;
        }
        return new CatalogVersionSummary(
                active.id(),
                active.version(),
                active.name(),
                active.source(),
                active.createdAt(),
                true);
    }

    public CatalogVersionSummary activate(String id) {
        CatalogVersionEntry found = versions.stream()
                .filter(entry -> entry.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Catalog version not found: " + id));
        this.active = found;
        return activeVersion();
    }

    private record CatalogVersionEntry(String id, String version, String name, String source, Instant createdAt, boolean active) {
    }
}
