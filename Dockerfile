FROM maven:3.8.1-openjdk-17 AS builder
COPY ./ ./
RUN mvn clean package -DskipTests

FROM adoptopenjdk/openjdk17:alpine-jre
COPY --from=builder /target/autopark-0.0.1-SNAPSHOT.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]