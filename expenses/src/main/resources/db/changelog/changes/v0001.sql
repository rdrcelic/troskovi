create table expenses (
    id BIGINT(20) not null,
    description VARCHAR(255) not null,
    amount NUMERIC(10,2) not null,
    active TINYINT(1),
    primary key (id)
);