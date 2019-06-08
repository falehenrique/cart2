package br.com.exmart.rtdpjlite.model;

import javax.persistence.*;

@Entity
@Table(name = "checklist_protocolo", schema = "rtdpj")
public class ChecklistProtocolo extends  AbstractEntity{
    @OneToOne(fetch= FetchType.EAGER)
    @JoinColumn(name="checklist_item_id")
    private ChecklistItem checklistItem;
    @Column(name = "protocolo_id")
    private Long protocoloId;

    public ChecklistProtocolo(ChecklistItem checklistItem, Long protocoloId) {
        this.checklistItem = checklistItem;
        this.protocoloId =protocoloId;
    }

    public ChecklistProtocolo() {
    }

    public ChecklistItem getChecklistItem() {
        return checklistItem;
    }

    public void setChecklistItem(ChecklistItem checklistItem) {
        this.checklistItem = checklistItem;
    }

    public Long getProtocoloId() {
        return protocoloId;
    }

    public void setProtocoloId(Long protocoloId) {
        this.protocoloId = protocoloId;
    }
}
