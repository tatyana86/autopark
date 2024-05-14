FROM maven:3.8.1-openjdk-17 AS builder
COPY . /app
WORKDIR /app
RUN mvn clean package -DskipTests

FROM openjdk:23-ea-17-jdk-bullseye
COPY --from=builder /app/target/autopark-0.0.1-SNAPSHOT.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
