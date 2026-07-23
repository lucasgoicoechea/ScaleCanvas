# ADR 0008 — Credenciales cloud por referencia

## Estado

Propuesto.

## Decisión

`ProviderConnection` guarda estrategia y referencia, nunca secreto. Usar default chains,
profiles, ADC, Workload Identity, roles o secret managers externos.

## Consecuencias

- reduce exposición;
- facilita rotación;
- exige configuración del entorno;
- la UI no puede mostrar ni recuperar secretos.
