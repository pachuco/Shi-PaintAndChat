import jaba.applet.Applet;

public class Main {
    public static void main(String[] args) {
        jaba.applet.Applet.d_setupDesktop("pchviewer.ini", "pchviewer_v1");
        Applet client = new pch.PCHViewer();
        client.init();
        client.start();
    }
}