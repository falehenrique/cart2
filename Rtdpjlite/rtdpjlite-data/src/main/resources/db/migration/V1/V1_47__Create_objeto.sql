CREATE TABLE objeto(
id serial,
nome varchar(255) not null,
ic_garantia boolean default false,
constraint pk_objeto primary key (id)
);