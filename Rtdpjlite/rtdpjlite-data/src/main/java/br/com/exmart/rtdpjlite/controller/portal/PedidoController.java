package br.com.exmart.rtdpjlite.controller.portal;

import br.com.exmart.rtdpjlite.annotations.NotFoundException;
import br.com.exmart.rtdpjlite.model.Protocolo;
import br.com.exmart.rtdpjlite.model.Registro;
import br.com.exmart.rtdpjlite.model.TipoProtocolo;
import br.com.exmart.rtdpjlite.model.portal.Pedido;
import br.com.exmart.rtdpjlite.model.portal.PedidoProtocolo;
import br.com.exmart.rtdpjlite.model.portal.PedidoProtocoloStatus;
import br.com.exmart.rtdpjlite.model.portal.UsuarioPortal;
import br.com.exmart.rtdpjlite.repository.portal.PedidoVoRepository;
import br.com.exmart.rtdpjlite.repository.portal.UsuarioPortalRepository;
import br.com.exmart.rtdpjlite.service.ArquivoService;
import br.com.exmart.rtdpjlite.service.AssinaturaService;
import br.com.exmart.rtdpjlite.service.ProtocoloService;
import br.com.exmart.rtdpjlite.service.RegistroService;
import br.com.exmart.rtdpjlite.service.enums.Diretorio;
import br.com.exmart.rtdpjlite.service.rest.balcaoonline.PedidoService;
import br.com.exmart.rtdpjlite.vo.portal.PedidoVO;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

@RestController
@RequestMapping("/portal/pedido")
public class PedidoController {
    @Autowired
    private PedidoService pedidoService;
    @Autowired
    private PedidoVoRepository pedidoVoRepository;
    @Autowired
    private ArquivoService arquivoService;
    @Autowired
    private AssinaturaService assinaturaService;
    @Autowired
    private ProtocoloService protocoloService;
    @Autowired
    private RegistroService registroService;

    @Autowired
    private UsuarioPortalRepository usuarioPortalRepository;



    @PostMapping("/")
    public Long novo(@RequestAttribute("usuarioId") Long usuarioId, @RequestParam(value = "arquivo", required = true) MultipartFile file, @RequestParam(value = "parte", required = true) String parte, @RequestParam(value = "parteDocumento", required = true) String parteDocumento) {

        UsuarioPortal usuario = usuarioPortalRepository.findOne(usuarioId);
        Long pedidoId =  pedidoService.criarPedido(usuario.getCliente(), null,"Em análise", null, null, usuario,null, parte, parteDocumento);
        String diretorio = arquivoService.recuperarDiretorioBase() + Diretorio.PEDIDO.getDiretorio() + File.separator + pedidoId + File.separator;
        String nomeArquivo = pedidoId+".PDF";
        try {
            Files.createDirectories(new File(diretorio).toPath());
            FileOutputStream fos = new FileOutputStream(diretorio + File.separator + nomeArquivo);
            fos.write(file.getBytes());
            fos.close();
        } catch (RuntimeException e) {
            throw new NotFoundException("Arquivo não encontrado: " + nomeArquivo);
        }catch (IOException e){
            throw new NotFoundException("Erro ao salvar o arquivo: " + nomeArquivo);
        }
        return pedidoId;
    }

    @GetMapping("/")
    public List<PedidoVO> findByCliente(@RequestAttribute("usuarioId") Long usuarioId, @RequestParam(name = "q", required = false) String query){
        if(Strings.isNullOrEmpty(query))
            return pedidoService.find(usuarioPortalRepository.findOne(usuarioId));
        return pedidoService.find(usuarioPortalRepository.findOne(usuarioId), query);
    }

    @GetMapping("/{id}")
    public Pedido findById(@PathVariable Long id, @RequestAttribute("usuarioId") Long usuarioId){
        return pedidoService.findById(id, usuarioPortalRepository.findOne(usuarioId));
    }

    @PostMapping("/{pedidoId}/protocolo/{pedidoProtocoloId}/status/{pedidoProtocoloStatusId}/averbacao")
    public void averbarCertidao(@PathVariable Long pedidoId, @PathVariable Long pedidoProtocoloId, @PathVariable Long pedidoProtocoloStatusId,  @RequestAttribute("usuarioId") Long usuarioId) throws Exception {
        pedidoService.criarProtocoloAverbacao(pedidoId, pedidoProtocoloId, pedidoProtocoloStatusId);
    }

    @PostMapping("/{pedidoId}/protocolo/{pedidoProtocoloId}/status/")
    public void informarDadosStatusPeidodProtocolo(@PathVariable Long pedidoId, @PathVariable Long pedidoProtocoloId,
                                                   @RequestParam(value = "arquivo", required = false) MultipartFile file,
                                                   @RequestParam(value = "arquivoId", required = false) String arquivoId,
                                                   @RequestParam(value = "numero", required = false) Long numero,
                                                   @RequestParam(value = "id", required = false) Long pedidoProtocoloStatusId,
                                                   @RequestParam(value = "mensagem", required = false) String mensagem){
        PedidoProtocoloStatus novoStatus = new PedidoProtocoloStatus();
        novoStatus.setMensagem(mensagem);
        novoStatus.setId(pedidoProtocoloStatusId);
        novoStatus.setNumero(numero);
        if(!Strings.isNullOrEmpty(arquivoId)){
            String diretorio = arquivoService.recuperarDiretorioBase() + Diretorio.PEDIDO.getDiretorio() + File.separator + pedidoId + File.separator + pedidoProtocoloId + File.separator + pedidoProtocoloStatusId + File.separator;
            String nomeArquivo = arquivoId+".PDF";
            try {
                Files.createDirectories(new File(diretorio).toPath());
                FileOutputStream fos = new FileOutputStream(diretorio + File.separator + nomeArquivo);
                fos.write(assinaturaService.getArquivoFromArquivoId(arquivoId));
                fos.close();
            } catch (RuntimeException e) {
                throw new NotFoundException("Arquivo não encontrado: " + nomeArquivo);
            }catch (IOException e){
                throw new NotFoundException("Erro ao salvar o arquivo: " + nomeArquivo);
            }
            novoStatus.setArquivo(diretorio+File.separator+nomeArquivo);

        }else if(file != null) {
            String diretorio = arquivoService.recuperarDiretorioBase() + Diretorio.PEDIDO.getDiretorio() + File.separator + pedidoId + File.separator + pedidoProtocoloId + File.separator + pedidoProtocoloStatusId + File.separator;
            String nomeArquivo = file.getOriginalFilename();
            try {
                if(!nomeArquivo.endsWith(".pdf")){
                    nomeArquivo = "Certidao_Gerada.pdf";
                }
                Files.createDirectories(new File(diretorio).toPath());
                FileOutputStream fos = new FileOutputStream(diretorio + File.separator + nomeArquivo);
                fos.write(file.getBytes());
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            novoStatus.setArquivo(diretorio+File.separator+nomeArquivo);
        }
        this.pedidoService.informarDadosStatusPeidodProtocolo(pedidoId, pedidoProtocoloId, novoStatus);
    }

    @GetMapping("/{pedidoId}/modelocertidao/{cartorioParceiroId}")
    public String gerarModeloCertidaoParceiroId(@PathVariable Long pedidoId, @PathVariable Long cartorioParceiroId){
        return pedidoService.gerarModeloCertidaoParceiroId(pedidoId, cartorioParceiroId);
    }

    @GetMapping("/{pedidoId}/protocolo/{numeroProtocolo}/registro/download/")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long pedidoId, @PathVariable Long numeroProtocolo,  @RequestAttribute("usuarioId") Long usuarioId) throws IOException {
        Pedido pedido = pedidoService.findById(pedidoId, usuarioPortalRepository.findOne(usuarioId));
        if(pedido == null){
            throw new NotFoundException("Pedido não encontrado: " + pedidoId);
        }


        List<Registro> registros = registroService.findByNumeroRegistroAndEspecialidade(numeroProtocolo, pedido.getTipo());
        if(registros.size() <= 0){
            throw new NotFoundException("Registro não encontrado: " + pedidoId);
        }

        File file = arquivoService.recuperarDocumentoProtocoloRegistroFile(registros.get(0).getRegistro(),numeroProtocolo,pedido.getTipo(), TipoProtocolo.recuperaTipoPrenotacaoByEspecialidade(pedido.getTipo()));
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

    @GetMapping("/{pedidoId}/protocolo/{numeroProtocolo}/certidao/download/")
    public ResponseEntity<Resource> downloadFileCertidao(@PathVariable Long pedidoId, @PathVariable Long numeroProtocolo,  @RequestAttribute("usuarioId") Long usuarioId) throws IOException {
        Pedido pedido = pedidoService.findById(pedidoId, usuarioPortalRepository.findOne(usuarioId));
        if(pedido == null){
            throw new NotFoundException("Pedido não encontrado: " + pedidoId);
        }


        Protocolo protocoloCertidao = protocoloService.findByNumeroAndPedidoId(numeroProtocolo, pedidoId);
        if(protocoloCertidao == null){
            throw new NotFoundException("Protocolo não encontrado: " + pedidoId);
        }

        File file = arquivoService.recuperarDocumentoProtocoloFile(numeroProtocolo, TipoProtocolo.recuperaEspecialidade(protocoloCertidao.getTipo()), protocoloCertidao.getTipo(), Diretorio.ASSINADO);
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

    @GetMapping("/{pedidoId}/{protocoloId}/{statusId}/download/")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long pedidoId, @PathVariable Long protocoloId, @PathVariable Long statusId, @RequestAttribute("usuarioId") Long usuarioId) throws FileNotFoundException {
        Pedido pedido = pedidoService.findById(pedidoId, usuarioPortalRepository.findOne(usuarioId));
        File file = null;
        for(PedidoProtocolo pedidoProtocolo : pedido.getProtocolos()){
            if(pedidoProtocolo.getId().equals(protocoloId)){
                for(PedidoProtocoloStatus pedidoProtocoloStatus : pedidoProtocolo.getStatus()){
                    if(pedidoProtocoloStatus.getId().equals(statusId)){
                        file = new File(pedidoProtocoloStatus.getArquivo());
                    }
                }
            }
        }

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
