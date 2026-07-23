# Changelog — especificación de observabilidad

## Alcance de esta entrega

Esta entrega modifica y agrega únicamente archivos Markdown. No se modificó:

- código Java;
- código TypeScript/React;
- `pom.xml`;
- `package.json`;
- archivos YAML;
- Docker;
- migraciones;
- tests;
- configuraciones de runtime;
- secretos.

## Hallazgos documentados

- La matriz llamada `ScalingMatrix3D` es actualmente un heatmap 2D.
- El endpoint backend de scaling matrix está sin implementar.
- Los cálculos actuales son de planificación, no telemetría observada.
- No existe modelo provider-neutral ni conectores cloud.
- No existen alertas de observabilidad.
- Hay una diferencia Java 21/Java 25 entre documentación y POM.

## Archivos nuevos principales

- `OBSERVABILITY_START_HERE.md`
- `docs/specs/OBS-001-cloud-observability/*`
- `docs/adr/0005-provider-neutral-observability.md`
- `docs/adr/0006-react-three-fiber-3d.md`
- `docs/adr/0007-api-not-mcp-data-plane.md`
- `docs/adr/0008-cloud-credentials-by-reference.md`
- `docs/adr/0009-planned-vs-observed-capacity.md`
- `prompts/10-observability-audit.md` a `17-observability-final-review.md`
- `.continue/prompts/10-observability-audit.md` a `17-observability-final-review.md`
- `.cline/rules/03-observability.md`
- `.continue/rules/03-observability.md`
- `.github/copilot-instructions.md`

## Documentos existentes actualizados

- README, AGENTS, MASTER_PROMPT y CLINE_KILO_SETUP.
- Product spec, requisitos, calidad, dominio, capacidad, API, datos, UX, seguridad,
  testing, observabilidad, deployment, backlog, criterios, glosario y estado real.
