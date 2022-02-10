CREATE TABLE IF NOT EXISTS EVENTS (
    ID VARCHAR(30) primary key,
    DURATION integer not null,
    ALERT boolean  not null,
    LOG_TYPE VARCHAR(50),
    HOST VARCHAR(30)
);