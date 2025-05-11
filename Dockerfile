# 1. OpenJDK 17.0.14 기반 이미지 사용
FROM eclipse-temurin:17.0.14_7-jdk

# 2. 앱 실행 디렉토리 지정
WORKDIR /app

# 3. JAR 파일 복사
COPY build/libs/*.jar app.jar

ENV SPRING_PROFILES_ACTIVE=docker

# 4. 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]