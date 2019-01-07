package paintchat_frame;

import jaba.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Window;
import java.awt.event.WindowEvent;

import static java.awt.event.ComponentEvent.*;

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
        this.enableEvents(WINDOW_EVENT_MASK);
        this.setLayout(new BorderLayout());
        this.add((Component) this.applet, (Object) "Center");
        this.applet.init();
        this.pack();
        Awt.moveCenter(this);
        this.setVisible(true);
        this.applet.start();
    }

    protected void processWindowEvent(WindowEvent var1) {
        Window var2;
        switch (var1.getID()) {
            case 200:
            default:
                break;
            case 201:
                this.applet.stop();
                var2 = var1.getWindow();
                var2.dispose();
                var2.removeAll();
                break;
            case 202:
                this.applet.destroy();
        }

        if (var1.getID() == 201) {
            var2 = var1.getWindow();
            var2.dispose();
            var2.removeAll();
        }

    }
}
