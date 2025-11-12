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

## 🛠️ Cómo ejecutar el proyecto

### Paso 1: Obtener el código

Si tienes Git instalado:
```bash
git clone <url-del-repositorio>
cd TpTallerConstruccionSoftware
```

O simplemente asegúrate de estar en la carpeta del proyecto.

### Paso 2: Iniciar Docker Desktop

1. Abre Docker Desktop
2. Espera a que esté completamente iniciado (el ícono dejará de estar en color naranja)

### Paso 3: Construir y ejecutar los contenedores

Abre una terminal (PowerShell o CMD) en la carpeta del proyecto y ejecuta:

```bash
docker-compose up --build
```

**¿Qué hace este comando?**
- Descarga las imágenes necesarias (MySQL y Java)
- Construye la aplicación
- Crea dos contenedores: uno para MySQL y otro para la aplicación
- Los inicia automáticamente

**Espera a ver estos mensajes:**
- `Started TpTallerConstruccionSoftwareApplication` - La aplicación está lista
- El proceso puede tardar 2-5 minutos la primera vez

### Paso 4: Verificar que funciona

Una vez que los contenedores estén ejecutándose, abre tu navegador o una herramienta como Postman y prueba:

**URL base de la API:**
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

## 📊 Configuración de la Base de Datos

El proyecto está configurado con las siguientes credenciales de MySQL:

- **Host**: localhost (cuando se ejecuta con Docker)
- **Puerto**: 3306
- **Nombre de BD**: demo_jpa
- **Usuario**: app_user
- **Contraseña**: 123456

Si necesitas acceder directamente a MySQL, puedes usar cualquier cliente de MySQL (como MySQL Workbench) con estas credenciales.

## ⏹️ Detener la aplicación

Para detener los contenedores, presiona `Ctrl + C` en la terminal donde ejecutaste docker-compose.

Si quieres detener y eliminar los contenedores completamente:

```bash
docker-compose down
```

**Para eliminar también los datos de la base de datos:**
```bash
docker-compose down -v
```

## 🔄 Reiniciar la aplicación

Si ya ejecutaste el proyecto anteriormente y quieres iniciarlo de nuevo:

```bash
docker-compose up
```

(Sin el `--build` si no has hecho cambios en el código)

## 🐛 Solución de problemas comunes

### Error: "Port 3306 is already in use"
**Problema:** Ya tienes MySQL ejecutándose en tu computadora.

**Solución:** Detén el servicio de MySQL local o cambia el puerto en el archivo `docker-compose.yml`.

### Error: "Port 8080 is already in use"
**Problema:** Ya hay otra aplicación usando el puerto 8080.

**Solución:** Detén la otra aplicación o cambia el puerto en el archivo `docker-compose.yml` (ejemplo: `"8081:8080"`).

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
- La aplicación se reconstruye automáticamente si cambias el código (requiere reiniciar con `docker-compose up --build`)
- Puedes ver los logs de cada contenedor con: `docker-compose logs <nombre-servicio>`


## 🛠️ Stack Tecnológico

- **Java 17**
- **Spring Boot 3.5.7**
- **MySQL 8.0**
- **Maven**
- **Docker & Docker Compose**
- **JPA/Hibernate**
- **Lombok**

---


