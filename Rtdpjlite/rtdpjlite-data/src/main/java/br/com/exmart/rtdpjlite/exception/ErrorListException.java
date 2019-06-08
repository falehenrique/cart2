package br.com.exmart.rtdpjlite.exception;

import java.util.List;

public class ErrorListException extends Exception{
    private List<String> erroList;


    public ErrorListException(String message, List<String> erroList) {
        super(message);
        this.erroList = erroList;
    }

    public List<String> getErroList() {
        return erroList;
    }

    public String recuperaErros(){
        String retorno = "";
        for(String erro : erroList){
            retorno = retorno + erro + "\n";
        }
        return  retorno;
    }
}
