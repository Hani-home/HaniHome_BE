# ------------------------------
# 1단계: 빌드 단계 (build stage)
# ------------------------------

#컨테이너에서 gradle(8.7)과 jdk 17을 사용해서 빌드하겠다.
FROM gradle:8.7.0-jdk17 AS build

#컨테이너 내부에 /app 디렉토리를 만들어서 그 디렉토리에서 작업하겠다.
WORKDIR /app

#Dockerfile 기반으로 이미지 만들 때 docker build . 명령어를 실행함 (.는 현재 디렉토리 즉 내 로컬 디렉토리 = 소스코드 전체)
#현재 디렉토리(소스코드 전체)를 컨테이너의 /app으로 복사.
#도커파일 .과 ..은 각각 내 컴퓨터의 현재 디렉토리, 컨테이너의 현재 학업 디렉토리를 의미한다고 한다.
COPY . .

#gradle로 프로젝트 빌드 => JAR 파일 생성
RUN gradle clean build -x test

# ------------------------------
# 2단계: 런타임 단계 (runtime stage)
# ------------------------------




# 1. OpenJDK 17.0.14 기반 이미지 사용
FROM eclipse-temurin:17.0.14_7-jdk

# 2. 앱 실행 디렉토리 지정
WORKDIR /app

# 3. JAR 파일 복사
COPY --from=build /app/build/libs/*.jar app.jar

ENV SPRING_PROFILES_ACTIVE=docker

# 4. 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]