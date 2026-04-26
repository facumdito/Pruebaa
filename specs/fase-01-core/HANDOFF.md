# Fase 01 — HANDOFF

## Estado: COMPLETADA

## Qué se construyó
Domain completo en `core/`:

**Nuevas entidades:**
- `Agente` — agente/tasador con activar/desactivar/actualizarContacto
- `Visita` — ciclo de vida: AGENDADA→CONFIRMADA→REALIZADA / CANCELADA / NO_SHOW
  - `confirmar()`, `realizar(notas)`, `cancelar(motivo)`, `marcarNoShow()`, `reprogramar(fecha, agente)`
  - Todas las transiciones inválidas lanzan `IllegalStateException`
- `Tasacion` — registro persistido de tasación AVM o manual
  - Factory `desdeResultadoAvm(id, tenantId, tasadorId, TasacionResult)` para crea desde AVM
  - `isHighConfidence()` → pe10 >= 0.70

**BANT scoring en Lead:**
- Campos: `bantBudget`, `bantAuthority`, `bantNeed`, `bantTimeline` (0-100 cada uno)
- `actualizarBant(b,a,n,t)` → `score = round(b×0.30 + a×0.15 + n×0.25 + t×0.30)`
- `actualizarScore(n)` sigue disponible para override manual

**Nuevos puertos:**
- `port.out`: `AgenteRepository`, `VisitaRepository`, `TasacionRepository`
- `port.in`: `AgendarVisitaUseCase` + `AgendarVisitaCommand`

**Flyway V3:** tablas `agentes`, `visitas`, `tasaciones` con FKs compuestas cross-tenant safe.

## Cobertura final
| Métrica | Resultado |
|---------|-----------|
| Tests totales | 89 (63 dominio + 8 ArchUnit + 19 value objects/commands) |
| Instruction coverage | 89.1% |
| Branch coverage | 91.3% |

## Decisiones tomadas
- `Tasacion` (entidad) vs `TasacionResult` (VO transiente): semánticas distintas, coexisten
- `Visita.reprogramar()` resetea a AGENDADA (no CONFIRMADA) — requiere nueva confirmación del lead
- BANT ponderación hardcodeada como constantes de clase (W_BUDGET etc.) — fácil de ajustar en Phase ML

## Estado de los módulos
| Módulo      | Compila | Tests |
|-------------|---------|-------|
| core        | ✅      | 89 ✅ (89.1% instrucciones) |
| persistence | ✅      | — (V1+V2+V3 Flyway listos) |
| auth        | ✅      | — (stub) |
| ml          | ✅      | — (PmmlAvmEngine stub) |
| scraper     | ✅      | — (stub) |
| api         | ✅      | — (main class) |
| desktop     | ✅      | — (placeholder) |

## Próxima fase
**Fase 02 — Persistence**: Implementar JPA entities (`JpaPropiedad`, etc.),
`JpaPropiedadRepository implements PropiedadRepository`, multi-tenant con Hibernate `@TenantId`,
`CurrentTenantIdentifierResolver` con `ThreadLocal`, Testcontainers MySQL para integration tests.
Prerequisito: tener MySQL 8.4 disponible en docker para correr `mvn -pl persistence test`.
