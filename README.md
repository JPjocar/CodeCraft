# CodeCraft

API REST tipo StackOverflow: preguntas, respuestas, comentarios, votos y tags.

Spring Boot 3.4.2 · Java 21 · PostgreSQL · JWT

---

## Configuración requerida

La aplicación **no arranca sin estas variables de entorno**. Es intencionado:
antes la clave de firma JWT y la contraseña de la base de datos estaban escritas
en `application.properties` y versionadas en Git.

| Variable | Obligatoria | Por defecto | Descripción |
|---|:---:|---|---|
| `JWT_SECRET` | **Sí** | — | Clave HMAC de firma. Mínimo 32 caracteres. |
| `DB_PASSWORD` | **Sí** | — | Contraseña de PostgreSQL. |
| `DB_HOST` | No | `localhost` | |
| `DB_PORT` | No | `5432` | |
| `DB_NAME` | No | `codecraftdb` | |
| `DB_USERNAME` | No | `postgres` | |
| `JWT_ISSUER` | No | `AUTH-COSMOS` | Emisor del token. |
| `UPLOADS_DIR` | No | `./uploads` | Carpeta de imágenes subidas. |
| `UPLOADS_PUBLIC_URL` | No | `http://localhost:8080/uploads` | Base pública de las imágenes. |
| `CORS_ALLOWED_ORIGINS` | No | `http://localhost:4200` | Orígenes permitidos, separados por coma. |
| `LOG_LEVEL_SECURITY` | No | `INFO` | Ponlo en `DEBUG` solo para depurar. |
| `LOG_LEVEL_WEB` | No | `INFO` | |

### Generar una clave JWT

```bash
openssl rand -hex 32
```

### Arrancar (PowerShell)

```powershell
$env:JWT_SECRET = "<clave de 64 caracteres hex>"
$env:DB_PASSWORD = "<tu contraseña de postgres>"
.\mvnw spring-boot:run
```

### Arrancar (bash)

```bash
export JWT_SECRET="<clave de 64 caracteres hex>"
export DB_PASSWORD="<tu contraseña de postgres>"
./mvnw spring-boot:run
```

En IntelliJ / NetBeans se definen en la configuración de ejecución, en
*Environment variables*.

### Tests

`src/test/resources/application.properties` trae valores propios, así que la
suite corre sin definir nada:

```bash
./mvnw test
```

Ese archivo **sustituye** al de `src/main/resources` (mismo nombre en el
classpath), no se fusiona con él: cualquier propiedad nueva de la aplicación hay
que añadirla también allí.

Los tests de integración necesitan un PostgreSQL accesible y usan los IDs fijos
de `import.sql`.

---

## Avisos

- `spring.jpa.hibernate.ddl-auto=create` **borra y recrea el esquema en cada
  arranque**. Vale para desarrollo; antes de producción hay que migrar a
  Flyway/Liquibase y cambiarlo a `validate`.
- La clave JWT que estuvo versionada en texto plano se considera comprometida.
  No la reutilices.

---

## Autenticación

```
POST /auth/sign-up    → crea usuario (siempre ROLE_USER) y devuelve token
POST /auth/log-in     → devuelve token
```

El token va en la cabecera `Authorization: Bearer <token>` y caduca a los 30 minutos.

Los roles **no** se piden en el registro. Solo un administrador puede concederlos:

```
PUT /admin/users/{username}/roles     (requiere ROLE_ADMIN)
Body: { "roles": ["MODERATOR"] }
```
