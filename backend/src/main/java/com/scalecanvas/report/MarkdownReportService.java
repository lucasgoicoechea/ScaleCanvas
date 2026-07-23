package com.scalecanvas.report;

import com.scalecanvas.evaluation.api.EvaluationResponse;
import com.scalecanvas.rules.RuleOutcome;
import org.springframework.stereotype.Service;

@Service
public class MarkdownReportService {
    public String generate(EvaluationResponse evaluation) {
        StringBuilder markdown = new StringBuilder();
        markdown.append("# ScaleCanvas Evaluation — ").append(evaluation.scenarioName()).append("\n\n");
        markdown.append("- Evaluation: `").append(evaluation.evaluationId()).append("`\n");
        markdown.append("- Catalog: `").append(evaluation.catalogVersion()).append("`\n");
        markdown.append("- Generated: ").append(evaluation.generatedAt()).append("\n\n");

        evaluation.results().forEach(result -> {
            markdown.append("## ").append(result.variant()).append("\n\n");
            markdown.append("### Derived metrics\n\n");
            markdown.append("| Metric | Value |\n|---|---:|\n");
            markdown.append("| Daily requests | ").append(result.derivedMetrics().dailyRequests()).append(" |\n");
            markdown.append("| Peak-hour requests | ").append(result.derivedMetrics().peakHourRequests()).append(" |\n");
            markdown.append("| Daily transfer (GB) | ").append(result.derivedMetrics().dailyTransferGb()).append(" |\n");
            markdown.append("| Read RPS | ").append(result.derivedMetrics().readRps()).append(" |\n");
            markdown.append("| Write RPS | ").append(result.derivedMetrics().writeRps()).append(" |\n");
            markdown.append("| Storage after 12 months (GB) | ").append(result.derivedMetrics().storageAfter12MonthsGb()).append(" |\n");
            markdown.append("| Allowed downtime/month (minutes) | ")
                    .append(result.derivedMetrics().allowedUnavailabilityMinutesPerMonth()).append(" |\n\n");

            markdown.append("### Recommendations\n\n");
            for (RuleOutcome outcome : result.recommendations()) {
                markdown.append("#### ").append(outcome.title()).append("\n\n");
                markdown.append("- Urgency: **").append(outcome.urgency()).append("**\n");
                markdown.append("- Action: **").append(outcome.action()).append("**\n");
                markdown.append("- Rule: `").append(outcome.ruleId()).append("`\n");
                markdown.append("- Why: ").append(outcome.rationale()).append("\n");
                markdown.append("- Threshold: ").append(outcome.threshold()).append("\n");
                markdown.append("- Simpler alternative: ").append(outcome.simplerAlternative()).append("\n\n");
            }

            if (!result.risks().isEmpty()) {
                markdown.append("### Risks\n\n");
                result.risks().forEach(risk -> markdown.append("- **")
                        .append(risk.level()).append(" — ").append(risk.title())
                        .append(":** ").append(risk.detail()).append("\n"));
                markdown.append("\n");
            }
        });
        return markdown.toString();
    }
}
