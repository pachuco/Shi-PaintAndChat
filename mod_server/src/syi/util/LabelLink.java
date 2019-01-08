package syi.util;

import jaba.applet.Applet;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.net.URL;

public class LabelLink extends Label {
    private String strLink = null;
    private boolean isMouse = false;
    private Color clLink;
    private Color clBack;
    private Applet applet;

    public LabelLink() {
    }

    public LabelLink(String var1) {
        super(var1);
    }

    public LabelLink(String var1, int var2) {
        super(var1, var2);
    }

    private void init() {
        if (!this.isMouse) {
            this.isMouse = true;
            this.enableEvents(AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);
            this.clBack = this.getForeground();
        }
    }

    protected void processMouseEvent(MouseEvent var1) {
        this.processMouseMotionEvent(var1);
    }

    protected void processMouseMotionEvent(MouseEvent var1) {
        if (this.isMouse) {
            try {
                switch (var1.getID()) {
                    case MouseEvent.MOUSE_PRESSED:
                        this.applet.getAppletContext().showDocument(new URL(this.strLink), "top");
                    case MouseEvent.MOUSE_RELEASED:
                    case MouseEvent.MOUSE_MOVED:
                    default:
                        break;
                    case MouseEvent.MOUSE_ENTERED:
                        this.setFont(true);
                        break;
                    case MouseEvent.MOUSE_EXITED:
                        this.setFont(false);
                }
            } catch (Throwable var3) {
                var3.printStackTrace();
            }

        }
    }

    public void setFont(boolean var1) {
        Font var2 = this.getFont();
        var2 = new Font(var2.getName(), var1 ? 2 : 0, var2.getSize());
        this.setFont(var2);
        this.setForeground(var1 ? this.clLink : this.clBack);
    }

    public void setLink(Applet var1, String var2, Color var3) {
        if (var2 != null && var2.length() > 0) {
            this.init();
            this.strLink = var2;
            this.clLink = var3;
            this.applet = var1;
        }
    }
}
