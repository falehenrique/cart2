package br.com.exmart.indicadorRTDPJ.ui.impl;

import br.com.exmart.indicadorRTDPJ.MyUI;
import br.com.exmart.indicadorRTDPJ.ui.Certidao;
import br.com.exmart.indicadorRTDPJ.ui.custom.event.ProtocoloEvent;
import br.com.exmart.rtdpjlite.exception.ErrorListException;
import br.com.exmart.rtdpjlite.exception.NotUseSelo;
import br.com.exmart.rtdpjlite.model.*;
import br.com.exmart.rtdpjlite.service.*;
import br.com.exmart.rtdpjlite.service.enums.Diretorio;
import br.com.exmart.rtdpjlite.service.rest.seloeletronico.SeloEletronicoRest;
import br.com.exmart.rtdpjlite.vo.financeiro.CustasCartorio;
import br.com.exmart.rtdpjlite.vo.financeiro.ServicoCalculado;
import br.com.exmart.rtdpjlite.vo.financeiro.ServicoEtiqueta;
import br.com.exmart.rtdpjlite.vo.seloeletronico.SeloParteRTDPJ;
import br.com.exmart.util.BeanLocator;
import br.com.exmart.util.Disposicao;
import com.google.common.base.Strings;
import com.vaadin.data.HasValue;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.wcs.wcslib.vaadin.widget.multifileupload.ui.MultiFileUpload;
import org.jtwig.JtwigModel;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CertidaoImpl extends Certidao implements PassoProtocolo{
    private Protocolo protocolo;
    private ArquivoService arquivoService;
    MultiFileUpload singleUpload = null;
    public boolean icPossuiArquivo = false;
    private ProtocoloService protocoloService;
    private ModeloService modeloService;
    private FinanceiroService financeiroService;

    private Modelo modeloInicial= null;
    private JtwigModel jtwigModel;
    private ConfiguracaoService configuracaoService;
    private PDFService pdfService;
    private SeloEletronicoRest seloEletronicoRest;
    Configuracao configuracao;

    Protocolo protocoloRegistro =null;
    private EventBus.ViewEventBus eventBus;
    private String natureza = "Títulos e Documentos e Pessoa Jurídica";
    List<ServicoCalculado> servcosSeloList = new ArrayList<>();
    private ServicoCalculado itemSelecionado = null;
    private Map<Long, List<ServicoEtiqueta>> servicoEtiquetaMap = new HashMap<>();
    private boolean faltaEtiqueta = false;
    private RegistroService registroService;


    public CertidaoImpl(Protocolo protocolo, EventBus.ViewEventBus eventBus) {
        this.eventBus = eventBus;
        this.protocoloService = BeanLocator.find(ProtocoloService.class);
        this.arquivoService = BeanLocator.find(ArquivoService.class);
        this.seloEletronicoRest = BeanLocator.find(SeloEletronicoRest.class);
        this.modeloService = BeanLocator.find(ModeloService.class);
        this.configuracaoService = BeanLocator.find(ConfiguracaoService.class);
        this.pdfService = BeanLocator.find(PDFService.class);
        this.financeiroService = BeanLocator.find(FinanceiroService.class);
        this.registroService = BeanLocator.find(RegistroService.class);
        this.protocolo = protocolo;
        this.eventBus.subscribe(this);

        this.configuracao = configuracaoService.findConfiguracao();

    }




    boolean icHabilita = false;
    @Override
    public void habilitarForm(boolean habilitar, boolean icProtocoloEditavel) {
        if(!icProtocoloEditavel){
            habilitar = icProtocoloEditavel;
        }
        this.icHabilita = habilitar;
    }

    private void aplicarModelo(HasValue.ValueChangeEvent<Modelo> valueChangeEvent) {
//        editorEtiquetas.setValue(valueChangeEvent.getValue().getModelo());
//        editarCarimbo();
    }




    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    @Override
    public void cancelar() {

    }

    @Override
    public void focus() {
        try {
            if(this.protocolo.isIcRegistrarTituloOutraPraca()){
                if(this.protocolo.getCartorioParceiroList().size() <= 0){
                    Notification.show("ATENÇÃO","Favor informar pelo menos 1 cartório parceiro",Notification.Type.ERROR_MESSAGE);
                }
            }
            try {
                seloEletronicoRest.validarSelo(protocolo, financeiroService.listarServicosProtocolo(this.protocolo.getNumero(), protocolo.getTipo()), Arrays.asList(new SeloParteRTDPJ(protocolo.getParte(), protocolo.getParteDocumento())));
            }catch (NotUseSelo e ){

            }

            //TODO corrigir isso
            File arquivo = null;
            boolean mostrarTelaPdf = false;
            if(protocolo.getNatureza().getTipoEmissaoCertidao() != null){
                if (protocolo.getNatureza().getTipoEmissaoCertidao().equals(TipoEmissaoCertidao.TEXTO)){
                    arquivo = null;
                    mostrarTelaPdf=true;
                }else {
                    if(Strings.isNullOrEmpty(protocolo.getRegistroReferencia())){
                        Notification.show("Atenção",  "Para certidões do tipo IMAGEM OU INTEIRO TEOR e necessário informar o Registro de Referência (caso ja tenha informado favor cadastrar o retroativo do mesmo)", Notification.Type.ERROR_MESSAGE);
                    }else{
                        Registro registro = this.registroService.findByRegistroAndEspecialidade(protocolo.getRegistroReferencia(), protocolo.getTipo());
                        if (registro != null) {
                            if (protocolo.getNatureza().getTipoEmissaoCertidao().equals(TipoEmissaoCertidao.IMAGEM_INTEIRO_TEOR)) {
                                List<String> registros = new ArrayList<>();
                                boolean icGerarArquivo = true;
                                for (CardTimeline registroReferencia : this.protocoloService.getTimeLine(this.protocolo.getRegistroReferencia(), TipoProtocolo.recuperaEspecialidade(this.protocolo.getTipo()))) {
                                    String diretorio = null;
                                    try {
                                        diretorio = arquivoService.recuperarPasta(registroReferencia.getNumeroRegistro(), registroReferencia.getEspecialidade(), Diretorio.REGISTRO, null, null);
                                        registros.add(diretorio + registroReferencia.getRegistro() + ".PDF");

                                        if(!new File(diretorio + registroReferencia.getRegistro() + ".PDF").exists()){
                                            mostrarTelaPdf = false;
                                            icGerarArquivo = false;
                                            Notification.show("Erro", "Arquivo do registro de nº: "+registroReferencia.getNumeroRegistro() + ", não encontrado", Notification.Type.ERROR_MESSAGE);
                                        }else {
                                            mostrarTelaPdf = true;
                                        }
                                    } catch (IOException e) {
                                        icGerarArquivo = false;
                                        Notification.show("Erro", "Erro ao recuperar arquivos do registro de nº: " + registroReferencia.getNumeroRegistro(), Notification.Type.ERROR_MESSAGE);
                                        e.printStackTrace();
                                    }
                                }
                                try{
                                    if(icGerarArquivo) {
                                        arquivo = pdfService.pdfMergeAllgs(registros, arquivoService.recuperarPasta(this.protocolo.getNumero(), TipoProtocolo.recuperaEspecialidade(this.protocolo.getTipo()), Diretorio.PROTOCOLO, null, protocolo.getTipo()) + File.separator + this.protocolo.getNumero() + ".PDF");
                                        mostrarTelaPdf = true;
                                    }else{
                                        mostrarTelaPdf = false;
                                    }
                                } catch (Exception e) {
                                    Notification.show("Erro", "Erro ao gerar arquivo de inteiro teor: ", Notification.Type.ERROR_MESSAGE);
                                    e.printStackTrace();
                                }
                            } else if (protocolo.getNatureza().getTipoEmissaoCertidao().equals(TipoEmissaoCertidao.IMAGEM)) {
                                arquivo = arquivoService.copiarDocumentoProtocoloregistroFile(protocolo.getNumero(), TipoProtocolo.recuperaEspecialidade(protocolo.getTipo()), registro.getRegistro(), registro.getNumeroRegistro(), protocolo.getTipo());
                                if(arquivo== null){
                                    Notification.show("Erro", "Arquivo do registro de nº: "+protocolo.getNumeroRegistroReferencia() + ", não encontrado", Notification.Type.ERROR_MESSAGE);
                                }else {
                                    mostrarTelaPdf = true;
                                }
                            }else{
                                Notification.show("Atenção", "Tipo de emissão não reconhecida" + protocolo.getNatureza().getTipoEmissaoCertidao() , Notification.Type.ERROR_MESSAGE);
                            }
                        } else {
                            Notification.show("Atenção", "Favor cadastrar o retroativo para o registro de nº: " + protocolo.getRegistroReferencia(), Notification.Type.ERROR_MESSAGE);
                        }
                    }
                }
            }else{
                Notification.show("Atenção","Natureza sem tipo de emissão (TEXTO, IMAGEM OU INTEIRO TEOR", Notification.Type.ERROR_MESSAGE);
            }

            if(mostrarTelaPdf) {
                if(arquivo != null)
                    arquivo = pdfService.converteA4(arquivo);
                if (conteudo.getComponentCount() < 1) {
//            if (arquivoService.isDocumentoProtocoloExists(this.protocolo.getNumero(), ((MyUI)UI.getCurrent()).getEspecialidadeAtual().getEspecialidade(), protocolo.getTipo())) {
                    conteudo.removeAllComponents();
                    List<CustasCartorio> custas = protocoloService.recuperarCustasServico(this.servcosSeloList, ((MyUI) UI.getCurrent()).getCustasCartorio());
//                TipoModelo tipoModeloCarimbo = this.tipoModeloService.findByNome("ETIQUETA CARIMBO");
//                TipoModelo tipoModeloTexto = this.tipoModeloService.findByNome("TEXTO CERTIDAO " + TipoEmissaoCertidao.nomeFromCode(this.protocolo.getNatureza().getTipoEmissaoCertidao().getId()));

                    List<Modelo> modelosCarimbo = modeloService.findModeloByTipo(this.protocolo.getNatureza().getId(), this.protocolo.getSubNatureza() != null ? this.protocolo.getSubNatureza().getId() : null, TipoModelo.CARIMBO);
                    List<Modelo> modelosTexto = modeloService.findModeloByTipo(this.protocolo.getNatureza().getId(), this.protocolo.getSubNatureza() != null ? this.protocolo.getSubNatureza().getId() : null, TipoModelo.TEXTO);
                    if(modelosCarimbo.size() > 0) {

                        ModalRegistroImpl modalRegistro = new ModalRegistroImpl(protocolo, modelosCarimbo, custas, ModalRegistroImpl.TipoDocumento.CERTIDAO, arquivo, modelosTexto, null, null);
                        modalRegistro.habilitar(this.icHabilita);
                        conteudo.addComponentsAndExpand(modalRegistro);
                    }else{
                        Notification.show("Atenção", "Nenhum carimbo foi encontrado para essa natureza, favor entrar em contato com o Administrador", Notification.Type.ERROR_MESSAGE);
                    }

//            }
                } else {
                    ((ModalRegistroImpl) conteudo.getComponent(0)).habilitar(this.icHabilita);
                }
            }

        } catch (ErrorListException e) {
            Notification.show("Erro", e.getMessage() + "\n" + e.recuperaErros(), Notification.Type.ERROR_MESSAGE);
        } catch (Exception e) {
            Notification.show("Erro", e.getMessage() + "\n" + e.getMessage(), Notification.Type.ERROR_MESSAGE);
            e.printStackTrace();
        }

    }

    @Override
    public void validate() throws Exception {

    }

    @Override
    public void removerListener() {

    }

    @Override
    public void addListener() {

    }

    @EventBusListenerMethod(scope = EventScope.VIEW)
    public void servicoAdicionado(ProtocoloEvent.ServicosAtualizado evento){
//        this.servcosSemSeloList.clear();
//        this.servcosSemSeloList.addAll(evento.getServicos().stream().filter(item -> item.isIcNecessitaSelo() == false).collect(Collectors.toList()));
        this.servcosSeloList.clear();
        this.servcosSeloList.addAll(evento.getServicos());//.stream().filter(item -> item.isIcNecessitaSelo() == true & item.getIdServicoCalculado() != null).collect(Collectors.toList()));

//        this.faltaEtiqueta = false;
//        for(ServicoCalculado servico : this.servcosSeloList){
//            List<ServicoEtiqueta> etiquetas = financeiroService.getEtiquetasServico(servico.getIdServico());
//            if(etiquetas.size() > 0) {
//                this.servicoEtiquetaMap.put(servico.getIdServico(), etiquetas);
//            }else{
//                Notification.show("Não foi encontrado nenhuma etiqueta para o serviço: " + servico.getNmServico(), Notification.Type.ERROR_MESSAGE);
//            }
//        }
    }

    boolean bloquearMultiplaExecucao = false;
    @EventBusListenerMethod(scope = EventScope.VIEW)
    public void atualizarCarimbo(ProtocoloEvent.CarimboAtualizado evento){
        try {
            if(bloquearMultiplaExecucao){
                return;
            }
            bloquearMultiplaExecucao = true;
            if(itemSelecionado == null){
                Notification.show("Atenção","Favor selecionar 1 serviço", Notification.Type.ERROR_MESSAGE);
                return;
            }
            File arquivo =  null;

            File arquivoOriginal = null;
            arquivoOriginal = arquivoService.recuperarDocumentoProtocoloFile(this.protocolo.getNumero(), ((MyUI)UI.getCurrent()).getEspecialidadeAtual().getEspecialidade(), protocolo.getTipo());
            if(arquivoOriginal != null) {


            pdfService.converteAssinaPdfCertidao(arquivoOriginal,
                    evento.getTexto(),
                    arquivoService.recuperarPasta(protocolo.getNumero(),((MyUI)UI.getCurrent()).getEspecialidadeAtual().getEspecialidade(), Diretorio.PROTOCOLO,Diretorio.CARIMBADO, protocolo.getTipo()),
                    protocolo.getNatureza().getCarimbo().getCarimbo(),
                    configuracaoService.findConfiguracao().getTxtExplicativoAssinatura(),
                    configuracaoService.findConfiguracao().getUrlQrcode(),
                    this.itemSelecionado.getIdServicoCalculado(),
                    protocoloService.recuperarHashArquivo(this.protocolo.getId(), this.itemSelecionado.getIdServicoCalculado()),
                    PDFService.DisposicaoPagina.PRIMEIRA_PAGINA,
                    Disposicao.DIREITA_SUPERIOR, null,
                    protocolo.getNatureza().isIcCertidaoDigital(),
                    null
            );
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            Notification.show("Erro","Erro ao gerar o hash do arquivo, favor contatar o Administrador", Notification.Type.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            Notification.show("Erro","Erro ao gerar o hash do arquivo, favor contatar o Administrador", Notification.Type.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }


}
