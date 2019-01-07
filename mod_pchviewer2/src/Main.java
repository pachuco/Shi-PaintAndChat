import jaba.applet.Applet;

public class Main {
    public static void main(String[] args) {
        jaba.applet.Applet.d_setupDesktop("pchviewer.ini", "pchviewer_v2");
        Applet client = new pch2.PCHViewer();
        client.init();
        client.start();
    }
}