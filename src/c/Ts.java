package c;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Panel;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.util.Locale;

import paintchat.M;
import paintchat.Res;
import paintchat_client.Me;
import syi.awt.Awt;
import syi.awt.LComponent;

class Ts extends LComponent {
    private static String SV = "1.114";
    public Res res;
    public Res cnf;
    private String[] strs = null;
    private int H;
    private int nowButton = -1;
    private ShiPainter app;
    private P p;
    protected Container parent;
    public Boolean isP = null;
    private boolean isUpper = true;
    public boolean isDialog = false;
    private Color[][] cls = new Color[2][3];
    private Color clText;
    private boolean is_visible;
    private boolean is_vv = true;
    private int V = 255;

    public static final synchronized void alert(String var0) {
        Me.alert(var0);
    }

    private int b(int var1) {
        FontMetrics var2 = this.getFontMetrics(this.getFont());
        int var3 = this.strs.length;
        int var4 = 2 + (int) (12.0F * LComponent.Q);
        if (var1 < var4) {
            return 0;
        } else {
            for (int var5 = 0; var5 < var3; ++var5) {
                if (var5 == 5) {
                    if (var1 > this.getSize().width - 10 - var2.stringWidth(this.strs[var5])) {
                        return 6;
                    }
                } else {
                    var4 += var2.stringWidth(this.strs[var5]) + 10;
                }

                if (var1 < var4) {
                    return var5 + 1;
                }
            }

            return -1;
        }
    }

    public static final synchronized boolean confirm(String var0) {
        return Me.confirm(var0, true);
    }

    private void ev(AWTEvent var1) {
        switch (var1.getID()) {
            case 101:
                this.pack();
                break;
            case 201:
                if (this.getV(2)) {
                    this.w(true);
                }
        }

    }

    public boolean getV(int var1) {
        return (this.V >>> var1 & 1) == 1;
    }

    public void init(ShiPainter var1, P var2, Res var3, Res var4) {
        this.parent = this.app = var1;
        this.p = var2;
        this.cnf = var4;
        boolean var5 = var4.getP("bar_visible", true);
        this.is_visible = var5;
        if (!var5) {
            this.setVisible(false);
        }

        Me.res = var3;
        this.res = var3;
        Me.conf = var4;
        super.isGUI = false;
        super.isHide = false;
        super.iGap = 0;
        String var6 = "bar_color_";
        if (var4.getP(var6 + "bk") != null) {
            this.setBackground(new Color(var4.getP(var6 + "bk", 0)));
        }

        if (var4.getP(var6 + "text") != null) {
            this.clText = new Color(var4.getP(var6 + "text", 0));
            this.setForeground(this.clText);
        }

        if (var4.getP(var6 + "frame") != null) {
            super.clFrame = new Color(var4.getP(var6 + "frame", 0));
        }

        String var7 = var6 + "off";

        int var9;
        for (var9 = 0; var9 < 2; ++var9) {
            Color[] var8 = this.cls[var9];
            if (var4.getP(var7) != null) {
                var8[0] = new Color(var4.getP(var7, 0));
            }

            if (var4.getP(var7 + "_hl") != null) {
                var8[1] = new Color(var4.getP(var7 + "_hl", 0));
            }

            if (var4.getP(var7 + "_dk") != null) {
                var8[2] = new Color(var4.getP(var7 + "_dk", 0));
            }

            var7 = var6 + "on";
        }

        var6 = var3.getP("app_name", (String) null);
        if (var6 == null) {
            var6 = "(C)" + (Locale.getDefault().getLanguage().equals("ja") ? "しぃちゃん v" + SV + " しぃペインター" : "Shi-chan v" + SV + " Shi-Painter");
        }

        this.strs = new String[]{"sUpload", "sF", "sRedo", "sUndo", "sFill", var6};

        for (var9 = 0; var9 < 5; ++var9) {
            this.strs[var9] = var3.res(this.strs[var9]);
        }

        if (var6.length() == 1 && var6.charAt(0) == '_') {
            this.is_vv = false;
        }

        Font var11 = Awt.getDefFont();
        this.setFont(var11);
        this.H = this.getFontMetrics(var11).getHeight() + 10;
        Dimension var10 = new Dimension(var1.getSize().width, this.H);
        this.setDimension(var10, var10, new Dimension(9999, this.H));
    }

    public boolean isMe() {
        return Me.isDialog();
    }

    public void layout(int var1) {
        if (this.is_visible) {
            Container var2 = this.parent;
            Dimension var3 = this.getSize();
            boolean var4 = var1 == 0;
            if (var4 && super.isGUI) {
                var4 = false;
                this.isUpper = this.getLocation().y <= var2.getSize().height / 2;
            }

            super.isGUI = var4;
            this.setSize(var3.width, var3.height);
        }

        this.pack();
    }

    synchronized void pack() {
        Container var1 = this.getParent();
        Dimension var2 = var1.getSize();
        if (var1 != null && var2 != null) {
            this.setVisible(false);
            if (this.p != null) {
                this.p.setVisible(false);
            }

            int var3 = var2.height - this.H;
            if (this.is_visible && !super.isGUI) {
                this.setBounds(0, this.isUpper ? 0 : var3, var2.width, this.H);
            } else {
                var3 = var2.height;
            }

            if (this.p != null) {
                this.p.setBounds(0, this.isUpper && this.is_visible && !super.isGUI ? this.H : 0, var2.width, var3);
            }

            if (this.is_visible) {
                this.setVisible(true);
            }

            if (this.p != null) {
                this.p.setVisible(true);
            }

        }
    }

    public void paint2(Graphics var1) {
        if (this.strs != null) {
            byte var2 = 2;
            byte var3 = 1;
            Dimension var4 = this.getSize();
            FontMetrics var5 = var1.getFontMetrics();
            int var6 = var5.getMaxAscent();
            int var7 = (int) (12.0F * LComponent.Q);
            var1.setColor(super.clFrame);
            var1.fillRect(0, 0, var7, (int) (8.0F * LComponent.Q));
            int var11 = var2 + var7;
            Color var9 = this.clText == null ? this.getForeground() : this.clText;

            Color[] var8;
            for (int var10 = 0; var10 < this.strs.length - 1; ++var10) {
                var8 = this.cls[this.nowButton == var10 + 1 ? 1 : 0];
                var7 = var5.stringWidth(this.strs[var10]) + 8;
                Awt.fillFrame(var1, this.nowButton == var10 + 1, var11, var3, var7, var4.height - 3, this.cls[0][0], this.cls[1][0], var8[2], var8[1]);
                var1.setColor(var9);
                var1.drawString(this.strs[var10], var11 + 4, var3 + var6 + 3);
                var11 += var7 + 2;
            }

            if (this.is_vv) {
                var7 = Math.min(var5.stringWidth(this.strs[5]) + 10, var4.width - var11 - 2);
                var11 = var4.width - var7 - 3;
                var1.clipRect(var11, var3, var7, var4.height - 3);
                var8 = this.cls[this.nowButton == 6 ? 1 : 0];
                Awt.fillFrame(var1, this.nowButton == 6, var11, var3, var7, var4.height - 3, this.cls[0][0], this.cls[1][0], var8[2], var8[1]);
                var1.setColor(var9);
                var1.drawString(this.strs[5], var4.width - var7 + 4, var3 + var6 + 3);
                var1.setClip(0, 0, var4.width, var4.height);
            }

        }
    }

    public void pMouse(MouseEvent var1) {
        int var2 = var1.getX();
        int var3 = this.b(var2);
        switch (var1.getID()) {
            case 501:
                if (this.getV(var3) && (this.nowButton = this.b(var2)) >= 1) {
                    this.repaint();
                }
                break;
            case 502:
                if (this.nowButton >= 0) {
                    if (this.nowButton == this.b(var2)) {
                        switch (this.nowButton) {
                            case 0:
                                this.layout(0);
                                break;
                            case 1:
                                run(this.app, 's', 2);
                                break;
                            case 2:
                                this.w(true);
                                break;
                            case 3:
                            case 4:
                                this.p.undo(this.nowButton == 4);
                                break;
                            case 5:
                                this.p.tool.up();
                                this.p.tool.lift();
                                M var4 = this.p.mi.info.m;
                                var4.iHint = 7;
                                var4.iPen = 10;
                                break;
                            case 6:
                                if (this.is_vv) {
                                    String var5 = this.cnf.getP("app_url", "http://shichan.jp/");
                                    if ((var5.length() != 1 || var5.charAt(0) != '_') && confirm("kakunin_0")) {
                                        this.app.jump(var5, "top");
                                    }
                                }
                        }
                    }

                    this.nowButton = -1;
                    this.repaint();
                }
        }

    }

    public static void run(Runnable var0) {
        run(var0, 'x', 1);
    }

    public static void run(Runnable var0, char var1, int var2) {
        Thread var3 = new Thread(var0, String.valueOf(var1));
        var3.setPriority(var2);
        var3.start();
    }

    public void setV(int var1, boolean var2) {
        int var3 = 1 << var1;
        this.V = this.V ^ var3 | (var2 ? 1 : 0) << var1;
    }

    public String v() {
        return this.strs == null ? "" : this.strs[5];
    }

    public void w(boolean var1) {
        Container var2 = this.parent;
        boolean var3 = var2 == this.app;
        if (!var1 || confirm(var3 ? "IsWindowView" : "IsPageView")) {
            var2.remove(this.p);
            var2.remove(this);
            if (!var3) {
                ((Window) this.parent).dispose();
            }

            Object var4 = null;
            if (var3) {
                Object var10001;
                label38:
                {
                    label37:
                    {
                        if (this.isP == null) {
                            if (confirm("IsIndependent")) {
                                break label37;
                            }
                        } else if (this.isP) {
                            break label37;
                        }

                        var10001 = new Ts.W(this.strs[5]);
                        break label38;
                    }

                    var10001 = new Ts.WF(this.strs[5]);
                }

                var4 = var10001;
                this.parent = (Container) var10001;
                Dimension var5 = this.getToolkit().getScreenSize();
                ((Container) var4).setLayout(new BorderLayout());
                Awt.getDef((Component) var4);
                Panel var6 = new Panel((LayoutManager) null);
                var6.add(this);
                var6.add(this.p);
                ((Container) var4).add((Component) var6, (Object) "Center");
                ((Component) var4).setLocation(10, 10);
                ((Component) var4).setSize(var5.width - 20, var5.height - 20);
                ((Component) var4).setVisible(true);
                if (var4 instanceof Frame) {
                    Awt.setPFrame((Frame) var4);
                }
            } else {
                ShiPainter var7 = this.app;
                this.parent = this.app;
                var7.add(this);
                var7.add(this.p);
                Awt.setPFrame((Frame) Awt.getParent(var7));
            }

            this.pack();
        }
    }

    private class W extends Dialog {
        W(String var2) {
            super(Awt.getPFrame(), var2, false);
            this.enableEvents(65L);
        }

        protected void processEvent(AWTEvent var1) {
            Ts.this.ev(var1);
        }
    }

    private class WF extends Frame {
        WF(String var2) {
            super(var2);
            this.enableEvents(65L);
        }

        protected void processEvent(AWTEvent var1) {
            Ts.this.ev(var1);
        }
    }
}
