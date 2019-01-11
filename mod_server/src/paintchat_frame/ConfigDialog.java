package paintchat_frame;

import jaba.applet.Applet;

import java.awt.*;
import java.awt.event.WindowEvent;

import paintchat.Config;
import paintchat.Res;
import syi.applet.ServerStub;
import syi.awt.Awt;

public class ConfigDialog extends Dialog {
    private Applet applet;

    public ConfigDialog(String var1, String var2, Config var3, Res var4, String var5) throws Exception {
        super(Awt.getPFrame());
        this.setModal(true);
        this.applet = (Applet) Class.forName(var1).newInstance();
        this.applet.setStub(ServerStub.getDefaultStub(var3, var4));
        this.enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        this.setLayout(new BorderLayout());
        this.add((Component) this.applet, (Object) "Center");
        this.applet.init();
        this.pack();
        Awt.moveCenter(this);
        this.setVisible(true);
        this.applet.start();
    }

    protected void processWindowEvent(WindowEvent event) {
        Window window;
        switch (event.getID()) {
            case WindowEvent.WINDOW_OPENED:
            default:
                break;
            case WindowEvent.WINDOW_CLOSING:
                this.applet.stop();
                window = event.getWindow();
                window.dispose();
                window.removeAll();
                break;
            case WindowEvent.WINDOW_CLOSED:
                this.applet.destroy();
        }

        if (event.getID() == WindowEvent.WINDOW_CLOSING) {
            window = event.getWindow();
            window.dispose();
            window.removeAll();
        }

    }
}
