import jaba.applet.Applet;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        jaba.applet.Applet.d_setupDesktop("paintbbs.ini", "paintbbs");
        Applet client = new pbbs.PaintBBS();
        Frame frame = new Frame();

        client.d_setDesktop(frame);
        client.init();
        client.start();
    }
}