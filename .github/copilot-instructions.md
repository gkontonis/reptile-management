# Copilot Instructions - Reptile Management

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

---

## Technology Stack

- **Frontend**: Angular 21, TypeScript, Tailwind CSS v4, DaisyUI
- **Backend**: Java 21, Spring Boot 4.0, Spring Security with JWT
- **Database**: PostgreSQL 17
- **Build**: Maven (backend), npm/Angular CLI (frontend), Docker Compose
- **Mapping**: MapStruct with Lombok

## Backend Conventions

### Package Structure

```
com.reptilemanagement
├── persistence.domain       # JPA entities
├── persistence.domain.base  # BaseEntity, EntityUpdatable
├── persistence.dto          # DTOs extending BaseDto
├── persistence.mapper       # MapStruct mappers extending BaseMapper
├── persistence.repository   # Spring Data JPA repositories
├── rest.controller          # REST endpoints
├── rest.service             # Business logic extending BaseCrudService
├── rest.service.base        # BaseCrudService, BaseService
├── security                 # JWT auth, SecurityConfig
└── shared.audit             # AuditService, audit logging
```

### Entity Pattern

Entities extend `BaseEntity<Long>` and implement `EntityUpdatable<Dto>`. The `update(Dto)` method defines which fields are mutable:

```java
@Entity
@Table(name = "reptiles")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Reptile extends BaseEntity<Long> implements EntityUpdatable<ReptileDto> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Override
    public void update(ReptileDto dto) {
        this.name = dto.getName();
        // only mutable fields here
    }
}
```

### Service Pattern

Services extend `BaseCrudService<Id, Entity, Dto>` which provides CRUD + audit logging. Override `getRepository()`, `getMapper()`, and `handleEntityRelationships()` for associations:

```java
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReptileService extends BaseCrudService<Long, Reptile, ReptileDto> {
    private final ReptileRepository reptileRepository;
    private final ReptileMapper reptileMapper;

    @Override
    protected JpaRepository<Reptile, Long> getRepository() { return reptileRepository; }

    @Override
    protected BaseMapper<Reptile, ReptileDto> getMapper() { return reptileMapper; }
}
```

### Java Style

- Use Lombok (`@Data`, `@RequiredArgsConstructor`, `@Slf4j`) to reduce boilerplate
- Use SLF4J logging, never `System.out.println`
- Use Java 21 features: records, switch expressions, text blocks, pattern matching
- All resources are user-scoped: verify ownership via `authenticationInformationProvider.getAuthenticatedUserId()`
- Public endpoints: `/api/auth/**`, `/api-docs/**`. Everything else requires JWT

## Frontend Conventions

### Angular 21 Rules

- Standalone components only. Do NOT set `standalone: true` in decorators (it's the default in v21+)
- Use `input()` / `output()` functions, not `@Input` / `@Output` decorators
- Use `computed()` for derived state
- Use `inject()` function, not constructor injection
- Use native control flow: `@if`, `@for`, `@switch` (not `*ngIf`, `*ngFor`)
- Do NOT use `ngClass` or `ngStyle` -- use class/style bindings
- State management via RxJS + services (no NgRx)

### Directory Structure

```
src/app/
├── core/
│   ├── config/       # feature-flags.ts, feature-registry.ts
│   ├── guards/       # authGuard, adminGuard
│   ├── interceptors/ # auth.interceptor (adds JWT to requests)
│   └── services/     # auth, user, theme services
├── features/
│   ├── auth/
│   ├── reptile-management/  # add-reptile, reptile-list, reptile-detail
│   ├── admin/
│   └── profile-settings/
└── shared/           # navbar, sidebar, confirmation-dialog
```

### Feature Flag System

New features must be registered in two places:
1. `feature-flags.ts` -- add flag to `FEATURE_FLAGS` object
2. `feature-registry.ts` -- add `FEATURE_CONFIGS` entry with lazy-loaded routes and nav items

### Tailwind CSS

- Never use dynamic class interpolation (`bg-${color}-500`). Use object maps with complete class names
- Follow Tailwind Prettier plugin class order

## Domain Model

- **User** (ADMIN, USER roles) -- owns Reptiles and Enclosures
- **Reptile** -- species, morph, gender, birthDate, status. Has many: FeedingLog, WeightLog, SheddingLog, ReptileImage. Belongs to: Enclosure
- **Enclosure** -- has many: Reptile, EnclosureCleaning
- **FeedingLog** / **WeightLog** / **SheddingLog** / **PoopLog** -- timestamped event logs per reptile
- **ReptileImage** -- photos stored as base64

All entities have audit fields (createdAt, createdBy, updatedAt, updatedBy) via BaseEntity.

## Adding a New Backend Feature

1. Entity in `persistence.domain` extending `BaseEntity<Long>` + `EntityUpdatable<Dto>`
2. DTO in `persistence.dto` extending `BaseDto<Long>`
3. Repository in `persistence.repository` extending `JpaRepository`
4. MapStruct mapper in `persistence.mapper` extending `BaseMapper`
5. Service in `rest.service` extending `BaseCrudService`
6. Controller in `rest.controller` with `@RestController` and `@RequestMapping`
7. Override `handleEntityRelationships()` if the entity has associations

## Adding a New Frontend Feature

1. Create directory under `src/app/features/`
2. Add flag to `FEATURE_FLAGS`
3. Register in `FEATURE_CONFIGS` with routes and navigation
4. Routes are auto-included when the flag is enabled
