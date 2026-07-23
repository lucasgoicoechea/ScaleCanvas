CREATE TABLE scenario_version (
    id UUID PRIMARY KEY,
    scenario_id UUID NOT NULL,
    version_label VARCHAR(64) NOT NULL,
    payload_json TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE evaluation (
    id UUID PRIMARY KEY,
    scenario_id UUID NOT NULL,
    scenario_name VARCHAR(160) NOT NULL,
    catalog_version VARCHAR(32) NOT NULL,
    generated_at TIMESTAMP NOT NULL,
    payload_json TEXT NOT NULL
);

CREATE TABLE evaluation_result (
    id UUID PRIMARY KEY,
    evaluation_id UUID NOT NULL,
    variant VARCHAR(32) NOT NULL,
    payload_json TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_scenario_version_scenario_id ON scenario_version(scenario_id);
CREATE INDEX idx_evaluation_scenario_id ON evaluation(scenario_id);
CREATE INDEX idx_evaluation_result_evaluation_id ON evaluation_result(evaluation_id);
