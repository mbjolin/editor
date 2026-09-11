
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
