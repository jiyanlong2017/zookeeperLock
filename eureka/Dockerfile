FROM 192.168.16.128:5000/registry/java:8
VOLUME /tmp
ADD ./target/registry.jar /app.jar

EXPOSE 8761
ENTRYPOINT ["java","-jar","/app.jar"]