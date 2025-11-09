# 🚀 Pátio Api - API Check-in e Check-out de Motos

Este projeto é uma API REST desenvolvida em Java 17 utilizando Spring Boot, que tem como objetivo gerenciar o check-in e check-out de motos em um pátio. O sistema permite o cadastro de motos e o controle de suas movimentações (entrada e saída).

## 🏗️ Tecnologias Utilizadas

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Azure SQL
- Bean Validation
- Lombok
- Maven
- Docker
- Tratamento centralizado de erros com ExceptionHandler

## 🔗 Endpoints Disponíveis

### 🏍️ Motos
- `GET /motos` — Lista todas as motos (com paginação e ordenação)
- `POST /motos` — Cadastra uma nova moto
- `GET /motos/search?placa={placa}` — Busca moto pela placa (com paginação)

### 📄 Registros (Movimentações)
- `POST /registros/checkin` — Realiza o check-in da moto no pátio
- `PUT /registros/checkout` — Realiza o check-out da moto
- `GET /registros?placa={placa}` — Lista os registros de uma moto (com paginação)

## 📦 Entidades

- `Moto`: placa, modelo
- `Registro`: data e hora de check-in e check-out, associado à moto

## 🔗 Relacionamento

- Uma moto pode ter vários registros de entrada e saída.

## ⚙️ Como Executar (Deploy e Testes)

**Pré-requisitos:**
- Java 17 (JDK)
- Maven

**Passos:**

1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/joaovictords11/Devops-sprint3.git
    cd Devops-sprint3
    ```

2.  **Compile e empacote o projeto:**
    Use o Maven para gerar o arquivo `.jar`.
    ```bash
    mvn clean package
    ```

3.  **Execute a aplicação:**
    Após a compilação, o arquivo `.jar` estará na pasta `target/`. Execute-o com o seguinte comando:
    ```bash
    java -jar target/patio-api.jar
    ```

4.  A API estará disponível em `http://localhost:8080`.

## 🧪 Teste via API

### Cadastrar uma nova moto

**Endpoint:** `POST /motos`
```bash
curl -X POST http://localhost:8080/motos \
-H "Content-Type: application/json" \
-d '{
  "placa": "BRA2E19",
  "modelo": "Honda PCX 150"
}'

