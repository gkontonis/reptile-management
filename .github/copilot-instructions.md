# Copilot Instructions - Reptile Management

> Kept in sync with [`AGENTS.md`](../AGENTS.md) by hand (no import support here) — update both when this changes.

## General Principles

### Think Before Coding

Don't assume. Don't hide confusion. Surface tradeoffs.

Before implementing:

- State your assumptions explicitly. If uncertain, ask.
- If multiple interpretations exist, present them - don't pick silently.
- If a simpler approach exists, say so. Push back when warranted.
- If something is unclear, stop. Name what's confusing. Ask.

### Simplicity First

Minimum code that solves the problem. Nothing speculative.

- No features beyond what was asked.
- No abstractions for single-use code.
- No "flexibility" or "configurability" that wasn't requested.
- No error handling for impossible scenarios.
- If you write 200 lines and it could be 50, rewrite it.

Ask yourself: "Would a senior engineer say this is overcomplicated?" If yes, simplify.

### Surgical Changes

Touch only what you must. Clean up only your own mess.

When editing existing code:

- Don't "improve" adjacent code, comments, or formatting.
- Don't refactor things that aren't broken.
- Match existing style, even if you'd do it differently.
- If you notice unrelated dead code, mention it - don't delete it.

When your changes create orphans:

- Remove imports/variables/functions that YOUR changes made unused.
- Don't remove pre-existing dead code unless asked.

The test: Every changed line should trace directly to the user's request.

### Goal-Driven Execution

Define success criteria. Loop until verified.

Transform tasks into verifiable goals:

- "Add validation" -> "Write tests for invalid inputs, then make them pass"
- "Fix the bug" -> "Write a test that reproduces it, then make it pass"
- "Refactor X" -> "Ensure tests pass before and after"

For multi-step tasks, state a brief plan:

1. [Step] -> verify: [check]
2. [Step] -> verify: [check]
3. [Step] -> verify: [check]

Strong success criteria let you loop independently. Weak criteria ("make it work") require constant clarification.

## Technology Stack

- **Frontend**: Angular 21, TypeScript, Tailwind CSS v4, DaisyUI
- **Backend**: Java 21, Spring Boot 4.0, Spring Security with JWT authentication
- **Database**: PostgreSQL 17
- **Mapping**: MapStruct with Lombok
- **Build**: Maven (backend), npm/Angular CLI (frontend), Docker Compose
- **Deployment**: Docker Compose with Nginx

## Development Commands

```bash
# Docker (primary)
docker-compose up -d                        # start all services
docker-compose down                         # stop all
docker-compose down -v                      # full reset (destroys data)
docker-compose up -d --build frontend       # rebuild frontend only
docker-compose up -d --build backend        # rebuild backend only
docker-compose logs -f [frontend|backend]   # stream logs

# Database only (local dev)
docker-compose up -d db
docker exec -it reptile-management-db psql -U postgres -d reptilemanagement

# Frontend (outside Docker)
cd frontend && npm install && npm start     # http://localhost:4200
npm run build:prod

# Backend (outside Docker)
cd backend && ./mvnw spring-boot:run       # http://localhost:8081
./mvnw clean install
```

## Architecture Overview

### Backend

Layered architecture: controller → service → repository → entity.

**Package Structure** (`com.reptilemanagement`):
- `rest.controller` - REST endpoints
- `rest.service` / `rest.service.base` - Business logic + `BaseCrudService`
- `persistence.domain` / `persistence.domain.base` - JPA entities + `BaseEntity`
- `persistence.repository` - Spring Data JPA repositories
- `persistence.dto` - DTOs
- `persistence.mapper` - MapStruct mappers
- `security` - JWT filter, Spring Security config
- `shared.audit` - Audit logging
- `shared.auth` / `shared.constants` - Auth helpers, constants
- `config` - App configuration beans
- `exception` - Custom exception types

**Key Patterns:**

- **`BaseCrudService<Id, Entity, Dto>`** — all services extend this; provides CRUD + audit logging + validation. Override `handleEntityRelationships()` for associations.
- **`EntityUpdatable<Dto>`** — entities implement `update(Dto dto)` to define mutable fields; prevents accidental overwrites.
- **MapStruct + Lombok** — mappers extend `BaseMapper`; annotation processors wired in `maven-compiler-plugin`.
- **Flyway** — schema managed via `db/migration/` SQL scripts; `FlywayConfig` ensures migrations complete before JPA validation.
- **Security** — JWT with refresh tokens; `JwtAuthenticationFilter` validates tokens; public endpoints: `/api/auth/**`, `/api-docs/**`. All other resources are user-scoped: verify ownership via `authenticationInformationProvider.getAuthenticatedUserId()`.

### Frontend

Feature-based Angular architecture with standalone components.

**Directory Structure** (`src/app`):
- `core/` — guards (`authGuard`, `adminGuard`), interceptors (JWT injection), services (auth, user, theme), `config/` (feature flags + registry)
- `features/` — `reptile-management`, `auth`, `profile-settings`, `admin`
- `components/` — reusable UI components
- `shared/` — navbar, sidebar, confirmation-dialog

**Key Patterns:**
- **Feature flags** (`feature-flags.ts`): `reptileManagement: true`, `userManagement: true`, `todos: false`
- **Feature registry** (`feature-registry.ts`): lazy-loaded routes + nav items driven by flags; register in `FEATURE_CONFIGS`
- **State**: RxJS + services only (no NgRx)

## Domain Model

- **User** — roles: ADMIN, USER
- **Reptile** → belongs to Enclosure; has many: FeedingLog, WeightLog, SheddingLog, PoopLog, ReptileImage
- **Enclosure** → has many: Reptile, EnclosureCleaning
- **FeedingLog**, **WeightLog**, **SheddingLog**, **PoopLog** — reptile event logs
- **ReptileImage** — stored as base64
- **EnclosureCleaning** — maintenance records

Swagger UI: `http://localhost:8081/swagger-ui/index.html`

## Adding New Features

**Backend checklist:**
1. Entity in `persistence.domain` extending `BaseEntity`, implementing `EntityUpdatable<Dto>`
2. DTO in `persistence.dto` extending `BaseDto`
3. Repository in `persistence.repository` extending `JpaRepository`
4. Mapper in `persistence.mapper` extending `BaseMapper`
5. Service in `rest.service` extending `BaseCrudService`
6. Controller in `rest.controller` with `@RestController` + `@RequestMapping`
7. Flyway migration script in `db/migration/` if schema changes
8. Override `handleEntityRelationships()` if entity has associations

**Frontend checklist:**
1. Feature directory in `src/app/features/`
2. Feature flag in `FEATURE_FLAGS` (`feature-flags.ts`)
3. Feature config in `FEATURE_CONFIGS` (`feature-registry.ts`) with lazy-loaded routes

## General Coding

**Angular 21:**
- Do NOT set `standalone: true` — it's the default in v21+
- Use `input()` / `output()` functions, not decorators
- Use `computed()` for derived state
- Native control flow: `@if`, `@for`, `@switch` — not `*ngIf`, `*ngFor`
- `inject()` for DI — not constructor injection
- Class bindings over `ngClass`; style bindings over `ngStyle`

**Java / Spring Boot:**
- Lombok for boilerplate; Google style indentation
- Java 21 features encouraged: records, switch expressions, text blocks, pattern matching
- SLF4J for logging — no `System.out.println`

**Tailwind CSS:**
- No dynamic interpolation (`bg-${color}-500` gets purged) — use full class name maps
- Follow Prettier plugin class ordering
