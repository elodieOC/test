------------------------------------------------------------
--        Script Postgre
------------------------------------------------------------


------------------------------------------------------------
-- Table: categoryIcon
------------------------------------------------------------
CREATE TABLE public.category_icon(
                                 id            SERIAL NOT NULL ,
                                 icon          oid ,
                                 CONSTRAINT category_icon_PK PRIMARY KEY (id)
)WITHOUT OIDS;


------------------------------------------------------------
-- Table: category
------------------------------------------------------------
alter table public.category drop column icon;
alter table public.category add column icon_id  INT default null;

alter table public.category add constraint categoryicon_FK FOREIGN KEY (icon_id) REFERENCES public.category_icon(id)




