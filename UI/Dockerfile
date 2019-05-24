FROM openjdk:8-jdk-alpine

VOLUME /tmp

COPY target/*.jar ui-app.jar

ENTRYPOINT ["java","-jar","ui-app.jar"]