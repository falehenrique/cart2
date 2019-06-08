<!DOCTYPE html>
<html lang="pt">

<head>
    <meta charset="utf-8">
    <title>A4</title>

    <!-- Normalize or reset CSS with your favorite library -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/normalize/3.0.3/normalize.css">
    <link href="https://fonts.googleapis.com/css?family=Roboto:300,400" rel="stylesheet">
    <!-- Load paper.css for happy printing -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/paper-css/0.2.3/paper.css">

    <!-- Set page size here: A5, A4 or A3 -->
    <!-- Set also "landscape" if you need -->
    <style>@page { size: A4 }
        body {
            font-family: 'Roboto', sans-serif;
            font-weight: lighter;
            font-size: small;
            <!--font-weight: 10px;-->
        }
        .cabecalho, th {
            text-align: right;
        }
        .cellpadding {
           padding-left: 10px;
           padding-right: 10px;
           padding-top: 5px;
           padding-bottom: 5px;
        }
        .linhapadding {
           padding-left: 15px;

        }
        .linhaVertical {
            width: 1px;
            background-color:black;
        }
         .invisivel {
            visibility: hidden
         }

    </style>
</head>

<!-- Set "A5", "A4" or "A3" for class name -->
<!-- Set also "landscape" if you need -->
<body class="A4">

<!-- Each sheet element should have the class "sheet" -->
<!-- "padding-**mm" is optional: you can set 10, 15, 20 or 25 -->
<section class="sheet padding-10mm">

    <!-- Write HTML just like a web page -->
    <article>
    <#list quantidadeVias as x >
        <table width="100%" class="cabecalho">
            <tr>
                <th rowspan="3" style="width: 40%;"><img src="/imagens/logo_2osasco_290px.png"></th>
                <td><strong>Rua Dante Battiston, 249 - Centro -Fone:(11) 3215-6400</strong></td>
            </tr>
            <tr>
                <td>Atendimento de segunda a sexta-feira das 09:00 às 16:00 horas</td>
            </tr>
            <tr>
                <td style="font-size: large;"><#if protocolo.tipo?starts_with("CERTIDAO") == false> <#assign num = protocolo.numeroRegistro?number> Registro nº <strong>${num?string("00000000")}<#else> Protocolo nº <strong>${protocolo.numero?string("00000000")}</strong></#if> - ${tipoProtocolo}</td>
            </tr>
        </table>
        <p></p>
        <table style="width: 100%; float: left;" cellspacing="0">
            <tbody>
            <tr>
                <td>Parte: <strong><#if protocolo.parte??>${protocolo.parte}<#else> Não informado</#if></strong></td>
                <td class="linhaVertical"></td>
                <td class="linhapadding">Natureza: <strong>${protocolo.natureza.nome}</strong></td>
            </tr>
            <tr>
                <td>CPF/CNPJ:  <strong><#if protocolo.parteDocumento??>${protocolo.parteDocumento}<#else>Não informado</#if></strong></td>
                <td class="linhaVertical"></td>
                <td class="linhapadding">Data da Solicita&ccedil;&atilde;o: <strong>${protocolo.dataProtocolo.format('dd/MM/yyyy')}</strong></td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td class="linhaVertical"></td>
                <td class="linhapadding">Protocolo nº <strong>${protocolo.numero?string("00000000")}</strong></td>
            </tr>
            <tr>
                <td>Apresentante: <strong>${protocolo.apresentante}</strong></td>
                <td class="linhaVertical"></td>
                <td class="linhapadding">Data de Registro: <strong>${protocolo.dtRegistro.format('dd/MM/yyyy')}</strong></td>
            </tr>
            <tr>
                <td>Responsável: <#if protocolo.responsavel??><strong>${protocolo.responsavel}</strong></#if></strong></td>
                <td class="linhaVertical"></td>
                <td class="linhapadding">
                <#if protocolo.tipo?starts_with("CERTIDAO") == false>
                Vencimento da prenota&ccedil;&atilde;o: <strong>${protocolo.dataVencimento.format('dd/MM/yyyy')}</strong>
                </#if>
                </td>
            </tr>
            <tr>
                <td>RG: <#if protocolo.apresentanteRg??><strong>${protocolo.apresentanteRg}</strong></#if></strong></td>
                <td class="linhaVertical"></td>
                <td class="linhapadding"></td>
            </tr>
            <tr>
                <td>Email: <strong><#if protocolo.email??>${protocolo.email}</#if></strong></td>
                <td class="linhaVertical"></td>
                <td class="linhapadding" rowspan="3">
                    <table class="custas" style="width: 95%; border-color: black; border: 1px solid black;">
                        <tbody>
                            <#list custas as custa>
                                 <tr>
                                     <td class="cellpadding">${custa.nome}</td>
                                     <td class="cellpadding" style="text-align: right;">R$ ${custa.valor?string("0.00")}</td>
                                 </tr>
                             </#list>

                            <td class="cellpadding"><strong>Total</strong></td>
                            <td class="cellpadding" style="text-align: right;"><strong>R$ ${saldo?string("0.00")}</strong></td>
                        </tbody>
                     </table>
                 </td>
            </tr>
            <tr>
                <td>Telefone: <strong><#if protocolo.telefone??>${protocolo.telefone}</#if></strong></td>
                <td class="linhaVertical"></td>
            </tr>
            <tr>
                <td style="width: 50%; font-size: smaller;"><br />
                    Obs: <strong><#if protocolo.observacoes?? >${protocolo.observacoes}</#if><br /><br /></strong>
                    <table class="custas invisivel" style="font-size: smaller; width: 95%; border-color: black; border: 1px solid black;">
                        <tbody>
                        <tr>
                            <td class="cellpadding">O Apresentante dever&aacute; comparecer a esta Serventia, a partir de 10 dias &uacute;teis, a contar da data de apresenta&ccedil;&atilde;o deste, a fim &nbsp;de inteirar sobre a validade e legalidade do titulo. Se n&atilde;o houver exig&ecirc;ncia a ser satisfeita, o interessado poder&aacute; retirar o titulo a partir de 30 dias a contar da data de apresenta&ccedil;&atilde;o, munido deste recibo</td>
                        </tr>
                        </tbody>
                    </table>
                </td>
                <td class="linhaVertical"></td>

            </tr>
            <tr>
                <td>&nbsp;</td>
                <td class="linhaVertical"></td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td style="text-align: left; font-size: small;">&nbsp;Data Retirada:_______/_________/______</td>
                <td class="linhaVertical"></td>
                <td class="linhapadding">
                    <table style="width: 100%; font-size: small;" cellspacing="0" cellpadding="0">
                        <tbody>
                        <tr>
                            <td></td>

<#--
                            <td>Dep&oacute;sito pr&eacute;vio:</td>
                            <td><#if totalPagamento?? ><strong>${totalPagamento}</strong> </#if></td>
 -->
                        </tr>
                        <tr>
  <#--
                            <td><#if labelSaldo?? >R$ ${labelSaldo}</#if></td>
                            <td><#if saldo?? ><strong>${saldo}</strong></#if></td>
  -->
                        </tr>
                        </tbody>
                    </table>
                </td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td class="linhaVertical"></td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td style="text-align: center;">____________________________________<br />Assinatura do Retirante</td>
                <td class="linhaVertical"></td>
                <td style="text-align: center;">&nbsp;____________________________________<br />Assinatura do&nbsp;Atendente</td>
            </tr>
            </tbody>
        </table>
        <#if x?is_last?c == 'false'>
            <p>&nbsp;</p>
            <hr>
        </#if>
        </#list>
    </article>
</section>
</body>
</html>
<script>
<!-- window.print();
setTimeout(function () { window.close(); }, 100); -->
</script>