# UI and UX

## Pantallas

- Home con ejemplos.
- Wizard: producto, demanda, datos, calidad, organización, infraestructura y revisión.
- Workspace con inputs, diagrama y recomendaciones.
- Comparador baseline/x2/x10.
- Rule Explorer.
- Infrastructure editor:
  - Tipo de servidor/server.
  - Plataforma cloud.
  - Servicio de despliegue.
  - Gateway y balanceador.
  - Capacidad de la unidad mínima (memoria/CPU).
  - Topología de servicios: total, microservicios, escalables.
  - Requests por minuto por servicio/server.
  - Binding por servicio (shared/dedicated/serverless).

## Flujo principal implementado

1. Definir o cargar un escenario.
2. Guardarlo si se necesita historial asociado.
3. Evaluar Baseline, x2 y x10.
4. Revisar métricas, riesgos y recomendaciones.
5. Abrir la evidencia de cada regla: versión, threshold, valores observados, beneficios,
   trade-offs y alternativa más simple.
6. Exportar informe Markdown o generar ADR.

La navegación lateral usa cinco pasos, pero `ScenarioForm` aún renderiza grupos compartidos
en varios pasos. Separar el formulario por secciones es una mejora UX pendiente.

## Visualizaciones

- Grafo de arquitectura.
- Heatmap de riesgos.
- Radar de atributos.
- Proyección de capacidad.
- Timeline de evolución.
- Distancia a thresholds.
- Matriz costo/complejidad.
- Board Do now / Watch / Not yet.
- Scaling equalizer y matrix actual:
  - Ejes conceptuales: servicios, servers y capacidad.
  - Implementación actual: heatmap 2D ECharts con servicios en X, servidores en Y y utilización en color; no es todavía una escena 3D.
  - Muestra cuándo agregar un server o un servicio.
  - Indica cuellos de botella por instancia y por servicio.
  - Permite comparar variantes baseline/x2/x10.

## Accesibilidad

Unidades visibles, teclado, labels, contraste y color no exclusivo.

<!-- OBSERVABILITY-UI:START -->
## Target de escena 3D OBS

- Marco exterior por recurso: capacidad.
- Fill interior: carga.
- CPU/GPU/memory/storage separados.
- Capacidad libre azul/cian translúcida.
- Healthy verde, warning amarillo, critical rojo.
- Unknown/stale/no-capacity con estados propios.
- Cotas 0/25/50/75/100 y líneas de threshold.
- Label principal debajo.
- Servicios hijos dentro o mediante drill-down.
- Panel de detalle con valores absolutos y procedencia.
- Filtros y búsqueda.
- Fallback 2D y tabla accesible.
- ECharts se mantiene para histórico y heatmap.
<!-- OBSERVABILITY-UI:END -->
