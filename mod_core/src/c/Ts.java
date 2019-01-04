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

import static java.awt.event.MouseEvent.*;

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

    private void ev(AWTEvent evt) {
        switch (evt.getID()) {
            case 101: // WINDOW_EVENT_MASK | MOUSE_MOTION_EVENT_MASK | FOCUS_EVENT_MASK | COMPONENT_EVENT_MASK
                this.pack();
                break;
            case 201: // ACTION_EVENT_MASK | WINDOW_EVENT_MASK | KEY_EVENT_MASK | COMPONENT_EVENT_MASK
                if (this.getV(2)) {
                    this.w(true);
                }
        }

    }

    /** Returns the bit at the pos position from V (something to do with buttons) */
    public boolean getV(int pos) {
        return (this.V >>> pos & 1) == 1;
    }

    /** Sets the pos bit in V (something to do with buttons) */
    public void setV(int pos, boolean value) { this.V = this.V ^ (1 << pos) | (value ? 1 : 0) << pos; }

    public void init(ShiPainter app, P panel, Res res, Res conf) {
        this.parent = this.app = app;
        this.p = panel;
        this.cnf = conf;
        boolean isBarVisible = conf.getP("bar_visible", !this.app.d_isDesktop());
        this.is_visible = isBarVisible;
        if (!isBarVisible) {
            this.setVisible(false);
        }

        Me.res = res;
        this.res = res;
        Me.conf = conf;
        super.isGUI = false;
        super.isHide = false;
        super.iGap = 0;
        String strBarColor = "bar_color_";
        if (conf.getP(strBarColor + "bk") != null) {
            this.setBackground(new Color(conf.getP(strBarColor + "bk", 0)));
        }

        if (conf.getP(strBarColor + "text") != null) {
            this.clText = new Color(conf.getP(strBarColor + "text", 0));
            this.setForeground(this.clText);
        }

        if (conf.getP(strBarColor + "frame") != null) {
            super.clFrame = new Color(conf.getP(strBarColor + "frame", 0));
        }

        String strBarColorOff = strBarColor + "off";

        int i;
        for (i = 0; i < 2; ++i) {
            Color[] var8 = this.cls[i];
            if (conf.getP(strBarColorOff) != null) {
                var8[0] = new Color(conf.getP(strBarColorOff, 0));
            }

            if (conf.getP(strBarColorOff + "_hl") != null) {
                var8[1] = new Color(conf.getP(strBarColorOff + "_hl", 0));
            }

            if (conf.getP(strBarColorOff + "_dk") != null) {
                var8[2] = new Color(conf.getP(strBarColorOff + "_dk", 0));
            }

            strBarColorOff = strBarColor + "on";
        }

        strBarColor = res.getP("app_name", (String) null);
        if (strBarColor == null) {
            strBarColor = "(C)" + (Locale.getDefault().getLanguage().equals("ja") ? "しぃちゃん v" + SV + " しぃペインター" : "Shi-chan v" + SV + " Shi-Painter");
        }

        this.strs = new String[]{"sUpload", "sF", "sRedo", "sUndo", "sFill", strBarColor};

        for (i = 0; i < 5; ++i) {
            this.strs[i] = res.res(this.strs[i]);
        }

        if (strBarColor.length() == 1 && strBarColor.charAt(0) == '_') {
            this.is_vv = false;
        }

        Font font = Awt.getDefFont();
        this.setFont(font);
        this.H = this.getFontMetrics(font).getHeight() + 10;
        Dimension dApp = new Dimension(app.getSize().width, this.H);
        this.setDimension(dApp, dApp, new Dimension(9999, this.H));
    }

    public boolean isMe() {
        return Me.isDialog();
    }

    public void layout(int var1) {
        if (this.is_visible) {
            Container parent = this.parent;
            Dimension dim = this.getSize();
            boolean var4 = var1 == 0;
            if (var4 && super.isGUI) {
                var4 = false;
                this.isUpper = this.getLocation().y <= parent.getSize().height / 2;
            }

            super.isGUI = var4;
            this.setSize(dim.width, dim.height);
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

    public void paint2(Graphics g) {
        if (this.strs != null) {
            byte var2 = 2;
            byte var3 = 1;
            Dimension var4 = this.getSize();
            FontMetrics var5 = g.getFontMetrics();
            int var6 = var5.getMaxAscent();
            int var7 = (int) (12.0F * LComponent.Q);
            g.setColor(super.clFrame);
            g.fillRect(0, 0, var7, (int) (8.0F * LComponent.Q));
            int var11 = var2 + var7;
            Color var9 = this.clText == null ? this.getForeground() : this.clText;

            Color[] var8;
            for (int var10 = 0; var10 < this.strs.length - 1; ++var10) {
                var8 = this.cls[this.nowButton == var10 + 1 ? 1 : 0];
                var7 = var5.stringWidth(this.strs[var10]) + 8;
                Awt.fillFrame(g, this.nowButton == var10 + 1, var11, var3, var7, var4.height - 3, this.cls[0][0], this.cls[1][0], var8[2], var8[1]);
                g.setColor(var9);
                g.drawString(this.strs[var10], var11 + 4, var3 + var6 + 3);
                var11 += var7 + 2;
            }

            if (this.is_vv) {
                var7 = Math.min(var5.stringWidth(this.strs[5]) + 10, var4.width - var11 - 2);
                var11 = var4.width - var7 - 3;
                g.clipRect(var11, var3, var7, var4.height - 3);
                var8 = this.cls[this.nowButton == 6 ? 1 : 0];
                Awt.fillFrame(g, this.nowButton == 6, var11, var3, var7, var4.height - 3, this.cls[0][0], this.cls[1][0], var8[2], var8[1]);
                g.setColor(var9);
                g.drawString(this.strs[5], var4.width - var7 + 4, var3 + var6 + 3);
                g.setClip(0, 0, var4.width, var4.height);
            }

        }
    }

    public void pMouse(MouseEvent evt) {
        int x = evt.getX();
        int var3 = this.b(x);
        switch (evt.getID()) {
            case MOUSE_PRESSED:
                if (this.getV(var3) && (this.nowButton = this.b(x)) >= 1) {
                    this.repaint();
                }
                break;
            case MOUSE_RELEASED:
                if (this.nowButton >= 0) {
                    if (this.nowButton == this.b(x)) {
                        switch (this.nowButton) {
                            case 0: // Toggle top bar
                                this.layout(0);
                                break;
                            case 1: // Upload
                                run(this.app, 's', 2);
                                break;
                            case 2: // Float
                                this.w(true);
                                break;
                            case 3: // Redo
                            case 4: // Undo
                                this.p.undo(this.nowButton == 4);
                                break;
                            case 5: // Fill
                                this.p.tool.up();
                                this.p.tool.lift();
                                M mg = this.p.mi.info.m;
                                mg.iHint = M.H_FILL;
                                mg.iPen = M.P_FILL;
                                break;
                            case 6: // Credits
                                if (this.is_vv) {
                                    String appUrl = this.cnf.getP("app_url", "http://shichan.jp/");
                                    if ((appUrl.length() != 1 || appUrl.charAt(0) != '_') && confirm("kakunin_0")) {
                                        this.app.jump(appUrl, "top");
                                    }
                                }
                        }
                    }

                    this.nowButton = -1;
                    this.repaint();
                }
        }

    }

    public static void run(Runnable target) {
        run(target, 'x', 1);
    }

    public static void run(Runnable target, char name, int priority) {
        Thread thread = new Thread(target, String.valueOf(name));
        thread.setPriority(priority);
        thread.start();
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
