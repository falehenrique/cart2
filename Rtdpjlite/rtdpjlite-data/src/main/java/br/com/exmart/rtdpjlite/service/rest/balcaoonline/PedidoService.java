package br.com.exmart.rtdpjlite.service.rest.balcaoonline;

import br.com.exmart.rtdpjlite.annotations.NotFoundException;
import br.com.exmart.rtdpjlite.model.*;
import br.com.exmart.rtdpjlite.model.portal.*;
import br.com.exmart.rtdpjlite.repository.portal.PedidoProtocoloRepository;
import br.com.exmart.rtdpjlite.repository.portal.PedidoRepository;
import br.com.exmart.rtdpjlite.repository.portal.PedidoVoRepository;
import br.com.exmart.rtdpjlite.repository.portal.UsuarioPortalRepository;
import br.com.exmart.rtdpjlite.service.*;

import br.com.exmart.rtdpjlite.util.DocumentoUtil;
import br.com.exmart.rtdpjlite.util.Utils;
import br.com.exmart.rtdpjlite.vo.portal.PedidoVO;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PedidoService {

    @Autowired
    private ConfiguracaoService configuracaoService;

    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private PedidoProtocoloRepository pedidoProtocoloRepository;
    @Autowired
    private UsuarioPortalRepository usuarioPortalRepository;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private PedidoVoRepository pedidoVoRepository;
    @Autowired
    private ProtocoloService protocoloService;
    @Autowired
    private CartorioParceiroService cartorioParceiroService;
    @Autowired
    private RegistroService registroService;
    @Autowired
    private DocumentoUtil documentoUtil;


    public List<Pedido> findByClienteId(Long clienteId){
        Cliente cliente = clienteService.findOne(clienteId);
        if(cliente == null){
            throw new NotFoundException("Cliente não encontrado");
        }
        return pedidoRepository.findByCliente(cliente);
    }

    public void atualizarPedido(Protocolo protocolo, Set<CartorioParceiro> cartorioParceiros){
        Pedido pedido = pedidoRepository.findOne(protocolo.getPedido());
        if(pedido.getProtocoloId() == null) {
            pedido.setProtocolo(protocolo.getNumero());
            pedido.setProtocoloId(protocolo.getId());
        }
        if(pedido.getProtocoloId().equals(protocolo.getId())) {
            String subNatureza = "Não Informado";
            if (TipoProtocolo.isCertidao(protocolo.getTipo())) {
                subNatureza = "CERTIDÃO";
            } else {
                subNatureza = protocolo.getSubNatureza().getNome();
            }
            pedido.setNrContrato(protocolo.getNrContrato());
            pedido.setParte(protocolo.getParte());
            pedido.setParteDocumento(protocolo.getParteDocumento());
            pedido.setCliente(protocolo.getCliente());
            pedido.setResponsavel(protocolo.getResponsavelPedido());

            pedido.setSubNatureza(subNatureza);
            List<PedidoStatus> statusRemover = new ArrayList<>();

            for (PedidoStatus status : pedido.getStatusPedido()) {
                if (status.getNome().equalsIgnoreCase(StatusPedido.PROTOCOLO.getNome())) {
                    if (status.getData() == null) {
                        status.setNumero(protocolo.getNumero());
                        status.setData(protocolo.getDataProtocolo());
                    }
                }
                if (pedido.getProtocoloId().equals(protocolo.getId())) {
                    if (status.getNome().equalsIgnoreCase(StatusPedido.CERTIDAO_ENCAMINHADA.getNome())) {
                        if (!cartorioParceiros.stream().anyMatch(cartorioParceiro -> cartorioParceiro.getId().equals(status.getCartorioParceiroId()))) {
                            statusRemover.add(status);
                        }
                    }
                }
            }
            pedido.getStatusPedido().removeAll(statusRemover);
            Iterator<CartorioParceiro> cartorioParceiroIterator = cartorioParceiros.iterator();
            while (cartorioParceiroIterator.hasNext()) {
                CartorioParceiro cartorioParceiro = cartorioParceiroIterator.next();
                if (!pedido.getStatusPedido().stream().anyMatch(status -> cartorioParceiro.getId().equals(status.getCartorioParceiroId()))) {
                    pedido.getStatusPedido().add(adicionarCartoriosParceiros(cartorioParceiro));
                }

            }
            pedidoRepository.save(pedido);
        }
    }

    public Long criarPedido(Cliente cliente, Long nrProtocolo, String subNatureza, String especialidade, Long protocoloId, UsuarioPortal responsavel, String nrContrato, String parte, String parteDocumento){
        Pedido novoPedido = new Pedido();
        novoPedido.setData(LocalDateTime.now());
        novoPedido.setProtocolo(nrProtocolo);
        novoPedido.setCliente(cliente);
        novoPedido.setTipo(especialidade);
        novoPedido.setSubNatureza(subNatureza);
        novoPedido.setProtocoloId(protocoloId);
        novoPedido.setResponsavel(responsavel);
        novoPedido.setNrContrato(nrContrato);
        novoPedido.setParte(parte);
        novoPedido.setParteDocumento(parteDocumento);

        LocalDateTime dtProtocolo = LocalDateTime.now();
        Long numeroProtocolo = nrProtocolo;
        LocalDateTime dtRegistro = null;
        Long numeroRegistro = null;
        if(subNatureza.equalsIgnoreCase("CERTIDÃO")) {
            Registro registro = registroService.findRegistroFromProtocoloCertidao(protocoloId);

            dtRegistro = registro.getDataRegistro();
            numeroRegistro = registro.getNumeroRegistro();

            Protocolo protocoloRegistro = protocoloService.findOne(registro.getProtocolo());
            dtProtocolo = protocoloRegistro.getDataProtocolo();
            numeroProtocolo = protocoloRegistro.getNumero();
            novoPedido.setNumeroRegistro(registro.getNumeroRegistro());
            novoPedido.setRegistro(registro.getRegistro());

        }


        novoPedido.getStatusPedido().add(
            new PedidoStatus(
                    StatusPedido.PROTOCOLO.getNomeAguardando(),
                    StatusPedido.PROTOCOLO.getNome(),
                    numeroProtocolo !=null ? dtProtocolo : null,
                    numeroProtocolo,
                    null,
                    null,
                    null,
                    StatusPedido.PROTOCOLO.getNrOrdem()
            )
        );
//        if(Strings.isNullOrEmpty(subNatureza) || !subNatureza.equalsIgnoreCase("CERTIDÃO")) {
            novoPedido.getStatusPedido().add(
                    new PedidoStatus(
                            StatusPedido.REGISTRO.getNomeAguardando(),
                            StatusPedido.REGISTRO.getNome(),
                            dtRegistro,
                            numeroRegistro,
                            null,
                            null,
                            null,
                            StatusPedido.REGISTRO.getNrOrdem()
                    )
            );
//        }

        pedidoRepository.save(novoPedido);

        return novoPedido.getId();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void informarRegistroCartorio(Long pedidoId, Long numeroRegistro, String registro, Long protocolo, Long protocoloId){

        //FIXME quando e feito o processo todo sem salvar o protocolo apos colocar os cartorios parceiro ele ao entende que tem cartorio parceiro e finaliza o pedido
        Pedido pedido = pedidoRepository.findOne(pedidoId);
        int totalParceiros = protocoloService.countParceiros(pedido.getProtocoloId());
        if(pedido.getNumeroRegistro() == null) {
            pedido.setNumeroRegistro(numeroRegistro);
            for (PedidoStatus statusPedido : pedido.getStatusPedido()) {
                if (statusPedido.getNome().equalsIgnoreCase(StatusPedido.REGISTRO.getNome())) {
                    statusPedido.setNumero(numeroRegistro);
                    statusPedido.setData(LocalDateTime.now());
                    if(totalParceiros <= 0){
                        pedido.setDtFinalizado(LocalDateTime.now());
                    }
                }
            }
            pedido.setRegistro(registro);
        }else{
            int qtdFinalizados = 0;
            Long cartorioParceiroAverbacaoId = null;
            for(PedidoStatus statusPedido : pedido.getStatusPedido()){
                if(statusPedido.getNome().equalsIgnoreCase(StatusPedido.AVERBACAO.getNome())){
                    if(statusPedido.getNumero().equals(protocolo)) {
                        statusPedido.setNumero(numeroRegistro);
                        statusPedido.setData(LocalDateTime.now());
                        cartorioParceiroAverbacaoId = statusPedido.getCartorioParceiroId();
                    }
                    if(statusPedido.getData() != null){
                        qtdFinalizados++;
                    }
                }
            }
            if(cartorioParceiroAverbacaoId != null){
                for(PedidoProtocolo pedidoProtocolo : pedido.getProtocolos()){
                    if(pedidoProtocolo.getCartorioParceiro().getId().equals(cartorioParceiroAverbacaoId)){
                        pedidoProtocolo.setProtocoloAverbacaoId(protocoloId);
                    }
                }
//                deve setar o campo protocolo_averbacao_id na tabela pedido_protoclo quando averbar a margem
            }
            if(totalParceiros <= qtdFinalizados)
                pedido.setDtFinalizado(LocalDateTime.now());
        }
        if(pedido.getDtFinalizado() != null){
            pedido.setStatusAtualCliente(Pedido.StatusAtualCliente.PEDIDO_CONCLUIDO.getNome());
        }
        pedidoRepository.save(pedido);
    }

    public void informarDadosStatusPeidodProtocolo(Long pedidoId, Long pedidoProtocoloId, PedidoProtocoloStatus novoStatus){
        Pedido pedido = pedidoRepository.findOne(pedidoId);
//        StatusPedido ultimoStatusProtocolo = StatusPedido.
        Pedido.StatusAtualCliente ultimoStatusCliente = Pedido.StatusAtualCliente.AGUARDE_CARTORIO;

        for(PedidoProtocolo pedidoProtocolo : pedido.getProtocolos()){

            if(pedidoProtocolo.getId().equals(pedidoProtocoloId)){
                for(PedidoProtocoloStatus pedidoProtocoloStatus : pedidoProtocolo.getStatus()){
                    if(pedidoProtocoloStatus.getId().equals(novoStatus.getId())){
//                        if(usuario.equalsIgnoreCase(pedidoProtocoloStatus.getRemetente())){
                        pedidoProtocoloStatus.setData(LocalDateTime.now());
                        pedidoProtocoloStatus.setNumero(novoStatus.getNumero());
                        pedidoProtocoloStatus.setArquivo(novoStatus.getArquivo());
                        pedidoProtocoloStatus.setMensagem(novoStatus.getMensagem());
//                        }
                    }
                }
            }
            String proximoStatus = pedidoProtocolo.getProximoStatus();
            if(StatusPedido.PAGAMENTO.getNomeAguardando().equalsIgnoreCase(proximoStatus)){
             ultimoStatusCliente = Pedido.StatusAtualCliente.RESPONDER_CARTORIO;
            }
            pedidoProtocolo.setUltimaAtualizacao(LocalDateTime.now());
            pedidoProtocolo.setStatusAtualParceiro(proximoStatus);
        }

        pedido.setUltimaAtualizacao(LocalDateTime.now());

        pedido.setStatusAtualCliente(ultimoStatusCliente.getNome());
        pedidoRepository.save(pedido);
    }

    public void encaminharCertidao(Long pedidoId, Protocolo protocolo, CartorioParceiro cartorioParceiro) throws Exception {
        Pedido pedido = pedidoRepository.findOne(pedidoId);
        for(PedidoStatus pedidoStatus : pedido.getStatusPedido()){
            if(pedidoStatus.getCartorioParceiroId() != null) {
                if (pedidoStatus.getCartorioParceiroId().equals(cartorioParceiro.getId())) {
                    pedidoStatus.setData(LocalDateTime.now());
                    pedidoStatus.setNumero(protocolo.getNumero());
                }
            }
        }
        PedidoProtocolo novoPedidoProtocolo = new PedidoProtocolo();
        novoPedidoProtocolo.setProtocolo(protocolo);
        novoPedidoProtocolo.setCartorioParceiro(cartorioParceiro);

        novoPedidoProtocolo.getStatus().add(
                new PedidoProtocoloStatus(
                        StatusPedido.PROTOCOLO.getNomeAguardando(),
                        StatusPedido.PROTOCOLO.getNome(),
                        null,
                        null,
                        null,
                        "PARCEIRO",
                        null,
                        StatusPedido.PROTOCOLO.getNrOrdem()
                )
        );
        novoPedidoProtocolo.getStatus().add(
                new PedidoProtocoloStatus(
                        StatusPedido.ORCAMENTO.getNomeAguardando(),
                        StatusPedido.ORCAMENTO.getNome(),
                        null,
                        null,
                        null,
                        "PARCEIRO",
                        null,
                        StatusPedido.ORCAMENTO.getNrOrdem()
                )
        );
        novoPedidoProtocolo.getStatus().add(
                new PedidoProtocoloStatus(
                        StatusPedido.PAGAMENTO.getNomeAguardando(),
                        StatusPedido.PAGAMENTO.getNome(),
                        null,
                        null,
                        null,
                        "CLIENTE",
                        null,
                        StatusPedido.PAGAMENTO.getNrOrdem()
                )
        );
        novoPedidoProtocolo.getStatus().add(
                new PedidoProtocoloStatus(
                        StatusPedido.REGISTRO.getNomeAguardando(),
                        StatusPedido.REGISTRO.getNome(),
                        null,
                        null,
                        null,
                        "PARCEIRO",
                        null,
                        StatusPedido.REGISTRO.getNrOrdem()
                )
        );

        pedido.getProtocolos().add(novoPedidoProtocolo);
        pedidoRepository.save(pedido);
    }



    public Pedido findById(Long id, UsuarioPortal usuario) {
        Pedido retorno = pedidoRepository.findOne(id);
        List<PedidoProtocolo> remover = new ArrayList<>();
        if(usuario.getCartorioParceiro() != null){
            for(PedidoProtocolo pedidoProtocolo : retorno.getProtocolos()){
                if(!pedidoProtocolo.getCartorioParceiro().getId().equals(usuario.getCartorioParceiro().getId())){
                    remover.add(pedidoProtocolo);
                }
            }
        retorno.getProtocolos().removeAll(remover);
        }

        return retorno;
    }

    public PedidoStatus adicionarCartoriosParceiros(CartorioParceiro cartorioParceiro) {
        return
            new PedidoStatus(
                    StatusPedido.CERTIDAO_ENCAMINHADA.getNomeAguardando(),
                    StatusPedido.CERTIDAO_ENCAMINHADA.getNome(),
                    null,
                    null,
                    null,
                    cartorioParceiro.getId(),
                    cartorioParceiro.getNome(),
                    StatusPedido.CERTIDAO_ENCAMINHADA.getNrOrdem()
            );
    }

    public List<PedidoVO> find(UsuarioPortal usuario, String query) {
        String nrContrato =query;
        Long pedidoProtocolo = 0l;
        try{
            pedidoProtocolo = Long.parseLong(query);
        }catch (NumberFormatException e){

        }
        if(usuario.getCliente() != null){
            return pedidoVoRepository.findByClienteId(usuario.getCliente().getId(), nrContrato, pedidoProtocolo, pedidoProtocolo);
        }else if(usuario.getCartorioParceiro() != null){
            return pedidoVoRepository.findByCartorioParceiroId(usuario.getCartorioParceiro().getId(), nrContrato, pedidoProtocolo, pedidoProtocolo);
        }
        return pedidoVoRepository.findAllCartorio(nrContrato, pedidoProtocolo, pedidoProtocolo, pedidoProtocolo);
    }

    public List<PedidoVO> find(UsuarioPortal usuario) {
        if(usuario.getCliente() != null){
            return pedidoVoRepository.findByClienteId(usuario.getCliente().getId());
        }else if(usuario.getCartorioParceiro() != null){
            return pedidoVoRepository.findByCartorioParceiroId(usuario.getCartorioParceiro().getId());
        }
        return pedidoVoRepository.findAllCartorio();
    }

    public void criarProtocoloAverbacao(Long pedidoId, Long pedidoProtocoloId, Long pedidoProtocoloStatusId) throws Exception {
        Pedido pedido = pedidoRepository.findOne(pedidoId);
        File certidao = null;
        CartorioParceiro cartorioProtocolo = null;
        if(pedido == null)
            throw new NotFoundException("Pedido não encontrado");

        for(PedidoProtocolo pedidoProtocolo : pedido.getProtocolos()){
            if(pedidoProtocolo.getId().equals(pedidoProtocoloId)){
                cartorioProtocolo = pedidoProtocolo.getCartorioParceiro();
                for(PedidoProtocoloStatus pedidoProtocoloStatus : pedidoProtocolo.getStatus()){
                    if(pedidoProtocoloStatus.getId().equals(pedidoProtocoloStatusId)){
                        certidao = new File(pedidoProtocoloStatus.getArquivo());
                    }
                }
            }
        }
        if(certidao == null)
            throw new NotFoundException("Arquivo da certidão não encontrado");
        Protocolo retorno = protocoloService.criarProtocoloAverbacaoByPedido(pedido.getProtocoloId(), certidao, pedido.getId());

        pedido.getStatusPedido().add(
            new PedidoStatus(
                    StatusPedido.AVERBACAO.getNomeAguardando(),
                    StatusPedido.AVERBACAO.getNome(),
                    null,
                    retorno.getNumero(),
                    null,
                    cartorioProtocolo.getId(),
                    cartorioProtocolo.getNome(),
                    StatusPedido.AVERBACAO.getNrOrdem()
            )
        );

        pedidoRepository.save(pedido);


    }

    public String gerarModeloCertidaoParceiroId(Long pedidoId, Long cartorioParceiroId) {
        Pedido pedido = pedidoRepository.findOne(pedidoId);
        Protocolo protocolo = protocoloService.findById(pedido.getProtocoloId());
        Registro registro = null;
        if(protocolo.getDtRegistro() != null){
           registro = registroService.findByProtocolo(protocolo.getId());
        }
        CartorioParceiro cartorioParceiro = cartorioParceiroService.findById(cartorioParceiroId);
        String modelo = this.configuracaoService.findConfiguracao().getModeloCertidaoPortal();

        JtwigModel jtwing = documentoUtil.iniciarVariaveisModelo(
                registro,
                protocolo,
                null,
                null,
                null,
                protocolo.getPartesProtocolo(),
                protocolo.getObjetos(),
                null,
                cartorioParceiro

        );
        PedidoProtocoloStatus pedidoProtocoloStatus = recuperaPedidoStatusProtocolo(pedido, cartorioParceiroId, StatusPedido.PROTOCOLO);
        if(pedidoProtocoloStatus != null) {
            jtwing.with("NR_PROTOCOLO_CLIENTE", pedidoProtocoloStatus.getNumero() == null ? "_____" : pedidoProtocoloStatus.getNumero())
                    .with("DT_PROTOCOLO_CLIENTE", Utils.formatarData(pedidoProtocoloStatus.getData()));
        }
        JtwigTemplate jtwigTemplate = JtwigTemplate.inlineTemplate(configuracaoService.findConfiguracao().getModeloCertidaoPortal());

        String retorno = jtwigTemplate.render(jtwing);
        return retorno;

    }

    private PedidoProtocoloStatus recuperaPedidoStatusProtocolo(Pedido pedido, Long cartorioParceiroId, StatusPedido status) {
        for(PedidoProtocolo pedidoProtocolo : pedido.getProtocolos()){
            if(pedidoProtocolo.getCartorioParceiro().getId().equals(cartorioParceiroId)){
                for(PedidoProtocoloStatus pedidoProtocoloStatus : pedidoProtocolo.getStatus()){
                    if(pedidoProtocoloStatus.getNome().equalsIgnoreCase(status.getNome())){
                        return pedidoProtocoloStatus;
                    }
                }
            }
        }
        return null;
    }

    public List<Pedido> findNovos() {
        return pedidoRepository.findByProtocoloIsNull();
    }


//    private HttpHeaders addAuthorization(){
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("x-");
//    }

//
//    public List<Pedido> listarPedidos() throws HttpClientErrorException, HttpServerErrorException{
//        ResponseEntity<Pedido[]> responseEntity = restTemplate.getForEntity(configuracaoService.findConfiguracao().getUrlQrcode()+"pedido", Pedido[].class);
//        List<Pedido> retornoImportador = Arrays.asList(responseEntity.getBody());
//        return retornoImportador;
//
//    }
//
//    public Pedido informarProtocoloPedido(Long pedido, Long numeroProtocolo, String especialidade, String subnatureza, Cliente cliente) {
//        Map<String, Object> protocolo = new HashMap<>();
//        protocolo.put("protocolo",numeroProtocolo);
//        protocolo.put("tipo",especialidade);
//        protocolo.put("observacao","Protocolado sobre o nº " + numeroProtocolo);
//        protocolo.put("subnatureza", subnatureza);
//        if(cliente != null){
//            protocolo.put("apresentante_id", cliente.getId());
//        }
//        HttpMethod metodo = HttpMethod.PATCH;
//        return atualizarPedido(pedido, protocolo, metodo);
//    }
//
//
//    public Pedido criarPedido(Long numeroProtocolo, String especialidade, String subnatureza, Cliente cliente){
//
//        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
//        parts.add("protocolo",numeroProtocolo);
//        parts.add("tipo",especialidade);
//        parts.add( "observacao","Protocolado sobre o nº " + numeroProtocolo);
//        parts.add("subnatureza", subnatureza);
//        parts.add("apresentante_id", cliente.getId());
//
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//
//        HttpEntity<MultiValueMap<String, Object>> requestEntity =  new HttpEntity<MultiValueMap<String, Object>>(parts, headers);
//
//        ResponseEntity<Pedido> response =  restTemplate.exchange(configuracaoService.findConfiguracao().getUrlQrcode()+"pedido", HttpMethod.POST, requestEntity, Pedido.class);
//
//        return response.getBody();
//    }
//    public Pedido informarRegistroPedido(Long pedido, Long numerRegistro,String registro ,String especialidade, String hashRegistro) {
//        Map<String, Object> protocolo = new HashMap<>();
//        protocolo.put("numero_registro",numerRegistro);
//        protocolo.put("registro",registro);
//        protocolo.put("tipo",especialidade);
//        protocolo.put("hash", hashRegistro);
//        return atualizarPedido(pedido, protocolo, HttpMethod.PATCH);
//    }
//
//    public Pedido atualizarPedido(Long idPedido, Map<String, Object> protocolo, HttpMethod metodo){
//        String url = configuracaoService.findConfiguracao().getUrlQrcode()+"pedido/"+idPedido;
//        HttpEntity<Map<String, Object>> requestUpdate = new HttpEntity<Map<String, Object>>(protocolo, null);
//        ResponseEntity<Pedido> responseEntity = restTemplate.exchange(url, metodo, requestUpdate, Pedido.class);
//        return responseEntity.getBody();
//    }
//
//
//    public byte[] recuperarArquivoPedido(Long pedido) {
//        String url = configuracaoService.findConfiguracao().getUrlQrcode()+"pedido/"+pedido;
//        HttpHeaders headers = new HttpHeaders();
//        headers.setAccept(Arrays.asList(MediaType.APPLICATION_PDF));
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//        ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, byte[].class);
//
//        return response.getBody();
//    }
//
//    public byte[] recuperarArquivoMensagem(Long pedido, Long idMensagem) {
//        String url = configuracaoService.findConfiguracao().getUrlQrcode()+"pedido/"+pedido+"/mensagem/"+ idMensagem;
//        HttpHeaders headers = new HttpHeaders();
//        headers.setAccept(Arrays.asList(MediaType.APPLICATION_PDF));
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//        ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, byte[].class);
//
//        return response.getBody();
//    }
//
//
//    public void encaminharCertidao(Long idPedido, Long protocolo, String hashArquivo, CartorioParceiro cartorioParceiro) throws Exception{
//        PedidoMensagem mensagem = new PedidoMensagem();
//        mensagem.setTipo(new PedidoMensagemTipo(3L));
//        mensagem.setDestinatario(new PedidoMensagemRemetente(cartorioParceiro.getId()));
//        mensagem.setRemetente(new PedidoMensagemRemetente(cartorioParceiro.getId()));
//        mensagem.setData(LocalDateTime.now());
//        mensagem.setConteudo("Encaminhar certidão para cartório responsável: " + cartorioParceiro.getNome() + "(" + cartorioParceiro.getEmail() + ")");
//        mensagem.setHash(hashArquivo);
//
//
//        String url = configuracaoService.findConfiguracao().getUrlQrcode() + "pedido/"+idPedido+ "/mensagem";
//        HttpEntity<PedidoMensagem> entity = new HttpEntity<PedidoMensagem>(mensagem);
//        ResponseEntity<PedidoMensagem> retorno = restTemplate.postForEntity(url, entity, PedidoMensagem.class);
//    }
//
//    public boolean cadastrarParceiro(CartorioParceiro parceiro) throws Exception{
//        String url = configuracaoService.findConfiguracao().getUrlQrcode() + "usuario/";
//        PedidoMensagemRemetente novoRemetente = new PedidoMensagemRemetente(parceiro.getId(), parceiro.getNome(), parceiro.getEmail());
//        HttpEntity<PedidoMensagemRemetente> entity = new HttpEntity<PedidoMensagemRemetente>(novoRemetente);
//        ResponseEntity<PedidoMensagemRemetente> retorno = restTemplate.postForEntity(url, entity, PedidoMensagemRemetente.class);
//        return true;
//    }
//
//    public Pedido recuperaPedido(Long idPedido) {
//        ResponseEntity<Pedido> responseEntity = restTemplate.getForEntity(configuracaoService.findConfiguracao().getUrlQrcode()+"pedido/"+idPedido, Pedido.class);
//        Pedido retornoImportador = responseEntity.getBody();
//        return retornoImportador;
//    }
//
//    public void enviarMensagem(Long idPedido, PedidoMensagemTipo mensagemTipo, String texto, File arquivo) throws IOException {
//        PedidoMensagem mensagem = new PedidoMensagem();
//        mensagem.setTipo(mensagemTipo);
//        mensagem.setData(LocalDateTime.now());
//        mensagem.setConteudo(texto);
//        mensagem.setArquivo(DatatypeConverter.printBase64Binary(IOUtils.toByteArray(new FileInputStream(arquivo))));
//        String url = configuracaoService.findConfiguracao().getUrlQrcode() + "pedido/"+idPedido+ "/mensagem";
//        HttpEntity<PedidoMensagem> entity = new HttpEntity<PedidoMensagem>(mensagem);
//        ResponseEntity<PedidoMensagem> retorno = restTemplate.postForEntity(url, entity, PedidoMensagem.class);
//    }

    public enum  StatusPedido{
        PROTOCOLO("AGUARDANDO PROTOCOLAR", "PROTOCOLADO",1),
        REGISTRO("AGUARDANDO REGISTRAR","REGISTRADO",4),
        CERTIDAO_ENCAMINHADA("AGUARDANDO ENCAMINHAR CERTIDÃO", "CERTIDÃO ENCAMINHADA",7),
        ORCAMENTO("AGUARDANDO ORÇAMENTO", "ORÇAMENTO INFORMADO",2),
        PAGAMENTO("AGUARDANDO PAGAMENTO", "PAGAMENTO EFETUADO",3),
        AVERBACAO("AGUARDANDO AVERBAR","AVERBADO À MARGEM", 9)
        ;

        private String nome;
        private String nomeAguardando;
        private int nrOrdem;

        StatusPedido(String nomeAguardando, String nome, int nrOrdem) {
            this.nomeAguardando = nomeAguardando;
            this.nome = nome;
            this.nrOrdem = nrOrdem;
        }

        public String getNome() {
            return nome;
        }

        public String getNomeAguardando() {
            return nomeAguardando;
        }

        public int getNrOrdem() {
            return nrOrdem;
        }
    }
}


/*
<!DOCTYPE html>
<html>
<head>
</head>
<body>
<p style="text-align: center;">ESTADO DE {{CARTORIO_PARCEIRO.ESTADO}}<br />MUNIC&Iacute;PIO E COMARCA DE {{CARTORIO_PARCEIRO.CIDADE}}<br /><strong>{{CARTORIO_PARCEIRO.NOME}}</strong></p>
<p style="text-align: center;">{{CARTORIO_PARCEIRO.OFICIAL}}<br />Oficial Registrador</p>
<p style="text-align: center;"><strong>CERTID&Atilde;O DE REGISTRO</strong></p>
<p style="text-align: justify;">Certifico, a requerimento da parte interessada, que na data de {{DATA_ATUAL}}, foi protocolado sob n<sup>o</sup> _____________ O REGISTRO DE T&Iacute;TULOS E DOCUMENTOS, sendo registrado na data {{DATA_ATUAL}}, sob n<sup>o</sup> _________________, o documento com os seguintes dados:</p>
<p style="text-align: justify;">Apresentante: {{NOME_CARTORIO}}</p>
<p style="text-align: justify;">Natureza do t&iacute;tulo: {{NATUREZA}} n<sup>o</sup> {{DOCUMENTO_NUMERO}} EMITIDO EM {{DOCUMENTO_DATA}} no valor de R$ {{DOCUMENTO_VALOR}}</p>
<p style="text-align: justify;">Indicadores:{% for parte in PARTES %}{{parte.NOME}},{{parte.QUALIDADE}};{% endfor %}</p>
<p style="text-align: justify;">Caracteristicas:&nbsp;</p>
<p style="text-align: center;">{{CARTORIO_PARCEIRO.cidade}} -&nbsp; {{CARTORIO_PARCEIRO.ESTADO}}, {{DATA_ATUAL_EXTENSO}}</p>
<p style="text-align: left;"><strong>Emolumentos:</strong></p>
</body>
</html>
*/
