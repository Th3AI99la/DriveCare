# DriveCare

![Spring Boot](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql)
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)

## 🚀 Acesso ao Projeto

**A aplicação está no ar! Acesse pelo link abaixo:**

[https://drivecare-app.onrender.com](https://drivecare-app.onrender.com)

*(Substitua pelo link correto do seu Web Service se for diferente)*

## 📝 Descrição
O DriveCare é um sistema web para controle de manutenção veicular, desenvolvido para auxiliar proprietários a registrarem seus veículos e acompanharem as manutenções realizadas, como troca de óleo, pneus, revisões e outras intervenções importantes. O sistema também oferece alertas periódicos para lembrar os usuários sobre futuras manutenções, garantindo assim a segurança e a boa conservação dos veículos.

## 🛠️ Tecnologias Utilizadas
- **Backend:** Java 17, Spring Boot 3
- **Frontend:** Thymeleaf, Bootstrap 5
- **Banco de Dados:** PostgreSQL
- **Persistência:** Spring Data JPA
- **Build:** Apache Maven
- **Deploy:** Docker, Render

## ⚙️ Como Executar o Projeto Localmente

Siga os passos abaixo para rodar o DriveCare no seu ambiente de desenvolvimento.

**Pré-requisitos:**
- Java 17 ou superior
- Apache Maven 3.9+
- PostgreSQL instalado e em execução

**Passos:**
1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/Th3AI99la/DriveCare.git](https://github.com/Th3AI99la/DriveCare.git)
    cd DriveCare
    ```

2.  **Configure o banco de dados local:**
    - Crie um banco de dados no seu PostgreSQL chamado `drivecare_local` (ou o nome que preferir).
    - Crie uma cópia do arquivo `application.properties` e renomeie para `application-dev.properties` dentro da pasta `src/main/resources/`.
    - Edite o `application-dev.properties` com suas credenciais locais:
      ```properties
      # Configurações para o ambiente de desenvolvimento local
      spring.datasource.url=jdbc:postgresql://localhost:5432/drivecare_local
      spring.datasource.username=seu_usuario_postgres
      spring.datasource.password=sua_senha_postgres
      spring.jpa.hibernate.ddl-auto=update
      ```

3.  **Execute a aplicação:**
    - Pelo terminal, na raiz do projeto, execute o comando Maven:
      ```bash
      mvn spring-boot:run -Dspring-boot.run.profiles=dev
      ```
    - A aplicação estará disponível em `http://localhost:8080`.

## ☁️ Configuração para Deploy (Render)

Este projeto está configurado para deploy contínuo na plataforma Render utilizando Docker.

1.  **Dockerfile:** O `Dockerfile` na raiz do projeto contém todas as instruções para criar uma imagem otimizada da aplicação.

2.  **Variáveis de Ambiente:** Para o deploy funcionar, as seguintes variáveis de ambiente devem ser configuradas no serviço do Render:
    - `DB_HOST`: Host do banco de dados do Render.
    - `DB_PORT`: Porta do banco (`5432`).
    - `DB_NAME`: Nome do banco de dados.
    - `DB_USER`: Usuário do banco de dados.
    - `DB_PASSWORD`: Senha do banco de dados.

O arquivo `application.properties` já está configurado para ler essas variáveis e montar a URL de conexão automaticamente.
