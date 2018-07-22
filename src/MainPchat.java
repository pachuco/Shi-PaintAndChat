import jaba.applet.Applet;

import java.awt.*;

public class MainPchat {
    public static void main(String[] args) {
        jaba.applet.Applet.d_setupDesktop("pchat366_client.ini", "PaintChat_Client");
        Applet client = new paintchat_client.Client();
        Frame frame = new Frame();
        client.d_setDesktop(frame);
        client.init();
        client.start();
    }
}
