# 使用 Eclipse Temurin 11 作为基础镜像（OpenJDK官方镜像已废弃）
FROM eclipse-temurin:11-jre

# 设置工作目录
WORKDIR /app

# 复制 jar 文件
COPY target/*.jar app.jar

# 暴露端口（会被 docker-compose 覆盖）
EXPOSE 8080

# 设置 JVM 参数
ENV JAVA_OPTS="-Xms256m -Xmx512m"

# 启动应用
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
