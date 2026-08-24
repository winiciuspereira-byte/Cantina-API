# 🍔 Cantina API

API REST desenvolvida com **Java + Spring Boot + MySQL** para gerenciamento do catálogo de lanches de uma cantina escolar.

---

## 📋 Descrição do Projeto

Este projeto é a segunda parte do sistema da cantina escolar (SA 2), dando continuidade ao sistema de caixa desenvolvido anteriormente. A API permite o gerenciamento dinâmico do cardápio sem a necessidade de alterações diretas no código-fonte, resolvendo o problema de manutenção manual dos lanches.

## 🎯 Objetivo

Desenvolver uma API REST seguindo boas práticas de desenvolvimento back-end, aplicando:
- Arquitetura em camadas (Controller → Service → Repository)
- Operações CRUD completas
- Verbos HTTP corretos e códigos de status adequados
- Validação de dados de entrada com Bean Validation
- Tratamento centralizado de erros
- Persistência com Spring Data JPA e MySQL
- Separação entre Entidade e DTO
- Clean Code e boas práticas

---

## 🛠️ Tecnologias Utilizadas

| Tecnologia       | Versão  | Finalidade                        |
|------------------|---------|-----------------------------------|
| Java             | 17      | Linguagem principal               |
| Spring Boot      | 3.2.5   | Framework back-end                |
| Spring Data JPA  | 3.2.5   | Persistência com ORM              |
| Spring Validation| 3.2.5   | Validação de dados de entrada     |
| MySQL            | 8.x     | Banco de dados relacional         |
| Lombok           | 1.18.x  | Redução de boilerplate            |
| H2               | Runtime | Banco em memória para testes      |
| JUnit 5          | 5.x     | Testes unitários e integração     |
| Mockito          | 5.x     | Mock para testes                  |
| Maven            | 3.x     | Gerenciamento de dependências     |

---

## 📂 Estrutura do Projeto

```
cantina-api/
├── src/
│   ├── main/
│   │   ├── java/com/cantina/api/
│   │   │   ├── CantinaApiApplication.java   ← Classe principal
│   │   │   ├── controller/
│   │   │   │   └── LancheController.java    ← Endpoints REST
│   │   │   ├── service/
│   │   │   │   └── LancheService.java       ← Regras de negócio
│   │   │   ├── repository/
│   │   │   │   └── LancheRepository.java    ← Acesso ao banco
│   │   │   ├── model/
│   │   │   │   └── Lanche.java              ← Entidade JPA
│   │   │   ├── dto/
│   │   │   │   ├── LancheRequestDTO.java    ← Entrada (com validações)
│   │   │   │   ├── LancheResponseDTO.java   ← Saída completa
│   │   │   │   ├── LancheResumoDTO.java     ← Saída resumida (nome + preço)
│   │   │   │   └── ErroResponseDTO.java     ← Resposta de erro padronizada
│   │   │   └── exception/
│   │   │       ├── LancheNotFoundException.java  ← Exceção customizada
│   │   │       └── GlobalExceptionHandler.java  ← Tratamento centralizado
│   │   └── resources/
│   │       └── application.properties       ← Configurações
│   └── test/
│       ├── java/com/cantina/api/
│       │   ├── LancheServiceTest.java        ← Testes unitários
│       │   └── LancheControllerTest.java     ← Testes de integração
│       └── resources/
│           └── application.properties        ← Config de teste (H2)
├── pom.xml
└── README.md
```

---

## 🗄️ Modelo de Dados

### Tabela `lanches`

| Coluna        | Tipo           | Obrigatório | Descrição                    |
|---------------|----------------|-------------|------------------------------|
| id            | BIGINT (PK)    | Sim         | Identificador único          |
| nome          | VARCHAR(100)   | Sim         | Nome do lanche               |
| descricao     | VARCHAR(500)   | Não         | Descrição detalhada          |
| preco         | DECIMAL(10,2)  | Sim         | Preço (valor positivo)       |
| categoria     | VARCHAR(50)    | Não         | Ex: Lanche, Bebida, Sobremesa|
| disponivel    | BOOLEAN        | Sim         | Se está disponível no cardápio|
| criado_em     | DATETIME       | Sim         | Data/hora de cadastro        |
| atualizado_em | DATETIME       | Sim         | Data/hora da última atualização|

---

## 🌐 Endpoints da API

Base URL: `http://localhost:8080/api/lanches`

### Funcionalidades Obrigatórias

| Método   | Endpoint              | Descrição                        | Status de Sucesso |
|----------|-----------------------|----------------------------------|-------------------|
| `POST`   | `/api/lanches`        | Cadastrar novo lanche            | `201 Created`     |
| `GET`    | `/api/lanches`        | Listar todos (apenas nome+preço) | `200 OK`          |
| `GET`    | `/api/lanches/{id}`   | Consultar lanche completo por ID | `200 OK`          |
| `PUT`    | `/api/lanches/{id}`   | Atualizar lanche por ID          | `200 OK`          |
| `DELETE` | `/api/lanches/{id}`   | Remover lanche por ID            | `204 No Content`  |

### Endpoints Bônus

| Método | Endpoint                          | Descrição                  | Status |
|--------|-----------------------------------|----------------------------|--------|
| `GET`  | `/api/lanches/disponiveis`        | Listar lanches disponíveis | `200`  |
| `GET`  | `/api/lanches/categoria/{cat}`    | Listar por categoria       | `200`  |

---

## 📝 Exemplos de Uso

### Cadastrar Lanche
```http
POST /api/lanches
Content-Type: application/json

{
  "nome": "X-Burguer",
  "descricao": "Hambúrguer artesanal com queijo e alface",
  "preco": 12.50,
  "categoria": "Lanche",
  "disponivel": true
}
```

**Resposta (201 Created):**
```json
{
  "id": 1,
  "nome": "X-Burguer",
  "descricao": "Hambúrguer artesanal com queijo e alface",
  "preco": 12.50,
  "categoria": "Lanche",
  "disponivel": true,
  "criadoEm": "2024-06-01T10:30:00",
  "atualizadoEm": "2024-06-01T10:30:00"
}
```

### Listar Todos (Cardápio Resumido)
```http
GET /api/lanches
```

**Resposta (200 OK):**
```json
[
  { "id": 1, "nome": "X-Burguer", "preco": 12.50 },
  { "id": 2, "nome": "Suco de Laranja", "preco": 5.00 }
]
```

### Consultar por ID
```http
GET /api/lanches/1
```

### Atualizar Lanche
```http
PUT /api/lanches/1
Content-Type: application/json

{
  "nome": "X-Burguer Duplo",
  "descricao": "Dois hambúrgueres artesanais",
  "preco": 18.00,
  "categoria": "Lanche",
  "disponivel": true
}
```

### Remover Lanche
```http
DELETE /api/lanches/1
```

---

## ⚠️ Tratamento de Erros

### Lanche não encontrado (404)
```json
{
  "status": 404,
  "erro": "Recurso não encontrado",
  "mensagens": ["Lanche não encontrado com o ID: 99"],
  "timestamp": "2024-06-01T10:35:00"
}
```

### Dados inválidos (400)
```json
{
  "status": 400,
  "erro": "Dados de entrada inválidos",
  "mensagens": [
    "nome: O nome do lanche é obrigatório.",
    "preco: O preço deve ser um valor positivo."
  ],
  "timestamp": "2024-06-01T10:35:00"
}
```

---

## ⚙️ Como Executar

### Pré-requisitos
- Java 17+
- Maven 3.6+
- MySQL 8.x

### 1. Configurar o banco de dados

```sql
CREATE DATABASE cantina_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. Configurar credenciais

Edite `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/cantina_db?createDatabaseIfNotExist=true
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
```

### 3. Executar a aplicação

```bash
# Clonar o repositório
git clone https://github.com/seu-usuario/cantina-api.git
cd cantina-api

# Executar
./mvnw spring-boot:run
```

### 4. Executar os testes

```bash
./mvnw test
```

A API estará disponível em: `http://localhost:8080`

---

## ✅ Validações Aplicadas

| Campo       | Regra                                               |
|-------------|-----------------------------------------------------|
| `nome`      | Obrigatório, entre 2 e 100 caracteres               |
| `descricao` | Opcional, máximo 500 caracteres                     |
| `preco`     | Obrigatório, valor positivo, até 8 dígitos inteiros |
| `categoria` | Opcional, máximo 50 caracteres                      |
| `disponivel`| Opcional, padrão `true`                             |

---

## 👨‍💻 Autor

Desenvolvido como parte da Situação de Aprendizagem 2 (SA 2) — Programação Back-end  
**Escola SENAI "Ricardo Lerner"**
