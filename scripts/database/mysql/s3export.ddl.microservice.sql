#
# TABLE STRUCTURE FOR: auftraege
#

DROP TABLE IF EXISTS `S3EXPORT_AUFTRAEGE_DB`.`auftraege`;

CREATE TABLE `S3EXPORT_AUFTRAEGE_DB`.`auftraege` (
  `auftragid` varchar(255) NOT NULL,
  `artikelnummer` varchar(255) NOT NULL,
  `created` varchar(255) NOT NULL,
  `lastchange` varchar(255) NOT NULL,
  `kundeid` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# TABLE STRUCTURE FOR: dummy
#

DROP TABLE IF EXISTS `S3EXPORT_AUFTRAEGE_DB`.`dummy`;

CREATE TABLE `S3EXPORT_AUFTRAEGE_DB`.`dummy` (
  `auftragid` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `aritkelnummer` varchar(255) NOT NULL,
  `created` varchar(255) NOT NULL,
  `lastchange` varchar(255) NOT NULL,
  `kundeid` bigint(20) unsigned NOT NULL,
  KEY `auftragid` (`auftragid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# TABLE STRUCTURE FOR: kunde
#

DROP TABLE IF EXISTS `S3EXPORT_KUNDE_DB`.`kunde`;

CREATE TABLE `S3EXPORT_KUNDE_DB`.`kunde` (
  `kundenid` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `vorname` varchar(100) NOT NULL,
  `nachname` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `strasse` varchar(255) NOT NULL,
  `strassenzusatz` varchar(255) NOT NULL,
  `ort` varchar(255) NOT NULL,
  `land` varchar(255) NOT NULL,
  `plz` varchar(255) NOT NULL,
  `firmenname` varchar(100) NOT NULL,
  KEY `kundenid` (`kundenid`),
  KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8;

#
# TABLE STRUCTURE FOR: spring batch framework on SEED
#

DROP TABLE IF EXISTS `S3EXPORT_SEED_DB`.`BATCH_JOB_INSTANCE`;

CREATE TABLE `S3EXPORT_SEED_DB`.`BATCH_JOB_INSTANCE`  (
    JOB_INSTANCE_ID BIGINT  NOT NULL PRIMARY KEY ,
    VERSION BIGINT ,
    JOB_NAME VARCHAR(100) NOT NULL,
    JOB_KEY VARCHAR(32) NOT NULL,
    constraint JOB_INST_UN unique (JOB_NAME, JOB_KEY)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `S3EXPORT_SEED_DB`.`BATCH_JOB_EXECUTION`;

CREATE TABLE `S3EXPORT_SEED_DB`.`BATCH_JOB_EXECUTION`  (
    JOB_EXECUTION_ID BIGINT  NOT NULL PRIMARY KEY ,
    VERSION BIGINT  ,
    JOB_INSTANCE_ID BIGINT NOT NULL,
    CREATE_TIME DATETIME NOT NULL,
    START_TIME DATETIME DEFAULT NULL ,
    END_TIME DATETIME DEFAULT NULL ,
    STATUS VARCHAR(10) ,
    EXIT_CODE VARCHAR(2500) ,
    EXIT_MESSAGE VARCHAR(2500) ,
    LAST_UPDATED DATETIME,
    JOB_CONFIGURATION_LOCATION VARCHAR(2500) NULL,
    constraint JOB_INST_EXEC_FK foreign key (JOB_INSTANCE_ID)
    references BATCH_JOB_INSTANCE(JOB_INSTANCE_ID)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `S3EXPORT_SEED_DB`.`BATCH_JOB_EXECUTION_PARAMS`;

CREATE TABLE `S3EXPORT_SEED_DB`.`BATCH_JOB_EXECUTION_PARAMS`  (
    JOB_EXECUTION_ID BIGINT NOT NULL ,
    TYPE_CD VARCHAR(6) NOT NULL ,
    KEY_NAME VARCHAR(100) NOT NULL ,
    STRING_VAL VARCHAR(250) ,
    DATE_VAL DATETIME DEFAULT NULL ,
    LONG_VAL BIGINT ,
    DOUBLE_VAL DOUBLE PRECISION ,
    IDENTIFYING CHAR(1) NOT NULL ,
    constraint JOB_EXEC_PARAMS_FK foreign key (JOB_EXECUTION_ID)
    references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `S3EXPORT_SEED_DB`.`BATCH_STEP_EXECUTION`;

CREATE TABLE `S3EXPORT_SEED_DB`.`BATCH_STEP_EXECUTION`  (
    STEP_EXECUTION_ID BIGINT  NOT NULL PRIMARY KEY ,
    VERSION BIGINT NOT NULL,
    STEP_NAME VARCHAR(100) NOT NULL,
    JOB_EXECUTION_ID BIGINT NOT NULL,
    START_TIME DATETIME NOT NULL ,
    END_TIME DATETIME DEFAULT NULL ,
    STATUS VARCHAR(10) ,
    COMMIT_COUNT BIGINT ,
    READ_COUNT BIGINT ,
    FILTER_COUNT BIGINT ,
    WRITE_COUNT BIGINT ,
    READ_SKIP_COUNT BIGINT ,
    WRITE_SKIP_COUNT BIGINT ,
    PROCESS_SKIP_COUNT BIGINT ,
    ROLLBACK_COUNT BIGINT ,
    EXIT_CODE VARCHAR(2500) ,
    EXIT_MESSAGE VARCHAR(2500) ,
    LAST_UPDATED DATETIME,
    constraint JOB_EXEC_STEP_FK foreign key (JOB_EXECUTION_ID)
    references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `S3EXPORT_SEED_DB`.`BATCH_STEP_EXECUTION_CONTEXT`;

CREATE TABLE `S3EXPORT_SEED_DB`.`BATCH_STEP_EXECUTION_CONTEXT` (
    STEP_EXECUTION_ID BIGINT NOT NULL PRIMARY KEY,
    SHORT_CONTEXT VARCHAR(2500) NOT NULL,
    SERIALIZED_CONTEXT TEXT ,
    constraint STEP_EXEC_CTX_FK foreign key (STEP_EXECUTION_ID)
    references BATCH_STEP_EXECUTION(STEP_EXECUTION_ID)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `S3EXPORT_SEED_DB`.`BATCH_JOB_EXECUTION_CONTEXT`;

CREATE TABLE `S3EXPORT_SEED_DB`.`BATCH_JOB_EXECUTION_CONTEXT`  (
    JOB_EXECUTION_ID BIGINT NOT NULL PRIMARY KEY,
    SHORT_CONTEXT VARCHAR(2500) NOT NULL,
    SERIALIZED_CONTEXT TEXT ,
    constraint JOB_EXEC_CTX_FK foreign key (JOB_EXECUTION_ID)
    references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `S3EXPORT_SEED_DB`.`BATCH_STEP_EXECUTION_SEQ`;

CREATE TABLE `S3EXPORT_SEED_DB`.`BATCH_STEP_EXECUTION_SEQ` (
    ID BIGINT NOT NULL,
    UNIQUE_KEY CHAR(1) NOT NULL,
    constraint UNIQUE_KEY_UN unique (UNIQUE_KEY)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `S3EXPORT_SEED_DB`.`BATCH_JOB_EXECUTION_SEQ`;

CREATE TABLE `S3EXPORT_SEED_DB`.`BATCH_JOB_EXECUTION_SEQ` (
    ID BIGINT NOT NULL,
    UNIQUE_KEY CHAR(1) NOT NULL,
    constraint UNIQUE_KEY_UN unique (UNIQUE_KEY)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `S3EXPORT_SEED_DB`.`BATCH_JOB_SEQ`;

CREATE TABLE `S3EXPORT_SEED_DB`.`BATCH_JOB_SEQ` (
    ID BIGINT NOT NULL,
    UNIQUE_KEY CHAR(1) NOT NULL,
    constraint UNIQUE_KEY_UN unique (UNIQUE_KEY)
) ENGINE=InnoDB;

#
# TABLE STRUCTURE FOR: spring batch framework on SYNC
#

DROP TABLE IF EXISTS `S3EXPORT_SYNC_DB`.`BATCH_JOB_INSTANCE`;

CREATE TABLE `S3EXPORT_SYNC_DB`.`BATCH_JOB_INSTANCE`  (
    JOB_INSTANCE_ID BIGINT  NOT NULL PRIMARY KEY ,
    VERSION BIGINT ,
    JOB_NAME VARCHAR(100) NOT NULL,
    JOB_KEY VARCHAR(32) NOT NULL,
    constraint JOB_INST_UN unique (JOB_NAME, JOB_KEY)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `S3EXPORT_SYNC_DB`.`BATCH_JOB_EXECUTION`;

CREATE TABLE `S3EXPORT_SYNC_DB`.`BATCH_JOB_EXECUTION`  (
    JOB_EXECUTION_ID BIGINT  NOT NULL PRIMARY KEY ,
    VERSION BIGINT  ,
    JOB_INSTANCE_ID BIGINT NOT NULL,
    CREATE_TIME DATETIME NOT NULL,
    START_TIME DATETIME DEFAULT NULL ,
    END_TIME DATETIME DEFAULT NULL ,
    STATUS VARCHAR(10) ,
    EXIT_CODE VARCHAR(2500) ,
    EXIT_MESSAGE VARCHAR(2500) ,
    LAST_UPDATED DATETIME,
    JOB_CONFIGURATION_LOCATION VARCHAR(2500) NULL,
    constraint JOB_INST_EXEC_FK foreign key (JOB_INSTANCE_ID)
    references BATCH_JOB_INSTANCE(JOB_INSTANCE_ID)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `S3EXPORT_SYNC_DB`.`BATCH_JOB_EXECUTION_PARAMS`;

CREATE TABLE `S3EXPORT_SYNC_DB`.`BATCH_JOB_EXECUTION_PARAMS`  (
    JOB_EXECUTION_ID BIGINT NOT NULL ,
    TYPE_CD VARCHAR(6) NOT NULL ,
    KEY_NAME VARCHAR(100) NOT NULL ,
    STRING_VAL VARCHAR(250) ,
    DATE_VAL DATETIME DEFAULT NULL ,
    LONG_VAL BIGINT ,
    DOUBLE_VAL DOUBLE PRECISION ,
    IDENTIFYING CHAR(1) NOT NULL ,
    constraint JOB_EXEC_PARAMS_FK foreign key (JOB_EXECUTION_ID)
    references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `S3EXPORT_SYNC_DB`.`BATCH_STEP_EXECUTION`;

CREATE TABLE `S3EXPORT_SYNC_DB`.`BATCH_STEP_EXECUTION`  (
    STEP_EXECUTION_ID BIGINT  NOT NULL PRIMARY KEY ,
    VERSION BIGINT NOT NULL,
    STEP_NAME VARCHAR(100) NOT NULL,
    JOB_EXECUTION_ID BIGINT NOT NULL,
    START_TIME DATETIME NOT NULL ,
    END_TIME DATETIME DEFAULT NULL ,
    STATUS VARCHAR(10) ,
    COMMIT_COUNT BIGINT ,
    READ_COUNT BIGINT ,
    FILTER_COUNT BIGINT ,
    WRITE_COUNT BIGINT ,
    READ_SKIP_COUNT BIGINT ,
    WRITE_SKIP_COUNT BIGINT ,
    PROCESS_SKIP_COUNT BIGINT ,
    ROLLBACK_COUNT BIGINT ,
    EXIT_CODE VARCHAR(2500) ,
    EXIT_MESSAGE VARCHAR(2500) ,
    LAST_UPDATED DATETIME,
    constraint JOB_EXEC_STEP_FK foreign key (JOB_EXECUTION_ID)
    references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `S3EXPORT_SYNC_DB`.`BATCH_STEP_EXECUTION_CONTEXT`;

CREATE TABLE `S3EXPORT_SYNC_DB`.`BATCH_STEP_EXECUTION_CONTEXT` (
    STEP_EXECUTION_ID BIGINT NOT NULL PRIMARY KEY,
    SHORT_CONTEXT VARCHAR(2500) NOT NULL,
    SERIALIZED_CONTEXT TEXT ,
    constraint STEP_EXEC_CTX_FK foreign key (STEP_EXECUTION_ID)
    references BATCH_STEP_EXECUTION(STEP_EXECUTION_ID)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `S3EXPORT_SYNC_DB`.`BATCH_JOB_EXECUTION_CONTEXT`;

CREATE TABLE `S3EXPORT_SYNC_DB`.`BATCH_JOB_EXECUTION_CONTEXT`  (
    JOB_EXECUTION_ID BIGINT NOT NULL PRIMARY KEY,
    SHORT_CONTEXT VARCHAR(2500) NOT NULL,
    SERIALIZED_CONTEXT TEXT ,
    constraint JOB_EXEC_CTX_FK foreign key (JOB_EXECUTION_ID)
    references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `S3EXPORT_SYNC_DB`.`BATCH_STEP_EXECUTION_SEQ`;

CREATE TABLE `S3EXPORT_SYNC_DB`.`BATCH_STEP_EXECUTION_SEQ` (
    ID BIGINT NOT NULL,
    UNIQUE_KEY CHAR(1) NOT NULL,
    constraint UNIQUE_KEY_UN unique (UNIQUE_KEY)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `S3EXPORT_SYNC_DB`.`BATCH_JOB_EXECUTION_SEQ`;

CREATE TABLE `S3EXPORT_SYNC_DB`.`BATCH_JOB_EXECUTION_SEQ` (
    ID BIGINT NOT NULL,
    UNIQUE_KEY CHAR(1) NOT NULL,
    constraint UNIQUE_KEY_UN unique (UNIQUE_KEY)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `S3EXPORT_SYNC_DB`.`BATCH_JOB_SEQ`;

CREATE TABLE `S3EXPORT_SYNC_DB`.`BATCH_JOB_SEQ` (
    ID BIGINT NOT NULL,
    UNIQUE_KEY CHAR(1) NOT NULL,
    constraint UNIQUE_KEY_UN unique (UNIQUE_KEY)
) ENGINE=InnoDB;

#
# TABLE STRUCTURE FOR; download and accesibility
#

DROP TABLE IF EXISTS `S3EXPORT_DOWNLOAD_DB`.`audit_event`;

CREATE TABLE `S3EXPORT_DOWNLOAD_DB`.`audit_event` (
    id int auto_increment,
    module varchar(100) not null,
    action varchar(100) not null,
    description varchar(255) not null,
    input varchar(255),
    output varchar(255),
    created_on timestamp default current_timestamp,
    created_by int not null,
    modified_on timestamp default current_timestamp,
    modified_by int,
    active_sw bigint default 1,
    version int default 0,
    constraint pk_audit_event primary key (id),
    index idx_audit_event_module (module),
    index idx_audit_event_action (action)
) ENGINE=InnoDB;