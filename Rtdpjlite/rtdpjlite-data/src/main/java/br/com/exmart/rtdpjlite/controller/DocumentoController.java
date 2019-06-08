package br.com.exmart.rtdpjlite.controller;

import br.com.exmart.rtdpjlite.annotations.NotFoundException;
import br.com.exmart.rtdpjlite.model.Protocolo;
import br.com.exmart.rtdpjlite.model.ProtocoloArquivoHash;
import br.com.exmart.rtdpjlite.model.Registro;
import br.com.exmart.rtdpjlite.model.TipoProtocolo;
import br.com.exmart.rtdpjlite.repository.ProtocoloArquivoHashRepository;
import br.com.exmart.rtdpjlite.service.ProtocoloService;
import br.com.exmart.rtdpjlite.service.RegistroService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@RestController
@RequestMapping("/documento")
public class DocumentoController {

    protected static Logger logger = LoggerFactory.getLogger(DocumentoController.class);
    @Autowired
    private ProtocoloArquivoHashRepository protocoloArquivoHashRepository;
    @Autowired
    private ProtocoloService protocoloService;
    @Autowired
    private RegistroService registroService;

    @RequestMapping(value = "/validar/{hash}", produces = "application/json")
    public Object getFromHash(@PathVariable String hash){
        ProtocoloArquivoHash hashBanco = protocoloArquivoHashRepository.findByHash(hash);
        if(hashBanco == null){
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }

        Protocolo protocolo =  protocoloService.findOne(hashBanco.getProtocoloId());
        return protocolo;
    }

    @GetMapping(value = "/registro/{natureza}/{registro}", produces = {MediaType.APPLICATION_PDF_VALUE})
    public Object getFromRegistro(@PathVariable String registro, @PathVariable String natureza) throws IOException {
        Registro registroDb = registroService.findByRegistroAndEspecialidade(registro, natureza);
        if(registroDb == null){
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }
        HttpHeaders respHeaders = new HttpHeaders();
        respHeaders.setContentType(MediaType.APPLICATION_PDF);
        respHeaders.setContentDispositionFormData("attachment", registro+".PDF");

        InputStreamResource isr = null;

        try {
            isr = new InputStreamResource(registroService.recuperarArquivo(registroDb));
            return new ResponseEntity<InputStreamResource>(isr, respHeaders, HttpStatus.OK);
        }catch (NullPointerException npe){
            return new ResponseEntity<Object>("Arquivo não encotnrado", HttpStatus.NOT_FOUND);
//            throw  new NotFoundException("Arquivo não encontrado");
        }

    }


    @GetMapping(value = "/validar/{hash}", produces = {MediaType.APPLICATION_PDF_VALUE})
    public Object getArquivoFromHash(@PathVariable String hash) throws IOException {
        logger.info("Entrou");
        ProtocoloArquivoHash hashBanco = protocoloArquivoHashRepository.findByHash(hash);
        if(hashBanco == null){
            return new ResponseEntity<Object>("Arquivo não encotnrado", HttpStatus.NOT_FOUND);
        }

        HttpHeaders respHeaders = new HttpHeaders();
        respHeaders.setContentType(MediaType.APPLICATION_PDF);
        respHeaders.setContentDispositionFormData("attachment", hash+".PDF");

        InputStreamResource isr = null;

        Protocolo protocolo =  protocoloService.findOne(hashBanco.getProtocoloId());
        try {
            if (protocolo.getTipo().equals(TipoProtocolo.CERTIDAO_PJ) || protocolo.getTipo().equals(TipoProtocolo.CERTIDAO_TD)) {
                isr = new InputStreamResource(protocoloService.recuperarArquivoCertidaoAssinado(protocolo));
            } else {
                Registro registro = registroService.findByProtocolo(protocolo.getId());

                isr = new InputStreamResource(registroService.recuperarArquivo(registro));
            }
            return new ResponseEntity<InputStreamResource>(isr, respHeaders, HttpStatus.OK);
        }catch (NullPointerException npe){
            return new ResponseEntity<Object>("Arquivo não encotnrado", HttpStatus.NOT_FOUND);
//            throw  new NotFoundException("Arquivo não encontrado");
        }
//        return IOUtils.toByteArray(protocoloService.recuperarArquivoCertidaoAssinado(protocolo));

    }

    @RequestMapping("/limpar/cache/{tipoId}")
    @CacheEvict(value = "naturezaByTipo", key = "#tipoId")
    public String limparCache(@PathVariable  Long tipoId){
        return "Foi";
    }


    @RequestMapping("/limpar/cache")
    @CacheEvict(value = "naturezaCache")
    public String limparCaches(){
        return "Foi";
    }
}
