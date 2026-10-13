# Changelog de cambios importantes

Resumen de los cambios relevantes incluidos en el commit `318dea7`
("Reconecta el repo tras la reorganizacion de carpetas"), documentados
como entradas individuales ya que no se separaron en commits distintos.

## 1. Renombrado del modulo backend

`com.chubb.test` -> `com.practice-clientapi`

- Paquetes Java movidos de `com.chubb.test` a `com.practice.clientapi`.
- `groupId` del `pom.xml` actualizado de `com.chubb.api` a `com.practice-clientapi.api`.
- Clases afectadas: `TestApplication`, `ClientController`, `Client`,
  `ClientService`, `IClientService`, `TestApplicationTests`.

## 2. Migracion de SQL Server a PostgreSQL

**Archivos:** `pom.xml`, `application.properties`, `docker-compose.yml`

- Dependencia `com.microsoft.sqlserver:mssql-jdbc` reemplazada por
  `org.postgresql:postgresql` (`42.7.4`).
- `application.properties`: nuevo `spring.datasource.url` apuntando a
  `jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:test}`
  con driver `org.postgresql.Driver` y credenciales parametrizadas por
  variables de entorno (`DB_USER`, `DB_PASSWORD`).
- `spring.sql.init.mode=always` para ejecutar `schema.sql` en cada arranque.
- `docker-compose.yml`: nuevo servicio `db` con imagen `postgres:16-alpine`,
  healthcheck (`pg_isready`) y volumen persistente `clientapi-db-data`;
  el servicio `app` ahora depende de `db` (`condition: service_healthy`).

## 3. Contrato API-first con OpenAPI

**Archivos:** `pom.xml`, `src/main/resources/api/clientapi.yaml`

- Se agrega `openapi-generator-maven-plugin` (`7.9.0`) para generar
  interfaces y DTOs (`ClientApi`, `ClientDto`, `ServiceResponse`) a partir
  de `clientapi.yaml` en la fase `generate-sources`.
- Se agregan `springdoc-openapi-starter-webmvc-ui` (Swagger UI),
  `jackson-databind-nullable` y `swagger-annotations`.
- `ClientController` ahora implementa la interfaz generada `ClientApi`
  en lugar de exponer los endpoints manualmente.

## 4. Implementacion del endpoint de borrado (DELETE)

**Archivos:** `ClientController.java`, `ClientService.java`,
`IClientService.java`, `ClientRepository.java`, `IClientRepository.java`

- Nuevo endpoint `DELETE` vía `ClientApi.deleteClient(Integer id)`:
  responde `204 No Content` si se elimina, `404 Not Found` si el
  cliente no existe.
- `IClientService.deleteById(int id)` / `ClientService.deleteById`
  delegan en `IClientRepository.deleteById`.
- Documentado con anotaciones `@Operation` / `@ApiResponse` (Swagger).

## 5. Contenerizacion de ambos modulos

- Nuevo `Dockerfile` y `.dockerignore` en `com.practice-clientapi`
  (backend) y en `my-app-exam` (frontend), más `nginx.conf` para servir
  el build de Angular.

## 6. Limpieza de archivos que no deben versionarse

- Se eliminan del repo la metadata de workspace de Eclipse
  (`com.chubb.test/.metadata/**`) y se agrega `.metadata` al
  `.gitignore` del backend.
- Se agrega `.claude/` al `.gitignore` (archivos de estado de
  herramientas locales).
- Se elimina el `.gitignore` raíz anterior junto con los documentos de
  examen (`*.docx`, `ExamDatabaseObjects.sql`, `examen-practico.md`) y
  se crea un `.gitignore` raíz nuevo (`.idea/`, `*.log`, `.claude/`).
