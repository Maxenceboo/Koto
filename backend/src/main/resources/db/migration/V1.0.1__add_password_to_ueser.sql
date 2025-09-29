-- V2: Ajout du champ password à app_user
ALTER TABLE app_user
    ADD COLUMN password VARCHAR(255) NOT NULL DEFAULT 'changeme';