DROP TABLE IF EXISTS "bots" CASCADE;
CREATE TABLE "bots"
(
    "id"                 SERIAL PRIMARY KEY,
    "name"               varchar,
    "start_date"         timestamp,
    "end_date"           timestamp,
    "result_value"       numeric(16, 2),
    "budget"             numeric(16, 2),
    "status"             varchar,
    "strategy"           varchar,
    "parameters"         varchar,
    "pair_stock"         varchar,
    "resample_frequency" varchar,
    "roi"                numeric(4, 2),
    "wlratio"            numeric(4, 2),
    "create_date"        timestamp
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
    "id"          SERIAL PRIMARY KEY,
    "bot_id"      integer not null,
    "type"        varchar,
    "open_price"  numeric(16, 2),
    "close_price" numeric(16, 2),
    "date_open"   timestamp,
    "date_close"  timestamp,
    "profit_lose" numeric(16, 2),
    "stop_loss"   numeric(16, 2),
    "take_profit" numeric(16, 2),
    "amount"      numeric,
    "comment"     varchar
);


ALTER TABLE "trades"
    ADD FOREIGN KEY ("bot_id") REFERENCES "bots" ("id");
