# Fase 00 — Definition of Done

## Checks automáticos (deben pasar todos)
- [ ] `mvn clean verify` termina en verde (exit 0)
- [ ] `mvn -pl core test` ejecuta 8 tests ArchUnit, todos GREEN
- [ ] Ninguna clase en `core/` tiene import de Spring, JPA, Hibernate o JavaFX
- [ ] No existen clases con nombre `*Impl` en ningún módulo
- [ ] GitHub Actions workflow ejecuta y muestra build verde en el último commit

## Checks manuales
- [ ] CLAUDE.md tiene ≤ 200 líneas y cubre: build commands, module map, hard rules, naming, never-use
- [ ] `.claude/settings.json` tiene deny para `.env*`, `*.pem`, `application-secrets*`
- [ ] `specs/fase-00-setup/HANDOFF.md` escrito y commiteado
- [ ] Todas las versiones en `pom.xml` son explícitas (sin `LATEST`, sin rangos `[x,)` excepto enforcer)

## Métricas
- Módulos compilables: 8/8
- Tests en `core`: ≥ 8 (ArchUnit)
- Tests fallando: 0
