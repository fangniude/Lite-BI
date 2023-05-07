INSERT INTO public."user" (id, created_time, last_modified_time, deleted, email, account, name, password, salt) VALUES (1, '2023-04-29 21:48:29.878281', '2023-04-29 21:48:29.878281', false, 'admin@lite.bi', 'admin', 'admin', '111111', '111111') ON CONFLICT DO NOTHING;

INSERT INTO public.tenant (id, created_time, last_modified_time, code, name, created_by_id, last_modified_by_id) VALUES (286209467285505, '2023-04-29 21:48:29.878281', '2023-04-29 21:48:29.878281', 'admin', 'admin', 1, 1) ON CONFLICT DO NOTHING;

-- init tenant
CREATE SCHEMA IF NOT EXISTS "admin";
CREATE EXTENSION IF NOT EXISTS mysql_fdw;
CREATE EXTENSION IF NOT EXISTS citus;
