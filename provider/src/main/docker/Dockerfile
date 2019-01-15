FROM openjdk:8-jdk-alpine
VOLUME /tmp 
ADD provider-1.0-SNAPSHOT.jar provider.jar
#RUN bash -c 'touch /provider.jar'
#EXPOSE 7890
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/provider.jar"]