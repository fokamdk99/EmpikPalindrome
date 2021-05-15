FROM openjdk:16-alpine3.13
WORKDIR /app
COPY mvnw pom.xml ./
COPY .mvn/ .mvn
COPY src ./src
EXPOSE 2223

CMD ["./mvnw", "spring-boot:run"]