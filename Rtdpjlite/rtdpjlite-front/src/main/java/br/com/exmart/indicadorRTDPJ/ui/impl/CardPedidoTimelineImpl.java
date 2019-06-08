package br.com.exmart.indicadorRTDPJ.ui.impl;

import br.com.exmart.indicadorRTDPJ.ui.CardPedidoTimeline;
import br.com.exmart.indicadorRTDPJ.ui.navigator.NavigationManager;
import br.com.exmart.indicadorRTDPJ.ui.util.GerenciarJanela;
import br.com.exmart.indicadorRTDPJ.ui.util.Utils;
import br.com.exmart.indicadorRTDPJ.ui.view.ModalVisualizarDocumento;
import br.com.exmart.rtdpjlite.model.TipoProtocolo;
import br.com.exmart.rtdpjlite.service.rest.balcaoonline.PedidoService;
import br.com.exmart.rtdpjlite.vo.balcaoonline.Pedido;
import br.com.exmart.rtdpjlite.vo.balcaoonline.PedidoMensagem;
import br.com.exmart.rtdpjlite.vo.balcaoonline.PedidoMensagemTipo;
import br.com.exmart.util.BeanLocator;
import com.google.common.base.Strings;
import com.vaadin.ui.Button;
import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.MessageBox;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.*;

public class CardPedidoTimelineImpl extends CardPedidoTimeline {
    PedidoMensagem mensagem  = null;
    Pedido pedido = null;
    PedidoService pedidoService;
    NavigationManager navigationManager;
    public CardPedidoTimelineImpl(PedidoMensagem mensagem, Pedido pedido) {
        navigationManager = BeanLocator.find(NavigationManager.class);
        pedidoService= BeanLocator.find(PedidoService.class);
        this.pedido = pedido;
        this.mensagem = mensagem;
        remetenteLabel.setValue(mensagem.getRemetente().getNome());
        statusLabel.setValue(mensagem.getTipo().getNome());
        if(mensagem.getData()!=null)
        dataLabel.setValue(Utils.formatarDataComHora(mensagem.getData()));
        mensagemText.setValue(mensagem.getConteudo());
        mensagemText.setEnabled(false);
        btnBaixarAnexo.setEnabled(!Strings.isNullOrEmpty(mensagem.getArquivo()));
        btnVisualizarAnexo.setEnabled(!Strings.isNullOrEmpty(mensagem.getArquivo()));

        btnBaixarAnexo.addClickListener(evt-> btnBaixarAnexoListener(evt));
        btnVisualizarAnexo.addClickListener(evt-> {
            try {
                File destinationFile = getFile();
                GerenciarJanela.abrirJanela("Documento do Pedido", 90, 60, new ModalVisualizarDocumento(destinationFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        if(mensagem.getTipo().getId().equals(new Long("3"))){
            btnBaixarAnexo.setVisible(true);
        }else{
            btnBaixarAnexo.setVisible(false);
        }
    }

    private File getFile() throws IOException {

        byte[] decodedImg = null;//FIXME corrigir pedidoService.recuperarArquivoMensagem(this.pedido.getId(), this.mensagem.getId());
        File destinationFile = null;
        destinationFile = File.createTempFile(String.valueOf(Calendar.getInstance().getTimeInMillis()),".PDF");
        Files.write(destinationFile.toPath(), decodedImg);
        return destinationFile;
    }

    private void btnBaixarAnexoListener(Button.ClickEvent evt) {
        MessageBox
                .createQuestion()
                .withCaption("Atenção")
                .withMessage("Deseja protocolocar uma averbação para esse registro?")
                .withYesButton(() -> {
                    List<NavigationManager.Parametro> parametros = new ArrayList<>();
                    parametros.add(new NavigationManager.Parametro("registroReferencia",this.pedido.getRegistro()));
                    parametros.add(new NavigationManager.Parametro("idPedido",this.pedido.getId().toString()));
                    parametros.add(new NavigationManager.Parametro("idCliente",this.pedido.getApresentante().getId().toString()));
                    File arquivoTemporario = null;
                    try {
                        arquivoTemporario = getFile();
                        parametros.add(new NavigationManager.Parametro("filename", arquivoTemporario.getName()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    navigationManager.navegarPara(TipoProtocolo.recuperaTipoPrenotacaoByEspecialidade(pedido.getTipo()), null, parametros);

                }, ButtonOption.caption("SIM"))
                .withNoButton(ButtonOption.caption("NÃO"))
                .open();
    }
}
