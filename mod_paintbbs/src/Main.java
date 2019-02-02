import jaba.applet.Applet;
import res.*;

public class Main {
    public static void main(String[] args) {
        new ResPaintBBS();
        jaba.applet.Applet.d_setupDesktop("paintbbs.ini", "paintbbs");
        Applet client = new pbbs.PaintBBS();
        client.init();
        client.start();
    }
}