# Deployment

## Verificación 2026-07-23

- El JAR inicia con Java 25, H2 y Flyway.
- `/actuator/health` responde `UP`.
- El build frontend de producción finaliza correctamente.
- Docker Compose está definido, pero no fue ejecutado durante esta verificación porque el
  host no tenía Docker instalado.

## Configuración operativa

- `MUTATION_API_KEY`: protección opcional para endpoints de escritura.
- `APP_CORS_ALLOWED_ORIGINS`: allowlist CORS.
- `SPRING_DATASOURCE_*`: conexión PostgreSQL.
- `AI_ENABLED`: habilita explicaciones opcionales.
- El shutdown del backend es graceful.

## Local

PostgreSQL con Docker Compose, backend Spring Boot y frontend Vite.

```powershell
docker compose up -d postgres
mvn -pl backend spring-boot:run
npm --prefix frontend run dev
```

## Portfolio

Frontend estático en Vercel/Cloudflare Pages; backend como container; PostgreSQL administrado o demo completa con Docker Compose. La app debe funcionar sin IA ni cuentas cloud.

<!-- OBSERVABILITY-DEPLOYMENT:START -->
## Deployment con conectores

La aplicación debe arrancar con todos los connectors deshabilitados.

Configuración conceptual:

```text
OBSERVABILITY_ENABLED=false
OBSERVABILITY_POLLING_ENABLED=false
OBSERVABILITY_MIN_POLL_SECONDS=30
OBSERVABILITY_MAX_RESOURCES=500
OBSERVABILITY_HISTORY_RETENTION_DAYS=7
```

Credenciales AWS/GCP se resuelven por el entorno, no por variables volcadas en UI.

Para portfolio:

- usar provider simulado;
- no publicar credenciales;
- no habilitar polling cloud público;
- limitar rangos y recursos.
<!-- OBSERVABILITY-DEPLOYMENT:END -->
