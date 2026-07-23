# Seguridad y credenciales

## Principios

- least privilege;
- read-only por defecto;
- secretos fuera de payloads;
- no credenciales en navegador;
- no credenciales en logs;
- no credenciales en Markdown;
- no credenciales en base de datos salvo referencia segura;
- conexión deshabilitada hasta validación explícita.

## ProviderConnection

Puede almacenar:

- provider type;
- account/project;
- region;
- role ARN;
- profile name;
- service account email;
- secret reference;
- scopes;
- poll interval.

No puede almacenar:

- AWS secret access key;
- token OAuth;
- refresh token;
- private key;
- JSON completo de service account;
- session token.

## AWS

Preferencias:

1. instance/task role;
2. web identity;
3. SSO/profile para local;
4. AssumeRole;
5. secret manager externo solo cuando no exista alternativa.

Permisos iniciales:

- identidad STS;
- describe/list de recursos;
- get/list de métricas;
- describe alarms.

Sin permisos de mutation.

## GCP

Preferencias:

1. Workload Identity;
2. Application Default Credentials;
3. impersonation;
4. credencial referenciada.

Roles iniciales de lectura y monitoreo. Separar permisos de alert policy mutation.

## Backend

- validar provider/region/project;
- allowlist de endpoints cloud;
- proteger contra SSRF;
- timeouts;
- límites de cardinalidad;
- límites de período;
- paginación;
- sanitizar labels;
- no devolver excepciones SDK completas;
- auditar test/sync/config changes.

## MCP

- tools read-only;
- autenticación;
- autorización por tool;
- no exponer secretos en resources;
- no permitir URL arbitraria;
- no ejecutar comandos;
- registrar invocaciones;
- mantener aprobación humana para acciones futuras.
