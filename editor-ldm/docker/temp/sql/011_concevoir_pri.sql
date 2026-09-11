--- ===========
--- Creation du schema
--- ===========

drop schema if exists "concevoir_pri" cascade;

create schema concevoir_pri;
comment on schema concevoir_pri is
  'Modelisation du processus qui sert à solution un probleme - section privee.';

--- ===========
--- Creation des tables
--- ===========

set search_path to "concevoir_pri";

--- ===========
--- Modélisation de l'action de concevoir.
--- ===========

create table "concept" (
  id_concept uuid not null,
  nom text not null,
  definition text not null,
  langue text,
  contexte text,
  alias text[],
  deconseilles text[],
  source text[],
  constraint "concept_pk0" primary key (id_concept)
);

create table "ddd" (
  id_ddd uuid not null,
  nom text not null,
  description text,
  categorie text,
  constraint "ddd_pk0" primary key (id_ddd)
);

create table "ddd_concept" (
  id_concept uuid not null,
  id_ddd uuid not null,
  constraint "ddd_concept_pk0" primary key (id_ddd, id_concept),
  constraint "ddd_concept_fk0" foreign key (id_ddd) references "ddd",
  constraint "ddd_concept_fk1" foreign key (id_concept) references "concept"
);

create table "projet" (
  id_projet text NOT NULL,
  id_ddd uuid not null,
  nom text NOT NULL,
  description text NOT NULL,
  constraint "projet_pk0" primary key (id_projet),
  constraint "projet_fk0" foreign key (id_ddd) references "ddd"
);

create table "transformation" (
  id_transformation uuid not null,
  nom text not null,
  code text,
  langage text,
  parser text,
  lexer text,
  entree "concevoir_pub".format NOT null,
  sortie "concevoir_pub".format NOT null,
  constraint "transformation_pk0" primary key (id_transformation),
  constraint "transformation_fk0" check (((code IS NOT NULL AND langage IS NOT NULL)) OR ((parser IS NOT NULL AND lexer IS NOT NULL)))
);

-- Il faut garder le username, password, role en anglais pour la library d'authentification.
create table "utilisateur" (
  id_utilisateur uuid not null,
  username text not null,
  password text not null,
  role concevoir_pub."rôle" not null,
  constraint "utilisateur_pk0" primary key (id_utilisateur)
);

-- processus
-- Reserver l'usage (utilisateur indique ce qui va être modifie, des qu'une chose est modifie.)
-- Avertir lorsqu'il y a des modifications ailleurs.
-- Il peut abandonner l'usage.
create table "session" (
  id_session uuid not null,
  id_utilisateur uuid not null,
  moment_debut timestamp not null,
  moment_execution timestamp not null,
  moment_fin timestamp not null,
  constraint "session_pk0" primary key (id_session),
  constraint "session_fk0" foreign key (id_utilisateur) references "utilisateur"
);

create table "simplesession" (
  id_simplesession uuid not null,
  titre text not null,
  systemj text not null,
  description1 text not null,
  description2 text not null,
  presente text not null,
  transformation text not null,
  moment_debut timestamp not null,
  moment_fin timestamp not null,
  constraint "simplesession_pk0" primary key (id_simplesession)
);

create table "action" (
  id_action uuid not null,
  fonction text not null,
  argument text not null,
  typecomposite text not null,
  constraint "action_pk0" primary key (id_action)
);

create table "session_action" (
  id_action uuid not null,
  id_session uuid not null,
  constraint "session_action_pk0" primary key (id_session, id_action),
  constraint "session_action_fk0" foreign key (id_session) references "session",
  constraint "session_action_fk1" foreign key (id_action) references "action"
);

--id de object
create table "verrou" (
  id_verrou uuid not null,
  argument text not null,
  constraint "verrou_pk0" primary key (id_verrou)
);

create table "session_verrou" (
  id_session uuid not null,
  id_verrou uuid not null,
  constraint "session_verrou_pk0" primary key (id_session, id_verrou),
  constraint "session_verrou_fk0" foreign key (id_session) references "session",
  constraint "session_verrou_fk1" foreign key (id_verrou) references "verrou"
);

-- Note : Indiquer qu'il y a la possibilite de gerer les conflits et comment.

-- Il manque temporalite ou snapshot à la git.
-- Il manque ownership ou l'auteur.
-- Il manque le rôle de l'utilisateur
-- Il manque le statut peut être du document, predicat ou autre.
-- peut-être faire comme citation et faire correction-paragraphe? pour aider le processus revision.

--create table "probleme_couverture" (
--  id_solution
--  id_probleme

--  moment_calcul timestamp not null default,

--);

--create table "document_couverture" (
--  id_document
--  id_probleme
--  charactere_
--  charactere_total
--  moment_calcul timestamp not null default,

--);

-- https://www.cybertec-postgresql.com/en/conditional-foreign-keys-polymorphism-in-sql/
-- https://stackoverflow.com/questions/28222533/polymorphism-for-foreign-key-constraints
-- J'ai opté pour ma propre solution.

create table "modification" (
  id_modification uuid not null,
  --auteur utilisateur not null,
  date timestamp not null,
  description text not null default '',
  --changement cause not null,
  constraint "modification_pk0" primary key ("id_modification")
);

create or replace function "concevoir_pri".foreign_key_on_objet(_uuid uuid, _table regclass)
  returns boolean
  language plpgsql
as $fun$
  declare
    issatified boolean;
    idfield text;
    resultnumber integer;
    tablearray text[];
    tablename text;
    schemaname text;
    codesql text;
  begin
    issatified := false;
    tablearray := regexp_split_to_array(_table::text, '\.');
    schemaname := tablearray[1];
    tablename := tablearray[2];
    idfield := 'id_' || tablename;

    codesql := format('select count(*) from %I.%I where %I=''%s'';', schemaname, tablename, idfield, _uuid);
    -- raise notice 'maxdebug2 %', codesql;
    execute (codesql) into resultnumber;

    if(resultnumber = 1) then
      issatified := true;
    end if;
    RETURN issatified;
  end
$fun$;

--drop table "objetmodification";
create table "objetmodification" (
  id_objet uuid not null,
  type_objet regclass not null,
  id_modification uuid not null,
  constraint "objet_modification_pk0" primary key (id_objet, id_modification),
  constraint "objet_modification_fk0" check ("concevoir_pri".foreign_key_on_objet(id_objet, type_objet)),
  constraint "objet_modification_fk1" foreign key (id_modification) references "modification"
);

-- Comme un gabarit, mais exprime un procede comme un statut qui doit change apres CR.
create table "politiqueevolution" (
  id_politiqueevolution uuid not null
  -- contraintes avec verification auto.
  -- contraintes avec verification manuel.
);

