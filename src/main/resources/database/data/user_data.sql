-- SCRIPT TO ADD DUMMY USER DATA FOR DEVELOPMENT

-- Login credentials:
-- Employee: employeetestemail1@test.ca / password179faj@8
-- Admin:    admintestemail1@test.ca   / password8KG9@js

DELETE FROM users
WHERE email IN (
    'employeetestemail1@test.ca',
    'admintestemail1@test.ca'
);

INSERT INTO users (email, password, role, first_name, last_name, created_at)
VALUES (
    'employeetestemail1@test.ca',
    '$2a$10$0yo3z8xv2tj4XYxBE0IMA.NqZBBFMNTkk5xR/8Ry8ajCeauzMQsX2', -- password: password179faj@8
    'EMPLOYEE',
    'TestEmployee',
    'TestEmployee1',
    '2026-01-01 10:35:42'
);

INSERT INTO users (email, password, role, first_name, last_name, created_at)
VALUES (
    'admintestemail1@test.ca',
    '$2a$10$U2AtVMVOjktx6M.wrMJt0..1aAGwUiA9bukkx0J7MNxbH5hhNsw7C', -- password: password8KG9@js
    'ADMIN',
    'TestAdmin',
    'TestAdmin1',
    '2026-01-02 14:42:24'
);