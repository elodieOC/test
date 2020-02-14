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
insert into users (username, first_name, last_name, email, password, address, longitude, latitude, id_role, reset_token, token_date) values ('elojito', 'elodie','ollivier','hellojito@gmail.com', MD5('mdp'), '14 rue des pavillons','2.2430728','48.8822912', 2, null, null);
insert into users (username, first_name, last_name, email, password, address, longitude, latitude, id_role, reset_token, token_date) values ('admin', 'admin','admin','elojito@live.fr', MD5('mdp'), '24 rue volta', '2.2339875', '48.8763126',1, null, null);



