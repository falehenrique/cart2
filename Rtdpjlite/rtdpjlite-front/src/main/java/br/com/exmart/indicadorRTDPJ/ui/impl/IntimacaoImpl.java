package br.com.exmart.indicadorRTDPJ.ui.impl;

import br.com.exmart.indicadorRTDPJ.MyUI;
import br.com.exmart.indicadorRTDPJ.ui.Intimacao;
import br.com.exmart.indicadorRTDPJ.ui.util.GerenciarJanela;
import br.com.exmart.indicadorRTDPJ.ui.util.Utils;
import br.com.exmart.rtdpjlite.model.*;
import br.com.exmart.rtdpjlite.service.FinanceiroService;
import br.com.exmart.rtdpjlite.service.ModeloService;
import br.com.exmart.rtdpjlite.service.ProtocoloService;
import br.com.exmart.rtdpjlite.vo.financeiro.CustasCartorio;
import br.com.exmart.util.BeanLocator;
import com.google.common.base.Strings;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.renderers.HtmlRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

public class IntimacaoImpl extends Intimacao implements PassoProtocolo{
	protected static Logger logger= LoggerFactory.getLogger(IntimacaoImpl.class);
	private ProtocoloService protocoloService;
	private FinanceiroService financeiroService;
	private ModeloService modeloService;
	Protocolo protocolo = null;
	public IntimacaoImpl(Protocolo protocolo) {
		logger.debug("iniciou IntimacaoImpl");
		this.protocoloService = BeanLocator.find(ProtocoloService.class);
		this.financeiroService = BeanLocator.find(FinanceiroService.class);
		this.modeloService = BeanLocator.find(ModeloService.class);

		this.protocolo = protocolo;
		btnAddResultado.setEnabled(false);
		btnGerarCertidao.setEnabled(false);
		dataHoraIntimacao.setEnabled(false);
		statusIntimacao.setEnabled(false);
		resultadoIntimacao.setEnabled(false);

		gridIntimacao.setItems(protocolo.getIntimacaosProtocolo());
		gridIntimacao.removeAllColumns();
		gridIntimacao.addColumn(item -> {
			return Utils.formatarDataComHora(item.getDia());
		}, new HtmlRenderer()).setCaption("Dt. Intimação").setId("dia");

		gridIntimacao.addColumn(IntimacaoProtocolo::getNome).setCaption("Status").setId("nome");
		gridIntimacao.addColumn(IntimacaoProtocolo::getResultado).setCaption("Resultado");
		gridIntimacao.addComponentColumn(item -> {
			Button button = new Button(VaadinIcons.TRASH);
			button.addStyleName("borderless");
			button.addClickListener(click -> {
				this.protocolo.getIntimacaosProtocolo().remove(item);
				gridIntimacao.getDataProvider().refreshAll();
			});
			return button;
		}).setWidth(100);

		gridIntimacao.setColumnOrder("dia","nome");
		statusIntimacao.setEmptySelectionAllowed(false);
		dataHoraIntimacao.setValue(LocalDateTime.now());
		dataHoraIntimacao.setRangeStart(protocolo.getDtRegistro());

		btnAddResultado.addClickListener(evt->adicionarResultado(evt));

		LocalDateTime primeiraIntimacao = LocalDateTime.now();
		if(this.protocolo.getIntimacaosProtocolo().size() >0) {
			primeiraIntimacao = this.protocolo.getIntimacaosProtocolo().iterator().next().getDia();
		}else {
			for (StatusProtocolo status : this.protocolo.getStatusProtocolo()) {
				if (status.getStatus().getNome().equalsIgnoreCase("REGISTRADO")) {
					primeiraIntimacao = status.getData().plusDays(15);
				}
			}
		}


		labelPrimeiraIntimacao.setValue(Utils.formatarDataComHora(primeiraIntimacao));
		btnAbrirCertidao.addClickListener(evt-> gerarCertidaoListener(evt));
		btnGerarCertidao.addClickListener(vt -> gerarCertidaoListener(vt));
		logger.debug("acabou IntimacaoImpl");
		labelValidadePrenotacao.setValue(Utils.formatarData(protocolo.getData_vencimento()));
	}

	private void gerarCertidaoListener(Button.ClickEvent evt) {
		List<CustasCartorio> custas = protocoloService.recuperarCustasServico( financeiroService.listarServicosProtocolo(protocolo.getNumero(), protocolo.getTipo()), ((MyUI) UI.getCurrent()).getCustasCartorio());
		List<Modelo> modelosTexto = modeloService.findModeloByTipo(this.protocolo.getNatureza().getId(), this.protocolo.getSubNatureza() != null ? this.protocolo.getSubNatureza().getId() : null, TipoModelo.TEXTO_CERTIDAO_NOTIFICACAO);
		List<Modelo> modelosCarimbo = modeloService.findModeloByTipo(this.protocolo.getNatureza().getId(), this.protocolo.getSubNatureza() != null ? this.protocolo.getSubNatureza().getId() : null, TipoModelo.CARIMBO);
        IntimacaoProtocolo ultimoStatus = recuperarUtlimoStatus();
        boolean positivo = ultimoStatus.getNome().startsWith("INTIMADO");

//		File arquivo = arquivoService.recuperarDocumentoProtocoloFile(this.protocolo.getNumero(), ((MyUI)UI.getCurrent()).getEspecialidadeAtual().getEspecialidade(), protocolo.getTipo());
		ModalRegistroImpl modalRegistro = new ModalRegistroImpl(protocolo, modelosCarimbo, custas, ModalRegistroImpl.TipoDocumento.CERTIDAO_NOTIFICACAO, null, modelosTexto, ultimoStatus.getResultado(), positivo);
		GerenciarJanela.abrirJanela("", 80, 90,modalRegistro, null, true, true, true, true);
	}

    private IntimacaoProtocolo recuperarUtlimoStatus() {
        IntimacaoProtocolo ultima = null;
	    try {
	        ultima = protocolo.getIntimacaosProtocolo().iterator().next();
            for (IntimacaoProtocolo intimacaoProtocolo : protocolo.getIntimacaosProtocolo()) {
                if (intimacaoProtocolo.getDia().isAfter(ultima.getDia())){
                    ultima = intimacaoProtocolo;
                }
            }
        }catch (NoSuchElementException w){

        }
        return ultima;
    }

    private void adicionarResultado(Button.ClickEvent evt) {


		if(Strings.isNullOrEmpty(statusIntimacao.getValue()) || Strings.isNullOrEmpty(resultadoIntimacao.getValue()) || dataHoraIntimacao.getValue() == null){
			Notification.show("Atenção", "Ops ;) Acho que faltou preencher algum campo", Notification.Type.ERROR_MESSAGE);
		}else {
			IntimacaoProtocolo novaIntimacao = new IntimacaoProtocolo(statusIntimacao.getValue(), resultadoIntimacao.getValue(), dataHoraIntimacao.getValue());
			statusIntimacao.clear();
			resultadoIntimacao.clear();
			dataHoraIntimacao.clear();
			this.protocolo.getIntimacaosProtocolo().add(novaIntimacao);
			gridIntimacao.getDataProvider().refreshAll();
		}
	}


	@Override
	public void habilitarForm(boolean habilitar, boolean icProtocoloEditavel) {
		btnAddResultado.setEnabled(habilitar);
		btnGerarCertidao.setEnabled(habilitar);
		dataHoraIntimacao.setEnabled(habilitar);
		statusIntimacao.setEnabled(habilitar);
		resultadoIntimacao.setEnabled(habilitar);
		gridIntimacao.getColumns().get(gridIntimacao.getColumns().size()-1).setHidden(!habilitar);

	}

	@Override
	public void cancelar() {

	}

	@Override
	public void focus() {

	}

	@Override
	public void validate() throws Exception {

	}

	@Override
	public void removerListener() {

	}

	@Override
	public void addListener() {

	}


}
