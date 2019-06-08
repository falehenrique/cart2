update feriado set recorrente = false
where
 dia in ('2017-02-28', '2017-04-14', '2017-06-15');

insert into feriado(nome, dia, recorrente) values ('Carnaval', '2018-02-13', false);
insert into feriado(nome, dia, recorrente) values ('Sexta-Feira Santa', '2018-03-30', false);
insert into feriado(nome, dia, recorrente) values ('Corpus Christi', '2018-05-31', false);

insert into feriado(nome, dia, recorrente) values ('Carnaval', '2019-03-05', false);
insert into feriado(nome, dia, recorrente) values ('Sexta-Feira Santa', '2019-04-19', false);
insert into feriado(nome, dia, recorrente) values ('Corpus Christi', '2019-06-20', false);
