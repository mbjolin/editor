--- ===========
--- Création du schéma
--- ===========

drop schema if exists "document_api" cascade;

create schema document_api;
comment on schema document_api is
  'Interface à appeler pour modifier le modèle.';

--- ===========
--- Création des CRUD
--- ===========

set search_path to "document_api";

select "util".create_crud_function('document', 'document');
select "util".create_search_function('document', 'document');
select "util".create_getall_function('document', 'document');
select "util".create_getwithids_function('document', 'document');
select "util".create_crud_function('document_meta', 'document');
select "util".create_crud_function('document_section', 'document');

select "util".create_crud_function('meta', 'document');
select "util".create_getwithids_function('meta', 'document');

select "util".create_crud_function('section', 'document');
select "util".create_crud_function('section_section', 'document');
select "util".create_crud_function('section_paragraphe', 'document');

select "util".create_crud_function('paragraphe', 'document');
select "util".create_crud_function('paragraphe_contenu', 'document');
select "util".create_crud_function('paragraphe_citation', 'document');
select "util".create_crud_function('paragraphe_reglelogique', 'document');

select "util".create_crud_function('citation', 'document');
select "util".create_search_function('citation', 'document');
select "util".create_crud_function('citation_source', 'document');

select "util".create_crud_function('contenu', 'document');

select "util".create_crud_function('reglelogique', 'document');
select "util".create_search_function('reglelogique', 'document');
select "util".create_crud_function('reglelogique_source', 'document');

select "util".create_crud_function('source', 'document');
select "util".create_search_function('source', 'document');

select "util".create_crud_function('contenu_concept', 'document');
select "util".create_crud_function('citation_concept', 'document');
select "util".create_crud_function('source_concept', 'document');
select "util".create_crud_function('reglelogique_concept', 'document');

SELECT "util".create_get_function('document', 'document','document_meta');
SELECT "util".create_get_function('document', 'document','document_section');
SELECT "util".create_get_function('document_meta', 'document','meta');
SELECT "util".create_get_function('document_section', 'document','section');
SELECT "util".create_get_function('section', 'document','section_section');
SELECT "util".create_get_function('section_section', 'document','section');
SELECT "util".create_get_function('section', 'document','section_paragraphe');
SELECT "util".create_get_function('section_paragraphe', 'document','paragraphe');
SELECT "util".create_get_function('paragraphe', 'document','paragraphe_contenu');
SELECT "util".create_get_function('paragraphe', 'document','paragraphe_citation');
SELECT "util".create_get_function('paragraphe', 'document','paragraphe_reglelogique');
SELECT "util".create_get_function('paragraphe_contenu', 'document','contenu');
SELECT "util".create_get_function('paragraphe_citation', 'document','citation');
SELECT "util".create_get_function('paragraphe_reglelogique', 'document','reglelogique');
SELECT "util".create_get_function('citation', 'document','citation_source');
SELECT "util".create_get_function('reglelogique', 'document','reglelogique_source');
SELECT "util".create_get_function('citation_source', 'document','source');
SELECT "util".create_get_function('reglelogique_source', 'document','source');

--- ===========
--- Création des API plus complexes
--- ===========
CREATE OR REPLACE FUNCTION "document_api"."get_modele" (_id_document text)
  RETURNS "document_obj"."modele"
  LANGUAGE plpgsql
  AS $fun$
DECLARE
  objet "document_obj"."objet";
  relation "document_obj"."relation";
  modele "document_obj"."modele";
  tempsection "document_obj"."section"[] := ARRAY[]::"document_obj"."section"[];
  tempsource "document_obj"."source"[] := ARRAY[]::"document_obj"."source"[];
BEGIN

  -- get Objet with ID
  SELECT "document_api"."getwithids_document"(ARRAY[_id_document]) INTO objet.documents;
  RAISE NOTICE '1 object : % | relation : %', objet , relation;

  -- getRelationsWithObjets
  SELECT "document_api"."get_document_meta"(objet.documents) INTO relation.document_meta;
  SELECT "document_api"."get_document_section"(objet.documents) INTO relation.document_section;
  RAISE NOTICE '2 object : % | relation : %', objet , relation;

  -- getObjetsWithRelations
  SELECT "document_api"."get_meta"(relation.document_meta) INTO objet.metas;
  SELECT "document_api"."get_section"(relation.document_section) INTO objet.sections;
  RAISE NOTICE '3 object : % | relation : %', objet , relation;

-- getRelationsWithObjets
  SELECT "document_api"."get_section_paragraphe"(objet.sections) INTO relation.section_paragraphe;
  SELECT "document_api"."get_section_section"(objet.sections) INTO relation.section_section;
  RAISE NOTICE '4 object : % | relation : %', objet , relation;

  -- getObjetsWithRelations
  SELECT "document_api"."get_paragraphe"(relation.section_paragraphe) INTO objet.paragraphes;
  RAISE NOTICE '5 object : % | relation : %', objet , relation;

-- getRelationsWithObjets
  SELECT "document_api"."get_paragraphe_contenu"(objet.paragraphes) INTO relation.paragraphe_contenu;
  SELECT "document_api"."get_paragraphe_citation"(objet.paragraphes) INTO relation.paragraphe_citation;
  SELECT "document_api"."get_paragraphe_reglelogique"(objet.paragraphes) INTO relation.paragraphe_reglelogique;
  RAISE NOTICE '6 object : % | relation : %', objet , relation;

  -- getObjetsWithRelations
  SELECT "document_api"."get_contenu"(relation.paragraphe_contenu) INTO objet.contenus;
  SELECT "document_api"."get_citation"(relation.paragraphe_citation) INTO objet.citations;
  SELECT "document_api"."get_reglelogique"(relation.paragraphe_reglelogique) INTO objet.reglelogiques; 
  RAISE NOTICE '7 object : % | relation : %', objet , relation;

  -- getRelationsWithObjets
  SELECT "document_api"."get_citation_source"(objet.citations) INTO relation.citation_source;
  SELECT "document_api"."get_reglelogique_source"(objet.reglelogiques) INTO relation.reglelogique_source;
  RAISE NOTICE '8 object : % | relation : %', objet , relation;

  -- getObjetsWithRelations
  SELECT "document_api"."get_source"(relation.citation_source) INTO objet.sources;
  SELECT "document_api"."get_source"(relation.reglelogique_source) INTO tempsource; --concat
  objet.sources = objet.sources || tempsource;

  modele.objet = objet;
  modele.relation = relation;
  RAISE NOTICE 'modele %', modele;

  RETURN modele::"document_obj"."modele";
END;
$fun$;

CREATE OR REPLACE FUNCTION "document_api"."create_modele" (_modele "document_obj"."modele")
  RETURNS void
  LANGUAGE plpgsql
  AS $fun$
DECLARE
  modele "document_obj"."modele";
  objet "document_obj"."objet";
  relation "document_obj"."relation";

  document "document_obj"."document";
  meta "document_obj"."meta";
  section "document_obj"."section";
  paragraphe "document_obj"."paragraphe";
  contenu "document_obj"."contenu";
  citation "document_obj"."citation";
  reglelogique "document_obj"."reglelogique";
  source "document_obj"."source";

  document_meta "document_obj"."document_meta";
  document_section "document_obj"."document_section";
  section_paragraphe "document_obj"."section_paragraphe";
  section_section "document_obj"."section_section";
  paragraphe_contenu "document_obj"."paragraphe_contenu";
  paragraphe_citation "document_obj"."paragraphe_citation";
  paragraphe_reglelogique "document_obj"."paragraphe_reglelogique";
  citation_source "document_obj"."citation_source";
  reglelogique_source "document_obj"."reglelogique_source";

BEGIN
  objet := _modele.objet::"document_obj"."objet";
  relation := _modele.relation::"document_obj"."relation";

  foreach document in array objet.documents
  loop
    perform "document_api"."create_document"(document);
  end loop;

  foreach meta in array objet.metas
  loop
    perform "document_api"."create_meta"(meta);
  end loop;

  foreach section in array objet.sections
  loop
    perform "document_api"."create_section"(section);
  end loop;

  foreach paragraphe in array objet.paragraphes
  loop
    perform "document_api"."create_paragraphe"(paragraphe);
  end loop;

  foreach contenu in array objet.contenus
  loop
    perform "document_api"."create_contenu"(contenu);
  end loop;

  foreach citation in array objet.citations
  loop
    perform "document_api"."create_citation"(citation);
  end loop;

  foreach reglelogique in array objet.reglelogiques
  loop
    perform "document_api"."create_reglelogique"(reglelogique);
  end loop;

  foreach source in array objet.sources
  loop
    perform "document_api"."create_source"(sources);
  end loop;

  foreach document_meta in array relation.document_meta
  loop
    perform "document_api"."create_document_meta"(document_meta);
  end loop;

  foreach document_section in array relation.document_section
  loop
    perform "document_api"."create_document_section"(document_section);
  end loop;

  foreach section_paragraphe in array relation.section_paragraphe
  loop
    perform "document_api"."create_section_paragraphe"(section_paragraphe);
  end loop;

  foreach section_section in array relation.section_section
  loop
    perform "document_api"."create_section_section"(section_section);
  end loop;

  foreach paragraphe_contenu in array relation.paragraphe_contenu
  loop
    perform "document_api"."create_paragraphe_contenu"(paragraphe_contenu);
  end loop;

  foreach paragraphe_citation in array relation.paragraphe_citation
  loop
    perform "document_api"."create_paragraphe_citation"(paragraphe_citation);
  end loop;

  foreach paragraphe_reglelogique in array relation.paragraphe_reglelogique
  loop
    perform "document_api"."create_paragraphe_reglelogique"(paragraphe_reglelogique);
  end loop;

  foreach citation_source in array relation.citation_source
  loop
    perform "document_api"."create_citation_source"(citation_source);
  end loop;

  foreach reglelogique_source in array relation.reglelogique_source
  loop
    perform "document_api"."create_reglelogique_source"(reglelogique_source);
  end loop;

END;
$fun$;

CREATE OR REPLACE FUNCTION "document_api"."update_modele" (_modelenew "document_obj"."modele", _modeleold "document_obj"."modele")
  RETURNS void
  LANGUAGE plpgsql
  AS $fun$
DECLARE
  modele "document_obj"."modele";
  objetnew "document_obj"."objet";
  relationnew "document_obj"."relation";
  objetold "document_obj"."objet";
  relationold "document_obj"."relation";

  document "document_obj"."document";
  meta "document_obj"."meta";
  section "document_obj"."section";
  paragraphe "document_obj"."paragraphe";
  contenu "document_obj"."contenu";
  citation "document_obj"."citation";
  reglelogique "document_obj"."reglelogique";
  source "document_obj"."source";

  document_meta "document_obj"."document_meta";
  document_section "document_obj"."document_section";
  section_paragraphe "document_obj"."section_paragraphe";
  section_section "document_obj"."section_section";
  paragraphe_contenu "document_obj"."paragraphe_contenu";
  paragraphe_citation "document_obj"."paragraphe_citation";
  paragraphe_reglelogique "document_obj"."paragraphe_reglelogique";
  citation_source "document_obj"."citation_source";
  reglelogique_source "document_obj"."reglelogique_source";

BEGIN
  objetnew := _modelenew.objet::"document_obj"."objet";
  relationnew := _modelenew.relation::"document_obj"."relation";

  objetold := _modeleold.objet::"document_obj"."objet";
  relationold := _modeleold.relation::"document_obj"."relation";

  -- Les relations
  foreach document_meta in array relationold.document_meta
  loop
    perform "document_api"."delete_document_meta"(document_meta);
  end loop;
  foreach document_section in array relationold.document_section
  loop
    perform "document_api"."delete_document_section"(document_section);
  end loop;
  
  foreach section_paragraphe in array relationold.section_paragraphe
  loop
    perform "document_api"."delete_section_paragraphe"(section_paragraphe);
  end loop;
  
  foreach section_section in array relationold.section_section
  loop
    perform "document_api"."delete_section_section"(section_section);
  end loop;
  
  foreach paragraphe_contenu in array relationold.paragraphe_contenu
  loop
    perform "document_api"."delete_paragraphe_contenu"(paragraphe_contenu);
  end loop;
  
  foreach paragraphe_citation in array relationold.paragraphe_citation
  loop
    perform "document_api"."delete_paragraphe_citation"(paragraphe_citation);
  end loop;
  
  foreach paragraphe_reglelogique in array relationold.paragraphe_reglelogique
  loop
    perform "document_api"."delete_paragraphe_reglelogique"(paragraphe_reglelogique);
  end loop;
  
   foreach citation_source in array relationold.citation_source
  loop
    perform "document_api"."delete_citation_source"(citation_source);
  end loop;
  
  foreach reglelogique_source in array relationold.reglelogique_source
  loop
    perform "document_api"."delete_reglelogique_source"(reglelogique_source);
  end loop;


  foreach source in array objetold.sources
  loop
    perform "document_api"."delete_source"(sources);
  end loop;

  foreach reglelogique in array objetold.reglelogiques
  loop
    perform "document_api"."delete_reglelogique"(reglelogique);
  end loop;
  
  
  foreach citation in array objetold.citations
  loop
    perform "document_api"."delete_citation"(citation);
  end loop;

  foreach contenu in array objetold.contenus
  loop
    perform "document_api"."delete_contenu"(contenu);
  end loop;

  foreach paragraphe in array objetold.paragraphes
  loop
    perform "document_api"."delete_paragraphe"(paragraphe);
  end loop;

  foreach section in array objetold.sections
  loop
    perform "document_api"."delete_section"(section);
  end loop;

  foreach meta in array objetold.metas
  loop
    perform "document_api"."delete_meta"(meta);
  end loop;
  
  foreach document in array objetold.documents
  loop
    perform "document_api"."delete_document"(document);
  end loop;

  /* Cela revient à la fonction update puisque tout est dans la même transaction. */

  foreach document in array objetnew.documents
  loop
    perform "document_api"."create_document"(document);
  end loop;

  foreach meta in array objetnew.metas
  loop
    perform "document_api"."create_meta"(meta);
  end loop;
  
  foreach section in array objetnew.sections
  loop
    perform "document_api"."create_section"(section);
  end loop;
  
  foreach paragraphe in array objetnew.paragraphes
  loop
    perform "document_api"."create_paragraphe"(paragraphe);
  end loop;
  
  foreach contenu in array objetnew.contenus
  loop
    perform "document_api"."create_contenu"(contenu);
  end loop;
  
  foreach citation in array objetnew.citations
  loop
    perform "document_api"."create_citation"(citation);
  end loop;
  
  foreach reglelogique in array objetnew.reglelogiques
  loop
    perform "document_api"."create_reglelogique"(reglelogique);
  end loop;
  
  foreach source in array objetnew.sources
  loop
    perform "document_api"."create_source"(sources);
  end loop;

    foreach document_meta in array relationnew.document_meta
  loop
    perform "document_api"."create_document_meta"(document_meta);
  end loop;
  
  foreach document_section in array relationnew.document_section
  loop
    perform "document_api"."create_document_section"(document_section);
  end loop;

  foreach section_paragraphe in array relationnew.section_paragraphe
  loop
    perform "document_api"."create_section_paragraphe"(section_paragraphe);
  end loop;

  foreach section_section in array relationnew.section_section
  loop
    perform "document_api"."create_section_section"(section_section);
  end loop;

  foreach paragraphe_contenu in array relationnew.paragraphe_contenu
  loop
    perform "document_api"."create_paragraphe_contenu"(paragraphe_contenu);
  end loop;

  foreach paragraphe_citation in array relationnew.paragraphe_citation
  loop
    perform "document_api"."create_paragraphe_citation"(paragraphe_citation);
  end loop;

  foreach paragraphe_reglelogique in array relationnew.paragraphe_reglelogique
  loop
    perform "document_api"."create_paragraphe_reglelogique"(paragraphe_reglelogique);
  end loop;

  foreach citation_source in array relationnew.citation_source
  loop
    perform "document_api"."create_citation_source"(citation_source);
  end loop;

  foreach reglelogique_source in array relationnew.reglelogique_source
  loop
    perform "document_api"."create_reglelogique_source"(reglelogique_source);
  end loop;
END;
$fun$;


create or replace function "document_api"."verify_creation_modele" (_modele "document_obj"."modele")
  RETURNS text
language plpgsql
as $fun$
  declare
  error_msg text;
  newvalue1 text;
  begin

    perform "document_api"."create_modele"(_modele);

    -- Je ne sais pas pourquoi, mais si je l'insére dans un text que je cast après dans mon type cela
    -- fonctionne. Sinon il y a cette erreur.
    -- SQL Error [22P02]: ERREUR: syntaxe en entrée invalide pour le type uuid : « (2c5a500a-...
    -- select ("concevoir_api"."execute_session"(_value1)) into _value1;

    raise exception 'On arrive à exécuter la session';
    exception
      when raise_exception then
        get STACKED diagnostics error_msg = MESSAGE_TEXT;
        if error_msg = 'On arrive à exécuter la session' then
          raise notice 'À faire';
        else
          raise notice 'Une erreur : %', error_msg;
          newvalue1 := 'Une erreur : %', error_msg;
        end if;

    return newvalue1;
  end;
$fun$;

create or replace function "document_api"."verify_modification_modele" (_modelenew "document_obj"."modele", _modeleold "document_obj"."modele")
  RETURNS text
language plpgsql
as $fun$
  declare
  error_msg text;
  newvalue1 text;
  begin

    perform "document_api"."update_modele"(_modelenew, _modeleold);

    -- Je ne sais pas pourquoi, mais si je l'insére dans un text que je cast après dans mon type cela
    -- fonctionne. Sinon il y a cette erreur.
    -- SQL Error [22P02]: ERREUR: syntaxe en entrée invalide pour le type uuid : « (2c5a500a-...
    -- select ("concevoir_api"."execute_session"(_value1)) into _value1;

    raise exception 'On arrive à exécuter la session';
    exception
      when raise_exception then
        get STACKED diagnostics error_msg = MESSAGE_TEXT;
        if error_msg = 'On arrive à exécuter la session' then
          raise notice 'À faire';
        else
          raise notice 'Une erreur : %', error_msg;
          newvalue1 := 'Une erreur : %', error_msg;
        end if;

    return newvalue1;
  end;
$fun$;

CREATE OR REPLACE FUNCTION document_api.delete_section_section(_value document_obj.section_section)
 RETURNS void
 LANGUAGE plpgsql
AS $function$
  declare
    _valueindb "document_obj".section_section;
  begin
    select * from document_pri.section_section where (id_section=_value.id_section and id_enfant=_value.id_enfant) into _valueindb;

    
    if(_valueindb = _value) then
      delete from document_pri.section_section where (id_section=_value.id_section and id_enfant=_value.id_enfant);
    else
      raise exception 'Objet à supprimer a précédemment changé. % : %', 'section_section' , _value.id_section_section;
    end if;
  end
$function$


--CREATE OR REPLACE FUNCTION document_api.get_all_concept_with_documentid(_id_document text)
-- RETURNS void
-- LANGUAGE plpgsql
--AS $function$
--  declare
--    _valueindb "document_obj".section_section;
--  begin
--    select * from document_pri.section_section where (id_section=_value.id_section and id_enfant=_value.id_enfant) into _valueindb;
--
--    
--    if(_valueindb = _value) then
--      delete from document_pri.section_section where (id_section=_value.id_section and id_enfant=_value.id_enfant);
--    else
--      raise exception 'Objet à supprimer a précédemment changé. % : %', 'section_section' , _value.id_section_section;
--    end if;
--  end
--$function$

