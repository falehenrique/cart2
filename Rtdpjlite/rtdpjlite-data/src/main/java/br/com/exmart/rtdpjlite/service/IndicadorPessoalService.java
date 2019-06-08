package br.com.exmart.rtdpjlite.service;

import br.com.exmart.rtdpjlite.model.IndicadorPessoal;
import br.com.exmart.rtdpjlite.model.IndicadorPessoalVO;
import br.com.exmart.rtdpjlite.repository.IndicadorPessoalRepository;
import br.com.exmart.rtdpjlite.repository.IndicadorPessoalVORepository;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IndicadorPessoalService {

    @Autowired
    private IndicadorPessoalVORepository indicadorPessoalVORepository;
    @Autowired
    private IndicadorPessoalRepository indicadorPessoalRepository;


    public List<IndicadorPessoalVO> findByObjeto(Long id, String nome, String valor, Long idCliente){
        return indicadorPessoalVORepository.findByObjeto(id, nome.toUpperCase(), valor.toUpperCase(), idCliente == null ? 0 : idCliente);
    }
    public List<IndicadorPessoalVO> findByParte(String nome, String documento, String especialidade){
        documento = limparPontuacaoDocumento(documento);
        return indicadorPessoalVORepository.findByParte(nome, documento, especialidade);

    }

    public List<IndicadorPessoalVO> findByParte(String nome, String documento, String especialidade, Long idCliente){
        documento = limparPontuacaoDocumento(documento);
        if(idCliente == null){
            return indicadorPessoalVORepository.findByParte(nome, documento, especialidade);
        }else {
            return indicadorPessoalVORepository.findByParte(nome, documento, especialidade, idCliente);
        }
    }

    public List<IndicadorPessoalVO> findByRegistro(Long idRegistro, String especialidade, Long idCliente){
        if(idCliente == null){
            return indicadorPessoalVORepository.findByRegistro(idRegistro, especialidade);
        }
        return indicadorPessoalVORepository.findByRegistro(idRegistro, especialidade, idCliente);
    }

    public List<IndicadorPessoalVO> findByParteFonetico(String nome, String documento, String especialidade, Long idCliente, String complementoFonetica) {
        documento = limparPontuacaoDocumento(documento);
        if(idCliente == null) {
            if(Strings.isNullOrEmpty(complementoFonetica)) {
                return indicadorPessoalVORepository.findByParteFonetico(nome, documento, especialidade);
            }else{
                return indicadorPessoalVORepository.findByParteFonetico(nome, documento, especialidade, complementoFonetica);
            }
        }else {
            if(Strings.isNullOrEmpty(complementoFonetica)) {
                return indicadorPessoalVORepository.findByParteFonetico(nome, documento, especialidade, idCliente);
            }else{
                return indicadorPessoalVORepository.findByParteFonetico(nome, documento, especialidade, idCliente, complementoFonetica);
            }
        }
    }
    public List<IndicadorPessoalVO> findByParteFonetico(String nome, String documento, String especialidade, String complementoFonetica) {
        documento = limparPontuacaoDocumento(documento);
        if(Strings.isNullOrEmpty(complementoFonetica)) {
            return indicadorPessoalVORepository.findByParteFonetico(nome, documento, especialidade);
        }else{
            return indicadorPessoalVORepository.findByParteFonetico(nome, documento, especialidade, complementoFonetica);
        }
    }

    private String limparPontuacaoDocumento(String documento) {
        documento = documento.replace(".","").replace("-","").replace("/","");
        return documento;
    }

    public List<IndicadorPessoal> findByDocumento(String documento) {
        return indicadorPessoalRepository.findByCpfCnpjOrderByIdDesc(documento);
    }
}
