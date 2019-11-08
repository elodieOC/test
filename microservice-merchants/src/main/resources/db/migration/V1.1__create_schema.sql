------------------------------------------------------------
--        Script Postgre
------------------------------------------------------------

------------------------------------------------------------
-- Table: merchants
------------------------------------------------------------
DROP TABLE IF EXISTS public.merchants;

CREATE TABLE public.merchants(
                                 id            SERIAL NOT NULL ,
                                 merchant_name   VARCHAR (50) NOT NULL UNIQUE ,
                                 email   VARCHAR (50) NOT NULL ,
                                 category     VARCHAR (200) NOT NULL ,
                                 reset_token varchar(36) default null,
                                 token_date timestamp default null,
                                 address   VARCHAR (100) ,
                                 password   VARCHAR (100)   ,
                                 CONSTRAINT merchants_PK PRIMARY KEY (id)
)WITHOUT OIDS;