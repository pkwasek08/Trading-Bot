DROP TABLE IF EXISTS "bots" CASCADE;
CREATE TABLE "bots"
(
    "id"           SERIAL PRIMARY KEY,
    "name"         varchar,
    "start_date"   timestamp,
    "end_date"     timestamp,
    "result_value" numeric(16, 2),
    "budget"       numeric(16, 2),
    "status"       varchar,
    "parameters"   varchar
);

DROP TABLE IF EXISTS "users" CASCADE;
CREATE TABLE "users"
(
    "id"       SERIAL PRIMARY KEY,
    "user"     varchar,
    "password" varchar,
    "account"  varchar
);

DROP TABLE IF EXISTS "trades" CASCADE;
CREATE TABLE "trades"
(
    "id"         SERIAL PRIMARY KEY,
    "date"       timestamp,
    "balance"    numeric(16, 2),
    "pair_stock" varchar,
    "type"       varchar,
    "bot_id"     integer
);


ALTER TABLE "trades" ADD FOREIGN KEY ("bot_id") REFERENCES "bots" ("id");
