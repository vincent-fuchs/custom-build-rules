alter table T_ALL_AUTHO_REPORTING_PCL add IS_ACTIVITY_ELIGIBLE NUMBER(1,0)
/

comment on column T_ALL_AUTHO_REPORTING_PCL.IS_PILOTAGE_ELIGIBLE is
'Flag to check eligibility of a manual autho for Synchronization with Activity Report'
/