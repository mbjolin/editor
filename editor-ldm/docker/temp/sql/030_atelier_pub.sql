--- ===========
--- Création du schéma
--- ===========

drop schema if exists "atelier_pub" cascade;
drop role if exists "atelier_pub";

create role atelier_pub;
comment on role atelier_pub is
  'Groupe des utilisateurs pouvant utiliser l’interface du modèle client du module "atelier".';

create schema atelier_pub;
comment on schema atelier_pub is
  'Modélisation du processus de raffinement d’un document en langage textuelle - section public.';

grant usage on schema atelier_pub to atelier_pub;

--- ===========
--- Création des domaines
--- ===========

set search_path to "atelier_pub";

create type "etape" as enum ('EPURER', 'FORMALISER', 'PRESENTER');
create type "etat" as enum (
'NOT_STARTED', 
'EPURER_SUCCESS', 'EPURER_FAIL', 
'FORMALISER_SUCCESS', 'FORMALISER_FAIL', 
'FORMALISER_RULE_SUCCESS', 'FORMALISER_RULE_FAIL', 
'PRESENTER_SUCCESS', 'PRESENTER_FAIL',
'ATELIER_SUCCESS', 'ATELIER_FAIL', 
'FINISH');

