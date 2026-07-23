# Quality Attributes

- Determinismo: misma entrada y catálogo producen la misma salida.
- Explicabilidad: regla, evidencia, threshold, beneficio, costo y alternativa.
- Mantenibilidad: reglas pequeñas, fórmulas puras, contratos y ADRs.
- Rendimiento: evaluación menor a 500 ms sin IA en el MVP.
- Seguridad: validación, límites y sin ejecución de código externo.
- Portabilidad: backend OCI, frontend estático/container, PostgreSQL estándar.
- Usabilidad: unidades visibles, defaults marcados y modo básico/avanzado.

<!-- OBSERVABILITY-QA:START -->
## Atributos de calidad OBS

- Frescura: cada dimensión expone timestamp y staleAfter.
- Veracidad: no inventar máximo, uso, topología ni estado.
- Resiliencia: respuestas parciales y último snapshot válido.
- Seguridad: credenciales por referencia y read-only.
- Portabilidad: providers mediante SPI.
- Rendimiento backend: batching, paginación, cache y control de cardinalidad.
- Rendimiento frontend: 250 unidades visuales iniciales, instancing y fallback.
- Accesibilidad: color no exclusivo y tabla alternativa.
- Auditabilidad: conexión, sync, alert state y cambios de reglas registrados.
- Costo: polling y rangos limitados para no generar consumo cloud inesperado.
<!-- OBSERVABILITY-QA:END -->
