import syi.awt.AppWindow;
import jaba.applet.Applet;

public class MainSpaint {
    public static void main(String[] args) {
        jaba.applet.Applet.d_setupDesktop("shipainter.ini", "ShiPainter");
        Applet client = new c.ShiPainter();
        AppWindow frame = new AppWindow();

        client.d_setDesktop(frame);
        client.init();
        client.start();
    }
}
