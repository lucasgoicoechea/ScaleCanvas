# Functional Requirements

## Escenarios

- Crear, editar, duplicar y versionar.
- Comparar variantes.
- Importar/exportar JSON.

## Entradas

- Tipo de producto.
- Usuarios y concurrencia.
- RPS promedio, pico y burst.
- Lecturas/escrituras.
- Payload.
- Datos actuales, crecimiento y retención.
- Latencia p50/p95/p99.
- SLO, RTO y RPO.
- Consistencia y alcance geográfico.
- Presupuesto, equipo y madurez operativa.
- Batch, archivos y asincronía.
- Infraestructura y despliegue:
  - Tipo de servidor/server (VM, container, serverless, bare metal).
  - Plataforma cloud (AWS, GCP, Azure, on-prem, híbrida).
  - Servicio de despliegue (ECS, EKS, Lambda, App Service, VM manual, Cloud Run, etc.).
  - Gateway y balanceador (API Gateway, ALB, NLB, Cloudflare, Kong, Nginx, etc.).
  - Capacidad de la unidad mínima (memoria y CPU).
  - Cantidad de servicios totales y microservicios.
  - Cantidad de servicios escalables.
  - Requests por minuto por servicio/server.
- Comparación de capacidad actual vs requerida.

## Evaluación

- Validar unidades.
- Calcular throughput y crecimiento.
- Evaluar reglas.
- Ordenar recomendaciones.
- Mostrar evidencia e incertidumbre.
- Detectar datos faltantes.

## Resultados

- Arquitectura actual.
- Cuellos de botella.
- Heatmap de riesgos.
- Radar de calidad.
- Evolución.
- “No hacer todavía”.
- Informe Markdown y ADR.

## IA opcional

Explicar y redactar sobre resultados existentes. La app funciona sin IA.

<!-- OBSERVABILITY-FR:START -->
## Requisitos funcionales OBS

- FR-OBS-001 Configurar múltiples conexiones provider-neutral.
- FR-OBS-002 Probar una conexión sin persistir secretos.
- FR-OBS-003 Descubrir recursos por account/project/region.
- FR-OBS-004 Consultar métricas en ventanas configurables.
- FR-OBS-005 Conservar unidad, valor original, valor canónico, timestamp y source.
- FR-OBS-006 Mostrar actual, máximo y ratio cuando exista máximo.
- FR-OBS-007 Mostrar UNKNOWN, STALE y NO_CAPACITY.
- FR-OBS-008 Visualizar CPU, GPU, memoria y storage independientemente.
- FR-OBS-009 Mostrar capacidad libre y carga.
- FR-OBS-010 Mostrar jerarquía demostrable.
- FR-OBS-011 Definir alertas locales con ventana e histéresis.
- FR-OBS-012 Importar alarmas cloud read-only.
- FR-OBS-013 Refrescar manualmente y mediante polling configurable.
- FR-OBS-014 Filtrar por provider, región, estado y tipo.
- FR-OBS-015 Consultar detalle e historial limitado.
- FR-OBS-016 Ejecutar sin cloud mediante provider simulado.
- FR-OBS-017 Exponer snapshots a MCP opcional read-only.
- FR-OBS-018 Mantener separados planned y observed.
<!-- OBSERVABILITY-FR:END -->
