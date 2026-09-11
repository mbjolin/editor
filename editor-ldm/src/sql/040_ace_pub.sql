--- ===========
--- Création du schéma
--- ===========

drop schema if exists "ace_pub" cascade;
drop role if exists "ace_pub";

create role ace_pub;
comment on role ace_pub is
  'Groupe des utilisateurs pouvant utiliser l’interface du modèle client du module "ace".';

create schema ace_pub;
comment on schema ace_pub is
  'Modélisation d’une représentation persistante de l’outil ACE c9.io et de la démo playground - section public.';

grant usage on schema ace_pub to ace_pub;

--- ===========
--- Création des domaines
--- ===========

set search_path to "ace_pub";

-- En minuscule comme en js.
create type "group_ui" as enum ('main', 'result', 'console');
