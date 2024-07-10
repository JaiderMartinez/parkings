FROM amazoncorretto:17-alpine3.18-jdk AS builder

# Establece la zona horaria y configura JLink para crear un JRE mínimo
RUN apk add --no-cache tzdata \
    && cp /usr/share/zoneinfo/America/Bogota /etc/localtime \
    && echo "America/Bogota" > /etc/timezone \
    && apk add --no-cache zip \
    && jlink --compress=2 --module-path /usr/lib/jvm/java-17-amazon-corretto/jmods/ \
    --add-modules java.base,java.logging,java.xml,jdk.unsupported,java.sql,java.naming,java.desktop,java.management,java.security.jgss,java.instrument,jdk.management,jdk.crypto.cryptoki \
    --no-header-files --no-man-pages --output /custom-jre

# Copia y compila la aplicación
COPY **/app-service/build/libs/*.jar /app/parking.jar

# Etapa final: crea una imagen mínima que solo contiene el JRE y la aplicación
FROM alpine:3.18

RUN apk add --no-cache tzdata \
    && cp /usr/share/zoneinfo/America/Bogota /etc/localtime \
    && echo "America/Bogota" > /etc/timezone \
    && addgroup -S user \
    && adduser -S user -G user

USER user

ARG VAR_PROFILE=dev

# Define variables de entorno
ENV JAVA_HOME=/opt/jdk
ENV PATH=$JAVA_HOME/bin:$PATH
ENV PROFILE=${VAR_PROFILE}
ENV JAVA_OPTS=" -Dspring.profiles.active=${PROFILE} -Duser.timezone=America/Bogota"

# Copia el JRE personalizado y la aplicación desde la etapa de construcción
COPY --from=builder /custom-jre /opt/jdk/
COPY --from=builder /app/parking.jar /app/parking.jar

WORKDIR /app

EXPOSE 8090

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS  -jar /app/parking.jar" ]
