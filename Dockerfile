FROM openjdk:11-jdk-slim-bullseye as build-stage

WORKDIR /app
COPY . /app/

RUN ./gradlew bootJar

FROM openjdk:11-jdk-slim-bullseye

COPY --from=build-stage /app/app/build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]