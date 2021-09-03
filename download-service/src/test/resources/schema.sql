create table if not exists audit_event (
    id identity not null,
    module varchar(100) not null,
    action varchar(100) not null,
    description varchar(255) not null,
    input varchar(255),
    output varchar(255),
    created_on datetime default current_timestamp,
    created_by int default -1,
    modified_on datetime default current_timestamp,
    modified_by int,
    active_sw boolean default true,
    version int default 0,
    constraint pk_audit_event primary key (id)
);
create index if not exists idx_audit_event_module on audit_event(module);
create index if not exists idx_audit_event_action on audit_event(action);