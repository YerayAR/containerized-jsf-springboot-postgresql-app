# 🚀 Guía de Troubleshooting Railway

## ⚠️ Problema Principal Identificado

**Tu aplicación NO tiene PostgreSQL configurado**. El `SPRING_DATASOURCE_URL` está vacío.

---

## ✅ Solución Paso a Paso

### **Paso 1: Agregar PostgreSQL al Proyecto**

Tienes dos opciones:

#### **Opción A: Desde el Dashboard Web (RECOMENDADO)**

1. Ejecuta:
   ```powershell
   railway open
   ```

2. En el dashboard:
   - Click en **"+ New"** (esquina superior derecha)
   - Selecciona **"Database"**
   - Elige **"Add PostgreSQL"**
   - Espera 1-2 minutos a que se cree

3. La base de datos creará automáticamente estas variables:
   - `DATABASE_URL`
   - `PGHOST`, `PGPORT`, `PGDATABASE`
   - `PGUSER`, `PGPASSWORD`

4. **IMPORTANTE**: Railway vinculará automáticamente estas variables a tu servicio `jsf-backend`

#### **Opción B: Usar Template (Alternativo)**

Si prefieres empezar desde cero con una configuración automática:

1. Ve a [Railway Templates](https://railway.app/templates)
2. Busca "Spring Boot + PostgreSQL"
3. Despliega el template
4. Clona el repositorio generado y migra tu código

---

### **Paso 2: Verificar la Configuración**

Después de agregar PostgreSQL, verifica que todo está correcto:

```powershell
# Ver variables de entorno
railway variables

# Deberías ver algo como:
# DATABASE_URL = postgresql://postgres:...@...railway.app:5432/railway
```

---

### **Paso 3: Desplegar la Aplicación**

Usa el script automatizado que creé:

```powershell
.\railway-deploy.ps1
```

Este script:
- ✅ Verifica que Railway CLI esté instalado
- ✅ Verifica autenticación
- ✅ Comprueba que PostgreSQL esté configurado
- ✅ (Opcional) Hace un build local de prueba
- ✅ Despliega en Railway
- ✅ Muestra logs y abre la app

---

## 🔍 Diagnóstico de Errores Comunes

### **Error 1: "DATABASE_URL not set"**

**Causa**: PostgreSQL no está agregado al proyecto

**Solución**:
1. `railway open`
2. Agregar PostgreSQL desde el dashboard
3. Esperar a que se cree
4. Volver a desplegar

---

### **Error 2: "Application failed to respond to health check"**

**Causa**: La aplicación tarda en iniciar o hay un error

**Solución**:
```powershell
# Ver logs en tiempo real
railway logs

# Buscar líneas con ERROR o Exception
```

**Cosas a verificar**:
- ¿La conexión a PostgreSQL está funcionando?
- ¿El puerto está correctamente configurado? (debe usar `$PORT`)
- ¿Hay suficiente memoria? (Railway free tier: 512MB)

---

### **Error 3: "Build failed"**

**Causa**: Error en el Dockerfile o dependencias Maven

**Solución**:
```powershell
# Test local del build
docker build -t test-app .

# Si falla, revisa:
# 1. Que pom.xml esté correcto
# 2. Que las dependencias se puedan descargar
# 3. Que la versión de Java sea correcta (17)
```

---

### **Error 4: "Memory limit exceeded"**

**Causa**: La aplicación Java usa demasiada memoria

**Solución**:
1. El Dockerfile ya tiene `-XX:MaxRAMPercentage=75.0` configurado
2. Si aún falla, considera:
   - Actualizar al plan Hobby ($5/mes con 1GB RAM)
   - Optimizar el código para usar menos memoria

---

## 📊 Comandos Útiles

```powershell
# Ver estado del proyecto
railway status

# Ver logs en tiempo real
railway logs

# Ver variables de entorno
railway variables

# Redesplegar sin cambios (útil si cambias variables)
railway redeploy

# Abrir dashboard
railway open

# Conectar a la base de datos directamente
railway connect postgres

# Ver deployments recientes
railway deployment list
```

---

## 🎯 Checklist Pre-Deploy

Antes de desplegar, asegúrate de:

- [ ] PostgreSQL agregado al proyecto Railway
- [ ] Variables `DATABASE_URL`, `PGUSER`, `PGPASSWORD` configuradas
- [ ] `railway.toml` existe en el proyecto
- [ ] `Dockerfile` está correctamente configurado
- [ ] Has probado el build localmente: `docker build -t test .`
- [ ] Estás autenticado: `railway whoami`
- [ ] Tienes un proyecto vinculado: `railway status`

---

## 🔗 Flujo Completo de Deploy

```mermaid
graph TD
    A[Código Local] --> B{PostgreSQL agregado?}
    B -->|No| C[Agregar desde Dashboard]
    B -->|Sí| D[railway up]
    C --> D
    D --> E{Build exitoso?}
    E -->|No| F[Revisar logs]
    E -->|Sí| G{Health check OK?}
    G -->|No| H[Revisar logs de app]
    G -->|Sí| I[✅ Deploy Exitoso]
    F --> J[Corregir error]
    H --> J
    J --> D
```

---

## 🆘 Último Recurso

Si nada funciona, empieza desde cero:

1. **Crear nuevo proyecto**:
   ```powershell
   railway init
   ```

2. **Agregar PostgreSQL**:
   - Desde el dashboard: `railway open`
   - + New → Database → PostgreSQL

3. **Vincular servicio**:
   ```powershell
   railway service
   # Selecciona tu servicio
   ```

4. **Desplegar**:
   ```powershell
   railway up
   ```

---

## 📚 Recursos

- [Railway Docs](https://docs.railway.app/)
- [Railway Discord](https://discord.gg/railway)
- [Spring Boot Docs](https://spring.io/projects/spring-boot)
- [PostgreSQL Docs](https://www.postgresql.org/docs/)

---

## 💡 Tips

1. **Siempre revisa los logs**: `railway logs` es tu mejor amigo
2. **Prueba localmente primero**: `docker-compose up` antes de desplegar
3. **Usa variables de entorno**: Nunca hardcodees credenciales
4. **Health checks son importantes**: Railway los usa para saber si tu app está lista
5. **Paciencia**: El primer deploy puede tardar 3-5 minutos

---

## 🎉 Siguiente Paso

Una vez que agregues PostgreSQL, ejecuta:

```powershell
.\railway-deploy.ps1
```

Y sigue las instrucciones interactivas del script.
