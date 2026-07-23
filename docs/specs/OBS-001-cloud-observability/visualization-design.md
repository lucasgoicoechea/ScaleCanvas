# Diseño de visualización 3D

## Tecnología propuesta

El frontend actual usa React 18. La opción preferida es:

- `three`
- `@react-three/fiber` major compatible con React 18
- `@react-three/drei`
- `@types/three`

ECharts continúa para gráficos 2D e históricos.

## Unidad visual

Cada host/nodo/plataforma tiene un `ResourceFrame`:

```text
ResourceFrame
├── CapacityShell
├── CpuColumn
├── GpuColumn (opcional)
├── MemoryColumn
├── StorageColumn
├── AlertHalo
├── ResourceLabel
└── ChildServiceGroup
```

## Capacidad y carga

- shell exterior: máximo;
- fill interior: valor actual;
- espacio libre: transparente;
- overflow: banda superior o marcador;
- actual/max visible en tooltip y panel;
- porcentaje visible solo si max es válido.

## Color

El color pertenece a cada dimensión, no al cubo completo:

- HEALTHY: verde;
- WARNING: amarillo;
- CRITICAL: rojo;
- UNKNOWN: gris;
- STALE: gris azulado;
- NO_CAPACITY: violeta;
- free capacity: azul/cian translúcido.

El borde del agrupador usa el peor estado.

## Ejes y cotas

Las dimensiones físicas del objeto no mezclan unidades absolutas entre recursos.

Dos modos:

### Normalized mode

Todas las columnas tienen la misma altura máxima visual y muestran 0–100 %.

### Absolute comparison mode

Compara recursos de la misma clase:

- CPU contra CPU;
- memoria contra memoria;
- storage contra storage.

Nunca comparar visualmente 8 vCPU con 32 GiB como si fueran la misma escala.

Cotas:

- ticks 0, 25, 50, 75, 100 %;
- línea warning;
- línea critical;
- tooltip con unidad absoluta;
- máximo configurable.

## Servicios internos

Mostrar hijos si:

- `relationType` válido;
- `confidence` suficiente;
- usuario no desactivó detalle.

Para muchos hijos:

- agrupar;
- mostrar top N por presión;
- representar resto como `+N services`;
- ofrecer drill-down.

## Labels

- nombre principal debajo;
- nombre de servicio dentro o lateral;
- evitar `Html` por cada objeto cuando haya cientos;
- usar text geometry/SDF o labels solo en selección/hover;
- incluir búsqueda.

## Interacción

- orbit controls;
- zoom;
- seleccionar;
- hover;
- filtros por provider/account/region/state/type;
- toggle CPU/GPU/memory/storage;
- toggle labels;
- expand/collapse;
- volver a vista general;
- refresh;
- pausar polling.

## Rendimiento

Objetivo inicial: 250 unidades visuales.

Estrategias:

- `frameloop="demand"`;
- geometry/material reuse;
- instancing para columnas repetidas;
- level of detail;
- labels bajo demanda;
- memoización de `Snapshot -> SceneModel`;
- no recrear toda la escena por cada sample;
- degradar a vista 2D/tabla si WebGL no está disponible;
- virtualizar paneles HTML.

## Accesibilidad

- color no exclusivo;
- ícono y texto de estado;
- tabla alternativa;
- navegación por teclado fuera del canvas;
- contraste;
- opción reducir movimiento;
- tooltips legibles;
- unidades visibles.
