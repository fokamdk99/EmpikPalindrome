FROM maven:3.5.2-jdk-11-alpine AS MAVEN_BUILD

COPY pom.xml /build/
COPY src /build/src/

WORKDIR /build/
RUN mvn package

FROM openjdk:11-jre-alpine

WORKDIR /app

COPY --from=MAVEN_BUILD /build/target/springboot-first-app-0.1.0-SNAPSHOT.jar /app/
ENTRYPOINT ["java", "-jar", "springboot-first-app-0.1.0-SNAPSHOT.jar"]