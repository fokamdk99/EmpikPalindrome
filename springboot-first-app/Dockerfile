FROM openjdk:16-alpine3.13
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY src ./src
EXPOSE 2223
CMD ["./mvnw", "spring-boot:run"]