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
                text-align: center;
            }
            .rodape {
                text-align: center;
                position:absolute;
                bottom:20px;
                width:100%;
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
            .linhaHorizontal {
                width: 1px;
                background-color:black;
            }
            .textoJustificado {
                text-align: justify;
            }
            .textcentralizado {
                text-align: center;
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
                <table width="100%" class="cabecalho">
                    <tr>
                        <th rowspan="1" style="width: 100%;"><img src="/imagens/logo_2osasco_290px.png"></th>
                    </tr>
                </table>
                <p></p>

                <br>
                <br>
                <br>
                <br>
                <br>
                <br>
                <br>
                <br>
                <br>
                <br>
                <br>
                <br>
                <br>
                <br>
                <br>
                <br>
                <br>



                <div class="textoJustificado">


                <#if registro.especialidade?starts_with("TD") == true>
                    <p>         Certifico que o presente título foi protocolado sob nº <b>${nr_protocolo}</b>,
                    e é constituído de ${folhaLivroExt} folha(s) e da certidão que encerra o registro nº <b>${registro.numeroRegistro}, </b>Livro <b>B</b>
                    realizado no dia <b>${dataRegistroExt}</b>, neste Segundo Registro de Títulos e Documentos de Osasco.</p>
                </#if>
                <#if registro.especialidade?starts_with("PJ") == true>
                    <p>         Certifico que o presente título foi protocolado sob nº <b>${nr_protocolo}</b>,
                    e é constituído de ${folhaLivroExt} folha(s) e da certidão que encerra o registro nº <b>${registro.numeroRegistro}, </b>Livro <b>A</b>
                    realizado no dia <b>${dataRegistroExt}</b>, neste Segundo Registro de Títulos e Documentos de Osasco.</p>
                </#if>

                    <br>
                    <br>
                    <br>
                    <br>
                    <br>
                    <br>
                    <br>
                </div>

                <div width="100%" class="textcentralizado">
                    <p">Osasco, ${dataRegistroExt}.</p>
                    <br>
                    <br>
                    <br>
                    <br>
                    <br>
                    <br>
                    <th rowspan="1" style="width: 100%;"><img src="/imagens/assinatura_recibo.png"></th>
                    <p>________________________________________________________</p>
                    <p>OFICIAL - SUBSTITUTO - ESCREVENTE</p>

                <br>
                <br>
                <br>
                <br>
                <br>
                <br>

                <table >
                    <tr colspan="2" width="100%">
                        <td width="70%" class="textoJustificado">A integridade deste documento poderá ser verificada no <b>${validaUrl}</b>.
                            HASH: <b>${hash}</b>
                            <br>
                            <p style="font-size: large;">CNT <strong>${registro.registro}</strong></p>
                        </td>
                        <td width="30%">
                            <div id="qrcode"></div>
                            <img src="https://api.qrserver.com/v1/create-qr-code/?size=115x115&data=${hashCompleto}">
                        </td>
                    </tr>
                <table>






                <div width="100%" class="rodape">
                    <div>
                        <div>Atendimento de segunda a sexta-feira das 09:00 às 16:00 horas</div>
                    </div>
                    <div>
                        <div><strong>Rua Dante Battiston, 249 - Centro -Fone:(11) 3215-6400</strong></div>
                    </div>
                </div>

            </article>
        </section>
    </body>
</html>
<script>
</script>