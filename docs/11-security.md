# Security

## Estado implementado

- La aplicación no guarda credenciales cloud; las conexiones usan referencias.
- Los conectores de observabilidad están definidos como read-only.
- CORS usa una allowlist configurable.
- Las mutaciones pueden protegerse definiendo `MUTATION_API_KEY`.
- Con API key habilitada, `POST`, `PUT` y `DELETE` bajo `/api/**` requieren el header
  `X-ScaleCanvas-Key`.
- La comparación de la key usa comparación constante.
- Los detalles de health no se exponen de forma incondicional.
- Los logs productivos usan nivel `INFO` por defecto.

La API key está deshabilitada por defecto para desarrollo local. No reemplaza autenticación
por usuario, autorización por roles ni auditoría; esas capacidades continúan fuera del MVP.

- Validación estricta de JSON.
- Límites de tamaño.
- CORS configurable.
- No ejecutar scripts de usuario.
- Sanitizar Markdown.
- No exponer trazas internas.
- Secrets en variables de entorno.
- IA deshabilitada por defecto.
- Integraciones futuras con scopes read-only y tokens cifrados.

<!-- OBSERVABILITY-SECURITY:START -->
## Seguridad cloud

- Backend exclusivamente para SDK/API cloud.
- Read-only por defecto.
- Credenciales por default chain, profile, ADC, identity o referencia.
- Nunca devolver secretos al frontend.
- No guardar access keys o service-account JSON.
- Allowlist de providers/endpoints.
- Protección SSRF.
- Timeouts, cuotas y paginación.
- Auditoría de test/sync/config.
- Escritura de alarmas e infraestructura deshabilitada.
- MCP read-only, autenticado y autorizado.
<!-- OBSERVABILITY-SECURITY:END -->
