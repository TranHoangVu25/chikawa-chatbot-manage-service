# Sử dụng image OpenJDK
FROM eclipse-temurin:17-jdk-alpine

# Thư mục làm việc trong container
WORKDIR /app

# Copy file jar (thay YOUR_APP.jar bằng tên file jar thực tế)
COPY target/chatbot_manage_service-0.0.1-SNAPSHOT.jar app.jar

# Expose port Spring Boot
EXPOSE 8083

# Lệnh chạy ứng dụng
ENTRYPOINT ["java","-jar","app.jar"]
