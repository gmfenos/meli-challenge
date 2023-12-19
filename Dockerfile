FROM eclipse-temurin:17-jdk-jammy
MAINTAINER gmfenos
COPY target/challenge-0.0.1.jar challenge-0.0.1.jar
ENTRYPOINT ["java","-jar","/challenge-0.0.1.jar"]