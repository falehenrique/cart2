package br.com.exmart.rtdpjlite.service;

import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.DEROutputStream;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.*;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.util.Store;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import sun.security.pkcs.ContentInfo;
import sun.security.pkcs.PKCS7;
import sun.security.pkcs.SignerInfo;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.x509.AlgorithmId;
import sun.security.x509.X500Name;

import javax.validation.Valid;

@Service
public class PKCS7Signer {



    Resource STORENAME;

    String STOREPASS;

    @Value(value = "${KEYSTORE_URL}")
    private Resource PATH_TO_KEYSTORE;
    @Value(value = "${keystore_alias}")
    private  String KEY_ALIAS_IN_KEYSTORE;
    @Value("${keystore_password}")
    private String KEYSTORE_PASSWORD;
    private static final String SIGNATUREALGO = "SHA1withRSA";

    public PKCS7Signer() {
    }

    @SuppressWarnings("unchecked")
    public KeyStore loadKeyStore() throws Exception {
        KeyStore keystore = KeyStore.getInstance("JKS");
        InputStream is = new FileInputStream(PATH_TO_KEYSTORE.getFile());
        keystore.load(is, KEYSTORE_PASSWORD.toCharArray());
        return keystore;
    }

    @SuppressWarnings("unchecked")
    public CMSSignedDataStreamGenerator setUpProvider(final KeyStore keystore) throws Exception {

        Security.addProvider(new BouncyCastleProvider());

        Certificate[] certchain = (Certificate[]) keystore.getCertificateChain(KEY_ALIAS_IN_KEYSTORE);

        final List<Certificate> certlist = new ArrayList<Certificate>();

        for (int i = 0, length = certchain == null ? 0 : certchain.length; i < length; i++) {
            certlist.add(certchain[i]);
        }

        Store certstore = new JcaCertStore(certlist);

        Certificate cert = keystore.getCertificate(KEY_ALIAS_IN_KEYSTORE);

        ContentSigner signer = new JcaContentSignerBuilder(SIGNATUREALGO).setProvider("BC").
                build((PrivateKey) (keystore.getKey(KEY_ALIAS_IN_KEYSTORE, KEYSTORE_PASSWORD.toCharArray())));

        CMSSignedDataStreamGenerator generator = new CMSSignedDataStreamGenerator();

        generator.addSignerInfoGenerator(new JcaSignerInfoGeneratorBuilder(new JcaDigestCalculatorProviderBuilder().setProvider("BC").
                build()).build(signer, (X509Certificate) cert));

        generator.addCertificates(certstore);

        return generator;

    }

    @SuppressWarnings("unchecked")
    public File signPkcs7(File file) throws Exception {
        int buff = 16384;
        byte[] buffer = new byte[buff];
        int unitsize = 0;
        long read = 0;
        long offset = file.length();
        KeyStore keyStore = loadKeyStore();
        CMSSignedDataStreamGenerator gen = setUpProvider(keyStore);
        FileInputStream is = new FileInputStream(file);
        File out = File.createTempFile(file.getName().replace(".PDF",""),".p7s");
        FileOutputStream bOut = new FileOutputStream(out);
        OutputStream sigOut = gen.open(bOut,true);

        int len = 0;

        while ((len = is.read(buffer, 0, buffer.length)) > 0)
        {
            sigOut.write(buffer, 0, len);
        }
        sigOut.close();
        bOut.close();
        is.close();

        out.deleteOnExit();
        return out;
    }



    @SuppressWarnings("unchecked")
    public byte[] unsignPkcs7(String envelopedData) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        CMSSignedData signeddata = new CMSSignedData(Base64.decode(envelopedData.getBytes()));
        Store store = signeddata.getCertificates();
        SignerInformationStore signers = signeddata.getSignerInfos();
        Collection c = signers.getSigners();
        Iterator it = c.iterator();
        //the following array will contain the content of encoded signed data
        byte[] data = null;
        while (it.hasNext()) {
            SignerInformation signer = (SignerInformation) it.next();
            Collection certCollection = store.getMatches(signer.getSID());
            Iterator certIt = certCollection.iterator();
            X509CertificateHolder certHolder = (X509CertificateHolder) certIt.next();
            X509Certificate cert = new JcaX509CertificateConverter().setProvider("BC").getCertificate(certHolder);
            if (signer.verify(new JcaSimpleSignerInfoVerifierBuilder().setProvider("BC").build(cert))) {
                System.out.println("verified");
                CMSProcessable sc = signeddata.getSignedContent();
                data = (byte[]) sc.getContent();
            }
        }
        return data;
    }

//    @SuppressWarnings("unchecked")
//    public static void main(String[] args) throws Exception {
//        PKCS7Signer signer = new PKCS7Signer();
//        KeyStore keyStore = signer.loadKeyStore();
//        CMSSignedDataGenerator signatureGenerator = signer.setUpProvider(keyStore);
//
//        String content = "some bytes to be signed";
//        byte[] signedBytes = signer.signPkcs7(content.getBytes("UTF-8"), signatureGenerator);
//        String signedString = new String(Base64.encode(signedBytes));
//        System.out.println("Signed Encoded Bytes: " + signedString);
//
//        byte[] unsignedBytes = signer.unsignPkcs7(signedString);
//        String unsignedString = new String(unsignedBytes);
//        System.out.println("Decoded String: " + unsignedString);
//    }

}
