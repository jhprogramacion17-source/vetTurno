# 🐾 VetTurno — API REST para Veterinaria Huellitas

Sistema backend profesional para la gestión digital de citas, propietarios, pacientes y profesionales veterinarios, diseñado para sustituir registros manuales y evitar cruces de horarios.

---

## 📖 Historia y Contexto

En la **Veterinaria Huellitas**, Doña Marta (administradora) y Paula (recepcionista) gestionaban las citas mediante cuadernos físicos y mensajes de WhatsApp. Esto generaba cruces de turnos para el Dr. Andrés, pérdida de historial de pacientes y falta de control sobre quién realizaba cada acción.

**VetTurno** soluciona esta problemática proporcionando una API REST robusta que garantiza:
- Citas sin solapamiento de horarios por veterinario.
- Validación de fechas futuras en agenda.
- Control de acceso basado en roles (`USER` y `ADMIN`) mediante JWT.
- Integridad referencial y separación estricta entre capa de persistencia y contratos de API mediante DTOs.

---

## 🛠️ Tecnologías Utilizadas

- **Lenguaje:** Java 17
- **Framework:** Spring Boot 3.x
- **Persistencia:** Spring Data JPA & Hibernate
- **Base de Datos:** MySQL 8.x
- **Seguridad:** Spring Security 6 & JJWT (JSON Web Token 0.11.5) con BCrypt
- **Validación:** Jakarta Bean Validation (`@Valid`, `@Future`, `@NotBlank`, `@Email`)
- **Documentación interactiva:** SpringDoc OpenAPI 2 / Swagger UI
- **Gestor de dependencias:** Apache Maven

---

## 🏛️ Arquitectura del Sistema

El proyecto sigue una arquitectura desacoplada por capas bajo el principio de inyección de dependencias por constructor:

com.huellitas.vetturno/
├── config/         # Configuración OpenAPI / Swagger con soporte Bearer JWT
├── controller/     # Controladores REST que exponen los endpoints y códigos HTTP
├── dto/            # Data Transfer Objects (Request / Response) para aislar entidades
├── exception/      # Manejo global de excepciones (ApiError, GlobalExceptionHandler)
├── model/          # Entidades JPA (Usuario, Rol, Propietario, Mascota, Veterinario, Cita)
├── repository/     # Repositorios Spring Data JPA con consultas derivadas
├── security/       # Configuración de seguridad stateless, filtros JWT y BCrypt
└── service/        # Reglas de negocio (validación de cruces y orquestación)