-- =============================================================================
-- 5TPROMART - Database Initialization Script
-- =============================================================================
-- This script runs ONCE when the PostgreSQL container is first created.
-- It's mounted to /docker-entrypoint-initdb.d/init.sql
-- =============================================================================

-- Create any additional databases if needed
-- CREATE DATABASE fivetpromart_test;

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE fivetpromart_db TO postgres;

-- Log success
DO $$
BEGIN
    RAISE NOTICE '5TProMart database initialized successfully!';
END $$;
