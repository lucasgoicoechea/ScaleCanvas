# Copilot instructions for ScaleCanvas

Read `AGENTS.md` and `OBSERVABILITY_START_HERE.md`.

For observability work:

- follow `docs/specs/OBS-001-cloud-observability/tasks.md`;
- work on one task only;
- do not add credentials or cloud write permissions;
- keep provider SDK types outside domain/API;
- preserve original metric values and units;
- distinguish planned capacity from observed telemetry;
- treat missing and stale data explicitly;
- use APIs/SDKs for telemetry and MCP only for AI-facing read-only queries;
- do not claim that resource pressure predicts collapse;
- update `progress.md` after verification.
