--- ===========
--- Création du schéma
--- ===========

drop schema if exists "atelier_obj" cascade;

create schema atelier_obj;
comment on schema atelier_obj is
  'Modélisation du processus qui sert à solution un problème - section privée.';

--- ===========
--- Création des types
--- ===========

set search_path to "atelier_obj";

select "util".create_type('intrant', 'atelier');
select "util".create_type('intrant_document', 'atelier');
select "util".create_type('atelier', 'atelier');
