FROM openjdk:8-jre-slim-buster
ADD target/inventorymanagement-0.0.1-SNAPSHOT.jar inventorymanagement-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","inventorymanagement-0.0.1-SNAPSHOT.jar"]
