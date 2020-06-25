CREATE DATABASE authserver;

CREATE USER 'authserver'@'localhost' IDENTIFIED BY 'authserver';

GRANT ALL PRIVILEGES ON authserver.* TO 'authserver'@'localhost';

USE authserver;

create table usuario(
   id int auto_increment primary key,
   nome varchar (100),
   email varchar (100),
   senha varchar (50)
);

-- cria as tabelas
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

CREATE TABLE perfis_usuario_por_cliente (
  id int auto_increment primary key,
  perfis varchar(255),
  cliente_id int references oauth_client_details(id),
  usuario_id int references usuario(id)
);

INSERT INTO usuario(id, nome, email, senha) VALUES ('1','admin','admin@mail.com','$2a$10$HcmfYza5glcYCv3gCjlNXeoUXEpJ32x3ZxFJXPBdZHPddQoH380yq');
INSERT INTO usuario(id, nome, email, senha) VALUES ('2','user','user@mail.com','$2a$10$qCdbgkX3Lyactgro06w2Ze9dPihGNzI08TLJ/0BjB2/3UsPCJ/RYG');

INSERT INTO perfil(id, nome) VALUES ('1', 'ADMIN');
INSERT INTO perfil(id, nome) VALUES ('2', 'USER');
INSERT INTO perfil(id, nome) VALUES ('3', 'read');
INSERT INTO perfil(id, nome) VALUES ('4', 'write');

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

