import jaba.applet.AppWindow;
import jaba.applet.Applet;

public class MainSpaint {
    public static void main(String[] args) {
        jaba.applet.Applet.setupDesktop("shipainter.ini", "ShiPainter");
        Applet client = new c.ShiPainter();
        AppWindow frame = new AppWindow();

        frame.add(client);
        client.init();
        client.start();
    }
}
