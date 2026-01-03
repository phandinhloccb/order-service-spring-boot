FROM openjdk:17-jre-slim
RUN apt-get update -y && apt-get install -y shadow-utils && apt-get clean && rm -rf /var/lib/apt/lists/*
WORKDIR /app
RUN useradd --shell /bin/bash app
USER app
COPY build/libs/*.jar /app/app.jar
CMD ["java", "-jar", "app.jar"]