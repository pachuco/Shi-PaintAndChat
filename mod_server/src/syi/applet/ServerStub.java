package syi.applet;

import jaba.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.applet.AudioClip;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import paintchat.Config;
import sun.applet.AppletAudioClip;
import syi.awt.Awt;
import syi.awt.Gui;
import syi.util.PProperties;

public class ServerStub implements AppletContext, AppletStub {
    private Config params;
    private Hashtable res;
    URL url_base;
    private static ServerStub default_stub = null;

    public Iterator getStreamKeys() {
        return null;
    }

    public ServerStub(Config var1, Hashtable var2) {
        this.params = var1;
        this.res = var2;

        try {
            String var3 = System.getProperty("user.dir");
            var3 = Awt.replaceText(var3, "/", "\\");
            if (!var3.endsWith("/")) {
                var3 = var3 + '/';
            }

            this.url_base = new URL("file:/" + var3);
        } catch (RuntimeException var4) {
        } catch (IOException var5) {
        }

    }

    public void appletResize(int var1, int var2) {
    }

    public Applet getApplet(String var1) {
        return null;
    }

    public AppletContext getAppletContext() {
        return this;
    }

    public Enumeration getApplets() {
        return null;
    }

    public AudioClip getAudioClip(URL var1) {
        try {
            return new AppletAudioClip(var1);
        } catch (Throwable var4) {
            System.out.println((Object) var4);

            try {
                return (AudioClip) var1.getContent();
            } catch (Exception var3) {
                return null;
            }
        }
    }

    public URL getCodeBase() {
        return this.url_base;
    }

    public static ServerStub getDefaultStub(Config var0, Hashtable var1) {
        if (default_stub == null) {
            default_stub = new ServerStub(var0, var1);
        }

        return default_stub;
    }

    public URL getDocumentBase() {
        return this.url_base;
    }

    public PProperties getHashTable() {
        return this.params;
    }

    public Image getImage(URL var1) {
        try {
            return (Image) var1.getContent();
        } catch (Exception var2) {
            return null;
        }
    }

    public String getParameter(String var1) {
        return (String) this.params.get(var1);
    }

    public boolean isActive() {
        return false;
    }

    public void showDocument(URL var1) {
        this.showDocument(var1, "");
    }

    public void showDocument(URL var1, String var2) {
        Gui.showDocument(var1.toExternalForm(), this.params, this.res);
    }

    public void showStatus(String var1) {
    }

    public InputStream getStream(String var1) {
        return null;
    }

    public void setStream(String var1, InputStream var2) {
    }
}
