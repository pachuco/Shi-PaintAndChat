import syi.awt.AppWindow;
import jaba.applet.Applet;

public class MainPchat {
    public static void main(String[] args) {
        jaba.applet.Applet.d_setupDesktop("pchat366_client.ini", "PaintChat_Client");
        Applet client = new paintchat_client.Client();
        AppWindow frame = new AppWindow();
        client.d_setDesktop(frame);
        client.init();
        client.start();
    }
}
