------------------------------------------------------------
--        Script Postgre
------------------------------------------------------------

------------------------------------------------------------
-- Table: roles
------------------------------------------------------------
DROP TABLE IF EXISTS public.roles;

CREATE TABLE public.roles(
                             id            SERIAL NOT NULL ,
                             role_name   VARCHAR (50) NOT NULL,
                             CONSTRAINT roles_PK PRIMARY KEY (id)
)WITHOUT OIDS;

------------------------------------------------------------
-- Table: users
------------------------------------------------------------
DROP TABLE IF EXISTS public.users;

CREATE TABLE public.users(
                               id            SERIAL NOT NULL ,
                               username   VARCHAR (50) NOT NULL UNIQUE ,
                               first_name   VARCHAR (50) NOT NULL ,
                               last_name     VARCHAR (50) NOT NULL ,
                               reset_token varchar(36) default null,
                               token_date timestamp default null,
                               email   VARCHAR (100) ,
                               password   VARCHAR (100)   ,
                               id_role   INT  NOT NULL  ,
                               CONSTRAINT users_PK PRIMARY KEY (id) ,
                               CONSTRAINT roles_FK FOREIGN KEY (id_role) REFERENCES public.roles(id)
)WITHOUT OIDS;


