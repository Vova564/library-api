INSERT INTO users (id, first_name, last_name, email, password_hash, role, created_at)
VALUES (nextval('users_seq'), 'Admin', 'Admin', 'admin@example.com', 'LemkSXwqSQENfGePAKjudwBE/EM+nhoJ+jYA8KF4TdNBoL7izY5Pcx1OFp0HaCap', 'ADMIN', CURRENT_TIMESTAMP)
ON CONFLICT (email) DO NOTHING;