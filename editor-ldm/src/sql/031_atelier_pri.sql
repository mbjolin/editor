--- ===========
--- Creation du schema
--- ===========

drop schema if exists "atelier_pri" cascade;

create schema atelier_pri;
comment on schema atelier_pri is
  'Modélisation du processus de raffinement d’un document en langage textuelle - section privee.';

--- ===========
--- Creation des tables
--- ===========

set search_path to "atelier_pri";

create table "intrant" (
  id_intrant uuid not null,
  titre text not null,
  extension text NOT null,
  contenu text not null,
  transformation text[] not null,
  etape atelier_pub."etape" not null,
  constraint "intrant_pk0" primary key (id_intrant)
);

create table "intrant_document" (
  id_intrant uuid not null,
  id_document text not null,
  constraint "intrant_document_pk0" primary key (id_intrant, id_document),
  constraint "intrant_document_fk0" foreign key (id_intrant) references "intrant",
  constraint "intrant_document_fk1" foreign key (id_document) references "document_pri"."document"
);

-- Je pourrais probablement utiliser une foreign key array pour les intrants.
-- Mais, je ne suis pas certain de vouloir coupler ces tables.
-- Probablement, je préférais un méthode cleanup qui supprimer les intrants.
-- intrant_db lorsqu'il n'y a plus de référence de document.
-- intrant_ui lorsqu'il n'y a plus de référence d'atelier.

create table "atelier" (
  config text NOT NULL,
  date_creation timestamp NOT NULL,
  date_modification timestamp NOT null,
  etat atelier_pub."etat" NOT NULL,
  id_atelier uuid not null,
  intrant_db uuid[],
  intrant_ui uuid[],
  titre text not null,
  constraint "atelier_pk0" primary key (id_atelier)
);

