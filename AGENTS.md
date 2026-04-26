# AGENTS.md — Rosario Real Estate SaaS

Cross-tool context file (Claude Code, Codex, Cursor, Gemini CLI).
Canonical detail lives in CLAUDE.md.

## Project
Java 21 + Spring Boot 3.3 + JavaFX 21 + MySQL 8.4 multi-tenant SaaS for Rosario (Argentina) real estate brokers.
Desktop-first (local-first sync) with cloud backend companion.

## Repo structure
```
shared/       value objects, enums — zero external deps
core/         domain entities + ports (pure Java, no frameworks)
persistence/  Hibernate 6 + Spring Data JPA + Flyway adapters
auth/         OAuth2 PKCE + JWT + License3j
ml/           AVM inference: jpmml-evaluator + Smile k-NN comparables
scraper/      JSoup + MercadoLibre API scraping adapters
api/          Spring Boot 3.3 REST cloud backend
desktop/      JavaFX 21 desktop application (Phase 13+)
specs/        Phase specs: SPEC.md, DoD.md, PROMPT.md, HANDOFF.md
```

## Key constraints
- `core/` is framework-free — ArchUnit tests enforce this
- Class names never end in `Impl`
- Multi-tenant via `tenant_id` discriminator on every business table
- Never use iText (AGPL) or Baileys/whatsapp-web.js
- See CLAUDE.md for full rules
