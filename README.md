

# FilmFeel aplicación web (en desarrollo)

> **Nota**: *Esta aplicación web está actualmente en una fase de desarrollo, por lo que algunas funcionalidades pueden estar en proceso de ajuste y es posible que se presente algún error. Se están realizando mejoras de forma continua para aumentar la estabilidad y añadir nuevas características.

**Tabla de contenidos**

[TOCM]

[TOC]


## 1. Descripción

FilmFeel es una aplicación web desarrollada en Java con Spring Boot que permite a los usuarios gestionar un catálogo cinematográfico de forma colaborativa e interactiva.

Los usuarios pueden:

- **Registrar nuevas películas** con  título, sinopsis, año de estreno, duración y portada.

-** Asociar personas del mundo del cine** a cada película: actores, directores, fotógrafos, guionistas y músicos, quienes también se registran en el sistema.

- **Dejar puntuaciones** (de 1 a 5 estrellas) y escribir reseñas personales 

- **Acceder a un sistema de autenticación y autorización con JWT**, con roles diferenciados (USER y ADMIN).

-** Visualizar las vistas web mediante Thymeleaf**

- Explorar la documentación interactiva de la API a través de **Swagger UI**.


## 2. Características principales

### 🎬 Gestión de Películas

- **CRUD **completo de películas accesible para usuarios con rol `ADMIN`.
- **Atributos**: título, sinopsis, año, duración y cartel (en local).
-** Asociación de profesionales del cine** (actores, músicos, fotógrafos, guionistas, directores) a cada película.
- **Visualización de ficha técnica** con reparto, equipo y críticas.


---

### 🧑‍💼 Sistema de Autenticación y Autorización

- Inicio de sesión y registro con **tokens JWT**.
- Seguridad implementada con **Spring Security**.
- Dos roles:
  - `USER`: puede puntuar y comentar películas.
  - `ADMIN`: acceso completo al sistema, también creación de nuevos administradores.

---

### 🌐 API REST con Seguridad JWT

-** Endpoints protegidos** con token JWT (`/auth/**`, `/api/**`).
- **Registro y login** vía JSON.
-** Endpoints REST **para crear y listar críticas de películas.

---

### 💬 Sistema de Reviews y Puntuaciones

- Cada usuario puede dejar **una única crítica y puntuación** por película.
- **Críticas gestionadas mediante API REST **y mostradas en la web.
- **Promedio de puntuaciones** calculado y visible para cada película.
-** Validación** que impide duplicados y genera errores personalizados (con `@ExceptionHandler`).

---

### 🖥️ Interfaz Web Dinámica (MVC)

- Diseño responsive con **Bootstrap 5.1.3** y plantillas **Thymeleaf**.
- **Vistas protegidas** por rol mediante `sec:authorize`.
- **Navegación**: inicio, login, registro, películas, nueva película, crear persona, etc.
- **Carga y visualización de imágenes** (carteles y avatares) desde almacenamiento local.

---

### 🔐 Configuración Técnica y Seguridad

-** Filtros personalizados** con `JwtValidator` para validar tokens en cada petición.
- `SecurityFilterChain` segmentado por contexto (`/auth`, `/api`, `/admin`...).
- **Encriptación** de contraseñas con `BCryptPasswordEncoder`.
- **Logs personalizados** para trazabilidad y depuración.

---

### 📖 Documentación Interactiva con Swagger

- **Documentación generada automáticamente** con **OpenAPI**.
- **Panel Swagger UI** para probar los endpoints y ver los modelos de datos.

🔗 **Acceso a Swagger UI**: [http://localhost:8080/doc/swagger-ui.html](http://localhost:8080/doc/swagger-ui.html)

---


## 2. Tecnologías y Dependencias

- Java 17
- Spring Boot 3
- Spring Security + JWT
- Spring Data JPA + Hibernate
- Spring MVC + Thymeleaf
- Spring Batch
- H2 (dev) / MySQL (producción)
- Swagger / OpenAPI
- Bootstrap 5.1.3
- ModelMapper
- Lombok

### 📂 Estructura del Proyecto

```plaintext
filmfeel/
├── src/
│   ├── main/
│   │   ├── java/com/filmfeel/
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── entity/
│   │   │   ├── repository/
│   │   │   ├── security/
│   │   │   ├── service/
│   │   │   └── FilmFeelApplication.java
│   │   └── resources/
│   │       ├── static/
│   │       ├── templates/
│   │       └── application.properties
└── pom.xml
```

---

## 2. 🔗  Endpoints principales

🎥 **Películas** (`/peliculas`)

| Método | Ruta                              | Descripción                                 |
|--------|-----------------------------------|---------------------------------------------|
| GET    | `/peliculas`                      | Lista las películas (vista paginada)        |
| GET    | `/peliculas/nueva-pelicula`       | Formulario para crear película (ADMIN)      |
| POST   | `/peliculas/nueva-pelicula`       | Guardar película nueva                      |
| GET    | `/peliculas/{id}`                 | Detalle de película                         |
| GET    | `/peliculas/{id}/editar`          | Formulario de edición de película           |
| POST   | `/peliculas/{id}/editar`          | Guardar cambios de película                 |
| POST   | `/peliculas/{id}/eliminar`        | Eliminar película                           |

👤 **Personas** (`/personas`)

| Método | Ruta                            | Descripción                          |
|--------|---------------------------------|--------------------------------------|
| GET    | `/personas`                     | Listado de personas                  |
| GET    | `/personas/nueva-persona`       | Formulario para nueva persona        |
| POST   | `/personas/nueva-persona`       | Guardar nueva persona                |
| POST   | `/personas/{id}`                | Eliminar persona por ID              |

💬 **Reseñas** (`/reviews`)

| Método | Ruta                            | Descripción                     |
|--------|---------------------------------|---------------------------------|
| POST   | `/reviews/submit/{filmId}`      | Enviar reseña de película       |
| POST   | `/reviews/{id}`                 | Eliminar reseña                 |

⭐ **Puntuaciones** (`/scores`)

| Método | Ruta                              | Descripción                         |
|--------|-----------------------------------|-------------------------------------|
| POST   | `/scores/submit/{filmId}`         | Enviar puntuación                   |
| GET    | `/scores/average/{filmId}`        | Obtener media de puntuaciones       |
| POST   | `/scores/{id}`                    | Eliminar puntuación                 |

🔐 **Autenticación**

| Método | Ruta              | Descripción                          |
|--------|-------------------|--------------------------------------|
| GET    | `/login`          | Formulario de inicio de sesión       |
| GET    | `/registro`       | Formulario de registro de usuario    |
| POST   | `/registro`       | Enviar datos de nuevo usuario        |
