INSERT INTO roles (name)
VALUES ('ADMIN'),
       ('DEVELOPER'),
       ('REPORTER');

INSERT INTO tags (name)
VALUES ('BACKEND'),
       ('FRONTEND'),
       ('FEATURE'),
       ('BUG');

INSERT INTO issue_statuses (name)
VALUES ('OPEN'),
       ('IN_PROGRESS'),
       ('CLOSED');

INSERT INTO issue_priorities (name)
VALUES ('LOW'),
       ('MEDIUM'),
       ('HIGH');

INSERT INTO users (login, email, password, role_id)
VALUES ('admin',
        'admin@gmail.com',
        'admin',
        (SELECT id FROM roles WHERE name = 'ADMIN'));