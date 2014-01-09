
    alter table Translation 
        drop 
        foreign key FKF5B39A9121237D2C;

    alter table Translation 
        drop 
        foreign key FKF5B39A91B2B40028;

    alter table Translation 
        drop 
        foreign key FKF5B39A91401B820C;

    alter table Translation 
        drop 
        foreign key FKF5B39A918B3B54E8;

    alter table authorities 
        drop 
        foreign key FK2B0F13219D90DDE5;

rename table authorities to tmp_authorities;
rename table AuditLogRecord to tmp_AuditLogRecord;
rename table Bundle to tmp_Bundle;
rename table Country to tmp_Country;
rename table Keyword to tmp_Keyword;
rename table Language to tmp_Language;
rename table ServerData to tmp_ServerData;
rename table Translation to tmp_Translation;
rename table User to tmp_User;

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
        audit_log_record_id bigint not null auto_increment,
        created datetime not null,
        entity_class varchar(255) not null,
        entity_id bigint not null,
        message varchar(7) not null,
        new_value longtext,
        old_value longtext,
        username varchar(255),
        primary key (audit_log_record_id)
    ) type=InnoDB;

    create table authorities (
        user_id bigint not null,
        permission varchar(13) not null,
        primary key (user_id, permission)
    ) type=InnoDB;

    create table bundle (
        bundle_id bigint not null auto_increment,
        description varchar(255),
        is_default bit not null,
        is_global bit not null,
        name varchar(255) not null unique,
        resource_name varchar(255) not null unique,
        primary key (bundle_id)
    ) type=InnoDB;

    create table country (
        country_id bigint not null auto_increment,
        code varchar(7) not null unique,
        name varchar(255) not null,
        primary key (country_id)
    ) type=InnoDB;

    create table internal_user (
        internal_user_id bigint not null auto_increment,
        account_non_expired bit not null,
        account_non_locked bit not null,
        credentials_non_expired bit not null,
        email varchar(255) not null,
        enabled bit not null,
        first_name varchar(255) not null,
        last_name varchar(255) not null,
        password varchar(255) not null,
        username varchar(255) not null unique,
        optlock integer,
        primary key (internal_user_id)
    ) type=InnoDB;

    create table keyword (
        keyword_id bigint not null auto_increment,
        context longtext,
        keyword varchar(255) not null unique,
        optlock integer,
        primary key (keyword_id)
    ) type=InnoDB;

    create table language (
        language_id bigint not null auto_increment,
        code varchar(7) not null unique,
        name varchar(255) not null,
        primary key (language_id)
    ) type=InnoDB;

    create table server_data (
        server_data_id bigint not null auto_increment,
        build_date datetime not null,
        build_number varchar(10) not null,
        setup_date datetime not null,
        version varchar(10) not null,
        primary key (server_data_id),
        unique (version, build_number)
    ) type=InnoDB;

    create table translation (
        translation_id bigint not null auto_increment,
        state varchar(10) not null,
        value longtext,
        optlock integer,
        bundle_id bigint,
        country_id bigint,
        keyword_id bigint,
        language_id bigint,
        primary key (translation_id),
        unique (keyword_id, language_id, country_id, bundle_id)
    ) type=InnoDB;

    alter table authorities 
        add index fk_internal_user_authorities (user_id), 
        add constraint fk_internal_user_authorities 
        foreign key (user_id) 
        references internal_user (internal_user_id);

    alter table translation 
        add index fk_keyword_translation (keyword_id), 
        add constraint fk_keyword_translation 
        foreign key (keyword_id) 
        references keyword (keyword_id);

    alter table translation 
        add index fk_bundle_translation (bundle_id), 
        add constraint fk_bundle_translation 
        foreign key (bundle_id) 
        references bundle (bundle_id);

    alter table translation 
        add index fk_country_translation (country_id), 
        add constraint fk_country_translation 
        foreign key (country_id) 
        references country (country_id);

    alter table translation 
        add index fk_language_translation (language_id), 
        add constraint fk_language_translation 
        foreign key (language_id) 
        references language (language_id);

insert into server_data(build_date, build_number, setup_date, version)
    select buildDate, buildNumber, setupDate, version from tmp_ServerData;
insert into bundle(description, is_default, is_global, name, resource_name)
    select description, isDefault, isGlobal, name, resourceName from tmp_Bundle;
insert into country select * from tmp_Country;
insert into language select * from tmp_Language;
insert into keyword(context, keyword, OPTLOCK)
    select context, keyword, optlock from tmp_Keyword;
insert into translation(state, value, optlock, bundle_id, country_id, keyword_id, language_id)
    select state, value, OPTLOCK, 
            (select b.bundle_id from bundle b, tmp_Bundle tmpBundle where t.BUNDLE_ID = tmpBundle.id and b.name = tmpBundle.name),
            (select c.country_id from country c, tmp_Country tmpCountry where t.COUNTRY_ID = tmpCountry.id and c.name = tmpCountry.name),
            (select k.keyword_id from keyword k, tmp_Keyword tmpKeyword where t.KEYWORD_ID = tmpKeyword.id and k.keyword = tmpKeyword.keyword),
            (select l.language_id from language l, tmp_Language tmpLanguage where t.LANGUAGE_ID = tmpLanguage.id and l.name = tmpLanguage.name)
    from tmp_Translation t;
insert into audit_log_record(created, entity_class, entity_id, message, new_value, old_value, username)
    select created, entityClass, entityId, message, null, null, username from tmp_AuditLogRecord;
update audit_log_record
set message = 'insert'
where message = 'new';
insert into internal_user(account_non_expired, account_non_locked, credentials_non_expired, email, enabled, first_name, last_name, password, username, optlock)
    select accountNonExpired, accountNonLocked, credentialsNonExpired, email, enabled, firstName, lastName, password, username, OPTLOCK from tmp_User;
insert into authorities(user_id, permission)
    select u.internal_user_id, a.permission 
    from tmp_authorities a, tmp_User tmpUser, internal_user u
    where a.userid = tmpUser.id
    and tmpUser.username = u.username;

drop table if exists tmp_Translation cascade;
drop table if exists tmp_AuditLogRecord cascade;
drop table if exists tmp_Keyword cascade;
drop table if exists tmp_Language cascade;
drop table if exists tmp_Bundle cascade;
drop table if exists tmp_Country cascade;
drop table if exists tmp_ServerData cascade;
drop table if exists tmp_authorities cascade;
drop table if exists tmp_User cascade;

commit;

