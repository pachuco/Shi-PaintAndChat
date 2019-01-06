package paintchat_frame;

import java.applet.Applet;
import java.awt.Color;
import java.awt.TextField;
import java.io.CharArrayWriter;

import paintchat.debug.Debug;
import paintchat.debug.DebugListener;
import syi.awt.TextPanel;
import syi.util.ThreadPool;

public class Console extends TextPanel implements Runnable, DebugListener {
    private Debug debug;
    private CharArrayWriter cOut;
    private Thread thread;
    private boolean isRun;

    public Console() {
        this((Applet) null, 400, Color.white, Color.black, (TextField) null);
    }

    public Console(Applet var1, int var2, Color var3, Color var4, TextField var5) {
        super(var1, var2, var3, var4, var5);
        this.debug = null;
        this.cOut = null;
        this.thread = null;
    }

    public void run() {
        try {
            for (Thread var1 = Thread.currentThread(); this.isRun; Thread.sleep(2500L)) {
                if (this.cOut.size() > 0) {
                    synchronized (this.cOut) {
                        this.setText(this.cOut.toString());
                        this.cOut.reset();
                    }
                }
            }
        } catch (Throwable var4) {
        }

        this.stop();
    }

    public synchronized void start(Debug var1) {
        this.debug = var1;
        if (this.cOut == null) {
            this.cOut = new CharArrayWriter();
        }

        this.debug.setListener(this);
        this.isRun = true;
        this.thread = ThreadPool.poolStartThread(this, "c");
    }

    public synchronized void stop() {
        if (this.isRun) {
            this.isRun = false;
            this.debug.setListener((DebugListener) null);
            if (this.thread != null && Thread.currentThread() != this.thread) {
                this.thread.interrupt();
                this.thread = null;
            }

        }
    }

    public void log(Object var1) {
        this.addText(var1.toString());
    }

    public void logDebug(Object var1) {
        this.addText(var1.toString());
    }
}
