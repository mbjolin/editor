
--- ===========
--- Creation du schema
--- ===========

drop schema if exists "document_pri" cascade;

create schema document_pri;
comment on schema document_pri is
  'Modelisation du processus qui sert à solution un probleme - section privee.';

--- ===========
--- Modélisation du document
--- ===========

set search_path to "document_pri";

create table "document" (
  id_document text not null,
  id_projet text NOT NULL,
  constraint "document_pk0" primary key (id_document),
  constraint "document_fk1" foreign key (id_projet) references "concevoir_pri"."projet"
);

create table "meta" (
  etiquette text not null,
  id_meta text not null,
  valeur text NOT null,
  constraint "meta_pk0" primary key (id_meta)
);

create table "document_meta" (
  id_document text not null,
  id_meta text not null,
  constraint "document_meta_pk0" primary key (id_document, id_meta),
  constraint "document_meta_fk0" foreign key (id_document) references "document",
  constraint "document_meta_fk1" foreign key (id_meta) references "meta"
);


create table "section" (
  id_section text not null,
  etiquette text not null,
  racinaire boolean not null DEFAULT FALSE, 
  constraint "section_pk0" primary key (id_section)
);

create table "section_section" (
  id_section text not null,
  id_enfant text not null,
  constraint "section_section_pk0" primary key (id_section, id_enfant),
  constraint "section_section_fk0" foreign key (id_section) references "section",
  constraint "section_section_fk1" foreign key (id_enfant) references "section"
);

create table "document_section" (
  id_document text not null,
  id_section text not null,
  constraint "document_section_pk0" primary key (id_document, id_section),
  constraint "document_section_fk0" foreign key (id_document) references "document",
  constraint "document_section_fk1" foreign key (id_section) references "section"
);

create table "paragraphe" (
  id_paragraphe text not null,
  somecolumn text,
  constraint "paragraphe_pk0" primary key (id_paragraphe)
);

create table "section_paragraphe" (
  id_section text not null,
  id_paragraphe text not null,
  constraint "section_paragraphe_pk0" primary key (id_section, id_paragraphe),
  constraint "section_paragraphe_fk0" foreign key (id_section) references "section",
  constraint "section_paragraphe_fk1" foreign key (id_paragraphe) references "paragraphe"
);

create table "citation" (
  id_citation text not null,
  description text NOT NULL,
  constraint "citation_pk0" primary key (id_citation)
);

create table "paragraphe_citation" (
  id_paragraphe text not null,  
  id_citation text not null,
  constraint "paragraphe_citation_pk0" primary key (id_paragraphe, id_citation),
  constraint "paragraphe_citation_fk0" foreign key (id_paragraphe) references "paragraphe",
  constraint "paragraphe_citation_fk1" foreign key (id_citation) references "citation"
);

create table "contenu" (
  id_contenu text not null,
  description text NOT null,
  constraint "contenu_pk0" primary key (id_contenu)
);

create table "paragraphe_contenu" (
  id_paragraphe text not null,  
  id_contenu text not null,
  constraint "paragraphe_contenu_pk0" primary key (id_paragraphe, id_contenu),
  constraint "paragraphe_contenu_fk0" foreign key (id_paragraphe) references "paragraphe",
  constraint "paragraphe_contenu_fk1" foreign key (id_contenu) references "contenu"
);

create table "reglelogique" (
  id_reglelogique text not null,
  description_formelle text,
  description_naturelle text,
  constraint "reglelogique_pk0" primary key (id_reglelogique),
  constraint "reglelogique_fk0" check (description_formelle IS NOT NULL OR description_naturelle IS NOT NULL)
);

create table "paragraphe_reglelogique" (
  id_paragraphe text not null,
  id_reglelogique text not null,
  constraint "paragraphe_reglelogique_pk0" primary key (id_paragraphe, id_reglelogique),
  constraint "paragraphe_reglelogique_fk0" foreign key (id_paragraphe) references "paragraphe",
  constraint "paragraphe_reglelogique_fk1" foreign key (id_reglelogique) references "reglelogique"
);

create table "source" (
  id_source text not null,
  description text not null,
  constraint "source_pk0" primary key (id_source)
);

create table "citation_source" (
  id_citation text not null,
  id_source text not null,
  constraint "citation_source_pk0" primary key (id_citation, id_source),
  constraint "citation_source_fk0" foreign key (id_citation) references "citation",
  constraint "citation_source_fk1" foreign key (id_source) references "source"
);

create table "reglelogique_source" (
  id_reglelogique text not null,
  id_source text not null,
  constraint "reglelogique_source_pk0" primary key (id_reglelogique, id_source),
  constraint "reglelogique_source_fk0" foreign key (id_reglelogique) references "reglelogique",
  constraint "reglelogique_source_fk1" foreign key (id_source) references "source"
);

create table "contenu_concept" (
  id_contenu text not null,
  id_concept uuid not null,
  constraint "contenu_concept_pk0" primary key (id_contenu, id_concept),
  constraint "contenu_concept_fk0" foreign key (id_contenu) references "contenu",
  constraint "contenu_concept_fk1" foreign key (id_concept) references "concevoir_pri"."concept"
);

create table "citation_concept" (
  id_citation text not null,
  id_concept uuid not null,
  constraint "citation_concept_pk0" primary key (id_citation, id_concept),
  constraint "citation_concept_fk0" foreign key (id_citation) references "citation",
  constraint "citation_concept_fk1" foreign key (id_concept) references "concevoir_pri"."concept"
);

create table "reglelogique_concept" (
  id_reglelogique text not null,
  id_concept uuid not null,
  constraint "reglelogique_concept_pk0" primary key (id_reglelogique, id_concept),
  constraint "reglelogique_concept_fk0" foreign key (id_reglelogique) references "reglelogique",
  constraint "reglelogique_concept_fk1" foreign key (id_concept) references "concevoir_pri"."concept"
);

create table "source_concept" (
  id_source text not null,
  id_concept uuid not null,
  constraint "source_concept_pk0" primary key (id_source, id_concept),
  constraint "source_concept_fk0" foreign key (id_source) references "source",
  constraint "source_concept_fk1" foreign key (id_concept) references "concevoir_pri"."concept"
);
