# Fase 00 — HANDOFF

## Estado: COMPLETADA

## Qué se construyó
Maven multi-módulo (`shared → core → persistence/auth/ml/scraper → api/desktop`) con:
- Domain puro en `core/`: `Propiedad`, `Lead`, value objects (`PropiedadId`, `LeadId`, `TenantId`,
  `Money`, `Coordenadas`, `Barrio`), puertos de salida (`PropiedadRepository`, `LeadRepository`,
  `AvmEngine`) e inbound use cases (`TasarPropiedadUseCase`, `RegistrarLeadUseCase`)
- 8 tests ArchUnit que verifican aislamiento hexagonal
- Migraciones Flyway V1 (schema multi-tenant: tenants, propiedades, leads) + V2 (sync_outbox)
- GitHub Actions CI con JDK Temurin 21
- `CLAUDE.md` / `AGENTS.md` / `.claude/settings.json`

## Decisiones tomadas
- `Propiedad` y `Lead`: clases mutables (no records) porque son entidades con ciclo de vida
- Value objects y commands: records (inmutables)
- `PmmlAvmEngine`: stub que lanza `UnsupportedOperationException` hasta Phase 8
- `DesktopApp`: placeholder sin JavaFX hasta Phase 13
- `persistence` depende de Spring Data JPA; el resto de módulos de infraestructura también
- `core` y `shared`: cero dependencias de frameworks

## Estado de los módulos
| Módulo      | Compila | Tests | Implementado |
|-------------|---------|-------|--------------|
| shared      | ✅      | —     | Completo (Phase 0) |
| core        | ✅      | 8 ArchUnit | Dominio + ports completos |
| persistence | ✅      | —     | Config placeholder + SQL migrations |
| auth        | ✅      | —     | Ports placeholder |
| ml          | ✅      | —     | PmmlAvmEngine stub |
| scraper     | ✅      | —     | ListingSource port + RawListing |
| api         | ✅      | —     | Spring Boot main class + application.yml |
| desktop     | ✅      | —     | Placeholder, JavaFX en Phase 13 |

## Próxima fase
**Fase 01 — Core domain completo**: agregar `Visita`, `Tasacion`, `Agente`, casos de uso
`AgendarVisitaUseCase`, lógica de scoring BANT en `Lead`, tests unitarios de dominio ≥80% coverage.

## Comandos útiles
```bash
mvn clean verify          # build completo
mvn -pl core test         # solo ArchUnit tests
git log --oneline -10     # ver commits de esta fase
```
