# Use a base image with Java installed
FROM openjdk:11

# Set the working directory inside the container
WORKDIR /app

# Copy the necessary files from the host to the container
COPY ./src /app/src
COPY ./out /app/out

# Compile the Java source code
RUN javac -cp /app/src -d /app/out /app/src/com/company/*.java

# Specify the command to run your application
CMD ["java", "-cp", "/app/out", "com.company.EcommerceApplication"]
