# Fase 01 — Definition of Done

## Automático (todos deben pasar)
- [x] `mvn clean verify` BUILD SUCCESS
- [x] 89 tests, 0 failures, 0 errors
- [x] ArchUnit: 8 reglas verdes
- [x] Instruction coverage core: 89.1% (≥80%)
- [x] Branch coverage core: 91.3% (≥80%)
- [x] Ninguna nueva clase en `core/` usa Spring/JPA/JavaFX

## Manual
- [x] `Visita.realizar()` lanza si está CANCELADA o NO_SHOW
- [x] `Visita.cancelar()` lanza si ya está REALIZADA
- [x] BANT computation verificada con test paramétrico (80+60+70+50 → 66)
- [x] Flyway V3 tiene FKs compuestas (tenant_id, entity_id) — sin FKs cross-tenant
- [x] HANDOFF.md escrito
