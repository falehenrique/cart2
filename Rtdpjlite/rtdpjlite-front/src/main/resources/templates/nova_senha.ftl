<!DOCTYPE html>
<html>
	<head>
		<style type="text/css">
			table {
			  position: absolute;
			  top: 50%;
			  left: 50%;
			  transform: translate(-50%, -50%);
			  width: 600px;
			  background-color: white;
			  border-collapse: collapse;
			}

			td {
			  border: 1px solid #999999;
			}
		</style>
	</head>
	<body style="background-color: #EEEEEE; ">
		<table style="border-collapse: collapse; " border="0" >
			<tbody>
				<tr style="background-color: #42A5F5; height: 65px">
					<td style="padding: 15px; width: 100%; color: white"><strong>Nova Senha</strong></td>
				</tr>
				<tr>
					<td style="width: 100%; padding-left: 15px;">
						<p>Ol&aacute; ${login},</p>
						<p>Segue nova senha para acesso, lembre-se de alterar a mesma apos efetuar o login</p>
						<p> Senha: ${senha}
					</td>
				</tr>
				<tr>
					<td style="width: 100%; padding-left: 15px;">
						<p>Qualquer dificuldade entre em contato com o nosso suporte: <a href="mailto:suporte@lumera.com.br">suporte@lumera.com.br</a></p>
						<p>Att,<br />Equipe ${cartorio}</p>
					</td>
				</tr>
			</tbody>
		</table>
	</body>
</html>