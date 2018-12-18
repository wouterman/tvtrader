create table API_CREDENTIALS
(
  KEY    VARCHAR(255) not null
    primary key,
  SECRET VARCHAR(255)
);

create table ACCOUNT
(
  EXCHANGE          VARCHAR(255) not null,
  NAME              VARCHAR(255) not null,
  BUY_LIMIT         DOUBLE       not null,
  MAIN_CURRENCY     VARCHAR(255),
  MINIMUM_GAIN      DOUBLE       not null,
  STOPLOSS          DOUBLE       not null,
  TRAILING_STOPLOSS DOUBLE       not null,
  CREDENTIALS_KEY   VARCHAR(255),
  primary key (EXCHANGE, NAME),
  constraint FKFKIWU0PTCE0QW72OD4WAKPS1O
    foreign key (CREDENTIALS_KEY) references API_CREDENTIALS
);

create table CONFIGURATION
(
  NAME                        VARCHAR(255) not null
    primary key,
  ASSET_REFRESH_RATE          INTEGER      not null,
  EXPECTED_SENDER             VARCHAR(255),
  MAIL_POLLING_INTERVAL       INTEGER      not null,
  OPEN_ORDERS_EXPIRATION_TIME INTEGER      not null,
  OPEN_ORDERS_INTERVAL        INTEGER      not null,
  RETRY_ORDER_FLAG            BOOLEAN      not null,
  STOPLOSS_INTERVAL           INTEGER      not null,
  TICKER_REFRESH_RATE         INTEGER      not null
);

create table MAIL_CONFIGURATION
(
  NAME     VARCHAR(255) not null
    primary key,
  HOST     VARCHAR(255),
  INBOX    VARCHAR(255),
  PASSWORD VARCHAR(255),
  PORT     INTEGER      not null,
  PROTOCOL VARCHAR(255),
  USERNAME VARCHAR(255)
);