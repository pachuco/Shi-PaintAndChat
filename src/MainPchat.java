import jaba.applet.AppWindow;
import jaba.applet.Applet;
import syi.awt.Awt;

public class MainPchat {
    public static void main(String[] args) {
        jaba.applet.Applet.setupDesktop("pchat366_client.ini", "PaintChat_Client");
        Applet client = new paintchat_client.Client();
        AppWindow frame = new AppWindow();

        Awt.setDesktop();
        frame.add(client);
        client.init();
        client.start();
    }
}
