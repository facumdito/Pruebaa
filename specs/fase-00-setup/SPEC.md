# Fase 00 — Setup: Maven multi-módulo + CI + ArchUnit + .claude/

## Objetivo
Establecer la base de todo el proyecto: estructura de módulos Maven, pipeline CI verde,
reglas arquitectónicas verificadas automáticamente, y configuración de Claude Code.

## Módulos (build order)
```
shared → core → persistence | auth | ml | scraper → api | desktop
```

## EARS criteria
- WHEN se ejecuta `mvn clean verify`, THEN el build termina en verde
- WHEN alguien agrega `import org.springframework.*` a `core/`, THEN ArchUnit falla en CI
- WHEN alguien nombra una clase `*Impl`, THEN ArchUnit falla en CI
- WHEN se hace push a cualquier branch, THEN GitHub Actions ejecuta el build

## Entregables
- `pom.xml` parent con dependency management completo (versiones locked, sin rangos)
- 8 `pom.xml` de módulo con dependencias declaradas
- Dominio `core/`: `Propiedad`, `Lead`, value objects, ports (interfaces)
- Migraciones Flyway V1 (schema inicial) + V2 (sync_outbox)
- ArchUnit test con 8 reglas de aislamiento hexagonal
- `.github/workflows/ci.yml` con JDK Temurin 21 + cache Maven
- `CLAUDE.md` ≤ 200 líneas, alta señal
- `.claude/settings.json` con deny rules para secrets
