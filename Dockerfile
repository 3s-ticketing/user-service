FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

COPY . .

ARG GPR_USER
ARG GPR_TOKEN

ENV GPR_USER=${GPR_USER}
ENV GPR_TOKEN=${GPR_TOKEN}

RUN chmod +x ./gradlew
RUN ./gradlew clean bootJar -x test --no-daemon

RUN JAR_FILE=$(find build/libs -name "*.jar" ! -name "*plain.jar" | head -n 1) && cp "$JAR_FILE" app.jar

FROM eclipse-temurin:17-jre

WORKDIR /app

ENV TZ=Asia/Seoul

COPY --from=build /app/app.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]