-- Add authentication-related columns to app_user
ALTER TABLE app_user
    ADD COLUMN IF NOT EXISTS password_hash VARCHAR(255),
    ADD COLUMN IF NOT EXISTS role VARCHAR(30) NOT NULL DEFAULT 'USER';

-- Optional: ensure email remains unique and not null (already present)
-- Update existing rows that might have null password_hash (for legacy data)
UPDATE app_user SET password_hash = password_hash WHERE password_hash IS NULL;

