-- Insertar Tags
INSERT INTO tags (id, name, description, created_at, updated_at) VALUES (1, 'java', 'Java programming', NOW(), NOW()), (2, 'spring', 'Spring Framework', NOW(), NOW()), (3, 'postgresql', 'PostgreSQL Database', NOW(), NOW()), (4, 'sqlserver', 'PostgreSQL Database', NOW(), NOW()), (5, 'typescript', 'PostgreSQL Database', NOW(), NOW()), (6, 'nodejs', 'PostgreSQL Database', NOW(), NOW());

-- Permisos
INSERT INTO permissions (id, name, created_at, updated_at) VALUES (1, 'READ_POST', now(), now()),(2, 'WRITE_POST', now(), now()),(3, 'DELETE_POST', now(), now()),(4, 'MANAGE_USERS', now(), now()),(5, 'VIEW_STATS', now(), now()),(6, 'MODERATE_CONTENT', now(), now());

-- Insert Roles
INSERT INTO roles (id, name, created_at, updated_at) VALUES (1, 'ADMIN', now(), now()),(2, 'MODERATOR', now(), now()),(3, 'USER', now(), now());

INSERT INTO roles_permissions (role_id, permission_id) VALUES (1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6);

INSERT INTO users (id, username, password, email, bio, reputation, created_at, updated_at, account_non_expired, account_non_locked, credentials_non_expired, enabled) VALUES (1, 'admin', '$2a$10$FdO3ZtUwVRaCiB58tm0bt.9UYns8.sLBDuvxlqPgmQ.bOqQ/htLBq', 'admin@example.com', 'System Administrator', 0, now(), now(), true, true, true, true),(2, 'moderator', '$2a$10$FdO3ZtUwVRaCiB58tm0bt.9UYns8.sLBDuvxlqPgmQ.bOqQ/htLBq', 'mod@example.com', 'Content Moderator', 0, now(), now(), true, true, true, true),(3, 'user', '$2a$10$KADnlCBPyTHrodDSbTpHyexRzhrfrRuORJYQKilMBzIwWqjHzChti', 'user1@example.com', 'Regular User', 0, now(), now(), true, true, true, true);

INSERT INTO users_roles (user_id, role_id) VALUES (1, 1), (2, 2),  (3, 3);
-- Insertar Preguntas
INSERT INTO questions (id, title, slug, content, views, score, created_at, updated_at, user_id) VALUES (1, 'How to use Hibernate?', 'how-to-hibernate', 'Best practices for Hibernate ORM', 0, 0, NOW(), NOW(), 1), (2, 'Spring Boot config', 'spring-boot-config', 'Configure application.properties', 0, 0, NOW(), NOW(), 2), (3, 'PostgreSQL indexing', 'postgres-indexes', 'Optimize queries with indexes', 0, 0, NOW(), NOW(), 1);

-- Relacionar Preguntas-Tags
INSERT INTO questions_tags (question_entity_id, tags_id) VALUES (1, 1), (1, 2), (2, 2), (3, 3);

-- Insertar Respuestas
INSERT INTO answers (id, content, score, is_correct, created_at, updated_at, question_id, user_id) VALUES (1, 'Use EntityManager properly', 0, false, NOW(), NOW(), 1, 1), (2, 'Enable Hibernate cache', 0, false, NOW(), NOW(), 1, 1), (3, 'Use @ConfigurationProperties', 0, true, NOW(), NOW(), 2, 1), (4, 'Externalize configuration', 0, false, NOW(), NOW(), 2, 2), (5, 'Use B-tree indexes', 0, true, NOW(), NOW(), 3, 2), (6, 'Avoid full table scans', 0, false, NOW(), NOW(), 3, 1);

-- Insertar Comentarios
INSERT INTO comments (id, content, post_type, created_at, updated_at, user_id, question_entity_id, answer_entity_id) VALUES (1, 'Great explanation!', 'question', NOW(), NOW(), 2, 1, NULL), (2, 'Need more examples', 'answer', NOW(), NOW(), 1, NULL, 3), (3, 'This solved my issue', 'question', NOW(), NOW(), 2, 2, NULL), (4, 'Outdated approach', 'answer', NOW(), NOW(), 1, NULL, 5);

INSERT INTO votes (id, answerentity_id, userentity_id, util) VALUES (1, 4, 1, true)

ALTER SEQUENCE tags_id_seq RESTART WITH 7;
ALTER SEQUENCE users_id_seq RESTART WITH 4;
ALTER SEQUENCE questions_id_seq RESTART WITH 4;
ALTER SEQUENCE answers_id_seq RESTART WITH 7;
ALTER SEQUENCE comments_id_seq RESTART WITH 5;
ALTER SEQUENCE permissions_id_seq RESTART WITH 7;
ALTER SEQUENCE roles_id_seq RESTART WITH 7;
ALTER SEQUENCE votes_id_seq RESTART WITH 2;
