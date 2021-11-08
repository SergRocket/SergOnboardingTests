FROM openjdk:11
RUN mkdir /app
COPY /SergOnboardingTests/src/test/LoginTest.java/ /app
WORKDIR /app
CMD java Main
