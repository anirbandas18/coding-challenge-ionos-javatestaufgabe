select * from s3export_sync_db.kunde;
-- 1990-09-05T03:45:12+0000
SELECT @@global.time_zone, @@session.time_zone;
select lastchange from s3export_sync_db.auftraege ;
select str_to_date(lastchange, '%Y-%m-%dT%H:%i:%s+0000'), lastchange from s3export_sync_db.auftraege ;

select * from kunde;

select * from S3EXPORT_AUFTRAEGE_DB.auftraege order by created desc;
select * from S3EXPORT_KUNDE_DB.kunde;
select * from S3EXPORT_DOWNLOAD_DB.audit_event;

show databases like 'S3EXPORT%';

select kundeid, 
TIMESTAMPDIFF(hour, utc_timestamp, str_to_date(lastchange, '%Y-%m-%dT%H:%i:%s+0000')) as diff_utc, 
TIMESTAMPDIFF(hour, current_timestamp(), str_to_date(lastchange, '%Y-%m-%dT%H:%i:%s+0000')) as diff_server,
lastchange,
convert_tz(str_to_date(lastchange, '%Y-%m-%dT%H:%i:%s+0000'), '+00:00', @@session.time_zone),
TIMESTAMPDIFF(hour, current_timestamp(), convert_tz(str_to_date(lastchange, '%Y-%m-%dT%H:%i:%s+0000'), '+00:00', @@session.time_zone)) as diff_server
from s3export_sync_db.auftraege where TIMESTAMPDIFF(hour, utc_timestamp, str_to_date(lastchange, '%Y-%m-%dT%H:%i:%s+0000')) <= 99;

select * from s3export_sync_db.auftraege where TIMESTAMPDIFF(hour, utc_timestamp, str_to_date(lastchange, '%Y-%m-%dT%H:%i:%s+0000')) <= 99;
select * from s3export_sync_db.auftraege where TIMESTAMPDIFF(minute, utc_timestamp, str_to_date(lastchange, '%Y-%m-%dT%H:%i:%s+0000')) <= 99;


select * from s3export_sync_db.auftraege where TIMESTAMPDIFF(hour, current_timestamp(), str_to_date(lastchange, '%Y-%M-%DT%H:%i:%S')) <= 3;