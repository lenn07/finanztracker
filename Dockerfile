FROM eclipse-temurin:17-jdk-jammy AS builder

WORKDIR /app

COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

COPY src src

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

CMD ["sh", "-c", "java \
  -Xms64m \
  -Xmx256m \
  -XX:MaxMetaspaceSize=96m \
  -XX:+UseContainerSupport \
  -XX:+ExitOnOutOfMemoryError \
  -XX:+HeapDumpOnOutOfMemoryError \
  -Djava.security.egd=file:/dev/./urandom \
  -Dserver.port=${PORT:-8080} \
  -jar /app/app.jar"]
