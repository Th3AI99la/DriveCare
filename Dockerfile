# ---- Estágio 1: Build ----
FROM maven:3.9-eclipse-temurin-17 AS build

# Define o diretório de trabalho dentro do container.
WORKDIR /app

# Copia primeiro o pom.xml para aproveitar o cache do Docker.
COPY pom.xml .

# Copia o resto do código fonte.
COPY src ./src

# Executa o build do Maven para gerar o arquivo .jar, pulando os testes.
RUN mvn clean package -DskipTests


# ---- Estágio 2: Run ----
# Usamos uma imagem bem menor, apenas com o Java 17 para rodar (JRE),
# o que torna sua aplicação final mais leve e segura.
FROM eclipse-temurin:17-jre

# Define o diretório de trabalho.
WORKDIR /app

# O nome do arquivo vem do <artifactId> e <version> no seu pom.xml.
COPY --from=build /app/target/project-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta 8080 (padrão do Spring Boot). O Render irá mapeá-la automaticamente.
EXPOSE 8080

# Comando para iniciar a aplicação quando o container rodar.
CMD ["java", "-jar", "app.jar"]