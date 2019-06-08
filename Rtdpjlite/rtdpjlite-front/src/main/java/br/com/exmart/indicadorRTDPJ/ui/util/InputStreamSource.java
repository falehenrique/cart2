package br.com.exmart.indicadorRTDPJ.ui.util;

import com.vaadin.server.StreamResource;

import java.io.InputStream;

public class InputStreamSource implements StreamResource.StreamSource {
    private final InputStream data;

    public InputStreamSource(InputStream data) {
        super();
        this.data = data;
    }


    @Override
    public InputStream getStream() {
        return data;
    }
}
