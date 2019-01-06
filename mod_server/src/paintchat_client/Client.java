package paintchat_client;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Component;

import syi.util.ThreadPool;

public class Client extends Applet {
    private Pl pl;

    public void destroy() {
        if (this.pl != null) {
            this.pl.destroy();
        }

    }

    public void init() {
        try {
            this.setLayout(new BorderLayout());
            this.pl = new Pl(this);
            this.add((Component) this.pl, (Object) "Center");
            this.validate();
            ThreadPool.poolStartThread(this.pl, 'i');
        } catch (Throwable var2) {
            var2.printStackTrace();
        }

    }
}
