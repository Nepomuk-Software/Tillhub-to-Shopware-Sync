FROM openjdk:11

ENV JAVA_XMX_SIZE "1g"

WORKDIR /home/wolke7

EXPOSE 8080

COPY wolke7-connector-artifact/* /opt

ENTRYPOINT ["java","-jar","/opt/wolke7-connector-0.0.1-SNAPSHOT.jar"]
