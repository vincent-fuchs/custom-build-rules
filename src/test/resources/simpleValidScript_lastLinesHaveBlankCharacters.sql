INSERT INTO T_AR_DATA_QUALITY_PCL(AR_DATA_QUALITY_ID, DATA_QUALITY_CODE, DATA_QUALITY_LABEL, DATA_QUALITY_DESC)
values(S_AR_DATA_QUALITY_PCL.NEXTVAL, 'CWF_MANUAL_AUTHO_SYNC', 'Synchronized with Manual Authorization',
'AR created and updated automatically from manual authorization')
/

INSERT INTO T_AR_DATA_QUALITY_PCL(AR_DATA_QUALITY_ID, DATA_QUALITY_CODE, DATA_QUALITY_LABEL, DATA_QUALITY_DESC)
values(S_AR_DATA_QUALITY_PCL.NEXTVAL, 'CWF_MANUAL_AUTHO_DESYNC', 'Desynchronized from Manual Authorization',
'AR is not automatically from manual authorization anymore')
/




