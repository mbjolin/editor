--- ===========
--- Création du schéma
--- ===========

drop schema if exists "document_pub" cascade;
drop role if exists "document_pub";

create role document_pub;
comment on role document_pub is
  'Groupe des utilisateurs pouvant utiliser l’interface du modèle client du module "document".';

create schema document_pub;
comment on schema document_pub is
  'Modélisation du processus qui sert à solutionner un problème - section publique.';

grant usage on schema document_pub to document_pub;

--- ===========
--- Création des domaines
--- ===========

set search_path to "document_pub";

