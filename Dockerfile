# Используем образ Maven для сборки приложения
FROM maven:3.8.1-openjdk-17 AS builder
COPY . /home/tanya/git/autopark
WORKDIR /home/tanya/git/autopark
RUN mvn clean package -DskipTests

# Собранное приложение скопируем в образ с OpenJDK 17
FROM openjdk:17-jdk-alpine
COPY --from=builder /home/tanya/git/autopark/target/autopark-0.0.1-SNAPSHOT.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
