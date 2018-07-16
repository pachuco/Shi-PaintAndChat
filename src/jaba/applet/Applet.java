package jaba.applet;

import java.applet.AppletContext;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import javax.imageio.ImageIO;

public class Applet extends java.applet.Applet {
    private java.applet.Applet japplet;
    private IniFile inifile = null;
    //private AppWindow frame;

    private static String iniPath;
    private static String iniSection;
    public static boolean isDesktop = false;

    private static String applicationPath;

    public Applet() {
        inifile = new IniFile();
        if (isDesktop) {
            try {
                inifile.openIni(openResFile(iniPath));
            } catch (Exception e) {
                System.out.println("Cannot load applet ini!");
                System.out.println(e.getMessage());
            }
            //init();
            //start();
        }
    }

    private static String getAppPath(){
        String path;
        File fr;

        if(applicationPath == null) {
            path = Applet.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            fr = new File(path);
            if(fr.isDirectory()) {
                applicationPath = fr.getPath();
            }else {
                applicationPath = fr.getParent();
            }
        }
        return applicationPath;
    }

    private static File openResFile(String filePath) {
        String appPath = getAppPath();
        return new File(appPath, filePath);
    }

    public static void setupDesktop(String iniPth, String iniSect) {
        iniPath = iniPth;
        iniSection = iniSect;
        isDesktop = true;
    }

    @Override
    public String getParameter(String name){
        if (!isDesktop) return super.getParameter(name);
        if (!inifile.isIniLoaded()) return null;

        return inifile.getParameter(iniSection, name, null);
    }

    @Override
    public Image getImage(URL url) {
        if (!isDesktop) return super.getImage(url);

        try {
            return ImageIO.read(url);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public Image getImage(URL url, String name) {
        if (!isDesktop) return super.getImage(url, name);

        try {
            return getImage(new URL(url, name));
        } catch (MalformedURLException e) {
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

        URL url = null;
        url = getClass().getProtectionDomain().getCodeSource().getLocation();
        try {
            url = new URL("file://");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    @Override
    public void showStatus(String msg) {
        if (!isDesktop) super.showStatus(msg);

        //frame.setTitle(msg);
    }

    @Override
    public AudioClip getAudioClip(URL url) {
        if (!isDesktop) return super.getAudioClip(url);

        return new AppletAudioClip(url);
    }

    @Override
    public AudioClip getAudioClip(URL url, String name) {
        if (!isDesktop) return super.getAudioClip(url, name);

        try {
            return new AppletAudioClip(new URL(url, name));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //@Override
    //public AudioClip newAudioClip(URL url) {
    //    if (!isDesktop) return super.newAudioClip(url);
    //    return new AppletAudioClip(url);
    //}

    @Override
    public void play(URL url) {
        if (!isDesktop) super.play(url);
        AudioClip clip = getAudioClip(url);
        if(clip != null) clip.play();
    }

    @Override
    public void play(URL url, String name) {
        if (!isDesktop) super.play(url, name);
        AudioClip clip = getAudioClip(url, name);
        if(clip != null) clip.play();
    }

    @Override
    public Locale getLocale() {
        if (!isDesktop) return super.getLocale();
        return Locale.getDefault();
    }

    @Override
    public AppletContext getAppletContext() {
        if (!isDesktop) return super.getAppletContext();

        AppletContext ac = new AppletContext() {
            public AudioClip getAudioClip(URL url) {
                return null;
            }

            public Image getImage(URL url) {
                return null;
            }

            public java.applet.Applet getApplet(String name) {
                return null;
            }

            public Enumeration<java.applet.Applet> getApplets() {
                return null;
            }

            public void showDocument(URL url) {

            }

            public void showDocument(URL url, String target) {

            }

            public void showStatus(String status) {

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
        return ac;

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
    @Override public void init(){ }
    @Override public void start(){ }
    @Override public void stop(){ }
    @Override public void destroy(){ }
}
