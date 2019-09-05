------------------------------------------------------------
--        Script Postgre
------------------------------------------------------------



------------------------------------------------------------
-- Tables: roles
-- init
------------------------------------------------------------
insert into roles (role_name) values ('ADMIN'), ('USER');

------------------------------------------------------------
-- Table: users
------------------------------------------------------------
insert into users (username, first_name, last_name, email, password, id_role, token, token_date) values ('elojito', 'elodie','ollivier','hellojito@gmail.com', MD5('mdp'), 2, null, null);
insert into users (username, first_name, last_name, email, password, id_role, token, token_date) values ('admin', 'admin','admin','elojito@live.fr', MD5('mdp'), 1, null, null);



