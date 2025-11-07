# 1️⃣ JDK 21 기반 slim 이미지 사용
FROM eclipse-temurin:21-jdk

# 2️⃣ Gradle 빌드 도구 복사
COPY . /app
WORKDIR /app

# 3️⃣ Gradle Wrapper 실행으로 빌드
RUN ./gradlew clean build -x test

# 4️⃣ 빌드된 JAR 실행 (plain JAR 말고 일반 JAR 사용)
CMD ["java", "-jar", "build/libs/file-extension-block-0.0.1-SNAPSHOT.jar"]

# 5️⃣ 포트 오픈
EXPOSE 8080

# 6️⃣ 환경 변수
ENV SPRING_PROFILES_ACTIVE=render
