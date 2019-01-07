package pch2;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Panel;
import java.awt.event.ComponentEvent;

import static java.awt.event.ComponentEvent.*;

public class PCHPanel extends Panel {
    public PCHPanel() {
        super((LayoutManager) null);
        this.enableEvents(COMPONENT_EVENT_MASK);
    }

    protected void processComponentEvent(ComponentEvent var1) {
        switch (var1.getID()) {
            case COMPONENT_RESIZED:
                this.setVisible(false);
                ((PCHCanvas) this.getComponent(0)).moveCenter();
                this.setVisible(true);
            default:
        }
    }

    public Dimension getPreferredSize() {
        Component var1 = this.getComponent(0);
        return var1 == null ? new Dimension(300, 300) : var1.getSize();
    }
}
