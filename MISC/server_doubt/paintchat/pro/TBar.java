package paintchat.pro;

import java.applet.Applet;
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

public class TBar extends LComponent {
    private boolean isOption;
    private Res res;
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

    public TBar(Res var1, Res var2, LComponent[] var3) {
        this.res = var2;
        this.config = var1;
        this.cs = var3;
        this.H = (int) (19.0F * LComponent.Q);
    }

    public final void drawFrame(Graphics var1, boolean var2, int var3, int var4, int var5, int var6) {
        Color[] var7 = this.cls[var2 ? 1 : 0];
        Awt.drawFrame(var1, var2, var3, var4, var5, var6, var7[2], var7[1]);
    }

    public final void fillFrame(Graphics var1, boolean var2, int var3, int var4, int var5, int var6) {
        this.drawFrame(var1, var2, var3, var4, var5, var6);
        Color[] var7 = this.cls[var2 ? 1 : 0];
        Awt.setup();
        Awt.fillFrame(var1, var2, var3, var4, var5, var6, this.cls[0][0], this.cls[1][0], var7[2], var7[1]);
    }

    public void init() {
        super.isHide = false;
        int var1 = this.H;
        FontMetrics var3 = this.getFontMetrics(this.getFont());
        int var4 = this.cs.length - 1;
        int var5 = 0;
        this.strs = new String[var4];

        for (int var6 = 0; var6 < var4; ++var6) {
            String var2 = this.res.res("window_" + var6);
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
        this.strAuthor = this.res.res("option_author");
        this.s();
        this.setDimension((Dimension) null, new Dimension(10, 10), (Dimension) null);
    }

    public void mouseO(MouseEvent var1) {
        if (var1.getID() == 501) {
            int var2 = var1.getY();
            if (var2 <= this.H) {
                boolean var3 = !this.flags[0];
                this.mi.isGUI = var3;
                this.flags[0] = var3;
                this.repaint();
                this.mi.setVisible(false);
                this.mi.setDimension((Dimension) null, this.mi.getSize(), this.getParent().getSize());
                this.mi.setVisible(true);
            } else if (var2 <= this.H * 2) {
                this.z();
                this.repaint();
            } else if (var2 > this.H * 3) {
                try {
                    String var6 = this.config.getP("app_url", "http://shichan.jp/");
                    if ((var6.length() != 1 || var6.charAt(0) != '_') && this.mi.alert("kakunin_0", true)) {
                        Applet var4 = (Applet) this.getParent().getParent();
                        var4.getAppletContext().showDocument(new URL(var4.getCodeBase(), var6), "_blank");
                    }
                } catch (Throwable var5) {
                    var5.printStackTrace();
                }
            }

        }
    }

    public void paint2(Graphics var1) {
        this.getSize();
        if (this.isOption) {
            this.paintO(var1);
        } else {
            this.paintBar(var1);
        }

    }

    private void paintBar(Graphics var1) {
        int var2 = 0;
        int var3 = 0;
        int var4 = 0;
        Dimension var5 = this.getSize();
        int var6 = this.W;
        int var7 = this.H;

        for (int var8 = 0; var8 < this.cs.length; ++var8) {
            if (this.cs[var8] != null && this.cs[var8] != this) {
                this.fillFrame(var1, !this.cs[var8].isVisible(), var2, var3, var6, var7);
                var1.setColor(this.clT == null ? Awt.cFore : this.clT);
                var1.drawString(this.strs[var4++], var2 + 2, var3 + var7 - 3);
                if ((var2 += var6) + var6 > var5.width) {
                    var2 = 0;
                    var3 += var7;
                }
            }
        }

    }

    private void paintO(Graphics var1) {
        int var6;
        int var7;
        if (this.image == null) {
            try {
                FontMetrics var3 = this.getFontMetrics(this.getFont());
                int var4 = this.strs.length;
                int var5 = 0;

                for (var6 = 0; var6 < var4; ++var6) {
                    String var2 = this.res.res("option_" + var6);
                    var5 = Math.max(var3.stringWidth(var2 + " OFF") + var2.length() + 3, var5);
                    this.strs[var6] = var2;
                }

                var5 = Math.max(var3.stringWidth(this.strAuthor) + this.strAuthor.length(), var5);
                this.image = this.getToolkit().createImage((byte[]) this.config.getRes("bn.gif"));
                Awt.wait(this.image);
                var6 = this.image.getWidth((ImageObserver) null);
                var7 = this.image.getHeight((ImageObserver) null);
                if (LComponent.Q > 1.0F) {
                    var6 = (int) ((float) var6 * LComponent.Q);
                    var7 = (int) ((float) var7 * LComponent.Q);
                }

                this.W = Math.max(var5, var6);
                this.H = var3.getHeight() + 2;
                Dimension var8 = new Dimension(this.W, this.H * (var4 + 2) + var7 + 2);
                this.setDimension(new Dimension(this.W, this.H), var8, var8);
            } catch (RuntimeException var11) {
            }
        }

        int var12 = this.image.getWidth((ImageObserver) null);
        int var13 = this.image.getHeight((ImageObserver) null);
        if (LComponent.Q > 1.0F) {
            var12 = (int) ((float) var12 * LComponent.Q);
            var13 = (int) ((float) var13 * LComponent.Q);
        }

        Dimension var14 = this.getSize();
        var1.setFont(this.getFont());
        byte var15 = 0;
        var6 = 0;
        var7 = this.W;
        int var16 = this.H;
        int var9 = this.strs.length;

        for (int var10 = 0; var10 < var9; ++var10) {
            this.fillFrame(var1, this.flags[var10], var15, var6, var7, var16);
            var1.setColor(this.clT == null ? this.getForeground() : this.clT);
            var1.drawString(this.strs[var10] + (this.flags[var10] ? " ON" : " OFF"), var15 + 2, var6 + var16 - 3);
            var6 += var16;
        }

        var6 = var14.height - this.H - var13 - 2;
        var1.drawRect(var15, var6, var7 - 1, var14.height - var6 - 1);
        var1.drawImage(this.image, (var14.width - var12) / 2, var6 + 2, var12, var13, this.getBackground(), (ImageObserver) null);
        var1.drawString(this.strAuthor, (var14.width - var1.getFontMetrics().stringWidth(this.strAuthor)) / 2, var14.height - 2);
    }

    public void pMouse(MouseEvent var1) {
        if (this.isOption) {
            this.mouseO(var1);
        } else {
            int var2 = var1.getID();
            int var3 = this.W;
            int var4 = this.H;
            Dimension var5 = this.getSize();
            if (var2 == 504) {
                this.repaint();
            }

            if (var2 == 501) {
                int var6 = var5.width / var3 * (var1.getY() / var4) + var1.getX() / var3;
                if (var6 >= this.cs.length || this.cs[var6] == this) {
                    return;
                }

                this.cs[var6].setVisible(!this.cs[var6].isVisible());
                this.invalidate();
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
