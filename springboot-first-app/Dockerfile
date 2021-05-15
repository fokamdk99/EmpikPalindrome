FROM adoptopenjdk:11-jre-hotspot
VOLUME /tmp
ADD target/springboot-first-app-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 2223