# Convenciones

## Backend

- Java 25, Spring Boot 3 y Maven.
- Organización por feature y dominio.
- Records para DTOs inmutables cuando corresponda.
- `BigDecimal` para costos y porcentajes financieros.
- `Duration` para tiempos.
- Sin lógica de dominio en controllers.
- Sin LLM dentro del motor de reglas.
- Tests unitarios para fórmulas y reglas.

## Frontend

- React y TypeScript estricto.
- React Flow para diagramas.
- ECharts para gráficos.
- Zod en límites.
- Estado local por defecto.
- No duplicar cálculos del backend.

## Git

- Un objetivo por commit.
- No modificar `main` directamente.
- Ramas `feature/`, `docs/` o `fix/`.
- Revisar `git diff` antes de commit.

## Documentación

- Markdown y Mermaid.
- IDs técnicos en inglés.
- No prometer costos cloud exactos sin catálogo versionado.
