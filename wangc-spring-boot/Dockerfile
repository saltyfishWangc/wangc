# 基于openjdk8作为基础镜像
FROM openjdk:8-slim

# 作者维护信息
MAINTAINER cls-admin by msph "open@msok.com"

# 设置容器时区为亚洲/上海
ENV TZ Asia/Shanghai

# 应用工作目录
WORKDIR /app

# 复制应用jar包到容器中工作目录
COPY cls-eureka.jar /app/cls-eureka.jar

# 更新jar包的文件时间戳
RUN sh -c 'touch cls-eureka.jar'

# 容器暴露的服务端口
EXPOSE 7000

# 容器启动执行命令
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/cls-eureka.jar"]
