package br.com.exmart.rtdpjlite.service;

import br.com.exmart.rtdpjlite.exception.ErrorListException;
import br.com.exmart.rtdpjlite.exception.NotUseSelo;
import br.com.exmart.rtdpjlite.model.*;
import br.com.exmart.rtdpjlite.model.portal.PedidoStatus;
import br.com.exmart.rtdpjlite.repository.*;
import br.com.exmart.rtdpjlite.service.enums.Diretorio;
import br.com.exmart.rtdpjlite.service.filter.ProtocoloFiltro;
import br.com.exmart.rtdpjlite.service.rest.balcaoonline.PedidoService;
import br.com.exmart.rtdpjlite.service.rest.fonetica.FoneticaService;
import br.com.exmart.rtdpjlite.service.rest.seloeletronico.SeloEletronicoRest;
import br.com.exmart.rtdpjlite.util.DocumentoUtil;
import br.com.exmart.rtdpjlite.util.RetornoSelo;
import br.com.exmart.rtdpjlite.util.Utils;
import br.com.exmart.rtdpjlite.vo.ArquivoCsvImportacao;
import br.com.exmart.rtdpjlite.vo.ObjetoProtocoloVO;
import br.com.exmart.rtdpjlite.vo.RetornoValidacaoLote;
import br.com.exmart.rtdpjlite.vo.StatusProtocoloJson;
import br.com.exmart.rtdpjlite.vo.balcaoonline.Pedido;
import br.com.exmart.rtdpjlite.vo.financeiro.CustasCartorio;
import br.com.exmart.rtdpjlite.vo.financeiro.FormaCalculoFinanceiro;
import br.com.exmart.rtdpjlite.vo.financeiro.ServicoCalculado;
import br.com.exmart.rtdpjlite.vo.financeiro.ServicoFinanceiro;
import br.com.exmart.rtdpjlite.vo.seloeletronico.SeloParteRTDPJ;
import br.com.exmart.rtdpjlite.vo.seloeletronico.SeloRTDPJ;
import com.google.common.base.Strings;
import com.opencsv.bean.CsvToBeanBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.dom4j.DocumentException;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional
public class ProtocoloService implements CrudService<Protocolo> {
//	@PersistenceContext
//	private EntityManager entityManager;
	protected static Logger logger = LoggerFactory.getLogger(ProtocoloService.class);
	@Autowired
	private LoteItemRepository loteItemRepository;
	@Autowired
    private ProtocoloGridRepository protocoloGridRepository;
	@Autowired
	private UsuarioNotificacaoService usuarioNotificacaoService;
	@Autowired
	private CalcularPrazoProtocoloService calcularPrazoProtocolo;
	@Autowired
	SeloEletronicoRest seloEletronicoRest;
	@Autowired
	GeradorProtocoloService geradorProtocoloService;
	@Autowired
	ProtocoloRepository protocoloRepository;
	@Autowired
	ModeloService modeloService;
	@Autowired
	StatusRepository statusRepository;
	@Autowired
	StatusProtocoloRepository statusProtocoloRepository;
	@Autowired
	ArquivoService arquivoService;
	@Autowired
	TimelineRepository timelineRepository;
	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private ParteService parteService;
	@Autowired
	private SigningService signingService;
	@Autowired
	private ConfiguracaoService configuracaoService;
	@Autowired
	private PDFService pdfService;
	@Autowired
	private FinanceiroService financeiroService;
	@Autowired
	private IndicadorPessoalVORepository indicadorPessoalRepository;
	@Autowired
	private RegistroService registroService;
    @Autowired
    private PedidoService pedidoService;
    @Autowired
	private FoneticaService foneticaService;
	@Autowired
	private NaturezaService naturezaService;
	@Autowired
	private SubNaturezaService subNaturezaService;
	@Autowired
	private QualidadeService qualidadeService;
	@Autowired
	private FormaEntregaService formaEntregaService;
	@Autowired
	private DocumentoUtil documentoUtil;
    @Autowired
    private ReverterService reverterService;


	@Autowired
	private ProtocoloArquivoHashRepository protocoloArquivoHashRepository;
	private String natureza = "Títulos e Documentos e Pessoa Jurídica";

	@Override
	public ProtocoloRepository getRepository() {
		return protocoloRepository;
	}

	@Override
	public long countAnyMatching(Optional<String> filter) {
		return getRepository().count();
	}

	@Override
	public Page<Protocolo> findAnyMatching(Optional<String> filter, Pageable pageable) {
		Page<Protocolo> retorno = getRepository().findAll(pageable);
		return retorno;
	}

	@Override
	public Page<Protocolo> find(Pageable pageable) {
		return getRepository().findAll(pageable);
	}

	@org.springframework.transaction.annotation.Transactional(isolation = Isolation.SERIALIZABLE)
	private synchronized Protocolo gerarNumeroProtocolo(Protocolo protocolo){
		protocolo.setNumero(geradorProtocoloService.getNumero(protocolo.getTipo()));
		protocoloRepository.saveAndFlush(protocolo);
		return protocolo;
	}



	public void informarPortal(Protocolo protocolo, Usuario usuarioLogado, Set<CartorioParceiro> cartoriosParceiros){
		if(!TipoProtocolo.isExameCalculo(protocolo.getTipo())) {
			if (protocolo.getCliente() != null) {
				if (protocolo.getPedido() == null) {
					try {
						logger.info("inicio criar pedido portal");
						String subNatureza = "Não Informado";
						if(TipoProtocolo.isCertidao(protocolo.getTipo())){
							subNatureza = "CERTIDÃO";
						}else{
							subNatureza = protocolo.getSubNatureza().getNome();
						}

						protocolo.setPedido(
								pedidoService.criarPedido(protocolo.getCliente(), protocolo.getNumero(), subNatureza,TipoProtocolo.recuperaEspecialidade(protocolo.getTipo()), protocolo.getId(), protocolo.getResponsavelPedido(), protocolo.getNrContrato(), protocolo.getParte(), protocolo.getParteDocumento())
						);
						logger.info("fim criar pedido portal");
						protocolo.setDataProtocoloInformadoPortal(LocalDateTime.now());
						StatusProtocoloJson statusJson = new StatusProtocoloJson();
						statusJson.setObservacao("Protocolo Informado no pedido nº: " + protocolo.getPedido());
						protocolo.getStatusProtocolo().add(new StatusProtocolo(statusRepository.findByNome("INFORMADO PORTAL - PROTOCOLO"), LocalDateTime.now(), null, statusJson, usuarioLogado));
					} catch (Exception e) {
						logger.error("Erro ao informar protocolo: " + e.getMessage());
					}
				}else{
//					if(cartoriosParceiros.size() > 0) {
//						pedidoService.adicionarCartoriosParceiros(protocolo.getPedido(), cartoriosParceiros);
//					}
					pedidoService.atualizarPedido(protocolo,cartoriosParceiros);
				}
				//TODO ENTENDER PQ TINHA ESSE CARA
//				else {
//					if (protocolo.getDataProtocoloInformadoPortal() == null) {
//						try {
//							logger.info("inicio informar protocolo portal");
//							pedidoService.informarProtocoloPedido(protocolo.getPedido(), protocolo.getNumero(), TipoProtocolo.recuperaEspecialidade(protocolo.getTipo()), protocolo.getSubNatureza().getNome(), null);
//							logger.info("fim informar protocolo portal");
//							protocolo.setDataProtocoloInformadoPortal(LocalDateTime.now());
//							StatusProtocoloJson statusJson = new StatusProtocoloJson();
//							statusJson.setObservacao("Protocolo Informado no pedido nº: " + protocolo.getPedido());
//							protocolo.getStatusProtocolo().add(new StatusProtocolo(statusRepository.findByNome("INFORMADO PORTAL - PROTOCOLO"), LocalDateTime.now(), null, statusJson, usuarioLogado));
//						} catch (Exception e) {
//							logger.error("Erro ao informar protocolo: " + e.getMessage());
//						}
//					}
//				}
			}
//			return protocoloRepository.save(protocolo);
		}else{
//			return protocoloRepository.save(protocolo);
		}
	}

	@org.springframework.transaction.annotation.Transactional
	public Protocolo save(Protocolo protocolo, ProtocoloService.ACAO acao, List<ServicoCalculado> servicos, boolean icPreProtocolar, boolean icLote, StatusProtocoloJson statusProtocoloJson, Usuario usuarioLogado) throws Exception {
		List<Status> statusInformarPortal = new ArrayList<>();

		for(StatusProtocolo statusPrto : protocolo.getStatusProtocolo()) {
			if (statusPrto.getId() == null) {
				statusInformarPortal.add(statusPrto.getStatus());
			}
		}
		if(protocolo.getCartorioParceiroList().size() > 0){
			protocolo.setIcRegistrarTituloOutraPraca(true);
		}
		this.icInformouCi = false;
		logger.info("inicio save protocolo ");
		String especialidade = TipoProtocolo.recuperaEspecialidade(protocolo.getTipo());
		//salvar objtos para nao ter problemas ao resalvar protocolo
		List<ObjetoProtocoloVO> objetosProtocolo = null;
		if(protocolo.getObjetos() != null){
			objetosProtocolo = new ArrayList<ObjetoProtocoloVO>(protocolo.getObjetos());
		}
		List<ServicoCalculado> servicosProtocolo = null;
		if(servicos != null){
			servicosProtocolo = new ArrayList<>(servicos);
		}
 		if(protocolo.getDataPrevista() == null) {
			protocolo.setDataVencimento(calcularPrazoProtocolo.calcularData(protocolo.getNatureza().getDiasVencimento(), true, LocalDate.now()));
			protocolo.setData_prevista(calcularPrazoProtocolo.calcularData(protocolo.getNatureza().getDiasPrevisao(), false, LocalDate.now()));
		}
//		protocolo.setServicos(servicos);


		Long numeroRegistroCI = null;
		if(acao.equals(ACAO.SALVAR)){
			if(protocolo.getNumero() == null) {

				protocolo.getStatusProtocolo().add(new StatusProtocolo(statusRepository.findByNome("PROTOCOLADO"), LocalDateTime.now(), null, null, usuarioLogado));
				if (icPreProtocolar) {
					protocolo.getStatusProtocolo().add(new StatusProtocolo(statusRepository.findByNome("PROTOCOLO INCOMPLETO"), LocalDateTime.now().plusSeconds(10), null, null, usuarioLogado));
				}
				if (protocolo.getDataProtocolo() == null) {
					protocolo.setDataProtocolo(LocalDateTime.now());
				}

				protocolo = gerarNumeroProtocolo(protocolo);

				if(!Strings.isNullOrEmpty(protocolo.getRegistroReferencia())) {
					Registro registro = registroService.findByRegistroAndEspecialidade(protocolo.getRegistroReferencia(), TipoProtocolo.recuperaEspecialidade(protocolo.getTipo()));

					objetosProtocolo = registro.getObjetos();
					protocolo.setObjetos(registro.getObjetos());
					for(IndicadorPessoal indicadorPessoal : registro.getIndicadorPessoal()) {
						protocolo.getPartesProtocolo().add(ParteProtocolo.fromIndicadorPessoal(parteService.findFirstByCpfCnpjOrderByIdDesc(indicadorPessoal.getCpfCnpj()),  indicadorPessoal.getQualidade(), indicadorPessoal.getVlParticipacao(), indicadorPessoal.getQtdCotas()));
					}
				}


			}else {
				boolean icCertidaoIntimacao = false;
				for (StatusProtocolo status : protocolo.getStatusProtocolo()) {
					if(status.getStatus().getNome().equalsIgnoreCase("CERTIDÃO INTIMAÇÃO EMITIDA")){
						if(status.getId() == null){
							icCertidaoIntimacao = true;
						}
					}
					if (status.getStatus().getNome().equalsIgnoreCase("ENTREGUE")) {
						if (status.getId() == null) {
							protocolo.setDataEntrega(status.getData());
						}
					}
				}
				if(icCertidaoIntimacao){
					Configuracao configuracao = configuracaoService.findConfiguracao();
					Registro registro = this.registroService.findByProtocolo(protocolo.getId());
					String txtCarimbo = protocolo.getNatureza().getCarimbo().getCarimbo().replace("[[DATA_REGISTRO]]", Utils.formatarData(registro.getDataRegistro())).replace("[[NUMERO_REGISTRO]]",registro.getNumeroRegistro().toString());
					File convertido = arquivoService.existeArquivoProtocoloCarimbado(protocolo.getNumero(), especialidade, Diretorio.CARIMBADO, protocolo.getTipo());
//					RetornoSelo retornoRest = seloEletronicoRest.gerarSelo(new SeloRTDPJ(protocolo, protocolo.getNumero(), configuracaoService.findConfiguracao().getCodigoCnsCartorio(), protocolo.getDtRegistro()));
					//FIXME VALIDAR SE PRECISA
					assinarArquivoCertidaoIntimacao(protocolo, especialidade, configuracao, convertido, txtCarimbo, null);
				}
			}

		}else if(acao.equals(ACAO.REGISTRAR)){

			//TODO VALIDAR se possui cartorio parceiro
			if(protocolo.isIcRegistrarTituloOutraPraca()){
				if(protocolo.getCartorioParceiroList().size() <=0){
					throw new Exception("Favor informar pelo menos 1 cartório parceiro");
				}
			}

			informarProtocoloCI(protocolo,servicos);
			boolean icCertidao = false;
			if(TipoProtocolo.isPrenotacao(protocolo.getTipo())) {
				try {
					protocolo.getStatusProtocolo().add(new StatusProtocolo(statusRepository.findByNome("REGISTRADO"),LocalDateTime.now(), null, statusProtocoloJson, usuarioLogado));
					LocalDateTime dtRegistro = LocalDateTime.now();
					Registro novoRegistro = registroService.gerarRegistro(protocolo, servicos, especialidade,dtRegistro);
					protocolo.setDtRegistro(novoRegistro.getDataRegistro());
					protocolo.setNumeroRegistro(novoRegistro.getNumeroRegistro().toString());
					numeroRegistroCI = novoRegistro.getNumeroRegistro();


					if(!icLote) {
						RetornoSelo retornoRest = null;
						try {
							retornoRest = seloEletronicoRest.gerarSelo(new SeloRTDPJ(protocolo, numeroRegistroCI, configuracaoService.findConfiguracao().getCodigoCnsCartorio(), dtRegistro, protocolo.getServicos(), SeloParteRTDPJ.fromParteProtocolo(protocolo.getPartesProtocolo())));
							protocolo.setSeloDigitalId(retornoRest.getSeloDigitalId());
						}catch (NotUseSelo e){
						}
						protocolo = assinarArquivoProtocolo(protocolo, especialidade, novoRegistro, usuarioLogado, retornoRest) ;
					}
				} catch (Exception e){
					e.printStackTrace();
					throw new Exception(e.getMessage());
				}


				protocolo = getRepository().save(protocolo);

			}else if(TipoProtocolo.isCertidao(protocolo.getTipo())) {
				protocolo.setDtRegistro(LocalDateTime.now());
				Configuracao configuracao = configuracaoService.findConfiguracao();
//				File convertido = arquivoService.existeArquivoProtocoloCarimbado(protocolo.getNumero(), especialidade, Diretorio.CARIMBADO, protocolo.getTipo());
				Registro registro = this.registroService.findByRegistroAndEspecialidade(protocolo.getRegistroReferencia(), protocolo.getTipo());
                String txtCarimbo = protocolo.getNatureza().getCarimbo().getCarimbo();
                if(registro!= null) {
                    txtCarimbo = txtCarimbo.replace("[[DATA_REGISTRO]]", Utils.formatarData(registro.getDataRegistro())).replace("[[NUMERO_REGISTRO]]", registro.getNumeroRegistro().toString());
                }

				if(registro == null){
					//FIXME deve colocar algo
					txtCarimbo = configuracaoService.findConfiguracao().getTxtCarimboCertidaoSemRegistro();
					txtCarimbo = txtCarimbo.replace("[[NOME_CARTORIO]]", configuracao.getNomeCartorio());
				}else {
					txtCarimbo = txtCarimbo.replace("[[NUMERO_REGISTRO]]", registro.getNumeroRegistro().toString());
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					txtCarimbo = txtCarimbo.replace("[[DATA_REGISTRO]]", br.com.exmart.rtdpjlite.util.Utils.formatarData(registro.getDataRegistro().toLocalDate()));
				}

				RetornoSelo retornoRest = null;
                try{
                	retornoRest = seloEletronicoRest.gerarSelo(new SeloRTDPJ(protocolo,
						protocolo.getNumero(),
						configuracaoService.findConfiguracao().getCodigoCnsCartorio(), protocolo.getDtRegistro(), protocolo.getServicos(), Arrays.asList(new SeloParteRTDPJ(protocolo.getParte(), protocolo.getParteDocumento()))));

					protocolo.setSeloDigitalId(retornoRest.getSeloDigitalId());
				}catch (NotUseSelo e){
				}

				assinarArquivoCertidao(protocolo, especialidade, configuracao, txtCarimbo, usuarioLogado, retornoRest);
				protocolo.getStatusProtocolo().add(new StatusProtocolo(statusRepository.findByNome("PRONTO PARA ENTREGA"),LocalDateTime.now(), null, statusProtocoloJson, usuarioLogado));

			}else{
				protocolo.setDtRegistro(LocalDateTime.now());
			}

			if(!TipoProtocolo.isExameCalculo(protocolo.getTipo())) {
				if (servicos.size() > 0) {
					logger.info("inicio marcar como concluido financeiro");
					if(TipoProtocolo.isCertidao(protocolo.getTipo())){
						numeroRegistroCI = protocolo.getNumero();
					}
					financeiroService.marcarComoConcluido(protocolo.getNumero(), protocolo.getTipo(),numeroRegistroCI);
					logger.info("fim marcar como concluido financeiro");
				}
			}
		}else if(acao.equals(ACAO.DEVOLVER)){
			protocolo.getStatusProtocolo().add(new StatusProtocolo(statusRepository.findByNome("DEVOLVIDO"),LocalDateTime.now(), null, statusProtocoloJson, usuarioLogado));
			protocolo.setDtDevolvido(LocalDateTime.now());
			financeiroService.marcarComoDevolvido(protocolo.getNumero(), protocolo.getTipo());
		}
		// SALVA o ARQUIVO DO PROTOCOLO
		if (protocolo.getArquivo() != null) {
			arquivoService.salvarDocumentoProtocolo(protocolo.getNumero(), protocolo.getArquivo(), especialidade, protocolo.getTipo(), false);
		}

		//SALVA ARQUIVOS DOS STATUS
		for (StatusProtocolo status : protocolo.getStatusProtocolo()) {
			if (status.getArquivo() != null) {
				//FIXME deve salvar apenas os novos
				arquivoService.salvarDocumentoProtocoloAndamento(protocolo.getNumero(), status.getId(), status.getArquivo(), especialidade, protocolo.getTipo());
			}
		}

		//CASO NAO SEJA EXAME CALCULO DEVE ENVIAR OS NOVOS SERVICOS PARA O FINANCEIRO
		informarProtocoloCI(protocolo, servicos);

		Set<CartorioParceiro> cartorioParceiroList = new HashSet<>();
		for(ProtocoloCartorioParceiro protocoloCartorioParceiro: protocolo.getCartorioParceiroList()){
            cartorioParceiroList.add(protocoloCartorioParceiro.getCartorioParceiro());
        }
		informarPortal(protocolo, usuarioLogado, cartorioParceiroList);



		if(objetosProtocolo != null){
			protocolo.setObjetos(objetosProtocolo);
		}
		if(servicosProtocolo != null) {
			protocolo.setServicos(servicosProtocolo);

		}


		logger.info("fim save protocolo ");
		protocoloRepository.save(protocolo);

		return protocolo;
	}

	boolean icInformouCi = false;
	private void informarProtocoloCI(Protocolo protocolo, List<ServicoCalculado> servicos) {
		if(!icInformouCi) {
			if (!protocolo.getTipo().equals(TipoProtocolo.EXAMECALCULO_TD) && !protocolo.getTipo().equals(TipoProtocolo.EXAMECALCULO_PJ)) {
				if (servicos != null) {
					if (servicos.size() > 0) {
						List<ServicoCalculado> novos = new ArrayList<>();
						for (ServicoCalculado servico : servicos) {
							if (servico.getIdServicoCalculado() == null) {
								novos.add(servico);
							}
						}
						if (novos.size() > 0) {
							financeiroService.protocolar(protocolo.getNumero(), protocolo.getApresentante(), protocolo.getApresentanteRg(), protocolo.getResponsavel(), novos, protocolo.getTipo(), protocolo.getVlDepositoPrevio());
							this.icInformouCi = true;
						}
					}
				}
			}
		}
	}

	private Protocolo assinarArquivoCertidaoIntimacao(Protocolo protocolo, String especialidade, Configuracao configuracao, File convertido, String txtCarimbo, RetornoSelo retSelo) {
		try {
			File arquivoAssinado = signingService.sign7(
					convertido,
					arquivoService.recuperarPasta(
							protocolo.getNumero(),
							especialidade,
							Diretorio.PROTOCOLO,
							Diretorio.ASSINADO,
							protocolo.getTipo()
					),
					convertido.getName(),
					"",
					configuracao.getNomeCartorio() + " - " + configuracao.getEnderecoCompleto(),
					LocalDateTime.now(),
					txtCarimbo,
					true,
					PDFService.DisposicaoPagina.PRIMEIRA_PAGINA,
					null
			);
			convertido.delete();

			File pastaCertidaoNotificacao = new File(arquivoService.recuperarPasta(protocolo.getNumero(), TipoProtocolo.recuperaEspecialidade(protocolo.getTipo()), Diretorio.PROTOCOLO, Diretorio.CERTIDAO_NOTIFICACAO, protocolo.getTipo()));
			String[] entries = pastaCertidaoNotificacao.list();
			for(String s: entries){
				File currentFile = new File(pastaCertidaoNotificacao.getPath(),s);
				currentFile.delete();
			}
			IOUtils.copy(new FileInputStream(arquivoAssinado), new FileOutputStream(new File(pastaCertidaoNotificacao,protocolo.getNumero()+".PDF")));

			apagarArquivosProtocolo(protocolo);
			return protocoloRepository.save(protocolo);
		}catch (Exception e){
			logger.error("Erro ao assinar Arquivo Protocolo: " + e.getMessage());
		}
		return protocolo;
	}

	//FIXME corrigir para carimbar novamente por causa do selo digital antes de registrar
	private Protocolo assinarArquivoCertidao(Protocolo protocolo, String especialidade, Configuracao configuracao, String txtCarimbo, Usuario usuarioLogado, RetornoSelo retSelo) {
		try {


			File arqOriginal = null;

            arqOriginal = arquivoService.recuperarDocumentoProtocoloFile(protocolo.getNumero(), TipoProtocolo.recuperaEspecialidade(protocolo.getTipo()),protocolo.getTipo());

			String hashArquivo = recuperarHashArquivo(protocolo.getId(), null);

			if(retSelo != null)
				retSelo.setMensagemCarimboSelo(configuracaoService.findConfiguracao().getDsMensagemCarimboSelo());

			File arquivoNovo = pdfService.converteAssinaPdf(
					arqOriginal,
					arquivoService.recuperarPasta(protocolo.getNumero(), TipoProtocolo.recuperaEspecialidade(protocolo.getTipo()), Diretorio.PROTOCOLO, Diretorio.CARIMBADO, protocolo.getTipo()),
					protocolo.getTextoCarimbo(),
					txtCarimbo,
					configuracaoService.findConfiguracao().getTxtExplicativoAssinatura().replace("<<URL_VALIDACAO>>", "%%%" + configuracaoService.findConfiguracao().getUrlQrcode() + "/documento/" + hashArquivo + "%%%"),
					configuracaoService.findConfiguracao().getUrlQrcode() + "/documento/",
					hashArquivo,
					PDFService.DisposicaoPagina.PRIMEIRA_PAGINA,
					null, null,
					protocolo.getNatureza().isIcCertidaoDigital(),
					retSelo);


			File arquivoAssinado = signingService.sign7(
					arquivoNovo,
					arquivoService.recuperarPasta(
							protocolo.getNumero(),
							especialidade,
							Diretorio.PROTOCOLO,
							Diretorio.ASSINADO,
							protocolo.getTipo()
					),
					arquivoNovo.getName(),
					protocolo.getTextoCarimbo(),
					configuracao.getNomeCartorio() + " - " + configuracao.getEnderecoCompleto(),
					LocalDateTime.now(),
					txtCarimbo,
					true,
					PDFService.DisposicaoPagina.PRIMEIRA_PAGINA,
					null
			);


			File arquivoCarimbo = arquivoService.recuperarDocumentoProtocoloFile(protocolo.getNumero(),TipoProtocolo.recuperaEspecialidade(protocolo.getTipo()),protocolo.getTipo(), Diretorio.CARIMBADO);

			if(arquivoCarimbo.exists()){
				arquivoCarimbo.delete();
			}
			StatusProtocoloJson statusJson = new StatusProtocoloJson();
			statusJson.setObservacao("Arquivo gerado: " + arquivoAssinado.getAbsoluteFile());
			protocolo.getStatusProtocolo().add(new StatusProtocolo(statusRepository.findByNome("ARQUIVO ASSINADO"), LocalDateTime.now(), null, statusJson, usuarioLogado));
			if (protocolo.getPedido() != null) {
				try {
					if(protocolo.getCartorioParceiroList().size() > 0) {
						for(ProtocoloCartorioParceiro cartorioParceiro : protocolo.getCartorioParceiroList()) {
							pedidoService.encaminharCertidao(protocolo.getPedido(), protocolo, cartorioParceiro.getCartorioParceiro());
							protocolo.setDataCertidaoInformadoPortal(LocalDateTime.now());
							protocolo.setDataRegistroInformadoPortal(LocalDateTime.now());
							protocolo.getStatusProtocolo().add(new StatusProtocolo(statusRepository.findByNome("INFORMADO PORTAL - ENCAMINHAR CERTIDÃO"), LocalDateTime.now(), null, null, usuarioLogado));
						}
					}
				} catch (Exception e) {
					logger.error(e.getMessage());
					e.printStackTrace();
				}
			}
			return protocoloRepository.save(protocolo);
		}catch (Exception e){
			logger.error("Erro ao assinar Arquivo Protocolo: " + e.getMessage());
		}
		return protocolo;
	}

	private Protocolo assinarArquivoProtocolo(Protocolo protocolo, String especialidade, Registro novoRegistro, Usuario usuarioLogado, RetornoSelo retSelo) throws IOException, DocumentException, GeneralSecurityException {
		logger.debug("comecou assinar arquivo protocolo");
        Configuracao configuracao = configuracaoService.findConfiguracao();
        String txtCarimbo = protocolo.getNatureza().getCarimbo().getCarimbo().replace("[[DATA_REGISTRO]]", Utils.formatarData(novoRegistro.getDataRegistro())).replace("[[NUMERO_REGISTRO]]", novoRegistro.getNumeroRegistro().toString());
        //FIXME deve vrificar se tem o arquivo
        File arquivoP7s = new File(arquivoService.recuperarPasta(protocolo.getNumero(), TipoProtocolo.recuperaEspecialidade(protocolo.getTipo()), Diretorio.PROTOCOLO, null, protocolo.getTipo())+protocolo.getNumero()+".p7s");
        if(!arquivoP7s.exists()){
        	arquivoP7s = null;
		}
        String hashArquivo = recuperarHashArquivo(protocolo.getId(), null);
        File arqOriginal = arquivoService.recuperarDocumentoProtocoloFile(protocolo.getNumero(), TipoProtocolo.recuperaEspecialidade(protocolo.getTipo()), protocolo.getTipo());


		if(retSelo != null)
        	retSelo.setMensagemCarimboSelo(configuracaoService.findConfiguracao().getDsMensagemCarimboSelo());

        File arquivoNovo = pdfService.converteAssinaPdf(
				arqOriginal
                ,
                arquivoService.recuperarPasta(protocolo.getNumero(), TipoProtocolo.recuperaEspecialidade(protocolo.getTipo()), Diretorio.PROTOCOLO, Diretorio.PROTOCOLO, protocolo.getTipo()),
                protocolo.getTextoCarimbo().replace("[[DATA_REGISTRO]]", Utils.formatarData(novoRegistro.getDataRegistro())).replace("[[NUMERO_REGISTRO]]", novoRegistro.getNumeroRegistro().toString()),
                protocolo.getTextoCertidao(),
                configuracaoService.findConfiguracao().getTxtExplicativoAssinatura().replace("<<URL_VALIDACAO>>", "%%%" + configuracaoService.findConfiguracao().getUrlQrcode() + "/documento/" + hashArquivo + "%%%"),
                configuracaoService.findConfiguracao().getUrlQrcode() + "/documento/",
                hashArquivo,
                PDFService.DisposicaoPagina.ULTIMA_PAGINA,
                null, arquivoP7s,
				protocolo.getNatureza().isIcCertidaoDigital(),
				retSelo);
        //TODO VOLTAR ISSO
		File arquivoAssinado = signingService.sign7(arquivoNovo,
				arquivoService.recuperarPasta(novoRegistro.getNumeroRegistro(), especialidade, Diretorio.REGISTRO, null, protocolo.getTipo()),
				novoRegistro.getRegistro() + ".PDF",
				protocolo.getTextoCarimbo().replace("[[DATA_REGISTRO]]",
						Utils.formatarData(novoRegistro.getDataRegistro())).replace("[[NUMERO_REGISTRO]]", novoRegistro.getNumeroRegistro().toString())
				, configuracao.getNomeCartorio() + " - " + configuracao.getEnderecoCompleto(), LocalDateTime.now(),
				txtCarimbo, true, PDFService.DisposicaoPagina.ULTIMA_PAGINA, arqOriginal);


		StatusProtocoloJson statusJson = new StatusProtocoloJson();
		statusJson.setObservacao("Arquivo gerado: "+ arquivoAssinado.getAbsoluteFile());
        protocolo.getStatusProtocolo().add(new StatusProtocolo(statusRepository.findByNome("ARQUIVO ASSINADO"),LocalDateTime.now(), null,statusJson , usuarioLogado));
        if(retSelo!= null) {
			protocolo.setSeloDigitalId(retSelo.getSeloDigitalId());
		}
        apagarArquivosProtocolo(protocolo);

        try {
            if(protocolo.getPedido() != null) {
				pedidoService.informarRegistroCartorio(protocolo.getPedido(), novoRegistro.getNumeroRegistro(), novoRegistro.getRegistro(), protocolo.getNumero(), protocolo.getId());
                protocolo.setDataRegistroInformadoPortal(LocalDateTime.now());
				protocolo.getStatusProtocolo().add(new StatusProtocolo(statusRepository.findByNome("INFORMADO PORTAL - REGISTRO"),LocalDateTime.now(), null,null , usuarioLogado));
            }
        }catch (Exception e ){
        	e.printStackTrace();
			logger.error("Erro ao assinar Arquivo Protocolo: " + e.getMessage());
        }
		logger.debug("acabou assinar arquivo protocolo");
        return protocoloRepository.save(protocolo);
    }

	private void apagarArquivosProtocolo(Protocolo protocolo) throws IOException {
		File pastaProtocolo = new File(arquivoService.recuperarPasta(protocolo.getNumero(), TipoProtocolo.recuperaEspecialidade(protocolo.getTipo()), Diretorio.PROTOCOLO, Diretorio.PROTOCOLO, protocolo.getTipo()));
		String[] entries = pastaProtocolo.list();
		for(String s: entries){
			File currentFile = new File(pastaProtocolo.getPath(),s);
			currentFile.delete();
		}
		pastaProtocolo.delete();
		File pastaAssinado = new File(arquivoService.recuperarPasta(protocolo.getNumero(), TipoProtocolo.recuperaEspecialidade(protocolo.getTipo()), Diretorio.PROTOCOLO, Diretorio.ASSINADO, protocolo.getTipo()));
		entries = pastaAssinado.list();
		for(String s: entries){
			File currentFile = new File(pastaAssinado.getPath(),s);
			currentFile.delete();
		}
		pastaAssinado.delete();
		File pastaCarimbado = new File(arquivoService.recuperarPasta(protocolo.getNumero(), TipoProtocolo.recuperaEspecialidade(protocolo.getTipo()), Diretorio.PROTOCOLO, Diretorio.CARIMBADO, protocolo.getTipo()));
		entries = pastaCarimbado.list();
		for(String s: entries){
			File currentFile = new File(pastaCarimbado.getPath(),s);
			currentFile.delete();
		}
		pastaCarimbado.delete();
	}

	@Override
	public Long count() {
		return this.getRepository().count();
	}

	public List<Protocolo> findAll(Integer protocolo) {
		return getRepository().findAll(protocolo);
	}

	public List<Protocolo> findByNumeroAndTipoProtocoloIn(Long protocolo, List<TipoProtocolo> tipoProtocolos) {
		return getRepository().findByNumeroAndTipoIn(protocolo, tipoProtocolos);
	}

	public Protocolo findByNumero(Long numero) {
		return getRepository().findByNumero(numero);
	}

	public Protocolo findByNumeroAndTipoProtocolo(Long protocolo, TipoProtocolo tipoProtocolo) {
		return getRepository().findByNumeroAndTipo(protocolo, tipoProtocolo);
	}

	@PersistenceContext
	private EntityManager em;
	@org.springframework.transaction.annotation.Transactional(readOnly=true)
    public List<Protocolo> findAll(int offset, int limit, Optional<ProtocoloFiltro> filter){
        List<Protocolo> retorno = null;

		ProtocoloFiltro filtro = filter.get();
		logger.debug("offset: {}" , offset);
		logger.debug("limit: {}" , limit);

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Protocolo> query = builder.createQuery(Protocolo.class);
		Root<Protocolo> root = query.from(Protocolo.class);

		Predicate predicate = montarWhere(filtro, builder, root);

		query.where(predicate);
		query.orderBy(builder.desc(root.get("dataProtocolo")));

		retorno = em.createQuery(query).setFirstResult(offset).setMaxResults(limit).getResultList();
        return retorno;
    }

	private Predicate montarWhere(ProtocoloFiltro filtro, CriteriaBuilder builder, Root<Protocolo> root) {
		Predicate predicate = builder.conjunction();


		if(filtro.getCliente() != null){
			predicate = builder.and(predicate,builder.equal(root.get("cliente"),filtro.getCliente()));
		}
		if(filtro.getNatureza() != null){
			predicate = builder.and(predicate,builder.equal(root.get("natureza"),filtro.getNatureza()));
		}
		if(filtro.getAndamento() != null){
			predicate = builder.and(predicate,builder.equal(root.join("statusProtocolo").join("status"),filtro.getAndamento()));
		}
		if(filtro.getTipoProtocolo() != null){
			predicate = builder.and(predicate,builder.equal(root.get("tipo"),filtro.getTipoProtocolo()));
		}
		if(filtro.getNumero() != null){
			predicate = builder.and(predicate,builder.equal(root.get("numero"),filtro.getNumero()));
		}
		if(!Strings.isNullOrEmpty(filtro.getBuscaTexto())){

//			predicate = builder.and(predicate,builder.like(root.get("apresentante"),filtro.getBuscaTexto().toUpperCase()+"%"));
			predicate = builder.and(
			        predicate,
                    builder.or(
                            builder.like(root.get("apresentante"),filtro.getBuscaTexto().toUpperCase()+"%"),
                            builder.like(root.get("parte"),filtro.getBuscaTexto().toUpperCase()+"%"),
                            builder.like(root.get("parteDocumento"),filtro.getBuscaTexto().toUpperCase()+"%")
                    )
            );


		}
		if(!Strings.isNullOrEmpty(filtro.getCampoData())){
			if(filtro.getDtInicial() != null & filtro.getDtFinal() != null) {
				if (filtro.getCampoData().equalsIgnoreCase("data_vencimento")) {
					predicate = builder.and(predicate,builder.between(root.get(filtro.getCampoData()), filtro.getDtInicial().toLocalDate(), filtro.getDtFinal().toLocalDate()));
				} else {
					predicate = builder.and(predicate,builder.between(root.get(filtro.getCampoData()), filtro.getDtInicial().with(LocalTime.MIN), filtro.getDtFinal().with(LocalTime.MAX)));

				}
			}
		}
		return predicate;
	}

	@org.springframework.transaction.annotation.Transactional(readOnly=true)
    public Integer countFindAll(Optional<ProtocoloFiltro> filter){
        		Integer total = 0;
		CriteriaBuilder builder = em.getCriteriaBuilder();

		CriteriaQuery<Long> query = builder.createQuery(Long.class);
		Root<Protocolo> root = query.from(Protocolo.class);

		query.select(builder.count(root));
		query.where(montarWhere(filter.get(), builder, root));
		total = em.createQuery(query).getSingleResult().intValue();
		return total;
    }


	public List<CardTimeline> getTimeLine(String numero, String especialidade) {
		List<CardTimeline> retorno = new ArrayList<>();
		if(Strings.isNullOrEmpty(numero) || Strings.isNullOrEmpty(especialidade))
			return retorno;

		retorno = this.timelineRepository.findTimeline(numero, especialidade);
		return retorno;
	}


	public boolean isArquivoEnviado(Long numero, String especialidade, TipoProtocolo tipoProtocolo){
		return arquivoService.isDocumentoProtocoloExists(numero, especialidade, tipoProtocolo);
	}

	@org.springframework.transaction.annotation.Transactional(readOnly = true)
	public boolean isEditavel(Protocolo protocolo) {
		return !protocolo.getStatus().getStatus().isBloquearProtocolo();
	}
	public Long existeProtocoloFromExameCalculo(TipoProtocolo tipo , Long nrExameCalculo){
		if(tipo.equals(TipoProtocolo.EXAMECALCULO_PJ)){
			return getRepository().findProtocoloByExameCalculo(nrExameCalculo, TipoProtocolo.PRENOTACAO_PJ.getId());
		}else{
			return getRepository().findProtocoloByExameCalculo(nrExameCalculo, TipoProtocolo.PRENOTACAO_TD.getId());
		}
	}

	@org.springframework.transaction.annotation.Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
	public String recuperarHashArquivo(Long numeroProtocolo, Long idServico) throws NoSuchProviderException, NoSuchAlgorithmException, IOException {
		synchronized (this) {
			ProtocoloArquivoHash hash = null;
			if (idServico == null) {
				hash = protocoloArquivoHashRepository.findByProtocolo(numeroProtocolo);
			} else {
				hash = protocoloArquivoHashRepository.findByProtocoloAndServico(numeroProtocolo, idServico);
			}

			if (hash == null) {
				String novoHash = null;
				do {
					novoHash = pdfService.gerarHash();

				} while (protocoloArquivoHashRepository.findByHash(novoHash) != null);
				hash = new ProtocoloArquivoHash(numeroProtocolo, idServico, novoHash);
				protocoloArquivoHashRepository.save(hash);
				protocoloRepository.flush();

			}
			return hash.getHash();
		}
	}

	public List<IndicadorPessoalVO> findIndicadorPessoal(String query, Integer tipobusca){
		return indicadorPessoalRepository.findIndicadorPessoal("%"+query+"%", query, tipobusca);
	}

    public void atualizarTextoCarimbo(Long id, String textoCarimbo) {
		this.protocoloRepository.atualizarTextoCarimbo(id, textoCarimbo);
    }
	public void atualizarTextoCertidao(Long id, String textoCertidao) {
		this.protocoloRepository.atualizarTextoCertidao(id, textoCertidao);
	}

	public Page<Protocolo> findByClientePageable(Cliente cliente, Pageable pagina) {
		return protocoloRepository.findByCliente(cliente, pagina);
//		return null;
	}

	public Protocolo findOne(Long protocoloId) {
		return this.protocoloRepository.findById(protocoloId);
	}

	public InputStream recuperarArquivoCertidaoAssinado(Protocolo protocolo) throws IOException {
		return arquivoService.recuperarDocumentoProtocoloIS(protocolo.getNumero(),TipoProtocolo.recuperaEspecialidade(protocolo.getTipo()),protocolo.getTipo(), Diretorio.ASSINADO);
	}
	public File recuperarArquivoCertidaoAssinadoFile(Protocolo protocolo) throws IOException {
		return arquivoService.recuperarDocumentoProtocoloFile(protocolo.getNumero(),TipoProtocolo.recuperaEspecialidade(protocolo.getTipo()),protocolo.getTipo(), Diretorio.ASSINADO);
	}

	public Protocolo findById(Long Id) {
		return this.protocoloRepository.findById(Id);
	}

	public List<Protocolo> findByTipo(TipoProtocolo tipo) {
		return protocoloRepository.findByTipo(tipo);
	}

	public RetornoValidacaoLote importarProtocolos(File arquivo, Cliente clinte) throws Exception{
		boolean erro = false;
		List<String> erros = new ArrayList<>();

		List<ServicoFinanceiro> servicos = financeiroService.listarServicos(TipoProtocolo.PRENOTACAO_TD).stream().filter(servico ->  (servico.getNmServico().toUpperCase().contains("AUTENTICAÇÃO DE LIVROS") || servico.getNmServico().toUpperCase().contains("MICROFILMAGEM"))).collect(Collectors.toList());
		if(servicos.size() <=0) {
			erro = true;
			erros.add(new String("Serviços 'AUTENTICAÇÃO DE LIVROS' e 'MICROFILMAGEM' não encontrados"));
		}
		List<FormaCalculoFinanceiro> formaCalculos = financeiroService.listarFormaCalculo();
		FormaCalculoFinanceiro custasIntegrais = null;
		for(FormaCalculoFinanceiro formaCalculo : formaCalculos){
			if(formaCalculo.getNmDivisor().equalsIgnoreCase("Custas Integrais")){
				custasIntegrais = formaCalculo;
			}
		}
		if(servicos.size() <=0) {
			erro = true;
			erros.add(new String("Forma de Calculo 'Custas Integrais' não encontrada"));
		}
		List<ServicoCalculado> servicosProtocolo = new ArrayList<>();
		for(ServicoFinanceiro servico : servicos){
			ServicoCalculado retornoServico = financeiroService.calcularServico(servico.getIdServico(), BigDecimal.ZERO, custasIntegrais.getCdDivisor(), TipoProtocolo.PRENOTACAO_TD);
			retornoServico.setNmFormaCalculo(custasIntegrais.getNmDivisor());
			servicosProtocolo.add(retornoServico);
		}

        List<ArquivoCsvImportacao> beans = new CsvToBeanBuilder<ArquivoCsvImportacao>(new InputStreamReader(new FileInputStream(arquivo), "ISO-8859-1"))
            .withSeparator(';')
            .withType(ArquivoCsvImportacao.class).build().parse();
		String diretorio = arquivo.getParent();
		Status statusRegistrado = statusRepository.findByNome("REGISTRADO");
		int linha = 1;
		for (ArquivoCsvImportacao arquivoImportar : beans) {

			if(arquivoImportar.contemCampoNull()){
				erro = true;
				erros.add(new String("Linha " + linha + ", Todos os campos devem estar preenchidos"));
			}
			if(!Utils.isCnpj(arquivoImportar.getParte2Documento())){
				erro = true;
				erros.add(new String("Linha " + linha + ", Documento inválido: " + arquivoImportar.getParte2Documento()));
			}
			File arquivoProtocolo = new File(diretorio + File.separator + arquivoImportar.getArquivo());
			if(!arquivoProtocolo.exists()){
				erro = true;
				erros.add(new String("Linha " + linha + "Arquivo não encontrado: " + arquivoProtocolo.getName()));
			}
			File TA_ArquivoProtocolo = new File(diretorio + File.separator + "TA_" + arquivoImportar.getArquivo());
			if(!TA_ArquivoProtocolo.exists()){
				erro = true;
				erros.add(new String("Linha " + linha + "Arquivo não encontrado: " + TA_ArquivoProtocolo.getName()));
			}
			File TE_ArquivoProtocolo = new File(diretorio + File.separator + "TE_" + arquivoImportar.getArquivo());
			if(!TE_ArquivoProtocolo.exists()){
				erro = true;
				erros.add(new String("Linha " + linha + "Arquivo não encontrado: " + TE_ArquivoProtocolo.getName()));
			}
			try{
				Utils.dataFromString(arquivoImportar.getDataDocumento());
			}catch (DateTimeParseException e){
				erro = true;
				erros.add("Linha " + linha + "Data inválida: " + arquivoImportar.getDataDocumento());
			}
			linha++;
		}

		if(erro) {
			File diretorioFile = new File(diretorio);
			for(String arquivoDeletar: diretorioFile.list()){
				File currentFile = new File(diretorioFile.getPath(),arquivoDeletar);
				currentFile.delete();
			}
			diretorioFile.delete();
			throw new Exception(erros.toString());
		}else{
				return new RetornoValidacaoLote( clinte, diretorio,statusRegistrado, beans, servicosProtocolo);
		}

	}






	public void processarArquivoLote(Lote lote, LoteItem loteItem, List<CustasCartorio> custasCartorio){//List<ArquivoCsvImportacao> arquivoImportarList, String diretorio, Usuario usuarioLogado){
		logger.info("comecou processar Arquivo LoteItem");
		File arquivoProtocolo = new File( loteItem.getArquivo());

		File TA_ArquivoProtocolo = new File(loteItem.getTermoAbertura());
		File TE_ArquivoProtocolo = new File(loteItem.getTermoEncerramento());

		List<String> arquivosMerge = new ArrayList<>();
		arquivosMerge.add(TA_ArquivoProtocolo.getAbsoluteFile().toString());
		arquivosMerge.add(arquivoProtocolo.getAbsoluteFile().toString());
		arquivosMerge.add(TE_ArquivoProtocolo.getAbsoluteFile().toString());
		try {

			String arquivoDestino = new File(loteItem.getArquivo()).getParent() + File.separator + "merge_" + FilenameUtils.getName(loteItem.getArquivo());
			File retorno = pdfService.pdfMergeAllgsWithoutPdfA(arquivosMerge, arquivoDestino);
			Protocolo protocolo = protocoloRepository.findById(loteItem.getProtocoloId());
			recuperarHashArquivo(protocolo.getId(), null);
//						protocolo.setArquivo(Files.readAllBytes(retorno.toPath()));
			arquivoService.salvarDocumentoProtocolo(protocolo.getNumero(), FileUtils.openInputStream(retorno), "TD", protocolo.getTipo(), false);
			Registro registro = registroService.findByProtocolo(protocolo.getId());
			assinarArquivoLote(lote.getUsuario(), custasCartorio, protocolo, registro);

			arquivoProtocolo.delete();
			TA_ArquivoProtocolo.delete();
			TE_ArquivoProtocolo.delete();
			retorno.delete();
			loteItem.setFinalizado(LocalDateTime.now());
		} catch (Exception e) {
			loteItem.setResultado(e.getMessage());
			e.printStackTrace();
		}
		loteItemRepository.save(loteItem);
		logger.info("finalizou processar Arquivo LoteItem");
	}

	public void assinarArquivoLote(Usuario usuario, List<CustasCartorio> custasCartorio, Protocolo protocolo, Registro registro) throws ErrorListException, IOException, DocumentException, GeneralSecurityException {
		RetornoSelo retornoRest = null;
		try{
			retornoRest = seloEletronicoRest.gerarSelo(new SeloRTDPJ(protocolo,
				registro.getNumeroRegistro(),
				configuracaoService.findConfiguracao().getCodigoCnsCartorio(),
				registro.getDataRegistro(),
				protocolo.getServicos(),
				SeloParteRTDPJ.fromParteProtocolo(protocolo.getPartesProtocolo())));
		}catch (NotUseSelo e){
		}
		List<Modelo> modelos = modeloService.findModeloByTipo(protocolo.getNatureza().getId(), protocolo.getSubNatureza().getId(), TipoModelo.CARIMBO);
		JtwigModel jtwigModel = documentoUtil.iniciarVariaveisModelo(registro, protocolo, new ArrayList<>(), custasCartorio, null, protocolo.getPartesProtocolo(), protocolo.getObjetos(), protocolo.getIntimacaosProtocolo(), null);
		JtwigTemplate jtwigTemplate =JtwigTemplate.inlineTemplate(modelos.get(0).getModelo());
		protocolo.setTextoCarimbo(jtwigTemplate.render(jtwigModel));
		assinarArquivoProtocolo(protocolo, TipoProtocolo.recuperaEspecialidade(protocolo.getTipo()), registro, usuario, retornoRest );
	}


	public List<CustasCartorio> recuperarCustasServico(List<ServicoCalculado> servicosSeloList, List<CustasCartorio> custas) {
		if(servicosSeloList.size()<= 0){
			return new ArrayList<>();
		}
		Class<ServicoCalculado> classeServico = ServicoCalculado.class;
		for(CustasCartorio custa : custas){
			Method metodo = null;
			try {
				metodo = classeServico.getMethod("getCustas"+custa.getNumero());
				custa.setValor(custa.getValor().add((BigDecimal) metodo.invoke(servicosSeloList.get(0))));
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}

		CustasCartorio total = new CustasCartorio();
		total.setNome("TOTAL");
		BigDecimal vlTotalServicos = BigDecimal.ZERO;
		for(ServicoCalculado serv : servicosSeloList){
			vlTotalServicos = vlTotalServicos.add(serv.getTotal());
		}
		total.setNumero(custas.size()+1);
		total.setValor(vlTotalServicos);


		List<CustasCartorio> retorno = new ArrayList<>();
		for(CustasCartorio custa : custas){
			retorno.add(custa);
		}
		retorno.add(total);
		return retorno;
	}

	public Long findNumeroById(Long protocolo) {
		return protocoloRepository.findNumeroById(protocolo);
	}

	public Protocolo criarProtocoloAverbacaoByPedido(Long protocoloAntigoId, File certidao, Long pedidoId) throws Exception {
		Protocolo antigo  = findById(protocoloAntigoId);
		if(antigo == null)
			throw new Exception("Pedido não encontrado");
        Registro registro = null;
        if(TipoProtocolo.isCertidao(antigo.getTipo())){
		    registro = registroService.findRegistroFromProtocoloCertidao(protocoloAntigoId);
		    antigo = findById(registro.getProtocolo());
        }else {
            registro = registroService.findByProtocoloIdAndEspecialidade(antigo.getId(), TipoProtocolo.recuperaEspecialidade(antigo.getTipo()));
        }
		if(registro == null)
			throw new Exception("Registro Não encontrado");

		Protocolo novo = new Protocolo();
		novo.setNatureza(antigo.getNatureza());
		novo.setTipo(antigo.getTipo());
		novo.setFormaEntrega(antigo.getFormaEntrega());
		novo.setObservacoes(antigo.getObservacoes());
		novo.setEmail(antigo.getEmail());
		novo.setTelefone(antigo.getTelefone());
		novo.setApresentanteRg(antigo.getApresentanteRg());
		novo.setResponsavel(antigo.getResponsavel());
		novo.setDataDoc(antigo.getDataDoc());
		novo.setValor(antigo.getValor());
		novo.setVias(antigo.getVias());
		novo.setCliente(antigo.getCliente());
		if(pedidoId == null) {
			novo.setPedido(antigo.getPedido());
		}else{
			novo.setPedido(pedidoId);
		}
		novo.setParteDocumento(antigo.getParteDocumento());
		novo.setIcRegistrarTituloOutraPraca(false);
		novo.setPastaPJ(antigo.getPastaPJ());
		novo.setObjetos(antigo.getObjetos());

		novo.setSubNatureza(antigo.getSubNatureza());
		novo.setIcGuardaConservacao(antigo.getIcGuardaConservacao());
		novo.setIcPossuiSigiloLegal(antigo.getIcPossuiSigiloLegal());
		novo.setResponsavelPedido(antigo.getResponsavelPedido());
		novo.setApresentante(antigo.getApresentante());
		novo.setParte(antigo.getParte());




		novo.setRegistroReferencia(registro.getRegistro());
		novo.setNumeroRegistroReferencia(registro.getNumeroRegistro());


		novo.setDataPrevista(calcularPrazoProtocolo.calcularData(antigo.getNatureza().getDiasPrevisao(), true, LocalDate.now()));
		novo.setDataVencimento(calcularPrazoProtocolo.calcularData(antigo.getNatureza().getDiasVencimento(), false, LocalDate.now()));

		novo = this.save(novo, ACAO.SALVAR, new ArrayList<>(),false, false, new StatusProtocoloJson(), usuarioService.getUsuarioPortal());
		arquivoService.salvarDocumentoProtocolo(novo.getNumero(), new FileInputStream(certidao), TipoProtocolo.recuperaEspecialidade(novo.getTipo()), novo.getTipo(), false);
		return novo;
	}

    public int countParceiros(Long protocoloId) {
        return protocoloRepository.countCartorioParceirosById(protocoloId);
    }

    public List<Long> duplicarProtocolo(Long protocoloId, Integer qtd, Usuario usuario) throws Exception {
		Protocolo prot = protocoloRepository.findById(protocoloId);
		List<Long> retorno = new ArrayList<>();
		retorno.add(prot.getNumero());
		File arquivo = null;
        if(arquivoService.isDocumentoProtocoloExists(prot.getNumero(), TipoProtocolo.recuperaEspecialidade(prot.getTipo()), prot.getTipo())){
            arquivo = arquivoService.recuperarDocumentoProtocoloFile(prot.getNumero(), TipoProtocolo.recuperaEspecialidade(prot.getTipo()), prot.getTipo());
        }
		for (int i = 0; i < qtd; i++) {
			List<ServicoCalculado> servicos = prot.cloneServicos();


			Protocolo novo = prot.clone(prot.getTipo(), calcularPrazoProtocolo);
			save(novo, ACAO.SALVAR, servicos, true, false, new StatusProtocoloJson(), usuario);
			retorno.add(novo.getNumero());
			if(arquivo != null)
			    arquivoService.salvarDocumentoProtocolo(novo.getNumero(), new FileInputStream(arquivo),TipoProtocolo.recuperaEspecialidade(novo.getTipo()), novo.getTipo(), false);

		}
		prot.getStatusProtocolo().add(new StatusProtocolo(statusRepository.findByNome("PROTOCOLO INCOMPLETO"), LocalDateTime.now().plusSeconds(10), null, null, usuario));
		save(prot, ACAO.SALVAR, new ArrayList<>(), false, false, new StatusProtocoloJson(), usuario);

		return retorno;
    }

	public void registrarProtocolosLote(List<CustasCartorio> custasCartorio, Usuario usuarioLogado) throws ErrorListException, Exception {

		List<Protocolo> retorno = protocoloRepository.findByLastStatus(statusRepository.findByNome("REGISTRAR EM LOTE").getId());
		List<String> errosMsg = new ArrayList<>();
		for (Protocolo protocolo : retorno) {
			if(!arquivoService.isDocumentoProtocoloExists(protocolo.getNumero(),TipoProtocolo.recuperaEspecialidade(protocolo.getTipo()), protocolo.getTipo())){
				errosMsg.add("\nArquivo não encontrado para o protocolo: " + protocolo.getNumero());
				continue;
			}
			if(protocolo.getPartesProtocolo().size() <= 0 ){
				errosMsg.add("\nPartes não encontrado para o protocolo: " + protocolo.getNumero());
				continue;
			}
			if(Strings.isNullOrEmpty(protocolo.getSituacaoAtualRegistro())){
				errosMsg.add("\nSituação atual do Registro não encontrado para o protocolo: " + protocolo.getNumero());
				continue;
			}

			List<Modelo> modelos = modeloService.findModeloByTipo(protocolo.getNatureza().getId(), protocolo.getSubNatureza().getId(), TipoModelo.CARIMBO);
			JtwigModel jtwigModel = documentoUtil.iniciarVariaveisModelo(null, protocolo, new ArrayList<>(), custasCartorio, null, protocolo.getPartesProtocolo(), protocolo.getObjetos(), protocolo.getIntimacaosProtocolo(), null);
			JtwigTemplate jtwigTemplate =JtwigTemplate.inlineTemplate(modelos.get(0).getModelo());
			protocolo.setTextoCarimbo(jtwigTemplate.render(jtwigModel));
//
//				recuperarHashArquivo(protocolo.getId(), null);

			StatusProtocoloJson statusJson = new StatusProtocoloJson();
			statusJson.setTexto(protocolo.getNatureza().getCarimbo().getCarimbo());
			logger.debug("Entrou sem Registro");

			save(protocolo, ProtocoloService.ACAO.REGISTRAR, financeiroService.listarServicosProtocolo(protocolo.getNumero(), protocolo.getTipo()), false, false, statusJson, usuarioLogado);


		}
		if(errosMsg.size() > 0){
			throw  new ErrorListException("Erro ao registrar em Lote", errosMsg);
		}
	}

	public Protocolo findByNumeroAndPedidoId(Long numeroProtocolo, Long pedidoId) {
		return protocoloRepository.findByNumeroAndPedido(numeroProtocolo, pedidoId);
	}


	public enum ACAO {
		REGISTRAR,
		DEVOLVER,
		SALVAR
	}
}