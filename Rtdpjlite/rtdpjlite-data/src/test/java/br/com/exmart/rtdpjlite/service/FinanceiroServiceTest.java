package br.com.exmart.rtdpjlite.service;

import br.com.exmart.rtdpjlite.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class FinanceiroServiceTest extends BaseTest {
    @Autowired
    private FinanceiroService financeiroService;

    @Test
    public void teste1(){

//        assertThat(financeiroService.listarServicos().size()).isGreaterThanOrEqualTo(1);
    }
    @Test
    public void teste2(){
//        assertThat(financeiroService.listarFormaCalculo().size()).isGreaterThanOrEqualTo(1);
    }

    @Test
    public void teste3(){
//        List<ServicoFinanceiro> servicos = financeiroService.listarServicos();
//        List<FormaCalculoFinanceiro> formaCalculo = financeiroService.listarFormaCalculo();
//
//        ServicoCalculado retorno = financeiroService.calcularServico(servicos.get(0).getIdServico(), BigDecimal.TEN, formaCalculo.get(0).getCdDivisor());
//        System.out.println(retorno);
//
//        List<ServicoCalculado> r = financeiroService.listarServicosProtocolo(3100L, "Títulos e Documentos e Pessoa Jurídica", false);
//        System.out.println(r);
    }
}
