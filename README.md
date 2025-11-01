# Task Service

Task management microservice with workflow automation and assignment capabilities.

## Quick Start

### Prerequisites
- Docker & Docker Compose
- External network: `dev_flowforge_network`

### Setup Network (One-Time)
```bash
docker network create --driver=bridge --subnet=81.12.0.0/16 dev_flowforge_network
```

### Build & Run
```bash
docker-compose -f ./docker-compose.yml up -d
```

### Stop
```bash
docker-compose -f ./docker-compose.yml down
```

---

## Endpoints

| Endpoint | URL |
|----------|-----|
| API | http://localhost:2102 |
| Swagger UI | http://localhost:2102/swagger-ui.html |
| API Docs | http://localhost:2102/api-docs |
| Health | http://localhost:2102/actuator/health |
| H2 Console | http://localhost:2102/h2-console |

### H2 Console Login
- **JDBC URL**: `jdbc:h2:mem:taskflowdb`
- **Username**: `sa`
- **Password**: (empty)

---

## Domain Model

### User
Manages user information and authentication.
- User ID, name, email, phone
- Status (ACTIVE/INACTIVE)
- Roles and permissions
- Country, language preferences

### Task
Core task entity with workflow integration.
- Title, description, notes
- Status (TODO, IN_PROGRESS, IN_REVIEW, DONE, CANCELLED)
- Priority (LOW, MEDIUM, HIGH, URGENT)
- Assignee and workflow tracking
- Due dates and completion timestamps

### Workflow
Defines task state machines and transitions.
- Name, description
- Multiple workflow states
- Active/inactive status

### WorkflowState
Individual states within a workflow.
- Associated workflow
- Task status
- Assignee requirements
- Allowed next states

### Assignment
Tracks task assignments.
- Task and assignee IDs
- Assignment strategy (MANUAL, AUTO, ROUND_ROBIN, LOAD_BASED)
- Assignment reason and timestamp
- Assigned by user

---

## Configuration

### Service Details
- **Port**: 2102
- **Network**: dev_flowforge_network (81.12.4.13)
- **Database**: H2 in-memory
- **Profile**: dev

### Environment Variables
```bash
SPRING_PROFILES_ACTIVE=dev
SPRING_DATASOURCE_URL=jdbc:h2:mem:taskflowdb
JAVA_OPTS=-Xmx512m -Xms256m
```

---

## Technology Stack
- Spring Boot 3.5.7
- Spring Data JPA
- Spring Security + JWT
- H2 Database
- Swagger/OpenAPI
- Prometheus Metrics
- Docker

---

## API Features

### Task Management
- Create, read, update, delete tasks
- Assign tasks to users
- Track task status and progress
- Set priorities and due dates

### Workflow Management
- Define custom workflows
- Configure workflow states
- Set state transitions
- Enforce workflow rules

### Assignment Management
- Manual task assignment
- Auto-assignment strategies
- Assignment history tracking

---

## Development

### View Logs
```bash
docker-compose -f ./docker-compose.yml logs -f
```

### Rebuild
```bash
docker-compose -f ./docker-compose.yml down
docker-compose -f ./docker-compose.yml up -d --build
```

### Check Health
```bash
curl http://localhost:2102/actuator/health
```

---

## Security
- JWT-based authentication
- Token expiration: 24 hours
- Refresh token: 30 days
- CORS enabled for development

---

## Monitoring
- **Health**: `/actuator/health`
- **Metrics**: `/actuator/metrics`
- **Prometheus**: `/actuator/prometheus`

---

## Author
Nandom Gusen
