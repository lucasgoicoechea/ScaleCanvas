package com.scalecanvas.rules;

import com.scalecanvas.rules.api.dto.CatalogVersionSummary;
import com.scalecanvas.rules.api.dto.RuleCatalogResponse;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/rule-catalog")
public class RuleCatalogController {
    private final RuleCatalog catalog;
    private final CatalogVersionService versionService;

    public RuleCatalogController(RuleCatalog catalog, CatalogVersionService versionService) {
        this.catalog = catalog;
        this.versionService = versionService;
    }

    @GetMapping
    List<RuleCatalogResponse> list() {
        return catalog.rules().stream()
                .map(rule -> new RuleCatalogResponse(
                        rule.id(),
                        rule.getClass().getSimpleName(),
                        "general",
                        "",
                        "ACTIVE"))
                .toList();
    }

    @GetMapping("/versions")
    List<CatalogVersionSummary> versions() {
        return versionService.listVersions();
    }

    @PostMapping("/versions/{id}/activate")
    CatalogVersionSummary activate(@PathVariable String id) {
        return versionService.activate(id);
    }

    @GetMapping("/versions/active")
    CatalogVersionSummary active() {
        return versionService.activeVersion();
    }
}
