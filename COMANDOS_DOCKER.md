# Comandos Útiles de Docker - Guía Rápida

## 🚀 Comandos Básicos

### Iniciar el proyecto
```bash
docker compose up --build
```

### Iniciar en segundo plano
```bash
docker compose up -d
```

### Detener los contenedores
```bash
# Presiona Ctrl + C en la terminal si está ejecutándose en primer plano

# O usa este comando:
docker compose stop
```

### Detener y eliminar los contenedores
```bash
docker compose down
```

### Detener y eliminar contenedores + volúmenes (BORRA LA BASE DE DATOS)
```bash
docker compose down -v
```

## 📊 Comandos de Monitoreo

### Ver los contenedores que están ejecutándose
```bash
docker ps
```

### Ver todos los contenedores (incluso los detenidos)
```bash
docker ps -a
```

### Ver los logs de la aplicación
```bash
docker compose logs spring-app
```

### Ver los logs de MySQL
```bash
docker compose logs mysql-db
```

### Ver los logs en tiempo real (seguir los logs)
```bash
docker compose logs -f spring-app
```


## 🔧 Comandos de Mantenimiento

### Reconstruir la aplicación después de cambios en el código
```bash
docker compose up --build
```

### Entrar a la terminal del contenedor de MySQL
```bash
docker exec -it mysql-db mysql -u app_user -p123456 demo_jpa
```

### Entrar a la terminal del contenedor de la aplicación
```bash
docker exec -it spring-app sh
```

### Ver el uso de recursos de los contenedores
```bash
docker stats
```

### Limpiar contenedores detenidos
```bash
docker container prune
```

### Limpiar imágenes no utilizadas
```bash
docker image prune
```

### Limpiar todo (contenedores, redes, imágenes, volúmenes)
```bash
docker system prune -a --volumes
```
**⚠️ CUIDADO: Esto eliminará TODO, incluyendo la base de datos**

## 🐛 Debug y Resolución de Problemas

### Reiniciar completamente el proyecto
```bash
docker compose down
docker compose up --build
```

### Ver información detallada de un contenedor
```bash
docker inspect spring-app
```

### Ver las redes de Docker
```bash
docker network ls
```

### Ver los volúmenes de Docker
```bash
docker volume ls
```

## 📱 Probar la API

### Usando PowerShell (Windows)
```powershell
# GET - Obtener todos los productos
Invoke-WebRequest -Uri "http://localhost:8080/system/api/v1/products" -Method GET

# POST - Crear un producto
$body = @{
    nombre = "Producto Test"
    descripcion = "Descripción de prueba"
    precio = 99.99
} | ConvertTo-Json

Invoke-WebRequest -Uri "http://localhost:8080/system/api/v1/products" -Method POST -Body $body -ContentType "application/json"
```


## 📝 Notas Importantes

- Siempre asegúrate de que Docker Desktop esté ejecutándose antes de usar estos comandos
- Los comandos con `-f` (follow) se quedan mostrando logs continuamente, presiona Ctrl + C para salir
- Los volúmenes mantienen los datos entre reinicios, a menos que uses `docker compose down -v`
- Si cambias el código Java, necesitas reconstruir con `--build`

## 🆘 Comandos de Emergencia

### Si algo no funciona, prueba esto en orden:

1. **Detener todo**
```bash
docker compose down
```

2. **Limpiar**
```bash
docker system prune -f
```

3. **Reconstruir desde cero**
```bash
docker compose up --build
```

4. **Si aún no funciona, eliminar también los volúmenes**
```bash
docker compose down -v
docker compose up --build
```

---

