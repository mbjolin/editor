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