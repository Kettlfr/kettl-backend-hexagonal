// ********************************************************************
// *                                                                  *
// *                        KEYSPACE                                  *
// *                                                                  *
// ********************************************************************

CREATE KEYSPACE IF NOT EXISTS kettl WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'}
                       AND durable_writes = true;


// ********************************************************************
// *                                                                  *
// *                        TYPES                                     *
// *                                                                  *
// ********************************************************************

CREATE TYPE IF NOT EXISTS kettl.address (
    street text,
    zipcode text,
    city text,
    country text
    );

CREATE TYPE IF NOT EXISTS kettl.phone (
    country_code int,
    number text
    );

// ********************************************************************
// *                                                                  *
// *                        TABLES                                    *
// *                                                                  *
// ********************************************************************


// USERS TABLE
// --------------------------------------------

CREATE TABLE IF NOT EXISTS kettl.users
(
    id             bigint PRIMARY KEY,
    created_at     timestamp,
    email          text,
    email_verified boolean,
    first_name     text,
    last_name      text,
    password       text,
    phone_number   text
);


CREATE INDEX IF NOT EXISTS users_email_index ON kettl.users (email);

// ORGANIZATIONS TABLE
// --------------------------------------------

CREATE TABLE IF NOT EXISTS kettl.organizations
(
    id             bigint PRIMARY KEY,
    address        frozen<address>,
    corporate_name text,
    created_at     timestamp,
    name           text,
    owner_id       bigint,
    siren          text
);

CREATE INDEX IF NOT EXISTS organizations_name_index ON kettl.organizations (name);


// PASSWORD_RESET TABLE
// --------------------------------------------


CREATE TABLE IF NOT EXISTS kettl.password_reset
(
    email        text PRIMARY KEY,
    reset_token text,
) WITH DEFAULT_TIME_TO_LIVE = 10800;

CREATE MATERIALIZED VIEW IF NOT EXISTS kettl.password_reset_by_token AS
SELECT email, reset_token
FROM kettl.password_reset
WHERE reset_token IS NOT null
  AND email IS NOT null
PRIMARY KEY (reset_token, email);

// ESTABLISHMENTS TABLE
// --------------------------------------------

CREATE TABLE IF NOT EXISTS kettl.establishments_members
(
    establishment_id bigint,
    user_id          bigint,
    PRIMARY KEY (establishment_id, user_id)
);

CREATE MATERIALIZED VIEW IF NOT EXISTS kettl.establishments_members_by_user AS
SELECT user_id, establishment_id
FROM kettl.establishments_members
WHERE user_id IS NOT null
  AND establishment_id IS NOT null
PRIMARY KEY (user_id, establishment_id)
WITH CLUSTERING ORDER BY (establishment_id ASC)
 AND comment = 'Materialized view for establishment_users';

CREATE TABLE IF NOT EXISTS kettl.collective_agreement
(
    id   int PRIMARY KEY,
    name text
);

CREATE TABLE IF NOT EXISTS kettl.establishments
(
    id             bigint PRIMARY KEY,
    address        frozen<address>,
    corporate_name text,
    name           text,
    siren          text
);