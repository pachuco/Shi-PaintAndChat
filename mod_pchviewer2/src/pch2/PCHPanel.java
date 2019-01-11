package pch2;

import java.awt.*;
import java.awt.event.ComponentEvent;

public class PCHPanel extends Panel {
    public PCHPanel() {
        super((LayoutManager) null);
        this.enableEvents(AWTEvent.COMPONENT_EVENT_MASK);
    }

    protected void processComponentEvent(ComponentEvent event) {
        switch (event.getID()) {
            case ComponentEvent.COMPONENT_RESIZED:
                this.setVisible(false);
                ((PCHCanvas) this.getComponent(0)).moveCenter();
                this.setVisible(true);
            default:
        }
    }

    public Dimension getPreferredSize() {
        Component comp = this.getComponent(0);
        return comp == null ? new Dimension(300, 300) : comp.getSize();
    }
}
