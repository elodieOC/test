------------------------------------------------------------
--        Script Postgre
------------------------------------------------------------



------------------------------------------------------------
-- Table: merchants
------------------------------------------------------------
insert into merchants (merchant_name, category, address, email, password, reset_token, token_date) values ('Le fournil', 'boulangerie','11 rue du pain','hellojito@gmail.com', MD5('mdp'),  null, null);
insert into merchants (merchant_name, category, address, email, password, reset_token, token_date) values ('La bio', 'boulangerie','7 rue du pain','elojito@live.fr', MD5('mdp'),  null, null);



