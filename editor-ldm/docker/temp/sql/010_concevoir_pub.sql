--- ===========
--- Création du schéma
--- ===========

drop schema if exists "concevoir_pub" cascade;
drop role if exists "concevoir_pub";

create role concevoir_pub;
comment on role concevoir_pub is
  'Groupe des utilisateurs pouvant utiliser l’interface du modèle client du module "concevoir".';

create schema concevoir_pub;
comment on schema concevoir_pub is
  'Modélisation du processus qui sert à solutionner un problème - section publique.';

grant usage on schema concevoir_pub to concevoir_pub;

--- ===========
--- Création des domaines
--- ===========

set search_path to "concevoir_pub";

create type "format" as enum ('modèle', 'text');
create type "rôle" as enum ('admin', 'user');
create type "cause" as enum ('ajout', 'retrait', 'correction', 'révision', 'ramaniement');
