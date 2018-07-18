import jaba.applet.AppWindow;
import jaba.applet.Applet;
import syi.awt.Awt;

public class MainSpaint {
    public static void main(String[] args) {
        jaba.applet.Applet.setupDesktop("shipainter.ini", "ShiPainter");
        Applet client = new c.ShiPainter();
        AppWindow frame = new AppWindow();

        Awt.setDesktop();
        frame.add(client);
        client.init();
        client.start();
    }
}
