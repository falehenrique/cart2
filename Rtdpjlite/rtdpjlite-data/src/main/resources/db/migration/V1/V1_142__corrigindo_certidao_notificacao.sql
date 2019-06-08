
INSERT INTO modelo (id,tipo_modelo_id, nome, modelo) VALUES (21,2,'BUSCA ENTRETANTO TD','

                                             <b> C E R T I D Ã O </b><br><br>
                                                                                                  Pedido n°:  {{NUMERO_PROTOCOLO}}<br>

Certifico, atendendo ao pedido feito por pessoa interessada que revendo neste Serviço, os livros deste 2º Oficial de Registro Civil das Pessoas Jurídicas da Comarca de Osasco – Estado de São Paulo, verificou INEXISTIR qualquer registro em nome da pessoa jurídica denominada <b>{{parte}}</b>, entretanto, encontram-se registradas nesta serventia sob o n° <b>{{REGISTRO}}</b> de {{DATA_REGISTRO}} a Ata de Assembleia Geral de Constituição, realizada aos _________ e o Estatuto social da pessoa jurídica denominada <b>“[NOME_EMPRESA]”</b>, anteriomente denominada <b>“[NOME_EMPRESA_ANTERIOR]”</b>, inscrita no CNPJ/MF sob o nº [CPF_CNPJ_EMPRESA], e sob o n° {{REGISTRO_ANTERIOR}} de [DATA_REGISTRO_ANTERIOR] o contrato social datado de [DATA_DOCUMENTO] da pessoa jurídica denominada “[NOME_EMPRESA]”. Nada mais. Todo o referido é verdade e dá fé. Osasco, {{DATA_ATUAL_EXTENSO}}. Eu, [NOME_AUXILIAR], Auxiliar de Registro, dei busca e digitei, e eu, [NOME_ESCREVENTE], Escrevente, conferi e assino.<br><br><br>



__________________________________
<b>[NOME_ESCREVENTE]</b><br>
<b>[CARGO_FUNCIONARIO] </b><br>

<br>
<br>
{{CUSTAS}} - Total R$ {{TOTAL_CUSTAS}}

');


INSERT INTO modelo (id,tipo_modelo_id, nome, modelo) VALUES (22,2,'BUSCA NEGATIVA TD','
                            <b>C E R T I D Ã O</b><br><br>


<b> Pedido n°: {{NUMERO_PROTOCOLO}}</b><br><br>


Certifica, atendendo ao pedido feito por pessoa interessada que revendo neste Serviço, os livros deste 2º Oficial de Registro Civil das Pessoas Jurídicas da Comarca de Osasco – Estado de São Paulo, verificou INEXISTIR qualquer registro em nome de <b>“{{parte}}”</b>. Nada mais. Todo o referido é verdade e dá fé. Osasco, {{DATA_ATUAL_EXTENSO}}. Eu, [NOME_AUXILIAR], Auxiliar de Registro, dei busca e digitei, e eu, [NOME_ESCREVENTE], Escrevente, conferi e assino.<br><br><br>



__________________________________
[NOME_ESCREVENTE]<br>
[CARGO_FUNCIONARIO] <br>

<br>
<br>
{{CUSTAS}} - Total R$ {{TOTAL_CUSTAS}}
');

INSERT INTO modelo (id,tipo_modelo_id, nome, modelo) VALUES (23,2,'BUSCA POSITIVA TD','<b> C E R T I D Ã O </b><br><br>


Pedido n°:  {{NUMERO_PROTOCOLO}}<br><br>



Certifico, atendendo ao pedido feito por pessoa interessada que revendo neste Serviço, a seu cargo, os livros deste 2º Oficial de Registro Civil das Pessoas Jurídicas da Comarca de Osasco – Estado de São Paulo, verificou CONSTAR registro em nome de “{{parte}}”, inscrita no CNPJ/MF sob o n° [CPF_CNPJ_PARTE], sendo que seu ato constitutivo se deu a partir do Contrato Social por Cotas de Responsabilidade Limitada, assinado aos [DATA_CONTRATO_SOCIAL], registrado sob o n° {{REGISTRO}} em {{DATA_REGISTRO}}. Nada mais. Todo o referido é verdade e dá fé. Osasco, {{DATA_ATUAL_EXTENSO}}. Eu, [NOME_AUXILIAR], Auxiliar de Registro, dei busca e digitei, e eu, [NOME_ESCREVENTE], Escrevente, conferi e assino.<br><br><br>



__________________________________
[NOME_ESCREVENTE]B<br>
[CARGO_FUNCIONARIO] <br>
<br>
<br>
{{CUSTAS}} - Total R$ {{TOTAL_CUSTAS}}
');


INSERT INTO modelo (id,tipo_modelo_id, nome, modelo) VALUES (24,5,'NOTIFICAÇÃO CERTIFICADA - Positiva','<b> C E R T I D Ã O </b><br><br> *


CERTIFICO e dou fé
que ENTREGUEI a notificação registrada sob o número {{NUMERO_REGISTRO}},
em diligência feita no endereço indicado no dia {{DATA_ATUAL}} às ____________h, _____________ às ______________h, ______________ às ____________h e ______________ às ___________h.  <br><br>


1- Registro<br>
A notificação foi registrada sob o nº {{NUMERO_REGISTRO}} em data de {{DATA_REGISTRO}}, no Registro de Títulos e Documentos.<br><br>


2-Partes<br>
Apresentante: {{APRESENTANTE_NOME}}<br><br>


Destinatário: ________________<br><br>


3-Encerramento<br>
O referido é verdade e dou fé.<br><br>


{{DATA_ATUAL_EXTENSO}}.<br><br>

__________________________________
[NOME_ESCREVENTE]<br>
[CARGO_FUNCIONARIO] <br>

');


INSERT INTO modelo (id,tipo_modelo_id, nome, modelo) VALUES (25,2,'NOTIFICAÇÃO CERTIFICADA - Negativa','<b> C E R T I D Ã O </b><br><br> *


CERTIFICO e dou fé
que a notificação registrada sob o número {{NUMERO_REGISTRO}},
NÃO FOI CUMPRIDA deixando de notificar o destinatário em virtude das ocorrências:
Não atendeu os avisos para comparecimento a este Oficial por mim deixados com as
__________________ (funcionárias do condomínio) e _________________ (esposa do destinatário)
que me informaram de que o destinatário reside no local, mas não estava no momento,
em diligências procedidas no endereço indicado nos dias: ___________ às ____________h, _____________ às ______________h, ______________ às ____________h e ______________ às ___________h.  <br><br>


1- Registro<br>
A notificação foi registrada sob o nº {{NUMERO_REGISTRO}} em data de {{DATA_REGISTRO}}, no Registro de Títulos e Documentos.<br><br>


2-Partes<br>
Apresentante: {{APRESENTANTE_NOME}}<br><br>


Destinatário: ________________<br><br>


3-Encerramento<br>
O referido é verdade e dou fé.<br><br>


{{DATA_ATUAL_EXTENSO}}.<br><br>

__________________________________
[NOME_ESCREVENTE]<br>
[CARGO_FUNCIONARIO] <br>

');

INSERT INTO modelo (id,tipo_modelo_id, nome, modelo) VALUES (26,2,'NOTIFICAÇÃO CERTIFICADA - Positiva','<b> C E R T I D Ã O </b><br><br> *


CERTIFICO e dou fé
que ENTREGUEI a notificação registrada sob o número {{NUMERO_REGISTRO}},
em diligência feita no endereço indicado no dia {{DATA_ATUAL}} às ____________h, _____________ às ______________h, ______________ às ____________h e ______________ às ___________h.  <br><br>


1- Registro<br>
A notificação foi registrada sob o nº {{NUMERO_REGISTRO}} em data de {{DATA_REGISTRO}}, no Registro de Títulos e Documentos.<br><br>


2-Partes<br>
Apresentante: {{APRESENTANTE_NOME}}<br><br>


Destinatário: ________________<br><br>


3-Encerramento<br>
O referido é verdade e dou fé.<br><br>


{{DATA_ATUAL_EXTENSO}}.<br><br>

__________________________________
[NOME_ESCREVENTE]<br>
[CARGO_FUNCIONARIO] <br>

');

update natureza set tipo_emissao_certidao_id = 40 where nome = 'NOTIFICAÇÃO';

--"NOTIFICAÇÃO"
delete from modelo_natureza_subnatureza where natureza_id = 18;
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (18,66,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (18,66,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (18,66,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (18,66,53);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (18,66,55);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (18,66,20);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (18,66,24);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (18,66,25);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (18,66,26);



--REGISTRO INDISPONIBILIDADE
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES ((select (id) from natureza where nome = 'INDISPONIBILIDADE'),(SELECT id FROM sub_natureza WHERE nome = 'REGISTRO' and natureza_id = (select (id) from natureza where nome = 'INDISPONIBILIDADE')),2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES ((select (id) from natureza where nome = 'INDISPONIBILIDADE'),(SELECT id FROM sub_natureza WHERE nome = 'REGISTRO' and natureza_id = (select (id) from natureza where nome = 'INDISPONIBILIDADE')),1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES ((select (id) from natureza where nome = 'INDISPONIBILIDADE'),(SELECT id FROM sub_natureza WHERE nome = 'REGISTRO' and natureza_id = (select (id) from natureza where nome = 'INDISPONIBILIDADE')),52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES ((select (id) from natureza where nome = 'INDISPONIBILIDADE'),(SELECT id FROM sub_natureza WHERE nome = 'REGISTRO' and natureza_id = (select (id) from natureza where nome = 'INDISPONIBILIDADE')),53);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES ((select (id) from natureza where nome = 'INDISPONIBILIDADE'),(SELECT id FROM sub_natureza WHERE nome = 'REGISTRO' and natureza_id = (select (id) from natureza where nome = 'INDISPONIBILIDADE')),55);

--CANCELAMENTO INDISPONIBILIDADE
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES ((select (id) from natureza where nome = 'INDISPONIBILIDADE'),(SELECT id FROM sub_natureza WHERE nome = 'CANCELAMENTO' and natureza_id = (select (id) from natureza where nome = 'INDISPONIBILIDADE')),1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES ((select (id) from natureza where nome = 'INDISPONIBILIDADE'),(SELECT id FROM sub_natureza WHERE nome = 'CANCELAMENTO' and natureza_id = (select (id) from natureza where nome = 'INDISPONIBILIDADE')),2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES ((select (id) from natureza where nome = 'INDISPONIBILIDADE'),(SELECT id FROM sub_natureza WHERE nome = 'CANCELAMENTO' and natureza_id = (select (id) from natureza where nome = 'INDISPONIBILIDADE')),52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES ((select (id) from natureza where nome = 'INDISPONIBILIDADE'),(SELECT id FROM sub_natureza WHERE nome = 'CANCELAMENTO' and natureza_id = (select (id) from natureza where nome = 'INDISPONIBILIDADE')),53);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES ((select (id) from natureza where nome = 'INDISPONIBILIDADE'),(SELECT id FROM sub_natureza WHERE nome = 'CANCELAMENTO' and natureza_id = (select (id) from natureza where nome = 'INDISPONIBILIDADE')),55);
