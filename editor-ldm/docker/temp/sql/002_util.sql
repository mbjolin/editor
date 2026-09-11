
drop schema if exists "util" cascade;
create schema "util";

comment on schema "util" is
  'Contient des fonctions utilitaires.';

drop type if exists "util"."infotype";
create type "util"."infotype" as (id text, typeschema text, typename text);

create or replace function "util".condition_on_id(_table text)
  returns text
  language plpgsql
as $fun$
  declare
    idarray text[];
    idarraysize integer;
    idcondition text;
  begin
    idarray := regexp_split_to_array(_table, '_');
    idarraysize := array_length(idarray, 1);

    if(idarraysize = 1) then
      idcondition := FORMAT('(id_%s=_value.id_%s)',
          _table, _table);
    elsif (idarraysize = 2) then
      idcondition := FORMAT('(id_%s=_value.id_%s and id_%s=_value.id_%s)',
          idarray[1], idarray[1], idarray[2], idarray[2]);
    else
      raise exception 'Il y a un problème pour chercher avec un id : %', _table;
    end if;

    RETURN idcondition;
  end
$fun$;

--https://stackoverflow.com/questions/21246201/postgresql-v9-x-have-real-array-of-record
CREATE OR REPLACE FUNCTION util.create_type(_table text, _schema_name text)
 RETURNS text
 LANGUAGE plpgsql
AS $fun$
  declare
    _inschema text := _schema_name || '_obj';
    _schema text := _schema_name || '_pri';
    rArray "util"."infotype"[];
    newType text;
    contentType text;
    r "util"."infotype";
  begin

    select array (select (column_name::text, udt_schema::text, udt_name::text)::"util"."infotype"
      from information_schema.columns where table_schema = _schema and table_name = _table)
      into rArray ;

    contentType := '';
    foreach r in array rArray
    loop
      contentType:= contentType || FORMAT('%s %s.%s,', r.id, r.typeschema, r.typename);
    end loop;

    contentType:= substring(contentType,1, length(contentType)-1);

    newType:= FORMAT('
              create type "%s"."%s" as (
              %s
              );
              ', _inschema, _table, contentType);

    execute newType;

    RETURN 'success';
END
$fun$;

/* Créer une fonction insertion pour l'objet en paramètre.
 * La nouvelle fonction doit contenir l'objet */
CREATE OR REPLACE FUNCTION "util".create_create_function(_type text, _schema_name text)
 RETURNS text
 LANGUAGE plpgsql
AS $fun$
  declare
    _inschema text := _schema_name || '_api';
    _schema text := _schema_name || '_pri';
    _table text := _type;
    r "util"."infotype";
    rArray "util"."infotype"[];
    fieldtext text;
    valuetext text;
    sqlCode text;
  begin

    select array (select (column_name::text, udt_schema::text, udt_name::text)::"util"."infotype"
      from information_schema.columns where table_schema = _schema and table_name = _table)
      into rArray ;

    fieldtext := '';
    valuetext := '';

    foreach r in array rArray
    loop
      fieldtext:= fieldtext || r.id || ',';
      valuetext:= valuetext || '_value.' || r.id || ',';
    end loop;

    fieldtext:= substring(fieldtext,1, length(fieldtext)-1);
    valuetext:= substring(valuetext,1, length(valuetext)-1);

    sqlCode:= FORMAT('
              create or replace function "%s"."create_%s" (
                _value "%s_obj".%s
              )
              returns "%s_obj".%s
              language plpgsql
              as $fun2$
                declare
                   result "%s_obj".%s;
                begin
                  insert into %s.%s
                  (%s)
                  values
                  (%s) returning %s into result;
                  return result;
                end;
              $fun2$;
              ', _inschema, _type, _schema_name, _type, _schema_name, _type, _schema_name, _table, _schema, _table, fieldtext, valuetext, fieldtext);


    --raise notice 'sqlCode %',sqlCode;
    execute sqlCode;

    RETURN 'success';
END
$fun$;

/* Créer une fonction lecture pour le type en paramètre. La nouvelle fonction doit contenir au moins
 * l'identifiant de l'objet. */
CREATE OR REPLACE FUNCTION "util".create_read_function(_type text, _schema_name text)
 RETURNS text
 LANGUAGE plpgsql
AS $fun$
  declare
    _inschema text := _schema_name || '_api';
    _schema text := _schema_name || '_pri';
    _table text := _type;
    r "util"."infotype";
    rArray "util"."infotype"[];
    fieldtext text;
    valuetext text;
    sqlSelect text;
    sqlCode text;
    idCondition text;
  begin

    select array (select (column_name::text, udt_schema::text, udt_name::text)::"util"."infotype"
      from information_schema.columns where table_schema = _schema and table_name = _table)
      into rArray ;

    fieldtext := '';
    valuetext := '';

    foreach r in array rArray
    loop
      fieldtext:= fieldtext || r.id || ',';
      valuetext:= valuetext || '_value.' || r.id || ',';
    end loop;

    fieldtext:= substring(fieldtext,1, length(fieldtext)-1);
    valuetext:= substring(valuetext,1, length(valuetext)-1);

    select "util".condition_on_id(_table) into idCondition;

    sqlSelect := FORMAT('select * from %s.%s where %s into _value;', _schema, _table, idCondition);

    sqlCode:= FORMAT('
              create or replace function "%s"."read_%s" (
                _value "%s_obj".%s
              )
              returns "%s_obj".%s
              language plpgsql
              as $fun2$
                begin
                  %s
                  return _value;
                end;
              $fun2$;
              ', _inschema, _table, _schema_name, _table, _schema_name, _table, sqlSelect);

    --raise notice 'sqlCode %',sqlCode;
    execute sqlCode;

    RETURN 'success';
END
$fun$;

/* Créer une fonction de mise à jour pour le type en paramètre. La nouvelle fonction doit contenir
 * l'ancien et le nouveau objet. */
CREATE OR REPLACE FUNCTION "util".create_update_function(_type text, _schema_name text)
 RETURNS text
 LANGUAGE plpgsql
AS $fun$
  declare
    _inschema text := _schema_name || '_api';
    _schema text := _schema_name || '_pri';
    _table text := _type;
    r "util"."infotype";
    rArray "util"."infotype"[];
    fieldtext text;
    valuetext text;
    settext text;
    sqlCode text;
    sqlSelect text;
    idCondition text;
  begin

    select array (select (column_name::text, udt_schema::text, udt_name::text)::"util"."infotype"
      from information_schema.columns where table_schema = _schema and table_name = _table)
      into rArray ;

    --raise notice 'size %',  array_length(rArray, 1);

    fieldtext := '';
    valuetext := '';
    settext := '';

    foreach r in array rArray
    loop
      fieldtext:= fieldtext || r.id || ',';
      valuetext:= valuetext || '_value.' || r.id || ',';
      settext := settext || r.id || '=' || '_value.' || r.id || ',';
    end loop;

    fieldtext:= substring(fieldtext,1, length(fieldtext)-1);
    valuetext:= substring(valuetext,1, length(valuetext)-1);
    settext:= substring(settext,1, length(settext)-1);


    select "util".condition_on_id(_table) into idCondition;

    sqlSelect := FORMAT('select * from %s.%s where %s into _valueindb;', _schema, _table, idCondition);

    sqlCode:= FORMAT('
              create or replace function "%s"."update_%s" (
                _value "%s_obj".%s,
                _oldvalue "%s_obj".%s
              )
              returns "%s_obj".%s
              language plpgsql
              as $fun2$
                declare
                  _returnvalue "%s_obj".%s;
                  _valueindb "%s_obj".%s;
                begin
                  %s
                  if(_valueindb = _oldvalue) then
                    update %s.%s
                    set %s
                    where
                    %s
                    returning %s into _returnvalue;
                  else
                    raise exception ''Objet à mettre à jour a précédemment changé.'';
                  end if;
                  return _returnvalue;
                end;
              $fun2$;
              ', _inschema, _table, _schema_name, _table,_schema_name, _table,_schema_name, _table,_schema_name, _table,_schema_name, _table, sqlSelect,
              _schema, _table, settext, idCondition, fieldtext );

    --raise notice 'sqlCode %',sqlCode;
    execute sqlCode;

    RETURN 'success';
END
$fun$;


/* Créer une fonction de suppression pour le type en paramètre. La nouvelle fonction verifie que
 * l'objet n'a pas changé dans le modèle avant de le supprimer. */
CREATE OR REPLACE FUNCTION "util".create_delete_function(_type text, _schema_name text)
 RETURNS text
 LANGUAGE plpgsql
AS $fun$
  declare
    _inschema text := _schema_name || '_api';
    _schema text := _schema_name || '_pri';
    _table text := _type;
    sqlSelect text;
    sqlDelete text;
    sqlCode text;
    idCondition text;
  begin

    select "util".condition_on_id(_table) into idCondition;

    sqlSelect := FORMAT('select * from %s.%s where %s into _valueindb;', _schema, _table, idCondition);


    sqlDelete := FORMAT('
                  if(_valueindb = _value) then
                    delete from %s.%s where %s;
                  else
                    raise exception ''Objet à supprimer a précédemment changé. %% : %%'', ''%s'' , _value.id_%s;
                  end if;',
                  _schema, _table, idCondition, _table, _table);


    sqlCode:= FORMAT('
              create or replace function "%s"."delete_%s" (
                _value "%s_obj".%s
              )
              returns void
              language plpgsql
              as $fun2$
                declare
                  _valueindb "%s_obj".%s;
                begin
                  %s

                  %s
                end
              $fun2$;',
              _inschema, _table,_schema_name, _table,_schema_name, _table, sqlSelect, sqlDelete);

    --raise notice 'sqlCode %', sqlCode;
    execute sqlCode;

    RETURN 'success';
END
$fun$;

CREATE OR REPLACE FUNCTION "util".create_search_function(_type text, _schema_name text)
 RETURNS text
 LANGUAGE plpgsql
AS $fun$
  declare
    _inschema text := _schema_name || '_api';
    _schema text := _schema_name || '_pri';
    _table text := _type;
    r "util"."infotype";
    rArray "util"."infotype"[];
    fieldtext text;
    searchtext text;
    sqlSelect text;
    sqlCode text;
    idCondition text;
  begin

    select array (select (column_name::text, udt_schema::text, udt_name::text)::"util"."infotype"
      from information_schema.columns where table_schema = _schema and table_name = _table)
      into rArray ;

    fieldtext := '';
    searchtext := '';

    foreach r in array rArray
    loop
      fieldtext:= fieldtext || r.id || ',';
      searchtext:= searchtext || r.id || '::text' || ' ilike _value' || ' or ';
    end loop;

    fieldtext:= substring(fieldtext,1, length(fieldtext)-1);
    searchtext:= substring(searchtext,1, length(searchtext)-4);

    sqlSelect := FORMAT('select array (select (%s)::%s_obj.%s from %s.%s where %s) into resultArray;', fieldtext, _schema_name, _table, _schema, _table, searchtext);

    sqlCode:= FORMAT('
              create or replace function "%s"."search_%s" (
                _value text
              )
              returns "%s_obj".%s[]
              language plpgsql
              as $fun2$
                declare
                   resultArray "%s_obj".%s[];
                begin
                  _value := _value || ''%%'';
                  %s
                  raise notice ''search sql %s'';
                  return resultArray;
                end;
              $fun2$;
              ', _inschema, _table, _schema_name, _table, _schema_name, _table, sqlSelect, sqlSelect);

    execute sqlCode;

    RETURN 'success';
END
$fun$;

CREATE OR REPLACE FUNCTION "util".create_getwithids_function(_type text, _schema_name text)
 RETURNS text
 LANGUAGE plpgsql
AS $fun$
  declare
    _inschema text := _schema_name || '_api';
    _schema text := _schema_name || '_pri';
    _table text := _type;
    r "util"."infotype";
    rArray "util"."infotype"[];
    fieldtext text;
    searchtext text;
    sqlSelect text;
    sqlCode text;
    idCondition text;
  begin

    select array (select (column_name::text, udt_schema::text, udt_name::text)::"util"."infotype"
      from information_schema.columns where table_schema = _schema and table_name = _table)
      into rArray ;

    fieldtext := '';
    searchtext := '';

    foreach r in array rArray
    loop
      fieldtext:= fieldtext || r.id || ',';
    end loop;

    fieldtext:= substring(fieldtext,1, length(fieldtext)-1);

    sqlSelect := FORMAT('select array (select (%s)::%s_obj.%s from %s.%s ', fieldtext, _schema_name, _table, _schema, _table);

    -- Pour des arrays il faut utiliser = ANY au lieu de in.
    -- https://dba.stackexchange.com/questions/317020/postgres-11-passing-a-list-of-uuids-to-function
    sqlCode:= FORMAT('
              create or replace function "%s"."getwithids_%s" (
                _value text[]
              )
              returns "%s_obj".%s[]
              language plpgsql
              as $fun2$
                declare
                   resultArray "%s_obj".%s[];
                begin
                  %s where id_%s = ANY (_value)) into resultArray;
                  raise notice ''getwithids sql %s'';
                  return resultArray;
                end;
              $fun2$;
              ', _inschema, _table, _schema_name, _table, _schema_name, _table, sqlSelect, _table, sqlSelect);

    execute sqlCode;

    RETURN 'success';
END
$fun$;

CREATE OR REPLACE FUNCTION "util".create_getwithidsforuuid_function(_type text, _schema_name text)
 RETURNS text
 LANGUAGE plpgsql
AS $fun$
  declare
    _inschema text := _schema_name || '_api';
    _schema text := _schema_name || '_pri';
    _table text := _type;
    r "util"."infotype";
    rArray "util"."infotype"[];
    fieldtext text;
    searchtext text;
    sqlSelect text;
    sqlCode text;
    idCondition text;
  begin

    select array (select (column_name::text, udt_schema::text, udt_name::text)::"util"."infotype"
      from information_schema.columns where table_schema = _schema and table_name = _table)
      into rArray ;

    fieldtext := '';
    searchtext := '';

    foreach r in array rArray
    loop
      fieldtext:= fieldtext || r.id || ',';
    end loop;

    fieldtext:= substring(fieldtext,1, length(fieldtext)-1);

    sqlSelect := FORMAT('select array (select (%s)::%s_obj.%s from %s.%s ', fieldtext, _schema_name, _table, _schema, _table);

    -- Pour des arrays il faut utiliser = ANY au lieu de in.
    -- https://dba.stackexchange.com/questions/317020/postgres-11-passing-a-list-of-uuids-to-function
    sqlCode:= FORMAT('
              create or replace function "%s"."getwithids_%s" (
                _value uuid[]
              )
              returns "%s_obj".%s[]
              language plpgsql
              as $fun2$
                declare
                   resultArray "%s_obj".%s[];
                begin
                  %s where id_%s = ANY (_value)) into resultArray;
                  raise notice ''getwithids sql %s'';
                  return resultArray;
                end;
              $fun2$;
              ', _inschema, _table, _schema_name, _table, _schema_name, _table, sqlSelect, _table, sqlSelect);

    execute sqlCode;

    RETURN 'success';
END
$fun$;

--Provient de Gemini avec ajustement pour la chaîne vide.
CREATE OR REPLACE FUNCTION util.is_valid_uuid(str text)
 RETURNS boolean
 LANGUAGE plpgsql
 IMMUTABLE
AS $function$
BEGIN
IF str IS NULL THEN
  return FALSE;
END IF;

PERFORM str::uuid;
  return TRUE;
EXCEPTION 
  WHEN invalid_text_representation THEN
    return FALSE;
END;
$function$
;

CREATE OR REPLACE FUNCTION "util".is_uuid(_value text)
 RETURNS text
 LANGUAGE plpgsql
AS $fun$
  DECLARE
    _result text;
  BEGIN
  IF "util".is_valid_uuid(_value) THEN
      _result := _value;
  ELSE
      _result := quote_literal(_value);
  END IF;
  RETURN _result;
END
$fun$;

CREATE OR REPLACE FUNCTION util.convert_id(_value text[])
 RETURNS text
 LANGUAGE plpgsql
AS $function$
  DECLARE
    _result text[];
  _resulttext text;
  BEGIN
  RAISE NOTICE 'DEBUG0 : %', _value;
  IF _value IS NULL THEN
    _result := ARRAY[''];
  ELSE
  SELECT array_to_string(_value, ',', '*') into _resulttext;
  RAISE NOTICE 'DEBUG1 %', _resulttext;
  select regexp_replace(_resulttext, E'[\\n\\r() "]*', '', 'g' ) into _resulttext;
  RAISE NOTICE 'DEBUG2 %', _resulttext;
  _resulttext := '{'||_resulttext||'}';
  RAISE NOTICE 'DEBUG3 %', _resulttext;
  _result := _resulttext::text[];
  END IF;
  RAISE NOTICE 'value=% type=%',
    _result,
    pg_typeof(_result);
  return _result;
END
$function$
;


CREATE OR REPLACE FUNCTION "util".create_get_function(_typein text, _schema_name text, _typeout text)
 RETURNS text
 LANGUAGE plpgsql
AS $fun$
  declare
    _inschema text := _schema_name || '_api';
    _schema text := _schema_name || '_pri';
    typeout_fieldtext text;
    sqlCode text;
    _typeinid text;
    idarray text[];
    idarraysize integer;
  begin

    SELECT "util".getfieldtext(_typeout, _schema_name) INTO typeout_fieldtext;

    idarray := regexp_split_to_array(_typein, '_');
    idarraysize := array_length(idarray, 1);

    if(idarraysize = 2) THEN
      _typeinid := idarray[2];
    else
      _typeinid := _typein;
    END IF;

    sqlCode:= FORMAT('
              create or replace function "%1$s_api"."get_%3$s" (
                _objets "%1$s_obj"."%2$s"[]
              )
              returns "%1$s_obj".%3$s[]
              language plpgsql
              as $fun2$
                declare
                  results "%1$s_obj"."%3$s"[] := ARRAY[]::"%1$s_obj"."%3$s"[];
                  relations "%1$s_obj"."%3$s"[];
                  var "%1$s_obj"."%2$s";
                  ids text[];
                  idsC text[];
                begin
                  foreach var in array _objets
                  loop
                    ids = array_append(ids, var.id_%5$s); 
                  end loop;
                  idsC = "util".convert_id(ids);
                  SELECT
                    ARRAY (
                      SELECT
                        (%4$s)::"%1$s_obj"."%3$s"
                      FROM
                        "%1$s_pri"."%3$s"
                      WHERE
                        id_%5$s = ANY(idsC)) INTO relations;
                  
                  RETURN (results || relations)::"%1$s_obj"."%3$s"[];
                end;
              $fun2$;
              ', _schema_name, _typein, _typeout, typeout_fieldtext, _typeinid);

    execute sqlCode;

    RETURN 'success';
END
$fun$;

CREATE OR REPLACE FUNCTION "util".create_crud_function(_type text, _schema_name text)
 RETURNS text
 LANGUAGE plpgsql
AS $fun$
  declare
    resultcreate text;
    resultread text;
    resultupdate text;
    resultdelete text;
  begin
    select "util".create_create_function(_type, _schema_name) into resultcreate;
    select "util".create_read_function(_type, _schema_name) into resultread;
    select "util".create_update_function(_type, _schema_name) into resultupdate;
    select "util".create_delete_function(_type, _schema_name) into resultdelete;
    RETURN 'success';
END
$fun$;

/* Créer une fonction lecture pour le type en paramètre. La nouvelle fonction doit contenir au moins
 * l'identifiant de l'objet. */
CREATE OR REPLACE FUNCTION "util".create_getall_function(_type text, _schema_name text)
 RETURNS text
 LANGUAGE plpgsql
AS $fun$
  declare
    _inschema text := _schema_name || '_api';
    _schema text := _schema_name || '_pri';
    _table text := _type;
    r "util"."infotype";
    rArray "util"."infotype"[];
    fieldtext text;
    searchtext text;
    sqlSelect text;
    sqlCode text;
    idCondition text;
  begin

    select array (select (column_name::text, udt_schema::text, udt_name::text)::"util"."infotype"
      from information_schema.columns where table_schema = _schema and table_name = _table)
      into rArray ;

    fieldtext := '';

    foreach r in array rArray
    loop
      fieldtext:= fieldtext || r.id || ',';
    end loop;

    fieldtext:= substring(fieldtext,1, length(fieldtext)-1);

    sqlSelect := FORMAT('select array (select (%s)::%s_obj.%s from %s.%s) into resultArray;', fieldtext, _schema_name, _table, _schema, _table);

    sqlCode:= FORMAT('
              create or replace function "%s"."getall_%s" ()
              returns "%s_obj".%s[]
              language plpgsql
              as $fun2$
                declare
                   resultArray "%s_obj".%s[];
                begin
                  %s
                  raise notice ''get sql %s'';
                  return resultArray;
                end;
              $fun2$;
              ', _inschema, _table, _schema_name, _table, _schema_name, _table, sqlSelect, sqlSelect);

    execute sqlCode;

    RETURN 'success';
END
$fun$;

CREATE OR REPLACE FUNCTION "util".getfieldtext(_table text, _schema_name text)
 RETURNS text
 LANGUAGE plpgsql
AS $fun$
  declare
    _schema text := _schema_name || '_pri';
    r "util"."infotype";
    rArray "util"."infotype"[];
    fieldtext text := '';
  begin
    select array (select (column_name::text, udt_schema::text, udt_name::text)::"util"."infotype"
      from information_schema.columns where table_schema = _schema and table_name = _table)
      into rArray ;
    foreach r in array rArray
    loop
      fieldtext:= fieldtext || r.id || ',';
    end loop;
    RETURN substring(fieldtext,1, length(fieldtext)-1);
END
$fun$;

-- https://stackoverflow.com/questions/59164197/cast-value-to-type-text-using-string-representation-of-type-text
-- il faut connaître le type pour que la fonction marche.
-- Je peux faire un enum et tester le cast avant de la passer à la fonction.
create or replace function "util"."call_api" (_fonction text, _argument text, _type text)
  returns text
  language plpgsql
as $fun$
  declare
  _fonctionObj regproc;
  _typeObj regtype;
  begin
     _fonctionObj = format('concevoir_api.%s', _fonction)::regproc;
     _typeObj = format('concevoir_obj.%s', _type)::regtype;
     --assert select "util"."trycast"(_argument, _type);
     execute format ('select %s(%s::%s);', _fonctionObj, _argument, _typeObj);
     return 'success';
  end;
$fun$;

--Ce sont les mêmes éléments supportés par la grammaire.

create or replace function "util"."trycast" (_arg text, _type text)
  returns boolean
  language plpgsql
as $fun2$
  declare
    castresult text;
  begin
    CASE
        WHEN _type ='concevoir_obj.session' THEN
          castresult := _arg::"concevoir_obj"."session";
        WHEN _type ='concevoir_obj.document' THEN
          castresult := _arg::"concevoir_obj"."document";
        ELSE
          raise exception 'Type non supporté : %', _type;
    END CASE;

    return true;
  end;
$fun2$;

-- pour un parcours d'arbre on peut implémenter une fonctionne utilaire qui va recalculé la complétude,
-- cela va servir pour le lock, il faut se servir du catalogue pour récupérer les infos sur les tables.
-- niveau 1 après niveau2 etc.
create or replace function "util"."find_relation" (_arg text, _type text)
  returns boolean
  language plpgsql
as $fun2$
  declare
    castresult text;
  begin
    SELECT
    tc.table_schema,
    tc.constraint_name,
    tc.table_name,
    kcu.column_name,
    ccu.table_schema AS foreign_table_schema,
    ccu.table_name AS foreign_table_name,
    ccu.column_name AS foreign_column_name
    FROM information_schema.table_constraints AS tc
    JOIN information_schema.key_column_usage AS kcu
        ON tc.constraint_name = kcu.constraint_name
        AND tc.table_schema = kcu.table_schema
    JOIN information_schema.constraint_column_usage AS ccu
        ON ccu.constraint_name = tc.constraint_name
    WHERE tc.constraint_type = 'FOREIGN KEY'
        AND tc.table_schema='myschema' --ccu à la place pour la provenance.
        AND tc.table_name='mytable';

    return true;
  end;
$fun2$;
