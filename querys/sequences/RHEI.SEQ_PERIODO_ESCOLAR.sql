DROP SEQUENCE RHEI.SEQ_PERIODO_ESCOLAR;

CREATE SEQUENCE RHEI.SEQ_PERIODO_ESCOLAR
  START WITH 62
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  NOCACHE
  NOORDER;


GRANT SELECT ON RHEI.SEQ_PERIODO_ESCOLAR TO ROLRHEI_ANALISTA;
