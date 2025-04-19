FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Скопировать исходники и собрать .jar внутри контейнера
COPY . .

# Собрать проект (если используешь Maven)
RUN ./mvnw clean package -DskipTests

# Копировать jar из target
COPY target/autoservice-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]