FROM openjdk:17-jdk-alpine
EXPOSE 8089
ADD target/timesheet2021-3.0.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]