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
    "strategy"     varchar,
    "parameters"   varchar,
    "create_date"  timestamp
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
    "bot_id"     integer,
    "type"       varchar,
    "pair_stock" varchar,
    "open_price"    numeric(16, 2),
    "date_open"       timestamp,
    "date_close"       timestamp,
    "balance_before"    numeric(16, 2),
    "balance_after"    numeric(16, 2),
    "stop_loss"    numeric(16, 2),
    "take_profit"    numeric(16, 2),
    "comment"       varchar
);


ALTER TABLE "trades"
    ADD FOREIGN KEY ("bot_id") REFERENCES "bots" ("id");
