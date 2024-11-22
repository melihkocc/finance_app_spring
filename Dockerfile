# 1. Java OpenJDK 17 imajını kullanıyoruz
FROM openjdk:17-jdk-slim

# 2. Çalışma dizinini /app olarak ayarlıyoruz
WORKDIR /app

# 3. Maven'i yükleyelim (derleme için)
RUN apt-get update && apt-get install -y maven

# 4. Proje dosyalarını konteynıra kopyalıyoruz
COPY . .

# 5. Projeyi derliyoruz (Maven kullanarak)
RUN mvn clean install

# 6. target klasörünü kontrol et ve .jar dosyasını kopyala
RUN ls -al target/  # Burada target dizini ve içeriklerini listeleyebilirsiniz

# 7. Uygulamayı çalıştıracak .jar dosyasını kopyalıyoruz
COPY target/demo-0.0.1-SNAPSHOT.jar app.jar

# 8. Uygulamayı çalıştırıyoruz
CMD ["java", "-jar", "app.jar"]
