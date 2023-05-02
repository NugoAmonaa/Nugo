# Use a base image with Java installed
FROM openjdk:11

# Set the working directory inside the container
RUN mkdir /app

# Copy the necessary files from the host to the container
Copy out/production/untitled10/com/company/ /app
# Compile the Java source code

workdir /app
#RUN javac -d . EcommerceApplication.java

# Specify the command to run your application
CMD ["java", "com.company.EcommerceApplication"]

#cmd java EcommerceApplication