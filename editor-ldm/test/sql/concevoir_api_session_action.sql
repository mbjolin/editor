begin transaction;

drop schema if exists test cascade;
create schema test;

select dataset.insert_base();

create or replace function test.create_session_action_test()
returns text
language plpgsql as $fun$
declare
  _actual concevoir_obj.session_action;
  _expected concevoir_obj.session_action;
  id_session uuid;
  id_action uuid;
  _result text;
begin
  -- Given
  id_session := '00000000-0000-0000-0000-100000000000';
  id_action := '00000000-0000-0000-0000-100000000003';
  _expected := (id_session,id_action)::concevoir_obj.session_action;
  _actual := (id_session,id_action)::concevoir_obj.session_action;

  -- When
  _actual := concevoir_api.create_session_action(_actual);

  -- Then
  raise exception query_canceled;
  exception
    when query_canceled then
    select is(_actual::concevoir_obj.session_action, _expected::concevoir_obj.session_action, 'create_session_action') into _result;
    return _result;
end
$fun$;

create or replace function test.read_session_action_test()
returns text
language plpgsql as $fun$
declare
  _actual concevoir_obj.session_action;
  _expected concevoir_obj.session_action;
  id_session uuid;
  id_action uuid;
  _result text;
begin
  -- Given
  id_session := '00000000-0000-0000-0000-100000000000';
  id_action := '00000000-0000-0000-0000-100000000001';
  _expected := (id_session,id_action)::concevoir_obj.session_action;
  _actual := (id_session,id_action)::concevoir_obj.session_action;

  -- When
  _actual := concevoir_api.read_session_action(_actual);

  -- Then
  raise exception query_canceled;
  exception
    when query_canceled then
    select is(_actual::concevoir_obj.session_action, _expected::concevoir_obj.session_action, 'read_session_action') into _result;
    return _result;
end
$fun$;

-- On ne peut tester car contient rien d'autre que la clé primaire.
create or replace function test.update_session_action_test()
returns text
language plpgsql as $fun$
declare
  _actual concevoir_obj.session_action;
  _expected concevoir_obj.session_action;
  _old concevoir_obj.session_action;
  id_session_action uuid;
  id_gabarit uuid;
  _result text;
begin
  -- Given
  id_session_action := '00000000-0000-0000-0000-001000000000';
  id_gabarit := '00000000-0000-0000-0000-000000010000';
  _expected := (id_session_action,'solution', 'changé contenu',id_gabarit)::concevoir_obj.session_action;
  _actual := (id_session_action,'solution', 'changé contenu', id_gabarit)::concevoir_obj.session_action;
  _old := (id_session_action,'problème', 'test contenu', id_gabarit)::concevoir_obj.session_action;

  -- When
  _actual := concevoir_api.update_session_action(_actual,_old);

  -- Then
  raise exception query_canceled;
  exception
    when query_canceled then
    select is(_actual::concevoir_obj.session_action, _expected::concevoir_obj.session_action, 'update_session_action') into _result;
    return _result;
end
$fun$;

-- On ne peut tester car contient rien d'autre que la clé primaire.
create or replace function test.update_session_action_exception_test()
returns text
language plpgsql as $fun$
declare
  _actual concevoir_obj.session_action;
  _expected concevoir_obj.session_action;
  _old concevoir_obj.session_action;
  id_session_action uuid;
  id_gabarit uuid;
  _result text;
  error_msg text;
begin
  -- Given
  id_session_action := '00000000-0000-0000-0000-001000000000';
  id_gabarit := '00000000-0000-0000-0000-000000010000';
  _expected := (id_session_action,'solution', 'changé contenu',id_gabarit)::concevoir_obj.session_action;
  _actual := (id_session_action,'solution', 'changé contenu', id_gabarit)::concevoir_obj.session_action;
  _old := (id_session_action,'problème', 'bad contenu', id_gabarit)::concevoir_obj.session_action;

  -- When
  _actual := concevoir_api.update_session_action(_actual,_old);

  -- Then
  raise exception query_canceled;
  exception
    when query_canceled then
      select fail('Où est l''exception?') into _result;
    --when raise_exception then
    when raise_exception then
      get STACKED diagnostics error_msg = MESSAGE_TEXT;
      if error_msg = 'Objet à mettre à jour a précédemment changé.' then
        select pass('Lance la bonne erreur.') into _result;
      else
        select pass('Mauvais message d''erreur.') into _result;
      end if;
  return _result;
end
$fun$;

create or replace function test.delete_session_action_test()
returns text
language plpgsql as $fun$
declare
  _actual concevoir_obj.session_action;
  id_session uuid;
  id_action uuid;
  _result text;
begin
  -- Given
  id_session := '00000000-0000-0000-0000-100000000000';
  id_action := '00000000-0000-0000-0000-100000000001';
  _actual := (id_session,id_action)::concevoir_obj.session_action;

  -- When
  perform concevoir_api.delete_session_action(_actual);

  -- Then
  raise exception query_canceled;
  exception
    when query_canceled then
    select pass('Suppression sans erreur.') into _result;
    return _result;
end
$fun$;

-- On ne peut tester car contient rien d'autre que la clé primaire.
create or replace function test.delete_session_action_exception_test()
returns text
language plpgsql as $fun$
declare
  _actual concevoir_obj.session_action;

  id_session_action uuid;
  id_gabarit uuid;
  _result text;
  error_msg text;
begin
  -- Given
  id_session_action := '00000000-0000-0000-0000-001000000000';
  id_gabarit := '00000000-0000-0000-0000-000000010000';
  _actual := (id_session_action,'solution', 'changé contenu', id_gabarit)::concevoir_obj.session_action;

  -- When
  perform concevoir_api.delete_session_action(_actual);

  -- Then
  raise exception query_canceled;
  exception
    when query_canceled then
      select fail('Où est l''exception?') into _result;
    --when raise_exception then
    when raise_exception then
      get STACKED diagnostics error_msg = MESSAGE_TEXT;
      if error_msg like 'Objet à supprimer a précédemment changé.%' then
        select pass('Lance la bonne erreur.') into _result;
      else
        select fail('Mauvais message d''erreur.') into _result;
      end if;
  return _result;
end
$fun$;

select plan(6);
select test.create_session_action_test();
select test.read_session_action_test();
--select test.update_session_action_test();
--select test.update_session_action_exception_test();
select test.delete_session_action_test();
--select test.delete_session_action_exception_test();
select finish();

rollback;
