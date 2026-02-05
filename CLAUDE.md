# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Technology Stack

- **Frontend**: Angular 21 with TypeScript, Tailwind CSS v4, DaisyUI
- **Backend**: Java 21, Spring Boot 4.0, Spring Security with JWT authentication
- **Database**: PostgreSQL 17
- **Deployment**: Docker Compose with Nginx

## Development Commands

### Docker Operations (Primary Development Mode)

```bash
# Start all services (frontend, backend, database)
docker-compose up -d

# Stop all services
docker-compose down

# View logs
docker-compose logs -f
docker-compose logs -f backend
docker-compose logs -f frontend

# Rebuild and restart frontend only
docker-compose up -d --build frontend

# Rebuild and restart backend only
docker-compose up -d --build backend

# Complete reset (removes all data)
docker-compose down -v
```

### Frontend Development

```bash
cd frontend

# Install dependencies
npm install

# Run development server (outside Docker)
npm start
# Accessible at http://localhost:4200

# Build for production
npm run build:prod

# Build with bundle analysis
npm run build:stats

# Run tests
npm test
```

### Backend Development

```bash
cd backend

# Build the project
./mvnw clean install

# Run the application (outside Docker)
./mvnw spring-boot:run
# Accessible at http://localhost:8081

# Run tests (when implemented)
./mvnw test

# Package as JAR
./mvnw package
```

### Database Access

```bash
# Connect to PostgreSQL inside Docker container
docker exec -it reptile-management-db psql -U postgres -d reptilemanagement
```

## Architecture Overview

### Backend Architecture

The backend follows a layered architecture with clear separation of concerns:

**Package Structure:**
- `com.reptilemanagement.rest.controller` - REST API endpoints
- `com.reptilemanagement.rest.service` - Business logic layer
- `com.reptilemanagement.rest.service.base` - Base service classes with CRUD operations
- `com.reptilemanagement.persistence.domain` - JPA entities
- `com.reptilemanagement.persistence.repository` - Spring Data JPA repositories
- `com.reptilemanagement.persistence.dto` - Data Transfer Objects
- `com.reptilemanagement.persistence.mapper` - MapStruct mappers for entity/DTO conversion
- `com.reptilemanagement.security` - JWT authentication and Spring Security configuration
- `com.reptilemanagement.shared.audit` - Audit logging system

**Key Design Patterns:**

1. **BaseCrudService Pattern**: All services extend `BaseCrudService<Id, Entity, Dto>` which provides:
   - Standard CRUD operations (create, read, update, delete)
   - Automatic audit logging for all operations
   - Validation logic for create/update operations
   - Template method `handleEntityRelationships()` for managing entity associations

2. **Entity Update Pattern**: Entities implement `EntityUpdatable<Dto>` interface with an `update(Dto dto)` method that defines which fields can be updated. This approach:
   - Centralizes update logic in the entity
   - Prevents accidental overwriting of immutable fields
   - Makes it clear which fields are updatable

3. **MapStruct Integration**: Uses MapStruct with Lombok for automatic DTO/Entity mapping. The `maven-compiler-plugin` is configured with annotation processors for both Lombok and MapStruct with proper binding.

4. **Audit System**: Automatic audit logging via `AuditService` tracks all create/update/delete operations with user information.

**Security:**
- JWT-based authentication with refresh token support
- `JwtAuthenticationFilter` intercepts requests and validates tokens
- `SecurityConfig` defines public endpoints (`/api/auth/**`, `/api-docs/**`) and protected resources
- Default admin user created on startup via `DatabaseInitializer`

### Frontend Architecture

The frontend follows Angular's feature-based architecture with standalone components:

**Directory Structure:**
- `src/app/core/` - Core services, guards, interceptors, and configuration
  - `guards/` - Route guards (authGuard, adminGuard)
  - `interceptors/` - HTTP interceptors (auth.interceptor adds JWT tokens)
  - `services/` - Core services (auth, user, theme)
  - `config/` - Feature flags and feature registry
- `src/app/features/` - Feature modules (reptile-management, auth, profile-settings)
- `src/app/shared/` - Shared components (navbar, sidebar, confirmation-dialog)

**Key Design Patterns:**

1. **Feature Flag System**: `FEATURE_FLAGS` object in `feature-flags.ts` allows enabling/disabling features:
   - `reptileManagement: true` - Core reptile management (enabled)
   - `todos: false` - Todo functionality (disabled)
   - `userManagement: false` - User management UI (disabled)

2. **Feature Registry Pattern**: `featureRegistry` dynamically registers routes and navigation based on feature flags:
   - Routes are lazy-loaded for each feature
   - Navigation items are generated based on enabled features
   - Provides centralized feature management

3. **Standalone Components**: All components use Angular's standalone component API (no NgModules)

4. **Route Guards**:
   - `authGuard` - Protects authenticated routes, redirects to /login if not authenticated
   - `adminGuard` - Protects admin-only routes

5. **HTTP Interceptor**: `authInterceptor` automatically adds JWT token to all outgoing requests

**State Management**: Uses RxJS and services for state management (no external state library like NgRx)

## Domain Model

Core entities and their relationships:

- **User** - Application users with roles (ADMIN, USER)
- **Reptile** - Individual reptile records (species, morph, gender, age)
  - Has many: FeedingLog, WeightLog, SheddingLog, ReptileImage
  - Belongs to: Enclosure
- **Enclosure** - Housing information for reptiles
  - Has many: Reptile, EnclosureCleaning
- **FeedingLog** - Feeding event records
- **WeightLog** - Weight measurement records
- **SheddingLog** - Shedding cycle records
- **ReptileImage** - Photos of reptiles (stored as base64)
- **EnclosureCleaning** - Enclosure maintenance records

## API Documentation

When backend is running, Swagger UI is available at: `http://localhost:8081/swagger-ui/index.html`

## Configuration Files

- `.env` - Environment variables (create from `.env.example`)
  - `POSTGRES_PASSWORD` - Database password
  - `JWT_SECRET` - Secret key for JWT signing
  - `FRONTEND_PORT`, `BACKEND_PORT`, `POSTGRES_PORT` - Port configuration
- `application.yml` - Spring Boot configuration (backend)
- `angular.json` - Angular CLI configuration (frontend)
- `docker-compose.yml` - Docker services configuration

## Adding New Features

When adding a new feature to the backend:
1. Create entity in `persistence.domain` extending `BaseEntity` and implementing `EntityUpdatable<Dto>`
2. Create DTO in `persistence.dto` extending `BaseDto`
3. Create repository in `persistence.repository` extending `JpaRepository`
4. Create MapStruct mapper in `persistence.mapper` extending `BaseMapper`
5. Create service in `rest.service` extending `BaseCrudService`
6. Create controller in `rest.controller` with `@RestController` and `@RequestMapping`
7. Override `handleEntityRelationships()` in service if entity has relationships

When adding a new feature to the frontend:
1. Create feature directory in `src/app/features/`
2. Add feature flag to `FEATURE_FLAGS` in `feature-flags.ts`
3. Register feature configuration in `FEATURE_CONFIGS` in `feature-registry.ts` with routes
4. Feature will be automatically included in routing when flag is enabled



## General coding


Angular 21 (Official Angular Best Practices)

Always use standalone components over NgModules. Must NOT set standalone: true inside Angular decorators—it's the default in Angular v21+.
Use input() and output() functions instead of decorators. Use computed() for derived state.
Use native control flow (@if, @for, @switch) instead of *ngIf, *ngFor, *ngSwitch.
Use the inject() function instead of constructor injection.
Do NOT use ngClass—use class bindings instead. Do NOT use ngStyle—use style bindings instead.

Spring Boot / Java

Project-specific code conventions are fundamental: using Lombok to reduce boilerplate, adopting Google's guidelines for indentation, having specific patterns for error handling, and working with DDD structure transforms Claude from a beginner to a junior developer who writes code indistinguishable from yours.
Follow Spring AI standard code formatting. Java 17+ features are encouraged (records, switch expressions, text blocks). Avoid System.out.println—use SLF4J logging.

Tailwind CSS

Never use dynamic class interpolation like bg-${color}-500—Tailwind purges these. Instead, use object maps with complete class names.
Class order should follow Tailwind recommended Prettier plugin guidelines. Container-query-driven layouts work best where component width matters.

Claude Code Skills (Official Recommendations)

Angular skills provide coding agents with up-to-date Angular v21+ patterns, best practices, and code examples including standalone components with signal inputs/outputs, OnPush change detection, and dependency injection with inject().