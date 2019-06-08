package br.com.exmart.rtdpjlite.service.rest.indisponibilidade;

import br.com.exmart.rtdpjlite.BaseTest;
import br.com.exmart.rtdpjlite.service.IndisponibilidadeService;
import br.com.exmart.rtdpjlite.vo.indisponibilidade.IndisponibilidadeVO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.ConnectException;
import java.time.LocalDateTime;
import java.util.List;

public class IndisponibilidadeRestTest extends BaseTest {
    @Autowired
    private IndisponibilidadeRest indisponibilidadeRest;
    @Autowired
    private IndisponibilidadeService indisponibilidadeService;

//    @Test
    public void testarWs()  {
        List<IndisponibilidadeVO> retorno = null;
        try {
            LocalDateTime data = LocalDateTime.now().withYear(2017);
            retorno = indisponibilidadeRest.recuperarUltimosAdicionados(data);
            System.out.println(retorno.size());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    @Test
    public void testarService() throws Exception {
        LocalDateTime data = LocalDateTime.now().withYear(2017);
//        indisponibilidadeService.verificarIndisponibilidade();

    }
}
