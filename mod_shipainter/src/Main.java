import config.*;
import jaba.applet.Applet;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        new ResShiClient();
        jaba.applet.Applet.d_setupDesktop("shipainter.ini", "ShiPainter");
        Applet client = new c.ShiPainter();
        client.init();
        client.start();
    }
}