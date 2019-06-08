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
        ${notaDevolucao}
    </article>
</section>
</body>
</html>
<script>
<!-- window.print();
setTimeout(function () { window.close(); }, 100); -->
</script>