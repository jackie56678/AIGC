#!/bin/sh

# 启动 Java 应用程序
cd /ssadmin-server
java ${JAVA_OPTS} -jar app.jar $ARGS &

# 启动 Python HTTP 服务器
cd /ssadmin-server/source
python3 -m http.server 8000


