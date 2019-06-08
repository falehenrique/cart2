package br.com.exmart.rtdpjlite.service;

import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

import br.com.exmart.rtdpjlite.util.Utils;
import com.google.common.base.Strings;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.io.RandomAccessBufferedFileInputStream;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.element.Image;
import com.itextpdf.signatures.DigestAlgorithms;
import com.itextpdf.signatures.ICrlClient;
import com.itextpdf.signatures.IExternalDigest;
import com.itextpdf.signatures.IExternalSignature;
import com.itextpdf.signatures.IOcspClient;
import com.itextpdf.signatures.ITSAClient;
import com.itextpdf.signatures.PdfSignature;
import com.itextpdf.signatures.PdfSignatureAppearance;
import com.itextpdf.signatures.PdfSignatureAppearance.RenderingMode;
import com.itextpdf.signatures.PdfSigner;
import com.itextpdf.signatures.PrivateKeySignature;
import com.itextpdf.signatures.ProviderDigest;
import com.itextpdf.signatures.SignatureUtil;

import br.com.exmart.rtdpjlite.vo.AssinaturaDocumentoVO;


@Service
public class SigningService {

		@Autowired
		public PDFService pdfService;

	    static Logger log= LoggerFactory.getLogger(SigningService.class);

	    @Value(value = "${KEYSTORE_URL}")
		public Resource KEYSTORE;

		@Value("${keystore_password}")
		public String KEYSTORE_PASSWORD;
		
		@Value("${keystore_alias}")
		public String alias;
		
		@Value("${URL_PDF_FONT}")
	    private Resource FONT;

		@Value("${URL_PDF_LOGO_CARIMBO_OVAL}")
		private Resource LOGO_CARIMBO_OVAL;

		@Value("${URL_PDF_LOGO_CARIMBO_DIGITAL}")
		private Resource LOGO_CARIMBO_DIGITAL;

		@Value("${URL_PDF_LOGO_CARIMBO_BG}")
		private Resource LOGO_CARIMBO_BG;

		KeyStore keystore = null;
	    public SigningService() {
		
	    	Provider[] providers = Security.getProviders();
    		for (Provider provider : providers) {
			log.info("Security Provider: {}", provider);
		}
	    	
	    try {
			keystore = KeyStore.getInstance("pkcs12", "BC");
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} 
	    }

	    public long diasExpirar() throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, Exception {
			String keystore_password = KEYSTORE_PASSWORD;
			String key_password = KEYSTORE_PASSWORD;
			keystore.load(KEYSTORE.getInputStream(), keystore_password.toCharArray());
			Enumeration aliases = keystore.aliases();
			Long ExpiresIn = null;
			for(; aliases.hasMoreElements();) {
				String alias = (String) aliases.nextElement();
				Date CertExpiryDate = ((X509Certificate) keystore.getCertificate(alias)).getNotAfter();
				SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
				//Tue Oct 17 06:02:22 AEST 2006
				Date today = new Date();
				long DateDiff = CertExpiryDate.getTime() - today.getTime();
				ExpiresIn = DateDiff / (24 * 60 * 60 * 1000);
				if(alias.equalsIgnoreCase(this.alias)){
				    break;
                }
				//System.out.println("Certifiate: " + alias + "\tExpires On: " + CertExpiryDate + "\tFormated Date: " + ft.format(CertExpiryDate)+ "\tToday's Date: " + ft.format(today) + "\tExpires In: "+ ExpiresIn);
			}
			if(ExpiresIn == null)
				throw new Exception("não foi possivel determinar quantos dias para expirar");
			return ExpiresIn;
		}

	    public File sign7(File pdfOriginal,
						  String diretorioSalvar,
						  String nomeArquivo,
						  String motivo, String local,
						  LocalDateTime data,
						  String textoAssinatura,
						  boolean visible,
						  PDFService.DisposicaoPagina dispPagina,
						  File arquivoOriginal
						  ) throws IOException, DocumentException, GeneralSecurityException {

			log.debug("comecou assinar");
	    	File diretorioSaida = new File(diretorioSalvar);
	    	diretorioSaida.mkdirs();
	    	File pdfAssinado = new File(diretorioSalvar+File.separator+nomeArquivo);
			String keystore_password = KEYSTORE_PASSWORD;
			String key_password = KEYSTORE_PASSWORD;
			keystore.load(KEYSTORE.getInputStream(), keystore_password.toCharArray());
			
			PrivateKey key = (PrivateKey) keystore.getKey(alias, key_password.toCharArray());
			Certificate[] chain = keystore.getCertificateChain(alias);


			log.debug("keystore provider   : {}", keystore.getProvider().getName());
			log.debug("Assinando com alias :{}", alias);
			log.debug("chain size: " + chain.length);

			PdfReader reader = new PdfReader(pdfOriginal);
			PdfWriter writer = new PdfWriter(pdfAssinado);
			PdfDocument doc = new PdfDocument(reader,writer);
			FileOutputStream os = new FileOutputStream(pdfAssinado);

			String temporaryFile = pdfAssinado.getAbsolutePath() + ".tmp";

//			System.out.println(temporaryFile);

			PdfSigner signer = new PdfSigner(doc.getReader(),os, temporaryFile,true);
			signer.setCertificationLevel(PdfSigner.NOT_CERTIFIED);


			//TEXTO DO CARIMBO
			String texto;
			ImageData imgCarimbo;


			PdfPage moldPage = doc.getLastPage();
			PageSize pSize = new PageSize(moldPage.getPageSize());
			PdfCanvas cPage = new PdfCanvas(moldPage);


			PdfFont font = null;
			try {
				font = PdfFontFactory.createFont(FONT.getFile().getPath(), PdfEncodings.WINANSI, true);
			} catch (IOException e) {
				e.printStackTrace();
			}
			cPage.setFillColor(Color.BLACK);

			Rectangle rect = new Rectangle(
					(float) (pSize.getWidth()*0.653),  //0.725  Y
					(float) (pSize.getHeight()*0.9),  //0.90	X
					(float) (pSize.getWidth()*0.32),   //0.25   Largura
  					(float) (pSize.getHeight()*0.068)); //0.07   Altura



			cPage.fillStroke();


			PdfFormXObject xObject = new PdfFormXObject(rect);

			Image rectImg = new Image(xObject);

			ImageData imgLogoCarimboOval = ImageDataFactory.create(LOGO_CARIMBO_DIGITAL.getFile().getPath());
			ImageData imgLogoCarimboBg = ImageDataFactory.create(LOGO_CARIMBO_BG.getFile().getPath());


			int paginaAparencia = (dispPagina == PDFService.DisposicaoPagina.ULTIMA_PAGINA?doc.getNumberOfPages():1);

			String arqOriginalHash = "";
			if (arquivoOriginal != null) {
				arqOriginalHash = pdfService.gerarHash(arquivoOriginal);
			}

			PdfSignatureAppearance appearance = signer
					.getSignatureAppearance()
					.setReason(motivo + " - Hash: " + arqOriginalHash)
					.setLocation(local)
					.setReuseAppearance(false)
					.setImage(imgLogoCarimboBg)
					.setSignatureGraphic(imgLogoCarimboOval)
					.setImageScale(100)
					.setRenderingMode(RenderingMode.GRAPHIC_AND_DESCRIPTION)
					.setPageRect(rect)
					.setLayer2Font(font)
					.setLayer2FontSize(6)
					.setLayer2Text(textoAssinatura)
					.setPageNumber(paginaAparencia);



			signer.setFieldName(signer.getNewSigFieldName());
			// Creating the signature
			IExternalSignature pks = new PrivateKeySignature(key, DigestAlgorithms.SHA1, "BC");
			IExternalDigest digest = new ProviderDigest("BC");


			Collection<ICrlClient> crlList=null; IOcspClient ocspClient = null; ITSAClient tsaClient=null;

			writer.close();
            signer.signDetached(digest, pks, chain, crlList, ocspClient, tsaClient, 0, PdfSigner.CryptoStandard.CMS);
            reader.close();

            os.close();
            log.debug("acabou assinar");
			return pdfAssinado;
    }    
	    
		   public List<AssinaturaDocumentoVO> getAssinaturas(String arquivo) throws Exception {
			   //TODO popular VO de assinaturas
			   List<AssinaturaDocumentoVO> retorno = new ArrayList<>();
		        PdfDocument pdfDoc = new PdfDocument(new PdfReader(arquivo));
		        SignatureUtil sign = new SignatureUtil(pdfDoc);
		        List<String> assinaturas = sign.getSignatureNames();
		        assinaturas.forEach((assinatura)->{
		        	PdfSignature ass = 	sign.getSignature(assinatura);
		        	
		        		log.debug(assinatura);
		        		log.debug("data" + formatarData(ass.getDate().toString()));
		        		log.debug("Nome   : " + ass.getName());
		        		log.debug("Local  : " +  ass.getLocation());
		        		log.debug("Motivo : " +  ass.getReason());
		        		retorno.add(new AssinaturaDocumentoVO(ass.getName(), formatarData(ass.getDate().toString()),ass.getLocation(), ass.getReason()));
		        }
	        		);
		        return retorno;
	   }

	private String formatarData(String dataString) {
	    	if(Strings.isNullOrEmpty(dataString))
	    		return "";
	    	LocalDateTime localDateTime = LocalDateTime.of(
	    			Integer.parseInt(dataString.substring(2,6)),
					Integer.parseInt(dataString.substring(6,8)),
					Integer.parseInt(dataString.substring(8,10)),
					Integer.parseInt(dataString.substring(10,12)),
					Integer.parseInt(dataString.substring(12,14)),
					Integer.parseInt(dataString.substring(14,16))
					);
	    	return Utils.formatarDataComHora(localDateTime);
	}

//	    public static void main(String[] args) {
//			SigningService signing = new SigningService();
//			try {
//				File f = new File("AAA.txt");
//				System.out.println(f.getAbsolutePath());
//				File retorno = signing.sign7(new File(COASSINADO),"Motivo da assinatura", "Local da assinatura", LocalDateTime.now(), true);
//			} catch (Exception  e) {
//				e.printStackTrace();
//			}
//	    }
	}  

