INSERT INTO language(code, name) VALUES ('DEFAULT', 'Default(English)');
INSERT INTO country(code, name) VALUES ('DEFAULT', 'Default');
INSERT INTO bundle(name, resource_name, description, is_default, is_global) 
    VALUES ('All', 'all', 'This bundle means the keyword applies to all bundles', true, true);
INSERT INTO internal_user(username, password, first_name, last_name, enabled, email, account_non_expired, account_non_locked, credentials_non_expired, optlock) 
    VALUES('admin', 'a4a88c0872bf652bb9ed803ece5fd6e82354838a9bf59ab4babb1dab322154e1', 'admin', 'admin', true, 'none', true, true, true, 0);
INSERT INTO authorities(user_id, permission) VALUES(1, 'ROLE_ADMIN');

COMMIT;