# Checklist de prueba OBS-001

## Backend
- [ ] `mvn clean compile`
- [ ] `mvn clean test`
- [ ] `GET /api/v1/observability/snapshot/simulated` responde 200

## Frontend
- [ ] `npm run build`
- [ ] `npm run dev`
- [ ] Home carga sin error
- [ ] Evaluación demo abre panel de resultados
- [ ] `Open 3D view` abre modal
- [ ] Modal muestra cubos transparentes + cotas CPU/MEM/STORAGE
- [ ] Selección resalta recurso y muestra label
- [ ] Jerarquía host→service visible con línea
- [ ] Si WebGL falla, se muestra fallback 2D

## Notas
- No conectes cloud todavía.
- No persistas secretos.
- No borres tests para pasar build.
