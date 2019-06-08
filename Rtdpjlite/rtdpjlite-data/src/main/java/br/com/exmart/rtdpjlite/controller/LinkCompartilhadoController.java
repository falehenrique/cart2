package br.com.exmart.rtdpjlite.controller;

import br.com.exmart.rtdpjlite.annotations.NotFoundException;
import br.com.exmart.rtdpjlite.model.LinkCompartilhado;
import br.com.exmart.rtdpjlite.model.LinkCompartilhadoRegistro;
import br.com.exmart.rtdpjlite.model.Registro;
import br.com.exmart.rtdpjlite.model.TipoProtocolo;
import br.com.exmart.rtdpjlite.model.portal.Pedido;
import br.com.exmart.rtdpjlite.model.portal.PedidoProtocolo;
import br.com.exmart.rtdpjlite.model.portal.PedidoProtocoloStatus;
import br.com.exmart.rtdpjlite.service.ArquivoService;
import br.com.exmart.rtdpjlite.service.LinkCompartilhadoService;
import br.com.exmart.rtdpjlite.service.RegistroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/portal/link")
public class LinkCompartilhadoController {
    @Autowired
    private LinkCompartilhadoService linkCompartilhadoService;
    @Autowired
    private RegistroService registroService;
    @Autowired
    private ArquivoService arquivoService;

    @GetMapping("/{id}")
    public List<LinkCompartilhadoRegistro> findRegistros(@PathVariable String id){
        return linkCompartilhadoService.findById(id).getRegistros();
    }
    @GetMapping("/{registroId}/download")
    public ResponseEntity<Resource> download(@PathVariable Long registroId) throws FileNotFoundException {
        Registro registro = registroService.findById(registroId);
        if(registro == null)
            throw new NotFoundException("Registro não encontrado");
        File file = arquivoService.recuperarDocumentoProtocoloRegistroFile(registro.getRegistro(),registro.getNumeroRegistro(), registro.getEspecialidade(),TipoProtocolo.recuperaTipoPrenotacaoByEspecialidade(registro.getEspecialidade()));

        if(file == null){
            throw new NotFoundException("Arquivo não encontrado");
        }
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }
}
