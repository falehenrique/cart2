--MODELOS
delete from modelo_natureza_subnatureza;

delete from modelo;

INSERT INTO modelo (id,tipo_modelo_id, nome, modelo) VALUES (4,2,'BREVE RELATO PJ','<b>Pedido nº. {{NUMERO_PROTOCOLO}}</b><br>


<b>Data do Pedido: {{DATA_PROTOCOLO}}</b><br><br>

<b>CERTIDÃO DE BREVE RELATO</b><br><br><br>


Certifica, atendendo ao pedido feito por pessoa interessada que revendo neste Serviço, a seu cargo, os livros deste 2° Oficial de Registro Civil das Pessoas Jurídicas da Comarca de Osasco – Estado de São Paulo, verificou CONSTAR em nome da “{{parte}}”, inscrita no CNPJ/MF sob o nº [CPF_CNPJ_PARTE], com sede social à [ENDERECO_SEDE_SOCIAL], os seguintes registros:

<table width="600" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td>{{REGISTRO}} em {{DATA_REGISTRO}}</td>
    <td>[INTEGRA_ATA]</td>
  </tr>
</table>
Nada mais. Todo o referido é verdade e dá fé. Osasco, {{DATA_ATUAL_EXTENSO}}. Eu, [NOME_AUXILIAR], Auxiliar de Registro, dei busca e digitei, e eu, [NOME_ESCREVENTE], Escrevente, conferi e assino.<br><br><br>



__________________________________
[NOME_ESCREVENTE]<br>
[CARGO_FUNCIONARIO]<br>
<br>
<br>

{{CUSTAS}} - Total R$ {{TOTAL_CUSTAS}}

');
INSERT INTO modelo (id,tipo_modelo_id, nome, modelo) VALUES (5,2,'BREVE RELATO TD','

<b>CERTIDÃO DE BREVE RELATO</b><br><br>

Certifica, atendendo ao pedido feito por pessoa interessada que revendo neste Serviço, a seu cargo, os livros deste 2º Oficial de Registro de Títulos e Documentos, verificou CONSTAR em nome do “{{parte}}”, inscrito no CNPJ/MF. sob o n°. [CPF_CNPJ_PARTE], situado na [ENDERECO_SEDE_SOCIAL], os seguintes registros: <br><br>
<table width="600" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><{{REGISTRO}}> em {{DATA_REGISTRO}}</td>
    <td>{{INTEGRA_ATA}}</td>
  </tr>
</table>
Nada mais. Todo o referido é verdade e dá fé. Osasco, {{DATA_ATUAL_EXTENSO}}. Eu, [NOME_AUXILIAR], Auxiliar de Registro, dei busca, digitei, e eu [NOME_ESCREVENTE], Escrevente, conferi e assino.<br><br><br>



______________________________
[NOME_ESCREVENTE]<br>
[CARGO_FUNCIONARIO]<br>
<br>
<br>
{{CUSTAS}} - Total R$ {{TOTAL_CUSTAS}}

');

INSERT INTO modelo (id,tipo_modelo_id, nome, modelo) VALUES (6,2,'BUSCA ENTRETANTO PJ','

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


INSERT INTO modelo (id,tipo_modelo_id, nome, modelo) VALUES (7,2,'BUSCA NEGATIVA PJ','
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

INSERT INTO modelo (id,tipo_modelo_id, nome, modelo) VALUES (8,2,'BUSCA POSITIVA PJ','<b> C E R T I D Ã O </b><br><br>


Pedido n°:  {{NUMERO_PROTOCOLO}}<br><br>



Certifico, atendendo ao pedido feito por pessoa interessada que revendo neste Serviço, a seu cargo, os livros deste 2º Oficial de Registro Civil das Pessoas Jurídicas da Comarca de Osasco – Estado de São Paulo, verificou CONSTAR registro em nome de “{{parte}}”, inscrita no CNPJ/MF sob o n° [CPF_CNPJ_PARTE], sendo que seu ato constitutivo se deu a partir do Contrato Social por Cotas de Responsabilidade Limitada, assinado aos [DATA_CONTRATO_SOCIAL], registrado sob o n° {{REGISTRO}} em {{DATA_REGISTRO}}. Nada mais. Todo o referido é verdade e dá fé. Osasco, {{DATA_ATUAL_EXTENSO}}. Eu, [NOME_AUXILIAR], Auxiliar de Registro, dei busca e digitei, e eu, [NOME_ESCREVENTE], Escrevente, conferi e assino.<br><br><br>



__________________________________
[NOME_ESCREVENTE]B<br>
[CARGO_FUNCIONARIO] <br>
<br>
<br>
{{CUSTAS}} - Total R$ {{TOTAL_CUSTAS}}
');

INSERT INTO modelo (id,tipo_modelo_id, nome, modelo) VALUES (9,2,'COMPROBATÓRIA','



                                        <b>C E R T I D Ã O</b><br><br>




Certifica, atendendo ao pedido feito por pessoa interessada que revendo neste Serviço, os livros deste 2º Oficial de Registro de Títulos e Documentos da Comarca de Osasco – Estado de São Paulo verificou CONSTAR em nome do <b>“{{parte}}”</b>, inscrito no CNPJ sob o nº. <b>[CPF_CNPJ_PARTE]</b>, a Ata da Assembleia Geral de Cotistas, registrada sob o n° {{REGISTRO}} em {{DATA_REGISTRO}}, averbada a margem do registro {{REGISTRO_ANTERIOR}} de [DATA_REGISTRO_ANTERIOR], da Constituição do Fundo. Nada mais. Todo o referido é verdade e dá fé. Osasco, {{DATA_ATUAL_EXTENSO}}. Eu, [NOME_AUXILIAR], Auxiliar de Registro, dei busca e digitei, e eu, [NOME_ESCREVENTE], Escrevente, conferi e assino.<br><br><br>



__________________________________
[NOME_ESCREVENTE]<br>
[CARGO_FUNCIONARIO] <br>
<br>
<br>
{{CUSTAS}} - Total R$ {{TOTAL_CUSTAS}}

');

INSERT INTO modelo (id,tipo_modelo_id, nome, modelo) VALUES (10,2,'COMPROBATÓRIA BANCO','



                                        <b>C E R T I D Ã O</b><br><br>




Certifica, atendendo ao pedido feito por pessoa interessada que revendo neste Serviço, os livros deste 2º Oficial de Registro de Títulos e Documentos da Comarca de Osasco – Estado de São Paulo verificou CONSTAR em nome do <b>“{{parte}}”</b>, inscrito no CNPJ sob o nº. <b>[CPF_CNPJ_PARTE]</b>, a Ata da Assembleia Geral de Cotistas, registrada sob o n° {{REGISTRO}} em {{DATA_REGISTRO}}, averbada a margem do registro {{REGISTRO_ANTERIOR}} de [DATA_REGISTRO_ANTERIOR], da Constituição do Fundo. Nada mais. Todo o referido é verdade e dá fé. Osasco, {{DATA_ATUAL_EXTENSO}}. Eu, [NOME_AUXILIAR], Auxiliar de Registro, dei busca e digitei, e eu, [NOME_ESCREVENTE], Escrevente, conferi e assino.<br><br><br>



__________________________________
[NOME_ESCREVENTE]<br>
[CARGO_FUNCIONARIO] <br>

<br>
<br>
{{CUSTAS}} - Total R$ {{TOTAL_CUSTAS}}
');


INSERT INTO modelo (id,tipo_modelo_id, nome, modelo) VALUES (11,2,'DOCUMENTO ELETRÔNICO - DAC - BEM','<b>Pedido nº. {{NUMERO_PROTOCOLO}}</b><br>
<b>Data do Pedido: {{DATA_PROTOCOLO}}</b><br><br>


<b>CE R T I D Ã O</b><br><br>

CERTIFICO que a presente certidão é constituída de [QUANTIDADE_FOLHAS] folhas, as quais foram extraídas do documento eletrônico: Ata da Assembleia Geral de Cotistas do “{{parte}}”, inscrito no CNPJ sob o nº. [CPF_CNPJ_PARTE], registrado e assinado eletronicamente pelo 2° Oficial de Registro de Imóveis, Títulos e Documentos e Civil de Pessoa Jurídica de Osasco, sob nº. {{REGISTRO}} em {{DATA_REGISTRO}}, averbado a margem do registro {{REGISTRO_ANTERIOR}} de [DATA_REGISTRO_ANTERIOR], da Ata de Assembleia Geral Extraordinária que alterou a administração do fundo para [NOME_EMPRESA], e possui o mesmo valor probante do original para todos os fins de Direito, seja em Juízo ou fora dele, nos termos dos artigos 161 da Lei n. 6.015/73 e 217 da Lei 10.406/02.
              Certifico, ainda que, o documento eletrônico foi assinado digitalmente por {{parte}} representado por sua administradora  [NOME_EMPRESA], nas pessoas de seus representantes legais _______________e ___________________.
              Nada mais. Todo o referido é verdade e dá fé. Osasco, {{DATA_ATUAL_EXTENSO}}. Eu, [NOME_AUXILIAR], Auxiliar de Registro, dei busca e digitei, e eu, [NOME_ESCREVENTE], Escrevente, conferi e assino.<br><br><br>



__________________________________
[NOME_ESCREVENTE]<br>
[CARGO_FUNCIONARIO] <br>

<br>
<br>
{{CUSTAS}} - Total R$ {{TOTAL_CUSTAS}}
');

INSERT INTO modelo (id,tipo_modelo_id, nome, modelo) VALUES (12,2,'DOCUMENTO FÍSICO - DAC','');
INSERT INTO modelo (id,tipo_modelo_id, nome, modelo) VALUES (13,2,'DOCUMENTO ELETRÔNICO BANCO - DAC - BEM','');

INSERT INTO modelo (id,tipo_modelo_id, nome, modelo) VALUES (14,2,'INTEIRO TEOR','              <b>C E R T I D Ã O DE  INTEIRO  TEOR</b><br><br>


                         CERTIFICO, e dou fé que, revendo os Arquivos deste Registro Público, encontrei sob os n°s. <{{REGISTRO}}>
{{DATA_REGISTRO}}, os registros dos documentos da <b>“{{parte}}”</b>, inscrito no CNPJ sob o nº. <b>[CPF_CNPJ_PARTE]</b>, cujo conteúdo compõe a presente CERTIDÃO DE INTEIRO TEOR, em cópias reprográficas, contendo [QUANTIDADE_FOLHAS] fuarenta e umaolhas, devidamente numeradas e rubricadas, as quais têm o mesmo valor do respectivo original, para todos os fins de direito, em juízo ou fora dele, de acordo com o Art. 217 do Código Civil (Lei n°. 10.406/2002) e 161 da Lei de Registros Públicos (Lei 6.015/73). CERTIFICA FINALMENTE QUE: revendo os demais livros de registro, deles verificou não constar outras anotações/averbações, além das relatadas na presente certidão. Nada mais. Dou fé.***************<br<br><br>




__________________________________
[NOME_ESCREVENTE]<br>
[CARGO_FUNCIONARIO] <br>
<br>
<br>
{{CUSTAS}} - Total R$ {{TOTAL_CUSTAS}}

');


INSERT INTO modelo (id,tipo_modelo_id, nome, modelo) VALUES (15,2,'ÔNUS','                                  <b>C E R T I D Ã O</b><br><br>


                 Pedido n°: {{NUMERO_PROTOCOLO}}<br><br>


Certifica, atendendo ao pedido feito por pessoa interessada que revendo neste Serviço, a seu cargo, os livros deste <b>2º Oficial de Registro de Títulos e Documentos da Comarca de Osasco – Estado de São Paulo</b>, verificou <b>NÃO CONSTAR</b> nenhuma alienação, ônus ou penhor, sobre o seguinte bem:<br>
 [OBJETO]

Nada mais. Todo o referido é verdade e dá fé. Osasco, {{DATA_ATUAL_EXTENSO}}. Eu, [NOME_AUXILIAR], Auxiliar de Registro, dei busca e digitei, e eu, [NOME_ESCREVENTE], Escrevente, conferi e assino.<br><br><br>



__________________________________
[NOME_ESCREVENTE]<br>
[CARGO_FUNCIONARIO] <br>
<br>
<br>
{{CUSTAS}} - Total R$ {{TOTAL_CUSTAS}}
');

INSERT INTO modelo (id,tipo_modelo_id, nome, modelo) VALUES (16,2,'POR QUESITO TD','');
INSERT INTO modelo (id,tipo_modelo_id, nome, modelo) VALUES (17,2,'POR QUESITO PJ','');
INSERT INTO modelo (id,tipo_modelo_id, nome, modelo) VALUES (18,2,'CAPA TD','');
INSERT INTO modelo (id,tipo_modelo_id, nome, modelo) VALUES (19,2,'CAPA PJ','');
INSERT INTO modelo (id,tipo_modelo_id, nome, modelo) VALUES (20,2,'NOTIFICAÇÃO CERTIFICADA','');

--delete from modelo where id IN (1,2,30,31,32,33,34,35);

INSERT INTO modelo (id,tipo_modelo_id, nome, modelo) VALUES (1,3,'NOTA DEVOLUÇÃO TD','<b><h3 style=”text-align:center” >NOTA DE DEVOLUÇÃO</b></h3><br><br>

<b>Apresentante: {{apresentante}}</b><br>
<b>Telefone: {{TELEFONE_APRESENTANTE}}</b><br>
<b>Natureza do Título: {{natureza}}</b><br>
<b>Empresa: {{parte}}</b><br><hr width="100%">
Analisado o título, o mesmo é devolvido pelas seguintes razões:<br>

{{checklist}}






Osasco, {{DATA_ATUAL_EXTENSO}}.<br><br>


______________________________
        {{escrevente}}<br><br>




         <b>NÃO TIRE ESTA NOTA – FACILITA O REGISTRO</b>');

INSERT INTO modelo (id,tipo_modelo_id, nome, modelo) VALUES (2,3,'NOTA DEVOLUÇÃO PJ','<b><h3 style=”text-align:center” >NOTA DE DEVOLUÇÃO</b></h3><br><br>
<b>NOME: {{parte}}</b><br>
<b>PASTA: {{pasta_pj}}</b><br>
<b>Data: {{protocolo_data}}</b><br>
<b>Nº de Ordem:{{protocolo_numero}}</b><br><br>

1- Foi apresentado para exame e cálculo a {{natureza}} datada de {{DATA_REGISTRO}};<br><br>

{{checklist}}<br><br>
Ultimo registro: {{REGISTRO_ANTERIOR}}<br>
Base de cálculo: {{CUSTAS}} <br>
Documentos apresentados: <br>
');

--carimbos

INSERT INTO modelo (id,tipo_modelo_id,nome,modelo) VALUES
		   (52,1, 'REGISTRO MANUAL', 'REGISTRO: Certifico que foi apresentado este documento, com {{PAGINAS}} páginas, registrado sob o número {{REGISTRO}} em {{DATA_REGISTRO}} neste 2º Oficial de Registro, Títulos e Documentos e Civil de Pessoa Jurídica de Osasco, possui o mesmo valor probante do original para todos os fins de Direito, seja em Juízo ou fora dele, nos termos dos artigos 161 da Lei n. 6.015/73 e 217 da Lei 10.406/02 e foi extraída sob forma de documento eletrônico devendo para validade ser conservada em meio eletrônico, bem como comprovada a autoria e integridade. Osasco, {{DATA_EXTENSO}}. {{NOME_CARTORIO}}, CNPJ {{CNPJ_CARTORIO}}. Certifico ainda, que a assinatura digital constante neste documento eletrônico está em conformidade com os padrões da ICP-Brasil, nos termos da Lei 11.977 de 07 de julho de 2009. {{CUSTAS}}');

INSERT INTO modelo (id,tipo_modelo_id,nome,modelo) VALUES
		   (53,1, 'REGISTRO ELETRÔNICO', 'REGISTRO ELETRÔNICO: Certifico que foi apresentado este documento, com {{PAGINAS}} páginas, registrado sob o número {{REGISTRO}} em {{DATA_REGISTRO}} neste 2º Oficial de Registro, Títulos e Documentos e Civil de Pessoa Jurídica de Osasco, possui o mesmo valor probante do original para todos os fins de Direito, seja em Juízo ou fora dele, nos termos dos artigos 161 da Lei n. 6.015/73 e 217 da Lei 10.406/02 e foi extraída sob forma de documento eletrônico devendo para validade ser conservada em meio eletrônico, bem como comprovada a autoria e integridade. Osasco, {{DATA_EXTENSO}}. {{NOME_CARTORIO}}, CNPJ {{CNPJ_CARTORIO}}. Certifico ainda, que a assinatura digital constante neste documento eletrônico está em conformidade com os padrões da ICP-Brasil, nos termos da Lei 11.977 de 07 de julho de 2009. {{CUSTAS}}');

INSERT INTO modelo (id,tipo_modelo_id,nome,modelo) VALUES
		   (54,1, 'CARIMBO CERTIDÃO','CERTIDÃO DE REGISTRO: Certifico que a presente certidão é constituída de {{PAGINAS}} páginas e foi extraída do documento registrado sob número {{REGISTRO}} em {{DATA_REGISTRO}} neste 2° Oficial de Registro de Títulos e Documentos e Civil de Pessoa Jurídica de Osasco. Certifico, ainda, que a presente certidão possui o mesmo valor probante do documento original para todos os fins de Direito, seja em Juízo ou fora dele, nos termos dos artigos 161 da Lei n. 6.015/73 e 217 da Lei 10.406/02, tendo sido extraída sob forma de documento eletrônico. Certifico, ainda, que a assinatura digital constante neste documento eletrônico é do 2° Oficial de Registro de Títulos e Documentos e Civil de Pessoa Jurídica de Osasco e está em conformidade com os padrões da ICP-Brasil, nos termos da Medida Provisória nº. 2.200-2, de 24 de agosto de 2001. Osasco, {{DATA_EXTENSO}}. {{NOME_CARTORIO}}, CNPJ {{CNPJ_CARTORIO}}.  Para verificar o documento registrado acesse o site www.2osasco.com.br e digite: Conta: megamix Login: megamix e Senha: mega12 e em Pesquisa de documento digite o número {{REGISTRO}}.');

-- APAGA TABELA depara_natureza



-- 1-CÉDULA DE CRÉDITO
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (1,7,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (1,7,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (1,7,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (1,7,53);

INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (1,8,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (1,8,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (1,8,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (1,8,53);

INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (1,9,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (1,9,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (1,9,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (1,9,53);

INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (1,10,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (1,10,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (1,10,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (1,10,53);

INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (1,11,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (1,11,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (1,11,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (1,11,53);

INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (1,12,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (1,12,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (1,12,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (1,12,53);

INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (1,13,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (1,13,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (1,13,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (1,13,53);

INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (1,14,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (1,14,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (1,14,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (1,14,53);

INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (1,15,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (1,15,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (1,15,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (1,15,53);

INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (1,16,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (1,16,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (1,16,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (1,16,53);

--Alienação Fiduciária
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (3,1,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (3,1,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (3,1,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (3,1,53);

--Anuencia
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,2,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,2,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,2,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,2,53);

--Arrendamento
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,3,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,3,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,3,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,3,53);

--Ata registro
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,4,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,4,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,4,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,4,53);

--Balanço
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,5,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,5,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,5,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,5,53);

--carta
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,6,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,6,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,6,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,6,53);

--certificado digital
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,17,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,17,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,17,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,17,53);

--cessao de direitos
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (3,18,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (3,18,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (3,18,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (3,18,53);

--cessao de direitos em garantia
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,19,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,19,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,19,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,19,53);

--cnh
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,20,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,20,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,20,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,20,53);

--comodato
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,21,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,21,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,21,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,21,53);

--compra e venda
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,22,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,22,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,22,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,22,53);

--COMPROMISSO DE CESSÃO DE DIREITOS
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,23,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,23,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,23,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,23,53);

--COMPROMISSO DE COMPRA E VENDA
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,24,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,24,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,24,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,24,53);

--COMPROMISSO DE PERMUTA
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,25,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,25,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,25,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,25,53);

--CONFISSÃO DE DÍVIDA
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,26,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,26,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,26,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,26,53);

--CONSÓRCIO
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,27,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,27,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,27,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,27,53);

--CONTRATO - OUTROS
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,28,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,28,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,28,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,28,53);

--CONTRATO SOCIAL
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,29,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,29,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,29,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,29,53);

--CONTRATO-PADRÃO
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,30,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,30,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,30,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,30,53);

--CTPS
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,31,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,31,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,31,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,31,53);

--DECLARAÇÃO
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,32,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,32,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,32,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,32,53);

--DIN
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,33,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,33,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,33,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,33,53);

--"DIREITO REAL DE USO"
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,34,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,34,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,34,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,34,53);

--"DOAÇÃO"
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,35,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,35,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,35,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,35,53);

--"DOCUMENTOS - OUTROS"
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (9,36,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (9,36,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (9,36,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (9,36,53);

--"DOCUMENTO ESTUDANTIL"
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,37,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,37,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,37,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,37,53);

--"DOCUMENTO NACIONAL EM IDIOMA ESTRANGEIRO "
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,38,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,38,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,38,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,38,53);

--"DUPLICATA"
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,39,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,39,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,39,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,39,53);

--"FIANÇA/AVAL"
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (3,40,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (3,40,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (3,40,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (3,40,53);

--"LAUDO/VISTORIA"
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,41,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,41,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,41,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,41,53);

--"LETRA DE CRÉDITO IMOBILIÁRIO"
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,42,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,42,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,42,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,42,53);

--"LIVRO"
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,43,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,43,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,43,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,43,53);

--"LOCAÇÃO"
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,44,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,44,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,44,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,44,53);

--"MICROFILME - AUTENTICAÇÃO"
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,45,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,45,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,45,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,45,53);

--"MÚTUO"
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,46,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,46,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,46,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,46,53);

--"NOTA DE CRÉDITO RURAL"
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,47,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,47,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,47,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,47,53);

--"NOTA FISCAL"
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,48,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,48,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,48,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,48,53);

--"NOTA PROMISSÓRIA"
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,49,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,49,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,49,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,49,53);

--"OUTRAS GARANTIAS"
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (3,50,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (3,50,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (3,50,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (3,50,53);

--"OUTROS"
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,51,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,51,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,51,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,51,53);

--"PARCERIA"
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,52,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,52,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,52,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,52,53);

--"PASSAPORTE"
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,53,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,53,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,53,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,53,53);

--"PENHOR"
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,54,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,54,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,54,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,54,53);

--"PERMUTA"
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,55,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,55,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,55,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,55,53);

--"POSSE"
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,56,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,56,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,56,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,56,53);

--"PRESTAÇÃO DE SERVIÇOS"
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,57,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,57,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,57,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,57,53);

--"PROCURAÇÃO"
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (9,58,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (9,58,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (9,58,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (9,58,53);

--"RH"
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,59,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,59,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,59,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,59,53);
--"SEGURO"
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,60,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,60,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,60,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,60,53);
--"SOCIEDADE EM CONTA DE PARTICIPAÇÃO"
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,61,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,61,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,61,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,61,53);

--"SUBSTABELECIMENTO - REGISTRO"
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (9,62,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (9,62,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (9,62,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (9,62,53);

--"TESTAMENTO"
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,63,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,63,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,63,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,63,53);

--"TRANSAÇÃO"
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,64,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,64,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,64,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,64,53);

--"USUFRUTO"
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,65,1);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,65,2);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,65,52);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (29,65,53);

--CERTIDOES BREVE RELATO
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (21,null,4);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (21,null,54);

INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (21,null,5);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (21,null,54);

--BUSCAS
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (27,null,6);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (27,null,54);

INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (27,null,7);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (27,null,54);

INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (27,null,8);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (27,null,54);

--CERTIDOES INTEIRO TEOR
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (26,null,14);--modelo de textoo caso tenha
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (26,null,54);--carimbo de CERTIDAO

--DEMAIS CERTIDOES
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (22,null,9);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (22,null,10);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (22,null,11);
--INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (22,null,12);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (22,null,13);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (22,null,15);
INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (22,null,54);

INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (23,null,54);

INSERT INTO modelo_natureza_subnatureza (natureza_id, sub_natureza_id, modelo_id) VALUES (24,null,54);
