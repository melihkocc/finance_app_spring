# 1. Temel imaj olarak openjdk kullanıyoruz
FROM openjdk:17-jdk-slim

# 2. Uygulamanızın jar dosyasını yerleştireceğiniz klasörü oluşturuyoruz
WORKDIR /app

# 3. Uygulamanızın .jar dosyasını konteynıra kopyalıyoruz
COPY target/demo-0.0.1-SNAPSHOT.jar app.jar

# 4. Uygulamayı çalıştırıyoruz
ENTRYPOINT ["java", "-jar", "app.jar"]

# 5. Konteynırın dinleyeceği portu belirtiyoruz
EXPOSE 8080
