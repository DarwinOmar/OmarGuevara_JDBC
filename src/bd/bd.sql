CREATE TABLE persona
(
  id_persona serial NOT NULL,
  nombres character varying(100),
  dni character varying(8),
  direccion character varying(200),
  edad integer,
  estado boolean,
  CONSTRAINT pk_persona PRIMARY KEY (id_persona)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE persona
  OWNER TO postgres;

--- Funcion listar 
CREATE OR REPLACE FUNCTION fu_lista_personas(parametro integer)
  RETURNS refcursor AS
$BODY$
declare 
lista refcursor;  
begin 
	OPEN lista FOR 
	SELECT * from persona where id_persona >=parametro;  
return lista; 
end 
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION fu_lista_personas(integer)
  OWNER TO postgres;

---- funcion registrar 


CREATE OR REPLACE FUNCTION fu_reg_persona(
    pnombres character varying,
    pdni character varying,
    pdireccion character varying,
    pedad integer,
    pestado boolean)
  RETURNS void AS
$BODY$
BEGIN
	INSERT INTO persona(nombres, dni, direccion, edad, estado) VALUES (pnombres, pdni, pdireccion, pedad, pestado);
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION fu_reg_persona(character varying, character varying, character varying, integer, boolean)
  OWNER TO postgres;
