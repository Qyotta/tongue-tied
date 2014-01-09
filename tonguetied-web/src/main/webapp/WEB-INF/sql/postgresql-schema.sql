
    alter table authorities 
        drop constraint fk_internal_user_authorities;

    alter table translation 
        drop constraint fk_keyword_translation;

    alter table translation 
        drop constraint fk_bundle_translation;

    alter table translation 
        drop constraint fk_country_translation;

    alter table translation 
        drop constraint fk_language_translation;

    drop table audit_log_record;

    drop table authorities;

    drop table bundle;

    drop table country;

    drop table internal_user;

    drop table keyword;

    drop table language;

    drop table server_data;

    drop table translation;

    drop sequence audit_log_record_id_seq;

    drop sequence bundle_id_seq;

    drop sequence country_id_seq;

    drop sequence internal_user_id_seq;

    drop sequence keyword_id_seq;

    drop sequence language_id_seq;

    drop sequence server_data_id_seq;

    drop sequence translation_id_seq;

    create sequence audit_log_record_id_seq;

    create sequence bundle_id_seq;

    create sequence country_id_seq;

    create sequence internal_user_id_seq;

    create sequence keyword_id_seq;

    create sequence language_id_seq;

    create sequence server_data_id_seq;

    create sequence translation_id_seq;

    create table audit_log_record (
        audit_log_record_id int8 not null default nextval('audit_log_record_id_seq'),
        created timestamp not null,
        entity_class varchar(255) not null,
        entity_id int8 not null,
        message varchar(7) not null,
        new_value text,
        old_value text,
        username varchar(255),
        primary key (audit_log_record_id)
    );

    create table authorities (
        user_id int8 not null,
        permission varchar(13) not null,
        primary key (user_id, permission)
    );

    create table bundle (
        bundle_id int8 not null default nextval('bundle_id_seq'),
        description varchar(255),
        is_default bool not null,
        is_global bool not null,
        name varchar(255) not null unique,
        resource_name varchar(255) not null unique,
        primary key (bundle_id)
    );

    create table country (
        country_id int8 not null default nextval('country_id_seq'),
        code varchar(7) not null unique,
        name varchar(255) not null,
        primary key (country_id)
    );

    create table internal_user (
        internal_user_id int8 not null default nextval('internal_user_id_seq'),
        account_non_expired bool not null,
        account_non_locked bool not null,
        credentials_non_expired bool not null,
        email varchar(255) not null,
        enabled bool not null,
        first_name varchar(255) not null,
        last_name varchar(255) not null,
        password varchar(255) not null,
        username varchar(255) not null unique,
        optlock int4,
        primary key (internal_user_id)
    );

    create table keyword (
        keyword_id int8 not null default nextval('keyword_id_seq'),
        context text,
        keyword varchar(255) not null unique,
        optlock int4,
        primary key (keyword_id)
    );

    create table language (
        language_id int8 not null default nextval('language_id_seq'),
        code varchar(7) not null unique,
        name varchar(255) not null,
        primary key (language_id)
    );

    create table server_data (
        server_data_id int8 not null default nextval('server_data_id_seq'),
        build_date timestamp not null,
        build_number varchar(10) not null,
        setup_date timestamp not null,
        version varchar(10) not null,
        primary key (server_data_id),
        unique (version, build_number)
    );

    create table translation (
        translation_id int8 not null default nextval('translation_id_seq'),
        state varchar(10) not null,
        value text,
        optlock int4,
        bundle_id int8,
        country_id int8,
        keyword_id int8,
        language_id int8,
        primary key (translation_id),
        unique (keyword_id, language_id, country_id, bundle_id)
    );

    alter table authorities 
        add constraint fk_internal_user_authorities 
        foreign key (user_id) 
        references internal_user;

    alter table translation 
        add constraint fk_keyword_translation 
        foreign key (keyword_id) 
        references keyword;

    alter table translation 
        add constraint fk_bundle_translation 
        foreign key (bundle_id) 
        references bundle;

    alter table translation 
        add constraint fk_country_translation 
        foreign key (country_id) 
        references country;

    alter table translation 
        add constraint fk_language_translation 
        foreign key (language_id) 
        references language;

    alter sequence audit_log_record_id_seq owned by audit_log_record.audit_log_record_id;

    alter sequence bundle_id_seq owned by bundle.bundle_id;

    alter sequence country_id_seq owned by country.country_id;

    alter sequence internal_user_id_seq owned by internal_user.internal_user_id;

    alter sequence keyword_id_seq owned by keyword.keyword_id;

    alter sequence language_id_seq owned by language.language_id;

    alter sequence server_data_id_seq owned by server_data.server_data_id;

    alter sequence translation_id_seq owned by translation.translation_id;
