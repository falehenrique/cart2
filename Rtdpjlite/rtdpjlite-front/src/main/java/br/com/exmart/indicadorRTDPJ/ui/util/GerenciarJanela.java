package br.com.exmart.indicadorRTDPJ.ui.util;


import com.vaadin.server.Resource;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;

import br.com.exmart.indicadorRTDPJ.ui.custom.MyMessageBox;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by rodrigobragiao on 22/09/16.
 */
public class GerenciarJanela {

    private final static Logger logger = Logger.getLogger(GerenciarJanela.class.getName());
    public static void abrirJanela( String titulo, Integer altura, Integer largura, Component tela, Window.CloseListener closeListener, boolean icCloseable){
        abrirJanela(titulo, altura, largura, tela, closeListener, icCloseable, true, false, false);
    }

    public static void abrirJanela( String titulo, Integer altura, Integer largura, Component tela, Window.CloseListener closeListener, boolean icCloseable, boolean icModal, boolean icDraggable, boolean icResizable){
        Window window = new Window(titulo);
        window.setCaption(titulo);
        window.setWidth(largura, Sizeable.Unit.PERCENTAGE);
        window.setHeight(altura, Sizeable.Unit.PERCENTAGE);
        window.setModal(icModal);
        window.setClosable(icCloseable);
        window.setDraggable(icDraggable);
        window.setResizable(icResizable);

        if(closeListener != null){
            window.addCloseListener(closeListener);
        }
        Class<?> clazz = tela.getClass();
        Field field;
        try {
            field = clazz.getDeclaredField("window");
            field.setAccessible(true);
            field.set(tela, window);

        } catch (Exception e) {
            logger.log(Level.WARNING,"View sem uma propriedade com o nome window");
        }
        window.setContent(tela);
        UI.getCurrent().addWindow(window);
        try{
            Method foco = clazz.getMethod("focus");
            foco.invoke(tela);
        } catch (NoSuchMethodException e) {

        } catch (InvocationTargetException e) {

        } catch (IllegalAccessException e) {

        }
    }


    public static void abrirPopup(String caption, Resource icon, String message, Button focus, Button... buttons){

        MyMessageBox window = new MyMessageBox(caption,icon, message,focus, buttons);
        window.setWidth(40, Sizeable.Unit.PERCENTAGE);
        window.setHeight(170, Sizeable.Unit.PIXELS);

        window.setClosable(false);
        window.setModal(true);
        window.setDraggable(false);
        window.setResizable(false);
//        window.setPositionY(Page.getCurrent().getBrowserWindowWidth()-150);
        window.center();
        UI.getCurrent().addWindow(window);
        try{
            Class<?> clazz = window.getClass();
            Method foco = clazz.getMethod("focusBotao");
            foco.invoke(window);
        } catch (NoSuchMethodException e) {
            e.getLocalizedMessage();
        } catch (InvocationTargetException e) {
            e.getLocalizedMessage();
        } catch (IllegalAccessException e) {
            e.getLocalizedMessage();
        }
    }

    public static void abrirJanela(String titulo, Integer altura, Integer largura, Component tela) {
        abrirJanela(titulo, altura, largura, tela, null, true);
    }

    public static void abrirJanela(String titulo, Integer altura, Integer largura, Component tela, boolean icCloseable) {
        abrirJanela(titulo, altura, largura, tela, null, icCloseable);
    }

    public static void alerta(Notification.Type tipo, String texto){
        Notification.show(texto, tipo);
    }


}
