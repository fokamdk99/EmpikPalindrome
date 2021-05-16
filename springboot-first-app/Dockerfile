FROM maven:3.8.1-openjdk-11-slim AS MAVEN_BUILD1

COPY pom.xml /build/
COPY src /build/src/

WORKDIR /build/
RUN mvn package

FROM openjdk:11.0.11-9-jre-slim-buster

WORKDIR /app

COPY --from=MAVEN_BUILD1 /build/target/springboot-first-app-0.0.1-SNAPSHOT.jar /app/
ENTRYPOINT ["java", "-jar", "springboot-first-app-0.0.1-SNAPSHOT.jar"]