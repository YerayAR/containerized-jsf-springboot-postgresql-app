#!/usr/bin/env pwsh
# Script para desplegar todos los servicios en Railway

Write-Host "🚀 Iniciando despliegue en Railway..." -ForegroundColor Cyan

# Array de servicios a desplegar
$services = @("postgres", "app")

foreach ($service in $services) {
    Write-Host "`n📦 Desplegando servicio: $service" -ForegroundColor Yellow
    
    try {
        railway up --service $service
        
        if ($LASTEXITCODE -eq 0) {
            Write-Host "✅ $service desplegado correctamente" -ForegroundColor Green
        } else {
            Write-Host "❌ Error al desplegar $service" -ForegroundColor Red
            exit 1
        }
    } catch {
        Write-Host "❌ Error inesperado: $_" -ForegroundColor Red
        exit 1
    }
}

Write-Host "`n🎉 Despliegue completado con éxito" -ForegroundColor Green
