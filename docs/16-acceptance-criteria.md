# Acceptance Criteria

- Escenarios validan unidades, porcentajes y defaults.
- Fórmulas están documentadas y testeadas.
- Motor determinista y versionado.
- Cada recomendación muestra evidencia.
- Se soporta insufficient-data.
- UI compara baseline/x2/x10 y exporta Markdown.
- Aplicación usable con IA apagada.
- Ninguna explicación altera resultados.

<!-- OBSERVABILITY-AC:START -->
## Acceptance OBS

- Cada métrica muestra unidad y timestamp.
- Ratio solo con máximo válido.
- CPU y GPU independientes.
- Missing = UNKNOWN.
- Viejo = STALE.
- Capacidad desconocida = NO_CAPACITY.
- Warning/critical con histéresis.
- Estado global explica razones.
- Servicios internos solo con mapping.
- Provider simulado sin credenciales.
- AWS/GCP read-only.
- Ningún secreto serializado.
- Escena real 3D y fallback.
- 250 unidades con degradación controlada.
- MCP read-only sobre snapshots normalizados.
<!-- OBSERVABILITY-AC:END -->
