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
-- Tables: roles
-- init
------------------------------------------------------------
insert into roles (role_name) values ('MERCHANT');

------------------------------------------------------------
-- Table: merchants
------------------------------------------------------------

alter table merchants add column if not exists id_role INT default 1;
alter table merchants add constraint roles_FK FOREIGN KEY (id_role) REFERENCES public.roles(id)




