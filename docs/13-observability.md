# Observability

- Logs estructurados y correlation ID.
- Actuator health.
- Micrometer/OpenTelemetry ready.
- Métricas: duración de evaluación, reglas evaluadas/activadas, datos insuficientes y generación de informe.
- Frontend con error boundary y sin datos sensibles en logs.

<!-- OBSERVABILITY-DUAL:START -->
## Dos significados de observabilidad

### Self-observability

Métricas, logs y health de ScaleCanvas.

### Observed systems

Métricas y alarmas de sistemas conectados.

Mantener namespaces, modelos y dashboards separados. ScaleCanvas no debe mezclar su propia
CPU/memoria con la infraestructura que está analizando.
<!-- OBSERVABILITY-DUAL:END -->
