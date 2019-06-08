package br.com.exmart.indicadorRTDPJ.ui.impl;

import br.com.exmart.indicadorRTDPJ.MyUI;
import br.com.exmart.indicadorRTDPJ.ui.Documentos;
import br.com.exmart.indicadorRTDPJ.ui.util.GerenciarJanela;
import br.com.exmart.rtdpjlite.model.Protocolo;
import br.com.exmart.rtdpjlite.model.Registro;
import br.com.exmart.rtdpjlite.model.TipoProtocolo;
import br.com.exmart.rtdpjlite.service.ArquivoService;
import br.com.exmart.rtdpjlite.service.PDFService;
import br.com.exmart.rtdpjlite.service.RegistroService;
import br.com.exmart.rtdpjlite.service.SigningService;
import br.com.exmart.rtdpjlite.vo.AssinaturaDocumentoVO;
import br.com.exmart.util.BeanLocator;
import com.google.common.base.Strings;
import com.google.common.io.ByteStreams;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.wcs.wcslib.vaadin.widget.multifileupload.ui.MultiFileUpload;
import com.wcs.wcslib.vaadin.widget.multifileupload.ui.UploadFinishedHandler;
import com.wcs.wcslib.vaadin.widget.multifileupload.ui.UploadStateWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.pdfviewer.PdfViewer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;


public class DocumentosImpl extends Documentos implements PassoProtocolo{

	protected static Logger logger= LoggerFactory.getLogger(DocumentosImpl.class);
private ArquivoService arquivoService;
private PDFService pdfService;
public static String VIEW_NAME = "documentos";


	private Protocolo protocolo;
	private SigningService signingService;
	private RegistroService registroService;
	private MultiFileUpload singleUpload;
	private boolean icCertidao = false;
	public DocumentosImpl(Protocolo protocolo) {
		logger.debug("iniciou DocumentosImpl");
		this.signingService = BeanLocator.find(SigningService.class);
		this.arquivoService = BeanLocator.find(ArquivoService.class);
		this.pdfService = BeanLocator.find(PDFService.class);
		this.registroService = BeanLocator.find(RegistroService.class);
		this.protocolo = protocolo;

		btnVerAssinatura.addClickListener(evt -> verAssinaturas());




		if (protocolo.getTipo().equals(TipoProtocolo.CERTIDAO_TD) || protocolo.getTipo().equals(TipoProtocolo.CERTIDAO_PJ)) {
			icCertidao = true;
		}
		if(!icCertidao) {
			UploadFinishedHandler handler = new UploadFinishedHandler() {
				@Override
				public void handleFile(InputStream inputStream, String nome, String tipo, long l, int i) {
					try {

//					Notification.show("Documento enviado com sucesso");
						if(tipo.equalsIgnoreCase("application/pdf")) {
							arquivoService.salvarDocumentoProtocolo(protocolo.getNumero(), inputStream, ((MyUI) UI.getCurrent()).getEspecialidadeAtual().getEspecialidade(), protocolo.getTipo(), false);

						}else{
							arquivoService.salvarDocumentoProtocolo(protocolo.getNumero(), inputStream, ((MyUI) UI.getCurrent()).getEspecialidadeAtual().getEspecialidade(), protocolo.getTipo(), true);
						}
						mostrarArquivoPdf(arquivoService.recuperarDocumentoProtocoloFile(protocolo.getNumero(), ((MyUI) UI.getCurrent()).getEspecialidadeAtual().getEspecialidade(), protocolo.getTipo()));
						inputStream.close();

					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			};
			UploadStateWindow uploadWindow = new UploadStateWindow();

			singleUpload = new MultiFileUpload(handler, uploadWindow, false);
			singleUpload.setId("btnUpload");

			singleUpload.setAcceptedMimeTypes(Arrays.asList("application/pdf","application/pkcs7-signature", "application/x-pkcs7-signature", "application/keychain_access"));
			singleUpload.getSmartUpload().setUploadButtonCaptions("Anexar documento", "");
			singleUpload.setMimeTypeErrorMsgPattern("Os documentos devem ser enviados somente com a extensão .pdf ou .p7s");
			panelAdicionarImagemUpload.addComponent(singleUpload);
			panelAdicionarImagemUpload.setExpandRatio(singleUpload, 1);
			panelAdicionarImagemUpload.setComponentAlignment(singleUpload, Alignment.TOP_LEFT);
		}

		logger.debug("acabou DocumentosImpl");
	}

	private void mostrarArquivoPdf(File arquivo) {
		panelVisualizarPdf.removeAllComponents();
		PdfViewer pdf = new PdfViewer();
		pdf.setPreviousPageCaption("ANTERIOR");
		pdf.setNextPageCaption("PRÓXIMO");
		pdf.setPageCaption("PÁGINA ");
		pdf.setDownloadBtnVisible(true);
		pdf.setToPageCaption("DE ");
		pdf.setStyleName("pdfRTDPJ");
		pdf.setFile(arquivo);
		panelVisualizarPdf.addComponentsAndExpand(pdf);
	}


	private void verAssinaturas() {
		List<AssinaturaDocumentoVO> assinaturas = null;
		try {
			assinaturas = signingService.getAssinaturas(arquivoService.recuperarDocumentoProtocoloFile(protocolo.getNumero(), TipoProtocolo.recuperaEspecialidade(protocolo.getTipo()), protocolo.getTipo()).getAbsolutePath());
			GerenciarJanela.abrirJanela("ASSINATURAS DO DOCUMENTO", 90,90, new ModalAssinaturasImpl(assinaturas));
		} catch (Exception e) {
			Notification.show("Assinatura", "O documento não está assinado digitalmente", Notification.Type.WARNING_MESSAGE);
			logger.debug(e.getMessage());
		}
	}



	@Override
	public void habilitarForm(boolean habilitar, boolean icProtocoloEditavel) {
		if(!icProtocoloEditavel){
			habilitar = icProtocoloEditavel;
		}
		panelAdicionarImagemUpload.setEnabled(habilitar);
		if(!icCertidao)
		singleUpload.setEnabled(habilitar);
	}

	@Override
	public void cancelar() {

	}

	@Override
	public void focus() {
		try {
			File arquivo = null;
			if(icCertidao){
				if(!Strings.isNullOrEmpty(protocolo.getRegistroReferencia())) {
					Registro registro = this.registroService.findByRegistroAndEspecialidade(protocolo.getRegistroReferencia(), protocolo.getTipo());
					if (registro != null)
						arquivo = arquivoService.recuperarDocumentoProtocoloRegistroFile(registro.getRegistro(), registro.getNumeroRegistro(), registro.getEspecialidade(), protocolo.getTipo());
				}
			}else {
				arquivo = arquivoService.recuperarDocumentoProtocoloFile(this.protocolo.getNumero(), ((MyUI) UI.getCurrent()).getEspecialidadeAtual().getEspecialidade(), protocolo.getTipo());
			}
			if(arquivo != null) {
				mostrarArquivoPdf(arquivo);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(panelVisualizarPdf.getComponentCount() >0){
			Notification.show("Carregando Documento","Favor aguardar", Notification.Type.TRAY_NOTIFICATION);
		}
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

