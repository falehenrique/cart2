package br.com.exmart.indicadorRTDPJ.ui.impl;

import br.com.exmart.indicadorRTDPJ.ui.VisualizarImagens;
import br.com.exmart.indicadorRTDPJ.ui.util.GerenciarJanela;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;

import java.io.File;

public class VisualizarImagensImpl extends VisualizarImagens {
	 
	
	public VisualizarImagensImpl() {

//		File documento = new File("/rtdpjlite/src/main/webapp/VAADIN/themes/mytheme/Lei6015-RTDPJ.pdf");

		String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
		
		//Image as a file resource
		FileResource resource = new FileResource(new File(basepath + "/WEB-INF/Lei6015-RTDPJ.pdf"));
		
		System.out.println(resource.getSourceFile());
//		PdfViewer viewer = new PdfViewer(resource.getSourceFile());
//		viewer.setPreviousPageCaption("ANTERIOR");
//		viewer.setNextPageCaption("PRÓXIMO");
//		viewer.setPageCaption("PÁGINA ");
//		viewer.setToPageCaption("DE ");
//
//		pdfViewer.addComponentsAndExpand(viewer);
		
	}


	private void maximizar() {
		GerenciarJanela.abrirJanela("", 100, 100, new VisualizarImagensImpl());
		
	}


}
