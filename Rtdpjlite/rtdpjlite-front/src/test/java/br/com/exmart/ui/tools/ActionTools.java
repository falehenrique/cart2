package br.com.exmart.ui.tools;

import br.com.exmart.ui.TestBase;
import com.vaadin.testbench.elements.*;

/**
 * Created by Heryk on 05/07/2018.
 */
public class ActionTools extends TestBase {

    public ActionTools() {
    }

    public void selTab(String caption) {
        TabSheetElement elem = $(TabSheetElement.class).first();
        elem.openTab(caption);
    }

    public ButtonElement getButton(String caption) {
        return $(ButtonElement.class).caption(caption).first();
    }
}
