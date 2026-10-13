# Examen Práctico — Desarrollador Full Stack

**Duración estimada:** 1.5 – 2 horas

---

## Objetivo

Evaluar la capacidad del candidato para configurar, integrar y extender una aplicación full stack compuesta por un API REST en Spring Boot y una aplicación frontend en Angular, aplicando buenas prácticas de desarrollo, documentación y entrega.

---

## Entregables
- **No usar palaras que incluyan Chubb** debido al registro de la marca no podemos usar la palabra Chubb en git para referencia del examen, use palabras  genéricas como "test".
- **Repositorio Git** (GitHub, GitLab) con el código fuente completo y organizado.
- **Archivo `SOLUTION.md`** en la raíz del repositorio que documente:
  - Qué se implementó y cómo.
  - Decisiones técnicas tomadas (cambio de gestor de base de datos, estructura del proyecto, etc.).
  - Dificultades encontradas y cómo fueron resueltas.
  - Mejoras adicionales que podrían aplicarse con más tiempo.
- **API documentada con Swagger (OpenAPI)** — el API debe exponer la documentación interactiva en `/swagger-ui.html` o ruta equivalente.
- El repositorio debe estar **listo y funcional** al momento de la entrevista. Se revisará en vivo.

> El examen debe llegar **completamente respondido**. En la entrevista se revisará el código, se discutirán las decisiones tomadas y se podrán hacer ajustes en tiempo real.

---

## Parte 1 — Backend: Spring Boot (`com.chubb.test`)

### 1.1 Configuración del ambiente local

- Instalar Java y un IDE de su agrado (se sugiere **Spring Tool Suite — STS**).
- El proyecto `com.chubb.test` viene configurado para **SQL Server Express**.
- El candidato puede usar el gestor de base de datos SQL que prefiera: **MySQL, PostgreSQL, H2, u otro**. Si cambia de gestor deberá:
  - Actualizar la dependencia del driver en `pom.xml`.
  - Ajustar la cadena de conexión en `application.properties` / `application.yml`.
  - Verificar compatibilidad del dialecto de Hibernate si aplica.

### 1.2 Documentación del API con Swagger

- Integrar **SpringDoc OpenAPI** (o Springfox, según versión del proyecto) para generar documentación automática.
- Todos los endpoints deben estar documentados con descripción, parámetros y respuestas.
- Dependencia sugerida:

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.x.x</version>
</dependency>
```

- La documentación debe ser accesible en: `http://localhost:8080/swagger-ui.html`

### 1.3 Nuevo endpoint requerido

Agregar al API el endpoint para **eliminar un cliente**:

```
DELETE /clientes/{id}
```

- Debe retornar `204 No Content` si se eliminó correctamente.
- Debe retornar `404 Not Found` si el cliente no existe.
- Documentar el endpoint en Swagger.

---

## Parte 2 — Frontend: Angular (`my-app-exam`)

### 2.1 Conexión con el API

- Conectar el proyecto Angular `my-app-exam` con el API Spring Boot `com.chubb.test`.
- Si el API no está funcional localmente, se pueden usar **métodos dummy / mock** que simulen las respuestas del servidor.

> En la entrevista se utilizará un ambiente interno con los proyectos completamente funcionales y se conectará con el API real.

### 2.2 Funcionalidades a implementar en el formulario de clientes

| # | Requerimiento |
|---|---------------|
| 1 | Al intentar guardar un cliente, mostrar un **mensaje de error** indicando los campos obligatorios que faltan por completar. |
| 2 | **Marcar en rojo** los campos requeridos que no hayan sido completados al momento de guardar. |
| 3 | **Limpiar el formulario** automáticamente una vez que el cliente se haya guardado exitosamente. |
| 4 | **Agregar el campo Email** al formulario de captura. |
| 5 | **Validar que el teléfono sea numérico** (solo dígitos, sin letras ni caracteres especiales). |
| 6 | **Validar el formato del Email** (debe cumplir con el patrón estándar `usuario@dominio.com`). |
| 7 | **Agregar opción de eliminar** clientes directamente desde la tabla (consumir el nuevo endpoint `DELETE /clientes/{id}` creado en el API). |

---

## Parte 3 — Documentación (`SOLUTION.md`)

Al finalizar, incluir en la raíz del repositorio un archivo `SOLUTION.md` con la siguiente estructura:

```markdown
# Solución — Examen Práctico

## Qué se implementó
...

## Cómo se implementó
...

## Cambios en base de datos / configuración
...

## Dificultades encontradas
...

## Mejoras que podrían aplicarse
...
```

---

## Parte 4 — Entrega en Git

1. Crear un repositorio en la plataforma de su preferencia (GitHub, GitLab).
2. Hacer al menos **un commit por cada parte** del examen (backend, frontend, documentación) con mensajes descriptivos.
3. Compartir el **enlace al repositorio** antes de la entrevista.
4. El repositorio debe incluir:
   - Código fuente del API (`com.chubb.test`) renombrar paquete antes del commit
   - Código fuente del frontend (`my-app-exam`)
   - Archivo `SOLUTION.md` en la raíz
   - Un `README.md` con instrucciones para levantar el proyecto localmente

---

## Criterios de evaluación

| Criterio | Descripción |
|----------|-------------|
| **Funcionalidad** | Las validaciones y features requeridos funcionan correctamente. |
| **Calidad del código** | Código limpio, organizado y legible. |
| **Documentación** | Swagger completo y `SOLUTION.md` claro y honesto. |
| **Entrega en Git** | Historial de commits ordenado y repositorio bien estructurado. |
| **Criterio técnico** | Capacidad para explicar las decisiones tomadas durante la entrevista. |

---

## Notas finales

- No hay una solución "perfecta" esperada — se valora el proceso, las decisiones tomadas y la capacidad de argumentarlas.
- Si algo no se pudo completar, documentarlo en `SOLUTION.md` con la razón y cómo se abordaría con más tiempo.
- El uso de IA generativa está permitido; lo que se evaluará es el entendimiento del código entregado.
