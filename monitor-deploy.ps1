#!/usr/bin/env pwsh
# Script para monitorear el deployment de Railway

Write-Host "Monitoreando deployment..." -ForegroundColor Cyan
Write-Host "Presiona Ctrl+C para detener`n" -ForegroundColor Yellow

$previousStatus = ""

while ($true) {
    $deployments = railway deployment list 2>&1 | Out-String
    
    # Extraer el primer deployment (el mas reciente)
    if ($deployments -match "(\S+)\s+\|\s+(\w+)\s+\|\s+(.+)") {
        $id = $matches[1]
        $status = $matches[2]
        $time = $matches[3]
        
        if ($status -ne $previousStatus) {
            $timestamp = Get-Date -Format "HH:mm:ss"
            
            switch ($status) {
                "DEPLOYING" { 
                    Write-Host "[$timestamp] DESPLEGANDO..." -ForegroundColor Yellow 
                }
                "SUCCESS" { 
                    Write-Host "[$timestamp] DEPLOY EXITOSO!" -ForegroundColor Green
                    Write-Host "`nAbriendo aplicacion..." -ForegroundColor Cyan
                    railway open
                    break
                }
                "FAILED" { 
                    Write-Host "[$timestamp] DEPLOY FALLIDO" -ForegroundColor Red
                    Write-Host "`nVer logs con: railway logs" -ForegroundColor Yellow
                    break
                }
                "BUILDING" { 
                    Write-Host "[$timestamp] CONSTRUYENDO..." -ForegroundColor Cyan 
                }
            }
            
            $previousStatus = $status
        }
    }
    
    Start-Sleep -Seconds 5
}
