FROM openjdk:8-jdk-alpine

VOLUME /tmp

COPY target/*.jar privat-api.jar

ENTRYPOINT ["java","-jar","privat-api.jar"]