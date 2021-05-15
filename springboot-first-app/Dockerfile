FROM adoptopenjdk:11-jre-hotspot
VOLUME /tmp
RUN ["cd springboot-first-app/"]
RUN ["./mvnw clean package"]
ADD springboot-first-app/target/springboot-first-app-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 2223