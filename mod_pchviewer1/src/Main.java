import jaba.applet.Applet;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        jaba.applet.Applet.d_setupDesktop("pchviewer.ini", "pchviewer_v1");
        Applet client = new pch.PCHViewer();
        Frame frame = new Frame();

        client.d_setDesktop(frame);
        client.init();
        client.start();
    }
}