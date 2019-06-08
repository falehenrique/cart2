package br.com.exmart.rtdpjlite.service;

import br.com.exmart.rtdpjlite.BaseTest;
import br.com.exmart.rtdpjlite.exception.CustaNotPresentException;
import br.com.exmart.rtdpjlite.model.*;
import br.com.exmart.rtdpjlite.repository.*;
import br.com.exmart.rtdpjlite.service.enums.Diretorio;
import br.com.exmart.rtdpjlite.util.RetornoSelo;
import br.com.exmart.rtdpjlite.vo.ObjetoProtocoloAtributoVO;
import br.com.exmart.rtdpjlite.vo.ObjetoProtocoloVO;
import br.com.exmart.rtdpjlite.vo.StatusProtocoloJson;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProtocoloServiceTest extends BaseTest{
	@Autowired ProtocoloService protocoloService;
	@Autowired ProtocoloRepository protocoloRepository;
	@Autowired NaturezaRepository naturezaRepository;
	@Autowired StatusRepository statusRepository;
	@Autowired ParteRepository parteRepository;
	@Autowired FormaPagamentoService formaPagamentoService;
	@Autowired FormaCalculoService formaCalculoService;
	@Autowired ItemTabelaRepository itemTabelaRepository;
	@Autowired QualidadeService qualidadeService;
	@Autowired FormaEntregaService formaEntregaService;
	@Autowired TipoDocumentoService tipoDocumentoService;
	@Autowired TipoLogradouroService tipoLogradouroService;
	@Autowired ClienteService clienteService;
	@Autowired SubNaturezaService subNaturezaService;
	@Autowired PDFService pdfService;
	@Autowired ArquivoService arquivoService;
	@Autowired ConfiguracaoService configuracaoService;
	@Autowired UsuarioService usuarioService;
	@Autowired ProtocoloArquivoHashRepository protocoloArquivoHashRepository;
	@Autowired ObjetoService objetoService;


	@Value("${URL_PDF_REGISTRO_TESTE}")
	private Resource URL_PDF_REGISTRO_TESTE;

	@Value("${URL_PDF_REGISTRO_TESTE2}")
	private Resource URL_PDF_REGISTRO_TESTE2;

    @Test
	public void A_whenCreateProtocolo_returnsProtocolo() throws Exception {
		Natureza natureza = naturezaRepository.findOne(1l);
		SubNatureza subNatureza = natureza.getSubNaturezas().iterator().next();
		getLogger().info("-----> " + natureza.toString());
		Parte parte = new Parte();
		parte.setNome("Nome do fulano");
		parte.setTipoDocumento(tipoDocumentoService.findAll().get(0));
		parte.setCpfCnpj("111.111.111-11");
		parte.setNmOrgaoExpedidorDocumento("SSP/SP");
		parte.setDtEmissaoDocumento(LocalDate.now());

		Integer vias = 1;
		Money valor = Money.of(CurrencyUnit.of("BRL"),new BigDecimal(1600f));
		LocalDate data_doc = LocalDate.now();
		LocalDate data_prevista = LocalDate.now();
		LocalDate data_vencimento = LocalDate.now();

		String pastaPj = "0001";
		String responsavel = "Nome Responsável";
		String apresentante = "Nome Apresentante";
		String telefone = "(11) 1234-5678";
		String email ="email@dominio.com";
		String observacoes = "Observações do protocolo";
	
		Protocolo protocolo = new Protocolo();
		protocolo.setNatureza(natureza);
		protocolo.setSubNatureza(subNatureza);
		protocolo.setTipo(TipoProtocolo.CERTIDAO_PJ);
		Set<ParteProtocolo> partes = new HashSet<>();
		partes.add(new ParteProtocolo(parte));
		protocolo.setVias(vias);
		protocolo.setValor(valor);
		protocolo.setParte("Nome da Parte");
		protocolo.setParteDocumento("785.535.741-64");
		protocolo.setDataDoc(data_doc);
		protocolo.setDataPrevista(data_prevista);
		protocolo.setDataVencimento(data_vencimento);
		protocolo.setPastaPJ(pastaPj);
		protocolo.setResponsavel(responsavel);
		protocolo.setFormaEntrega(formaEntregaService.findAll().get(0));
		protocolo.setApresentante(apresentante);
		protocolo.setTelefone(telefone);
		protocolo.setEmail(email);
		protocolo.setObservacoes(observacoes);
		
		//Lista de servicos do protocolo
		FormaCalculo integral = formaCalculoService.findOne(1L);
		Money zero = Money.of(CurrencyUnit.of("BRL"),new BigDecimal(1600f));

		getLogger().info("Salvar protocolo CERTIDAO_PJ -----> ");
		Protocolo _protocolo = protocoloService.save(protocolo, ProtocoloService.ACAO.SALVAR, null, false, false, null, usuarioService.findById(1L));
		assertThat(_protocolo.getId()).isGreaterThan(0);
//		assertThat(_protocolo.getServicosProtocolo().size()).isGreaterThan(0);
		assertThat(_protocolo.getNumero()).isGreaterThanOrEqualTo(6000);
		assertThat(_protocolo.getTipo()).isEqualTo(TipoProtocolo.CERTIDAO_PJ);

		getLogger().info("Custas do protocolo:");
//		List<Custa> custas = protocolo.getCustasTotal();
//		custas.forEach(custa->{
//			getLogger().info("-----> " + custa.getCustaTitulo() + " : " + custa.getCustaFormatted());
//		});
		
		getLogger().info("-----> " + _protocolo.toString());
	}

	
	@Test
	public void E_whenFindServicoByItemFaixa_returnsServicoProtocolo() throws CustaNotPresentException {
		getLogger().info("E_whenFindServicoByItem_returnsServicoProtocolo");
		FormaCalculo integral = formaCalculoService.findOne(1L);
		Money valorInformado = Money.of(CurrencyUnit.of("BRL"),new BigDecimal(1600f));
	}
	
@Test
	public void F_whenFindServicoByItem_returnsServicoProtocolo() throws CustaNotPresentException {
		getLogger().info("E_whenFindServicoByItem_returnsServicoProtocolo");
		FormaCalculo integral = formaCalculoService.findOne(1L);
		Money valorInformado = Money.of(CurrencyUnit.of("BRL"),new BigDecimal(1600f));
	}

@Test
	public void G_whenFindByNumeroAndTipoDoNotExist_returnsFalse() throws Exception {
		Cliente novoCliente = new Cliente();
		novoCliente.setNome("Cliente 1");
		novoCliente.setIdentificacao("1");
		novoCliente.setDocumento("111.222.333-96");
		novoCliente.setEmail("email@email.com");
		novoCliente.setNomeFantasia("Nome Fantasia");
		clienteService.save(novoCliente);
		getLogger().info("G_whenFindByNumeroAndTipoDoNotExist_returnsFalse");
		Qualidade qualidade = qualidadeService.findAll().get(0);
 		Protocolo protocolo = saveProtocolo(TipoProtocolo.EXAMECALCULO_PJ, null, qualidade,novoCliente, null);

		List<Protocolo> retornoFalse = protocoloService.findByNumeroAndTipoProtocoloIn(protocolo.getNumero(), Arrays.asList(TipoProtocolo.EXAMECALCULO_TD));
		assertThat(retornoFalse.size()).isEqualTo(0);

		List<Protocolo> retornoTrue = protocoloService.findByNumeroAndTipoProtocoloIn(protocolo.getNumero(), Arrays.asList(TipoProtocolo.EXAMECALCULO_PJ));
		assertThat(retornoTrue.size()).isEqualTo(1);

		LocalDateTime hoje = LocalDateTime.now();
		hoje = hoje.minusDays(101);
		Cliente novoCliente2 = new Cliente();
		novoCliente2.setNome("Cliente 2");
		novoCliente2.setIdentificacao("2");
		novoCliente2.setDocumento("111.222.333-96");
		novoCliente2.setEmail("email@email.com");
		novoCliente2.setNomeFantasia("Nome Fantasia");
		clienteService.save(novoCliente2);
		for(int x=0; x<50; x++){
			hoje = hoje.plusDays(1);
			saveProtocolo(TipoProtocolo.PRENOTACAO_TD,hoje , qualidade, novoCliente2, null);
		}
	}

@Test
	public void H_whenFindByNumeroAndTipoDoNotExist_returnsFalse() {
		protocoloService.findByNumero(12313L);
	}

	private Protocolo saveProtocolo(TipoProtocolo tipo, LocalDateTime dtProtocolo, Qualidade qualidade, Cliente cliente, InputStream arquivo) throws Exception {
		if(dtProtocolo == null){
			dtProtocolo = LocalDateTime.now();
		}
		Natureza natureza = naturezaRepository.findAll().get(0);
		getLogger().info("-----> " + natureza.toString());
		Parte parte = new Parte();
		parte.setNome("Nome do fulano");
		parte.setEmail("Email@email.com");
		parte.setBairro("bairro");


		parte.setTipoDocumento(tipoDocumentoService.findAll().get(0));
		parte.setCpfCnpj("111.111.111-11");
		parte.setNmOrgaoExpedidorDocumento("SSP/SP");
		parte.setDtEmissaoDocumento(LocalDate.now());





		Integer vias = 1;
		Money valor = Money.of(CurrencyUnit.of("BRL"),new BigDecimal(1600f));
		LocalDate data_doc = LocalDate.now();
		LocalDate data_prevista = LocalDate.now();
		LocalDate data_vencimento = LocalDate.now();
		String pastaPj = "0001";
		String responsavel = "Nome Responsável";
		String apresentante = "Nome Apresentante";
		String telefone = "(11) 1234-5678";
		String email ="email@dominio.com";
		String observacoes = "Observações do protocolo";

		Protocolo protocolo = new Protocolo();
		
		protocolo.setNatureza(natureza);

		ObjetoProtocoloVO objetosProtocolo = new ObjetoProtocoloVO();
		Objeto obj = objetoService.findByNome("Veículo automotor terrestre");
		if(obj != null){
			objetosProtocolo.setId(obj.getId());
			objetosProtocolo.setNome(obj.getNome());
			for(ObjetoAtributo att : obj.getAtributos()){
				if (att.getNome().equalsIgnoreCase("RENAVAN")){
					ObjetoProtocoloAtributoVO atributoVO = new ObjetoProtocoloAtributoVO();
					atributoVO.setId(att.getId());
					atributoVO.setNome(att.getNome());
					atributoVO.setValor("123456");
					objetosProtocolo.setAtributos(Arrays.asList(atributoVO));
					break;
				}
			}

		}
		protocolo.setObjetos(Arrays.asList(objetosProtocolo));

		protocolo.setSubNatureza(natureza.getSubNaturezas().iterator().next());
		protocolo.setTipo(tipo);
		Set<ParteProtocolo> partes = new HashSet<>();
		partes.add(new ParteProtocolo(parte, qualidade));
		protocolo.setPartesProtocolo(partes);
		protocolo.setVias(vias);
		protocolo.setArquivo(arquivo);
		protocolo.setValor(valor);

		protocolo.setParte("Nome da Parte");
		protocolo.setParteDocumento("785.535.741-64");

		protocolo.setDataDoc(data_doc);
		protocolo.setDataPrevista(data_prevista);
		protocolo.setDataVencimento(data_vencimento);
		protocolo.setPastaPJ(pastaPj);
		protocolo.setResponsavel(responsavel);
		protocolo.setApresentante(apresentante);
		protocolo.setDataProtocolo(dtProtocolo);
		protocolo.setFormaEntrega(formaEntregaService.findAll().get(0));
		protocolo.setTelefone(telefone);
		protocolo.setEmail(email);
		protocolo.setObservacoes(observacoes);
		protocolo.setCliente(cliente);
		protocolo.setTextoCertidao("Texto Certidao");
		protocolo.setTextoCarimbo("Texto Carimbo");


		getLogger().info("Salvar protocolo CERTIDAO_PJ -----> ");
		protocolo = protocoloService.save(protocolo, ProtocoloService.ACAO.SALVAR, null, false, false, null, usuarioService.findById(1L));
		return protocolo;
	}

@Test
	public void Z_register() throws Exception {

	//FIXME Fabio Ebner Alterar com o objeto do selo
	RetornoSelo retSelo = new RetornoSelo(
			"urlTribunalJustica",
			"Selo Digital 1137614TICD000004440EA183",
			"total",
			"iss",
			"assinaturaDigital",
			"https://selodigital.tjsp.jus.br/?r=1234561AB123456789012318I%7C1000%7C50%7CyYVHtIzr7snVc\n" +
					"0dDAbLVEe9ko7L4o40m6wpRrJqOcWWDQyg8NrXTt%2BRlWpBCafAtnjaAKmf52kZq8PKt9m8hB6\n" +
					"GveKA8WbCFQoX4YOUskthcwH1Ek63fdgME2Eiy1da1TqL4NH7G3PShvEwtPFhWLXI%2Bv%2B\n" +
					"wJxbJPobIJ4tyM4FI6jfT0cEaycLAQ0TwZhSSk%2FP%2BAw40K%2FLl%2FB6hSjR9AokWDdRsSZ\n" +
					"GAx%2BD16WlpF2Tj9JgAQi4lA5f6e9IWR3d4qHFPL1N628x8d5zQVt%2Fg%2F%2FqjSz4VEhYIoi\n" +
					"UWJnRzh4zDYhMDX96zzayYCo5wDChSNBnOvuzTgFD%2B02KlKtJDy9g%3D%3D",
			"Para verificar a autenticidade do documento, acesse o site da Corregedoria Geral da Justiça: https://selodigital.tjsp.jus.br.",
			null
	);


	Qualidade qualidade = qualidadeService.findAll().get(0);
		Protocolo protocoloOld = saveProtocolo(TipoProtocolo.PRENOTACAO_TD, null, qualidade,clienteService.findOne(1L), new FileInputStream(URL_PDF_REGISTRO_TESTE.getFile()));
		Long id = protocoloOld.getId().longValue();
		Protocolo protocolo = protocoloService.findById(id);
		protocolo.setDtRegistro(LocalDateTime.now());
		ProtocoloArquivoHash hashArquivo = new ProtocoloArquivoHash(protocolo.getId(), null, "LUMERA_TESTE");
		protocoloArquivoHashRepository.save(hashArquivo);
		pdfService.converteAssinaPdf(arquivoService.recuperarDocumentoProtocoloFile(protocolo.getNumero(),TipoProtocolo.recuperaEspecialidade(protocolo.getTipo()),protocolo.getTipo())
				,
				arquivoService.recuperarPasta(protocolo.getNumero(), TipoProtocolo.recuperaEspecialidade(protocolo.getTipo()) ,Diretorio.PROTOCOLO, Diretorio.PROTOCOLO, protocolo.getTipo()),
				protocolo.getTextoCarimbo(),
				protocolo.getTextoCertidao(),
				configuracaoService.findConfiguracao().getTxtExplicativoAssinatura().replace("<<URL_VALIDACAO>>", "%%%"+configuracaoService.findConfiguracao().getUrlQrcode()+"/documento/"+hashArquivo+"%%%"),
				configuracaoService.findConfiguracao().getUrlQrcode()+"/documento/",
				hashArquivo.getHash(),
				PDFService.DisposicaoPagina.ULTIMA_PAGINA,
				null, null,
				protocolo.getNatureza().isIcCertidaoDigital(),
				retSelo);


		StatusProtocoloJson statusJson= new StatusProtocoloJson();
		statusJson.setTexto("ALGO 2 [[NUMERO_REGISTRO]]");
		protocoloService.save(protocolo, ProtocoloService.ACAO.REGISTRAR, new ArrayList<>(), false, false, statusJson, usuarioService.findById(1L));
	}


@Test
	public void Z_register2() throws Exception {

	//FIXME Fabio Ebner Alterar com o objeto do selo
	RetornoSelo retSelo = new RetornoSelo(
			"urlTribunalJustica",
			"Selo Digital 1137614TICD000004440EA183",
			"total",
			"iss",
			"assinaturaDigital",
			"https://selodigital.tjsp.jus.br/?r=1234561AB123456789012318I%7C1000%7C50%7CyYVHtIzr7snVc\n" +
					"0dDAbLVEe9ko7L4o40m6wpRrJqOcWWDQyg8NrXTt%2BRlWpBCafAtnjaAKmf52kZq8PKt9m8hB6\n" +
					"GveKA8WbCFQoX4YOUskthcwH1Ek63fdgME2Eiy1da1TqL4NH7G3PShvEwtPFhWLXI%2Bv%2B\n" +
					"wJxbJPobIJ4tyM4FI6jfT0cEaycLAQ0TwZhSSk%2FP%2BAw40K%2FLl%2FB6hSjR9AokWDdRsSZ\n" +
					"GAx%2BD16WlpF2Tj9JgAQi4lA5f6e9IWR3d4qHFPL1N628x8d5zQVt%2Fg%2F%2FqjSz4VEhYIoi\n" +
					"UWJnRzh4zDYhMDX96zzayYCo5wDChSNBnOvuzTgFD%2B02KlKtJDy9g%3D%3D",
			"Para verificar a autenticidade do documento, acesse o site da Corregedoria Geral da Justiça: https://selodigital.tjsp.jus.br.",
			null
	);


	Qualidade qualidade = qualidadeService.findAll().get(0);
		Protocolo protocoloOld = saveProtocolo(TipoProtocolo.PRENOTACAO_TD, null, qualidade,clienteService.findOne(1L), Files.newInputStream(URL_PDF_REGISTRO_TESTE2.getFile().toPath()));
		Long id = protocoloOld.getId().longValue();
		Protocolo protocolo = protocoloService.findById(id);
		protocolo.setDtRegistro(LocalDateTime.now());
		ProtocoloArquivoHash hashArquivo = new ProtocoloArquivoHash(protocolo.getId(), null, "LUMERA_TESTE1");
		protocoloArquivoHashRepository.save(hashArquivo);
	    pdfService.converteAssinaPdf(arquivoService.recuperarDocumentoProtocoloFile(protocolo.getNumero(),TipoProtocolo.recuperaEspecialidade(protocolo.getTipo()),protocolo.getTipo())
				,
				arquivoService.recuperarPasta(protocolo.getNumero(), TipoProtocolo.recuperaEspecialidade(protocolo.getTipo()) ,Diretorio.PROTOCOLO, Diretorio.PROTOCOLO, protocolo.getTipo()),
				protocolo.getTextoCarimbo(),
				protocolo.getTextoCertidao(),
				configuracaoService.findConfiguracao().getTxtExplicativoAssinatura().replace("<<URL_VALIDACAO>>", "%%%"+configuracaoService.findConfiguracao().getUrlQrcode()+"/documento/"+hashArquivo+"%%%"),
				configuracaoService.findConfiguracao().getUrlQrcode()+"/documento/",
				hashArquivo.getHash(),
				PDFService.DisposicaoPagina.ULTIMA_PAGINA,
				null, null, protocolo.getNatureza().isIcCertidaoDigital(),
				retSelo);


		StatusProtocoloJson statusJson= new StatusProtocoloJson();
		statusJson.setTexto("ALGO [[NUMERO_REGISTRO]]");
		protocoloService.save(protocolo, ProtocoloService.ACAO.REGISTRAR, new ArrayList<>(), false, false, statusJson, usuarioService.findById(1L));
	}
@Test
	public void Z_register3() throws Exception {

	//FIXME Fabio Ebner Alterar com o objeto do selo
	RetornoSelo retSelo = new RetornoSelo(
			"urlTribunalJustica",
			"Selo Digital 1137614TICD000004440EA183",
			"total",
			"iss",
			"assinaturaDigital",
			"https://selodigital.tjsp.jus.br/?r=1234561AB123456789012318I%7C1000%7C50%7CyYVHtIzr7snVc\n" +
					"0dDAbLVEe9ko7L4o40m6wpRrJqOcWWDQyg8NrXTt%2BRlWpBCafAtnjaAKmf52kZq8PKt9m8hB6\n" +
					"GveKA8WbCFQoX4YOUskthcwH1Ek63fdgME2Eiy1da1TqL4NH7G3PShvEwtPFhWLXI%2Bv%2B\n" +
					"wJxbJPobIJ4tyM4FI6jfT0cEaycLAQ0TwZhSSk%2FP%2BAw40K%2FLl%2FB6hSjR9AokWDdRsSZ\n" +
					"GAx%2BD16WlpF2Tj9JgAQi4lA5f6e9IWR3d4qHFPL1N628x8d5zQVt%2Fg%2F%2FqjSz4VEhYIoi\n" +
					"UWJnRzh4zDYhMDX96zzayYCo5wDChSNBnOvuzTgFD%2B02KlKtJDy9g%3D%3D",
			"Para verificar a autenticidade do documento, acesse o site da Corregedoria Geral da Justiça: https://selodigital.tjsp.jus.br.",
			null
	);


	Qualidade qualidade = qualidadeService.findAll().get(0);//
		Protocolo protocoloOld = saveProtocolo(TipoProtocolo.PRENOTACAO_TD, null, qualidade,clienteService.findOne(1L), new FileInputStream((URL_PDF_REGISTRO_TESTE.getFile())));
		Long id = protocoloOld.getId().longValue();
		Protocolo protocolo = protocoloService.findById(id);

		ProtocoloArquivoHash hashArquivo = new ProtocoloArquivoHash(protocolo.getId(), null, "LUMERA_TESTE2");
		protocoloArquivoHashRepository.save(hashArquivo);
	    pdfService.converteAssinaPdf(arquivoService.recuperarDocumentoProtocoloFile(protocolo.getNumero(),TipoProtocolo.recuperaEspecialidade(protocolo.getTipo()),protocolo.getTipo())
				,
				arquivoService.recuperarPasta(protocolo.getNumero(), TipoProtocolo.recuperaEspecialidade(protocolo.getTipo()) ,Diretorio.PROTOCOLO, Diretorio.PROTOCOLO, protocolo.getTipo()),
				protocolo.getTextoCarimbo(),
				protocolo.getTextoCertidao(),
				configuracaoService.findConfiguracao().getTxtExplicativoAssinatura().replace("<<URL_VALIDACAO>>", "%%%"+configuracaoService.findConfiguracao().getUrlQrcode()+"/documento/"+hashArquivo+"%%%"),
				configuracaoService.findConfiguracao().getUrlQrcode()+"/documento/",
				hashArquivo.getHash(),
				PDFService.DisposicaoPagina.ULTIMA_PAGINA,
				null, null, protocolo.getNatureza().isIcCertidaoDigital(),
				retSelo);


		StatusProtocoloJson statusJson= new StatusProtocoloJson();
		statusJson.setTexto("ALGO [[NUMERO_REGISTRO]]");
		protocoloService.save(protocolo, ProtocoloService.ACAO.REGISTRAR, new ArrayList<>(), false, false, statusJson, usuarioService.findById(1L));
	}

//@Test
	public void Z_testeImport(){
//		Cliente cliente = clienteService.findOne(1330L);
//		protocoloService.importarProtocolos(new File("/Users/fabioebner/Downloads/Importacao_planilha_osasco/IndiceSetembro.txt"), cliente);
//		protocoloService.importarProtocolos(new File("/Users/fabioebner/Downloads/Importacao_planilha_osasco_completa/Indice Setembro .txt"), cliente);
	}
	
}
