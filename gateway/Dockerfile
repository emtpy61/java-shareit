FROM amazoncorretto:11
COPY target/*.jar /shareit-gateway.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/shareit-gateway.jar"]