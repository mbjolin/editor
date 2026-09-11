--- ===========
--- Création du schéma
--- ===========

drop schema if exists "ace_api" cascade;

create schema ace_api;
comment on schema ace_api is
  'Interface à appeler pour modifier le modèle.';

--- ===========
--- Création des CRUD
--- ===========

set search_path to "ace_api";

select "util".create_crud_function('tab', 'ace');
select "util".create_search_function('tab', 'ace');
select "util".create_getall_function('tab', 'ace');
select "util".create_getwithidsforuuid_function('tab', 'ace');

--- ===========
--- Création des API plus complexes
--- ===========

