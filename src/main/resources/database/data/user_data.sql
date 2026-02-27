-- SCRIPT TO ADD DUMMY USER DATA FOR DEVELOPMENT

INSERT INTO users (email, password, role, first_name, last_name, created_at)
	VALUES (
		'employeetestemail1@test.ca',
		'password179faj@8',
		'EMPLOYEE',
		'TestEmployee',
		'TestEmployee1',
		'2026-01-01 10:35:42'
	);
	
	INSERT INTO users (email, password, role, first_name, last_name, created_at)
	VALUES (
		'admintestemail1@test.ca',
		'password8KG9@js',
		'ADMIN',
		'TestAdmin',
		'TestAdmin1',
		'2026-01-02 14:42:24'
	);