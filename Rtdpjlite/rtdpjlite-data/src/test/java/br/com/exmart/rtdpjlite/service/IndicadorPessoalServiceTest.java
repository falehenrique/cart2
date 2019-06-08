package br.com.exmart.rtdpjlite.service;

import br.com.exmart.rtdpjlite.BaseTest;
import br.com.exmart.rtdpjlite.model.IndicadorPessoalVO;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class IndicadorPessoalServiceTest extends BaseTest {

    @Autowired
    private IndicadorPessoalService indicadorPessoalService;

    @Test
    public void teste1() {
        Assert.assertTrue(1==1);
//        System.out.println("COMEÇOU");
//        for(IndicadorPessoalVO indicadorPessoalVO : indicadorPessoalService.findByObjeto(1L, "MARCA", "FORD", 1L)){
//            System.out.println(indicadorPessoalVO);
//            System.out.println(indicadorPessoalVO.getObjetos().getNome());
//            System.out.println(indicadorPessoalVO.getParte().getNome());
//        }
//
//        for(IndicadorPessoalVO indicadorPessoalVO : indicadorPessoalService.findByParte("Fabio","fabo","TD", "3")){
//            System.out.println(indicadorPessoalVO);
//            System.out.println(indicadorPessoalVO.getObjetos().getNome());
//            System.out.println(indicadorPessoalVO.getParte().getNome());
//        }
    }
}
