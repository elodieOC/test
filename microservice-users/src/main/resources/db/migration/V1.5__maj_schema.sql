------------------------------------------------------------
--        Script Postgre
------------------------------------------------------------


------------------------------------------------------------
-- Table: newsletter
------------------------------------------------------------
CREATE TABLE public.newsletter(
                             id            SERIAL NOT NULL ,
                             email   VARCHAR (255) NOT NULL,
                             CONSTRAINT newsletter_PK PRIMARY KEY (id)
)WITHOUT OIDS;

alter table public.users add column newsletter boolean not null default false;



