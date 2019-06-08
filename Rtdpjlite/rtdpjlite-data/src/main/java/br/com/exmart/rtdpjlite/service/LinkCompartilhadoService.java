package br.com.exmart.rtdpjlite.service;

import br.com.exmart.rtdpjlite.annotations.NotFoundException;
import br.com.exmart.rtdpjlite.model.LinkCompartilhado;
import br.com.exmart.rtdpjlite.model.LinkCompartilhadoRegistro;
import br.com.exmart.rtdpjlite.model.Registro;
import br.com.exmart.rtdpjlite.model.Usuario;
import br.com.exmart.rtdpjlite.repository.LinkCompartilhadoRepository;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class LinkCompartilhadoService {
    @Autowired
    private LinkCompartilhadoRepository linkCompartilhadoRepository;
    @Autowired
    private ConfiguracaoService configuracaoService;

    public String gerarLink(List<Registro> registros, Usuario usuario) throws Exception {
        String urlPortal = configuracaoService.findConfiguracao().getUrlPortal();
        if(Strings.isNullOrEmpty(urlPortal)){
            throw new Exception("Favor configurar a URL do portal");
        }
        LinkCompartilhado novoLink = new LinkCompartilhado(UUID.randomUUID().toString(), usuario.getId());
        for(Registro registro : registros){
            novoLink.getRegistros().add(new LinkCompartilhadoRegistro(registro.getId(), registro.getNumeroRegistro(), registro.getRegistro(), registro.getEspecialidade(), registro.getDataRegistro()));
        }

        linkCompartilhadoRepository.save(novoLink);
        if(!urlPortal.endsWith("/")){
            urlPortal = urlPortal+"/";
        }
        urlPortal = urlPortal+"link/"+novoLink.getId();
        return urlPortal;
    }

    public LinkCompartilhado findById(String id) {
        LinkCompartilhado retorno = linkCompartilhadoRepository.findById(id);
        if(retorno == null){
            throw  new NotFoundException("Hash não encontrado: " + id);
        }
        return retorno;
    }
}

