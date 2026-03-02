# Estudio API

API REST para gerenciamento de reservas de estúdios de música — cadastro de clientes, salas, equipamentos e agendamento de reservas.

---

## Arquitetura

O projeto utiliza uma arquitetura **monolítica containerizada** composta por três camadas:

| Camada | Tecnologia | Papel |
|--------|-----------|-------|
| **Reverse Proxy** | Nginx 1.25 (Alpine) | Ponto de entrada único na porta 80. Aplica rate limiting (5 req/s com burst de 10), cache de 10 s para GETs, compressão gzip, autenticação básica no Swagger e headers de segurança. |
| **API** | Spring Boot 4.0.2 / Java 21 | Aplicação monolítica que expõe endpoints REST sob o context-path `/api`. Organizada em módulos (cliente, sala, equipamento, reserva), cada um com Controller → Service → Repository (Spring Data JPA). Documentação via SpringDoc/Swagger UI. |
| **Banco de Dados** | H2 (in-memory) | Banco embarcado em memória (`jdbc:h2:mem:estudio-db`). Tabelas criadas automaticamente pelo Hibernate (`ddl-auto=create`) e populadas pelo `data.sql` na inicialização. |

---

## Diagrama da Arquitetura

```
            ┌───────────────────────┐
            │   Cliente / Browser   │
            └───────────┬───────────┘
                        │  HTTP :80
                        ▼
   ┌────────────────────────────────────────┐
   │          Nginx (reverse proxy)         │
   │                                        │
   │  • Rate limiting   (5 req/s burst=10)  │
   │  • Cache GET 200   (TTL 10s)           │
   │  • Gzip            (json/text)         │
   │  • Basic Auth      (Swagger)           │
   │  • Security headers                    │
   └───────────────────┬────────────────────┘
                        │  proxy_pass :8080
                        ▼
   ┌────────────────────────────────────────┐
   │     Spring Boot API (monolítica)       │
   │          context-path: /api            │
   │                                        │
   │  ┌──────────┐ ┌──────────┐ ┌─────────┐ │
   │  │  Cliente │ │   Sala   │ │ Reserva │ │
   │  │  module  │ │  module  │ │  module │ │
   │  └────┬─────┘ └────┬─────┘ └────┬────┘ │
   │       │            │            │      │
   │       ▼            ▼            ▼      │
   │  ┌─────────────────────────────────┐   │
   │  │         Service Layer           │   │
   │  └──────────────┬──────────────────┘   │
   │                 │                      │
   │  ┌──────────────▼──────────────────┐   │
   │  │      Repository Layer (JPA)     │   │
   │  └──────────────┬──────────────────┘   │
   │                 │                      │
   │  ┌──────────────▼──────────────────┐   │
   │  │      H2 Database (in-memory)    │   │
   │  └─────────────────────────────────┘   │
   └────────────────────────────────────────┘
```

---

## Passo a passo para execução

### Pré-requisitos

- Git
- Docker
- Docker Compose

> **Nota:** Não é necessário ter Java ou Maven instalados — o build é feito dentro do container via multi-stage Dockerfile.

### 1. Clonar o repositório

```bash
git clone https://github.com/luaxi/estudio-api.git
cd estudio-api
```

### 2. Criar o arquivo de credenciais do Swagger (Basic Auth)

O Nginx exige um arquivo `.htpasswd` para proteger o Swagger UI. Crie-o com `htpasswd` (do pacote `apache2-utils`) ou use um gerador online:

```bash
# Instalar htpasswd (se necessário)
sudo apt install apache2-utils        # Debian/Ubuntu
# ou
brew install httpd                     # macOS

# Gerar o arquivo (substitua 'admin' pelo usuário desejado)
htpasswd -c nginx/conf/.htpasswd admin
```

### 3. Subir os containers

```bash
docker compose up --build
```

Isso irá:

1. **Build da API** — compilar o projeto Java 21 com Maven e gerar o JAR (multi-stage).
2. **Nginx** — iniciar o reverse proxy na porta **80**, conectado à API na porta interna 8080.

### 4. Verificar se está funcionando

```bash
# Health check do Nginx
curl http://localhost/health

# Health check da API (via proxy)
curl http://localhost/api/health

# Listar salas
curl http://localhost/api/salas
```

### 5. Acessar o Swagger UI

Abra no navegador:

```
http://localhost/api/swagger-ui
```

Será solicitado usuário e senha (os definidos no `.htpasswd`).

### 6. Parar os containers

```bash
docker compose down
```

---

## Como testar os requisitos

### 1. Reverse Proxy
- Confirmar que o Nginx está rodando na porta 80 em `http://localhost/health`.
- Confirmar que a API está acessível via proxy em `http://localhost/api/health`.

### 2. Headers de segurança
- Fazer uma requisição GET e verificar os headers `X-Content-Type-Options`, `X-Frame-Options` e `X-XSS-Protection` na resposta.

### 3. Rate Limiting
- Disparar múltiplas requisições rápidas e verificar retorno HTTP 429.

Exemplo:
```bash
for i in {1..20}; do curl -o /dev/null -s -w "%{http_code}\n" http://localhost/api/health; done
```

### 4. Limite de Payload
- Tentar enviar um payload maior que 1 MB e verificar retorno HTTP 413.

Exemplo:
```bash
curl -X POST http://localhost/api/cliente -H "Content-Type: application/json" -d @large_payload.json
```

### 5. Compressão
- Fazer uma requisição GET e verificar se a resposta está comprimida (header `Content-Encoding: gzip`).

### 6. Logs
- Verificar os arquivos de logs do Nginx enquanto faz requisições, confirmando que os logs estão sendo gerados corretamente.

Exemplo:
```bash
docker exec -it estudio-api-nginx-1 tail -f /var/log/nginx/api_access.log
```
### 7. Cache de GET
- Fazer uma requisição GET e verificar que a resposta é cacheada (header `X-Cache-Status: HIT` ou `MISS`).

### 8. Basic Auth no Swagger
- Tentar acessar o Swagger UI sem autenticação e verificar que o acesso é negado.

Exemplo:
```bash
curl http://localhost/api/swagger-ui
```

---