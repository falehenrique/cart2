package br.com.exmart.indicadorRTDPJ.ui.impl;

import br.com.exmart.indicadorRTDPJ.MyUI;
import br.com.exmart.indicadorRTDPJ.ui.Partes;
import br.com.exmart.indicadorRTDPJ.ui.custom.MyStringToDateConverter;
import br.com.exmart.indicadorRTDPJ.ui.custom.event.ProtocoloEvent;
import br.com.exmart.indicadorRTDPJ.ui.util.FormatarDocumento;
import br.com.exmart.indicadorRTDPJ.ui.util.Utils;
import br.com.exmart.rtdpjlite.model.*;
import br.com.exmart.rtdpjlite.service.*;
import br.com.exmart.util.BeanLocator;
import com.google.common.base.Strings;
import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.data.HasValue;
import com.vaadin.data.ValidationException;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.event.FieldEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ErrorMessage;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.spring.events.EventBus;
import org.vaadin.textfieldformatter.CustomStringBlockFormatter;
import org.vaadin.textfieldformatter.PhoneFieldFormatter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class PartesImpl extends Partes implements PassoProtocolo{

	protected static Logger logger= LoggerFactory.getLogger(PartesImpl.class);
	private BeanValidationBinder<ParteProtocolo> binderParteProtocolo = new BeanValidationBinder<>(ParteProtocolo.class);
	private BeanValidationBinder<ParteProtocolo> binderConjugeProtocolo = new BeanValidationBinder<>(ParteProtocolo.class);
	private ProtocoloService protocoloService;
	private Protocolo protocolo;
	private QualidadeService qualidadeService;
	private NacionalidadeService nacionalidadeService;
	private EstadoCivilService estadoCivilService;
	private EstadoService estadoService;
	private ProfissaoService profissaoService;
	private ParteProtocolo parteSelecionado = new ParteProtocolo();
	private ParteProtocolo conjugeSelecionado = new ParteProtocolo();
	private RegimeBensService regimeBensService;
	private ParteService parteService;
	private TipoLogradouroService tipoLogradouroService;
	private TipoDocumentoService tipoDocumentoService;
	private TipoLogradouro tipoRua = null;
	private IndisponibilidadeService indisponibilidadeService;
	private EventBus.ViewEventBus eventBus;


	
	public PartesImpl(Protocolo protocolo, EventBus.ViewEventBus eventBus) {
		this.eventBus = eventBus;
		logger.debug("iniciou PartesImpl");
		this.protocolo = protocolo;
		this.protocoloService =  BeanLocator.find(ProtocoloService.class);
		this.indisponibilidadeService = BeanLocator.find(IndisponibilidadeService.class);

		this.parteService = BeanLocator.find(ParteService.class);
		this.qualidadeService =  BeanLocator.find(QualidadeService.class);
		this.nacionalidadeService =  BeanLocator.find(NacionalidadeService.class);
		this.estadoCivilService =  BeanLocator.find(EstadoCivilService.class);
		this.estadoService =  BeanLocator.find(EstadoService.class);
		this.profissaoService =  BeanLocator.find(ProfissaoService.class);
		this.regimeBensService = BeanLocator.find(RegimeBensService.class);
		this.tipoLogradouroService = BeanLocator.find(TipoLogradouroService.class);
		this.tipoDocumentoService = BeanLocator.find(TipoDocumentoService.class);
		List<Qualidade> qualidadeList = ((MyUI) UI.getCurrent()).getQualidades();
		fieldQualidadeParte.setItems((s, s1) -> {return s.toUpperCase().startsWith(s1.toUpperCase());},qualidadeList);
		fieldNacionalidade.setItems((s, s1) -> {return s.toUpperCase().startsWith(s1.toUpperCase());},((MyUI) UI.getCurrent()).getNacionalidades());
		fieldEstadoCivilParte.setItems((s, s1) -> {return s.toUpperCase().startsWith(s1.toUpperCase());},((MyUI) UI.getCurrent()).getEstadoCivils());
		fieldProfissao.setItems((s, s1) -> {return s.toUpperCase().startsWith(s1.toUpperCase());},((MyUI) UI.getCurrent()).getProfissaos());
		fieldRegimeBens.setItems((s, s1) -> {return s.toUpperCase().startsWith(s1.toUpperCase());},((MyUI) UI.getCurrent()).getRegimeBens());

		fieldQualidadeParte.setPageLength(1000);
		fieldNacionalidade.setPageLength(1000);
		fieldEstadoCivilParte.setPageLength(1000);
		fieldProfissao.setPageLength(1000);
		fieldRegimeBens.setPageLength(1000);
		fieldUfParte.setPageLength(1000);
		fieldCidadeParte.setPageLength(1000);
		fieldConjugeCidade.setPageLength(1000);
		fieldConjugeNacionalidade.setPageLength(1000);
		fieldConjugeProfissao.setPageLength(1000);
		fieldConjugeUf.setPageLength(1000);
		fieldConjugeTipoDoc.setPageLength(1000);
		fieldConjugeTipoLogradouro.setPageLength(1000);


		new PhoneFieldFormatter(fieldConjugeTelefone, "BR");
		new PhoneFieldFormatter(fieldTelefoneParte, "BR");

		new CustomStringBlockFormatter(fieldCepParte,new int[]{5,3},new String[]{"-"}, CustomStringBlockFormatter.ForceCase.UPPER);
		new CustomStringBlockFormatter(fieldConjugeCep,new int[]{5,3},new String[]{"-"}, CustomStringBlockFormatter.ForceCase.UPPER);

		fieldUfParte.setItems((s, s1) -> {return s.toUpperCase().startsWith(s1.toUpperCase());},((MyUI) UI.getCurrent()).getEstados());
		fieldUfParte.addValueChangeListener(evt-> listarCidades(evt));
		fieldTipoDoc.setItems((s, s1) -> {return s.toUpperCase().startsWith(s1.toUpperCase());},((MyUI) UI.getCurrent()).getTipoDocumentos());
		fieldTipoLogradouro.setItems((s, s1) -> {return s.toUpperCase().startsWith(s1.toUpperCase());},((MyUI) UI.getCurrent()).getTipoLogradouros());

		fieldConjugeUf.setItems((s, s1) -> {return s.toUpperCase().startsWith(s1.toUpperCase());},((MyUI) UI.getCurrent()).getEstados());
		fieldConjugeUf.addValueChangeListener(evt-> listarCidadesConjuge(evt));
		fieldConjugeTipoDoc.setItems((s, s1) -> {return s.toUpperCase().startsWith(s1.toUpperCase());},((MyUI) UI.getCurrent()).getTipoDocumentos());
		List<TipoLogradouro> tipoLogradouroList = ((MyUI) UI.getCurrent()).getTipoLogradouros();
		fieldConjugeTipoLogradouro.setItems((s, s1) -> {return s.toUpperCase().startsWith(s1.toUpperCase());},tipoLogradouroList);

		fieldConjugeQualidade.setItems((s, s1) -> {return s.toUpperCase().startsWith(s1.toUpperCase());},((MyUI) UI.getCurrent()).getQualidades());
		fieldConjugeNacionalidade.setItems((s, s1) -> {return s.toUpperCase().startsWith(s1.toUpperCase());},((MyUI) UI.getCurrent()).getNacionalidades());
		fieldConjugeProfissao.setItems((s, s1) -> {return s.toUpperCase().startsWith(s1.toUpperCase());},((MyUI) UI.getCurrent()).getProfissaos());

		new CustomStringBlockFormatter(fieldDtEmissaoDocumento, new int[]{2,2,4}, new String[]{"/","/"}, CustomStringBlockFormatter.ForceCase.NONE);
		new CustomStringBlockFormatter(fieldConjugeDataDoc, new int[]{2,2,4}, new String[]{"/","/"}, CustomStringBlockFormatter.ForceCase.NONE);

		conjugePanel.setVisible(false);
		this.protocolo.getPartesProtocolo();
		configurarGrid();
		bindCampos();
		btnAddParte.addClickListener(evt -> addParteClickListener(evt));
		btnCancelarParte.addClickListener(evt->cancelarParteClickListener(evt));
		fieldEstadoCivilParte.addValueChangeListener(evt->estadoCivilListener(evt));

		fieldDocumento.addBlurListener(evt-> documentoBlurListener(evt));
		fieldConjugeDocumento.addBlurListener(evt-> documentoConjugeBlurListener(evt));
		fieldRegimeBens.setEnabled(false);
		btnCadastrarConjuge.setEnabled(false);

		btnCopiarDados.addClickListener(evt->copiarDadosListener(evt));

		btnCadastrarConjuge.addClickListener(evt -> {
			String uuidConjuge = UUID.randomUUID().toString();
			this.parteSelecionado.getParte().setUuidConjuge(uuidConjuge);
			this.conjugeSelecionado.getParte().setUuidConjuge(uuidConjuge);
			conjugePanel.setVisible(!conjugePanel.isVisible());});
		verificarIndisponibilidade();
		logger.debug("acabou PartesImpl");
	}

	private void verificarIndisponibilidade(){
		if(TipoProtocolo.isPj(this.protocolo.getTipo())) {
			if (this.protocolo.getPartesProtocolo().size() > 0) {
				boolean possuiIndisponibilidade = false;
				for (ParteProtocolo parteProtocolo : this.protocolo.getPartesProtocolo()) {
					parteProtocolo.setIcIndisponivel(indisponibilidadeService.isIndisponivel(parteProtocolo.getParte().getCpfCnpj()));
					if (parteProtocolo.isIcIndisponivel()) {
						possuiIndisponibilidade = true;
					}
				}
				this.eventBus.publish(this, new ProtocoloEvent.ParteIndisponivel(possuiIndisponibilidade));
			}
		}
	}

	private void copiarDadosListener(Button.ClickEvent evt) {
		String uuidConjuge = UUID.randomUUID().toString();

		fieldConjugeCep.setValue(fieldCepParte.getValue());
		fieldConjugeEndereco.setValue(fieldEnderecoParte.getValue());
		fieldConjugeNumero.setValue(fieldNumeroParte.getValue());
		fieldConjugeComplemento.setValue(fieldComplementoParte.getValue());
		fieldConjugeBairro.setValue(fieldBairroParte.getValue());
		fieldConjugeQualidade.setValue(fieldQualidadeParte.getValue());
		fieldConjugeUf.setValue(fieldUfParte.getValue());
		fieldConjugeCidade.setValue(fieldCidadeParte.getValue());
		fieldConjugeTipoLogradouro.setValue(fieldTipoLogradouro.getValue());
		fieldConjugeTelefone.setValue(fieldTelefoneParte.getValue());


		this.conjugeSelecionado.getParte().setRegimeBens(this.parteSelecionado.getParte().getRegimeBens());
		this.conjugeSelecionado.getParte().setEstadoCivil(this.parteSelecionado.getParte().getEstadoCivil());
		this.parteSelecionado.getParte().setUuidConjuge(uuidConjuge);
		this.conjugeSelecionado.getParte().setUuidConjuge(uuidConjuge);

		fieldConjugeTelefone.focus();
	}

	private void documentoConjugeBlurListener(FieldEvents.BlurEvent evt) {
		TextField textField = (TextField) evt.getSource();
		ErrorMessage error = textField.getComponentError();
		if(error == null) {
			textField.setValue(FormatarDocumento.formataDocumento(textField.getValue()));
		}
	}

	private void listarCidadesConjuge(HasValue.ValueChangeEvent<Estado> evt) {
//		if(!panelAddParte.isVisible()){
//			return;
//		}
		if(evt.getValue() != null) {
			fieldConjugeCidade.clear();
			fieldConjugeCidade.setItems((s, s1) -> {return s.toUpperCase().startsWith(s1.toUpperCase());},evt.getValue().getCidades());
		}
	}

	private void listarCidades(HasValue.ValueChangeEvent<Estado> evt) {
//		if(!panelAddParte.isVisible()){
//			return;
//		}
		if(evt.getValue() != null) {
			fieldCidadeParte.clear();
			fieldCidadeParte.setItems((s, s1) -> {return s.toUpperCase().startsWith(s1.toUpperCase());},evt.getValue().getCidades());
		}
	}

	Parte parteEncontradaBanco = null;
	Parte conjugeEncontradaBanco = null;
	private void documentoBlurListener(FieldEvents.BlurEvent evt) {
		TextField textField = (TextField) evt.getSource();
		ErrorMessage error = textField.getComponentError();
		if(error == null) {
			if(textField.getValue().length() > 14){
				panelDocumento.setEnabled(false);
				panelAddInfo.setEnabled(false);
				fieldEstadoCivilParte.setEnabled(false);
				fieldRegimeBens.setEnabled(false);
				btnCadastrarConjuge.setEnabled(false);
			}else{
				panelDocumento.setEnabled(true);
				panelAddInfo.setEnabled(true);
				fieldEstadoCivilParte.setEnabled(true);
				fieldRegimeBens.setEnabled(true);
				btnCadastrarConjuge.setEnabled(true);
			}
			if(!Strings.isNullOrEmpty(textField.getValue())){
				Parte parte = parteService.findFirstByCpfCnpjOrderByIdDesc(FormatarDocumento.formataDocumento(textField.getValue()));
				if (parte != null) {
					parteEncontradaBanco = parte;
					fieldNomeParte.setValue(parte.getNome());
					fieldDocumento.setValue(parte.getCpfCnpj());
					fieldIdentidade.setValue(parte.getNrDocumento());
					fieldTipoDoc.setValue(parte.getTipoDocumento());
					fieldOrgaoExp.setValue(parte.getNmOrgaoExpedidorDocumento());
					fieldDtEmissaoDocumento.setValue(Utils.localDateToString(parte.getDtEmissaoDocumento()));
					fieldTelefoneParte.setValue(parte.getTelefone());
					fieldEmailParte.setValue(parte.getEmail());
					fieldCepParte.setValue(parte.getCep());
					fieldEnderecoParte.setValue(parte.getEndereco());
					fieldNumeroParte.setValue(parte.getNumero());
					fieldComplementoParte.setValue(parte.getComplemento());
					fieldBairroParte.setValue(parte.getBairro());
					fieldUfParte.setValue(parte.getEstado());
					fieldTipoLogradouro.setValue(parte.getTipoLogradouro());
					fieldCidadeParte.setValue(parte.getCidade());
					fieldNacionalidade.setValue(parte.getNacionalidade());
					fieldProfissao.setValue(parte.getProfissao());
					fieldEstadoCivilParte.setValue(parte.getEstadoCivil());

					fieldRegimeBens.setValue(parte.getRegimeBens());
					if(!Strings.isNullOrEmpty(parte.getUuidConjuge())){
						Parte conjuge= parteService.findConjuge(parte.getUuidConjuge(), parte.getId());
						conjugeEncontradaBanco = conjuge;
						fieldConjugeDocumento.setValue(conjuge.getCpfCnpj());
						fieldConjugeNome.setValue(conjuge.getNome());
						fieldConjugeIdentidade.setValue(conjuge.getNrDocumento());

						fieldConjugeTipoDoc.setValue(conjuge.getTipoDocumento());
						fieldConjugeOrgaoExp.setValue(conjuge.getNmOrgaoExpedidorDocumento());
						fieldConjugeDataDoc.setValue(Utils.localDateToString(conjuge.getDtEmissaoDocumento()));

						fieldConjugeTelefone.setValue(conjuge.getTelefone());
						fieldConjugeEmail.setValue(conjuge.getEmail());
						fieldConjugeNacionalidade.setValue(conjuge.getNacionalidade());
						fieldConjugeProfissao.setValue(conjuge.getProfissao());
						fieldConjugeCep.setValue(conjuge.getCep());
						fieldConjugeEndereco.setValue(conjuge.getEndereco());
						fieldConjugeTipoLogradouro.setValue(conjuge.getTipoLogradouro());
						fieldConjugeNumero.setValue(conjuge.getNumero());
						fieldConjugeComplemento.setValue(conjuge.getComplemento());
						fieldConjugeBairro.setValue(conjuge.getBairro());
						fieldConjugeUf.setValue(conjuge.getEstado());
						fieldConjugeCidade.setValue(conjuge.getCidade());
					}

					estadoCivilListener(new HasValue.ValueChangeEvent<EstadoCivil>(fieldEstadoCivilParte,parte.getEstadoCivil(), false));
				}else{
					textField.setValue(FormatarDocumento.formataDocumento(textField.getValue()));
				}
			}
			if(!Strings.isNullOrEmpty(textField.getValue())) {
				panelDocumento.setVisible(FormatarDocumento.isCpf(textField.getValue()));
				panelAddInfo.setVisible(FormatarDocumento.isCpf(textField.getValue()));
				panelAddInfo2.setVisible(FormatarDocumento.isCpf(textField.getValue()));
			}
		}


	}


	private void cancelarParteClickListener(Button.ClickEvent evt) {
		this.parteSelecionado = new ParteProtocolo();
		this.conjugeSelecionado = new ParteProtocolo();
		binderConjugeProtocolo.readBean(this.conjugeSelecionado);
		binderParteProtocolo.readBean(this.parteSelecionado);
		fieldTipoLogradouro.setValue(this.tipoRua);
	}

	private void estadoCivilListener(HasValue.ValueChangeEvent<EstadoCivil> evt) {
//TODO Deve remover pq agora tem botao para mostarr ou nao
		if(evt.getValue() != null && evt.getValue().getNome().toUpperCase().startsWith("CASAD")){
//			ParteProtocolo novaParte = new ParteProtocolo();
//			binderConjugeProtocolo.setBean(novaParte);
//			conjugePanel.setVisible(true);
//			copiarDadosListener(null);
			fieldRegimeBens.setEnabled(true);
			btnCadastrarConjuge.setEnabled(true);
//			this.fieldRegimeBens.focus();
		}else{
			fieldRegimeBens.clear();
			fieldRegimeBens.setEnabled(false);
			btnCadastrarConjuge.setEnabled(false);
//			conjugePanel.setVisible(false);
		}
	}

	private void addParteClickListener(Button.ClickEvent evt) {
		try {
			BinderValidationStatus<ParteProtocolo> binderParteProtocoloValidate = binderParteProtocolo.validate();
			if(!binderParteProtocoloValidate.hasErrors()){
				if(!Strings.isNullOrEmpty(this.parteSelecionado.getParte().getUuidConjuge())){
					BinderValidationStatus<ParteProtocolo> binderConjugeProtocoloValidate = binderConjugeProtocolo.validate();
					if(!binderConjugeProtocoloValidate.hasErrors()){
						binderParteProtocolo.writeBean(this.parteSelecionado);
						this.conjugeSelecionado.getParte().setEstadoCivil(this.parteSelecionado.getParte().getEstadoCivil());
						this.conjugeSelecionado.getParte().setRegimeBens(this.parteSelecionado.getParte().getRegimeBens());
						binderConjugeProtocolo.writeBean(this.conjugeSelecionado);

						this.protocolo.getPartesProtocolo().remove(this.parteSelecionado);
						if(parteSelecionado.getParte().isEqualsBanco(this.parteEncontradaBanco)){
							this.parteSelecionado.setParte(this.parteEncontradaBanco);
						}
						this.protocolo.getPartesProtocolo().add(this.parteSelecionado);

						this.protocolo.getPartesProtocolo().remove(this.conjugeSelecionado);
						if(conjugeSelecionado.getParte().isEqualsBanco(this.conjugeEncontradaBanco)){
							this.conjugeSelecionado.setParte(this.conjugeEncontradaBanco);
						}
						this.protocolo.getPartesProtocolo().add(this.conjugeSelecionado);

					}
				}else{
					binderParteProtocolo.writeBean(this.parteSelecionado);

					this.protocolo.getPartesProtocolo().remove(this.parteSelecionado);
					if(parteSelecionado.getParte().isEqualsBanco(this.parteEncontradaBanco)){
						this.parteSelecionado.setParte(this.parteEncontradaBanco);
					}
					this.protocolo.getPartesProtocolo().add(this.parteSelecionado);
				}

				cancelarParteClickListener(null);
				verificarIndisponibilidade();
			}else{
				Notification.show("Atenção", "Ops ;) Acho que faltou preencher algum campo corretamente", Notification.Type.ERROR_MESSAGE);
			}

			gridPartes.getDataProvider().refreshAll();
			fieldTipoLogradouro.setValue(this.tipoRua);
		} catch (ValidationException e) {
			e.printStackTrace();
		}

	}

	private void bindCampos() {
		binderParteProtocolo.bind(fieldNomeParte,"parte.nome");
//		binderParteProtocolo.bind(fieldDocumento,"parte.documento");
		binderParteProtocolo.forField(fieldDocumento).withValidator(valor -> FormatarDocumento.validarCpfCnpj(valor), "Documento Inválido").bind("parte.cpfCnpj");
		binderParteProtocolo.bind(fieldTelefoneParte,"parte.telefone");

		binderParteProtocolo.bind(fieldTipoDoc,"parte.tipoDocumento");
		binderParteProtocolo.bind(fieldIdentidade,"parte.nrDocumento");
		binderParteProtocolo.bind(fieldOrgaoExp,"parte.nmOrgaoExpedidorDocumento");
//		binderParteProtocolo.bind(fieldDtEmissaoDocumento,"parte.dtEmissaoDocumento");
		binderParteProtocolo.forField(fieldDtEmissaoDocumento).withConverter(new MyStringToDateConverter()).bind("parte.dtEmissaoDocumento");


		binderParteProtocolo.bind(fieldQualidadeParte,"qualidade");
		binderParteProtocolo.bind(fieldEmailParte,"parte.email");
		binderParteProtocolo.bind(fieldNacionalidade,"parte.nacionalidade");
		binderParteProtocolo.bind(fieldEstadoCivilParte,"parte.estadoCivil");
		binderParteProtocolo.bind(fieldProfissao,"parte.profissao");
//		binderParteProtocolo.forField(fieldValorParticipacaoParte).withConverter(new StringToDoubleConverter(Double.parseDouble("0"),"Não pode ser nulo")).bind("vlParticipacao");
		binderParteProtocolo.forField(fieldCotasParte).withConverter(new StringToIntegerConverter(0,"Não pode ser nulo")).bind("qtdCotas");
		binderParteProtocolo.bind(fieldCepParte,"parte.cep");
		binderParteProtocolo.bind(fieldTipoLogradouro,"parte.tipoLogradouro");
		binderParteProtocolo.bind(fieldEnderecoParte,"parte.endereco");
		binderParteProtocolo.bind(fieldNumeroParte,"parte.numero");
		binderParteProtocolo.bind(fieldComplementoParte,"parte.complemento");
		binderParteProtocolo.bind(fieldBairroParte,"parte.bairro");
		binderParteProtocolo.bind(fieldRegimeBens,"parte.regimeBens");
		binderParteProtocolo.bind(fieldCidadeParte,"parte.cidade");
		binderParteProtocolo.bind(fieldUfParte,"parte.estado");
		binderParteProtocolo.readBean(this.parteSelecionado);


		binderConjugeProtocolo.bind(fieldConjugeNome,"parte.nome");
//		binderConjugeProtocolo.bind(fieldConjugeDocumento,"parte.documento");

		binderConjugeProtocolo.forField(fieldConjugeDocumento).withValidator(valor -> FormatarDocumento.validarCpfCnpj(valor), "Documento Inválido").bind("parte.cpfCnpj");

		binderConjugeProtocolo.bind(fieldConjugeTipoDoc,"parte.tipoDocumento");
		binderConjugeProtocolo.bind(fieldConjugeIdentidade,"parte.nrDocumento");
		binderConjugeProtocolo.bind(fieldConjugeOrgaoExp,"parte.nmOrgaoExpedidorDocumento");

		binderConjugeProtocolo.forField(fieldConjugeDataDoc).withConverter(new MyStringToDateConverter()).bind("parte.dtEmissaoDocumento");

		binderConjugeProtocolo.bind(fieldConjugeTelefone,"parte.telefone");
		binderConjugeProtocolo.bind(fieldConjugeQualidade,"qualidade");
		binderConjugeProtocolo.bind(fieldConjugeEmail,"parte.email");
		binderConjugeProtocolo.bind(fieldConjugeNacionalidade,"parte.nacionalidade");
		binderConjugeProtocolo.bind(fieldConjugeProfissao,"parte.profissao");
		binderConjugeProtocolo.forField(fieldConjugeCota).withConverter(new StringToIntegerConverter(0,"Não pode ser nulo")).bind("qtdCotas");
		binderConjugeProtocolo.bind(fieldConjugeCep,"parte.cep");
		binderConjugeProtocolo.bind(fieldConjugeEndereco,"parte.endereco");
		binderConjugeProtocolo.bind(fieldConjugeNumero,"parte.numero");
		binderConjugeProtocolo.bind(fieldConjugeComplemento,"parte.complemento");
		binderConjugeProtocolo.bind(fieldConjugeBairro,"parte.bairro");
		binderConjugeProtocolo.readBean(this.conjugeSelecionado);
		binderConjugeProtocolo.bind(fieldConjugeCidade,"parte.cidade");
		binderConjugeProtocolo.bind(fieldConjugeTipoLogradouro,"parte.tipoLogradouro");
		binderConjugeProtocolo.bind(fieldConjugeUf,"parte.estado");


	}

	private void configurarGrid() {
		gridPartes.removeAllColumns();
		gridPartes.addColumn(item->{return item.getParte().getNome();}).setCaption("Nome");
		gridPartes.addColumn(item->{return item.getParte().getCpfCnpj();}).setCaption("Documento");
		gridPartes.addColumn(item->{return item.getQualidade();}).setCaption("Qualidade");

		gridPartes.setItems(this.protocolo.getPartesProtocolo());
		gridPartes.setSelectionMode(Grid.SelectionMode.SINGLE);
		gridPartes.addItemClickListener(evt -> gridParteSelectListener(evt));
		gridPartes.addComponentColumn(item -> {
			Button button = new Button(VaadinIcons.TRASH);
			button.addStyleName("borderless");
			button.addClickListener(click ->
			{
				this.protocolo.getPartesProtocolo().remove(item);
				this.gridPartes.getDataProvider().refreshAll();
				this.verificarIndisponibilidade();
			});
//					Notification.show("Clicked: " + person.toString(), Notification.Type.TRAY_NOTIFICATION));
			return button;
		});
		gridPartes.setStyleGenerator(parteProtocolo -> {

			if (parteProtocolo.isIcIndisponivel())
				return "red";
			else
				return null;
		});
	}

	private void gridParteSelectListener(Grid.ItemClick<ParteProtocolo> evt) {
		if(!panelAddParte.isVisible()){
			return;
		}
		this.parteSelecionado = evt.getItem();
		this.conjugeSelecionado = new ParteProtocolo();

		if(!Strings.isNullOrEmpty(this.parteSelecionado.getParte().getUuidConjuge())){
			this.protocolo.getPartesProtocolo().forEach(item -> {
				if(!Strings.isNullOrEmpty(item.getParte().getUuidConjuge())){
					if(item.getParte().getUuidConjuge().compareTo(this.parteSelecionado.getParte().getUuidConjuge()) == 0){
						if(!item.getParte().getNome().equalsIgnoreCase(evt.getItem().getParte().getNome())){
							this.conjugeSelecionado = item;
							return;
						}
					}
				}
			});
		}
		binderParteProtocolo.readBean(this.parteSelecionado);
		binderConjugeProtocolo.readBean(this.conjugeSelecionado);
		fieldCidadeParte.setValue(this.parteSelecionado.getParte().getCidade());
		if(this.conjugeSelecionado != null){
			fieldConjugeCidade.setValue(this.conjugeSelecionado.getParte().getCidade());
		}
	}

	public void mostrarEdicao() {
		panelAddParte.setVisible(!panelAddParte.isVisible());
	}

	
	public void mostrarCamposPJ(){
		panelAddInfo.setVisible(true);
		panelAddEndereco.setVisible(true);
	}
	public void esconderCamposPJ(){
		panelAddEndereco.setVisible(false);
		panelAddInfo.setVisible(false);
	}


	@Override
	public void habilitarForm(boolean habilitar, boolean icProtocoloEditavel) {
		if(!icProtocoloEditavel){
			habilitar = icProtocoloEditavel;
		}
		panelAddParte.setVisible(habilitar);
		gridPartes.getColumns().get(gridPartes.getColumns().size()-1).setHidden(!habilitar);


	}

	@Override
	public void cancelar() {

	}

	@Override
	public void focus() {
		gridPartes.deselectAll();
		btnCancelarParte.click();
		fieldDocumento.focus();
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

