insert into forma_calculo(nome,formula) values ('Custas integrais','oficial*1;estado*1;ipesp*1;registro_civil*1;tj*1;mp*1;iss=oficial*0.02;total=oficial+estado+ipesp+registro_civil+tj+mp+iss');
insert into forma_calculo(nome,formula) values ('Zerar custas','oficial*0;estado*1;ipesp*1;registro_civil*1;tj*1;mp*1;iss=oficial*0.02;total=oficial+estado+ipesp+registro_civil+tj+mp+iss');
insert into forma_calculo(nome,formula) values ('Desconto oficial 50%','oficial*0.5;estado*1;ipesp*1;registro_civil*1;tj*1;mp*1;iss=oficial*0.02;total=oficial+estado+ipesp+registro_civil+tj+mp+iss');
insert into forma_calculo(nome,formula) values ('Adicional de diligência 50%','oficial*1.5;estado*1;ipesp*1;registro_civil*1;tj*1;mp*1;iss=oficial*0.02;total=oficial+estado+ipesp+registro_civil+tj+mp+iss');
