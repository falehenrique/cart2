package br.com.exmart.indicadorRTDPJ.ui.impl;

import br.com.exmart.indicadorRTDPJ.ui.ViewLote;
import br.com.exmart.indicadorRTDPJ.ui.util.GerenciarJanela;
import br.com.exmart.indicadorRTDPJ.ui.util.Utils;
import br.com.exmart.rtdpjlite.model.Lote;
import br.com.exmart.rtdpjlite.repository.LoteRepository;
import br.com.exmart.rtdpjlite.service.LoteService;
import br.com.exmart.rtdpjlite.vo.balcaoonline.Pedido;
import com.github.appreciated.app.layout.annotations.MenuCaption;
import com.github.appreciated.app.layout.annotations.MenuIcon;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.renderers.HtmlRenderer;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@SpringView(name = "processamento_lote")
@UIScope
@MenuCaption("Processamento de Lotes")
@MenuIcon(VaadinIcons.FILE_ZIP)
public class ViewLoteImpl extends ViewLote implements View {

    @Autowired
    private LoteService loteService;

    public ViewLoteImpl() {
        gridLotes.removeAllColumns();
        gridLotes.addColumn(Lote::getId).setCaption("Nº Lote");
        gridLotes.addColumn(Lote::getUsuario).setCaption("Usuario");
        gridLotes.addColumn(Lote::getCliente).setCaption("Cliente").setMinimumWidthFromContent(true);
        gridLotes.addColumn(item -> {return Utils.formatarDataComHora(item.getDia());}, new HtmlRenderer()).setCaption("Data");
        gridLotes.addColumn(item -> {return  item.getFinalizados()+" ("+item.getError()+") / "+item.getTotal();}, new HtmlRenderer()).setCaption("Finalizados (Erros) / Total");
        gridLotes.addComponentColumn(lote -> {
            Button button = new Button(VaadinIcons.COMPRESS);
            button.addStyleName("borderless");
            button.addClickListener(click -> {
                panelDownload.removeAllComponents();
                try {
                    Button downloadButton = new Button("Download Arquivo");
                    StreamResource myResource = null;
                    myResource = createResource(loteService.gerarArquivoDonload(lote));
                    FileDownloader fileDownloader = new FileDownloader(myResource);
                    fileDownloader.extend(downloadButton);

                    panelDownload.addComponent(downloadButton);
                } catch (IOException e) {
                    Notification.show("Erro", e.getMessage(), Notification.Type.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            });
            return button;
        }).setWidth(110).setCaption("Gerar Zip");
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        gridLotes.setItems(loteService.findAll());
    }

    private StreamResource createResource(File arquivo) {
        return new StreamResource(new StreamResource.StreamSource() {
            @Override
            public InputStream getStream() {

                try {
                    return new ByteArrayInputStream(FileUtils.readFileToByteArray(arquivo));
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }

            }
        }, FilenameUtils.getName(arquivo.getAbsolutePath()));
    }
}
