package br.com.exmart.rtdpjlite.vo;

public class Arquivo{
    private String base64;  // base64 do arquivo
    private String md5;    // md5 do arquivo

    public Arquivo() {
    }

    public Arquivo(String base64, String md5) {
        this.base64 = base64;
        this.md5 = md5;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}
