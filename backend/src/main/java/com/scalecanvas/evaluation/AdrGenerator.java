package com.scalecanvas.evaluation;

import com.scalecanvas.evaluation.api.EvaluationResponse;
import com.scalecanvas.evaluation.api.RiskFinding;
import com.scalecanvas.evaluation.api.VariantResult;
import com.scalecanvas.evaluation.api.dto.AdrResponse;
import com.scalecanvas.rules.RuleOutcome;
import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class AdrGenerator {

    public AdrResponse generate(EvaluationResponse evaluation) {
        String id = UUID.randomUUID().toString();
        String title = evaluation.scenarioName() + " architecture decision";
        String context = buildContext(evaluation);
        String options = buildOptions(evaluation);
        String decision = buildDecision(evaluation);
        String consequences = buildConsequences(evaluation);
        return new AdrResponse(
                id,
                title,
                context,
                options,
                decision,
                consequences,
                evaluation.evaluationId().toString(),
                Instant.now());
    }

    private String buildContext(EvaluationResponse evaluation) {
        StringBuilder sb = new StringBuilder();
        sb.append("Scenario: ").append(evaluation.scenarioName()).append("\n");
        sb.append("Catalog version: ").append(evaluation.catalogVersion()).append("\n");
        sb.append("Evaluated variants: ").append(evaluation.results().size()).append("\n");
        sb.append("Top risks: ");
        int count = 0;
        for (VariantResult result : evaluation.results()) {
            for (RiskFinding risk : result.risks()) {
                if (count++ < 3) {
                    sb.append(risk.title()).append(" (").append(risk.level()).append("), ");
                }
            }
        }
        return sb.toString();
    }

    private String buildOptions(EvaluationResponse evaluation) {
        StringBuilder sb = new StringBuilder();
        for (VariantResult result : evaluation.results()) {
            sb.append(result.variant().name())
                    .append(": ")
                    .append(result.recommendations().size())
                    .append(" recommendations, ")
                    .append(result.risks().size())
                    .append(" risks\n");
        }
        return sb.toString();
    }

    private String buildDecision(EvaluationResponse evaluation) {
        VariantResult best = evaluation.results().stream()
                .min((a, b) -> Integer.compare(a.recommendations().size(), b.recommendations().size()))
                .orElse(null);
        if (best == null) {
            return "No viable variant found.";
        }
        return "Proceed with " + best.variant().name() + " based on lowest recommendation count and risk profile.";
    }

    private String buildConsequences(EvaluationResponse evaluation) {
        StringBuilder sb = new StringBuilder();
        for (VariantResult result : evaluation.results()) {
            sb.append(result.variant().name()).append(": ");
            for (RuleOutcome recommendation : result.recommendations()) {
                sb.append("- ").append(recommendation.action()).append("\n");
            }
            for (RiskFinding risk : result.risks()) {
                sb.append("! ").append(risk.title()).append(": ").append(risk.detail()).append("\n");
            }
        }
        return sb.toString();
    }
}
