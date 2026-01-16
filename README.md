# Reptile Management System

A comprehensive web application for managing reptile care, tracking feeding schedules, weight logs, shedding cycles, and enclosure maintenance.

## Features

- 🦎 **Reptile Management**: Add, edit, and track your reptile collection
- 📸 **Image Gallery**: Upload and manage photos of your reptiles
- 🏠 **Enclosure Tracking**: Monitor and manage enclosure information
- 🍖 **Feeding Logs**: Track feeding schedules and diet history
- ⚖️ **Weight Monitoring**: Record and visualize weight changes over time
- 🐍 **Shedding Logs**: Track shedding cycles and patterns
- 🧹 **Cleaning Schedule**: Manage enclosure cleaning and maintenance
- 👤 **User Management**: Multi-user support with role-based access control
- 🔐 **Secure Authentication**: JWT-based authentication system

## Tech Stack

### Backend
- Java 21
- Spring Boot 4.0
- Spring Security with JWT
- PostgreSQL 17
- Maven
- MapStruct for DTO mapping

### Frontend
- Angular 21
- TypeScript 5
- Tailwind CSS & DaisyUI
- RxJS

### Infrastructure
- Docker & Docker Compose
- Nginx (for production)

## Prerequisites

- Docker Desktop installed and running
- Git
- (Optional) Java 21 and Node.js 22 for local development

## Quick Start with Docker

1. **Clone the repository**
   ```bash
   git clone <your-repo-url>
   cd reptile-management
   ```

2. **Create environment file**
   ```bash
   cp .env.example .env
   ```
   
   Edit `.env` and update:
   - `POSTGRES_PASSWORD`: Set a strong password
   - `JWT_SECRET`: Generate a new secret (run: `openssl rand -base64 32`)

3. **Start the application**
   ```bash
   docker-compose up -d
   ```

4. **Access the application**
   - Frontend: http://localhost:3000
   - Backend API: http://localhost:8081/api
   - Database: localhost:5432

5. **Default credentials**
   - Username: `admin`
   - Password: `admin123`
   
   ⚠️ **Change the default password immediately after first login!**

## Database Only (Development Mode)

If you want to run only the PostgreSQL database in Docker and run backend/frontend locally:

```bash
# Start only the database
docker-compose up -d postgres

# Backend (in backend/ directory)
./mvnw spring-boot:run

# Frontend (in frontend/ directory)
pnpm install
pnpm start
```

## Docker Desktop Management

### View Running Containers
In Docker Desktop, you should see:
- `reptile-management-db` - PostgreSQL database
- `reptile-management-backend` - Spring Boot API
- `reptile-management-frontend` - Angular application

### Stop the Application
```bash
docker-compose down
```

### Stop and Remove Data
```bash
docker-compose down -v
```

## Development

### Backend Development
```bash
cd backend
./mvnw clean install
./mvnw spring-boot:run
```

### Frontend Development
```bash
cd frontend
pnpm install
pnpm start
```
Frontend will be available at http://localhost:4200 with hot-reload enabled.

### Database Access
```bash
# Connect to PostgreSQL
docker exec -it reptile-management-db psql -U postgres -d reptilemanagement
```

## API Documentation

Once the backend is running, API endpoints are available at:
- Base URL: `http://localhost:8081/api`

### Main Endpoints
- `POST /api/auth/login` - User authentication
- `GET /api/reptiles` - List all reptiles
- `POST /api/reptiles` - Create new reptile
- `GET /api/reptiles/{id}` - Get reptile details
- `POST /api/feeding-logs` - Add feeding log
- `POST /api/weight-logs` - Add weight log
- `POST /api/shedding-logs` - Add shedding log

## Project Structure

```
reptile-management/
├── backend/              # Spring Boot backend
├── frontend/             # Angular frontend
├── docker-compose.yml    # Docker orchestration
├── .env.example         # Environment template
└── README.md
```

## Configuration

### Database Configuration
Database settings can be modified in `.env`:
```env
POSTGRES_DB=reptilemanagement
POSTGRES_USER=postgres
POSTGRES_PASSWORD=your_secure_password
POSTGRES_PORT=5432
```

### Backend Configuration
Backend settings in `.env`:
```env
BACKEND_PORT=8081
JWT_SECRET=your_jwt_secret_here
JWT_EXPIRATION=86400000
```

### Frontend Configuration
Frontend settings in `.env`:
```env
FRONTEND_PORT=3000
```

## Troubleshooting

### Port Already in Use
If ports 5432, 8081, or 3000 are already in use, change them in `.env`:
```env
POSTGRES_PORT=5433
BACKEND_PORT=8082
FRONTEND_PORT=3001
```

### Database Connection Issues
1. Ensure Docker Desktop is running
2. Check if the database container is healthy:
   ```bash
   docker-compose ps
   ```
3. View logs:
   ```bash
   docker-compose logs postgres
   ```

### Backend Not Starting
```bash
# View backend logs
docker-compose logs backend

# Rebuild backend
docker-compose up -d --build backend
```

### Frontend Not Loading
```bash
# View frontend logs
docker-compose logs frontend

# Rebuild frontend
docker-compose up -d --build frontend
```

## Security Notes

- Always change default passwords in production
- Generate a strong JWT secret: `openssl rand -base64 64`
- Never commit `.env` files to version control
- Use HTTPS in production
- Regularly update dependencies

## License

[Your License]

## Contributing

[Your contributing guidelines]

## Support

[Your support information]
