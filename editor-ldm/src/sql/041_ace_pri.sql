--- ===========
--- Creation du schema
--- ===========

drop schema if exists "ace_pri" cascade;

create schema ace_pri;
comment on schema ace_pri is
  'Modélisation d’une représentation persistante de l’outil ACE c9.io et de la démo playground - section privee.';

--- ===========
--- Creation des tables
--- ===========

set search_path to "ace_pri";


create table "tab" (
  contenu text not null,
  etape atelier_pub."etape" not null,
  extension text NOT NULL,
  form_ui text not null,
  group_ui ace_pub."group_ui" not null,
  id_tab uuid not null,
  langage text not null,
  titre text not null,
  constraint "tab_pk0" primary key (id_tab)
);


