package br.com.exmart.indicadorRTDPJ.ui.component;

import br.com.exmart.rtdpjlite.model.ChecklistItem;
import com.vaadin.data.HasValue;

public class ChecklistItemComponentImpl extends ChecklistItemComponent {
    private ChecklistItem checklistItem;

    public ChecklistItemComponentImpl(ChecklistItem checklistItem) {
        this.checklistItem = checklistItem;
        this.item.setCaption(this.checklistItem.getNome());

        this.item.setValue(this.checklistItem.isCheckado());
        if(checklistItem.isObrigatorio()) {
            this.item.addStyleName("red");
        }
        this.item.addValueChangeListener(evt->valueChangeListener(evt));
    }

    private void valueChangeListener(HasValue.ValueChangeEvent<Boolean> evt) {
        this.checklistItem.setCheckado(this.item.getValue());
    }

    public ChecklistItem getChecklistItem() {
        return checklistItem;
    }
    public boolean isChecked(){
        return this.item.getValue();
    }
}
