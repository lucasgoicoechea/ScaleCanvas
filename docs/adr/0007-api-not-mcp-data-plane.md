# ADR 0007 — API/SDK como data plane; MCP como query plane

## Estado

Propuesto.

## Contexto

MCP conecta aplicaciones de IA con tools/resources. La ingestión periódica de métricas
requiere control de batching, cuotas, cache, timeouts, retries y frescura.

## Decisión

Consultar AWS/GCP/Prometheus mediante APIs/SDK desde adapters backend. MCP se agrega después
para consultar snapshots ya normalizados.

## Consecuencias

- ingestión predecible;
- menor dependencia de disponibilidad de MCP servers externos;
- MCP no reemplaza observability provider;
- la IA obtiene contexto sin recibir credenciales.
