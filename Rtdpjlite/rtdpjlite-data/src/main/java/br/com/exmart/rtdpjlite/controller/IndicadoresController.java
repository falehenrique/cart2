package br.com.exmart.rtdpjlite.controller;

import br.com.exmart.rtdpjlite.model.Cliente;
import br.com.exmart.rtdpjlite.model.IndicadorPessoalVO;
import br.com.exmart.rtdpjlite.model.Objeto;
import br.com.exmart.rtdpjlite.service.ClienteService;
import br.com.exmart.rtdpjlite.service.IndicadorPessoalService;
import br.com.exmart.rtdpjlite.service.ObjetoService;
import br.com.exmart.rtdpjlite.service.ProtocoloService;
import br.com.exmart.rtdpjlite.service.rest.fonetica.FoneticaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/indicador")
public class IndicadoresController {

    protected static Logger logger = LoggerFactory.getLogger(IndicadoresController.class);
    @Autowired
    private IndicadorPessoalService indicadorPessoalService;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private ObjetoService objetoService;
    @Autowired
    FoneticaService foneticaService;

    @GetMapping(value = "/real/objeto")
    public List<Objeto> findAll(){
        return objetoService.findAll();
    }

    @PostMapping(value = "/real")
    public List<IndicadorPessoalVO> findByObjeto(@RequestBody Map<String, Object> parametros){
        Long idCliente= null;
        if(parametros.get("idCliente") != null) {
            idCliente = retornaCliente(parametros.get("idCliente")).getId();
        }
        try {
            Long idObjeto = Long.parseLong(parametros.get("idObjeto").toString());
            String nomeAtributo = parametros.get("nomeAtributo").toString();
            String valorAtributo = parametros.get("valorAtributo").toString();
            logger.info(idObjeto.toString());
            logger.info(nomeAtributo);
            logger.info(valorAtributo);
            return indicadorPessoalService.findByObjeto(idObjeto, nomeAtributo.toUpperCase(), valorAtributo.toUpperCase(), idCliente);
        }catch (NullPointerException e){
            throw new RuntimeException("Informar os campos corretamente");
        }
    }

    @PostMapping(value = "/pessoal")
    public List<IndicadorPessoalVO> findByNomeAndDocumento(@RequestBody Map<String, Object> parametros){
        Long idCliente= null;
        if(parametros.get("idCliente") != null) {
            idCliente = retornaCliente(parametros.get("idCliente")).getId();
        }
        String nomeFonetico = null;
        try {
            String nomeDocumento = parametros.get("nomeDocumento").toString();
            String especialidade = parametros.get("especialidade").toString();
            try {

                Pattern p = Pattern.compile("-?\\d+");
                Matcher m = p.matcher(nomeDocumento);
                String numeros = "%";
                boolean entrou = false;
                while (m.find()) {
                    entrou = true;
                    numeros = numeros + m.group()+"%";
                }
                if(!entrou)
                    numeros = null;

                nomeFonetico = "%"+foneticaService.foneticar(nomeDocumento)+"%" ;
                logger.info("Busca por nome fonetico");
                return indicadorPessoalService.findByParteFonetico(nomeFonetico, nomeDocumento, especialidade, idCliente, numeros);
            } catch (Exception e) {
                logger.info("Busca por nome nao fonetico");
                return indicadorPessoalService.findByParte("%"+nomeDocumento+"%", nomeDocumento, especialidade, idCliente);
            }
        }catch (NullPointerException e){
            throw new RuntimeException("Informar os campos corretamente");
        }

    }

    @PostMapping(value = "/registro")
    public List<IndicadorPessoalVO> findByRegistro(@RequestBody Map<String, Object> parametros){
        Long idCliente= null;
        if(parametros.get("idCliente") != null) {
            idCliente = retornaCliente(parametros.get("idCliente")).getId();
        }
        return indicadorPessoalService.findByRegistro(Long.parseLong(parametros.get("registro").toString()),parametros.get("especialidade").toString(), idCliente);
    }

    private Cliente retornaCliente(Object id){
        try {
            Long idCliente = Long.parseLong(id.toString());
            Cliente cliente = clienteService.findOne(idCliente);

            if (cliente == null) {
                throw new RuntimeException("Cliente não encontrado: " + id);
            }
            return cliente;
        }catch (Exception e){
            throw new RuntimeException("Cliente não encontrado: " + id);
        }
    }

}
