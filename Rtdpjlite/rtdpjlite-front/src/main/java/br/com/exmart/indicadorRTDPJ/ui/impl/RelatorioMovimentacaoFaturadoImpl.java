package br.com.exmart.indicadorRTDPJ.ui.impl;

import br.com.exmart.indicadorRTDPJ.ui.RelatorioMovimentacaoFaturado;
import br.com.exmart.indicadorRTDPJ.ui.util.Utils;
import br.com.exmart.rtdpjlite.model.Cliente;
import br.com.exmart.rtdpjlite.service.ClienteService;
import br.com.exmart.rtdpjlite.service.ConfiguracaoService;
import com.github.appreciated.app.layout.annotations.MenuCaption;
import com.github.appreciated.app.layout.annotations.MenuIcon;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileResource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.vaadin.addon.ewopener.EnhancedBrowserWindowOpener;

import javax.sql.DataSource;
import java.io.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@SpringView(name = "faturados")
@UIScope
@MenuCaption("Movimentação por Cliente(Faturado)")
@MenuIcon(VaadinIcons.PIGGY_BANK)
public class RelatorioMovimentacaoFaturadoImpl extends RelatorioMovimentacaoFaturado implements View {

    @Autowired
    private ClienteService clienteService;
    @Autowired
    private ConfiguracaoService configuracaoService;
    @Autowired
    protected DataSource localDataSource;
    @Autowired
    ResourceLoader resourceLoader;
    java.sql.Connection con = null;

    public RelatorioMovimentacaoFaturadoImpl() {
        comboCliente.setItemCaptionGenerator(Cliente::getNome);
        btnGerarRelatorio.addClickListener(evt->btnGerarRelatorioClickListener());
    }

    private void btnGerarRelatorioClickListener() {
        EnhancedBrowserWindowOpener.extendOnce(btnGerarRelatorio).open(gerarRelatorio());
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        comboCliente.setItems(clienteService.findAll());
        comboCliente.getDataProvider().refreshAll();
    }


    private Map<String, Object> preencherParametros(String diretorio) {
        Map<String, Object> parametros = new HashMap<>();

        parametros.put("SUBREPORT_DIR",diretorio+ File.separator);
        parametros.put("cartorio", configuracaoService.findConfiguracao().getNomeCartorio());
        parametros.put("endereco", configuracaoService.findConfiguracao().getEnderecoCompleto());
        parametros.put("cnpj", configuracaoService.findConfiguracao().getCnpjCartorio());
        parametros.put("tabeliao", configuracaoService.findConfiguracao().getOficial());
        parametros.put("dtInicial", Utils.localDateToDate(this.dtInicial.getValue()));
        parametros.put("dtFinal", Utils.localDateToDateEndDay(this.dtFinal.getValue()));
        java.util.Locale locale = new Locale( "pt", "BR" );
        parametros.put( JRParameter.REPORT_LOCALE, locale );
        parametros.put("oficial", configuracaoService.findConfiguracao().getOficial());
        Integer idCliente = null;
        if(comboCliente.getValue() != null) {
            idCliente = comboCliente.getValue().getId().intValue();
        }
        parametros.put("ClienteId", idCliente);
        parametros.put("diretorioImagem", VaadinService.getCurrent().getBaseDirectory().getAbsolutePath() + "/static/relatorios/");
        return parametros;
    }

    private StreamResource gerarRelatorio() {
        StreamResource resourcesReport = null;
        try {
            if(!validarForm()){
                Notification.show("Ops ;) Acho que faltou preencher algum campo", Notification.Type.ERROR_MESSAGE);
                return null;
            }

            con = localDataSource.getConnection();

            // Find the application directory
            String basepath = VaadinService.getCurrent()
                    .getBaseDirectory().getAbsolutePath();

// Image as a file resource
            FileResource resource = new FileResource(new File(basepath +
                    "/static/relatorios/FaturadosGeral.jrxml"));


//            File resource = new File("classpath:");
            JasperDesign jasperDesign = JRXmlLoader.load(new FileInputStream(resource.getSourceFile()));

            JasperReport jasperReport  = JasperCompileManager.compileReport(jasperDesign);

            StreamResource.StreamSource source = new StreamResource.StreamSource() {
                public InputStream getStream() {
                    byte[] b = null;
                    try{
                        b = JasperRunManager.runReportToPdf(jasperReport, preencherParametros(resource.getSourceFile().getParent()), con);

                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                    return new ByteArrayInputStream(b);
                }
            };
            resourcesReport = new StreamResource(source, "Faturados" + System.currentTimeMillis() + ".PDF");
            return resourcesReport;

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (JRException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resourcesReport;
    }

    private boolean validarForm() {
        if(this.dtInicial.getValue() == null){
            return false;
        }
        if(this.dtFinal.getValue() == null){
            return false;
        }
        return true;
    }
    Button.ClickListener clickListener;
    @Override
    public void attach() {
        super.attach();
        EnhancedBrowserWindowOpener opener = EnhancedBrowserWindowOpener.extendOnce(btnGerarRelatorio);
        clickListener = e2 -> opener.open(gerarRelatorio());
    }
    // In detach remove the extension and the click listener
    @Override
    public void detach() {
        EnhancedBrowserWindowOpener.extendOnce(btnGerarRelatorio).remove();
        btnGerarRelatorio.removeClickListener(clickListener);
        super.detach();
    }
}