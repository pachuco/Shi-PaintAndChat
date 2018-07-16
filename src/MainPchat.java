import jaba.applet.AppWindow;
import jaba.applet.Applet;

public class MainPchat {
    public static void main(String[] args) {
        jaba.applet.Applet.setupDesktop("pchat366_client.ini", "PaintChat_Client");
        Applet client = new paintchat_client.Client();
        AppWindow frame = new AppWindow();

        frame.add(client);
        client.init();
        client.start();
    }
}
