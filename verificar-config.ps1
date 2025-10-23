#!/usr/bin/env pwsh
# Script para verificar la configuración de Railway

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "🔍 VERIFICANDO CONFIGURACIÓN" -ForegroundColor Cyan
Write-Host "========================================`n" -ForegroundColor Cyan

Write-Host "📋 Variables de entorno del servicio jsf-backend:`n" -ForegroundColor Yellow
$vars = railway variables 2>&1 | Out-String

# Verificar variables críticas
$hasDatabase = $vars -match "DATABASE_URL"
$hasUser = $vars -match "PGUSER"
$hasPassword = $vars -match "PGPASSWORD"

if ($hasDatabase) {
    Write-Host "✅ DATABASE_URL configurado" -ForegroundColor Green
} else {
    Write-Host "❌ DATABASE_URL NO configurado" -ForegroundColor Red
}

if ($hasUser) {
    Write-Host "✅ PGUSER configurado" -ForegroundColor Green
} else {
    Write-Host "❌ PGUSER NO configurado" -ForegroundColor Red
}

if ($hasPassword) {
    Write-Host "✅ PGPASSWORD configurado" -ForegroundColor Green
} else {
    Write-Host "❌ PGPASSWORD NO configurado" -ForegroundColor Red
}

Write-Host "`n========================================`n" -ForegroundColor Cyan

if ($hasDatabase -and $hasUser -and $hasPassword) {
    Write-Host "🎉 ¡CONFIGURACIÓN CORRECTA!" -ForegroundColor Green
    Write-Host "Puedes desplegar con: railway up`n" -ForegroundColor Cyan
} else {
    Write-Host "⚠️  Faltan variables. Agrega en el dashboard:" -ForegroundColor Yellow
    Write-Host "1. Click en 'jsf-backend' → 'Variables'" -ForegroundColor White
    Write-Host "2. Agrega las variables faltantes`n" -ForegroundColor White
}

# Mostrar últimos deployments
Write-Host "📊 Últimos deployments:`n" -ForegroundColor Yellow
railway deployment list
