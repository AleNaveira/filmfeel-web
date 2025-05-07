# Usa una imagen base con Java 17 ya instalado
FROM openjdk:17-jdk-slim

# Define el nombre del archivo .jar que se va a copiar
ARG JAR_FILE=target/FilmFeel-0.0.1-SNAPSHOT.jar

# Copia ese archivo desde tu proyecto al contenedor y lo renombra como app.jar
COPY ${JAR_FILE} app.jar

# Comando que se ejecuta cuando arranca el contenedor
ENTRYPOINT ["java", "-jar", "/app.jar"]
