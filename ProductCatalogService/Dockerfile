# Step 1: Use a base image with Java installed
FROM openjdk:17-jdk-slim

# Step 2: Set the working directory in the container
WORKDIR /app

# Step 3: Copy the Spring Boot JAR file into the container
COPY target/ProductCatalogService-0.0.1-SNAPSHOT.jar app.jar

# Step 4: Expose the port on which the application runs
EXPOSE 8080

# Step 5: Run the JAR file when the container starts
#CMD ["java", "-jar", "app.jar"]
