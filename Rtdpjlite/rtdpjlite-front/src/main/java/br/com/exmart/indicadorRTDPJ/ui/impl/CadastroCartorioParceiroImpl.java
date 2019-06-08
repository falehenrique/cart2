package br.com.exmart.indicadorRTDPJ.ui.impl;

import br.com.exmart.indicadorRTDPJ.ui.CadastroCartorioParceiro;
import br.com.exmart.indicadorRTDPJ.ui.util.FormatarDocumento;
import br.com.exmart.rtdpjlite.model.CartorioParceiro;
import br.com.exmart.rtdpjlite.model.Estado;
import br.com.exmart.rtdpjlite.service.CartorioParceiroService;
import br.com.exmart.rtdpjlite.service.EstadoService;
import br.com.exmart.util.BeanLocator;
import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.HasValue;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.event.FieldEvents;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.MessageBox;

import java.util.ArrayList;

public class CadastroCartorioParceiroImpl extends CadastroCartorioParceiro {
    private BeanValidationBinder<CartorioParceiro> binderCartorioParceiro = new BeanValidationBinder<>(CartorioParceiro.class);
    private Window window;

    CartorioParceiroService cartorioParceiroService;
    public CadastroCartorioParceiroImpl() {
        estado.setItems(BeanLocator.find(EstadoService.class).findAll());
        estado.addValueChangeListener(evt->listarCidade(evt));

        this.cartorioParceiroService = BeanLocator.find(CartorioParceiroService.class);

        bindCartorioParceiro();

        salvar.addClickListener(evt -> enviarConvite());
        icCertidaoEletronica.addValueChangeListener(evt-> certidaoEletronicaClick(evt));
        fechar.addClickListener(evt->this.window.close());
        cpf.addBlurListener(evt->formatarDocumento(evt));
        cnpj.addBlurListener(evt->formatarDocumento(evt));

    }

    private void formatarDocumento(FieldEvents.BlurEvent evt) {
        TextField textField = (TextField) evt.getSource();
        textField.setValue(FormatarDocumento.formataDocumento(textField.getValue()));
    }

    private void certidaoEletronicaClick(HasValue.ValueChangeEvent<Boolean> evt) {
        if(evt.getValue()){
            qtdViasFisica.setValue("0");
            qtdViasFisica.setEnabled(false);
        }else{
            qtdViasFisica.setEnabled(true);
        }
    }

    private void bindCartorioParceiro() {
        binderCartorioParceiro.setBean(new CartorioParceiro());
        binderCartorioParceiro.bind(estado, "estado");
        binderCartorioParceiro.bind(cidade, "cidade");
        binderCartorioParceiro.bind(comarca, "comarca");
        binderCartorioParceiro.bind(cns, "cns");
        binderCartorioParceiro.bind(cnpj, "cnpj");
        binderCartorioParceiro.bind(denominacao, "nome");
        binderCartorioParceiro.bind(apelido, "alias");
        binderCartorioParceiro.bind(oficial, "oficial");
        binderCartorioParceiro.bind(cpf, "oficialCpf");
        binderCartorioParceiro.bind(email, "email");
        binderCartorioParceiro.bind(icCertidaoEletronica, "icEnvioEletronico");
//        binderCartorioParceiro.bind(qtdViasFisica, "qtdCertidaoFisica");
        binderCartorioParceiro.forField(qtdViasFisica).withConverter(new StringToIntegerConverter(0,"Campo deve conter apenas números")).bind(CartorioParceiro::getQtdCertidaoFisica, CartorioParceiro::setQtdCertidaoFisica);

    }

    private void enviarConvite() {


        String msg = "Deseja realmente cadastrar esse cartorio?";
        if(this.icCertidaoEletronica.getValue()){
            msg = msg + "\n Será enviado um email para o cartório com os dados para acesso e receber certidão online";
        }

        MessageBox
                .createQuestion()
                .withCaption("Confirmar Cadastro")
                .withMessage(msg)
                .withYesButton(() -> {

                    try {
                        cartorioParceiroService.save(binderCartorioParceiro.getBean());
                        this.window.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Notification.show("Atenção","Não foi possivel cadastrar o cartório parceiro: "+ e.getMessage(), Notification.Type.ERROR_MESSAGE);
                    }

                },ButtonOption.caption("Enviar convite"), ButtonOption.focus())
                .withNoButton(ButtonOption.caption("Cancelar"))
                .open();
    }

    private void listarCidade(HasValue.ValueChangeEvent<Estado> evt) {
        cidade.setItems(new ArrayList<>());
        if(evt.getValue() != null){
            cidade.setItems(evt.getValue().getCidades());
        }
        cidade.getDataProvider().refreshAll();
    }
}
