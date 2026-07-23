# Pruebas de aceptación

## Visualización

### Capacidad saludable

Dado un host con 4 vCPU y uso 2 vCPU,
cuando se renderiza,
entonces CPU muestra 50 %, valor 2, máximo 4 y estado HEALTHY.

### Warning

Dado un ratio CPU de 0.75 sostenido durante la ventana,
cuando se evalúa,
entonces la columna CPU es amarilla y el host indica WARNING.

### Critical

Dado memory ratio 0.90 sostenido,
entonces memoria es roja y el borde del recurso es CRITICAL.

### Independencia

Dado CPU critical y storage healthy,
entonces CPU es roja y storage verde.

### GPU ausente

Dado un host sin GPU,
entonces no aparece columna GPU.

### Máximo desconocido

Dado storage current sin maximum,
entonces no se calcula porcentaje y se muestra NO_CAPACITY.

### Stale

Dado un sample más viejo que staleAfter,
entonces se muestra STALE y nunca HEALTHY.

### Jerarquía

Dada una relación RUNS_ON de alta confianza,
entonces el servicio aparece dentro del host.

Dada una relación desconocida,
entonces el servicio aparece como unassigned.

## Providers

### Simulado

Misma seed y perfil producen el mismo snapshot.

### AWS

Una conexión sin permisos devuelve error canónico y no expone stack trace ni credencial.

### GCP

Una conexión sin ADC devuelve error canónico.

### Parcial

Si una región falla y otra responde,
entonces snapshot `partial=true` y conserva datos válidos.

## Alertas

### Histéresis

Una oscilación de un sample sobre warning no cambia inmediatamente el estado.

### Recuperación

Critical no vuelve a healthy sin atravesar las condiciones de salida.

### No data

Ausencia de datos produce UNKNOWN por default.

## Seguridad

- secretos no aparecen en JSON;
- secretos no aparecen en logs;
- frontend no recibe credential material;
- provider mutation está deshabilitada;
- MCP solo expone tools read-only.

## Performance

Con 250 unidades visuales:

- interacción aceptable;
- labels degradables;
- no render loop permanente si no hay cambios;
- fallback disponible.
