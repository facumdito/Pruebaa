# Fase 01 — Core domain completo

## Objetivo
Completar el dominio puro en `core/` con las entidades restantes, scoring BANT,
y tests unitarios con ≥80% de cobertura de instrucciones.

## Nuevas entidades y tipos
- `Agente` (agente inmobiliario/tasador), `AgenteId`
- `Visita` con ciclo de vida: AGENDADA→CONFIRMADA→REALIZADA / CANCELADA / NO_SHOW
- `Tasacion` (registro persistido de tasación), `TasacionId`
- `MetodologiaTasacion` enum: AVM_PMML, COMPARABLE_KNN, MANUAL, MIXTA

## BANT scoring en Lead
Ponderación: Budget 30% + Authority 15% + Need 25% + Timeline 30%.
`actualizarBant(budget, authority, need, timeline)` recalcula `score` automáticamente.
Validación: cada factor 0-100, IllegalArgumentException si fuera de rango.

## EARS criteria
- WHEN `Visita` está CANCELADA, THEN `realizar()` lanza `IllegalStateException`
- WHEN `Visita` está REALIZADA, THEN `cancelar()` lanza `IllegalStateException`
- WHEN BANT factor > 100, THEN `actualizarBant()` lanza `IllegalArgumentException`
- WHEN se persiste una Tasacion, THEN incluye banda inferior Y superior (no sólo punto central)
- WHEN `mvn -pl core test`, THEN cobertura de instrucciones ≥ 80%

## Nuevos puertos
- `AgenteRepository`, `VisitaRepository`, `TasacionRepository` (port.out)
- `AgendarVisitaUseCase` + `AgendarVisitaCommand` (port.in)

## Flyway
V3: tablas `agentes`, `visitas`, `tasaciones` con FKs compuestas (tenant_id + id).
