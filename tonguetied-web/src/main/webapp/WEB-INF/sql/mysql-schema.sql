
    alter table authorities 
        drop foreign key fk_internal_user_authorities;

    alter table translation 
        drop foreign key fk_keyword_translation;

    alter table translation 
        drop foreign key fk_bundle_translation;

    alter table translation 
        drop foreign key fk_country_translation;

    alter table translation 
        drop foreign key fk_language_translation;

    drop table if exists audit_log_record;

    drop table if exists authorities;

    drop table if exists bundle;

    drop table if exists country;

    drop table if exists internal_user;

    drop table if exists keyword;

    drop table if exists language;

    drop table if exists server_data;

    drop table if exists translation;

  

    create table audit_log_record (
        audit_log_record_id int8 not null auto_increment primary key,
        created timestamp not null,
        entity_class varchar(255) not null,
        entity_id int8 not null,
        message varchar(7) not null,
        new_value text,
        old_value text,
        username varchar(255)
    );

    create table authorities (
        user_id int8 not null,
        permission varchar(13) not null,
        primary key (user_id, permission)
    );

    create table bundle (
        bundle_id int8 not null auto_increment primary key,
        description varchar(255),
        is_default bool not null,
        is_global bool not null,
        name varchar(255) not null unique,
        resource_name varchar(255) not null unique
    );

    create table country (
        country_id int8 not null auto_increment primary key,
        code varchar(7) not null unique,
        name varchar(255) not null
    );

    create table internal_user (
        internal_user_id int8 not null auto_increment primary key,
        account_non_expired bool not null,
        account_non_locked bool not null,
        credentials_non_expired bool not null,
        email varchar(255) not null,
        enabled bool not null,
        first_name varchar(255) not null,
        last_name varchar(255) not null,
        password varchar(255) not null,
        username varchar(255) not null unique,
        optlock int4
    );

    create table keyword (
        keyword_id int8 not null auto_increment primary key,
        context text,
        keyword varchar(255) not null unique,
        optlock int4
    );

    create table language (
        language_id int8 not null auto_increment primary key,
        code varchar(7) not null unique,
        name varchar(255) not null
    );

    create table server_data (
        server_data_id int8 not null auto_increment primary key,
        build_date timestamp not null,
        build_number varchar(10) not null,
        setup_date timestamp not null,
        version varchar(10) not null,
        unique (version, build_number)
    );

    create table translation (
        translation_id int8 not null auto_increment primary key,
        state varchar(10) not null,
        value text,
        optlock int4,
        bundle_id int8,
        country_id int8,
        keyword_id int8,
        language_id int8,
        unique (keyword_id, language_id, country_id, bundle_id)
    );

    alter table authorities 
        add constraint fk_internal_user_authorities 
        foreign key (user_id)
        references internal_user (internal_user_id);

    alter table translation 
        add constraint fk_keyword_translation 
        foreign key (keyword_id) 
        references keyword (keyword_id);

    alter table translation 
        add constraint fk_bundle_translation 
        foreign key (bundle_id) 
        references bundle (bundle_id);

    alter table translation 
        add constraint fk_country_translation 
        foreign key (country_id) 
        references country (country_id);

    alter table translation 
        add constraint fk_language_translation 
        foreign key (language_id) 
        references language (language_id);
