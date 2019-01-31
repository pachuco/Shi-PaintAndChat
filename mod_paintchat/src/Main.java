import jaba.applet.Applet;
import res.*;

public class Main {
    public static void main(String[] args) {
        new ResShiClient();
        jaba.applet.Applet.d_setupDesktop("pchat366_client.ini", "PaintChat_Client");
        Applet client = new paintchat_client.Client();
        client.init();
        client.start();
    }
}
