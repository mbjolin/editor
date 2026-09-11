--- ===========
--- Création du schéma
--- ===========

drop schema if exists "concevoir_api" cascade;

create schema concevoir_api;
comment on schema concevoir_api is
  'Interface à appeler pour modifier le modèle.';

--- ===========
--- Création des CRUD
--- ===========

set search_path to "concevoir_api";

select "util".create_crud_function('projet', 'concevoir');
select "util".create_search_function('projet', 'concevoir');
select "util".create_getall_function('projet', 'concevoir');

select "util".create_crud_function('transformation', 'concevoir');
select "util".create_search_function('transformation', 'concevoir');
select "util".create_getall_function('transformation', 'concevoir');

select "util".create_crud_function('utilisateur', 'concevoir');
select "util".create_search_function('utilisateur', 'concevoir');

select "util".create_crud_function('simplesession', 'concevoir');
select "util".create_search_function('simplesession', 'concevoir');

select "util".create_crud_function('session', 'concevoir');
select "util".create_crud_function('action', 'concevoir');
select "util".create_crud_function('session_action', 'concevoir');

select "util".create_crud_function('concept', 'concevoir');
select "util".create_search_function('concept', 'concevoir');
select "util".create_getall_function('concept', 'concevoir');

--Pendant le CRUD ajouter l'utilisateur qui le modifie?

--- ===========
--- Création des API plus complexes
--- ===========
--drop function "concevoir_api"."execute_session";
create or replace function "concevoir_api"."execute_session" (
  _value2 "concevoir_obj"."session"
)
returns "concevoir_obj"."session"
language plpgsql
as $fun$
  declare
  actionarray "concevoir_obj"."action"[];
  currentaction "concevoir_obj"."action";
  newvalue "concevoir_obj"."session";
  begin

    select array (select ("session_action".id_action, fonction, argument, typecomposite)::"concevoir_obj"."action"
      from "concevoir_pri"."session_action" inner join "concevoir_pri"."action"
      on "session_action"."id_action" = "action"."id_action"
      where id_session=_value2.id_session)
      into actionarray;

    foreach currentaction in array actionarray
    loop
      perform "util"."call_api"(currentaction."fonction", currentaction."argument", currentaction."typecomposite");
    end loop;

    foreach currentaction in array actionarray
    loop
      perform "concevoir_api"."delete_session_action"((_value2.id_session, currentaction.id_action)::"concevoir_obj"."session_action");
      perform "concevoir_api"."delete_action"(currentaction);
    end loop;

    perform "concevoir_api"."delete_session"(_value2);

    _value2.moment_fin := now();
    _value2.id_session := gen_random_uuid();
    _value2.id_utilisateur := gen_random_uuid();
    return _value2::"concevoir_obj"."session";
  end;
$fun$;

--drop function "concevoir_api"."verify_session";
create or replace function "concevoir_api"."verify_session" (
  _value1 "concevoir_obj"."session"
)
returns "concevoir_obj"."session"
language plpgsql
as $fun$
  declare
  error_msg text;
  newvalue1 text;
  begin

    select "concevoir_api"."execute_session"(_value1) into newvalue1;

    -- Je ne sais pas pourquoi, mais si je l'insére dans un text que je cast après dans mon type cela
    -- fonctionne. Sinon il y a cette erreur.
    -- SQL Error [22P02]: ERREUR: syntaxe en entrée invalide pour le type uuid : « (2c5a500a-...
    -- select ("concevoir_api"."execute_session"(_value1)) into _value1;

    raise exception 'On arrive à exécuter la session';
    exception
      when raise_exception then
        get STACKED diagnostics error_msg = MESSAGE_TEXT;
        if error_msg = 'On arrive à exécuter la session' then
          raise notice 'À faire';
        else
          raise notice 'une erreur aa : %', error_msg;
        end if;

    return newvalue1::"concevoir_obj"."session";
  end;
$fun$;

--select concevoir_api.execute_session(('00000000-0000-0000-0000-100000000000','00000000-0000-0000-0000-000000000001','2024-11-11 19:49:11.925902','2024-11-11 19:49:11.925902','2024-11-11 19:49:11.925902'));

--select concevoir_api.verify_session(('00000000-0000-0000-0000-100000000000','00000000-0000-0000-0000-000000000001','2024-11-11 19:49:11.925902','2024-11-11 19:49:11.925902','2024-11-11 19:49:11.925902')::"concevoir_obj"."session");

