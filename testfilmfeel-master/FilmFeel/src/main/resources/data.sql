INSERT INTO roles (name) VALUES ('USER');
INSERT INTO roles (name) VALUES ('ADMIN');

INSERT INTO users (
user_username,
user_password,
user_active,
user_name,
user_surname,
user_email

)
SELECT
'admin',
'$2a$12$3G7Lpe9nXRfKjktweeD/uulTJIgV3jOe9zA58NGKprLzqYp2w/okG',
true,
'administrador',
'principal',
'administrador@filmfeel.com'


WHERE NOT EXISTS (SELECT 1 FROM users WHERE user_username = 'admin');

INSERT INTO user_roles (user_id, role_id)
SELECT u.user_id, r.id FROM users u, roles r
WHERE u.user_username = 'admin' AND r.name = 'ADMIN'
AND NOT EXISTS (
    SELECT 1 FROM user_roles WHERE user_id = u.user_id AND role_id = r.id
);

