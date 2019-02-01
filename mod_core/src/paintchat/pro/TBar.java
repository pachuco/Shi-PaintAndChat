package paintchat.pro;

import jaba.applet.Applet;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;
import java.net.URL;

import paintchat.Res;
import paintchat_client.Mi;
import syi.awt.Awt;
import syi.awt.LComponent;

import static res.ResShiClient.*;

public class TBar extends LComponent {
    private boolean isOption;
    private Tools tools;
    private Res config;
    private LComponent[] cs;
    private String[] strs;
    private boolean[] flags;
    private Mi mi;
    private URL codebase;
    private String strAuthor;
    private int W;
    private int H;
    private Image image = null;
    private Color[][] cls = new Color[2][3];
    private Color clT;

    public TBar(Tools var1, Res config, LComponent[] var4) {
        this.config = config;
        this.cs = var4;
        this.tools = var1;
        this.H = (int) (19.0F * LComponent.Q);
    }

    public final void drawFrame(Graphics g, boolean var2, int var3, int var4, int var5, int var6) {
        Color[] var7 = this.cls[var2 ? 1 : 0];
        Awt.drawFrame(g, var2, var3, var4, var5, var6, var7[2], var7[1]);
    }

    public final void fillFrame(Graphics g, boolean var2, int var3, int var4, int var5, int var6) {
        this.drawFrame(g, var2, var3, var4, var5, var6);
        Color[] var7 = this.cls[var2 ? 1 : 0];
        Awt.setup();
        Awt.fillFrame(g, var2, var3, var4, var5, var6, this.cls[0][0], this.cls[1][0], var7[2], var7[1]);
    }

    public void init() {
        super.isHide = false;
        int var1 = this.H;
        FontMetrics var3 = this.getFontMetrics(this.getFont());
        int var4 = this.cs.length - 1;
        int var5 = 0;
        this.strs = new String[var4];

        for (int var6 = 0; var6 < var4; ++var6) {
            String var2 = lang.get("window_" + var6);
            var5 = Math.max(var3.stringWidth(var2) + var2.length(), var5);
            this.strs[var6] = var2;
            this.cs[var6].setTitle(var2);
        }

        this.s();
        Color[] var7 = this.cls[0];
        this.cls[0] = this.cls[1];
        this.cls[1] = var7;
        this.W = var5;
        this.setDimension(new Dimension(this.W, var1), new Dimension(this.W, var1 * var4), new Dimension(this.W * var4, var1 * var4));
    }

    public void initOption(URL var1, Mi var2) {
        this.isOption = true;
        this.mi = var2;
        this.codebase = var1;
        byte var3 = 2;
        this.strs = new String[var3];
        this.flags = new boolean[var3];
        this.strAuthor = lang.get("option_author");
        this.s();
        this.setDimension((Dimension) null, new Dimension(10, 10), (Dimension) null);
    }

    public void mouseO(MouseEvent event) {
        if (event.getID() == MouseEvent.MOUSE_PRESSED) {
            int mouseY = event.getY();
            if (mouseY <= this.H) {
                Dimension var3 = new Dimension(this.mi.getSize());
                boolean var4 = !this.flags[0];
                this.mi.isGUI = var4;
                this.flags[0] = var4;
                var3.width += this.mi.getGapW();
                var3.height += this.mi.getGapH();
                this.repaint();
                this.mi.setVisible(false);
                this.mi.setDimension((Dimension) null, var3, new Dimension(this.getParent().getSize()));
                this.mi.setVisible(true);
                this.mi.resetGraphics();
                this.tools.pack();
            } else if (mouseY <= this.H * 2) {
                this.z();
                this.repaint();
            } else if (mouseY > this.H * 3) {
                try {
                    String var6 = this.config.getP("app_url", "http://shichan.jp/");
                    if ((var6.length() != 1 || var6.charAt(0) != '_') && this.mi.alert("kakunin_0", true)) {
                        Applet var7 = (Applet) this.getParent().getParent();
                        var7.getAppletContext().showDocument(new URL(var7.getCodeBase(), var6), "_blank");
                    }
                } catch (Throwable ex) {
                    ex.printStackTrace();
                }
            }

        }
    }

    public void paint2(Graphics g) {
        //this.getSize();
        if (this.isOption) {
            this.paintO(g);
        } else {
            this.paintBar(g);
        }

    }

    private void paintBar(Graphics g) {
        int var2 = 0;
        int var3 = 0;
        int var4 = 0;
        Dimension dim = this.getSize();
        int w = this.W;
        int h = this.H;

        for (int i = 0; i < this.cs.length; ++i) {
            if (this.cs[i] != null && this.cs[i] != this) {
                boolean var8 = this.cs[i].getParent() == null;
                this.fillFrame(g, var8, var2, var3, w, h);
                if (var8) {
                    g.setColor(Awt.cFore);
                    g.drawLine(var2 + 4, var3 + 4, var2 + w - 4, var3 + h - 4);
                }

                g.setColor(this.clT == null ? Awt.cFore : this.clT);
                g.drawString(this.strs[var4++], var2 + 2, var3 + h - 3);
                if ((var2 += w) + w > dim.width) {
                    var2 = 0;
                    var3 += h;
                }
            }
        }

    }

    private void paintO(Graphics g) {
        if (this.image == null) {
            try {
                FontMetrics var3 = this.getFontMetrics(this.getFont());
                int var4 = this.strs.length;
                int var5 = 0;

                for (int i = 0; i < var4; ++i) {
                    String var2 = lang.get("option_" + i);
                    var5 = Math.max(var3.stringWidth(var2 + " OFF") + var2.length() + 3, var5);
                    this.strs[i] = var2;
                }

                var5 = Math.max(var3.stringWidth(this.strAuthor) + this.strAuthor.length(), var5);
                this.image = this.getToolkit().createImage((byte[]) this.config.getRes("bn.gif"));
                Awt.wait(this.image);
                int var6 = this.image.getWidth(null);
                int var7 = this.image.getHeight(null);
                if (LComponent.Q > 1.0F) {
                    var6 = (int) ((float) var6 * LComponent.Q);
                    var7 = (int) ((float) var7 * LComponent.Q);
                }

                this.W = Math.max(var5, var6);
                this.H = var3.getHeight() + 2;
                Dimension var8 = new Dimension(this.W, this.H * (var4 + 2) + var7 + 2);
                this.setDimension(new Dimension(this.W, this.H), var8, var8);
            } catch (RuntimeException var11) {
                ;
            }
        }

        int imW = this.image.getWidth(null);
        int imH = this.image.getHeight(null);
        if (LComponent.Q > 1.0F) {
            imW = (int) ((float) imW * LComponent.Q);
            imH = (int) ((float) imH * LComponent.Q);
        }

        Dimension var14 = this.getSize();
        g.setFont(this.getFont());
        byte var15 = 0;
        int var6 = 0;
        int w = this.W;
        int h = this.H;
        int strLen = this.strs.length;

        for (int i = 0; i < strLen; ++i) {
            this.fillFrame(g, this.flags[i], var15, var6, w, h);
            g.setColor(this.clT == null ? this.getForeground() : this.clT);
            g.drawString(this.strs[i] + (this.flags[i] ? " ON" : " OFF"), var15 + 2, var6 + h - 3);
            var6 += h;
        }

        var6 = var14.height - this.H - imH - 2;
        g.drawRect(var15, var6, w - 1, var14.height - var6 - 1);
        g.drawImage(this.image, (var14.width - imW) / 2, var6 + 2, imW, imH, this.getBackground(), null);
        g.drawString(this.strAuthor, (var14.width - g.getFontMetrics().stringWidth(this.strAuthor)) / 2, var14.height - 2);
    }

    public void pMouse(MouseEvent event) {
        if (this.isOption) {
            this.mouseO(event);
        } else {
            int var2 = event.getID();
            int var3 = this.W;
            int var4 = this.H;
            Dimension var5 = this.getSize();
            if (var2 == MouseEvent.MOUSE_ENTERED) {
                this.repaint();
            }

            if (var2 == MouseEvent.MOUSE_PRESSED) {
                int var6 = var5.width / var3 * (event.getY() / var4) + event.getX() / var3;
                if (var6 >= this.cs.length || this.cs[var6] == this) {
                    return;
                }

                LComponent var7 = this.cs[var6];
                if (var7.getParent() != null) {
                    this.getParent().remove(var7);
                } else {
                    int var8 = 0;

                    while (this.getParent().getComponent(var8++) == this) {
                        this.getParent().add(var7, var8);
                    }

                    var7.inParent();
                }

                this.repaint();
            }

        }
    }

    private void s() {
        String var1 = "pro_menu_color_off";

        for (int var3 = 0; var3 < 2; ++var3) {
            Color[] var2 = this.cls[var3];
            if (this.config.getP(var1) != null) {
                var2[0] = new Color(this.config.getP(var1, 0));
            }

            if (this.config.getP(var1 + "_hl") != null) {
                var2[1] = new Color(this.config.getP(var1 + "_hl", 0));
            }

            if (this.config.getP(var1 + "_dk") != null) {
                var2[2] = new Color(this.config.getP(var1 + "_dk", 0));
            }

            var1 = "pro_menu_color_on";
        }

        if (this.config.getP("pro_menu_color_text") != null) {
            this.clT = new Color(this.config.getP("pro_menu_color_text", 0));
        }

    }

    public void z() {
        boolean var1 = !this.flags[1];
        this.flags[1] = var1;

        for (int var2 = 0; var2 < this.cs.length; ++var2) {
            if (this.cs[var2] != null) {
                this.cs[var2].isUpDown = var1;
            }
        }

    }
}
