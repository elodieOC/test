------------------------------------------------------------
--        Script Postgre
------------------------------------------------------------



------------------------------------------------------------
-- Table: merchants
------------------------------------------------------------

alter table merchants add column if not exists user_id INT NOT NULL ;
alter table merchants drop column if exists reset_token;
alter table merchants drop column if exists token_date;
alter table merchants drop column if exists password;
alter table merchants add column if not exists max_points INT NOT NULL;





