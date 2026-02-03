#!/bin/bash

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${BLUE}Starting Reptile Management System...${NC}\n"

# Run docker-compose up with minimal output
docker-compose up -d --remove-orphans 2>&1 | grep -E "(Creating|Starting|Created|Started|Recreating|Pulling|Building)" || true

echo ""
echo -e "${YELLOW}Waiting for containers to be healthy...${NC}"

# Wait for postgres to be healthy
max_attempts=30
attempt=0
while [ $attempt -lt $max_attempts ]; do
    if docker inspect --format='{{.State.Health.Status}}' reptile-management-db 2>/dev/null | grep -q "healthy"; then
        break
    fi
    attempt=$((attempt + 1))
    sleep 1
    echo -n "."
done
echo ""

# Check if all containers are running
if docker ps --filter "name=reptile-management" --format "{{.Names}}" | grep -q "reptile-management"; then
    running_count=$(docker ps --filter "name=reptile-management" --format "{{.Names}}" | wc -l)
    if [ $running_count -eq 3 ]; then
        echo -e "${GREEN}All containers started successfully!${NC}\n"

        # Display the ASCII art
        cat << 'EOF'
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢠⣦⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣠⣤⣶⠶⠿⠛⠛⠛⠛⠛⣿⠛⢳⣶⣶⣤⣄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣴⡾⡏⢉⡿⠃⠌⠀⠀⠀⠀⣀⣴⣾⣿⣿⣿⣿⣿⣿⣿⣿⣶⣄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⢠⣾⣯⣤⣤⣼⣥⣶⣶⣶⣶⣶⣿⣿⣿⣿⣿⡿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠘⠻⠿⣿⣿⡇⢻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠳⣿⣿⣿⣿⣿⣿⣿⣿⠿⢿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⠃⠈⣿⡍⣉⣉⣻⣿⣿⣿⣿⡇⠀⢹⡏⠉⠛⠛⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢻⣷⠘⣿⣿⣯⠉⠙⠛⣋⣿⢻⠇⢸⣿⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⣶⠿⠋⠀⠀⠀⠉⠙⠒⠚⠛⣿⠛⠀⠈⠛⢿⣦⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⣴⡿⠃⢀⡤⠤⣀⠀⠀⠀⠀⠀⠀⣿⠀⣀⡤⢄⡀⠻⣷⡄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⢶⣶⣦⣤⣤⣼⡿⠁⠠⡏⠸⠗⢸⡆⠀⣤⣴⢤⣄⣿⠰⡇⣿⠆⡷⠀⠹⣿⣄⣀⣀⣤⣤⡄⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⣀⣈⣽⡟⠙⡓⡦⠙⠒⠒⠋⠀⠀⠈⠉⠈⠁⣿⠀⠙⠒⠚⢡⣴⣒⣻⣿⣯⣭⣭⣭⣅⣀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠾⠟⠛⢛⣿⣟⣉⡩⠭⠗⠲⢤⡀⠀⠀⠀⠀⠀⠀⠀⣿⠀⠀⠀⠀⠰⠮⠥⢬⣿⣧⣀⣀⠀⠀⠉⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⣀⣤⣶⣿⡟⠉⠀⠀⠀⠀⠀⠀⠈⠳⣄⠀⠀⠀⠛⢸⣿⣂⣀⣀⣀⠀⠀⠀⠀⠸⣿⡝⠛⠻⠷⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠙⠋⣾⡟⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣬⣧⠤⣿⣧⣼⣿⡟⠉⠈⠉⠉⠛⠲⢦⡄⢻⣷⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⣼⡟⠀⠀⠀⠀⠀⢀⣤⡶⠛⠉⠁⠀⣀⠐⠿⣷⣼⣿⡀⠀⠀⠀⠀⠀⠀⠀⠀⠈⣿⣇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⣸⡿⠀⠀⡴⠀⢀⣴⢟⣵⣲⣄⠀⢠⣾⠿⢷⠈⢆⠰⠟⢷⡀⠀⠀⠀⠀⠀⠀⠀⠀⠸⣿⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⢰⣿⠃⠀⣸⠃⣠⡾⠁⠘⠉⠀⠁⠀⠈⠀⠀⠀⠀⠈⢦⠀⠈⢻⣄⠀⠀⠀⠀⠀⠀⠀⠀⣿⣇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⣾⡏⠀⠀⡟⠀⡿⠁⠀⠀⠀⠀⠀⠀⣀⡀⠀⠀⢠⢖⡺⣄⠀⠀⢙⣧⡀⠀⠀⠀⠀⠀⠀⢸⣿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⢀⣿⠃⠀⢸⠇⢸⠃⣠⡶⣄⠀⠀⢠⣞⠧⢝⡆⠀⠛⠉⠉⠹⡀⠰⠟⠺⣿⢦⣄⠀⠀⠀⠀⢸⣿⡆⠀⠀⠀⠀⠀⠀⠀⠀⠀
⢸⡿⠀⠀⣾⠀⣿⠸⠟⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡇⠀⠀⠀⠀⠈⠙⢳⣦⣄⡀⠀⣿⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀
⢸⡇⠀⠀⣿⢀⡿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠙⠀⠀⠀⠀⠀⠀⠀⠁⠈⣿⠀⣿⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀
⢸⣇⠀⣰⡟⢸⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡿⠀⣿⠇⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠸⣿⡔⢣⡇⢸⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡇⠀⣿⡆⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⣿⣿⣿⣇⠸⣧⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⡇⢰⣿⠃⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠘⠛⢿⣿⠀⢿⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣾⠁⣸⣿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠘⣿⡄⠘⣧⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢠⠏⠀⠀⠀⠀⠀⠀⠀⠀⣼⠃⢠⣿⠇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⢻⣷⠀⠘⢷⣄⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡴⠁⠀⠀⠀⠀⠀⠀⠀⣠⡾⠃⢀⣾⡟⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⢻⣧⡀⠀⠙⢷⣄⠀⠀⠀⠀⠀⠀⠒⠉⠀⠀⠀⠀⠀⠀⠀⣠⡾⠋⠀⢠⣾⠟⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠻⣷⣄⠀⣠⠏⠻⠶⣤⣀⡀⠀⠀⠀⠀⠀⢀⣀⣤⡴⠟⠉⠀⣀⣴⡿⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠈⣿⣿⣿⣾⣦⣄⣀⣈⣉⣭⣿⣿⣛⣉⣉⣉⣀⣠⣤⣶⣶⣿⣿⣤⣤⣄⣀⣀⣀⣀⣤⣤⣤⣤⣤⣤⣤⡄
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠉⠛⠛⠛⠛⠛⠻⠿⠻⠿⠿⠟⠿⠟⠿⠿⠿⠿⠟⠛⠛⠛⠛⠛⠛⠉⠈⠉⠁
EOF

        echo ""
        echo -e "${GREEN}Containers running:${NC}"
        docker ps --filter "name=reptile-management" --format "  - {{.Names}} ({{.Status}})"
        echo ""
        echo -e "${BLUE}Access your application:${NC}"
        echo -e "  Frontend: http://localhost:${FRONTEND_PORT:-3000}"
        echo -e "  Backend:  http://localhost:${BACKEND_PORT:-8081}"
        echo ""
    else
        echo -e "${YELLOW}Warning: Only $running_count/3 containers are running${NC}"
        docker ps --filter "name=reptile-management" --format "{{.Names}}: {{.Status}}"
    fi
else
    echo -e "${YELLOW}Warning: Containers may not have started properly${NC}"
    docker ps --filter "name=reptile-management"
fi
