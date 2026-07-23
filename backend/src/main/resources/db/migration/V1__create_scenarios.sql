CREATE TABLE architecture_scenario (
    id UUID PRIMARY KEY,
    name VARCHAR(160) NOT NULL,
    description TEXT,
    payload_json TEXT NOT NULL,
    rule_catalog_version VARCHAR(32) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_architecture_scenario_updated_at
    ON architecture_scenario(updated_at DESC);
