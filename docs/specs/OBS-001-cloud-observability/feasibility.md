# Factibilidad

## Sí, directamente viable

| Idea | Decisión |
|---|---|
| Contenedor 3D por servidor | Viable |
| Máximo y valor actual | Viable |
| Volumen usado proporcional | Viable mediante ratio normalizado |
| Verde/amarillo/rojo | Viable con thresholds e histéresis |
| Capacidad libre azul | Viable como material translúcido |
| Nombre debajo del recurso | Viable |
| Nombre dentro del agrupador | Viable con limitación de densidad |
| Servicios dentro de un host | Viable si existe mapping |
| Alertas locales | Viable |
| Lectura AWS/GCP | Viable mediante SDK/API |
| Alarmas cloud read-only | Viable |
| Configuración multi-provider | Viable mediante SPI |

## Viable con condiciones

### GPU

La capacidad puede obtenerse de metadata, pero la utilización requiere telemetría del SO,
driver o agente. Sin dato se muestra `UNKNOWN`, no 0.

### Servicios dentro de una VM

El proveedor conoce la VM, pero no necesariamente los procesos. Fuentes posibles:

- OpenTelemetry Resource attributes;
- CloudWatch Agent procstat;
- Google Ops Agent process metrics;
- ECS/Kubernetes metadata;
- tags;
- configuración manual.

### Máximos de disco

Distinguir:

- tamaño provisionado;
- filesystem utilizable;
- bytes ocupados;
- límites de IOPS y throughput.

### Serverless

No debe inventarse una “máquina”. Se representa una plataforma lógica con instancias
efímeras, concurrencia, memoria configurada, throttling y límites del servicio.

### Predicción

Puede mostrarse `pressureScore` o `riskIndicator`. No llamarlo probabilidad de colapso sin
modelo validado, histórico suficiente y evaluación estadística.

## No recomendable

- combinar todas las dimensiones en una única barra;
- sumar porcentajes;
- usar el volumen total como salud;
- mantener verde un dato viejo;
- ocultar unidades;
- hardcodear nombres de métricas cloud en el frontend;
- leer métricas directamente desde el navegador;
- almacenar claves cloud en tablas o localStorage;
- habilitar permisos de escritura por defecto;
- permitir que una IA cambie alarmas o infraestructura.
