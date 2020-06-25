# Projeto authserver - Authorization Server

## Servidor

Port: `8081`

Login URL: `localhost:8081/login`


## Roles e Scopes definidos:
| roles  |  scopes  |
| :----: | :------: |
|  read  |  read    |
|  write |  write   |
|  USER  |          |
|  ADMIN |          |


## Usuários cadastrados:

| username        |  senha   | 
| :------:        | :------: | 
| admin@mail.com  |  admin   | 
|  user@mail.com  |  123     | 

## Roles de Usuários por Cliente:
| username        |  cliente               | roles |
| :------:        | :------:               |  :------:       
| admin@mail.com  |  cliente-agendamento   | ADMIN,read,write
| admin@mail.com  |  cliente-guia-medico   | ADMIN,read,write,EMITIR_GUIA
| user@mail.com  |  cliente-agendamento   | USER,read
| user@mail.com  |  cliente-guia-medico   | USER,read,write


## Clients cadastrados:

| client_id            |  client_secret  |  scopes
| :------             | :------        | :------: |
| cliente-agendamento  |  OG9xam2B5CN07FNs5qpnKhfX72YNFZuc6UASeHrD7TeF3eJEsx   |  read write
| cliente-guia-medico  |  AxsITUY3fvhO0Nj2k6xoltwCX5bku0E9zaiMiAUVqJBTuLcgEY   |  read write

## Resources Cadastrados - ID Resource Server:

`cliente-agendamento-resource` e `cliente-guia-medico-resource`

## Assinatura JWT

signKey: `assinatura`

## Banco de dados

Arquivo com SQL pode ser encontrado em:
    [/authserver/database.sql](database.sql)

- Criar uma base dados mysql:

	`CREATE DATABASE authserver;`
- Criar um usuário (mesmo nome da base):
	
    `CREATE USER 'authserver'@'localhost' IDENTIFIED BY 'authserver';`

- Conceder privilégios para este usuário sobre o banco de dados.
	
    `GRANT ALL PRIVILEGES ON authserver.* TO 'authserver'@'localhost';`

- Criar a tabela usuario
    ```
    create table usuario(
        id int auto_increment primary key,
        nome varchar (100),
        email varchar (100),
        senha varchar (50)
    );
    ```

- Criar tabela oauth_client_details
    ```
    create table oauth_client_details (
        id int auto_increment primary key,
        client_id VARCHAR(256),
        resource_ids VARCHAR(256),
        client_secret VARCHAR(256),
        scope VARCHAR(256),
        authorized_grant_types VARCHAR(256),
        web_server_redirect_uri VARCHAR(256),
        authorities VARCHAR(256),
        access_token_validity INTEGER,
        refresh_token_validity INTEGER,
        additional_information VARCHAR(4096),
        autoapprove VARCHAR(256)
    );
    ```

- Criar tabela perfis_usuario_por_cliente
    ```
    CREATE TABLE perfis_usuario_por_cliente (
    id int auto_increment primary key,
    perfis varchar(255),
    cliente_id int references oauth_client_details(id),
    usuario_id int references usuario(id)
    );
    ```

- Adicionar registros na tabela usuario
    ```
    INSERT INTO usuario(id, nome, email, senha) VALUES ('1','admin','admin@mail.com','$2a$10$HcmfYza5glcYCv3gCjlNXeoUXEpJ32x3ZxFJXPBdZHPddQoH380yq');

    INSERT INTO usuario(id, nome, email, senha) VALUES ('2','user','user@mail.com','$2a$10$qCdbgkX3Lyactgro06w2Ze9dPihGNzI08TLJ/0BjB2/3UsPCJ/RYG');
    ```

- Adicionar registros na tabela oauth_client_details
    ```
    INSERT INTO oauth_client_details
        (id, client_id, resource_ids, client_secret, scope, authorized_grant_types,
        web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity,
        additional_information, autoapprove)
    VALUES
        (1, 'cliente-agendamento', 'cliente-agendamento-resource', 'OG9xam2B5CN07FNs5qpnKhfX72YNFZuc6UASeHrD7TeF3eJEsx',
        'read,write', 'password,authorization_code,client_credentials,implicit,refresh_token',
        'http://localhost:4200/callback',
        'read,write', 300, -1, NULL, 'false');
        
    INSERT INTO oauth_client_details
        (id, client_id, resource_ids, client_secret, scope, authorized_grant_types,
        web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity,
        additional_information, autoapprove)
    VALUES
        (2, 'cliente-guia-medico', 'cliente-guia-medico-resource', 'AxsITUY3fvhO0Nj2k6xoltwCX5bku0E9zaiMiAUVqJBTuLcgEY',
        'read,write', 'password,authorization_code,client_credentials,implicit,refresh_token',
        'http://localhost:4200/callback',
        'read,write', 300, -1, NULL, 'false');
    ```

- Adicionar registros na tabela perfis_usuario_por_cliente
    ```
    INSERT INTO perfis_usuario_por_cliente
        (id, perfis, cliente_id, usuario_id)
    VALUES
        (1, 'ADMIN,read,write', 1, 1);
        
    INSERT INTO perfis_usuario_por_cliente
        (id, perfis, cliente_id, usuario_id)
    VALUES
        (2, 'USER,read', 1, 2);
        
    INSERT INTO perfis_usuario_por_cliente
        (id, perfis, cliente_id, usuario_id)
    VALUES
        (3, 'ADMIN,read,write,EMITIR_GUIA', 2, 1);
        
    INSERT INTO perfis_usuario_por_cliente
        (id, perfis, cliente_id, usuario_id)
    VALUES
        (4, 'USER,read,write', 2, 2);
    ```

## Fluxos OAuth2

### Password Credentials

Resource Server: cliente-agendamento

usuário: user@mail.com | senha: 123

    curl --location 
    --request POST 'http://localhost:8081/oauth/token' \
    --header 'Authorization: Basic Y2xpZW50ZS1hZ2VuZGFtZW50bzpPRzl4YW0yQjVDTjA3Rk5zNXFwbktoZlg3MllORlp1YzZVQVNlSHJEN1RlRjNlSkVzeA==' \
    --header 'Content-Type: application/x-www-form-urlencoded' \
    --data-urlencode 'grant_type=password' \
    --data-urlencode 'username=user@mail.com' \
    --data-urlencode 'password=123' \
    --data-urlencode 'scope=read write'
    

### Clients Credentials

Resource Server: cliente-guia-medico

    curl --location 
    --request POST 'http://localhost:8081/oauth/token' \
    --header 'Authorization: Basic Y2xpZW50ZS1ndWlhLW1lZGljbzpBeHNJVFVZM2Z2aE8wTmoyazZ4b2x0d0NYNWJrdTBFOXphaU1pQVVWcUpCVHVMY2dFWQ==' \
    --header 'Content-Type: application/x-www-form-urlencoded' \
    --data-urlencode 'grant_type=client_credentials'

### Refresh Token

Resource Server: cliente-agendamento

    curl --location 
    --request POST 'http://localhost:8081/oauth/token' \
    --header 'Authorization: Basic Y2xpZW50ZS1hZ2VuZGFtZW50bzpPRzl4YW0yQjVDTjA3Rk5zNXFwbktoZlg3MllORlp1YzZVQVNlSHJEN1RlRjNlSkVzeA==' \
    --header 'Content-Type: application/x-www-form-urlencoded' \
    --data-urlencode 'grant_type=refresh_token' \
    --data-urlencode 'refresh_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsiY2xpZW50ZS1ndWlhLW1lZGljby1yZXNvdXJjZSIsImNsaWVudGUtYWdlbmRhbWVudG8tcmVzb3VyY2UiXSwidXNlcl9uYW1lIjoidXNlckBtYWlsLmNvbSIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdLCJhdGkiOiIxMzgyMTgwNi03ZTdkLTRlMzQtYjcwZS0zNjRlODExMjk4NTAiLCJleHAiOjE1OTUwMzgxMzIsImF1dGhvcml0aWVzIjpbIlVTRVIiLCJyZWFkIl0sImp0aSI6IjMxYjVkNDM4LTRhNzYtNDUwZi04ODkzLTczNGM0YTc0NDIxYyIsImNsaWVudF9pZCI6ImNsaWVudGUtYWdlbmRhbWVudG8ifQ.fvl1T2w_rmfP3vs7Z8X4YFXil5rDtZEGIxv_zgJXCSU' \
    --data-urlencode 'scope=read write'

## Authorization Code + PKCE

- Obter código de autorização:

    **Resource server:** cliente-guia-medico

    **URL de redirecionamento:** localhost:4200/callback

    **code_challenge:** 4cc9b165-1230-4607-873b-3a78afcf60c5

- Acessar URL

    http://localhost:8081/oauth/authorize?client_id=cliente-guia-medico&response_type=code&redirect_uri=http://localhost:4200/callback&code_challenge=4cc9b165-1230-4607-873b-3a78afcf60c5

- Autenticar usuário
- Obter código de autorização através da URL redirecionada:
    http://localhost4200/callback?code=ybm6vS&state=teste

- Obter Access Token:

    **Authorization Code:** ybm6vS

        curl --location 
        --request POST 'http://localhost:8081/oauth/token' \
        --header 'Authorization: Basic Y2xpZW50ZS1hZ2VuZGFtZW50bzpPRzl4YW0yQjVDTjA3Rk5zNXFwbktoZlg3MllORlp1YzZVQVNlSHJEN1RlRjNlSkVzeA==' \
        --data-urlencode 'grant_type=authorization_code' \
        --data-urlencode 'redirect_uri=http://localhost:4200/callback' \
        --data-urlencode 'scope=read write' \
        --data-urlencode 'code=ybm6vS'
        --data-urlencode 'code_verifier=4cc9b165-1230-4607-873b-3a78afcf60c5'


### Implicit

**Resource server:** cliente-agendamento

**URL de redirecionamento:** localhost:4200/callback

        http://localhost:8081/oauth/authorize?response_type=token&redirect_uri=http://localhost:4200/callback&client_id=cliente-agendamento&scope=read+write&state=teste
