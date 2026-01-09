@echo off
setlocal

echo ==== 检查 Docker Engine 状态 ====
set PATH=%PATH%;C:\Program Files\Docker\Docker\resources\bin

docker info >nul 2>&1
if errorlevel 1 (
    echo Docker 未运行，启动 Docker Desktop...
    start "" "C:\Program Files\Docker\Docker\Docker Desktop.exe"
) else (
    echo Docker 已在运行，跳过启动
)

echo ==== 等待 Docker Engine 就绪 ====
:wait_docker
docker info >nul 2>&1
if errorlevel 1 (
    echo Docker 尚未就绪，等待中...
    timeout /t 3 >nul
    goto wait_docker
)

echo ==== Docker 已就绪 ====

echo ==== 检查 Redis 容器状态 ====
set REDIS_RUNNING=
for /f "tokens=*" %%i in ('docker inspect -f "{{.State.Running}}" myredis 2^>nul') do set REDIS_RUNNING=%%i

if "%REDIS_RUNNING%"=="true" (
    echo ==== Redis 已在运行，跳过启动 ====
) else (
    echo ==== 启动 Redis ====
    rem 首次启动必写，后续不用：docker run -d -p 6379:6379 --name myredis redis:6.2.19
    docker start myredis
    echo ==== Redis 已就绪 ====
)

endlocal
