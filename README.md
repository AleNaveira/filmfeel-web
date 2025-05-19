*This README is avaliable in [Español](#version-español) 

**[LIVE DEMO]**  
Try the application: [https://filmfeel.onrender.com ](https://filmfeel-web.onrender.com) 
*Note: this application is currently under development and not yet optimized for mobile devices. Improvements are in progress to enhance the mobile experience.*


# FilmFeel Web Application (In Development)

> **Note**: *This application is currently under active development,  some features may still be in progress and occasional errors might occur. Continuous improvements are being made to increase stability and add new functionalities.

## 1. Description

FilmFeel is a web application developed in Java using Spring Boot that allows users to collaboratively and interactively manage a movie catalog.

Users can:

- **Register new films** with title, synopsis, release year, duration, and poster image.

- **Link film professionals** to each movie, including actors, directors, cinematographers, screenwriters, and composers — all of whom are also stored in the system.

- **Submit ratings** (from 1 to 5 stars) and write personal reviews for each movie.

- **Authenticate and authorize users with JWT-based security**, supporting distinct roles (USER and ADMIN).

- **View web pages** rendered through **Thymeleaf**.


## 2. Keys Features

### 🎬 Film management

- **Complete CRUD operations** for films, available to authorized users.
- **Attributes** include: title, synopsis, release year, duration, and poster (stored locally).
- **Linking film professionals** (actors, musicians, cinematographers, screenwriters, directors) to each movie.
- **Detailed movie pages** showing cast, crew, reviews, and ratings.

---

### 🧑‍💼 Authentication and Authorization System

- Login and registration using **JWT tokens**.
- Security powered by **Spring Security**.
- Two roles:
  - `USER`: can rate and review films.
  - `ADMIN`: full system access, including permission to register new administrators.

---

### 🌐 REST API with JWT Security

- **Protected endpoints** using JWT (`/auth/**`, `/api/**`).
- **User registration and login** via JSON requests.
- **REST endpoints** to submit and access film reviews.

---

### 💬 Reviews and Ratings System

- Each user can submit **only one review and one rating per film**.
- **Reviews managed via REST API** and displayed in the web interface.
- **Average rating** automatically calculated and displayed per film.
- **Validation layer** prevents duplicates and handles errors with custom exceptions `@ExceptionHandler`.

---

### 🖥️ Dynamic Web Interface (MVC)

- Responsive design with **Bootstrap 5.1.3** and **Thymeleaf templates**.
- **Role-based view protection** using `sec:authorize`.
- **Navigation structure** includes home, login, registration, movie list, new movie form, add person, etc.
- **Upload and display of images** (poster images and avatars) from local storage.

---

### 🔐 Technical Configuration and Security

- **Custom filters** using `JwtValidator` to verify tokens per request.
- Segmented `SecurityFilterChain` based on context paths (`/auth`, `/api`, `/admin`, etc.).
- **Password encryption** with `BCryptPasswordEncoder`.
- **Custom logging** for traceability and debugging.

---

### 📖 Interactive API Documentation with Swagger

- **Automatically generated documentation** via **OpenAPI**.
- **Swagger UI panel** to test endpoints and inspect data models.

🔗 **Access Swagger UI**: [http://localhost:8080/doc/swagger-ui.html](http://localhost:8080/doc/swagger-ui.html)

---


## 3. Technologies and Dependencies

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

### 📂 Project Structure

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

## 4. 🔗  Main Endpoints

🎥 **Films** (`/peliculas`)

| Méthod | Path                              | Description                                 |
|--------|-----------------------------------|---------------------------------------------|
| GET    | `/peliculas`                      | List all films  (paginated view)        |
| GET    | `/peliculas/nueva-pelicula`       | Form to create new film     |
| POST   | `/peliculas/nueva-pelicula`       | Submit and save a new film                    |
| GET    | `/peliculas/{id}`                 | view film details                         |
| GET    | `/peliculas/{id}/editar`          | Form to edit an existing film          |
| POST   | `/peliculas/{id}/editar`          | Save film updates             |
| POST   | `/peliculas/{id}/eliminar`        | Delete film                         |

👤 **People** (`/personas`)

| Method | Path                           | Description                          |
|--------|---------------------------------|--------------------------------------|
| GET    | `/personas`                     | List all registered people                  |
| GET    | `/personas/nueva-persona`       | Form to register a new person      |
| POST   | `/personas/nueva-persona`       | Submit and save a new person                |
| POST   | `/personas/{id}`                | Delete a person by ID             |

💬 **Reviews** (`/reviews`)

| Méthod | Path                            | Description                     |
|--------|---------------------------------|---------------------------------|
| POST   | `/reviews/submit/{filmId}`      | Submit a film review      |
| POST   | `/reviews/{id}`                 | Delete a review                |

⭐ **Rating** (`/scores`)

| Method | Path                              | Description                        |
|--------|-----------------------------------|-------------------------------------|
| POST   | `/scores/submit/{filmId}`         | Submit a score for a film                  |
| GET    | `/scores/average/{filmId}`        | Get average score     |
| POST   | `/scores/{id}`                    | Delete a score                |

🔐 **Autentication**

| Method | Path              | Description                          |
|--------|-------------------|--------------------------------------|
| GET    | `/login`          | Login form       |
| GET    | `/registro`       | Registration form    |
| POST   | `/registro`       | Submit new user       |



## 5. 🧩 Features Under Development


The application is under active development, and several key features are still to be implemented such as:

- 🔁 **Spring Batch**
  Daily migration of newly added films to a CSV file, with listeners (`JobExecutionListener`, `ItemWriteListener`) and migration tracking.

- 📊 **Detailed logging system**  
  Execution traces to monitor key processes.

- ❗ **Improved custom error page**  
  Integration of `error.html` for user-friendly handling of exceptions (404, 500...).

- ⚡ **Visual and performance improvements**  
  Optimization of views, image handling, and overall performance.




## 6. 🚀 Installation

1. Clone repository from GitHub:
```bash
git clone https://github.com/AleNaveira/filmfeel-web.git
```

2. Open project in your IDE (e.g., IntelliJ o Eclipse).

3. Run `FilmFeelApplication.java`

4. Access: 
   `http://localhost:8080`

## 7. 🛡️ Architecture and Development Standards

- Layered architecture: controllers, services, repositories, and entities.
- Data validation in forms and DTOs (`@Valid`, `BindingResult`).
- Secure password handling using `BCryptPasswordEncoder`.
- Role-based access control (USER and ADMIN) with Spring Security and JWT.
- Secure route protection in Thymeleaf views using `sec:authorize`.
- Interactive REST API documentation with Swagger/OpenAPI.
- Custom error handling and validation with `@ExceptionHandler`.
- Secure and dynamic file storage (images) on the local server.
- Use of DTOs and ModelMapper to separate domain entities from presentation layers.
- RESTful architectural practices in API endpoints.
- Organized and environment-specific `application.properties` configuration.
- Prepared for production database usage (MySQL).



------------




## Versión en español

**[LIVE DEMO]**  
Prueba la aplicación: https://filmfeel.onrender.com  
*Nota: la aplicación aún no está optimizada para dispositivos móviles. Se están realizando ajustes para mejorar su visualización.*


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

### 🎬 Gestión de películas

- **CRUD **completo de películas accesible para usuarios.
- **Atributos**: título, sinopsis, año, duración y carátula (almacenada en local).
- ** Asociación de profesionales del cine** (actores, músicos, fotógrafos, guionistas, directores) a cada película.
- **Visualización de ficha técnica de cada film** con reparto, equipo y críticas/puntuaciones.


---

### 🧑‍💼 Sistema de autenticación y autorización

- Inicio de sesión y registro con **tokens JWT**.
- Seguridad implementada con **Spring Security**.
- Dos roles:
  - `USER`: puede puntuar y comentar películas.
  - `ADMIN`: acceso completo al sistema, también la creación de nuevos administradores.

---

### 🌐 API REST con Seguridad JWT

- ** Endpoints protegidos** con token JWT (`/auth/**`, `/api/**`).
- **Registro y login** vía JSON.
-** Endpoints REST **para crear y listar críticas de películas.

---

### 💬 Sistema de reviews y puntuaciones

- Cada usuario puede dejar **una única crítica y puntuación** por película.
- **Críticas gestionadas mediante API REST **y mostradas en la web.
- **Promedio de puntuaciones** calculado y visible para cada película.
- ** Validación** que impide duplicados y genera errores personalizados (`@ExceptionHandler`).

---

### 🖥️ Interfaz web dinámica (MVC)

- Diseño responsive con **Bootstrap 5.1.3** y plantillas **Thymeleaf**.
- **Vistas protegidas** por rol mediante `sec:authorize`.
- **Navegación**: inicio, login, registro, películas, nueva película, crear persona, etc.
- **Carga y visualización de imágenes** (carátulas y avatares) desde almacenamiento local.

---

### 🔐 Configuración técnica y seguridad

- ** Filtros personalizados** con `JwtValidator` para validar tokens en cada petición.
- `SecurityFilterChain` segmentado por contexto (`/auth`, `/api`, `/admin`...).
- **Encriptación** de contraseñas con `BCryptPasswordEncoder`.
- **Logs personalizados** para trazabilidad y depuración.

---

### 📖 Documentación interactiva con Swagger

- **Documentación generada automáticamente** con **OpenAPI**.
- **Panel Swagger UI** para probar los endpoints y ver los modelos de datos.

🔗 **Acceso a Swagger UI**: [http://localhost:8080/doc/swagger-ui.html](http://localhost:8080/doc/swagger-ui.html)

---


## 3. Tecnologías y dependencias

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

### 📂 Estructura del proyecto

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

## 4. 🔗  Endpoints principales

🎥 **Películas** (`/peliculas`)

| Método | Ruta                              | Descripción                                 |
|--------|-----------------------------------|---------------------------------------------|
| GET    | `/peliculas`                      | Lista las películas (vista paginada)        |
| GET    | `/peliculas/nueva-pelicula`       | Formulario para crear película     |
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



## 5. 🧩 Pendiente de implementación

La aplicación se encuentra en desarrollo activo y aún hay funcionalidades clave por implementar como:

- 🔁 ** Spring Batch**  
  Migración diaria de nuevas películas a un CSV, con listeners (`JobExecutionListener`, `ItemWriteListener`) y marca de migración.

- 📊 **Sistema de logs detallado**  
  Trazas de ejecución para monitorizar procesos clave.

- ❗ ** Mejora de la página personalizada de errores**  
  Integración de `error.html` para manejo elegante de excepciones (404, 500...).

- ⚡ **Mejoras visuales y de rendimiento**  
  Optimización de vistas, imágenes y rendimiento general.



## 6. 🚀 Instalación y ejecución

1. Clona el repositorio desde GitHub:
```bash
git clone https://github.com/AleNaveira/filmfeel-web.git
```

2. Abre el proyecto en el IDE (como IntelliJ o Eclipse).

3. Ejecuta la clase `FilmFeelApplication.java`

4. Accede a la app en: 
   `http://localhost:8080`

## 7. 🛡️ Buenas prácticas aplicadas

- Separación por capas: controladores, servicios, repositorios y entidades.
- Validaciones de datos en formularios y DTOs (`@Valid`, `BindingResult`).
- Manejo seguro de contraseñas con `BCryptPasswordEncoder`.
- Control de acceso basado en roles (USER y ADMIN) usando Spring Security y JWT.
- Gestión segura de rutas protegidas en vistas Thymeleaf con `sec:authorize`.
- Documentación de API REST interactiva con Swagger/OpenAPI.
- Gestión de errores y validaciones personalizadas mediante `@ExceptionHandler`.
- Almacenamiento seguro y dinámico de archivos (imágenes) en servidor local.
- Uso de DTOs y ModelMapper para separar entidades de la capa de presentación.
- Prácticas de arquitectura RESTful en endpoints de la API.
- Configuración de `application.properties` organizada y adaptada a distintos entornos.
- Preparación para uso de bases de datos en producción (MySQL).


