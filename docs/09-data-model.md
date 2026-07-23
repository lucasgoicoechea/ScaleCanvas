# Data Model

## Tablas

- `architecture_scenario`: id, nombre, product_type, payload JSONB, catalog_version, timestamps y versión optimista.
- `scenario_version`: snapshot por versión.
  - id, scenario_id, version_label, payload_json, created_at.
- `evaluation`: escenario, versión y catálogo.
  - id, scenario_id, scenario_name, catalog_version, generated_at, payload_json.
- `evaluation_result`: variante, métricas, findings, recomendaciones y riesgos JSONB.
  - id, evaluation_id, variant, payload_json, created_at.
- `rule_catalog`: versión, checksum, fecha y definición.

## Decisión

El MVP almacena payload de escenario como JSONB para iterar rápido, manteniendo dominio tipado. Normalizar campos cuando aparezcan consultas estables.

## Historial

- Cada evaluación ejecutada se persiste en `evaluation` con su response completo.
- Cada variante de evaluación se persiste en `evaluation_result` para auditoría.
- Cada versión de escenario se persiste en `scenario_version` con label y timestamp.
- Índices en `scenario_id`, `evaluation_id` para consultas por escenario.

<!-- OBSERVABILITY-DATA:START -->
## Tablas propuestas OBS

- `provider_connection`: configuración sin secreto.
- `observed_resource`: inventario canónico.
- `resource_relation`: jerarquía y procedencia.
- `metric_sample`: samples seleccionados con retención.
- `observability_snapshot`: snapshot resumido.
- `alert_rule`: reglas locales.
- `alert_instance`: lifecycle de alertas.
- `external_alarm`: estado importado.

No almacenar tokens, private keys, access keys ni JSON de service account.

No crear todas estas tablas en una sola migración. Cada fase debe justificar consultas,
índices y retención.
<!-- OBSERVABILITY-DATA:END -->
