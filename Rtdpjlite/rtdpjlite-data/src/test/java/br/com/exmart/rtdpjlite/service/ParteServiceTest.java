package br.com.exmart.rtdpjlite.service;

import br.com.exmart.rtdpjlite.BaseTest;
import br.com.exmart.rtdpjlite.model.Parte;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ParteServiceTest extends BaseTest {
    @Autowired private ParteService parteService;
    @Autowired private TipoDocumentoService tipoDocumentoService;

    @Test
    public void A_when_find_return_null() {
        Parte retorno =parteService.findFirstByCpfCnpjOrderByIdDesc("1231231233");
        assertThat(retorno).isNull();
    }

    @Test
    public void B_when_find_return_not_null() throws IOException {
        Parte parte = new Parte();
        parte.setEstadoCivil(null);
        parte.setEmail("email@email.com");
        parte.setNome("Fulano da Silva");
        parte.setTipoDocumento(tipoDocumentoService.findAll().get(0));
        parte.setNmOrgaoExpedidorDocumento("SSP/SP");
        parte.setDtEmissaoDocumento(LocalDate.now());

        parte.setNrDocumento("465.262.577-47");
        parte.setCpfCnpj("465.262.577-47");
        parteService.save(parte);
        Parte retorno =parteService.findFirstByCpfCnpjOrderByIdDesc("465.262.577-47");
        assertThat(retorno).isNotNull();
    }

    @Test
    public void B_must_return_last() throws IOException {
        Parte parte = new Parte();
        parte.setEmail("email@email.com");
        parte.setNome("Fulano da Silva");
        parte.setTipoDocumento(tipoDocumentoService.findAll().get(0));
        parte.setNmOrgaoExpedidorDocumento("SSP/SP");
        parte.setDtEmissaoDocumento(LocalDate.now());
        parte.setNrDocumento("380.323.689-49");
        parte.setCpfCnpj("380.323.689-49");
        parteService.save(parte);

        Parte parte2 = new Parte();
        parte2.setEmail("email@email.com");
        parte2.setNome("Fulano da Silva 2");
        parte.setTipoDocumento(tipoDocumentoService.findAll().get(0));
        parte.setNmOrgaoExpedidorDocumento("SSP/SP");
        parte.setDtEmissaoDocumento(LocalDate.now());
        parte2.setNrDocumento("380.323.689-49");
        parte2.setCpfCnpj("380.323.689-49");
        parteService.save(parte2);
        Parte retorno =parteService.findFirstByCpfCnpjOrderByIdDesc("380.323.689-49");
        System.out.println(retorno.getNome());
        System.out.println(retorno.getId());
        assertThat(retorno.getNome().equalsIgnoreCase(parte2.getNome())).isTrue();
    }
}
