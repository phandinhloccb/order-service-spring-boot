FROM openjdk:17-jre-slim
RUN yum -y update && yum install -y shadow-utils
WORKDIR /app
RUN useradd --shell /bin/bash app
USER app
COPY build/libs/*.jar /app/app.jar
CMD ["java", "-jar", "app.jar"]