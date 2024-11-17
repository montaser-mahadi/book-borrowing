# Stage 1: Build Stage
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /build

COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests


# Stage 2: Runtime Stage
FROM amazoncorretto:17
ARG PROFILE=dev
ARG APP_VERSION=library-borrowing-service-0.0.1-SNAPSHOT

# Copy the built JAR file from the build stage
WORKDIR /app
COPY --from=build /build/target/library-borrowing-service-*.jar /app/

# Extract the JAR version
#RUN APP_VERSION=$(ls /app | grep ./*jar | awk 'NR==2{split($0,a,"-"); print a[3]}' | awk '{sub(/.jar$/,"")}1')\
#    && echo "Building container with BSN v-$version"
EXPOSE 2304

# Run the application
ENV DB_URL=jdbc:postgresql://postgres-sql:5432/book_borrowing
ENV DB_USERNAME=book_borrowing
ENV DB_PASSWORD=book_borrowing
ENV MAILDEV_URL=localhost

ENV ACTIVE_PROFILE=${PROFILE}
ENV JAR_VERSION=${APP_VERSION}

CMD ["java", "-Dspring.profiles.active=dev", "-jar", "library-borrowing-service-0.0.1-SNAPSHOT.jar"]
