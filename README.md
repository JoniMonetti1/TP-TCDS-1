# Sistema de Gestión de Productos - TpTallerConstruccionSoftware

## 📋 Descripción

Este proyecto es una aplicación web desarrollada con Spring Boot que permite gestionar productos a través de una API REST. Utiliza MySQL como base de datos y está completamente dockerizado para facilitar su ejecución.

## 🚀 Requisitos Previos

Antes de ejecutar el proyecto, necesitas tener instalado en tu computadora:

1. **Docker Desktop**
   - Descargar desde: https://www.docker.com/products/docker-desktop/
   - Instalar y asegurarse de que está en ejecución (debe aparecer el ícono en la barra de tareas)

2. **Git** (opcional, si quieres clonar el repositorio)
   - Descargar desde: https://git-scm.com/downloads

## 📦 ¿Qué incluye este proyecto?

- **Aplicación Spring Boot**: Backend con API REST para gestionar productos
- **Base de datos MySQL**: Almacenamiento de información
- **Docker**: Contenedores para ejecutar todo de forma aislada y sencilla

## 🧱 Arquitectura y buenas prácticas implementadas

- **DTOs y validaciones**: La API recibe `ProductRequest` con anotaciones de Bean Validation (`@NotBlank`, `@DecimalMin`, etc.) y responde con `ProductResponse`, evitando exponer entidades JPA.
- **Servicios desacoplados del transporte**: La capa de servicio ya no devuelve `ResponseEntity`; en su lugar maneja reglas de negocio y lanza excepciones tipadas que el controlador transforma en HTTP.
- **Reglas de negocio explícitas**: El nombre del producto es único tanto a nivel de base como de aplicación; los servicios verifican duplicados y devuelven `409 Conflict` cuando corresponde.
- **Transacciones acotadas**: Las operaciones de escritura utilizan `@Transactional` para garantizar consistencia.
- **Documentación y tooling actualizados**: Se añadió `spring-boot-starter-validation`, se actualizaron los comandos a `docker compose` y se describieron escenarios de ejecución local y dockerizada.

## 🛠️ Cómo ejecutar el proyecto

Puedes levantar la app de dos maneras según lo que necesites probar.

### Opción A: Entorno local con Maven (sin Docker)

1. Asegúrate de tener JDK 17 instalado y con `JAVA_HOME` configurado.
2. Desde la raíz del proyecto ejecuta:

```bash
./mvnw clean spring-boot:run
```

La aplicación quedará disponible en `http://localhost:8080/system/api/v1`. Si el puerto está ocupado, puedes cambiarlo en caliente con:

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

Detén la ejecución con `Ctrl + C`.

### Opción B: Stack completo con Docker Compose

1. Clona el repositorio (o descarga el ZIP) y entra en la carpeta del proyecto.
2. Inicia Docker Desktop y espera a que quede listo.
3. Construye y levanta toda la solución:

```bash
docker compose up --build
```

El comando descarga las imágenes necesarias, construye el JAR y crea dos contenedores (MySQL + aplicación). La primera ejecución puede tardar unos minutos.

Cuando veas `Started TpTallerConstruccionSoftwareApplication`, la API estará disponible en:

```
http://localhost:8080/system/api/v1/products
```

## 🔍 Endpoints disponibles

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/system/api/v1/products` | Obtener todos los productos |
| GET | `/system/api/v1/products/{id}` | Obtener un producto por ID |
| GET | `/system/api/v1/products/name/{name}` | Buscar productos por nombre |
| POST | `/system/api/v1/products` | Crear un nuevo producto |
| PUT | `/system/api/v1/products/{id}` | Actualizar un producto |
| DELETE | `/system/api/v1/products/{id}` | Eliminar un producto |

### Ejemplo de creación de producto (POST)

**URL:** `http://localhost:8080/system/api/v1/products`

**Body (JSON):**
```json
{
  "nombre": "Laptop",
  "descripcion": "Laptop Dell Inspiron 15",
  "precio": 899.99
}
```

### Ejemplo de obtener todos los productos (GET)

**URL:** `http://localhost:8080/system/api/v1/products`

Simplemente haz una petición GET a esta URL.

## ✅ Validaciones y manejo de errores

- Todos los cuerpos de entrada pasan por `ProductRequest` y Bean Validation. Si un campo es inválido se responde con `400 Bad Request` y el detalle del error.
- Cuando se intenta consultar un recurso inexistente, se responde con `404 Not Found`.
- Crear o actualizar un producto con un nombre ya registrado responde `409 Conflict`.

Ejemplo de error de validación:

```json
{
  "timestamp": "2025-11-13T15:00:21.123+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "El nombre es obligatorio",
  "path": "/system/api/v1/products"
}
```

## 📊 Configuración de la Base de Datos

El proyecto está configurado con las siguientes credenciales de MySQL:

- **Host**: localhost (cuando se ejecuta con Docker)
- **Puerto**: 3306
- **Nombre de BD**: demo_jpa
- **Usuario**: app_user
- **Contraseña**: 123456

Si necesitas acceder directamente a MySQL, puedes usar cualquier cliente de MySQL (como MySQL Workbench) con estas credenciales.

## ⏹️ Detener la aplicación

Para detener los contenedores, presiona `Ctrl + C` en la terminal donde ejecutaste docker compose.

Si quieres detener y eliminar los contenedores completamente:

```bash
docker compose down
```

**Para eliminar también los datos de la base de datos:**
```bash
docker compose down -v
```

## 🔄 Reiniciar la aplicación

Si ya ejecutaste el proyecto anteriormente y quieres iniciarlo de nuevo:

```bash
docker compose up
```

(Sin el `--build` si no has hecho cambios en el código)

## 🐛 Solución de problemas comunes

### Error: "Port 3306 is already in use"
**Problema:** Ya tienes MySQL ejecutándose en tu computadora.

**Solución:** Detén el servicio de MySQL local o cambia el puerto en el archivo `docker-compose.yml`.

### Error: "Port 8080 is already in use"
**Problema:** Ya hay otra aplicación usando el puerto 8080.

**Solución:** Detén la otra aplicación o cambia el puerto en el archivo `docker-compose.yml` (ejemplo: `"8081:8080"`).

### Error: "Conflict. The container name \"/mysql-db\" is already in use"
**Problema:** Quedó un contenedor antiguo con el mismo nombre.

**Solución:** Elimina el contenedor huérfano y vuelve a levantar el stack:

```bash
docker rm -f mysql-db
docker compose up --build
```

### La aplicación no inicia
**Solución:**
1. Verifica que Docker Desktop esté ejecutándose
2. Asegúrate de estar en la carpeta correcta del proyecto
3. Revisa los logs en la terminal para ver el error específico

### No puedo conectarme a la API
**Solución:**
1. Verifica que ambos contenedores estén corriendo: `docker ps`
2. Espera 1-2 minutos después de que aparezca el mensaje de "Started"
3. Verifica que la URL sea correcta: `http://localhost:8080/system/api/v1/products`

## 📝 Notas adicionales

- Los datos de la base de datos se mantienen entre reinicios gracias a los volúmenes de Docker
- La aplicación se reconstruye automáticamente si cambias el código (requiere reiniciar con `docker compose up --build`)
- Puedes ver los logs de cada contenedor con: `docker compose logs <nombre-servicio>`


## 🛠️ Stack Tecnológico

- **Java 17**
- **Spring Boot 3.5.7**
- **MySQL 8.0**
- **Maven**
- **Docker & Docker Compose**
- **JPA/Hibernate**
- **Lombok**

---
