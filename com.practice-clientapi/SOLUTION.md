# SOLUTION.md

## 1. Qué se implementó y cómo

El objetivo fue convertir el proyecto a un enfoque **API-first** y dejarlo corriendo contra **PostgreSQL** en Docker.

### 1.1 Contrato OpenAPI (API-first)
- Se creó `src/main/resources/api/clientapi.yaml` con el contrato OpenAPI 3.0.3 de los 3 endpoints del servicio:
  - `GET /api/clients` — lista todos los clientes.
  - `POST /api/client` — crea un cliente.
  - `DELETE /api/clients/{id}` — elimina un cliente por id (`204`/`404`).
- Se agregó el plugin **`openapi-generator-maven-plugin`** en `pom.xml`, ejecutado en la fase `generate-sources`, que genera a partir de ese YAML:
  - La interfaz `com.practice.clientapi.generated.api.ClientApi` (con las anotaciones `@RequestMapping` de Spring ya resueltas).
  - Los modelos `com.practice.clientapi.generated.model.ClientDto` y `ServiceResponse`.
- `ClientController` pasó de declarar sus propios `@GetMapping`/`@PostMapping` a hacer `implements ClientApi` y solo sobrescribir (`@Override`) los métodos `getClients`, `saveClient` y `deleteClient`. Esto garantiza que el código **no puede desviarse del contrato**: si el YAML cambia, el controller deja de compilar hasta que se actualice.
- Se eliminaron los DTOs manuales (`model/dtos/ClientDto.java`, `ServiceResponse.java`) porque ahora los genera el plugin.
- Se agregó **springdoc-openapi** (`springdoc-openapi-starter-webmvc-ui`) para exponer Swagger UI en runtime (`/swagger-ui.html`), útil para explorar y probar la API ya corriendo.

### 1.2 Base de datos
- Se mantiene `spring-boot-starter-data-jdbc` + `JdbcTemplate` (no se migró a JPA/Hibernate pese a que `spring-boot-starter-data-jpa` sigue en el `pom.xml` como dependencia heredada del proyecto original).
- Se corrigió `schema.sql`, que estaba escrito en sintaxis T-SQL (SQL Server: `AUTO_INCREMENT`, `PRIMARY KEY CLUSTERED ... WITH (...) ON PRIMARY`) y no era válido en Postgres. Se reescribió a sintaxis estándar de PostgreSQL (`SERIAL PRIMARY KEY`).
- Se corrigieron dos bugs en `ClientRepository` que habrían fallado contra una base real:
  - El `INSERT` no indicaba la lista de columnas y enviaba 4 valores contra una tabla de 5 columnas.
  - El `DELETE` apuntaba a la tabla `client` (singular) cuando la tabla real es `clients`.

### 1.3 Docker
- El `Dockerfile` ya existía (build multi-stage con Maven + JRE Alpine) y no requirió cambios.
- Se agregó `docker-compose.yml` con dos servicios:
  - `db`: PostgreSQL 16 Alpine, con volumen persistente y `healthcheck` (`pg_isready`).
  - `app`: build del `Dockerfile`, con `depends_on: db` condicionado a que la base esté healthy, y las variables de entorno (`DB_HOST=db`, etc.) que `application.properties` ya esperaba.

### 1.4 Documentación para pruebas manuales
- `curls.md`: cURL de los 3 endpoints.
- `postman_collection.json`: colección Postman (v2.1) equivalente, importable directo en Postman.

## 2. Decisiones técnicas

- **Cambio de motor de base de datos a PostgreSQL**: el proyecto original tenía `schema.sql` en sintaxis de SQL Server pero el driver y la configuración (`application.properties`) ya apuntaban a `org.postgresql.Driver`. Se decidió alinear todo a Postgres (en vez de volver a SQL Server) porque es el motor que la configuración y el `docker-compose` realmente usan, y es el más simple de levantar en un contenedor para pruebas locales.
- **`interfaceOnly=true` en el generador OpenAPI**: se eligió generar solo la interfaz (no una implementación default ni un controller completo) para que el controller existente la implemente explícitamente. Esto deja clara la separación entre "contrato" (generado, no se edita a mano) e "implementación" (`ClientController`, mantenido a mano).
- **Versiones de dependencias explícitas**: se fijó versión a `postgresql`, `swagger-annotations`, `springdoc`, `jackson-databind-nullable`, etc., porque no todas están en el BOM de `spring-boot-starter-parent:3.0.6` y Maven fallaba al resolver el POM sin una versión explícita.
- **No se migró el dominio a JPA**: se mantuvo `JdbcTemplate` con SQL explícito para minimizar el radio de cambio sobre un proyecto que ya tenía su propia capa `service`/`repository` funcionando; migrar a JPA/Entities se deja como mejora futura (ver sección 4).

## 3. Dificultades encontradas y cómo se resolvieron

- **`pom.xml` con dependencias mal declaradas**: varias dependencias (`swagger-annotations`, `postgresql`, el propio `spring-boot-starter-data-jdbc`) estaban sin versión o con una versión incompatible con el parent (`spring-boot-starter-data-jdbc` estaba fijada en `4.1.1`, que es una versión de Spring Boot 4.x, mientras el proyecto usa el parent `3.0.6`). Esto generaba desde errores de resolución del POM (`'dependencies.dependency.version' ... is missing`) hasta choques de beans duplicados en tiempo de arranque (`BeanDefinitionOverrideException` sobre `hikariPoolDataSourceMetadataProvider`). Se resolvió fijando las versiones faltantes y quitando la versión incorrecta de `spring-boot-starter-data-jdbc` para que la tomara del BOM del parent.
- **`mvn clean` fallando por rutas con caracteres especiales**: al ejecutar Maven desde una ruta de carpeta con `&` y espacios, el build no se ejecutaba correctamente (el shell interpretaba el `&` como separador de comandos en vez de parte de la ruta). Se resolvió invocando Maven con la ruta completa entre comillas y usando el wrapper (`mvnw`) desde un shell que respeta comillas correctamente (Git Bash), evitando que el `&` cortara el comando a la mitad.
- **Errores de sintaxis preexistentes en el código**: `ClientRepository.deleteById` tenía una línea sin `;` que impedía compilar, y el `INSERT`/`DELETE` tenían los bugs de SQL ya mencionados en la sección 1.2. Se detectaron al correr `mvnw clean compile`/`test` y se corrigieron directamente.
- **Validación sin base de datos disponible**: el test de contexto (`TestApplicationTests`) fallaba porque no había un Postgres corriendo en `localhost:5432`. Se resolvió levantando un contenedor Postgres desechable (`docker run postgres:16-alpine`) solo para validar, confirmando que el error no era de código sino de entorno.

## 4. Mejoras adicionales con más tiempo

- **Seguridad**: no hay autenticación ni autorización — se podría agregar Spring Security (OAuth2/JWT) y declarar los esquemas de seguridad directamente en `clientapi.yaml` para que el generador OpenAPI los propague a la interfaz y a Swagger UI.
- **Refactor del dominio a Java puro**: actualmente el modelo `Client` y el flujo `service`/`repository` dependen de anotaciones de Spring/Lombok y de `JdbcTemplate` mezclado con lógica de negocio. Con más tiempo se separaría el dominio (entidad `Client`, reglas de negocio) de la infraestructura (Spring, JDBC), dejando el dominio como Java plano sin dependencias de framework, y moviendo el mapeo a adaptadores explícitos (puertos/adaptadores o un mapper con MapStruct, que ya está como dependencia pero no se usa).
- **Migrar de `JdbcTemplate` a JPA o un mapeo automático**: hoy el mapeo `Client` ↔ `ClientDto` es manual línea por línea; se podría usar MapStruct (ya está en el `pom.xml`) para generarlo, o pasar a Spring Data JPA con entidades.
- **Validación de entrada**: el `ClientDto` generado soporta `@Valid`/`jakarta.validation`, pero el YAML no declara restricciones (`required`, longitudes, formatos) — se podrían agregar para que el generador produzca validación automática en el controller.
- **Tests**: solo existe el test de contexto (`TestApplicationTests`). Faltan tests unitarios de `ClientService`/`ClientRepository` y tests de integración de los 3 endpoints (por ejemplo con Testcontainers para Postgres, en vez de depender de una base externa).
- **CI**: no hay pipeline; se podría agregar un workflow que levante `docker-compose`, corra `mvnw test` y valide el build de la imagen en cada push.

## 5. Cómo correr el proyecto en Docker

Requisitos: Docker y Docker Compose instalados.

1. **Levantar la base de datos y el microservicio juntos** (construye la imagen si no existe):
   ```bash
   docker compose up --build -d
   ```
2. **Ver logs del microservicio** (confirmar que arrancó bien y conectó a Postgres):
   ```bash
   docker compose logs -f app
   ```
3. La API queda disponible en `http://localhost:8080` (ej. `GET http://localhost:8080/api/clients`) y Swagger UI en `http://localhost:8080/swagger-ui.html`. Postgres queda expuesto en `localhost:5432` (user/password/db = `test`).
4. **Parar los servicios** (conserva los datos de Postgres en el volumen `clientapi-db-data`):
   ```bash
   docker compose down
   ```
5. **Parar y borrar también los datos** de la base (reinicio limpio):
   ```bash
   docker compose down -v
   ```
6. **Reconstruir solo el microservicio** después de un cambio de código, sin tocar la base ya levantada:
   ```bash
   docker compose up --build -d app
   ```

Las pruebas manuales de los endpoints están en `curls.md` y en `postman_collection.json` (importable directo en Postman).
