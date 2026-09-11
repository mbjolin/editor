begin transaction;

drop schema if exists test cascade;
create schema test;

select dataset.insert_base();

create or replace function test.create_document_test()
returns text
language plpgsql as $fun$
declare
  _actual concevoir_obj.document;
  _expected concevoir_obj.document;
  id_document uuid;
  id_gabarit uuid;
  _result text;
begin
  -- Given
  id_document := gen_random_uuid();
  id_gabarit := '00000000-0000-0000-0000-000000010000';
  _expected := (id_document,'solution','contenu',id_gabarit)::concevoir_obj.document;
  _actual := (id_document,'solution','contenu',id_gabarit)::concevoir_obj.document;

  -- When
  _actual := concevoir_api.create_document(_actual);

  -- Then
  raise exception query_canceled;
  exception
    when query_canceled then
    select is(_actual::concevoir_obj.document, _expected::concevoir_obj.document, 'create_document') into _result;
    return _result;
end
$fun$;

create or replace function test.read_document_test()
returns text
language plpgsql as $fun$
declare
  _actual concevoir_obj.document;
  _expected concevoir_obj.document;
  id_document uuid;
  id_gabarit uuid;
  _result text;
begin
  -- Given
  id_document := '00000000-0000-0000-0000-001000000000';
  id_gabarit := '00000000-0000-0000-0000-000000010000';
  _expected := (id_document,'problème', 'test contenu',id_gabarit)::concevoir_obj.document;
  _actual := (id_document,'solution', '', id_gabarit)::concevoir_obj.document;

  -- When
  _actual := concevoir_api.read_document(_actual);

  -- Then
  raise exception query_canceled;
  exception
    when query_canceled then
    select is(_actual::concevoir_obj.document, _expected::concevoir_obj.document, 'read_document') into _result;
    return _result;
end
$fun$;

create or replace function test.update_document_test()
returns text
language plpgsql as $fun$
declare
  _actual concevoir_obj.document;
  _expected concevoir_obj.document;
  _old concevoir_obj.document;
  id_document uuid;
  id_gabarit uuid;
  _result text;
begin
  -- Given
  id_document := '00000000-0000-0000-0000-001000000000';
  id_gabarit := '00000000-0000-0000-0000-000000010000';
  _expected := (id_document,'solution', 'changé contenu',id_gabarit)::concevoir_obj.document;
  _actual := (id_document,'solution', 'changé contenu', id_gabarit)::concevoir_obj.document;
  _old := (id_document,'problème', 'test contenu', id_gabarit)::concevoir_obj.document;

  -- When
  _actual := concevoir_api.update_document(_actual,_old);

  -- Then
  raise exception query_canceled;
  exception
    when query_canceled then
    select is(_actual::concevoir_obj.document, _expected::concevoir_obj.document, 'update_document') into _result;
    return _result;
end
$fun$;

create or replace function test.update_document_exception_test()
returns text
language plpgsql as $fun$
declare
  _actual concevoir_obj.document;
  _expected concevoir_obj.document;
  _old concevoir_obj.document;
  id_document uuid;
  id_gabarit uuid;
  _result text;
  error_msg text;
begin
  -- Given
  id_document := '00000000-0000-0000-0000-001000000000';
  id_gabarit := '00000000-0000-0000-0000-000000010000';
  _expected := (id_document,'solution', 'changé contenu',id_gabarit)::concevoir_obj.document;
  _actual := (id_document,'solution', 'changé contenu', id_gabarit)::concevoir_obj.document;
  _old := (id_document,'problème', 'bad contenu', id_gabarit)::concevoir_obj.document;

  -- When
  _actual := concevoir_api.update_document(_actual,_old);

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

create or replace function test.delete_document_test()
returns text
language plpgsql as $fun$
declare
  _actual concevoir_obj.document;
  id_document uuid;
  id_gabarit uuid;
  _result text;
begin
  -- Given
  id_document := '00000000-0000-0000-0000-001000000000';
  id_gabarit := '00000000-0000-0000-0000-000000010000';
  _actual := (id_document,'problème', 'test contenu', id_gabarit)::concevoir_obj.document;

  -- When
  perform concevoir_api.delete_document(_actual);

  -- Then
  raise exception query_canceled;
  exception
    when query_canceled then
    select pass('Suppression sans erreur.') into _result;
    return _result;
end
$fun$;

create or replace function test.delete_document_exception_test()
returns text
language plpgsql as $fun$
declare
  _actual concevoir_obj.document;

  id_document uuid;
  id_gabarit uuid;
  _result text;
  error_msg text;
begin
  -- Given
  id_document := '00000000-0000-0000-0000-001000000000';
  id_gabarit := '00000000-0000-0000-0000-000000010000';
  _actual := (id_document,'solution', 'changé contenu', id_gabarit)::concevoir_obj.document;

  -- When
  perform concevoir_api.delete_document(_actual);

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
select test.create_document_test();
select test.read_document_test();
select test.update_document_test();
select test.update_document_exception_test();
select test.delete_document_test();
select test.delete_document_exception_test();
select finish();

rollback;
