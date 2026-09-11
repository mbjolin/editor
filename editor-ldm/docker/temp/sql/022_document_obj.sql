--- ===========
--- Création du schéma
--- ===========

drop schema if exists "document_obj" cascade;

create schema document_obj;
comment on schema document_obj is
  'Modélisation du processus qui sert à solution un problème - section privée.';

--- ===========
--- Création des types
--- ===========

set search_path to "document_obj";

select "util".create_type('document', 'document');
select "util".create_type('meta', 'document');
select "util".create_type('document_meta', 'document');
select "util".create_type('section', 'document');
select "util".create_type('document_section', 'document');
select "util".create_type('section_section', 'document');
select "util".create_type('paragraphe', 'document');

select "util".create_type('section_paragraphe', 'document');
select "util".create_type('citation', 'document');
select "util".create_type('paragraphe_citation', 'document');
select "util".create_type('contenu', 'document');
select "util".create_type('paragraphe_contenu', 'document');
select "util".create_type('reglelogique', 'document');
select "util".create_type('paragraphe_reglelogique', 'document');

select "util".create_type('source', 'document');
select "util".create_type('citation_source', 'document');
select "util".create_type('reglelogique_source', 'document');

select "util".create_type('contenu_concept', 'document');
select "util".create_type('reglelogique_concept', 'document');
select "util".create_type('citation_concept', 'document');
select "util".create_type('source_concept', 'document');

CREATE TYPE "document_obj"."relation" AS (
  document_meta "document_obj"."document_meta"[],
  document_section "document_obj"."document_section"[],
  section_paragraphe "document_obj"."section_paragraphe"[],
  section_section "document_obj"."section_section"[],
  paragraphe_contenu "document_obj"."paragraphe_contenu"[],
  paragraphe_citation "document_obj"."paragraphe_citation"[],
  paragraphe_reglelogique "document_obj"."paragraphe_reglelogique"[],
  citation_source "document_obj"."citation_source"[],
  reglelogique_source "document_obj"."reglelogique_source"[]
);

CREATE TYPE "document_obj"."objet" AS (
  documents "document_obj"."document"[],
  metas "document_obj"."meta"[],
  sections "document_obj"."section"[],
  paragraphes "document_obj"."paragraphe"[],
  contenus "document_obj"."contenu"[],
  citations "document_obj"."citation"[],
  reglelogiques "document_obj"."reglelogique"[],
  sources "document_obj"."source"[]
);

CREATE TYPE "document_obj"."modele" AS (
  objet "document_obj"."objet",
  relation "document_obj"."relation"
);

