# --- Stage 1: build del frontend React con Vite ---
FROM node:20-slim AS frontend-build
WORKDIR /build/frontend
COPY frontend/package*.json ./
RUN npm ci
COPY frontend/ .
RUN npm run build

# --- Stage 2: build del backend Spring Boot (jar eseguibile) ---
FROM maven:3.9-eclipse-temurin-17 AS backend-build
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn -B -q package -DskipTests

# --- Stage 3: immagine finale, solo runtime ---
# Riproduce la stessa struttura di cartelle che hai in locale (jar + frontend/dist
# nella stessa working directory), cosi' WebConfig.java trova frontend/dist
# senza bisogno di modifiche: la ricerca parte da "." (qui /app) e lo trova subito.
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=backend-build /build/target/*.jar app.jar
COPY --from=frontend-build /build/frontend/dist ./frontend/dist

# Railway assegna la porta tramite la variabile PORT: la leggiamo e la
# passiamo a Spring Boot sovrascrivendo server.port a runtime.
ENV JAVA_OPTS=""
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dserver.port=${PORT:-8080} -jar app.jar"]
