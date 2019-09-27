------------------------------------------------------------
--        Script Postgre
------------------------------------------------------------

------------------------------------------------------------
-- Table: rewards
------------------------------------------------------------
DROP TABLE IF EXISTS public.rewards;

CREATE TABLE public.rewards(
                                 id     SERIAL NOT NULL ,
                                 points INT DEFAULT NULL,
                                 max_points INT NOT NULL,
                                 rewards INT DEFAULT NULL,
                                 id_user INT NOT NULL,
                                 id_merchant INT NOT NULL,
                                 CONSTRAINT rewards_PK PRIMARY KEY (id)
)WITHOUT OIDS;