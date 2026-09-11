--- ===========
--- Création du schéma
--- ===========

drop schema if exists "concevoir_obj" cascade;

create schema concevoir_obj;
comment on schema concevoir_obj is
  'Modélisation du processus qui sert à solution un problème - section privée.';

--- ===========
--- Création des types
--- ===========

--- Note : Postgresql crée automatiquement des types pour chacune des tables. Je préfère garder
--- la séparation de mes types dans une schéma distinct.

set search_path to "concevoir_obj";

select "util".create_type('projet', 'concevoir');
select "util".create_type('transformation', 'concevoir');
select "util".create_type('utilisateur', 'concevoir');

select "util".create_type('session', 'concevoir');
select "util".create_type('action', 'concevoir');
select "util".create_type('session_action', 'concevoir');

select "util".create_type('concept', 'concevoir');

select "util".create_type('simplesession', 'concevoir');
