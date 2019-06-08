package br.com.exmart.util;

import java.io.File;

/**
 * Created by Heryk on 08/12/17.
 */
public class PdfHash {
    File orig;
    String hash;

    public PdfHash() {
    }

    public PdfHash(File orig, String hash) {
        this.orig = orig;
        this.hash = hash;
    }

    public File getOrig() {
        return orig;
    }

    public void setOrig(File orig) {
        this.orig = orig;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
