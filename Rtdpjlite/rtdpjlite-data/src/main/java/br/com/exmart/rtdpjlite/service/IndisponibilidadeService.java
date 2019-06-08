package br.com.exmart.rtdpjlite.service;

import br.com.exmart.rtdpjlite.model.Registro;
import br.com.exmart.rtdpjlite.service.rest.indisponibilidade.IndisponibilidadeRest;
import br.com.exmart.rtdpjlite.vo.indisponibilidade.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class IndisponibilidadeService {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private IndisponibilidadeRest indisponibilidadeRest;
    @Autowired
    private ConfiguracaoService configuracaoService;
    @Autowired
    private IndicadorPessoalService indicadorPessoalService;
    @Autowired
    private RegistroService registroService;

    public RetornoIndisponibilidade verificarIndisponibilidade() throws Exception {
        List<IndisponibilidadeVO> indisponibilidadeVOList = indisponibilidadeRest.recuperarUltimosAdicionados(configuracaoService.findConfiguracao().getDtUltimaConsultaIndisponibilidade());
        List<ParteIndisponivel> partesEncontradasIndisponibilidade = new ArrayList<>();
        List<ParteIndisponivel> partesEncontradasCancelamento = new ArrayList<>();
        List<ParteIndisponivel> partesEncontradasIndisponibilidadeSemDocumento = new ArrayList<>();
        List<ParteIndisponivel> partesEncontradasCancelamentoSemDocumento  = new ArrayList<>();
        List<ParteIndisponivel> partesNaoEncontradas  = new ArrayList<>();

        List<PessoaXMLVO> marcarConcluido = new ArrayList<>();


        for(IndisponibilidadeVO indisponibilidadeVO : indisponibilidadeVOList){

            String tipo = "INDISPONIVEL";
            if(indisponibilidadeVO.getCancelamentoData() != null) {
                tipo = "CANCELADO";
            }
            for(PessoaXMLVO parte : indisponibilidadeVO.getPartes()) {
                List<Registro> registroList = registroService.findByIndicadorPessoalDocumentoAndEspecialidade(parte.getCpf_cnpj(), "PJ" ,parte.getProtocolosEncontrados(), parte.getRegistrosEncontrados());
                boolean icAchouPorDocumento = false;
                boolean icAchouPorNome = false;
                if(registroList.size() > 0) {
                    icAchouPorDocumento = true;
                    List<ParteIndisponivelRegistro> parteIndisponivelRegistros = new ArrayList<>();
                    for (Registro registro : registroList) {
                        parteIndisponivelRegistros.add(new ParteIndisponivelRegistro(registro.getNumeroRegistro(), registro.getRegistro(), registro.getNrPastaPj(), registro.getId()));
                    }
                    ParteIndisponivel parteIndisponivel = new ParteIndisponivel(
                            parte.getId(),
                            parte.getNome(),
                            parte.getNome_fonetico(),
                            parte.getCpf_cnpj(),
                            indisponibilidadeVO.getProtocoloIndisponibilidade(),
                            indisponibilidadeVO.getProtocoloCancelamentoIndisponibilidade(),
                            indisponibilidadeVO.getCancelamentoTipo(),
                            indisponibilidadeVO.getCancelamentoData(),
                            indisponibilidadeVO.getDataPedido(),
                            indisponibilidadeVO.getNumeroProcesso(),
                            indisponibilidadeVO.getTelefone(),
                            indisponibilidadeVO.getNomeInstituicao(),
                            indisponibilidadeVO.getForumVara(),
                            indisponibilidadeVO.getUsuario(),
                            indisponibilidadeVO.getEmail(),
                            parteIndisponivelRegistros,
                            tipo,
                            indisponibilidadeVO.getId()
                            );
                    if(indisponibilidadeVO.getCancelamentoData() == null) {
                        partesEncontradasIndisponibilidade.add(parteIndisponivel);
                    }else{
                        partesEncontradasCancelamento.add(parteIndisponivel);
                    }
                }


                List<Registro> registroSemDocumentoList = registroService.findByIndicadorPessoalNomeFoneticoAndEspecialidadeIn(parte.getNome_fonetico(), "PJ", parte.getRegistrosNegativos());
                if(registroSemDocumentoList.size() > 0) {
                    icAchouPorNome = true;
                    List<ParteIndisponivelRegistro> parteIndisponivelRegistros = new ArrayList<>();
                    for (Registro registro : registroSemDocumentoList) {
                        parteIndisponivelRegistros.add(new ParteIndisponivelRegistro(registro.getNumeroRegistro(), registro.getRegistro(), registro.getNrPastaPj(), registro.getId()));
                    }
                    ParteIndisponivel parteIndisponivel = new ParteIndisponivel(
                            parte.getId(),
                            parte.getNome(),
                            parte.getNome_fonetico(),
                            parte.getCpf_cnpj(),
                            indisponibilidadeVO.getProtocoloIndisponibilidade(),
                            indisponibilidadeVO.getProtocoloCancelamentoIndisponibilidade(),
                            indisponibilidadeVO.getCancelamentoTipo(),
                            indisponibilidadeVO.getCancelamentoData(),
                            indisponibilidadeVO.getDataPedido(),
                            indisponibilidadeVO.getNumeroProcesso(),
                            indisponibilidadeVO.getTelefone(),
                            indisponibilidadeVO.getNomeInstituicao(),
                            indisponibilidadeVO.getForumVara(),
                            indisponibilidadeVO.getUsuario(),
                            indisponibilidadeVO.getEmail(),
                            parteIndisponivelRegistros,
                            tipo,
                            indisponibilidadeVO.getId()
                    );
                    if(indisponibilidadeVO.getCancelamentoData() == null) {
                        partesEncontradasIndisponibilidadeSemDocumento.add(parteIndisponivel);
                    }else {
                        partesEncontradasCancelamentoSemDocumento.add(parteIndisponivel);
                    }
                }

                if(!icAchouPorDocumento && !icAchouPorNome){
                    partesNaoEncontradas.add(new ParteIndisponivel(
                            parte.getId(),
                            parte.getNome(),
                            parte.getNome_fonetico(),
                            parte.getCpf_cnpj(),
                            indisponibilidadeVO.getProtocoloIndisponibilidade(),
                            indisponibilidadeVO.getProtocoloCancelamentoIndisponibilidade(),
                            indisponibilidadeVO.getCancelamentoTipo(),
                            indisponibilidadeVO.getCancelamentoData(),
                            indisponibilidadeVO.getDataPedido(),
                            indisponibilidadeVO.getNumeroProcesso(),
                            indisponibilidadeVO.getTelefone(),
                            indisponibilidadeVO.getNomeInstituicao(),
                            indisponibilidadeVO.getForumVara(),
                            indisponibilidadeVO.getUsuario(),
                            indisponibilidadeVO.getEmail(),
                            new ArrayList<>(),
                            tipo,
                            indisponibilidadeVO.getId()
                    ));
                    marcarConcluido.add(parte);
                }
            }
        }

        logger.debug("Indisponivel");
//        for(ParteIndisponivel parteIndisponivel : partesEncontradasIndisponibilidade){
//            logger.debug(parteIndisponivel.toString());
//        }
//        logger.debug("Indisponivel Sem Documento");
//        for(ParteIndisponivel parteIndisponivel : partesEncontradasIndisponibilidadeSemDocumento){
//            logger.debug(parteIndisponivel.toString());
//        }
//        logger.debug("Cancelamento");
//        for(ParteIndisponivel parteIndisponivel : partesEncontradasCancelamento){
//            logger.debug(parteIndisponivel.toString());
//        }
//        logger.debug("Cancelamento Sem Documento");
//        for(ParteIndisponivel parteIndisponivel : partesEncontradasCancelamentoSemDocumento){
//            logger.debug(parteIndisponivel.toString());
//        }
        if(marcarConcluido.size() > 0){
            for(PessoaXMLVO pessoaMarcarConcluido : marcarConcluido){
                indisponibilidadeRest.marcarComoFinalizado(pessoaMarcarConcluido.getId());
            }
        }
        return new RetornoIndisponibilidade(partesEncontradasIndisponibilidade,
                partesEncontradasCancelamento,
                partesEncontradasIndisponibilidadeSemDocumento,
                partesEncontradasCancelamentoSemDocumento,
                partesNaoEncontradas);

    }

    public boolean informarProtocolado(Long idParte, List<IndisponibilidadePessoaProtocoloVO> protocolos) {
        return this.indisponibilidadeRest.informarProtocolado(idParte, protocolos);
    }


    public boolean marcarComoFinalizado(Long idParte){
        return this.indisponibilidadeRest.marcarComoFinalizado(idParte);
    }

    public boolean informarRegistroNegativo(Long idParte, List<IndisponibilidadePessoaRegistroNegativoVO> registros){
        return this.indisponibilidadeRest.informarRegistroNegativo(idParte, registros);
    }

    public boolean isIndisponivel(String documento){
        //FIXME retornando penas true ou face
        try {
            List<PessoaIndisponibilidadeVO> retorno = indisponibilidadeRest.isIndisponivel(documento);
            return retorno.size()>0;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

//    @Async
    public ResultadoImportacaoIndisponibilidadeXMLVO enviarArquivoIndisponibilidade(File arquivo, Runnable callback) throws Exception {
        return this.indisponibilidadeRest.enviarArquivoIndisponibilidade(arquivo);
//        if(callback != null)
//            callback.run();
//        return retorno;
    }
}
