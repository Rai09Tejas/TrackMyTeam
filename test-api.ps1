# TrackMyTeam API Test Script
# This script demonstrates the basic API functionality

Write-Host "=== TrackMyTeam API Test Script ===" -ForegroundColor Cyan
Write-Host ""

# Base URL
$baseUrl = "http://localhost:8080"
$token = ""

# Test 1: Register a new user
Write-Host "1. Registering a new user..." -ForegroundColor Yellow
$registerBody = '{"username":"testuser2","email":"test2@example.com","password":"test123"}'

try {
    $registerResponse = Invoke-WebRequest -Uri "$baseUrl/api/auth/register" -Method POST -Headers @{"Content-Type"="application/json"} -Body $registerBody
    $token = $registerResponse.Content
    Write-Host "User registered successfully!" -ForegroundColor Green
    Write-Host "JWT Token: $token" -ForegroundColor Gray
    Write-Host ""
}
catch {
    Write-Host "User already exists or registration failed, trying login..." -ForegroundColor Yellow
}

# Test 2: Login
Write-Host "2. Logging in..." -ForegroundColor Yellow
$loginBody = '{"username":"testuser2","password":"test123"}'

try {
    $loginResponse = Invoke-WebRequest -Uri "$baseUrl/api/auth/login" -Method POST -Headers @{"Content-Type"="application/json"} -Body $loginBody
    $token = $loginResponse.Content
    Write-Host "Login successful!" -ForegroundColor Green
    Write-Host "JWT Token: $token" -ForegroundColor Gray
    Write-Host ""
}
catch {
    Write-Host "Login failed: $_" -ForegroundColor Red
    exit 1
}

# Test 3: Create a task
Write-Host "3. Creating a task..." -ForegroundColor Yellow
$taskBody = '{"title":"Complete project documentation","description":"Write comprehensive documentation","status":"TODO","priority":"HIGH","dueDate":"2025-12-15T10:00:00"}'

try {
    $createTaskResponse = Invoke-WebRequest -Uri "$baseUrl/api/tasks" -Method POST -Headers @{"Content-Type"="application/json";"Authorization"="Bearer $token"} -Body $taskBody
    $createdTask = $createTaskResponse.Content | ConvertFrom-Json
    Write-Host "Task created successfully!" -ForegroundColor Green
    Write-Host "Task ID: $($createdTask.id)" -ForegroundColor Gray
    Write-Host ""
}
catch {
    Write-Host "Task creation failed: $_" -ForegroundColor Red
}

# Test 4: Get all tasks
Write-Host "4. Fetching all tasks..." -ForegroundColor Yellow
try {
    $getTasksResponse = Invoke-WebRequest -Uri "$baseUrl/api/tasks" -Method GET -Headers @{"Authorization"="Bearer $token"}
    $tasks = $getTasksResponse.Content | ConvertFrom-Json
    Write-Host "Retrieved $($tasks.Count) task(s)!" -ForegroundColor Green
    foreach ($task in $tasks) {
        Write-Host "  - [$($task.id)] $($task.title) - Status: $($task.status)" -ForegroundColor Gray
    }
    Write-Host ""
}
catch {
    Write-Host "Failed to fetch tasks: $_" -ForegroundColor Red
}

Write-Host "=== Tests completed! ===" -ForegroundColor Cyan
Write-Host "H2 Console: http://localhost:8080/h2-console" -ForegroundColor Gray
