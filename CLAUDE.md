# CLAUDE.md — Rosario Real Estate SaaS

## Build & run
```bash
mvn clean verify              # full build + all tests
mvn -pl core test             # only core unit/arch tests
mvn -pl api spring-boot:run   # start cloud API (needs MySQL)
```

## Module map (build order = dependency order)
```
shared → core → [persistence | auth | ml | scraper] → [api | desktop]
```
- `core/` — pure Java domain, NO Spring/JPA/JavaFX (ArchUnit enforces this)
- `api/` — Spring Boot 3.3.5 cloud backend
- `desktop/` — JavaFX 21 desktop app (activated Phase 13)

## Hard rules — ArchUnit tests in `core/` enforce these
- `core/` NEVER imports `org.springframework.*`, `jakarta.persistence.*`, or `javafx.*`
- Class names NEVER end in `Impl` — name by technology: `JpaPropiedadRepository`, `PmmlAvmEngine`
- Everything in `core.port.out` must be an interface
- No standalone interface with a single implementation unless it is a hexagonal port

## Multi-tenant
Every business table carries `tenant_id CHAR(36)`. Hibernate `@TenantId` discriminates automatically.
Every FK includes `tenant_id` — cross-tenant FKs are forbidden.
Tenant context stored in `ThreadLocal`, resolved from JWT claim `tenant_id`.

## Naming conventions
- Value objects: Java records (`TenantId`, `PropiedadId`, `Money`, `Coordenadas`)
- Domain entities: plain mutable class (`Propiedad`, `Lead`) — versioned with `long version`
- Outbound ports: interface, tech-named adapter (`PropiedadRepository` → `JpaPropiedadRepository`)
- Inbound ports (use cases): interface in `core.port.in`
- Commands: record in `core.port.in`

## Never use
- **iText** (AGPL viral) — use `openhtmltopdf 1.0.10` for all PDF
- **Baileys / whatsapp-web.js** — Meta Cloud API + n8n only
- **Lombok** in `core/` or `shared/`
- Storing phone/email of scraped listing owners (Ley 25.326 Argentina)
- Spring/JPA annotations in `core/` or `shared/`
- Class names ending in `Impl`

## Currency — always persist all three
```sql
precio_usd        DECIMAL(15,2)   -- primary for compraventa
tipo_cambio_mep   DECIMAL(15,4)   -- TC del día, dolarapi.com/v1/dolares/bolsa
fecha_cotizacion  DATE
```
Dólar source: `dolarapi.com/v1/dolares/bolsa`, cache 15 min, retry ×3 with backoff.

## AVM (ML valuation)
- Train: Python `sklearn2pmml` → `*.pmml` file
- Infer: `ml/` module, `PmmlAvmEngine implements AvmEngine`, `jpmml-evaluator 1.7.7`
- Target: `log(precio_usd)` → inverse with `Math.exp()`
- Always report: MdAPE + PE10/PE20 + 80% confidence band, never point estimate alone

## Sync (local-first)
UI reads/writes local MySQL only. `SyncManager` drains `sync_outbox` to cloud in background.
Conflict resolution: LWW by `updated_at` for 90% of fields; manual UI for `precio_venta`, `estado`, `comision`.

## Spec files
Each phase has `specs/fase-NN-<slug>/{SPEC,DoD,PROMPT,HANDOFF}.md`.
Read the relevant HANDOFF.md at session start before any coding.
