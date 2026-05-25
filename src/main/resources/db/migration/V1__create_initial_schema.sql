-- ============================================================================
-- V1__create_initial_schema.sql
-- Optimized database schema for GirAPI (PostgreSQL)
-- ============================================================================

-- 1. Users Table (Identity module)
CREATE TABLE users (
    id UUID PRIMARY KEY,
    email VARCHAR(150) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- 2. Verification Tokens Table (Identity module)
CREATE TABLE verification_tokens (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    type VARCHAR(30) NOT NULL,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_verification_tokens_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- Index to avoid full table scans on user deletion check
CREATE INDEX idx_verification_tokens_user ON verification_tokens (user_id);

-- 3. Profiles Table (Profile module)
CREATE TABLE profiles (
    id UUID PRIMARY KEY,
    user_id UUID UNIQUE NOT NULL, -- UNIQUE enforces the 1:1 user-to-profile constraint
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone VARCHAR(30) NOT NULL,
    active_address_id UUID,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_profiles_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- 4. Addresses Table (Profile module)
CREATE TABLE addresses (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    address_line1 VARCHAR(255) NOT NULL,
    address_line2 VARCHAR(255),
    locality VARCHAR(150) NOT NULL,
    administrative_area VARCHAR(150),
    postal_code VARCHAR(20) NOT NULL,
    country_code VARCHAR(2) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_addresses_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- Index to avoid full table scans on user deletion check
CREATE INDEX idx_addresses_user ON addresses (user_id);

-- 5. Outbox Events Table (Shared Outbox module)
CREATE TABLE outbox_events (
    id UUID PRIMARY KEY,
    aggregate_type VARCHAR(100) NOT NULL,
    aggregate_id VARCHAR(100) NOT NULL,
    payload JSONB NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    processed_at TIMESTAMP WITH TIME ZONE,
    processed BOOLEAN NOT NULL DEFAULT FALSE,
    error_message VARCHAR(500),
    retry_count INT NOT NULL DEFAULT 0
);

-- Partial Indexes for Outbox Events (Relay & Cleanup optimizations)
CREATE INDEX idx_outbox_pending ON outbox_events (created_at) WHERE processed = FALSE;
CREATE INDEX idx_outbox_cleanup ON outbox_events (processed_at) WHERE processed = TRUE;
