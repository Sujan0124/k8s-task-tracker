# k8s-task-tracker

A beginner-friendly **Spring Boot REST API** for tracking tasks, built with Java 17 and designed for Kubernetes deployment.

---

## 📁 Project Structure

```
k8s-task-tracker/
├── pom.xml                          # Maven build config & dependencies
├── Dockerfile                       # Multi-stage Docker build
├── .dockerignore                    # Files excluded from Docker context
├── README.md                        # You are here
└── src/
    ├── main/
    │   ├── java/com/example/tasktracker/
    │   │   ├── K8sTaskTrackerApplication.java   # App entry point
    │   │   ├── model/
    │   │   │   └── Task.java                    # Task data model
    │   │   ├── service/
    │   │   │   └── TaskService.java             # In-memory task storage
    │   │   └── controller/
    │   │       └── TaskController.java          # REST endpoints
    │   └── resources/
    │       └── application.properties           # Spring Boot config
    └── test/
        └── java/com/example/tasktracker/
            ├── controller/
            │   └── TaskControllerTest.java      # Controller unit tests
            └── service/
                └── TaskServiceTest.java         # Service unit tests
```

### What each file does

| File | Purpose |
|------|---------|
| `pom.xml` | Declares dependencies (Spring Web, Actuator, Test) and the Java 17 compiler target. |
| `K8sTaskTrackerApplication.java` | Bootstraps the Spring Boot app — component scanning, auto-configuration, embedded Tomcat. |
| `Task.java` | Plain Java object (POJO) with `id`, `title`, and `completed` fields. |
| `TaskService.java` | Stores tasks in a thread-safe `ArrayList`. Generates unique IDs with `AtomicLong`. |
| `TaskController.java` | Maps HTTP requests to service methods. `GET` lists tasks; `POST` creates one. |
| `application.properties` | Enables the `/actuator/health` endpoint for Kubernetes readiness/liveness probes. |
| `Dockerfile` | Two-stage build: compiles with Maven, then runs on a slim JRE Alpine image (~80 MB smaller). |

---

## 🛠 Prerequisites

- **Java 17** (JDK) — [Download](https://adoptium.net/)
- **Maven 3.8+** — [Download](https://maven.apache.org/download.cgi)
- **Docker** (optional) — [Download](https://www.docker.com/products/docker-desktop/)

---

## 🚀 Build & Run

### Option 1 — Run locally with Maven

```bash
# Clone and enter the project
cd k8s-task-tracker

# Build (compiles + runs tests)
mvn clean package

# Run the application
java -jar target/k8s-task-tracker-0.0.1-SNAPSHOT.jar
```

The server starts on **http://localhost:8080**.

### Option 2 — Run with Docker

```bash
# Build the Docker image
docker build -t k8s-task-tracker:latest .

# Run a container
docker run -p 8080:8080 k8s-task-tracker:latest
```

---

## 🧪 Run Tests

```bash
mvn test
```

You should see output like:

```
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0  (TaskControllerTest)
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0  (TaskServiceTest)
```

---

## 📡 API Reference

### Health Check

```bash
curl http://localhost:8080/actuator/health
```

Response:
```json
{
  "status": "UP",
  "components": {
    "diskSpace": { "status": "UP" },
    "ping": { "status": "UP" }
  }
}
```

### List all tasks

```bash
curl http://localhost:8080/api/tasks
```

Response (empty at first):
```json
[]
```

### Create a task

```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"title": "Learn Spring Boot", "completed": false}'
```

Response:
```json
{
  "id": 1,
  "title": "Learn Spring Boot",
  "completed": false
}
```

### Verify it was saved

```bash
curl http://localhost:8080/api/tasks
```

Response:
```json
[
  {
    "id": 1,
    "title": "Learn Spring Boot",
    "completed": false
  }
]
```

---

## 📝 Notes

- **No database** — tasks are stored in memory and reset on restart. This keeps the project simple for learning.
- **Thread-safe** — the service uses `Collections.synchronizedList` and `AtomicLong` so concurrent requests are handled safely.
- **Kubernetes-ready** — the `/actuator/health` endpoint can be used as a liveness/readiness probe in K8s pod specs.
