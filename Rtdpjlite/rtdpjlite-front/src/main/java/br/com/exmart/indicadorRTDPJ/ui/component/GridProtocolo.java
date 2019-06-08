package br.com.exmart.indicadorRTDPJ.ui.component;

import br.com.exmart.indicadorRTDPJ.MyUI;
import br.com.exmart.indicadorRTDPJ.ui.custom.event.EspecialidadeEvent;
import br.com.exmart.indicadorRTDPJ.ui.navigator.NavigationManager;
import br.com.exmart.indicadorRTDPJ.ui.util.Especialidade;
import br.com.exmart.indicadorRTDPJ.ui.util.Utils;
import br.com.exmart.rtdpjlite.model.Protocolo;
import br.com.exmart.rtdpjlite.model.Protocolo;
import br.com.exmart.rtdpjlite.model.TipoProtocolo;
import br.com.exmart.util.BeanLocator;
import com.vaadin.ui.Grid;
import com.vaadin.ui.UI;
import com.vaadin.ui.renderers.HtmlRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.spring.events.EventBus;

public class GridProtocolo extends Grid<Protocolo>{

	protected static Logger logger= LoggerFactory.getLogger(GridProtocolo.class);
	private NavigationManager navigationManager;
	private EventBus.SessionEventBus eventBus;


	public GridProtocolo() {
		this.eventBus = BeanLocator.find(EventBus.SessionEventBus.class);
		this.navigationManager = BeanLocator.find(NavigationManager.class);
		addItemClickListener(evt -> {
			Protocolo protocolo = evt.getItem();
			TipoProtocolo tipo = protocolo.getTipo();
			if(tipo.equals(TipoProtocolo.PRENOTACAO_PJ)) {
				((MyUI) UI.getCurrent()).setEspecialidadeAtual(Especialidade.PJ);
				this.eventBus.publish(this, new EspecialidadeEvent(Especialidade.PJ));
				navigationManager.navegarPara(tipo, evt.getItem().getNumero());
			}else if(tipo.equals(TipoProtocolo.PRENOTACAO_TD)) {
				((MyUI) UI.getCurrent()).setEspecialidadeAtual(Especialidade.TD);
				this.eventBus.publish(this, new EspecialidadeEvent(Especialidade.TD));
				navigationManager.navegarPara(tipo, evt.getItem().getNumero());
			}else if(tipo.equals(TipoProtocolo.EXAMECALCULO_PJ)) {
				((MyUI) UI.getCurrent()).setEspecialidadeAtual(Especialidade.PJ);
				this.eventBus.publish(this, new EspecialidadeEvent(Especialidade.PJ));
				navigationManager.navegarPara(tipo, evt.getItem().getNumero());
			}else if(tipo.equals(TipoProtocolo.EXAMECALCULO_TD)) {
				((MyUI) UI.getCurrent()).setEspecialidadeAtual(Especialidade.TD);
				this.eventBus.publish(this, new EspecialidadeEvent(Especialidade.TD));
				navigationManager.navegarPara(tipo, evt.getItem().getNumero());
			}else if(tipo.equals(TipoProtocolo.CERTIDAO_TD)) {
				((MyUI) UI.getCurrent()).setEspecialidadeAtual(Especialidade.TD);//TODO VALIDAR SE EXISTE AS 2 SITUACOES PARA CERTIDAO
				this.eventBus.publish(this, new EspecialidadeEvent(Especialidade.TD));
				navigationManager.navegarPara(tipo, evt.getItem().getNumero());
			}else if(tipo.equals(TipoProtocolo.CERTIDAO_PJ)) {
				((MyUI) UI.getCurrent()).setEspecialidadeAtual(Especialidade.PJ);//TODO VALIDAR SE EXISTE AS 2 SITUACOES PARA CERTIDAO
				this.eventBus.publish(this, new EspecialidadeEvent(Especialidade.PJ));
				navigationManager.navegarPara(tipo, evt.getItem().getNumero());
			}

		});

		addStyleName("gridProtocolo");
		setSizeFull();
//		removeHeaderRow(0);

		// Add stylenames to rows
		setStyleGenerator(GridProtocolo::getRowStyle);



		// Icon column
		Column<Protocolo, String> iconColumn = addColumn(
				order -> twoRowCellIcon(Utils.getIconCorretoFromTipoProtocolo(order.getTipo()).getHtml(), "  3!","Protocolo nº:",order.getNumero(), order.getTipo()),
				new HtmlRenderer()).setCaption("Nº PROT.").setWidth(135);


		// Natureza column
		Column<Protocolo, String> naturezaColumn = addColumn(
				order -> twoRowCellNatureza("Natureza:",order.getNatureza().getNome(),"Apresentante:",order.getApresentante()),
				new HtmlRenderer()).setCaption("NATUREZA");

		// Vencimento column
		Column<Protocolo, String> vencimentoColumn = addColumn(
						order -> twoRowCellNatureza("Data Venc.:", Utils.formatarData(order.getDataVencimento()),"Parte:",order.getParte()),
						new HtmlRenderer()).setCaption("DATA VENC.");

		// Status column
		//TOOD deve arrumar o Saldo
		Column<Protocolo, String> statusColumn = addColumn(
					order -> twoRowCellStatus("Status:",order.getStatus().getStatus().getNome(),"",""),
					new HtmlRenderer()).setCaption("STATUS");

	}

	private void abrirProtocolo() {

	}


	private static String getRowStyle(Protocolo order) {
		return "null";
	}


	// Icon column
	private static String twoRowCellIcon(String icon, String alertas, String captionProtocolo, Long protocolo, TipoProtocolo tipo) {
		return "<div class=\"icon\">" + icon +
				"<div class=\"caption\">" + captionProtocolo +
				"</div><div class='contentProtocolo "+TipoProtocolo.recuperaEspecialidade(tipo).toLowerCase()+"'>" +TipoProtocolo.recuperaEspecialidade(tipo) + " - "+ protocolo +
				"</div>";
	}
	// Natureza/Vencimento column
	private static String twoRowCellNatureza(String captionNatureza, String natureza, String captionParte, String parte) {
		return  "<div class=\"caption\">" + captionNatureza +
				"</div><div class=\"content\">" + natureza +
				"</div>" + "<div class=\"caption\">" + captionParte +
				"</div><div class=\"content2\">" + parte + 
				"</div>";
	}
	// Status column
	private static String twoRowCellStatus(String captionStatus, String status, String captionSaldo, String saldo) {
		StringBuffer retorno = new StringBuffer();
		retorno.append("<div class=\"caption\">" + captionStatus);
		if(status.equalsIgnoreCase("REGISTRADO")){
			retorno.append("</div><div class=\"content green\">" + status);
		}else if(status.equalsIgnoreCase("DEVOLVIDO")){
			retorno.append("</div><div class=\"content red\">" + status);
		}else{
			retorno.append("</div><div class=\"content\">" + status);
		}

		retorno.append("</div>" + "<div class=\"caption\">" + captionSaldo);
		retorno.append("</div><div class=\"content2\">" + saldo);
		retorno.append("</div>");
		return retorno.toString();

	}

}
