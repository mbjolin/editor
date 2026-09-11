--- ===========
--- Création du schéma
--- ===========

drop schema if exists "atelier_api" cascade;

create schema atelier_api;
comment on schema atelier_api is
  'Interface à appeler pour modifier le modèle.';

--- ===========
--- Création des CRUD
--- ===========

set search_path to "atelier_api";

select "util".create_crud_function('intrant', 'atelier');
select "util".create_getwithidsforuuid_function('intrant', 'atelier');
select "util".create_crud_function('intrant_document', 'atelier');
select "util".create_crud_function('atelier', 'atelier');

select "util".create_search_function('intrant', 'atelier');
select "util".create_search_function('intrant_document', 'atelier');
select "util".create_search_function('atelier', 'atelier');

select "util".create_getall_function('intrant', 'atelier');
select "util".create_getall_function('atelier', 'atelier');

--- ===========
--- Création des API plus complexes
--- ===========

