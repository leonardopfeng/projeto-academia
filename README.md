# 📌 Projeto Academia API

Este projeto é uma API RESTful para gerenciamento de academias, permitindo o cadastro de usuários, treinadores, clientes, pagamentos, exercícios e planos de treino.


## 📖 Sumário

- 🛠 Tecnologias

- 🚀 Instalação

- 🔐 Autenticação

- 🌊 Fluxo

- 📌 Endpoints

    - Autenticação (Auth) - Operações para `signin` e `refresh`

    - Usuários (User) - Operações para `cadastrar` usuários

    - Clientes (Client) - Operações para `cadastrar`, `atualizar`, `buscar` e `deletar` clientes

    - Treinadores (Coach) - Operações para `cadastrar`, `atualizar`, `buscar` e `deletar` treinadores

    - Pagamentos (Payment) - Operações para `cadastrar`, `atualizar`, `buscar` e `deletar` pagamentos

    - Exercícios (Exercise) - Operações para `cadastrar`, `atualizar`, `buscar` e `deletar` exercícios
        
    - Grupos de Exercício (Exercise Group) - Operações para `cadastrar`, `atualizar`, `buscar` e `deletar` grupos de exercícios

    - Planos de Treino (Training Session) - Operações para `cadastrar`, `atualizar`, `buscar` e `deletar` planos de treino

    - Exercícios do Treino (Session Exercise) - Operações para `cadastrar`, `atualizar`, `buscar` e `deletar` exercícios de um treino



## 🛠 Tecnologias

- Java 21

- Spring Boot 3

- MySQL

- CORS

- JWT para autenticação


## 🚀 Instalação

### Clone o repositório
- `git clone https://github.com/leonardopfeng/projeto-academia.git`

### Acesse a pasta do projeto
- `cd projeto-academia`

### Instale as dependências
- `mvn install`

### Crie o banco de dados
- `Crie um banco de dados MySQL chamado "projeto_academia"`

### Inicie o servidor
- `mvn spring-boot:run`

## 🔐 Autenticação

Todas as requisições protegidas exigem um token JWT. Para obter um token, faça login usando o endpoint de autenticação.


## 🌊 Fluxo do programa
  
  - Acessar o Swagger (`http://localhost:8080/swagger-ui/index.html#/`)
  - Autenticação pelo Swagger (admin / admin)
    - Copiar o `accessToken` gerado na resposta e colar no botão "Authorize"
  - Cadastro de grupo de exercício
    - Cadastro de exercício
  - Cadastro de novos usuários
    - Cadastro de treinador
      - Cadastro de cliente
  - Cadastro de sessão de treinamento
    - Cadastro de exercícios no treino



## 📌 Exemplos de Endpoints

Autenticação (Auth)

POST `/auth/login`

  - Realiza o login e retorna um token JWT válido
  - Input

  ```

  {
  "username": "admin",
  "password": "admin"
  }

  ```
  - Output 
  
  ```

 {
    "username": "admin",
    "authenticated": true,
    "created": "2025-02-13T21:22:34.236+00:00",
    "expiration": "2025-02-13T22:22:34.236+00:00",
    "accessToken": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9...",
    "refreshToken": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9..."
}

  ```

Usuário (User)

POST `/api/user/v1`

  - Realiza o cadastro de um novo usuário 
  - Input

  ```

{
  "userName": "client",
  "password": "client",
  "roles": [
    "ADMIN",
    "MANAGER",
    "COMMOM_USER"
  ]
}

  ```
  - Output 
  
  ```

{
    "userName": "client",
    "password": "{pbkdf2}83e197fbb4902bd55f...",
    "roles": [
        "ADMIN",
        "MANAGER"
    ]
}

  ```


Treinador (Coach)

GET `/api/coach/v1`

  - Retorna todos os treinadores cadastrados
  - Output

  ```

{
    "_embedded": {
        "coachVOList": [
            {
                "key": 1,
                "certification": "certification1",
                "hiredDate": "2026-01-27T03:00:00.000+00:00",
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/api/coach/v1/1"
                    }
                }
            },
            {
                "key": 14,
                "certification": "certification2",
                "hiredDate": "2026-01-27T03:00:00.000+00:00",
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/api/coach/v1/14"
                    }
                }
            }
        ]
    },
    "_links": {
        "self": {
            "href": "http://localhost:8080/api/coach/v1?page=0&size=12&direction=ASC"
        }
    },
    "page": {
        "size": 12,
        "totalElements": 2,
        "totalPages": 1,
        "number": 0
    }
}

  ```

Cliente (Client)

GET `/api/client/v1/:id`

  - Retorna um cliente com base no `id` passado como argumento
  - Output

  ```

{
    "userId": 2,
    "coachId": 1,
    "_links": {
        "self": {
            "href": "http://localhost:8080/api/client/v1/2"
        }
    }
}

  ```

Exercício (Exercise)

POST `/api/exercise/v1`

  - Cadastra um novo exercício
  - Input 

  ```
  {
  "name": "exemplo_exercicio",
  "videoUrl": "exemplo_exercicio",
  "groupId": 1
  }
  ```

  - Output

  ```

{
    "key": 5,
    "name": "exemplo_exercicio",
    "videoUrl": "exemplo_exercicio",
    "groupId": 1,
    "_links": {
        "self": {
            "href": "http://localhost:8080/api/exercise/v1/5"
        }
    }
}

  ```

Sessão de Treino (Training Session)

GET `/api/trainingSession/v1/findAllByClientId/:id`

  - Retorna todos os treinos cadastrados para um usuário específico
  - Output

  ```

  {
    "_embedded": {
        "trainingSessionVOList": [
            {
                "key": 1,
                "clientId": 2,
                "coachId": 1,
                "name": "Peito",
                "startDate": "2025-02-05",
                "status": true,
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/api/trainingSession/v1/1"
                    }
                }
            },
            {
                "key": 2,
                "clientId": 2,
                "coachId": 1,
                "name": "Costas",
                "startDate": "2025-02-05",
                "status": true,
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/api/trainingSession/v1/2"
                    }
                }
            }
        ]
    },
    "_links": {
        "self": {
            "href": "http://localhost:8080/api/trainingSession/v1?page=0&size=12&direction=ASC"
        }
    },
    "page": {
        "size": 12,
        "totalElements": 2,
        "totalPages": 1,
        "number": 0
    }
}

  ```

Exercício do Treino (Session Exercise)

GET `/api/sessionExercise/v1/:sessionId/:exerciseId`

  - Retorna um exercício cadastrado em um treino
  - Output

  ```
  {
    "id": {
        "sessionId": 1,
        "exerciseId": 2
    },
    "sequence": 10,
    "sets": 10,
    "reps": 10,
    "weight": 10.0,
    "_links": {
        "self": {
            "href": "http://localhost:8080/api/sessionExercise/v1/1/2"
        }
    }
  }

  ```

Pagamento (Payment)

GET `/api/payment/v1/`

  - Retorna todos os pagamentos cadastrados no sistema
  - Output

  ```

  {
    "_embedded": {
        "paymentVOList": [
            {
                "key": 1,
                "clientId": 2,
                "amount": 100.0,
                "discount": 10.0,
                "total": 90.0,
                "paymentDate": "2025-01-29",
                "dueDate": "2025-01-29",
                "status": "paid",
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/api/payment/v1/1"
                    }
                }
            }
        ]
    },
    "_links": {
        "self": {
            "href": "http://localhost:8080/api/payment/v1?page=0&size=12&direction=ASC"
        }
    },
    "page": {
        "size": 12,
        "totalElements": 1,
        "totalPages": 1,
        "number": 0
    }
}

  ```

Grupo de Exercício (Exercise Group)

GET `/api/exerciseGroup/v1/:id/`

  - Retorna as informações de um grupo de exercício 
  - Output

  ```

{
    "key": 1,
    "name": "Ombro",
    "_links": {
        "self": {
            "href": "http://localhost:8080/api/exerciseGroup/v1/1"
        }
    }
}

  ```




