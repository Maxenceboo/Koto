-- Flyway V1: initial schema
-- Extensions required
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Utilisateurs de l’app
CREATE TABLE app_user (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(255) NOT NULL UNIQUE,
    username VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    password VARCHAR(255) NOT NULL
);

-- Canaux (sur invitation)
CREATE TABLE IF NOT EXISTS channel (
    id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name              VARCHAR(80) NOT NULL,
    description       TEXT,
    created_by        UUID NOT NULL REFERENCES app_user(id) ON DELETE RESTRICT,
    created_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_invite_only    BOOLEAN NOT NULL DEFAULT TRUE      -- tous tes canaux le sont
    );

-- Membres d’un canal
CREATE TABLE IF NOT EXISTS channel_member (
    channel_id        UUID NOT NULL REFERENCES channel(id) ON DELETE CASCADE,
    user_id           UUID NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
    role              VARCHAR(20) NOT NULL DEFAULT 'member',   -- owner | moderator | member
    joined_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (channel_id, user_id)
    );

-- Invitations (d’un membre vers un utilisateur par email)
CREATE TABLE IF NOT EXISTS channel_invite (
    id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    channel_id        UUID NOT NULL REFERENCES channel(id) ON DELETE CASCADE,
    invited_email     VARCHAR NOT NULL,
    invited_by        UUID NOT NULL REFERENCES app_user(id) ON DELETE RESTRICT,
    token             VARCHAR(100) NOT NULL UNIQUE,
    status            VARCHAR(20) NOT NULL DEFAULT 'pending',  -- pending | accepted | revoked | expired
    created_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    accepted_at       TIMESTAMP
    );

-- Phrases (les "kotos")
CREATE TABLE IF NOT EXISTS koto (
    id                   UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    channel_id           UUID NOT NULL REFERENCES channel(id) ON DELETE CASCADE,
    original_author_id   UUID NOT NULL REFERENCES app_user(id) ON DELETE RESTRICT,  -- "qui l’a dite"
    content              VARCHAR(500) NOT NULL,       -- la phrase
    said_at              TIMESTAMP,                 -- quand elle a été dite (optionnel)
    place                VARCHAR(120),                -- où (optionnel)
    created_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_editor_id       UUID REFERENCES app_user(id) ON DELETE SET NULL,
    last_edited_at       TIMESTAMP
    );

CREATE INDEX IF NOT EXISTS ix_koto_channel_created ON koto(channel_id, created_at DESC);
CREATE INDEX IF NOT EXISTS ix_koto_said_at ON koto(said_at);

-- Likes sur une phrase
CREATE TABLE IF NOT EXISTS koto_like (
    user_id          UUID NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
    koto_id          UUID NOT NULL REFERENCES koto(id) ON DELETE CASCADE,
    created_at       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, koto_id)
    );
