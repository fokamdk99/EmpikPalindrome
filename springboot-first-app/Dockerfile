FROM adoptopenjdk:11-jre-hotspot
VOLUME /tmp
RUN ["springboot-first-app/./mvnw clean package"]
ADD target/springboot-first-app-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 2223