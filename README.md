# Translator API

API REST desenvolvida com **Spring Boot**, conectada a um banco de dados **PostgreSQL**, 
e preparada para deploy com **Docker**. A aplicação permite o cadastro e autenticação de tradutores, 
além da proteção de rotas via JWT.

## 🔧 Tecnologias Utilizadas

| Tecnologia                         | Descrição |
|-----------------------------------|-----------|
| **Java 21**                       | Linguagem principal da aplicação. |
| **Spring Boot**                   | Framework para criação da API. |
| **Spring Data JPA + Hibernate**   | Mapeamento objeto-relacional com PostgreSQL. |
| **Spring Security**               | Segurança da aplicação e proteção de rotas. |
| **Spring WebFlux**                | Suporte à programação reativa. |
| **Spring Validation**             | Validação de campos nas requisições. |
| **JWT (JJWT)**                    | Geração e validação de tokens JWT. |
| **PostgreSQL**                    | Banco de dados relacional. |
| **Google Cloud Translate**        | API de tradução para textos. |
| **Docker**                        | Containerização e deploy. |
| **Lombok**                        | Redução de boilerplate (getters, setters, etc). |
| **Spring Dotenv**                 | Carregamento de variáveis de ambiente via `.env`. |
| **Devtools**                      | Hot reload durante o desenvolvimento. |

---

## Requisitos para rodar o projeto localmente

- Java 21
- Postgres
- Docker


## Variáveis de Ambiente

Essas variáveis devem ser configuradas no painel da Render (ou num `.env` se for rodar localmente):

```env```
DATABASE_URL=jdbc:postgresql://<host>:<port>/<db>
DATABASE_USERNAME=<usuário do banco>
DATABASE_PASSWORD=<senha do banco>
SECRET_KEY=uma-chave-secreta-com-no-mínimo-32-caracteres
EXPIRATION_TIME=tempo-de-expiracao-em-milissegundos
PGADMIN_EMAIL=email@example.com
PGADMIN_PASSWORD=admin

- OBS: para gerar a secret key você pode usar o comando: openssl rand -base64 32 (ex para Debian Ubuntu). Esse comando irá gerar
algo como mKuz09QtxD93QUMCfjKw3cu5weO4AyZMyBa+pzSAt0A=. Depois, copie esse valor e atualize nas suas 
variáveis de ambiente.

## Como rodar o projeto localmente

### Se for rodar na sua máquina e usando o Postgres instalado localmente

```bash```
git clone https://github.com/seu-usuario/translator-api.git
cd translator-api

- Crie um arquivo .env na raiz do seu diretório com as variáveis de embiente necessárias mencionadas acima;
- Execute o projeto localmente garantindo que o seu banco de dados está acessível e as credenciais foram definidas corretamente


### Google credential

#### Como configurar a Google Cloud Translate API 

- Criar um projeto no Google Cloud
   Acesse https://console.cloud.google.com

- Clique em "Selecionar projeto" > "Novo Projeto"

- Dê um nome ao projeto (ex: translator-api) e clique em Criar

- No painel do projeto, vá até "APIs e serviços" > "Biblioteca"

- Busque por Cloud Translation API

- Clique nela e depois em "Ativar"

- Criar uma conta de serviço

- Vá para IAM & Admin > Contas de serviço

- Clique em "Criar conta de serviço"

- Dê um nome (ex: translator-api-sa)

- Clique em "Criar e continuar"

- Conceda a permissão: Editor (ou Tradutor se quiser limitar)

- Clique em "Concluído"

- Na lista de contas de serviço, clique na conta que você criou

- Vá em "Chaves" > "Adicionar chave" > "Criar nova chave"

- Escolha o tipo JSON e clique em "Criar"

- Um arquivo .json será baixado automaticamente. Guarde-o com segurança.

#### Criar variável de ambiente no IntelliJ

- Vá em Run > Edit Configurations...

- Selecione sua aplicação (Spring Boot) na lista

- Em Environment variables, clique no ícone ...

- Adicione:
  GOOGLE_APPLICATION_CREDENTIALS= {o json com suas credenciais}

- Clique em OK e aplique as mudanças.

## Rodando projeto com Docker

- Adicione suas credenciais em um arquivo .json na raiz do seu projeto;

- Adicione as linhas abaixo ao seu Dockerfile:
COPY credentials.json /app/.secrets/credentials.json (abaixo da linha que copia o .jar do projeto)
ENV GOOGLE_APPLICATION_CREDENTIALS=/app/.secrets/credentials.json (abaixo da linha que expõe a posta 8080)


- rode o comando docker-compose up --build

- isso vai gerar a imagem da aplicação e subir os containers da sua aplicação, do Postgres e do PgAdmin


## 🚀 Endpoints de Autenticação

### POST /api/auth/register
- request body:
- {
  "name": "Nome do Tradutor",
  "email": "email@dominio.com", (pode utilizar um email fictício (nome@mail.com), ainda não fazemos verificação de e-mails)
  "password": "senha-segura", (senha@123)
  "confirmPassword": "senha-segura" (senha@123)
  }

- request response:
- {
  "message": "Cadastro realizado com sucesso!",
  "name": "Nome do Tradutor",
  "email": "email@dominio.com"
  }

### POST /api/auth/login
- request body
  {
  "email": "email@dominio.com",
  "password": "senha-segura"
  }

- request response:
  {
  "token": "JWT_token_aqui"
  }

### POST /api/auth/logout
- request header Request Header:
Authorization: Bearer <JWT_token_aqui>

## 🚀 Endpoints para criação, listagem, edição e deleção de tradutores

### POST /api/translator (criar novo tradutor)
- request body
  {
  "name": "Nome do Tradutor",
  "email": "email@dominio.com",
  "password": "senha-segura",
  "confirmPassword": "senha-segura"
  }
- request header
  Authorization: Bearer <JWT_token_aqui>

- request response
  {
  "id": "UUID_do_tradutor",
  "name": "Nome do Tradutor",
  "email": "email@dominio.com"
  }

### PUT /api/translator/{id} (editar tradutor)
- path parameter 
id do tradutor
- request body
  {
  "name": "Novo Nome do Tradutor",
  "email": "novoemail@dominio.com",
  "password": "nova-senha",
  "confirmPassword": "nova-senha"
  }
- request header
Authorization: Bearer <JWT_token_aqui>

- request response
  {
  "id": "UUID_do_tradutor",
  "name": "Novo Nome do Tradutor",
  "email": "novoemail@dominio.com"
  }

### DELETE /api/translator/{id} (deletar tradutor)
- Path Parameter:
id: O ID do tradutor a ser deletado (UUID).

- response: 204 (no content)

### GET /api/translatorS (listar todos os tradutores)
- request header
  Authorization: Bearer <JWT_token_aqui>

- request response:
[
    {
    "id": "UUID_do_tradutor",
    "name": "Nome do Tradutor",
    "email": "email@dominio.com"
    },
    {
    "id": "UUID_do_tradutor2",
    "name": "Nome do Tradutor 2",
    "email": "email2@dominio.com"
    }
]

### GET /api/translators/{id} (obter um tradutor)
- path parameter
  id do tradutor
- request header
  Authorization: Bearer <JWT_token_aqui>
- request response
  {
  "id": "UUID_do_tradutor",
  "name": "Nome do Tradutor",
  "email": "email@dominio.com"
  }

## 🚀 Endpoints de Documentos

### POST /api/documents/translate (tradução de texto livre)
- request header
  Authorization: Bearer <JWT_token_aqui>
- request body
  {
  "subject": "Assunto do Documento",
  "content": "Conteúdo do Documento",
  "authorEmail": "autor@dominio.com",
  "sourceLanguage": "pt",
  "targetLanguage": "en"
  }
- request response
- {
  "id": "UUID_do_documento",
  "subject": "Assunto do Documento",
  "content": "Conteúdo do Documento",
  "translatedContent": "Conteúdo Traduzido",
  "authorEmail": "autor@dominio.com",
  "sourceLanguage": "pt",
  "targetLanguage": "en",
  "translatorId": "UUID_do_tradutor"
  }

### GET /api/documents
- request header
  Authorization: Bearer <JWT_token_aqui>
-request response
  [
      {
      "id": "UUID_do_documento",
      "subject": "Assunto do Documento",
      "content": "Conteúdo do Documento",
      "translatedContent": "Conteúdo Traduzido",
      "authorEmail": "autor@dominio.com",
      "sourceLanguage": "pt",
      "targetLanguage": "en",
      "translatorId": "UUID_do_tradutor"
      },
      {
      "id": "UUID_do_documento_2",
      "subject": "Assunto do Documento 2",
      "content": "Conteúdo do Documento 2",
      "translatedContent": "Conteúdo Traduzido 2",
      "authorEmail": "autor2@dominio.com",
      "sourceLanguage": "es",
      "targetLanguage": "en",
      "translatorId": "UUID_do_tradutor_2"
      }
  ]

### GET /api/documents/{id} (dados de documento específico)
- path parameter
  id do documento
- request header
  Authorization: Bearer <JWT_token_aqui>
- request response
  {
  "id": "UUID_do_documento",
  "subject": "Assunto do Documento",
  "content": "Conteúdo do Documento",
  "translatedContent": "Conteúdo Traduzido",
  "authorEmail": "autor@dominio.com",
  "sourceLanguage": "pt",
  "targetLanguage": "en",
  "translatorId": "UUID_do_tradutor"
  }

### POST /api/documents/csv/upload (upload de arquivos csv para tradução)
- request header
  Authorization: Bearer <JWT_token_aqui>
- 
- request parameters (dois arquivos):
translatorsCsv: Arquivo CSV contendo informações dos tradutores (name, email, sourceLanguage e targetLanguage)
documentsCsv: Arquivo CSV contendo informações dos documentos a serem traduzidos (subject, content, locale?, authorEmail). 

- request response
200 ok e os dados do documento para download




  


