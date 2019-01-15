FROM openjdk:8-jdk-alpine
VOLUME /tmp 
ADD eureka-1.0-SNAPSHOT.jar eureka.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/eureka.jar"]