package syi.awt;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class Awt {
    public static Frame main_frame = null;
    public static Color cC;
    public static Color cDk;
    public static Color cLt;
    public static Color cBk;
    public static Color cFore;
    public static Color cF;
    public static Color cFSel;
    public static Color clBar;
    public static Color clLBar;
    public static Color clBarT;
    private static Font fontDef = null;
    private static float Q = 0.0F;
    private static MediaTracker mt = null;

    public static final void drawFrame(Graphics g, boolean var1, int var2, int var3, int var4, int var5) {
        setup();
        drawFrame(g, var1, var2, var3, var4, var5, cDk, cLt);
    }

    public static final void drawFrame(Graphics g, boolean var1, int xTopLeft, int yTopLeft, int width, int height, Color colorDark, Color colorLight) {
        setup();
        int var8 = xTopLeft + width;
        int var9 = yTopLeft + height;
        g.setColor(colorDark == null ? cDk : colorDark);
        g.fillRect(xTopLeft, yTopLeft, width, 1);
        g.fillRect(xTopLeft, yTopLeft + 1, 1, height - 2);
        g.fillRect(xTopLeft + 2, var9 - 2, width - 2, 1);
        g.fillRect(var8 - 1, yTopLeft + 2, 1, height - 4);
        g.setColor(colorLight == null ? cLt : colorLight);
        if (!var1) {
            g.fillRect(xTopLeft + 1, yTopLeft + 1, width - 2, 1);
            g.fillRect(xTopLeft + 1, yTopLeft + 2, 1, height - 4);
        }

        g.fillRect(xTopLeft + 1, var9 - 1, width - 1, 1);
        g.fillRect(var8, yTopLeft + 1, 1, height - 2);
    }

    public static final void fillFrame(Graphics g, boolean var1, int var2, int var3, int var4, int var5) {
        fillFrame(g, var1, var2, var3, var4, var5, cC, cDk, cDk, cLt);
    }

    public static final void fillFrame(Graphics g, boolean var1, int var2, int var3, int var4, int var5, Color var6, Color var7, Color var8, Color var9) {
        drawFrame(g, var1, var2, var3, var4, var5, var8, var9);
        g.setColor(var1 ? (var7 == null ? cDk : var7) : (var6 == null ? cC : var6));
        g.fillRect(var2 + 2, var3 + 2, var4 - 3, var5 - 4);
    }

    public static void getDef(Component var0) {
        setup();
        var0.setBackground(cBk);
        var0.setForeground(cFore);
        var0.setFont(getDefFont());
        if (var0 instanceof LComponent) {
            LComponent var1 = (LComponent) var0;
            var1.clBar = clBar;
            var1.clLBar = clLBar;
            var1.clBarT = clBarT;
            var1.clFrame = cF;
        }

    }

    public static Font getDefFont() {
        if (fontDef == null) {
            fontDef = new Font("sansserif", 0, (int) (15.0F * q()));
        }

        return fontDef;
    }

    public static Component getParent(Component var0) {
        Container var1 = var0.getParent();
        return (Component) (var1 == null ? var0 : (var1 instanceof Window ? var1 : getParent(var1)));
    }

    public static Frame getPFrame() {
        if (main_frame == null) {
            main_frame = new Frame();
        }

        return main_frame;
    }

    public static boolean isR(MouseEvent event) {
        return event.isAltDown() || event.isControlDown() || (event.getModifiers() & 4) != 0;
    }

    public static boolean isWin() {
        String var0 = "Win";
        return System.getProperty("os.name", var0).startsWith(var0);
    }

    public static void moveCenter(Window var0) {
        Dimension var1 = var0.getToolkit().getScreenSize();
        Dimension var2 = var0.getSize();
        var0.setLocation(var1.width / 2 - var2.width / 2, var1.height / 2 - var2.height / 2);
    }

    public static InputStream openStream(URL var0) throws IOException {
        URLConnection var1 = var0.openConnection();
        var1.setUseCaches(true);
        return var1.getInputStream();
    }

    public static float q() {
        if (Q == 0.0F) {
            Dimension var0 = Toolkit.getDefaultToolkit().getScreenSize();
            short var1 = 2264;
            int var2 = var0.width + var0.height;
            Q = Math.min(1.0F + (float) (var2 - var1) / (float) var1 / 2.0F, 2.0F);
        }

        return Q;
    }

    public static String replaceText(String var0, String var1, String var2) {
        if (var0.indexOf(var2) < 0) {
            return var0;
        } else {
            StringBuffer var3 = new StringBuffer();

            try {
                char[] var4 = var2.toCharArray();
                if (var4.length <= 0) {
                    return var0;
                }

                int var5 = 0;
                int var6 = 0;
                int var8 = var0.length();

                for (int var9 = 0; var9 < var8; ++var9) {
                    char var7;
                    if ((var7 = var0.charAt(var9)) == var4[var6]) {
                        if (var6 == 0) {
                            var5 = var9;
                        }

                        ++var6;
                        if (var6 >= var4.length) {
                            var6 = 0;
                            var3.append(var1);
                        }
                    } else {
                        if (var6 > 0) {
                            for (int var10 = 0; var10 < var6; ++var10) {
                                var3.append(var0.charAt(var5 + var10));
                            }

                            var6 = 0;
                        }

                        var3.append(var7);
                    }
                }
            } catch (RuntimeException var11) {
                System.out.println("replace" + var11);
            }

            return var3.toString();
        }
    }

    public static void setDef(Component var0, boolean var1) {
        try {
            if (!var1) {
                var1 = true;
            } else {
                Container var2 = var0.getParent();
                var2.setFont(var2.getFont());
                var2.setForeground(var2.getForeground());
                var2.setBackground(var2.getBackground());
            }

            if (var0 instanceof Container) {
                Component[] var5 = ((Container) var0).getComponents();
                if (var5 != null) {
                    for (int var3 = 0; var3 < var5.length; ++var3) {
                        var0 = var5[var3];
                        setDef(var0, true);
                    }
                }
            }
        } catch (Throwable var4) {
            var4.printStackTrace();
        }

    }

    public static void setPFrame(Frame var0) {
        main_frame = var0;
    }

    public static final void setup() {
        if (cC == null) {
            cC = new Color(13487565);
            cLt = null;
            cDk = null;
        }

        if (cDk == null) {
            cDk = cC.darker();
        }

        if (cLt == null) {
            cLt = cC.brighter();
        }

        if (cBk == null) {
            cBk = new Color(13619199);
        }

        if (cFore == null) {
            cFore = new Color(5263480);
        }

        if (cF == null) {
            cF = cFore;
        }

        if (cFSel == null) {
            cFSel = new Color(15610675);
        }

        if (clBar == null) {
            clBar = new Color(6711039);
        }

        if (clLBar == null) {
            clLBar = new Color(8947967);
        }

        if (clBarT == null) {
            clBarT = Color.white;
        }

    }

    public static Image toMin(Image var0, int var1, int var2) {
        Image var3 = var0.getScaledInstance(var1, var2, 16);
        var0.flush();
        wait(var3);
        return var3;
    }

    public static String trimString(String var0, String var1, String var2) {
        if (var0 != null && var0.length() > 0 && var1 != null && var2 != null) {
            try {
                int var3;
                if ((var3 = var0.indexOf(var1)) == -1) {
                    return "";
                } else {
                    int var4;
                    if ((var4 = var0.indexOf(var2, var3 + var1.length())) == -1) {
                        var4 = var0.length() - 1;
                    }

                    return var0.substring(var3 + var1.length(), var4);
                }
            } catch (RuntimeException var6) {
                System.out.println("t_trimString:" + var6.toString());
                return "";
            }
        } else {
            return "";
        }
    }

    public static int[] getPix(Image var0) {
        try {
            PixelGrabber var1 = new PixelGrabber(var0, 0, 0, var0.getWidth((ImageObserver) null), var0.getHeight((ImageObserver) null), true);
            var1.grabPixels();
            return (int[]) var1.getPixels();
        } catch (Throwable var2) {
            var2.printStackTrace();
            return null;
        }
    }

    public static void wait(Image var0) {
        if (mt == null) {
            mt = new MediaTracker(getPFrame());
        }

        try {
            mt.addImage(var0, 0);
            mt.waitForID(0);
        } catch (InterruptedException var1) {
            ;
        }

        mt.removeImage(var0, 0);
    }
}
