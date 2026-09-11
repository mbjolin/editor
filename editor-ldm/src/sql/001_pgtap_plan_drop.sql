

CREATE OR REPLACE FUNCTION public.plan_drop()
 RETURNS text
 LANGUAGE plpgsql
AS $fun$
BEGIN
    DROP TABLE IF EXISTS __tcache__ CASCADE;
    DROP SEQUENCE IF EXISTS __tcache___id_seq CASCADE;
    DROP SEQUENCE IF EXISTS __tresults___numb_seq CASCADE;
    DROP INDEX IF EXISTS __tcache___key CASCADE;
    RETURN 'clean';
END
$fun$;
