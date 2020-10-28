FROM maven:3.6.1-jdk-14

RUN mkdir /app
WORKDIR /app

# Download dependencies once, not every build
COPY ./auction-challenge/pom.xml pom.xml
RUN mvn dependency:resolve compile package

COPY ./auction-challenge/src src
RUN mvn compile package

ENTRYPOINT ["java", "-jar", "target/auction-challenge-1.0-SNAPSHOT-jar-with-dependencies.jar"]
