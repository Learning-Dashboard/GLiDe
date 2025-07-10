FROM gradle:jdk21 AS builder
WORKDIR /app

COPY build.gradle settings.gradle /app/

COPY src /app/src

RUN gradle build --no-daemon

FROM openjdk:21
ARG JAR_FILE=/app/build/libs/*.jar
COPY --from=builder ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]