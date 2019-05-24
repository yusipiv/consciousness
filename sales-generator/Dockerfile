FROM openjdk:8-jdk-alpine

VOLUME /tmp

COPY target/*.jar sales-generator-app.jar

ENTRYPOINT ["java","-jar","sales-generator-app.jar"]