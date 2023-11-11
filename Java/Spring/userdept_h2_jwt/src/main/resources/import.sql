INSERT INTO tb_department(dpt_nome) VALUES ('Gestão');
INSERT INTO tb_department(dpt_nome) VALUES ('Informática');

INSERT INTO tb_user(dpt_id, usr_nome, usr_email, usr_senha, usr_nascimento) VALUES (1, 'Maria Silva', 'maria@gmail.com', '000', '2001-02-23');
INSERT INTO tb_user(dpt_id, usr_nome, usr_email, usr_senha, usr_nascimento) VALUES (1, 'Bob Amarante', 'bob@gmail.com', '111', '2001-02-23');
INSERT INTO tb_user(dpt_id, usr_nome, usr_email, usr_senha, usr_nascimento) VALUES (2, 'Alex', 'alex@gmail.com', '111', '2001-02-23');
INSERT INTO tb_user(dpt_id, usr_nome, usr_email, usr_senha, usr_nascimento) VALUES (2, 'Ana', 'ana@gmail.com','333', '2001-02-23');
INSERT INTO tb_user(dpt_id, usr_nome, usr_email, usr_senha, usr_nascimento) VALUES (2, 'Mario', 'mario@gmail.com','111', '2001-02-23');
INSERT INTO tb_user(dpt_id, usr_nome, usr_email, usr_senha, usr_nascimento) VALUES (2, 'Ana da Silva', 'anasilva@gmail.com','555', '2001-02-23');

INSERT INTO tb_address(usr_id, adr_endereco) VALUES (1, 'Rua Miguel Socol, 1753, Serafina Corrêa - RS');
INSERT INTO tb_address(usr_id, adr_endereco) VALUES (2, 'Rua Linha Dr. Parobe, 1000, Serafina Corrêa - RS');
INSERT INTO tb_address(usr_id, adr_endereco) VALUES (3, 'Rua Ipiranga, 435, Serafina Corrêa - RS');
INSERT INTO tb_address(usr_id, adr_endereco) VALUES (4, 'AV. Brasil Oeste, 555, Passo Fundo - RS');