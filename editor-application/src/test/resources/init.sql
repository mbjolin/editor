
insert into concevoir_pri.utilisateur (id_utilisateur, username, password, role) values ('00000000-0000-0000-0000-000000000001', 'admin', 'mdp', 'admin');
insert into concevoir_pri.utilisateur (id_utilisateur, username, password, role) values ('00000000-0000-0000-0000-000000000002', 'user','mdp', 'user');


insert into concevoir_pri."ddd"(id_ddd, nom, description, categorie)
values ('00000000-0000-0000-0000-000000000100',
        'Dictionnaire de données',
        'provient en grande partie du glossaire',
        'informatique');


--insert into concevoir_pri."ddd_terme" (id_terme, id_ddd)
--  select id_terme, '00000000-0000-0000-0000-000000000100' from concevoir_pri.terme;


-- On va attendre d'avoir un interface pour modifier les langages avant des ajouter.

/*Lorsque les grammaires vont être stabilisées, on pensera à l'interface. */

insert into concevoir_pri."transformation" (
id_transformation, nom, 
code, langage,
parser, lexer, 
entree, sortie)
values (
'00000000-0001-0000-0000-000000000000', 'AsciidocMacro',
null, null,
'in editor-application', 'in editor-application',
'text', 'text');

insert into concevoir_pri."transformation" (
id_transformation, nom, 
code, langage,
parser, lexer, 
entree, sortie)
values (
'00000000-0002-0000-0000-000000000000', 'Asciidoc',
null, null,
'in editor-application', 'in editor-application',
'text', 'modèle');

insert into concevoir_pri."transformation" (
id_transformation, nom, 
code, langage,
parser, lexer, 
entree, sortie)
values (
'00000000-0003-0000-0000-000000000000', 'Identity',
null, null,
'in editor-application', 'in editor-application',
'text', 'text');

insert into concevoir_pri."transformation" (
id_transformation, nom, 
code, langage,
parser, lexer, 
entree, sortie)
values (
'00000000-0006-0000-0000-000000000000', 'ReStructuredText',
null, null,
'in editor-application', 'in editor-application',
'text', 'modèle');

insert into concevoir_pri."transformation" (
id_transformation, nom, 
code, langage,
parser, lexer, 
entree, sortie)
values (
'00000000-0004-0000-0000-000000000000', 'Rule',
null, null,
'in editor-application', 'in editor-application',
'modèle', 'modèle');

insert into concevoir_pri."transformation" (
id_transformation, nom, 
code, langage,
parser, lexer, 
entree, sortie)
values (
'00000000-0005-0000-0000-000000000000', 'AdocPresentation',
'in editor-application', 'in editor-application',
null, null,
'modèle', 'text');


insert into concevoir_pri."projet" (
id_projet, id_ddd, 
nom, description)
values (
'projectDefaultTitle', '00000000-0000-0000-0000-000000000100', 
'Un projet quelconque', 'un test');

insert into concevoir_pri."projet" (
id_projet, id_ddd, 
nom, description)
values (
'projetdescription1', '00000000-0000-0000-0000-000000000100', 
'Un projet quelconque', 'un test');

insert into concevoir_pri."projet" (
id_projet, id_ddd, 
nom, description)
values (
'projetdescription2', '00000000-0000-0000-0000-000000000100', 
'Un projet quelconque', 'un test');



insert into concevoir_pri."session" (id_session, id_utilisateur, moment_debut, moment_execution, moment_fin)
values ('00000000-0000-0000-0000-100000000000', '00000000-0000-0000-0000-000000000001', '2024-11-11 19:49:11.925902', '2024-11-11 19:49:11.925902', '2024-11-11 19:49:11.925902');

insert into concevoir_pri."action" (id_action, fonction, argument, typecomposite)
values ('00000000-0000-0000-0000-100000000001', 'create_document', '(gen_random_uuid(),''solution'', ''changé contenu'',''00000000-0000-0000-0000-000000010000'')', 'document');

insert into concevoir_pri."action" (id_action, fonction, 
argument, typecomposite)
values ('00000000-0000-0000-0000-100000000002', 'create_utilisateur', 
'(''30000000-0000-0000-0000-000000000000'', ''user'',''mdp'', ''user'')', 'utilisateur');

insert into concevoir_pri."action" (id_action, fonction, argument, typecomposite)
values ('00000000-0000-0000-0000-100000000003', 'delete_utilisateur', '(''30000000-0000-0000-0000-000000000000'', ''user'',''mdp'', ''user'')', 'utilisateur');

insert into concevoir_pri."session_action" (id_session, id_action)
values ('00000000-0000-0000-0000-100000000000', '00000000-0000-0000-0000-100000000001');

insert into concevoir_pri."session_action" (id_session, id_action)
values ('00000000-0000-0000-0000-100000000000', '00000000-0000-0000-0000-100000000002');


insert into document_pri."document" (
id_document, id_projet)
values (
'iddocument1', 'projectDefaultTitle');

insert into document_pri."meta" (id_meta, etiquette, valeur)
values ('idmeta1', 'etiquette1', 'valeur1');

insert into document_pri."document_meta" (id_document, id_meta)
values ('iddocument1', 'idmeta1');

insert into document_pri."section" (id_section, etiquette) 
values ('idsection1','etiquette1');

insert into document_pri."section" (id_section, etiquette) 
values ('idsection1.1','etiquette2');

insert into document_pri."document_section" (id_document, id_section)
values ('iddocument1', 'idsection1');

insert into document_pri."section_section" (id_section, id_enfant)
values ('idsection1', 'idsection1.1');

insert into document_pri."paragraphe" (id_paragraphe)
values ('idparagraphe1');

insert into document_pri."section_paragraphe" (id_section, id_paragraphe)
values ('idsection1', 'idparagraphe1');

insert into document_pri."citation" (id_citation, description)
values ('idcitation1', 'description1');

insert into document_pri."paragraphe_citation" (id_paragraphe, id_citation)
values ('idparagraphe1', 'idcitation1');

insert into document_pri."contenu" (id_contenu, description)
values ('idcontenu1', 'description2');

insert into document_pri."paragraphe_contenu" (id_paragraphe, id_contenu)
values ('idparagraphe1', 'idcontenu1');

insert into document_pri."reglelogique" (id_reglelogique, description_formelle, description_naturelle)
values ('idreglelogique1', 'description_formelle', 'description_naturelle');

insert into document_pri."paragraphe_reglelogique" (id_paragraphe, id_reglelogique)
values ('idparagraphe1', 'idreglelogique1');

insert into document_pri."source" (id_source, description)
values ('idsource1', 'description_source');

insert into document_pri."reglelogique_source" (id_reglelogique, id_source)
values ('idreglelogique1', 'idsource1');

