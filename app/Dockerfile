FROM openjdk:8u181

ADD target/app*.jar /opt/application.jar

ENTRYPOINT ["java","-jar","/opt/application.jar"]