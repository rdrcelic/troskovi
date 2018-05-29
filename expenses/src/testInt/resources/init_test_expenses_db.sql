create table expenses (
    id BIGINT not null,
    description VARCHAR(255) not null,
    amount NUMERIC(10,2) not null,
    active INT,
    primary key (id)
);

insert into expenses (id, active, amount, description) values ( 1, 1, 7.45, 'hrana');