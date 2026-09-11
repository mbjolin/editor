
insert into document_pri."document" (
id_document, id_projet)
values (
'iddocument1', 'idprojet1');

insert into document_pri."meta" (id_meta, valeur)
values ('idmeta1', 'valeur1');

insert into document_pri."document_meta" (id_document, id_meta)
values ('iddocument1', 'idmeta1');

insert into document_pri."section" (id_section) 
values ('idsection1');

insert into document_pri."section" (id_section) 
values ('idsection1.1');

insert into document_pri."document_section" (id_document, id_section)
values ('iddocument1', 'idsection1');

insert into document_pri."section_section" (id_section, id_section_enfant)
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

