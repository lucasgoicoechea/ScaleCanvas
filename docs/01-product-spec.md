# Product Specification

## Problema

Las decisiones de arquitectura suelen convertirse en recetas sin requisitos ni métricas: “usar microservicios”, “agregar Kafka” o “migrar a Kubernetes”. ScaleCanvas obliga a justificar cada cambio.

## Propuesta

Recibe un escenario cuantitativo y cualitativo, calcula capacidad aproximada, evalúa reglas versionadas y genera:

- Arquitectura recomendada hoy.
- Riesgos y cuellos de botella.
- Distancia al próximo threshold.
- Alternativas más simples.
- Escenarios de crecimiento o falla.
- Diagrama editable.
- Informe técnico y ejecutivo.
- ADRs sugeridos.
- Perfil de despliegue: servidor, plataforma, gateway, balanceador, unidad mínima y topología de servicios.
- Visualización de escalado tipo equalizer: matriz cúbica que muestra cuándo agregar un server o un servicio.

## Usuarios

Backend engineers, architects, tech leads, SREs y estudiantes avanzados.

## MVP

1. Crear escenario manual.
2. Definir demanda y atributos de calidad.
3. Calcular métricas derivadas.
4. Evaluar catálogo inicial de reglas.
5. Mostrar recomendaciones justificadas.
6. Comparar actual, x2 y x10.
7. Renderizar diagrama.
8. Exportar informe Markdown.
9. Guardar escenarios.
10. Definir perfil de despliegue y topología de servicios.
11. Visualizar scaling equalizer.

## Fuera del MVP

- Aprovisionar infraestructura.
- Modificar repositorios.
- Leer métricas productivas automáticamente.
- Estimar facturas cloud exactas.
- Generar IaC de producción.

<!-- OBSERVABILITY-PRODUCT:START -->
## Evolución OBS-001

El MVP original sigue funcionando sin cuentas cloud. La siguiente evolución incorpora:

- comparación entre capacidad planificada y telemetría observada;
- visualización 3D de recursos;
- alertas locales;
- lectura AWS/GCP;
- jerarquía host-servicio;
- MCP read-only para consulta asistida.

La lectura automática de métricas deja de ser “fuera del producto”, pero continúa fuera del
MVP original hasta completar las fases OBS.
<!-- OBSERVABILITY-PRODUCT:END -->
