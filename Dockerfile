# 1. Aşama: Maven ile uygulamayı derleyelim
FROM maven:3.8.4-openjdk-17-slim AS build

# Çalışma dizini oluşturuyoruz
WORKDIR /app

# Proje dosyalarını konteynıra kopyalıyoruz
COPY . .

# Maven ile projeyi derliyoruz
RUN mvn clean install

# 2. Aşama: Derlenmiş jar dosyasını final Docker imajına taşıyoruz
FROM openjdk:17-jdk-slim

# Çalışma dizini oluşturuyoruz
WORKDIR /app

# 1. aşamadan .jar dosyasını kopyalıyoruz
COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar app.jar

# Uygulamayı çalıştırıyoruz
CMD ["java", "-jar", "app.jar"]
