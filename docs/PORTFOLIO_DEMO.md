# Portfolio demo

## Objetivo

Mostrar criterio de arquitectura, capacidad de modelado y calidad de implementación. La
demo no intenta presentar ScaleCanvas como un SaaS productivo.

## Preparación

1. Ejecutar `.\scripts\test-all.ps1`.
2. Ejecutar `.\scripts\run-local.ps1`.
3. Cargar el escenario SaaS B2B incluido.
4. Mantener IA deshabilitada para demostrar que el resultado es determinista.
5. Cerrar aplicaciones o notificaciones que puedan aparecer en la grabación.

## Guion de video — 3 minutos

### 0:00–0:20 — Problema

“Recomendaciones como microservicios, Kafka o Kubernetes suelen aparecer sin métricas.
Construí ScaleCanvas para justificar decisiones arquitectónicas con escenarios,
thresholds y reglas reproducibles.”

### 0:20–0:50 — Escenario

- Mostrar demanda, crecimiento, disponibilidad y madurez operativa.
- Explicar que cada entrada tiene unidad.
- Ejecutar la evaluación.

### 0:50–1:35 — Resultado

- Cambiar entre Baseline, x2 y x10.
- Abrir una recomendación.
- Señalar ID y versión de regla, threshold, evidencia, beneficios, trade-offs y alternativa
  simple.
- Mostrar un caso `Not yet` para explicar cómo se evita sobrearquitectura.

### 1:35–2:10 — Visualización

- Diagrama de arquitectura.
- Capacity chart o scaling equalizer.
- Vista 3D de observabilidad simulada.
- Aclarar que los conectores cloud son contratos read-only todavía no implementados.

### 2:10–2:40 — Ingeniería

- Monolito modular Spring Boot.
- React y TypeScript.
- PostgreSQL/H2 y Flyway.
- Motor determinista; IA opcional sin autoridad.
- Tests y GitHub Actions.

### 2:40–3:00 — Cierre

“ScaleCanvas no intenta adivinar la arquitectura correcta. Hace visibles los supuestos y
permite discutir decisiones con evidencia.”

## Capturas sugeridas

1. Wizard con escenario completo.
2. Board `Do now / Watch / Not yet`.
3. Detalle de evidencia de una regla.
4. Comparación Baseline/x2/x10.
5. Diagrama o escena 3D.
6. Badge de CI verde cuando el repositorio esté en GitHub.

## Borrador de post para LinkedIn

¿Cuándo necesita realmente un sistema Kubernetes, Kafka o sharding?

Construí ScaleCanvas para explorar esa pregunta con escenarios cuantitativos y reglas
deterministas. La aplicación modela demanda, crecimiento, datos, disponibilidad y madurez
operativa; después compara Baseline, x2 y x10.

Cada recomendación muestra su threshold, evidencia, trade-offs y una alternativa más
simple. También puede decir “todavía no” cuando una tecnología agrega más complejidad que
valor.

Stack: Java 25, Spring Boot, React, TypeScript, PostgreSQL/H2, Flyway, React Flow, ECharts y
Three.js.

La IA es opcional y sólo explica resultados: no decide la arquitectura.

[VIDEO]
[REPOSITORY]

Me interesa especialmente recibir feedback sobre el modelo de reglas y la forma de
comunicar decisiones arquitectónicas.

## Antes de publicar

- Reemplazar `[VIDEO]` y `[REPOSITORY]`.
- Confirmar que el repositorio no contiene secretos.
- Ejecutar la demo desde un clon limpio.
- Revisar el texto y subtítulos del video.
- Añadir una imagen social al README si se dispone de una captura final.
