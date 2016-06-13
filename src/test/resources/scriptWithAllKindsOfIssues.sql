DELETE FROM FEATURES where feature_name='someFeatureName1'
/

-- skipMavenCheck
CREATE TABLE IF NOT EXISTS "TABLE_WITH_INCORRECT_COMMENT_BUT_SKIPPED"  ("SOME_FIELD"    VARCHAR2(256 BYTE));
comment on table TABLE_OK is 'some comment not matching configured regexp';
/

CREATE TABLE IF NOT EXISTS "TABLE_WITH_INCORRECT_COMMENT_NOT_SKIPPED"  ("SOME_FIELD"    VARCHAR2(256 BYTE));
comment on table TABLE_WITH_INCORRECT_COMMENT_NOT_SKIPPED is 'some comment not matching configured regexp';
/


