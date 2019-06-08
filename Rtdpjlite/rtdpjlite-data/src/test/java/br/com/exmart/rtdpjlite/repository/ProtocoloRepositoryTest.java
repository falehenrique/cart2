package br.com.exmart.rtdpjlite.repository;

import br.com.exmart.rtdpjlite.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ProtocoloRepositoryTest extends BaseTest {
	@Autowired ProtocoloRepository protocoloRepository;
	@Autowired NaturezaRepository naturezaRepository;
	@Autowired StatusRepository statusRepository;
	@Autowired ParteRepository parteRepository;

	@Test
	public void whenCreateProtocolo_returnsProtocolo() {
		try {
//		Natureza natureza = naturezaRepository.findOne(1l);
//		getLogger().info("-----> " + natureza.toString());
//		Parte parte = new Parte();
//		parte.setNome("Nome do fulano");
//		parte.setDocumento("111.111.111-11");
//		parteRepository.save(parte);
//
//		Status status = statusRepository.findOne(1l);
//		Set<StatusProtocolo> prenotado = new HashSet<>();
//		prenotado.add(new StatusProtocolo(status, LocalDateTime.now()));
//		Integer vias = 1;
//		Money valor = Money.of(CurrencyUnit.of("BRL"),new BigDecimal(1600f));
//		Date data_doc = new Date();
//		Date data_prevista = new Date();
//		Date data_vencimento = new Date();
//		Date data_entrega = new Date();
//		String pastaPj = "0001";
//		String responsavel = "Nome Responsável";
//		String apresentante = "Nome Apresentante";
//		String telefone = "(11) 1234-5678";
//		String email ="email@dominio.com";
//		String observacoes = "Observações do protocolo";
//
//		Protocolo protocolo = new Protocolo();
//		protocolo.setNatureza(natureza);
//		protocolo.setNumero(1L);
//		protocolo.setTipo(TipoProtocolo.CERTIDAO_PJ);
//		Set<ParteProtocolo> partes = new HashSet<>();
//		partes.add(new ParteProtocolo(parte));
//		protocolo.setPartesProtocolo(partes);
//		protocolo.setStatusProtocolo(prenotado);
//		protocolo.setVias(vias);
//		protocolo.setValor(valor);
//		protocolo.setDataDoc(data_doc);
//		protocolo.setDataPrevista(data_prevista);
//		protocolo.setDataVencimento(data_vencimento);
//		protocolo.setDataEntrega(data_entrega);
//		protocolo.setPastaPJ(pastaPj);
//		protocolo.setResponsavel(responsavel);
//		protocolo.setApresentante(apresentante);
//		protocolo.setTelefone(telefone);
//		protocolo.setEmail(email);
//		protocolo.setObservacoes(observacoes);
//
//		//Lista de servicos do protocolo
//		List<ServicoProtocolo> servicos = new ArrayList<ServicoProtocolo>();
//		Servico servico1 = servicoRepository.findOne(1L);
//		Servico servico2 = servicoRepository.findOne(2L);
//
//
//		ServicoProtocolo servicosProtocolo1 = new ServicoProtocolo();
//		servicosProtocolo1.setServico(servico1);
//
//		ServicoProtocolo servicosProtocolo2 = new ServicoProtocolo();
//		servicosProtocolo2.setServico(servico2);
//
//		//protocolo.setServicosProtocolo(servicos);
//
//
//
//			Protocolo _protocolo = protocoloRepository.save(protocolo);
//			assertThat(_protocolo.getId()).isGreaterThan(0);
//			getLogger().info("-----> " + protocolo.toString());
		}catch (Exception e){
			e.printStackTrace();
		}

	}
}
