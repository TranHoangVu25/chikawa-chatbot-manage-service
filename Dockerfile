# # Sử dụng base image OpenJDK 21
# FROM eclipse-temurin:21-jdk-jammy
#
# # Thiết lập thư mục làm việc
# WORKDIR /app
#
# # Copy file jar vào container
# COPY target/search_service-0.0.1-SNAPSHOT.jar app.jar
#
# # Mở cổng ứng dụng
# EXPOSE 8083
#
# # Khởi chạy ứng dụng
# ENTRYPOINT ["java", "-jar", "app.jar"]
# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -Dmaven.test.skip=true

# Stage 2: Create the final runtime image
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]