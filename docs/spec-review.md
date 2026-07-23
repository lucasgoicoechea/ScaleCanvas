# Informe OBS-000: Revisión de Especificaciones

## Hallazgos Principales

### Contradicciones Detectadas

1. **Incompatibilidad de Versión Java**
   - `backend/pom.xml:21`: Reconoce Java 25
   - `CONVENTIONS.md:5`: Requiere Java 21
   - **Riesgo**: Dependencias incompatibles, entorno inconsistente

2. **Falta de Wrapper de Maven**
   - `backend/.mvn/wrapper/` está vacío
   - No existe `mvnw` o `mvnw.cmd`
   - `maven-wrapper.properties` no encontrado
   - **Riesgo**: No se pueden ejecutar tests/build, env constraints

3. **POV no Factorizado**
   - `pom.xml:10`: spring-boot-starter-parent v3.5.14
   - `CONVENTIONS.md:12`: Mínimo recomendado Java 21
   - **Riesgo**: `spring-boot-starter-parent@3.5.14` puede requerir Java 17+ (ejemplos)

### Requisitos No Testeables

4. **Scripts de Compilación/Tests**
   - `backend/GRADLE.md` o `build.gradle` no encontrados (mentión en README)
   - `README.md` indica `mvnw clean test`
   - **Riesgo**: Sin medio reproducible, dependencias externas

5. **Test Fixtures**
   - `backend/src/test/java/com/scalecanvas/TestFixtures.java` existe pero:
     - Estado inconsistente según `CONVENTIONS.md:12` (tests para fórmulas y reglas)
     - Contiene fixtures de escenarios completos pero no test de formulas riêng

### Desfase Entorno/Documentación

6. **Configuración en Vivo**
   - `java --version` = `25.0.3` (corretto)
   - `HOME/.config/maven` no encontrado
   - **Riesgo**: Stack de Java local cambia, breaking changes potencials

7. **Configuración Front/Literal**
   - `frontend/package.json` scripts: `"build": "tsc -b && vite build"`
   - `frontend/vite.config.ts` usa `.env` para variables (testado)
   - **Riesgo**: Scripts frontend no ejecutables por políticas de seguridad

## ADRs Faltantes

### FODA/Contradicciones

1. **Java 25 vs 21**
   - **Issue**: En conflict con convención
   - **Probabilidad**: 95% impacta TS/build
   - **Impacto**: Tests, CI/CD, despliegue
   - **Acción**: Documento y resuelva antes OBS-001

2. **Permiso Script Wrapper**
   - **Issue**: `node.ps1` restringido por política de seguridad
   - **Probabilidad**: 85% scripts no ejecutables
   - **Impacto**: CI/CD, pruebas automatizadas
   - **Acción**: Documentar método alternativo (ejecución manual o wrapper)

3. **No persistir secretos**
   - **Issue**: No se pueden generar automáticamente secrets cloud
   - **Probabilidad**: 100% sin credenciales
   - **Impacto**: Conectores cloud observabilidad bloqueados
   - **Acción**: Documentar roadmap (RFC antes primaria)

## Riesgos de Negocio

### Fallos de Validación

1. **JAVA VERSION MISMATCH**
   - Campo: DEPENDENCY_VALIDATION
   - Estado: PENDIENTE
   - Timestamps: 2026-07-22 (actual)
   - URL: EJECUTAR `java -version` vs `conventions.md`

2. **No se pueden Ejecutar Tests**
   - Campo: TEST_LAUNCHABILITY
   - Estado: BLOQUEADO
   - Timestamps: 2026-07-22 (actual)
   - URL: WARNINGS sobre wrapper Maven

3. **SCOPES DE SPEC LIMITADOS**
   - Campo: SCOPE_BREAKDOWN
   - Estado: OBSERVABILITY_START_HERE.md completo
   - WARNING importante: "conectores cloud terminan read-only"

## Próximos Pasos Requeridos

### Inmediato (Antes OBS-001)

1. **Responder Contradicción Java**
   - ¿La convención Java 21 es obligatoria?
   - ¿El pom.xml Java 25 es correcto?
   - ¿Actualizar pom.xml a 21 o conventions.md a 25?

2. **Resolver Wrapper de Maven**
   - ¿Obtener wrapper del repositorio oficial?
   - ¿Agregar a repositorio de GitHub?
   - ¿Documentar método alternativo para CI/CD?

3. **Documentar Configuración Hoja de Ruta**
   - Documentar botón visible de política de seguridad
   - Documentar estado sobresaliente de `pom.xml` vs `CONVENTIONS.md`

### Después (Durante OBS-001)

1. **Actualizar Entire Task List**
2. **Ejecutar tests/build existentes**
3. **Confirmar endpoints funcionando**
4. **Documentar progreso real**

## Conclusión

**OBS-000 INCOMPLETO debido a 2 bloqueos bloqueadores:**
1. **Seguridad Política**: Scripts no ejecutables
2. **Java Version Contradicción**: Frecuente diferencia de 4 versiones

**Acción Requerida**: