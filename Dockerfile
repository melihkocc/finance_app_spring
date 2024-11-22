# 1. Java OpenJDK 17 imajını kullanıyoruz
FROM openjdk:17-jdk-slim

# 2. Çalışma dizinini /app olarak ayarlıyoruz
WORKDIR /app

# 3. Maven'i yükleyelim (derleme için)
RUN apt-get update && apt-get install -y maven

# 4. GitHub'dan projeyi çekmek için gerekli bağımlılıkları ve dosyaları kopyalıyoruz
COPY . .

# 5. Projeyi derliyoruz (Maven kullanarak)
RUN mvn clean install

# 6. Uygulamayı çalıştıracak .jar dosyasını kopyalıyoruz
# (Derleme işlemi sonucunda target dizininde oluşan .jar dosyasını alacağız)
COPY target/*.jar app.jar

# 7. Uygulamayı çalıştırıyoruz
CMD ["java", "-jar", "app.jar"]
