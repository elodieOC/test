------------------------------------------------------------
--        Script Postgre
------------------------------------------------------------



------------------------------------------------------------
-- Table: category
------------------------------------------------------------

DROP TABLE IF EXISTS public.category;

CREATE TABLE public.category(
                                 id            SERIAL NOT NULL ,
                                 category_name     VARCHAR (200) NOT NULL ,
                                 CONSTRAINT category_PK PRIMARY KEY (id)
)WITHOUT OIDS;

alter table public.merchants add column id_category INT default null;
alter table public.merchants drop column category;
alter table public.merchants add constraint category_shop_FK FOREIGN KEY (id_category) REFERENCES public.category(id)





