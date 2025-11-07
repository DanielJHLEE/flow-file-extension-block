# 1️⃣ JDK 21 기반 slim 이미지 사용
FROM openjdk:21-jdk-slim

# 2️⃣ JAR 파일 복사 (이름 정확히 맞춰야 함)
ARG JAR_FILE=build/libs/file-extension-block-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

# 3️⃣ 애플리케이션 실행 명령
ENTRYPOINT ["java", "-jar", "/app.jar"]

# 4️⃣ 포트 오픈 (Spring Boot 기본 8080)
EXPOSE 8080
