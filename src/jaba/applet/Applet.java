package jaba.applet;

import java.applet.AppletContext;
import java.applet.AudioClip;
import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;
import javax.imageio.ImageIO;

public class Applet extends java.applet.Applet {
    private Applet ctx;
    private IniFile inifile = null;
    private Frame frame;

    private static String iniPath;
    private static String iniSection;
    public static boolean isDesktop = false;

    private static String applicationPath;

    public Applet() {
        ctx = this;
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

    private static String getAppPath() {
        String path;
        File fr;

        if (applicationPath == null) {
            path = Applet.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            fr = new File(path);
            if (fr.isDirectory()) {
                applicationPath = fr.getPath();
            } else {
                applicationPath = fr.getParent();
            }
        }
        return applicationPath;
    }

    private static File openResFile(String filePath) {
        String appPath = getAppPath();
        return new File(appPath, filePath);
    }

    public void attachFrame(Frame f) {
        if(frame != null) detachFrame();
        f.add(this);
        frame = f;
    }

    public void detachFrame() {
        if(frame == null) return;
        frame.remove(this);
        frame = null;
    }

    public static void setupDesktop(String iniPth, String iniSect) {
        iniPath = iniPth;
        iniSection = iniSect;
        isDesktop = true;
    }

    @Override
    public String getParameter(String name) {
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

        if (frame != null) frame.setTitle(msg);
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

        AppletContext ac = new AppletContext() {
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

                    @Override
                    public boolean hasMoreElements() {
                        return i < 1;
                    }

                    @Override
                    public java.applet.Applet nextElement() {
                        if (hasMoreElements()) {
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
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
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
    @Override
    public void init() {
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }

    @Override
    public void destroy() {
    }
}
