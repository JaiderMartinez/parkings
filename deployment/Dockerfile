FROM openjdk:17-alpine@sha256:4b6abae565492dbe9e7a894137c966a7485154238902f2f25e9dbd9784383d81

EXPOSE 8090

ARG VAR_PROFILE=dev

ENV PROFILE=${VAR_PROFILE}
ENV JAVA_OPTS=" -Dspring.profiles.active=${PROFILE} -Duser.timezone=America/Bogota"

WORKDIR /app

COPY **/app-service/build/libs/*.jar /app/parking.jar

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS  -jar /app/parking.jar" ]
