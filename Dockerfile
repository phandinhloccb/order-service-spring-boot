FROM openjdk:17-jre-slim
RUN apt-get update && apt-get install -y --no-install-recommends passwd
WORKDIR /app
RUN useradd --shell /bin/bash app
USER app
COPY build/libs/*.jar /app/app.jar
CMD ["java", "-jar", "app.jar"]