package br.com.exmart.rtdpjlite.service;

import br.com.exmart.rtdpjlite.BaseTest;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CalcularPrazoProtocoloTest extends BaseTest{

    @Autowired
    private CalcularPrazoProtocoloService calcularPrazoProtocolo;

    @Test
    public void A_when_find_return_null() {

        assertThat(true).isTrue();
    }
}
