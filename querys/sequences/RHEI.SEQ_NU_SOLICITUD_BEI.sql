DROP SEQUENCE RHEI.SEQ_NU_SOLICITUD_BEI;

CREATE SEQUENCE RHEI.SEQ_NU_SOLICITUD_BEI
  START WITH 837
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  NOCACHE
  NOORDER;


GRANT SELECT ON RHEI.SEQ_NU_SOLICITUD_BEI TO ROLRHEI_ANALISTA;