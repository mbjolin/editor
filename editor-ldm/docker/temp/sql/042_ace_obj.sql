--- ===========
--- Création du schéma
--- ===========

drop schema if exists "ace_obj" cascade;

create schema ace_obj;
comment on schema ace_obj is
  'Modélisation du processus qui sert à solution un problème - section privée.';

--- ===========
--- Création des types
--- ===========

set search_path to "ace_obj";

select "util".create_type('tab', 'ace');