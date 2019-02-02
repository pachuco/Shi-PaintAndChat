package jaba.applet;

import com.sun.media.sound.*;
import syi.util.cust.*;

import java.applet.AppletContext;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;
import javax.imageio.ImageIO;

public class Applet extends java.applet.Applet implements WindowListener {
    private Applet ctx;
    private IniMap inifile = null;
    private Frame frame;

    private static String iniPath;
    private static String iniSection;
    public static boolean isDesktop = false;

    public Applet() {
        ctx = this;
        if (isDesktop) {
            try {
                inifile = new IniMap(new RFile(RFile.F_FS, iniPath).getInputStream(), iniSection, IniMap.ACC_RW);
            } catch (IOException ex) {
                System.err.println("Cannot load applet ini!");
                System.err.println(iniPath);
            }
            frame = new Frame();
            frame.add(this);
            frame.addWindowListener(this);
            frame.setSize(800, 600);
            frame.setVisible(true);
        }
    }

    public boolean d_isDesktop() {
        return frame != null;
    }

    public Frame d_getFrame() {
        return frame;
    }

    public static void d_setupDesktop(String iniPth, String iniSect) {
        iniPath = iniPth;
        iniSection = iniSect;
        isDesktop = true;
    }

    @Override
    public String getParameter(String name) {
        if (!isDesktop) return super.getParameter(name);

        return inifile==null ? null : inifile.get(name);
    }

    @Override
    public Image getImage(URL url) {
        if (!isDesktop) return super.getImage(url);

        try {
            return ImageIO.read(url);
        } catch (IOException ex) {
            return null;
        }
    }

    @Override
    public Image getImage(URL url, String name) {
        if (!isDesktop) return super.getImage(url, name);

        try {
            return getImage(new URL(url, name));
        } catch (MalformedURLException ex) {
            return null;
        }
    }

    @Override
    public URL getDocumentBase() {
        if (!isDesktop) return super.getDocumentBase();

        return getCodeBase();
    }

    @Override
    public URL getCodeBase() {
        if (!isDesktop) return super.getCodeBase();

        String base = "";
        try {
            String protocol = Applet.class.getResource("").getProtocol();
            if(protocol.equals("jar")){
                base = new File(Applet.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent();
            } else if(protocol.equals("file")) {
                base = System.getProperty("user.dir");
            }
            return new File(base).toURI().toURL();
        } catch (MalformedURLException e) {
            return null;
        }
    }

    @Override
    public void showStatus(String msg) {
        if (!isDesktop) super.showStatus(msg);

        if (frame != null) frame.setTitle(msg);
    }

    @Override
    public AudioClip getAudioClip(URL url) {
        if (!isDesktop) return super.getAudioClip(url);

        try {
            return new JavaSoundAudioClip(url.openStream());
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public AudioClip getAudioClip(URL url, String name) {
        if (!isDesktop) return super.getAudioClip(url, name);

        try {
            return new JavaSoundAudioClip(new URL(url, name).openStream());
        } catch (IOException ex) {
            return null;
        }
    }

    //@Override
    //public AudioClip newAudioClip(URL url) {
    //    if (!d_isDesktop) return super.newAudioClip(url);
    //    return new AppletAudioClip(url);
    //}

    @Override
    public void play(URL url) {
        if (!isDesktop) super.play(url);
        AudioClip clip = getAudioClip(url);
        if (clip != null) clip.play();
    }

    @Override
    public void play(URL url, String name) {
        if (!isDesktop) super.play(url, name);
        AudioClip clip = getAudioClip(url, name);
        if (clip != null) clip.play();
    }

    @Override
    public Locale getLocale() {
        if (!isDesktop) return super.getLocale();
        return Locale.getDefault();
    }

    @Override
    public AppletContext getAppletContext() {
        if (!isDesktop) return super.getAppletContext();

        return new AppletContext() {
            public AudioClip getAudioClip(URL url) {
                return ctx.getAudioClip(url);
            }

            public Image getImage(URL url) {
                return ctx.getImage(url);
            }

            public java.applet.Applet getApplet(String name) {
                return ctx;
            }

            public Enumeration<java.applet.Applet> getApplets() {
                return new Enumeration<java.applet.Applet>() {
                    private int i = 0;

                    public boolean hasMoreElements() {
                        return i < 1;
                    }

                    public java.applet.Applet nextElement() {
                        if (hasMoreElements()) {
                            i++;
                            return ctx;
                        } else {
                            throw new NoSuchElementException();
                        }
                    }
                };
            }

            public void showDocument(URL url) {
                showDocument(url, null);
            }

            public void showDocument(URL url, String target) {
                try {
                    Desktop.getDesktop().browse(url.toURI());
                } catch (IOException ex) {
                    System.err.println("Cannot showDocument(): " + url.toString());
                } catch (URISyntaxException ex) {
                    System.err.println("Bad URI in showDocument(): " + url.toString());
                }
            }

            public void showStatus(String status) {
                ctx.showStatus(status);
            }

            public void setStream(String key, InputStream stream) throws IOException {

            }

            public InputStream getStream(String key) {
                return null;
            }

            public Iterator<String> getStreamKeys() {
                return null;
            }

        };
    }

    @Override
    public void setSize(Dimension d) {
        setSize(d.width, d.height);
    }

    @Override
    public void setSize(int width, int height) {
        if (!isDesktop) super.setSize(width, height);

        //frame.setSize(width, height);
        super.setSize(width, height);
    }

    //for overriding
    @Override public void init() { }
    @Override public void start() { }
    @Override public void stop() { }
    @Override public void destroy() { }

    private void exit() {
        frame.dispose();
        System.exit(0);
    }

    //WindowListener
    public void windowClosing(WindowEvent e) {
        exit();
    }

    public void windowOpened(WindowEvent e) { }
    public void windowClosed(WindowEvent e) { }
    public void windowIconified(WindowEvent e) { }
    public void windowDeiconified(WindowEvent e) { }
    public void windowActivated(WindowEvent e) { }
    public void windowDeactivated(WindowEvent e) { }
}
