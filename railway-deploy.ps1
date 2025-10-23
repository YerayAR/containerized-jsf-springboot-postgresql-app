#!/usr/bin/env pwsh
# ========================================
# Script completo de despliegue en Railway
# ========================================

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "🚀 RAILWAY DEPLOYMENT SCRIPT" -ForegroundColor Cyan
Write-Host "========================================`n" -ForegroundColor Cyan

# Paso 1: Verificar que Railway CLI está instalado
Write-Host "📋 Paso 1: Verificando Railway CLI..." -ForegroundColor Yellow
try {
    $version = railway --version 2>&1
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ Railway CLI instalado: $version" -ForegroundColor Green
    } else {
        Write-Host "❌ Railway CLI no encontrado. Instálalo con: npm i -g @railway/cli" -ForegroundColor Red
        exit 1
    }
} catch {
    Write-Host "❌ Error verificando Railway CLI: $_" -ForegroundColor Red
    exit 1
}

# Paso 2: Verificar autenticación
Write-Host "`n📋 Paso 2: Verificando autenticación..." -ForegroundColor Yellow
try {
    $whoami = railway whoami 2>&1
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ $whoami" -ForegroundColor Green
    } else {
        Write-Host "❌ No autenticado. Ejecuta: railway login" -ForegroundColor Red
        exit 1
    }
} catch {
    Write-Host "❌ Error verificando autenticación: $_" -ForegroundColor Red
    exit 1
}

# Paso 3: Verificar estado del proyecto
Write-Host "`n📋 Paso 3: Estado del proyecto..." -ForegroundColor Yellow
railway status

# Paso 4: Verificar variables de entorno
Write-Host "`n📋 Paso 4: Variables de entorno configuradas..." -ForegroundColor Yellow
$vars = railway variables 2>&1 | Out-String
if ($vars -match "DATABASE_URL") {
    Write-Host "✅ DATABASE_URL configurado" -ForegroundColor Green
} else {
    Write-Host "⚠️  DATABASE_URL NO configurado" -ForegroundColor Red
    Write-Host "   Necesitas agregar PostgreSQL desde el dashboard:" -ForegroundColor Yellow
    Write-Host "   1. Ejecuta: railway open" -ForegroundColor Yellow
    Write-Host "   2. Click en '+ New' → 'Database' → 'Add PostgreSQL'" -ForegroundColor Yellow
    Write-Host "   3. Espera a que se cree la base de datos" -ForegroundColor Yellow
    Write-Host "   4. Vuelve a ejecutar este script`n" -ForegroundColor Yellow
    
    $response = Read-Host "¿Quieres abrir el dashboard ahora? (s/n)"
    if ($response -eq "s" -or $response -eq "S") {
        railway open
        Write-Host "`nAbre el dashboard y agrega PostgreSQL. Presiona Enter cuando termines..." -ForegroundColor Cyan
        Read-Host
    } else {
        exit 1
    }
}

# Paso 5: Test local (opcional)
Write-Host "`n📋 Paso 5: ¿Quieres hacer un test local primero? (s/n)" -ForegroundColor Yellow
$testLocal = Read-Host
if ($testLocal -eq "s" -or $testLocal -eq "S") {
    Write-Host "🧪 Construyendo imagen Docker localmente..." -ForegroundColor Cyan
    docker build -t jsf-app-test .
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ Build local exitoso" -ForegroundColor Green
    } else {
        Write-Host "❌ Error en build local. Revisa el Dockerfile" -ForegroundColor Red
        exit 1
    }
}

# Paso 6: Deploy
Write-Host "`n📋 Paso 6: Iniciando deploy en Railway..." -ForegroundColor Yellow
Write-Host "   Esto puede tardar varios minutos...`n" -ForegroundColor Cyan

try {
    railway up
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "`n✅ Deploy completado!" -ForegroundColor Green
        
        # Paso 7: Ver logs
        Write-Host "`n📋 Paso 7: ¿Quieres ver los logs? (s/n)" -ForegroundColor Yellow
        $viewLogs = Read-Host
        if ($viewLogs -eq "s" -or $viewLogs -eq "S") {
            Write-Host "📜 Mostrando logs (Ctrl+C para salir)...`n" -ForegroundColor Cyan
            railway logs
        }
        
        # Paso 8: Abrir app
        Write-Host "`n📋 Paso 8: ¿Quieres abrir la aplicación en el navegador? (s/n)" -ForegroundColor Yellow
        $openApp = Read-Host
        if ($openApp -eq "s" -or $openApp -eq "S") {
            # Obtener la URL pública
            $status = railway status 2>&1 | Out-String
            railway open
        }
        
        Write-Host "`n========================================" -ForegroundColor Cyan
        Write-Host "🎉 DEPLOYMENT EXITOSO" -ForegroundColor Green
        Write-Host "========================================" -ForegroundColor Cyan
        Write-Host "`nComandos útiles:" -ForegroundColor Yellow
        Write-Host "  railway logs           - Ver logs en tiempo real" -ForegroundColor White
        Write-Host "  railway open           - Abrir dashboard" -ForegroundColor White
        Write-Host "  railway status         - Ver estado del proyecto" -ForegroundColor White
        Write-Host "  railway variables      - Ver variables de entorno" -ForegroundColor White
        Write-Host "  railway redeploy       - Redesplegar sin cambios" -ForegroundColor White
        
    } else {
        Write-Host "`n❌ Error en el deploy" -ForegroundColor Red
        Write-Host "Ejecuta 'railway logs' para ver los errores" -ForegroundColor Yellow
        exit 1
    }
} catch {
    Write-Host "`n❌ Error inesperado durante el deploy: $_" -ForegroundColor Red
    exit 1
}
