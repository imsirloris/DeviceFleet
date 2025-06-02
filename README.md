# Device Fleet

Device Fleet is a REST API designed for registering and managing devices.

---

### Languages & Frameworks

* **Java 21**
* **Gradle 8**
* **Spring Boot** (Spring Web, Spring Data MongoDB)

### Database

* **MongoDB**

### Containerization

* **Docker**
* **Docker Compose**

### Testing

* **JUnit 5**
* **Mockito**
* **JaCoCo**

### Architecture

* **Hexagonal Architecture**
* **SOLID principles**
* **Clean Code**

### Environment Requirements

* Java 21
* Gradle 8.14
* MongoDB running locally or in Docker
* Docker (optional, for containerization)
* Docker Compose (optional, for orchestrating containers)

---

## Environment Setup

1. **Clone the repository**

   ```bash
   git clone git@github.com:imsirloris/DeviceFleet.git
   cd DeviceFleet
   ```

2. **Create a `.env` file** in the project root:

   ```env
   MONGO_USERNAME= # MongoDB username
   MONGO_PASSWORD= # MongoDB password
   ```

3. **Run** 
   - **without Docker** (be sure MongoDB is up):

   ```bash
   ./gradlew build
   ./gradlew bootRun
   ```

   - **with Docker** (requires Docker & Docker Compose):

   ```bash
   docker-compose up --build
   ```

4. The API will be available at **[http://localhost:8080/](http://localhost:8080/)**

5. Swagger UI can be accessed at **[http://localhost:8080/swagger-ui/](http://localhost:8080/swagger-ui/)**

---

### Endpoint Reference

| Method   | URL                                                     | Description                           |
| -------- | ------------------------------------------------------- |---------------------------------------|
| `POST`   | `/v1/api/devices/create`                                | Create a new device                   |
| `GET`    | `/v1/api/devices/fetchAll`                              | List all devices                      |
| `GET`    | `/v1/api/devices/fetchById?id={id}`                     | Find device by ID                     |
| `GET`    | `/v1/api/devices/fetchBy?brand={brand}&status={status}` | Filter devices by brand and/or status |
| `PUT`    | `/v1/api/devices/update`                                | Full/Partial update of a device       |
| `PATCH`  | `/v1/api/devices/updateStatus?id={id}&status={status}`  | Partial update (status only)          |
| `DELETE` | `/v1/api/devices/delete?id={id}`                        | Delete a device                       |

### Sample Payloads

**Create (`POST /create`)**

```json
{
  "name": "iPhone 15 Pro",
  "brand": "Apple",
  "status": "AVAILABLE"
}
```

**Update (`PUT /update`)**

```json
{
  "id": "664d40e5d90a9e2ba03d6300",
  "name": "iPhone 15 Pro Max",
  "brand": "Apple",
  "status": "IN_USE"
}
```

---

## Business Rules & Validation

1. `createdTime` cannot be modified after creation.
2. `name` and `brand` **cannot** be changed while the device status is `IN_USE`.
3. Devices in `IN_USE` cannot be deleted.
4. Allowed status values: `AVAILABLE`, `IN_USE`, `INACTIVE`.

---

### Tests & Coverage

```bash
./gradlew test
```

* HTML report generated at **build/reports/jacoco/test/html/index.html**

---

### TODO

* [ ] Implement authentication & authorization
* [ ] Add Testcontainers for integration tests
* [ ] Introduce caching to improve performance
* [ ] Set up CI/CD pipeline with GitHub Actions
