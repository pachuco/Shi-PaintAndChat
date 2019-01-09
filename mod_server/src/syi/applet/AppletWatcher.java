package syi.applet;

import jaba.applet.Applet;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Hashtable;

import paintchat.Config;
import syi.awt.Awt;
import syi.awt.Gui;
import syi.util.PProperties;

public class AppletWatcher extends Frame {
    private Applet applet;
    private boolean bool_exit;

    public AppletWatcher(String var1, String var2, Config var3, Hashtable var4, boolean var5) throws ClassNotFoundException, Exception, IOException {
        super(var2);
        this.enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        this.bool_exit = var5;
        this.setLayout(new BorderLayout());
        this.applet = (Applet) Class.forName(var1).newInstance();
        this.applet.setStub(ServerStub.getDefaultStub(var3, var4));
        this.add((Component) this.applet, (Object) "Center");
        this.applet.init();
        Gui.getDefSize(this);
        Awt.moveCenter(this);
    }

    private PProperties getProperties(URL var1) {
        try {
            boolean var2 = false;
            URLConnection var3 = var1.openConnection();
            var3.connect();
            BufferedInputStream var4 = new BufferedInputStream(var3.getInputStream());
            int var5 = var3.getContentLength();
            int var6 = 0;

            char[] var7;
            int var8;
            for (var7 = new char[var5]; var6 < var5; ++var6) {
                var8 = var4.read();
                switch (var8) {
                    case -1:
                        var7[var6] = 0;
                        var5 = 0;
                        break;
                    case 9:
                    case 10:
                    case 13:
                    case 32:
                        if (!var2) {
                            var2 = true;
                            var7[var6] = (char) var8;
                        }
                        break;
                    default:
                        var2 = false;
                        var7[var6] = (char) var8;
                }
            }

            var4.close();
            var4 = null;
            var5 = var6;
            var2 = false;
            var8 = 0;
            int var9 = 0;
            char[] var10 = new char[]{'p', 'a', 'r', 'a', 'm'};
            char[] var11 = new char[]{'a', 'p', 'p', 'l', 'e', 't'};

            for (var6 = 0; var6 < var5; ++var6) {
                if (var7[var6] == '<') {
                    ++var6;

                    for (; var6 < var5; ++var6) {
                        if (var7[var6] == ' ') {
                            if (var2) {
                                break;
                            }
                        } else {
                            var2 = true;
                            char var12 = Character.toLowerCase(var7[var6]);
                            var8 = var10[var8] == var12 ? var8 + 1 : 0;
                            var9 = var11[var8] == var12 ? var9 + 1 : 0;
                        }
                    }
                }
            }
        } catch (Throwable var13) {
            var13.printStackTrace();
        }

        return null;
    }

    public static void main(String[] var0) {
    }

    protected void processWindowEvent(WindowEvent event) {
        try {
            int eventID = event.getID();
            Window window = event.getWindow();
            switch (eventID) {
                case WindowEvent.WINDOW_OPENED:
                    this.applet.start();
                    break;
                case WindowEvent.WINDOW_CLOSING:
                    window.dispose();
                    this.applet.stop();
                    break;
                case WindowEvent.WINDOW_CLOSED:
                    this.applet.destroy();
                    this.applet = null;
                    if (this.bool_exit) {
                        System.exit(0);
                    }
            }
        } catch (Throwable ex) {
        }

    }
}
