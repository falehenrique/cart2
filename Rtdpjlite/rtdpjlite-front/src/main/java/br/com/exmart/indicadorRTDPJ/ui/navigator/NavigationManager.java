package br.com.exmart.indicadorRTDPJ.ui.navigator;

import br.com.exmart.indicadorRTDPJ.MyUI;
import br.com.exmart.indicadorRTDPJ.ui.impl.*;
import br.com.exmart.rtdpjlite.model.TipoProtocolo;
import com.google.common.base.Strings;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.spring.internal.Conventions;
import com.vaadin.spring.navigator.SpringNavigator;
import com.vaadin.ui.UI;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.List;

@Component
@UIScope
public class NavigationManager{//} extends SpringNavigator {

    /**
     * Find the view id (URI fragment) used for a given view class.
     *
     * @param viewClass
     *            the view class to find the id for
     * @return the URI fragment for the view
     */
    public String getViewId(Class<? extends View> viewClass) {
        SpringView springView = viewClass.getAnnotation(SpringView.class);
        if (springView == null) {
            throw new IllegalArgumentException("The target class must be a @SpringView");
        }

        return Conventions.deriveMappingForView(viewClass, springView);
    }

    /**
     * Navigate to the given view class.
     *
     *
     *            the class of the target view, must be annotated using
     *            {@link SpringView @SpringView}
     */
    public void navigateTo(Class<? extends View> targetView) {
        String viewId = getViewId(targetView);
//        navigateTo(viewId);
        ((MyUI) UI.getCurrent()).navigateTo(viewId);
    }
    public void navegarPara(TipoProtocolo tipo){
        if (tipo.equals(TipoProtocolo.PRENOTACAO_PJ)) {
            navigateTo(ViewPrenotacaoPj.class);
        } else if (tipo.equals(TipoProtocolo.PRENOTACAO_TD)) {
            navigateTo(ViewPrenotacaoTd.class);
        } else if (tipo.equals(TipoProtocolo.EXAMECALCULO_PJ)) {
            navigateTo(ViewProtocoloExameCalculoPj.class);
        } else if (tipo.equals(TipoProtocolo.EXAMECALCULO_TD)) {
            navigateTo(ViewProtocoloExameCalculoTd.class);
        }else if (tipo.equals(TipoProtocolo.CERTIDAO_TD)) {
            navigateTo(ViewProtocoloCertidaoTd.class);
        }else if (tipo.equals(TipoProtocolo.CERTIDAO_PJ)) {
            navigateTo(ViewProtocoloCertidaoPj.class);
        }else{
            navigateTo(BalcaoImpl.class);
        }
    }



    public void navegarPara(TipoProtocolo tipo, Long numeroProtocolo){
        navegarPara(tipo, numeroProtocolo, null);
    }

    public void navegarPara(TipoProtocolo tipo, Long numeroProtocolo, List<Parametro> parametro){
        if (tipo.equals(TipoProtocolo.PRENOTACAO_PJ)) {
            navigateTo(ViewPrenotacaoPj.class, numeroProtocolo, parametro);
        } else if (tipo.equals(TipoProtocolo.PRENOTACAO_TD)) {
            navigateTo(ViewPrenotacaoTd.class, numeroProtocolo, parametro);
        } else if (tipo.equals(TipoProtocolo.EXAMECALCULO_PJ)) {
            navigateTo(ViewProtocoloExameCalculoPj.class, numeroProtocolo, parametro);
        } else if (tipo.equals(TipoProtocolo.EXAMECALCULO_TD)) {
            navigateTo(ViewProtocoloExameCalculoTd.class, numeroProtocolo, parametro);
        }else if (tipo.equals(TipoProtocolo.CERTIDAO_TD)) {
            navigateTo(ViewProtocoloCertidaoTd.class, numeroProtocolo, parametro);
        }else if (tipo.equals(TipoProtocolo.CERTIDAO_PJ)) {
            navigateTo(ViewProtocoloCertidaoPj.class, numeroProtocolo, parametro);
        }
    }
    public void navigateTo(Class<? extends View> targetView, Object parameter, Parametro parametros) {
        String viewId = getViewId(targetView);
        if(parametros == null) {
            ((MyUI) UI.getCurrent()).navigateTo(viewId + "/" + parameter.toString() + "?t=" + Calendar.getInstance().getTimeInMillis());
        }else{
            ((MyUI) UI.getCurrent()).navigateTo(viewId + "/?"+parametros.nome+"="+parametros.valor);
        }
    }
    public void navigateTo(Class<? extends View> targetView, Object parameter, List<Parametro> parametros) {
        String viewId = getViewId(targetView);
        if(parametros == null) {
            ((MyUI) UI.getCurrent()).navigateTo(viewId + "/" + parameter.toString() + "?t=" + Calendar.getInstance().getTimeInMillis());
        }else{
            StringBuilder parametrosUrl = new StringBuilder("/?");
            for(Parametro parametro : parametros){
                parametrosUrl.append(parametro.nome+"="+parametro.valor+"&");
            }
            ((MyUI) UI.getCurrent()).navigateTo(viewId +parametrosUrl.toString().substring(0,parametrosUrl.length()-1));
        }
    }
//    public void navigateTo(Class<? extends View> targetView, Object parameter) {
//        String viewId = getViewId(targetView);
//        ((MyUI) UI.getCurrent()).navigateTo(viewId + "/" + parameter.toString() + "&t=" + Calendar.getInstance().getTimeInMillis());
//
//    }

    public static class Parametro{
        String nome;
        String valor;

        public Parametro(String nome, String valor) {
            this.nome = nome;
            this.valor = valor;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getValor() {
            return valor;
        }

        public void setValor(String valor) {
            this.valor = valor;
        }
    }


    public String recuperarValorParametro(String parametro, String url){
        if(Strings.isNullOrEmpty(parametro) || Strings.isNullOrEmpty(url))
            return null;
        if(parametro.toLowerCase().equals("numeroprotocolo")){
            return url.substring(0,url.indexOf("?"));
        }
        int index = url.toLowerCase().indexOf(parametro.toLowerCase());
        if(index > 0){
            int indexProximoParametro = url.indexOf("&", index);
            if(indexProximoParametro <=0){
                indexProximoParametro= url.length();
            }
            String parametroUrl = url.substring(index,indexProximoParametro);
            String[] parametroArray = parametroUrl.split("=");
            return parametroArray[1];
        }
        return null;
    }


}