package paintchat_client;

import jaba.applet.Applet;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import syi.awt.Awt;
import syi.util.ThreadPool;

public class Client extends Applet implements WindowListener {
    private Pl pl;
    private Frame frame;

    public void destroy() {
        if (this.pl != null) {
            this.pl.destroy();
        }

    }

    public void init() {
        if(d_isDesktop()) {
            frame = d_getFrame();
            frame.addWindowListener(this);
            frame.setBackground(Awt.cC);
            frame.setForeground(Awt.cFore);
        }
        try {
            this.setLayout(new BorderLayout());
            this.pl = new Pl(this);
            this.add((Component) this.pl, (Object) "Center");
            this.validate();
            ThreadPool.poolStartThread(this.pl, 'i');
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

    }

    public void exit() {
        frame.dispose();
        System.exit(0);
    }

    //WindowListener
    public void windowClosing(WindowEvent e) {
        exit();
    }

    public void windowOpened(WindowEvent e) { }
    public void windowClosed(WindowEvent e) { }
    public void windowIconified(WindowEvent e) { }
    public void windowDeiconified(WindowEvent e) { }
    public void windowActivated(WindowEvent e) { }
    public void windowDeactivated(WindowEvent e) { }
}
