package br.com.exmart.rtdpjlite.service;

import br.com.exmart.rtdpjlite.model.*;
import br.com.exmart.rtdpjlite.repository.LoteItemRepository;
import br.com.exmart.rtdpjlite.repository.LoteRepository;
import br.com.exmart.rtdpjlite.repository.ProtocoloRepository;
import br.com.exmart.rtdpjlite.repository.StatusRepository;
import br.com.exmart.rtdpjlite.util.DocumentoUtil;
import br.com.exmart.rtdpjlite.util.Utils;
import br.com.exmart.rtdpjlite.vo.ArquivoCsvImportacao;
import br.com.exmart.rtdpjlite.vo.StatusProtocoloJson;
import br.com.exmart.rtdpjlite.vo.financeiro.CustasCartorio;
import br.com.exmart.rtdpjlite.vo.financeiro.ServicoCalculado;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class LoteService {

    @Autowired
    private RegistroService registroService;
    @Autowired
    SubNaturezaService subNaturezaService;
    @Autowired
    NaturezaService naturezaService;
    @Autowired
    StatusRepository statusRepository;
    @Autowired
    QualidadeService qualidadeService;
    @Autowired
    FormaEntregaService formaEntregaService;
    @Autowired
    ProtocoloService protocoloService;
    @Autowired
    ModeloService modeloService;
    @Autowired
    FinanceiroService financeiroService;
    @Autowired
    ProtocoloRepository protocoloRepository;
    @Autowired
    UsuarioNotificacaoService usuarioNotificacaoService;
    @Autowired
    DocumentoUtil documentoUtil;
    @Autowired
    LoteItemRepository loteItemRepository;
    @Autowired
    LoteRepository loteRepository;
    @Autowired
    private ArquivoService arquivoService;

    protected static Logger logger = LoggerFactory.getLogger(LoteService.class);

    public List<ArquivoCsvImportacao> importarProtocolo(Cliente cliente, String diretorio, Status statusRegistrado, List<ArquivoCsvImportacao> arquivoImportarList, List<ServicoCalculado> servicosProtocolo, Usuario usuarioLogado, List<CustasCartorio> custasCartorio) throws Exception{
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start = LocalDateTime.now();
        logger.debug("Comecou : " + start.format(formatter));

        Lote lote = new Lote();
        lote.setUsuario(usuarioLogado);
        lote.setCliente(cliente);
        lote.setDia(LocalDateTime.now());
        loteRepository.save(lote);
        for(ArquivoCsvImportacao arquivoImportar: arquivoImportarList){
            synchronized (this) {
                LoteItem loteItem = new LoteItem();
                loteItem.setLote(lote);
                loteItem.setInicio(LocalDateTime.now());
                loteItem.setNomeIgnorar(arquivoImportar.getParteIgnorar());
                try{
                    SubNatureza subNatureza = subNaturezaService.findByNome(arquivoImportar.getNatureza().trim());
                    if(subNatureza == null){
                        throw new Exception("Subnatureza não encontrada: " + arquivoImportar.getNatureza().trim());
                    }
                    Natureza natureza = naturezaService.findBySubnatureza(subNatureza);
                    File arquivoProtocolo = new File(diretorio + File.separator + arquivoImportar.getArquivo());

                    File TA_ArquivoProtocolo = new File(diretorio + File.separator + "TA_" + arquivoImportar.getArquivo());
                    File TE_ArquivoProtocolo = new File(diretorio + File.separator + "TE_" + arquivoImportar.getArquivo());

                    loteItem.setArquivo(arquivoProtocolo.getAbsoluteFile().toString());
                    loteItem.setTermoAbertura(TA_ArquivoProtocolo.getAbsoluteFile().toString());
                    loteItem.setTermoEncerramento(TE_ArquivoProtocolo.getAbsoluteFile().toString());

                    List<String> arquivosMerge = new ArrayList<>();
                    arquivosMerge.add(TA_ArquivoProtocolo.getAbsoluteFile().toString());
                    arquivosMerge.add(arquivoProtocolo.getAbsoluteFile().toString());
                    arquivosMerge.add(TE_ArquivoProtocolo.getAbsoluteFile().toString());

                    Protocolo protocolo = new Protocolo();
                    protocolo.setTipo(TipoProtocolo.PRENOTACAO_TD);
                    protocolo.getStatusProtocolo().add(new StatusProtocolo(statusRepository.findByNome("PROTOCOLADO"), LocalDateTime.now(), null, null, usuarioLogado));

//				File retorno = pdfService.pdfMergeAllgsWithoutPdfA(arquivosMerge, diretorio + File.separator + "merge_" + arquivoImportar.getArquivo());


                    protocolo.setDataDoc(Utils.dataFromString(arquivoImportar.getDataDocumento()));
                    Qualidade qualidadeDiretor = qualidadeService.findByNome("Representante");
                    Parte parteDiretor = new Parte();
                    parteDiretor.setNome(arquivoImportar.getParte());
                    parteDiretor.setCpfCnpj("999.999.999-99");
                    protocolo.getPartesProtocolo().add(new ParteProtocolo(parteDiretor, qualidadeDiretor));

                    Qualidade qualidadeDenominacaoAtual = qualidadeService.findByNome("DENOMINAÇÃO ATUAL");
                    Parte parteDenominacaoAtual = new Parte();
                    parteDenominacaoAtual.setNome(arquivoImportar.getParte2());
                    parteDenominacaoAtual.setCpfCnpj(arquivoImportar.getParte2Documento());
                    protocolo.getPartesProtocolo().add(new ParteProtocolo(parteDenominacaoAtual, qualidadeDenominacaoAtual));


                    protocolo.setNatureza(natureza);
                    protocolo.setSubNatureza(subNatureza);
                    protocolo.setFormaEntrega(formaEntregaService.findByNome("Digital"));

                    protocolo.setDataProtocolo(LocalDateTime.now());

                    protocolo.setParte(arquivoImportar.getParte2());
                    protocolo.setParteDocumento(arquivoImportar.getParte2Documento());

                    protocolo.setApresentante(arquivoImportar.getParte2());

                    if (cliente != null) {
                        protocolo.setCliente(cliente);
                        protocolo.setApresentante(cliente.getApresentante());
                    }


                    logger.debug("entrou Protocolo sem Numero ");
                    protocoloService.save(protocolo, ProtocoloService.ACAO.SALVAR,  servicosProtocolo, false, true, null, usuarioLogado);
                    loteItem.setDtProtocolo(protocolo.getDataProtocolo());
                    loteItem.setProtocoloId(protocolo.getId());
                    arquivoImportar.setProtocoloId(protocolo.getId());
                    logger.debug("Sai Protocolo com Numero "+ protocolo.getNumero());

//FIXME DEVE SETAR O TEXT DO CARIMBO NO PROTOCOLO

                    List<Modelo> modelos = modeloService.findModeloByTipo(natureza.getId(), subNatureza.getId(), TipoModelo.CARIMBO);
                    JtwigModel jtwigModel = documentoUtil.iniciarVariaveisModelo(null, protocolo, new ArrayList<>(), custasCartorio, null, protocolo.getPartesProtocolo(), protocolo.getObjetos(), protocolo.getIntimacaosProtocolo(), null);
                    JtwigTemplate jtwigTemplate =JtwigTemplate.inlineTemplate(modelos.get(0).getModelo());
                    protocolo.setTextoCarimbo(jtwigTemplate.render(jtwigModel));
//
//				recuperarHashArquivo(protocolo.getId(), null);

                    StatusProtocoloJson statusJson = new StatusProtocoloJson();
                    statusJson.setTexto(protocolo.getNatureza().getCarimbo().getCarimbo());
                    logger.debug("Entrou sem Registro");

                    protocoloService.save(protocolo, ProtocoloService.ACAO.REGISTRAR, financeiroService.listarServicosProtocolo(protocolo.getNumero(), protocolo.getTipo()), false, true, statusJson, usuarioLogado);
//                protocoloRepository.flush();
                    loteItem.setDtRegistro(protocolo.getDtRegistro());

//TODO DEVE INFORMAR AO CI PARA ENVIAR O PROTOCOLO PRA CONTA DO CLIENTE
                    logger.debug("Faturou protocolo " +financeiroService.faturarProtocoloCliente(protocolo.getNumero(), cliente, usuarioLogado, protocolo.getTipo()));
                    loteItem.setDtFaturado(LocalDateTime.now());
                    logger.debug("Saiu com registro: " + protocolo.getNumeroRegistro());

                    loteItemRepository.save(loteItem);
                }catch (Exception e){
                    if(loteItem.getProtocoloId() != null) {
                        String erro = "";
                        if(loteItem.getDtRegistro() == null ){
                            erro = erro + " [[ erro ao registrar ]]  ";
                        }else  if(loteItem.getDtFaturado() == null){
                            erro = erro + " [[ erro ao Faturar ]]  ";
                        }
                        erro = erro + e.getMessage();
                        loteItem.setResultado(erro);
                        loteItemRepository.save(loteItem);
                        usuarioNotificacaoService.adicionarNotificacao(usuarioLogado.getId(), "Processamento LoteItem", "Erro ao processar o registro: "+erro);
                    }else{
                        usuarioNotificacaoService.adicionarNotificacao(usuarioLogado.getId(), "Processamento LoteItem", "Erro ao processar o registro"+arquivoImportar.toString());
                        throw new Exception(e.getMessage());
                    }
                    e.printStackTrace();
                }
            }
        }
        usuarioNotificacaoService.adicionarNotificacao(usuarioLogado.getId(), "Processamento LoteItem", "O arquivo do cliente "+ cliente.getNome() + ", foi processado com sucesso.");
        LocalDateTime end = LocalDateTime.now();

        logger.debug("Acabou : " + end.format(formatter));
        logger.debug("demorou: "+ChronoUnit.MILLIS.between(start,end) / 60000+ " minutos " );

        return arquivoImportarList;

    }


    @Async
    public void processarArquivosLote(){//List<ArquivoCsvImportacao> arquivoImportarList, String diretorio, Usuario usuarioLogado){
        Thread one = new Thread() {
            @Override
            public void run() {
            logger.debug("comecou processar arquivos");
            List<CustasCartorio> custas = financeiroService.findAllCustas();
            for (LoteItem loteItem : loteItemRepository.findByFinalizadoIsNullAndResultadoIsNull()) {
                protocoloService.processarArquivoLote(loteItem.getLote(),loteItem, custas);

            }
            logger.debug("acabou processar arquivos");
            }
        };
        one.start();
    }

    public File gerarArquivoDonload(Lote lote) throws IOException {
        if(lote == null)
            return null;
        logger.debug("Iniciado gerar zip");
        String diretorioLote = arquivoService.diretorioLote()+"zip"+File.separator;
        String diretorioLoteZip = diretorioLote+lote.getId()+File.separator;


        List<LoteItem> items = loteItemRepository.findByLoteOrderById(lote);

        List<ArquivoCsvImportacao> arquivoTxt = new ArrayList<>();

        for(LoteItem item : items){
            Protocolo protocolo = protocoloService.findById(item.getProtocoloId());
            Registro registro = registroService.findByProtocolo(protocolo.getId());

            File arquivoRegistro = arquivoService.recuperarDocumentoProtocoloRegistroFile(registro.getRegistro(), registro.getNumeroRegistro(),registro.getEspecialidade(),null);
            FileUtils.copyFile(arquivoRegistro, new File(diretorioLoteZip+FilenameUtils.getName(item.getArquivo())));


            String nomeParte1 = null;
            String nomeParte2 = null;
            String documentoParte2 = null;

            for(IndicadorPessoal indicadorPessoal : registro.getIndicadorPessoal()){
                if(indicadorPessoal.getCpfCnpj().equalsIgnoreCase("999.999.999-99")){
                    nomeParte1 = indicadorPessoal.getNome() ;
                }else{
                    nomeParte2 = indicadorPessoal.getNome();
                    documentoParte2 = indicadorPessoal.getCpfCnpj();
                }
            }

            arquivoTxt.add(new ArquivoCsvImportacao(
                    registro.getSubNatureza().getNome(),
                    nomeParte1,
                    item.getNomeIgnorar(),
                    nomeParte2,
                    documentoParte2,
                    Utils.formatarData(protocolo.getData_doc()),
                    FilenameUtils.getName(item.getArquivo()),
                    registro.getNumeroRegistro().toString(),
                    Utils.formatarData(registro.getDataRegistro())
            ));
        }



        try {
            Writer writer = null;
            BufferedWriter out = null;
            writer = new OutputStreamWriter(new FileOutputStream(diretorioLoteZip + "indice.txt"), StandardCharsets.UTF_8);


            StatefulBeanToCsv<ArquivoCsvImportacao> csv = new StatefulBeanToCsvBuilder<ArquivoCsvImportacao>(writer).withSeparator(';').withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).build();
            csv.write(arquivoTxt);
            writer.close();
        } catch (CsvDataTypeMismatchException e) {
            e.printStackTrace();
        } catch (CsvRequiredFieldEmptyException e) {
            e.printStackTrace();
        }


        FileOutputStream fos = new FileOutputStream(diretorioLote+lote.getId()+".zip");
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        File fileToZip = new File(diretorioLoteZip);

        zipFile(fileToZip, fileToZip.getName(), zipOut);
        zipOut.close();
        fos.close();
        FileUtils.deleteDirectory(fileToZip);
        logger.debug("Finalizado gerar zip");

        return new File(diretorioLote+lote.getId()+".zip");

    }


    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                zipOut.closeEntry();
            }
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }

    public List<Lote> findAll() {
        return loteRepository.findAll();
    }

    @Scheduled(cron = "0 50 21 ? * *")
    public void limparZips(){
        String diretorioLote = arquivoService.diretorioLote()+"zip"+File.separator;
        try {
            FileUtils.deleteDirectory(new File(diretorioLote));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
