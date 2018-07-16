import c.ShiPainter;
import paintchat_client.Client;

public class Main {

    private ShiPainter sp;
    private Client pc;

    public Main() {
        sp = new ShiPainter();
        sp.init();
        sp.start();
        pc = new Client();
        pc.init();
        pc.start();
    }

    public static void main(String[] args) {
        new Main();
    }
}