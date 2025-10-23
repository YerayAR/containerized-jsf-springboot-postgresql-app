# 🔗 Cómo Vincular PostgreSQL con jsf-backend

## 📊 Estado Actual

✅ Tienes PostgreSQL creado: `jsf-postgres`
✅ Tienes el backend: `jsf-backend`
❌ **NO ESTÁN CONECTADOS**

---

## 🎯 Solución: Vincular las Variables

El dashboard se acaba de abrir. Sigue estos pasos:

### **Paso 1: Ir al servicio jsf-backend**

1. En el dashboard de Railway
2. Click en el servicio **`jsf-backend`**
3. Ve a la pestaña **"Variables"**

---

### **Paso 2: Agregar Referencias a PostgreSQL**

En la sección de Variables, agrega estas 3 variables haciendo click en **"+ New Variable"**:

#### **Variable 1: DATABASE_URL**
```
Name: DATABASE_URL
Value: ${{jsf-postgres.DATABASE_URL}}
```

#### **Variable 2: PGUSER**
```
Name: PGUSER  
Value: ${{jsf-postgres.POSTGRES_USER}}
```

#### **Variable 3: PGPASSWORD**
```
Name: PGPASSWORD
Value: ${{jsf-postgres.POSTGRES_PASSWORD}}
```

---

### **Paso 3: Explicación de las Variables**

Railway usa la sintaxis `${{service.VARIABLE}}` para referenciar variables de otros servicios.

En tu caso:
- `jsf-postgres` es el servicio de PostgreSQL
- `DATABASE_URL` será algo como: `postgresql://appuser:password@jsf-postgres.railway.internal:5432/productdb`

---

### **Paso 4: Deploy Automático**

Una vez agregues las variables:
1. Railway automáticamente redesplegarà el servicio
2. Espera 2-3 minutos
3. Verifica los logs para confirmar que conecta a la BD

---

## ✅ Verificación

Después de agregar las variables, ejecuta:

```powershell
# Verificar que las variables se agregaron
railway variables

# Ver logs del deploy
railway logs

# Ver estado
railway deployment list
```

Deberías ver:
- `DATABASE_URL` con valor
- `PGUSER` = appuser
- `PGPASSWORD` = (el password)

---

## 🚀 Siguiente Paso

Una vez configurado, el deploy automático debería funcionar.

Si quieres forzar un nuevo deploy:
```powershell
railway redeploy
```

O desplegar código nuevo:
```powershell
railway up
```
