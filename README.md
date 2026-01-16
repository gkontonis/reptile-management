# Reptile Management System

A comprehensive web application designed to help reptile enthusiasts and professionals manage their reptile collections. Track feeding schedules, monitor health metrics, maintain enclosure records, and keep detailed care histories all in one place.

## Screenshots

### Dashboard Overview
![Dashboard](docs/screenshots/dashboard.png)
*Main dashboard showing your reptile collection at a glance*

### Reptile Details
![Reptile Details](docs/screenshots/reptile-detail.png)
*Detailed view with feeding logs, weight tracking, and shedding history*

### Weight Tracking
![Weight Chart](docs/screenshots/weight-chart.png)
*Visual weight monitoring over time*

### Feeding Schedule
![Feeding Logs](docs/screenshots/feeding-logs.png)
*Track feeding schedules and dietary information*

## Features

**Reptile Collection Management**
- Maintain detailed profiles for each reptile (species, morph, age, gender)
- Upload and organize photos of your animals
- Quick access to all important care information

**Health Monitoring**
- Track weight changes over time with visual charts
- Record shedding cycles and patterns
- Monitor growth and development

**Care Tracking**
- Log feeding schedules and diet history
- Maintain enclosure cleaning records
- Set reminders for routine maintenance

**Multi-User Support**
- Secure user authentication
- Role-based access control
- Share collections with team members or family

## Getting Started

### Prerequisites

- **Docker Desktop** - [Download here](https://www.docker.com/products/docker-desktop)
- **Git** - [Download here](https://git-scm.com/downloads)

That's it! Docker handles all the technical dependencies automatically.

### Installation

**Step 1: Get the Code**
```bash
git clone https://github.com/gkontonis/reptile-management.git
cd reptile-management
```

**Step 2: Configure Environment**
```bash
cp .env.example .env
```

Open the `.env` file and set secure values:
- `POSTGRES_PASSWORD` - Choose a strong database password
- `JWT_SECRET` - Generate with: `openssl rand -base64 32`

**Step 3: Launch the Application**
```bash
docker-compose up -d
```

Docker will automatically download and configure everything needed. This may take a few minutes on first run.

**Step 4: Access the Application**

Open your browser and navigate to: **http://localhost:3000**

**Default Login:**
- Username: `admin`
- Password: `Admin123!`

⚠️ **Important:** Change the default password immediately after your first login.

### Usage

Once logged in, you can:
1. Add reptiles to your collection from the main dashboard
2. Click on any reptile to view detailed information
3. Log feeding events, weight measurements, and shedding cycles
4. View charts and track health trends over time
5. Upload photos to create a visual record

## Managing the Application

### Stopping the Application
```bash
docker-compose down
```

### Restarting the Application
```bash
docker-compose down
```

```bash
docker-compose up -d
```

### Viewing Logs
```bash
docker-compose logs -f
```

### Complete Reset (Remove All Data)
⚠️ This will delete all reptile data, user accounts, and records:
```bash
docker-compose down -v
```

## Configuration

If the default ports (3000, 8081, 5434) are already in use on your system, you can change them in the `.env` file:

```env
FRONTEND_PORT=3001
BACKEND_PORT=8082
POSTGRES_PORT=5435
```

After changing ports, restart the application with `docker-compose up -d`.

## Troubleshooting

**Application won't start**
- Ensure Docker Desktop is running
- Check that ports 3000, 8081, and 5434 are not in use
- Try: `docker-compose down && docker-compose up -d`

**Can't login**
- Verify containers are running: `docker-compose ps`
- Wait 30 seconds for services to fully start
- Check logs: `docker-compose logs backend`

**Changes not appearing**
- Hard refresh your browser (Ctrl+Shift+R or Cmd+Shift+R)
- Clear browser cache
- Restart containers: `docker-compose restart`

**Need more help?**
View detailed logs: `docker-compose logs -f`

## Technology Stack

Built with modern, reliable technologies:

- **Frontend:** Angular 21, TypeScript, Tailwind CSS
- **Backend:** Java 21, Spring Boot 4.0, Spring Security
- **Database:** PostgreSQL 17
- **Deployment:** Docker, Docker Compose, Nginx

## For Developers

Detailed development documentation is available in [DEVELOPMENT.md](DEVELOPMENT.md)

Key resources:
- API documentation at `http://localhost:8081/api`
- Database access via Docker: `docker exec -it reptile-management-db psql -U postgres -d reptilemanagement`

## License

MIT License - feel free to use this project for your own reptile collection management needs.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## Support

If you encounter any issues or have questions:
- Open an issue on GitHub
- Check existing issues for solutions
- Review the troubleshooting section above
