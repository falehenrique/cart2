package br.com.exmart.rtdpjlite.service;

import br.com.exmart.rtdpjlite.model.Configuracao;
import br.com.exmart.rtdpjlite.model.Registro;
import br.com.exmart.rtdpjlite.model.TipoProtocolo;
import br.com.exmart.rtdpjlite.service.enums.Diretorio;
import br.com.exmart.rtdpjlite.vo.AssinaturaDocumentoVO;
import com.google.common.base.Strings;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cms.CMSProcessable;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.util.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class ArquivoService {
    @Autowired
    private ConfiguracaoService configuracaoService;
    @Autowired
    private PDFService pdfService;
    protected static Logger logger = LoggerFactory.getLogger(ArquivoService.class);

    private String recuperaPastaSobreNumero(Long numero){
        Double divisao = Math.floor(numero/1000);
        String pastaBaseProtocolo = String.format("%06d",divisao.intValue());
        return pastaBaseProtocolo;
    }

    public String recuperarPasta(Long numeroProtocolo, String especialidade, Diretorio tipoDiretorio, Diretorio subDiretorio, TipoProtocolo tipoProtocolo) throws IOException {
        String diretorioUpload = null;
        diretorioUpload = recuperarDiretorioBase();

        String diretorioBase = especialidade+File.separator+tipoDiretorio.getDiretorio();
        if(tipoDiretorio.equals(Diretorio.PROTOCOLO)){
            if(tipoProtocolo.equals(TipoProtocolo.CERTIDAO_TD) || tipoProtocolo.equals(TipoProtocolo.CERTIDAO_PJ)){
                diretorioBase = diretorioBase+File.separator+Diretorio.PROTOCOLO_CERTIDAO.getDiretorio()+File.separator;
            }else if(tipoProtocolo.equals(TipoProtocolo.PRENOTACAO_PJ) || tipoProtocolo.equals(TipoProtocolo.PRENOTACAO_TD)){
                diretorioBase = diretorioBase+File.separator+Diretorio.PROTOCOLO_PRENOTACAO.getDiretorio()+File.separator;
            }else{
                diretorioBase = diretorioBase+File.separator+Diretorio.PROTOCOLO_EXAME_CALCULO.getDiretorio()+File.separator;
            }
        }
        String retorno = diretorioUpload+diretorioBase+File.separator+recuperaPastaSobreNumero(numeroProtocolo)+ File.separator + String.format("%06d",numeroProtocolo)+File.separator;        
        if(subDiretorio != null){
            retorno = retorno + subDiretorio.getDiretorio()+File.separator;
        }
        File diretorio = new File(retorno);
        Files.createDirectories(diretorio.toPath());
        return retorno;
    }

    public String recuperarDiretorioBase() {
        Configuracao configuracao = configuracaoService.findConfiguracao();
        if(!Strings.isNullOrEmpty(configuracao.getDiretorioUpload())){
            return configuracao.getDiretorioUpload();
        }else{
            return System.getProperty("user.home")+ File.separator + "Downloads"+File.separator;
        }
    }

    public void salvarDocumentoProtocolo(Long numeroProtocolo, InputStream arquivo, String especialidade, TipoProtocolo tipoProtocolo, boolean isPkcs7) throws IOException {
        logger.info("comecou salvar documento protocolo");
        String extensao = ".PDF";
        if(isPkcs7)
            extensao = ".p7s";
        File novo = new File(recuperarPasta(numeroProtocolo, especialidade, Diretorio.PROTOCOLO, null, tipoProtocolo) + numeroProtocolo + extensao);
        FileUtils.copyInputStreamToFile(arquivo, novo);
//        Files.write(novo.toPath(), arquivo);
        if(isPkcs7) {
            try{


                byte[] buffer = new byte[(int) novo.length()];
                DataInputStream in = new DataInputStream(new FileInputStream(novo));
                in.readFully(buffer);
                in.close();

                //Corresponding class of signed_data is CMSSignedData
                CMSSignedData signature = new CMSSignedData(buffer);
                Store cs = signature.getCertificates();
                SignerInformationStore signers = signature.getSignerInfos();
                Collection c = signers.getSigners();
                Iterator it = c.iterator();

                //the following array will contain the content of xml document
                byte[] data = null;

                while (it.hasNext()) {
                    SignerInformation signer = (SignerInformation) it.next();
//                    Collection certCollection = cs.getMatches(signer.getSID());
//                    Iterator certIt = certCollection.iterator();
//                    X509CertificateHolder cert = (X509CertificateHolder) certIt.next();

                    CMSProcessable sc = signature.getSignedContent();
                    data = (byte[]) sc.getContent();
                }
                FileUtils.writeByteArrayToFile(new File(recuperarPasta(numeroProtocolo, especialidade, Diretorio.PROTOCOLO, null, tipoProtocolo) + numeroProtocolo +".PDF"), data);
            }catch (Exception e ){
                throw new IOException(e.getMessage());
            }
        }
        logger.info("acabou salvar documento protocolo");
    }

    public File criarArquivoTemporario(String numeroProtocolo, byte[] arquivo) throws IOException {
        File temp = File.createTempFile(numeroProtocolo,"PDF");
//        File novo = new File(recuperarPasta(numeroProtocolo, especialidade, Diretorio.PROTOCOLO, null, tipoProtocolo)+numeroProtocolo+".PDF");
        Files.write(temp.toPath(), arquivo);
        return temp;
    }
    public byte[] recuperarDocumentoProtocolo(Long numeroProtocolo, String especialidade, TipoProtocolo tipoProtocolo) throws IOException {
        if(numeroProtocolo == null){
            return null;
        }
        File arquivo = new File(recuperarPasta(numeroProtocolo, especialidade, Diretorio.PROTOCOLO, null, tipoProtocolo)+numeroProtocolo+".PDF");
        if(arquivo.exists()) {
            return IOUtils.toByteArray(new FileInputStream(arquivo));
        }else{
            return null;
        }
    }

    public InputStream recuperarDocumentoProtocoloIS(Long numeroProtocolo, String especialidade, TipoProtocolo tipoProtocolo) throws IOException {
        if(numeroProtocolo == null){
            return null;
        }
        File arquivo = new File(recuperarPasta(numeroProtocolo, especialidade, Diretorio.PROTOCOLO, null, tipoProtocolo)+numeroProtocolo+".PDF");
        if(arquivo.exists()) {
            return FileUtils.openInputStream(arquivo);
        }else{
            return null;
        }
    }

    public File recuperarDocumentoProtocoloFile(Long numeroProtocolo, String especialidade, TipoProtocolo tipoProtocolo) throws IOException {
        if(numeroProtocolo == null){
            return null;
        }
        File arquivo = new File(recuperarPasta(numeroProtocolo, especialidade,Diretorio.PROTOCOLO, null, tipoProtocolo)+numeroProtocolo+".PDF");
        if(arquivo.exists()) {
            return arquivo;
        }else{
            return null;
        }
    }

    public File recuperarDocumentoProtocoloRegistroFile(String registro, Long numeroRegistro, String especialidade, TipoProtocolo tipoProtocolo){
        File arquivo = null;
        try {
            arquivo = new File(recuperarPasta(numeroRegistro, especialidade, Diretorio.REGISTRO, null, tipoProtocolo)+File.separator+registro+".PDF");
        } catch (IOException e) {
            return null;
        }
        if(arquivo.exists()) {
            return arquivo;
        }else{
            return null;
        }
    }
    public FileInputStream recuperarDocumentoProtocoloIS(Long numeroProtocolo, String especialidade, TipoProtocolo tipoProtocolo, Diretorio subDiretorio) throws IOException {
        if(numeroProtocolo == null){
            return null;
        }
        File arquivo = new File(recuperarPasta(numeroProtocolo, especialidade, Diretorio.PROTOCOLO, subDiretorio, tipoProtocolo)+numeroProtocolo+".PDF");
        if(arquivo.exists()) {
            return new FileInputStream(arquivo);
        }else{
            return null;
        }
    }

    public File recuperarDocumentoProtocoloFile(Long numeroProtocolo, String especialidade, TipoProtocolo tipoProtocolo, Diretorio subDiretorio) throws IOException {
        if(numeroProtocolo == null){
            return null;
        }
        File arquivo = new File(recuperarPasta(numeroProtocolo, especialidade, Diretorio.PROTOCOLO, subDiretorio, tipoProtocolo)+numeroProtocolo+".PDF");
        if(arquivo.exists()) {
            return arquivo;
        }else{
            return null;
        }
    }

    public void salvarDocumentoProtocoloAndamento(Long numeroProtocolo,Long idAndamento, byte[] arquivo, String especialidade, TipoProtocolo tipoProtocolo) throws IOException {
        Files.write(new File(recuperarPasta(numeroProtocolo,especialidade, Diretorio.PROTOCOLO, Diretorio.ANDAMENTO, tipoProtocolo)+idAndamento+".PDF").toPath(), arquivo);
    }

    public byte[] recuperarDocumentoProtocoloAndamento(Long numeroProtocolo, Long idAndamento, String especialidade, TipoProtocolo tipoProtocolo) throws IOException {
        if(numeroProtocolo == null || idAndamento == null){
            return null;
        }
        File arquivo =new File(recuperarPasta(numeroProtocolo,especialidade, Diretorio.PROTOCOLO, Diretorio.ANDAMENTO, tipoProtocolo)+idAndamento+".PDF");
        if(arquivo.exists()) {
            return Files.readAllBytes(arquivo.toPath());
        }else{
            return null;
        }
    }

    public List<AssinaturaDocumentoVO> visualizarAssinaturasDocumentoProtocolo(Long numero) {
        //TODO deve fazer rotina para recuperar assinaturas
        return new ArrayList<>();
    }

    public boolean isDocumentoProtocoloExists(Long numeroProtocolo, String especialidade, TipoProtocolo tipoProtocolo) {
        if(numeroProtocolo == null){
            return false;
        }
        File arquivo = null;
        try {
            arquivo = new File(recuperarPasta(numeroProtocolo,especialidade, Diretorio.PROTOCOLO, null, tipoProtocolo)+numeroProtocolo+".PDF");
        } catch (IOException e) {
            return false;
        }
        return arquivo.exists();
    }


    public File copiarDocumentoProtocoloregistroFile(Long numeroProtocolo, String especialidade, String registro, Long numeroRegistro, TipoProtocolo tipoProtocolo) {

        try {
            File arquivoFrom = recuperarDocumentoProtocoloRegistroFile(registro, numeroRegistro, especialidade, tipoProtocolo);
            if(arquivoFrom != null) {
                String arquivoTo = recuperarPasta(numeroProtocolo, especialidade,Diretorio.PROTOCOLO,null, tipoProtocolo);
                try {
                    Files.delete(new File(arquivoTo + numeroProtocolo + ".PDF").toPath());
                }catch (IOException e){

                }
                File fileTo = new File(arquivoTo + numeroProtocolo + ".PDF");
                FileUtils.copyFile(arquivoFrom,fileTo);
                return fileTo;
            }
        } catch (IOException e) {
        }
        return null;
    }

    public File existeArquivoProtocoloCarimbado(Long numeroProtocolo, String especialidade, Diretorio sub, TipoProtocolo tipoProtocolo) {
        try {

            String pasta = recuperarPasta(numeroProtocolo, especialidade, Diretorio.PROTOCOLO, sub, tipoProtocolo);
            if(Files.exists(new File(pasta+numeroProtocolo+".PDF").toPath())){
                return  new File(pasta+numeroProtocolo+ ".PDF");
            }else{
                return null;
            }

        } catch (IOException e) {
            return null;
        }

    }


    public void excluirArquivo(File arquivo){
        if(arquivo == null) {
            return;
        }
        if(arquivo.exists()) {
            arquivo.delete();
        }
    }

    public void salvarArquivoRetroativo(String registro, File arquivoTemporario, String especialidade, Long numeroRegistro) {
        TipoProtocolo tipoProtocolo = null;
        if(especialidade.equalsIgnoreCase("PJ")){
            tipoProtocolo = TipoProtocolo.PRENOTACAO_PJ;
        }else{
            tipoProtocolo = TipoProtocolo.PRENOTACAO_TD;
        }
        try {
            File arquivoDestino = new File(recuperarPasta(numeroRegistro, especialidade, Diretorio.REGISTRO, null, tipoProtocolo)+File.separator+registro+".PDF");
            FileUtils.copyFile(arquivoTemporario, arquivoDestino);
            arquivoTemporario.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public InputStream recuperarArquivoPedido(Long id) throws IOException {
        return FileUtils.openInputStream(recuperarArquivoPedidoFile(id));
    }
    public File recuperarArquivoPedidoFile(Long id){
        String diretorioBase = recuperarDiretorioBase();
        return new File(diretorioBase+File.separator+"PEDIDO"+File.separator+id+File.separator+id+".PDF");
    }

    public InputStream recuperarDocumentoProtocoloRegistroFileIS(String registro, Long numeroRegistro, String especialidade, TipoProtocolo tipoProtocolo) throws FileNotFoundException {
        if(Strings.isNullOrEmpty(registro )){
            return null;
        }
        File arquivo = recuperarDocumentoProtocoloRegistroFile(registro, numeroRegistro, especialidade, tipoProtocolo);
        if(arquivo.exists()) {
            return new FileInputStream(arquivo);
        }else{
            return null;
        }

    }


    public String diretorioLote(){
        Configuracao configuracao = configuracaoService.findConfiguracao();

        String diretorioBase = null;
        if(!Strings.isNullOrEmpty(configuracao.getDiretorioUploadLote())){
            diretorioBase = configuracao.getDiretorioUpload();
        }else{
            diretorioBase = System.getProperty("user.home")+ File.separator + "Downloads"+File.separator+"lote"+File.separator;
        }

        return diretorioBase;
    }
    public File salvarArquivoLote(InputStream inputStream, String nome, Long idCliente) throws IOException {
        String diretorioBase = diretorioLote();

        File arquivoZip = new File(diretorioBase+nome);
        FileUtils.copyInputStreamToFile(inputStream, arquivoZip);

        String arquivoRetorno = null;
        FileInputStream fis;
        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(arquivoZip);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while(ze != null){
                String fileName = ze.getName();
                if(!fileName.toUpperCase().contains("MACOS")){

                    File newFile = new File(diretorioBase+idCliente+ File.separator + fileName);
                    if(fileName.endsWith(".txt")){
                        arquivoRetorno=newFile.getAbsolutePath();
                    }
                    //create directories for sub directories in zip
                    new File(newFile.getParent()).mkdirs();
                    FileOutputStream fos = new FileOutputStream(newFile);
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                    //close this ZipEntry
                    zis.closeEntry();
                }

                ze = zis.getNextEntry();
            }
            //close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
            arquivoZip.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new File(arquivoRetorno);

    }

    public boolean isDocumentoRegistroExistis(Registro registro, TipoProtocolo tipoProtocolo) {
        File arquivo = recuperarDocumentoProtocoloRegistroFile(registro.getRegistro(), registro.getNumeroRegistro(), TipoProtocolo.recuperaEspecialidade(tipoProtocolo), tipoProtocolo);
        if(arquivo == null){
            return false;
        }else{
            return true;
        }
    }

    public void excluirArquivos(Diretorio diretorio, Long numeroRegistro, String especialidade, TipoProtocolo tipoProtocolo) throws IOException {
        FileUtils.deleteDirectory(new File(recuperarPasta(numeroRegistro, especialidade, diretorio, null, tipoProtocolo)));
    }
}
