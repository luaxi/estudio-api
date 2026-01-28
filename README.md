# Estudio API

## 1. Descrição do problema
O sistema pretende resolver o problema de gerenciamento de reservas para pequeno estúdios de música. Isso também envolve gerenciar salas, equipamentos e clientes, facilitando as validações necessárias para evitar conflitos e automatizando o cálculo do preço de agendamento.

## 2. Objetivo do sistema
O sistema oferece:
- Cadastro de clientes, salas e equipamentos
- Agendamento de reservas
- Validações de regra de negócio
    - Conflito de horários na mesma sala
    - Restrições de horários e horários redondos
    - Restrições para deletar salas
    - Cálculo de preço por hora
- Interface RESTful para consumo (Postman ou frontend a ser implementado)

## 3. Estilo arquitetural adotado
> Arquitetura Monolítica

## 4. Diagrama simples da arquitetura (em ASCII)

```
┌────────────────────────────────────────────────────────┐
│           Cliente (Postman/Frontend)                   │
└────────────────────────────────┬───────────────────────┘
                                 │ HTTP REST
                                 ↓
┌────────────────────────────────────────────────────────┐
│          Spring Boot API (Monolítica)                  │
├────────────────────────────────────────────────────────┤
│  ┌────────────┐  ┌─────────────┐  ┌───────────────┐    │
│  │  Cliente   │  │ Equipamento │  │ ...outros     │    │
│  │ Controller │  │ Controller  │  │ controllers...│    │
│  └──────┬─────┘  └──────┬──────┘  └──────┬────────┘    │
│         │               │                │             │
│  ┌──────┴───────────────┴──┬────────────────────────┐  │
│  │                         ↓                        │  │
│  │  ┌─────────────────────────────────────────┐     │  │
│  │  │             Services Layer              │     │  │
│  │  │         (ClienteService, etc)           │     │  │
│  │  └─────────────────────────────────────────┘     │  │
│  │                      ↓                           │  │
│  │  ┌─────────────────────────────────────────┐     │  │
│  │  │          Repository Layer (JPA)         │     │  │
│  │  │        (ClienteRepository, etc)         │     │  │
│  │  └─────────────────────────────────────────┘     │  │
│  │                      ↓                           │  │
│  └──────────────────────┬───────────────────────────┘  │
│                         │                              │
├─────────────────────────┬──────────────────────────────┤
│  ┌─────────────────────────────────────────────────┐   │
│  │      Banco de Dados (H2)                        │   │
│  │   (Cliente, Sala, Equipamento, Reserva)         │   │
│  └─────────────────────────────────────────────────┘   │
└────────────────────────────────────────────────────────┘
```

## 5. Justificativa das decisões arquiteturais

A arquitetura monolítica foi pensada de acordo com o escopo das problemáticas dos pequenos estúdios de música, que não exigem grande escalabilidade ou balanceamento de carga.

O sistema está organizado em módulos, cada um com suas responsabilidades, para facilitar uma futura migração para SOA ou Microsserviços.

## 6. Instruções para execução do projeto

### Pré-requisitos
- Java 21 ou superior
- Maven 3.6 ou superior
- Git

### Passos para executar

1. **Clone o repositório**
   ```bash
   git clone https://github.com/luaxi/estudio-api
   cd estudio-api
   ```

2. **Compile o projeto**
   ```bash
   mvn clean install
   ```

3. **Execute a aplicação**
   ```bash
   mvn spring-boot:run
   ```

4. **Acesse a API**
   - API disponível em: `http://localhost:8080`
   - Console do banco H2: `http://localhost:8080/h2-console`


### Estrutura de diretórios principais

- `src/main/java/com/example/estudio_api/` - Código fonte da aplicação
  - `cliente/` - Módulo de clientes
  - `equipamento/` - Módulo de equipamentos
  - `sala/` - Módulo de salas
  - `reserva/` - Módulo de reservas
  - `shared/errors/` - Tratamento de erros
- `src/main/resources/` - Arquivos de configuração e dados iniciais
- `src/test/` - Testes unitários

## 7. Nome completo dos integrantes
- Lua Schneider Pittelkow