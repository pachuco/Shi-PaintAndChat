package paintchat_client;

import jaba.applet.Applet;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.lang.reflect.Method;

import paintchat.M;
import paintchat.Res;
import paintchat.ToolBox;
import syi.awt.Awt;
import syi.awt.LComponent;

import static java.awt.event.KeyEvent.*;
import static java.awt.event.MouseEvent.*;

public class Mi extends LComponent implements ActionListener {
    private LComponent tab;
    private Method mGet;
    private Method mPoll;
    public ToolBox tBox = null;
    private IMi imi;
    private boolean isRight = false;
    public TextField text;
    private boolean isText;
    private M m;
    public M.Info info;
    public M.User user;
    private M mgInfo;
    public boolean isEnable = false;
    private int[] ps = new int[5];
    private int psCount = -1;
    private Graphics primary;
    private Graphics primary2;
    private int oldX = 0;
    private int oldY = 0;
    private boolean isIn = false;
    private int nowCur = -1;
    private Cursor[] cursors;
    private Image imCopy = null;
    private boolean isSpace = false;
    private boolean isScroll = false;
    private boolean isDrag = false;
    private boolean isVTool = true;
    private Point poS = new Point();
    private int[] rS = new int[20];
    private int sizeBar = 20;
    private Color[] cls;
    private Color cPre;

    public Mi(IMi var1, Res var2) throws Exception {
        this.cPre = Color.black;
        this.imi = var1;
        super.isRepaint = false;
        super.isHide = false;
        super.isGUI = false;
        super.iGap = 2;
        Me.res = var2;
    }

    public void actionPerformed(ActionEvent var1) {
        if (this.text != null) {
            this.addText(var1.getActionCommand());
            if (this.isText) {
                this.text.setVisible(false);
            }
        }

    }

    public void addText(String var1) {
        try {
            if (!this.mgInfo.isText()) {
                return;
            }

            this.setM();
            byte[] var2 = ('\u0000' + var1).getBytes("UTF8");
            this.m.setRetouch(this.ps, var2, var2.length, true);
            this.m.draw();
            this.send(this.m);
        } catch (Exception var3) {
            ;
        }

    }

    public boolean alert(String var1, boolean var2) {
        try {
            return Me.confirm(var1, var2);
        } catch (Throwable var4) {
            var4.printStackTrace();
            return true;
        }
    }

    private boolean b(int var1, int var2) throws Throwable {
        if (!this.in(var1, var2)) {
            return false;
        } else {
            Dimension var3 = this.info.getSize();
            int var4 = var3.width;
            int var5 = var3.height;
            if (this.isSpace) {
                this.isIn = false;
                this.imi.scroll(true, 0, 0);
                this.isScroll = true;
                this.poS.setLocation(var1, var2);
                return true;
            } else if (var1 <= var4 && var2 <= var5) {
                return false;
            } else {
                if (var1 < this.sizeBar) {
                    this.scaleChange(this.isRight ? -1 : 1, false);
                    this.isDrag = false;
                } else if (var2 < this.sizeBar) {
                    if (this.tab == null) {
                        try {
                            Class var6 = Class.forName("syi.awt.Tab");
                            this.tab = (LComponent) var6.getConstructors()[0].newInstance(this.getParent(), this.info);
                            this.mGet = var6.getMethod("strange", (Class[]) null);
                            this.mPoll = var6.getMethod("poll", (Class[]) null);
                        } catch (Throwable var7) {
                            this.tab = this;
                        }
                    } else {
                        this.getParent().add(tab, 0);
                        this.tab.setVisible(true);
                    }

                    this.isDrag = false;
                } else if (var1 > var4 && var2 > var5) {
                    this.scaleChange(this.isRight ? 1 : -1, false);
                    this.isDrag = false;
                } else {
                    this.isIn = false;
                    this.imi.scroll(true, 0, 0);
                    this.isScroll = true;
                    this.poS.setLocation(var1, var2);
                }

                return true;
            }
        }
    }

    private void cursor(int var1, int var2, int var3) {
        if (var1 == MOUSE_MOVED) {
            Dimension var4 = this.info.getSize();
            int var5 = this.sizeBar;
            int var6 = var4.width;
            int var7 = var4.height;
            int var8;
            if (var2 <= var6 && var3 < var7) {
                var8 = 0;
            } else {
                var8 = var2 < var5 ? 2 : (var3 < var5 ? 0 : (var2 > var6 && var3 > var7 ? 3 : 1));
            }

            if (this.nowCur != var8) {
                this.nowCur = var8;
                this.setCursor(this.cursors[var8]);
            }

        }
    }

    private void dBz(int var1, int var2, int var3) {
        try {
            if (this.psCount <= 0) {
                if (var1 != MOUSE_RELEASED) {
                    this.dLine(var1, var2, var3);
                } else {
                    this.primary2.drawLine(this.ps[0] >> 16, (short) this.ps[0], this.ps[1] >> 16, (short) this.ps[1]);
                    this.user.setRect(0, 0, 0, 0);
                    this.p(3, var2, var3);
                    this.ps[1] = this.ps[0];
                    this.ps[2] = this.ps[0];
                    this.psCount = 1;
                    this.dPreB(false);
                }

                return;
            }

            this.ePre();
            this.dPreB(true);
            switch (var1) {
                case MOUSE_RELEASED:
                    this.p(this.psCount++, var2, var3);
                    if (this.psCount >= 3) {
                        --this.psCount;
                        this.psCount = -1;
                        this.m.reset(true);
                        this.m.setRetouch(this.ps, (byte[]) null, 0, true);
                        this.m.draw();
                        this.dEnd(false);
                        return;
                    }

                    this.p(this.psCount, var2, var3);
                    break;
                case MOUSE_MOVED:
                case MOUSE_DRAGGED:
                    this.p(this.psCount, var2, var3);
                    this.p(2, var2, var3);
                case MOUSE_ENTERED:
                case MOUSE_EXITED:
            }

            this.dPreB(false);
        } catch (Throwable var5) {
            var5.printStackTrace();
        }

    }

    private void dClear() throws InterruptedException {
        if (this.alert("kakunin_1", true)) {
            this.setM();
            this.m.setRetouch((int[]) null, (byte[]) null, 0, true);
            this.m.draw();
            this.dEnd(false);
        }
    }

    private void dCopy(int var1, int var2, int var3) throws InterruptedException {
        if (this.psCount <= 1) {
            if (var1 == 502) {
                if (this.psCount <= 0) {
                    return;
                }

                this.psCount = 2;
                this.p(1, var2, var3);
                if (!this.transRect()) {
                    this.psCount = -1;
                } else {
                    this.ps[0] = this.user.points[0];
                    this.ps[1] = this.user.points[1];
                    this.ps[2] = this.ps[0];
                }

                this.ps[4] = this.mgInfo.iLayer;
            } else {
                this.dRect(var1, var2, var3);
            }

        } else {
            int var4 = this.ps[2] >> 16;
            short var5 = (short) this.ps[2];
            int var6 = (this.ps[1] >> 16) - (this.ps[0] >> 16);
            int var7 = (short) this.ps[1] - (short) this.ps[0];
            int var8;
            switch (var1) {
                case MOUSE_PRESSED:
                    if (this.imCopy != null) {
                        this.imCopy.flush();
                    }

                    this.imCopy = this.m.getImage(this.ps[4], var4, var5, var6, var7);
                    this.p(3, var2, var3);
                    break;
                case MOUSE_RELEASED:
                    var4 += var2 - (this.ps[3] >> 16);
                    var8 = var5 + (var3 - (short) this.ps[3]);
                    this.p(2, var4, var8);
                    this.m.set(this.mgInfo);
                    this.m.iLayerSrc = this.ps[4];
                    this.m.setRetouch(this.ps, (byte[]) null, 0, true);
                    this.m.draw();
                    this.dEnd(false);
                    this.psCount = -1;
                case MOUSE_MOVED:
                case MOUSE_ENTERED:
                case MOUSE_EXITED:
                default:
                    break;
                case MOUSE_DRAGGED:
                    this.m_paint(var4, var5, var6, var7);
                    var4 += var2 - (this.ps[3] >> 16);
                    var8 = var5 + (var3 - (short) this.ps[3]);
                    this.p(2, var4, var8);
                    this.p(3, var2, var3);
                    this.primary2.setPaintMode();
                    this.primary2.drawImage(this.imCopy, var4, var8, var6, var7, Color.white, (ImageObserver) null);
                    this.primary2.setXORMode(Color.white);
            }

        }
    }

    private void dEnd(boolean var1) throws InterruptedException {
        M mg = this.m;
        if (mg.iHint != M.H_CLEAR && mg.iPen == M.P_FUSION && mg.iHint != M.H_L) {
            int waitWas = this.user.wait;
            this.user.wait = -2;
            if (var1) {
                mg.dEnd();
            }

            int layer = mg.iLayer;


            for (int i = layer + 1; i < this.info.L; ++i) {
                if (this.info.layers[i].iAlpha != 0.0F) {
                    mg.iLayerSrc = i;
                    this.setA();
                    mg.draw();
                    this.send(mg);
                }
            }

            for (int i = layer - 1; i >= 0; --i) {
                if (this.info.layers[i].iAlpha != 0.0F) {
                    mg.iLayerSrc = i;
                    this.setA();
                    mg.draw();
                    this.send(mg);
                }
            }

            this.user.wait = waitWas;
            this.mPaint((Graphics) null);
        } else {
            if (var1) {
                mg.dEnd();
            }

            this.send(mg);
        }

    }

    private void dFLine(int eventId, int x, int y) {
        try {
            switch (eventId) {
                case MOUSE_PRESSED:
                    this.poll();
                    this.setM();
                    this.m.dStart(x, y, this.getS(), true, true);
                    this.oldX = 0;
                    this.oldY = 0;
                    this.psCount = 1;
                    this.p(0, x, y);
                    break;
                case MOUSE_RELEASED:
                    if (this.psCount >= 0) {
                        if (this.m.iHint == M.H_SP) {
                            this.m.dNext(x, y, this.getS(), 0);
                        }

                        this.dEnd(true);
                        this.psCount = -1;
                    }
                case MOUSE_MOVED:
                case MOUSE_ENTERED:
                case MOUSE_EXITED:
                default:
                    break;
                case MOUSE_DRAGGED:
                    if (this.psCount >= 0 && this.isOKPo(x, y)) {
                        this.psCount = 0;
                        this.m.dNext(x, y, this.getS(), 0);
                        this.p(this.psCount, x, y);
                        ++this.psCount;
                    }
            }
        } catch (Throwable var5) {
            var5.printStackTrace();
        }

    }

    private void dLine(int eventId, int x, int y) {
        try {
            switch (eventId) {
                case MOUSE_PRESSED:
                    this.setM();

                    for (int i = 0; i < 4; ++i) {
                        this.p(i, x, y);
                    }

                    this.psCount = 0;
                    this.primary2.setColor(new Color(this.m.iColor));
                    this.primary2.drawLine(x, y, x, y);
                    break;
                case MOUSE_RELEASED:
                    if (this.psCount >= 0) {
                        int var4 = this.ps[0] >> 16;
                        short var5 = (short) this.ps[0];
                        int var6 = this.m.iSize;
                        int var7 = x - var4;
                        int var8 = y - var5;
                        int var9 = Math.max(Math.abs(var7), Math.abs(var8));
                        if (var9 > 0) {
                            this.m.dStart(var4, var5, var6, true, true);
                            this.m.dNext(x, y, var6, 0);
                            this.dEnd(true);
                        } else {
                            this.mPaint((Graphics) null);
                        }

                        this.psCount = -1;
                    }
                case MOUSE_MOVED:
                case MOUSE_ENTERED:
                case MOUSE_EXITED:
                default:
                    break;
                case MOUSE_DRAGGED:
                    if (this.psCount >= 0) {
                        this.primary2.drawLine(this.ps[0] >> 16, (short) this.ps[0], this.ps[1] >> 16, (short) this.ps[1]);
                        this.primary2.drawLine(this.ps[0] >> 16, (short) this.ps[0], x, y);
                        this.p(1, x, y);
                    }
            }
        } catch (Throwable var10) {
            var10.printStackTrace();
        }

    }

    private final void dPre(int var1, int var2, boolean var3) {
        if (this.mgInfo != null && !this.mgInfo.isText() && this.primary2 != null && this.psCount < 0) {
            int var4 = this.mgInfo.iHint;
            if (var4 < 3 || var4 > 6) {
                try {
                    int var5 = this.info.getPenSize(this.mgInfo) * this.info.scale / this.info.Q;
                    if (var5 <= 5) {
                        return;
                    }

                    int var6 = var5 >>> 1;
                    Graphics var7 = this.primary2;
                    Color var8 = this.cPre;
                    var8 = (var8.getRGB() & 0xFFFFFF) != this.mgInfo.iColor >>> 1 ? new Color(this.mgInfo.iColor >>> 1) : var8;
                    this.cPre = var8;
                    var7.setColor(this.mgInfo.iPen != M.P_WHITE && this.mgInfo.iPen != M.P_SWHITE ? (var8.getRGB() == 0xFFFFFF ? Color.red : var8) : Color.cyan);
                    if (var5 <= this.info.scale * 2) {
                        if (var3) {
                            var7.fillRect(this.oldX - var6, this.oldY - var6, var5, var5);
                        }

                        var7.fillRect(var1 - var6, var2 - var6, var5, var5);
                    } else {
                        if (var3) {
                            var7.drawOval(this.oldX - var6, this.oldY - var6, var5, var5);
                        }

                        var7.drawOval(var1 - var6, var2 - var6, var5, var5);
                    }

                    this.oldX = var1;
                    this.oldY = var2;
                } catch (RuntimeException var9) {
                    ;
                }

            }
        }
    }

    private void dPreB(boolean var1) throws InterruptedException {
        if (!var1) {
            long var2 = this.user.getRect();
            this.m.reset(false);
            this.user.isPre = true;
            this.m.setRetouch(this.ps, (byte[]) null, 0, true);
            this.m.draw();
            this.user.addRect((int) (var2 >>> 48), (int) (var2 >>> 32) & '\uffff', (int) (var2 >>> 16) & '\uffff', (int) (var2 & 65535L));
            this.m.dBuffer();
            this.user.isPre = false;
        }

        Graphics var7 = this.primary2;
        int var3 = this.psCount + 1;

        for (int var4 = 0; var4 < (var3 - 2 + 1) * 2; var4 += 2) {
            var7.drawLine(this.ps[var4] >> 16, (short) this.ps[var4], this.ps[var4 + 1] >> 16, (short) this.ps[var4 + 1]);
        }

        byte var8 = 7;
        int var5 = var8 / 2;

        for (int var6 = 1; var6 < var3; ++var6) {
            var7.drawOval((this.ps[var6] >> 16) - var5, (short) this.ps[var6] - var5, var8, var8);
        }

    }

    public void drawScroll(Graphics g) {
        try {
            if (g == null) g = this.primary;
            if (g == null) return;

            float var3 = (float) this.info.scale;
            int var4 = this.sizeBar;
            int var5 = this.info.scaleX;
            int var6 = this.info.scaleY;
            int var7 = this.info.imW;
            int var8 = this.info.imH;
            Dimension var9 = this.info.getSize();
            int var10 = (int) ((float) var9.width / var3);
            int var11 = (int) ((float) var9.height / var3);
            if (var5 + var10 >= var7) {
                var5 = Math.max(0, var7 - var10);
                this.info.scaleX = var5;
            }

            if (var6 + var11 - 1 >= var8) {
                var6 = Math.max(0, var8 - var11);
                this.info.scaleY = var6;
            }

            int var12 = var9.width - var4;
            int var13 = var9.height - var4;
            int var14 = Math.min((int) ((float) var10 / (float) var7 * (float) var12), var12);
            int var15 = Math.min((int) ((float) var11 / (float) var8 * (float) var13), var13);
            int var16 = (int) ((float) var5 / (float) var7 * (float) var12);
            int var17 = (int) ((float) var6 / (float) var8 * (float) var13);
            int[] var18 = this.rS;
            g.setColor(this.cls[0]);

            int var2;
            for (var2 = 0; var2 < 20; var2 += 4) {
                g.drawRect(var18[var2], var18[var2 + 1], var18[var2 + 2], var18[var2 + 3]);
            }

            if (var16 > 0) {
                g.setColor(this.cls[2]);
                g.drawRect(var18[0] + 1, var18[1] + 1, var16 - 2, var18[3] - 2);
                g.setColor(this.cls[1]);
                g.fillRect(var18[0] + 2, var18[1] + 2, var16 - 2, var18[3] - 3);
            }

            g.setColor(this.cls[2]);
            g.drawRect(var18[0] + var16 + var14, var18[1] + 1, var18[2] - var16 - var14 - 1, var18[3] - 2);
            g.setColor(this.cls[1]);
            g.fillRect(var18[0] + 1 + var16 + var14, var18[1] + 2, var18[2] - var16 - var14 - 2, var18[3] - 3);
            g.setColor(this.cls[1]);
            if (var17 > 0) {
                g.setColor(this.cls[2]);
                g.drawRect(var18[4] + 1, var18[5] + 1, var18[6] - 2, var17 - 1);
                g.setColor(this.cls[1]);
                g.fillRect(var18[4] + 2, var18[5] + 2, var18[6] - 3, var17 - 1);
            }

            g.setColor(this.cls[2]);
            g.drawRect(var18[4] + 1, var18[5] + var17 + var15, var18[6] - 2, var18[7] - var17 - var15 - 1);
            g.setColor(this.cls[1]);
            g.fillRect(var18[4] + 2, var18[5] + var17 + var15, var18[6] - 3, var18[7] - var17 - var15 - 1);

            int var19;
            int var20;
            int var21;
            int var22;
            for (var2 = 8; var2 < 20; var2 += 4) {
                for (var19 = 0; var19 < 2; ++var19) {
                    g.setColor(this.cls[2 - var19]);
                    if (var19 == 0) {
                        g.drawRect(var18[var2] + 1, var18[var2 + 1] + 1, var18[var2 + 2] - 2, var18[var2 + 3] - 2);
                    } else {
                        g.fillRect(var18[var2] + 2, var18[var2 + 1] + 2, var18[var2 + 2] - 3, var18[var2 + 3] - 3);
                    }
                }

                g.setColor(this.cls[3]);
                var19 = var18[var2 + 2] / 2;
                var20 = var18[var2 + 3] / 2;
                if (var2 == 16) {
                    var21 = var18[var2] + var19 / 2;
                    var22 = var18[var2 + 1] + var20 / 2;
                    var20 /= 2;
                    g.drawRect(var21, var22, var19, var20);
                    g.fillRect(var21, var22 + var20, 1, var20);
                } else {
                    g.fillRect(var18[var2] + var19 / 2, var18[var2 + 1] + var20, var19 + 1, 1);
                    if (var2 == 8) {
                        g.fillRect(var18[var2] + var19, var18[var2 + 1] + var20 / 2, 1, var20);
                    }
                }
            }

            var19 = var18[0] + var16;
            var20 = var18[1] + 1;
            var21 = var18[4] + 1;
            var22 = var18[5] + var17;
            g.setColor(this.cls[0]);
            g.drawRect(var19, var20, var14, var18[3] - 2);
            g.drawRect(var21, var22, var18[6] - 2, var15 + 1);
            g.setColor(this.cls[3]);
            g.fillRect(var19 + 2, var20 + 2, var14 - 3, var18[3] - 5);
            g.fillRect(var21 + 2, var22 + 2, var18[6] - 5, var15 - 2);
            g.setColor(this.cls[4]);
            g.fillRect(var19 + 1, var20 + 1, var14 - 2, 1);
            g.fillRect(var19 + 1, var20 + 2, 1, var18[3] - 5);
            g.fillRect(var21 + 1, var22 + 1, var18[6] - 4, 1);
            g.fillRect(var21 + 1, var22 + 2, 1, var15 - 2);
            g.setColor(this.cls[5]);
            g.fillRect(var19 + var14 - 1, var20 + 1, 1, var18[3] - 4);
            g.fillRect(var19 + 1, var20 + var18[3] - 3, var14 - 1, 1);
            g.fillRect(var21 + var18[6] - 3, var22 + 1, 1, var15 - 1);
            g.fillRect(var21 + 1, var22 + var15, var18[6] - 3, 1);
        } catch (Throwable var23) {
            var23.printStackTrace();
        }

    }

    private void dRect(int eventId, int var2, int var3) {
        try {
            int[] var4 = this.user.points;
            switch (eventId) {
                case MOUSE_PRESSED:
                    this.setM();
                    this.p(0, var2, var3);
                    this.m.memset((int[]) var4, (int) 0);
                    this.psCount = 1;
                    break;
                case MOUSE_RELEASED:
                    if (this.psCount > 0) {
                        this.p(1, var2, var3);
                        if (this.transRect()) {
                            this.ps[0] = var4[0];
                            this.ps[1] = var4[1];
                            this.m.setRetouch(this.ps, (byte[]) null, 0, true);
                            if (this.m.iPen != M.P_FUSION) {
                                this.m.draw();
                            }

                            this.dEnd(false);
                        }
                    }

                    this.psCount = -1;
                case MOUSE_MOVED:
                case MOUSE_ENTERED:
                case MOUSE_EXITED:
                default:
                    break;
                case MOUSE_DRAGGED:
                    if (this.psCount == 1) {
                        int var5 = var4[0];
                        int var6 = var4[1];
                        int var7 = var5 >> 16;
                        short var8 = (short) var5;
                        this.primary2.drawRect(var7, var8, (var6 >> 16) - var7 - 1, (short) var6 - var8 - 1);
                        this.p(1, var2, var3);
                        this.transRect();
                        var5 = var4[0];
                        var6 = var4[1];
                        var7 = var5 >> 16;
                        var8 = (short) var5;
                        this.primary2.drawRect(var7, var8, (var6 >> 16) - var7 - 1, (short) var6 - var8 - 1);
                    }
            }
        } catch (Throwable var9) {
            var9.printStackTrace();
        }

    }

    public void dScroll(MouseEvent event, int var2, int var3) {
        if (var2 == 0 && var3 == 0) {
            int var4 = event.getID();
            Point var5 = event.getPoint();
            if (var4 != MOUSE_RELEASED && var4 != MOUSE_DRAGGED) {
                if (var4 == MOUSE_PRESSED) {
                    this.poS = var5;
                }
            } else {
                this.scroll(this.isSpace ? this.poS.x - var5.x : var5.x - this.poS.x, this.isSpace ? this.poS.y - var5.y : var5.y - this.poS.y);
                this.poS = var5;
            }

        } else {
            this.scroll(var2, var3);
        }
    }

    public void dText(int eventId, int var2, int var3) {
        switch (eventId) {
            case MOUSE_RELEASED:
                this.setM();
                if (this.text == null) {
                    this.text = new TextField(16);
                    this.text.addActionListener(this);
                    this.isText = true;
                    this.getParent().add(this.text, 0);
                }

                if (!this.isText) {
                    this.primary.setColor(new Color(this.mgInfo.iColor));
                    this.primary.fillRect(var2 - 1, var3 - 1, this.mgInfo.iSize + 1, 1);
                    this.primary.fillRect(var2 - 1, var3, 1, this.mgInfo.iSize);
                } else {
                    this.text.setFont(this.m.getFont(this.m.iSize * this.info.scale / this.info.Q));
                    this.text.setSize(this.text.getPreferredSize());
                    Point var4 = this.getLocation();
                    this.text.setLocation(var2 + var4.x, var3 + var4.y + 2);
                    this.text.setVisible(true);
                }

                this.p(0, var2, var3);
            case MOUSE_PRESSED:
            default:
        }
    }

    private void ePre() {
        this.dPre(this.oldX, this.oldY, false);
        this.oldX = -1000;
        this.oldY = -1000;
    }

    private final int getS() {
        try {
            return this.tab != null && this.tab != this ? (Integer) this.mGet.invoke(this.tab, (Object[]) null) : 255;
        } catch (Throwable var1) {
            this.tab = this;
            return 255;
        }
    }

    private final boolean in(int var1, int var2) {
        Dimension var3 = this.getSize();
        return var1 >= 0 && var2 >= 0 && var1 < var3.width && var2 < var3.height;
    }

    public void init(Applet app, Res res, int var3, int var4, int var5, int var6, Cursor[] var7) throws IOException {
        String var8 = "color_";
        this.cursors = var7;
        this.cls = new Color[6];
        this.cls[0] = new Color(res.getP(var8 + "frame", 0x505078));
        this.cls[1] = new Color(res.getP(var8 + "icon", 0xCCCCFF));
        this.cls[2] = new Color(res.getP(var8 + "bar_hl", 0xFFFFFF));
        this.cls[3] = new Color(res.getP(var8 + "bar", 0x6F6FAE));
        this.cls[4] = new Color(res.getP(var8 + "bar_hl", 0xEEEEFF));
        this.cls[5] = new Color(res.getP(var8 + "bar_shadow", 0xAAAAAA));
        this.setBackground(Color.white);
        this.m = new M();
        this.user = this.m.newUser(this);
        M.Info info = this.m.newInfo(app, this, res);
        info.setSize(var3, var4, var5);
        info.setL(var6);
        this.info = info;
        this.mgInfo = info.m;

        try {
            Method var10 = this.getClass().getMethod("setFocusTraversalKeys", Integer.TYPE, Class.forName("java.util.Set"));
            var10.invoke(this, 0, Class.forName("java.util.Collections").getField("EMPTY_SET").get((Object) null));
        } catch (Throwable var11) {
            var11.printStackTrace();
        }

    }

    private final boolean isOKPo(int var1, int var2) {
        int var3 = this.ps[this.psCount - 1];
        return Math.max(Math.abs(var1 - (var3 >> 16)), Math.abs(var2 - (short) var3)) >= this.info.scale;
    }

    public void m_paint(int var1, int var2, int var3, int var4) {
        Dimension var5 = this.info.getSize();
        if (this.m == null) {
            this.primary.setColor(Color.white);
            this.primary.fillRect(0, 0, var5.width, var5.height);
        } else {
            if (var1 == 0 && var2 == 0 && var3 == 0 && var4 == 0) {
                var2 = 0;
                var1 = 0;
                var3 = var5.width;
                var4 = var5.height;
            } else {
                var1 = Math.max(var1, 0);
                var2 = Math.max(var2, 0);
                var3 = Math.min(var3, var5.width);
                var4 = Math.min(var4, var5.height);
            }

            this.m.m_paint(var1, var2, var3, var4);
        }
    }

    public void m_paint(Rectangle var1) {
        if (var1 == null) {
            this.m_paint(0, 0, 0, 0);
        } else {
            this.m_paint(var1.x, var1.y, var1.width, var1.height);
        }

    }

    private void mPaint(Graphics var1) {
        if (var1 == null) {
            var1 = this.primary;
        }

        this.drawScroll(var1);
        if (super.isPaint) {
            this.m_paint(var1 == this.primary ? null : var1.getClipBounds());
        }
    }

    private final void p(int var1, int var2, int var3) {
        this.ps[var1] = var2 << 16 | var3 & '\uffff';
    }

    public void paint2(Graphics var1) {
        try {
            Rectangle var2 = var1.getClipBounds();
            int var3 = (this.info == null ? 0 : this.info.scale) * 2;
            var1.setClip(var2.x - var3, var2.y - var3, var2.width + var3 * 2, var2.height + var3 * 2);
            this.mPaint(var1);
        } catch (Throwable var4) {
            var4.printStackTrace();
        }

    }

    public void pMouse(MouseEvent ev) {
        try {
            int id = ev.getID();
            int scale = this.info.scale;
            int sX = ev.getX() / scale * scale;
            int sY = ev.getY() / scale * scale;
            boolean isMPress   = id == MOUSE_PRESSED;
            boolean isMRelease = id == MOUSE_RELEASED;
            boolean isMDrag    = id == MouseEvent.MOUSE_DRAGGED;
            boolean isMRight   = this.isRight;
            if (ev.isAltDown() && ev.isControlDown()) {
                if (this.psCount >= 0) {
                    this.reset();
                }

                if (isMPress) {
                    this.poS.y = sY;
                    this.poS.x = this.mgInfo.iSize;
                    this.m_paint((Rectangle) null);
                }

                if (isMDrag) {
                    Dimension var9 = this.getSize();
                    int var10 = var9.width / 2;
                    int var11 = var9.height / 2;
                    int var12 = this.info.getPenSize(this.mgInfo) * this.info.scale;
                    this.m_paint(var10 - var12, var11 - var12, var12 * 2, var12 * 2);
                    this.imi.setLineSize((sY - this.poS.y) / 4 + this.poS.x);
                    this.dPre(var10, var11, false);
                }

                return;
            }

            if (isMPress) {
                isMRight = this.isRight = Awt.isR(ev);
                if (!this.isDrag) {
                    this.dPre(this.oldX, this.oldY, false);
                }

                this.isDrag = true;
                if (this.b(sX, sY)) {
                    return;
                }

                if (this.isText && this.text != null && this.text.isVisible()) {
                    this.text.setVisible(false);
                }
            }

            if (isMRelease) {
                if (!this.isDrag) {
                    return;
                }

                this.isRight = false;
                this.isDrag = false;
                super.isPaint = true;
                if (this.isScroll) {
                    this.isScroll = false;
                    this.imi.scroll(false, 0, 0);
                    if (this.info.scale < 1) {
                        this.m_paint((Rectangle) null);
                    }

                    return;
                }
            }

            if (this.isRight && this.isDrag) {
                if (this.psCount >= 0) {
                    this.reset();
                    this.isRight = false;
                    this.isDrag = false;
                    super.isPaint = true;
                } else {
                    this.imi.setARGB(this.user.getPixel(sX / this.info.scale + this.info.scaleX, sY / this.info.scale + this.info.scaleY) & 0xFFFFFF | this.info.m.iAlpha << 24);
                }

                return;
            }

            if (!this.isDrag) {
                this.cursor(id, sX, sY);
                switch (id) {
                    case MOUSE_MOVED:
                        this.dPre(sX, sY, this.isIn);
                        this.isIn = true;
                        break;
                    case MOUSE_ENTERED:
                        if (this.text == null || !this.text.isVisible()) {
                            this.requestFocus();
                        }

                        this.getS();
                        break;
                    case MOUSE_EXITED:
                        if (this.isIn) {
                            this.isIn = false;
                            this.dPre(this.oldX, this.oldY, false);
                        }
                }
            }

            if (this.isScroll) {
                this.dScroll(ev, 0, 0);
                return;
            }

            if (this.isEnable && ((long) (this.mgInfo.iLayer + 1) & this.info.permission) != 0L && !isMRight && (this.mgInfo.iHint == 10 || this.info.layers[this.mgInfo.iLayer].iAlpha > 0.0F)) {
                switch (this.mgInfo.iHint) {
                    case M.H_FLINE:
                    case M.H_SP:
                        this.dFLine(id, sX, sY);
                        break;
                    case M.H_LINE:
                        this.dLine(id, sX, sY);
                        break;
                    case M.H_BEZI:
                        this.dBz(id, sX, sY);
                        break;
                    case M.H_RECT:
                    case M.H_FRECT:
                    case M.H_OVAL:
                    case M.H_FOVAL:
                    default:
                        this.dRect(id, sX, sY);
                        break;
                    case M.H_FILL:
                        if (isMPress && this.info.isFill) {
                            this.m.set(this.info.m);
                            this.p(0, sX, sY);
                            this.p(1, sX + 1024, sY + 1024);
                            this.transRect();
                            this.m.setRetouch(this.user.points, (byte[]) null, 0, true);
                            this.m.draw();
                            this.send();
                        }
                        break;
                    case M.H_TEXT:
                    case M.H_VTEXT:
                        this.dText(id, sX, sY);
                        break;
                    case M.H_COPY:
                        this.dCopy(id, sX, sY);
                        break;
                    case M.H_CLEAR:
                        if (this.info.isClean && isMPress) {
                            this.dClear();
                        }
                }
            }

            if (id == MOUSE_RELEASED && this.isIn) {
                this.dPre(sX, sY, false);
                this.isDrag = false;
            }
        } catch (Throwable var13) {
            var13.printStackTrace();
        }

    }

    private final void poll() {
        if (this.tab != null && this.tab != this) {
            try {
                if ((Boolean) this.mPoll.invoke(this.tab, (Object[]) null)) {
                    return;
                }
            } catch (Throwable var2) {
                var2.printStackTrace();
            }

            this.mgInfo.iSOB = 0;
        }
    }

    protected void processEvent(AWTEvent event) {
        try {
            int eventId = event.getID();
            if (event instanceof KeyEvent) {
                KeyEvent keyEvent = (KeyEvent) event;
                boolean isCtrlOrShiftDown = keyEvent.isControlDown() || keyEvent.isShiftDown();
                boolean isAltDown = keyEvent.isAltDown();
                boolean isUndo = true; // if false it'll do a redo
                keyEvent.consume();
                label56:
                switch (eventId) {
                    case 401: // ADJUSTMENT_EVENT_MASK | ACTION_EVENT_MASK | MOUSE_EVENT_MASK | COMPONENT_EVENT_MASK
                        switch (keyEvent.getKeyCode()) {
                            case 9:
                                this.isVTool = !this.isVTool;
                                this.tBox.mVisible(this.isVTool);
                                break label56;
                            case VK_SPACE:
                                this.isSpace = true;
                                break label56;
                            case VK_LEFT:
                                this.scroll(-5, 0);
                                break label56;
                            case VK_UP:
                                this.scroll(0, -5);
                                break label56;
                            case VK_RIGHT:
                                this.scroll(5, 0);
                                break label56;
                            case VK_DOWN:
                                this.scroll(0, 5);
                                break label56;
                            case VK_B:
                                this.dPre(this.oldX, this.oldY, false);
                                this.tBox.selPix(false);
                                this.dPre(this.oldX, this.oldY, false);
                                break label56;
                            case VK_E:
                                this.dPre(this.oldX, this.oldY, false);
                                this.tBox.selPix(true);
                                this.dPre(this.oldX, this.oldY, false);
                                break label56;
                            case VK_R:
                            case VK_Y:
                                isUndo = false;
                            case VK_Z:
                                if (isAltDown) {
                                    isUndo = false;
                                }

                                if (isCtrlOrShiftDown) {
                                    this.imi.undo(isUndo);
                                }
                                break label56;
                            case VK_ADD:
                                this.scaleChange(1, false);
                                break label56;
                            case VK_SUBTRACT:
                                this.scaleChange(-1, false);
                            default:
                                break label56;
                        }
                    case 402: // ADJUSTMENT_EVENT_MASK | ACTION_EVENT_MASK | MOUSE_EVENT_MASK | CONTAINER_EVENT_MASK
                        switch (keyEvent.getKeyCode()) {
                            case VK_SPACE:
                                this.isSpace = false;
                        }
                }
            } else if (super.isGUI) {
                switch (eventId) {
                    case 101: // WINDOW_EVENT_MASK | MOUSE_MOTION_EVENT_MASK | FOCUS_EVENT_MASK | COMPONENT_EVENT_MASK
                    case 102: // WINDOW_EVENT_MASK | MOUSE_MOTION_EVENT_MASK | FOCUS_EVENT_MASK | CONTAINER_EVENT_MASK
                        this.resetGraphics();
                        this.repaint();
                }
            }
        } catch (Throwable var7) {
            var7.printStackTrace();
        }

        super.processEvent(event);
    }

    public void reset() {
        if (this.psCount >= 0) {
            this.psCount = -1;
            switch (this.mgInfo.iHint) {
                case M.H_FLINE:
                case M.H_SP:
                    this.m.reset(true);
                    break;
                default:
                    this.m.reset(false);
                    this.m_paint((Rectangle) null);
            }
        }

    }

    public synchronized void resetGraphics() {
        if (this.primary != null) {
            this.primary.dispose();
        }

        if (this.primary2 != null) {
            this.primary2.dispose();
        }

        Dimension var1 = this.getSize();
        int var2 = var1.width - this.sizeBar;
        int var3 = var1.height - this.sizeBar;
        this.primary = this.getGraphics();
        if (this.primary != null) {
            this.primary.translate(this.getGapX(), this.getGapY());
            this.primary2 = this.primary.create(0, 0, var2, var3);
            this.primary2.setXORMode(Color.white);
            this.info.setComponent(this, this.primary, var2, var3);
        }

        int[] var4 = this.rS;
        int var5 = this.sizeBar;
        var1 = this.info.getSize();

        for (int var6 = 0; var6 < 20; ++var6) {
            var4[var6] = var5;
        }

        var4[1] = var1.height;
        var4[2] = var1.width - var5;
        var4[4] = var1.width;
        var4[7] = var1.height - var5;
        var4[8] = 0;
        var4[9] = var1.height;
        var4[12] = var1.width;
        var4[13] = var1.height;
        var4[16] = var1.width;
        var4[17] = 0;
    }

    public void scaleChange(int var1, boolean var2) {
        if (this.isIn) {
            this.ePre();
        }

        boolean var3 = this.info.addScale(var1, var2);
        float var4 = (float) this.info.scale;
        int var5 = (int) ((float) this.info.imW * var4) + this.sizeBar;
        int var6 = (int) ((float) this.info.imH * var4) + this.sizeBar;
        if (var3) {
            if (super.isGUI) {
                this.setSize(var5, var6);
            } else {
                Dimension var7 = this.getSize();
                int var8 = var7.width;
                int var9 = var7.height;
                if (var5 != var7.width || var6 != var7.height) {
                    this.setSize(var5, var6);
                }

                var7 = this.getSize();
                if (var7.width == var8 && var7.height == var9) {
                    this.mPaint((Graphics) null);
                } else {
                    this.imi.changeSize();
                }
            }
        }

    }

    public void scroll(int var1, int var2) {
        if (this.info != null) {
            Dimension var3 = this.info.getSize();
            int var4 = this.info.imW;
            int var5 = this.info.imH;
            float var6 = (float) this.info.scale;
            int var7 = this.info.scaleX;
            int var8 = this.info.scaleY;
            float var9 = (float) var1 * ((float) var4 / (float) var3.width);
            if (var6 < 1.0F) {
                var9 /= var6;
            }

            if (var9 != 0.0F) {
                var9 = var9 >= 0.0F && var9 <= 1.0F ? 1.0F : (var9 <= 0.0F && var9 >= -1.0F ? -1.0F : var9);
            }

            int var10 = (int) var9;
            var9 = (float) var2 * ((float) var5 / (float) var3.height);
            if (var9 != 0.0F) {
                var9 = var9 >= 0.0F && var9 <= 1.0F ? 1.0F : (var9 <= 0.0F && var9 >= -1.0F ? -1.0F : var9);
            }

            if (var6 < 1.0F) {
                var9 /= var6;
            }

            int var11 = (int) var9;
            Graphics var12 = this.primary;
            this.info.scaleX = Math.max(var7 + var10, 0);
            this.info.scaleY = Math.max(var8 + var11, 0);
            this.drawScroll(var12);
            this.poS.translate(var1, var2);
            int var13 = (int) ((float) (this.info.scaleX - var7) * var6);
            int var14 = (int) ((float) (this.info.scaleY - var8) * var6);
            var10 = var3.width - Math.abs(var13);
            var11 = var3.height - Math.abs(var14);

            try {
                var12.copyArea(Math.max(var13, 0), Math.max(var14, 0), var10, var11, -var13, -var14);
                if (var6 >= 1.0F) {
                    if (var13 != 0) {
                        if (var13 > 0) {
                            this.m_paint(var3.width - var13, 0, var13, var3.height);
                        } else {
                            this.m_paint(0, 0, -var13, var3.height);
                        }
                    }

                    if (var14 != 0) {
                        if (var14 > 0) {
                            this.m_paint(0, var3.height - var14, var3.width, var14);
                        } else {
                            this.m_paint(0, 0, var3.width, -var14);
                        }
                    }
                } else {
                    if (var13 != 0) {
                        if (var13 > 0) {
                            var12.clearRect(var3.width - var13, 0, var13, var3.height);
                        } else {
                            var12.clearRect(0, 0, -var13, var3.height);
                        }
                    }

                    if (var14 != 0) {
                        if (var13 > 0) {
                            var12.clearRect(0, var3.height - var14, var3.width, var14);
                        } else {
                            var12.clearRect(0, 0, var3.width, -var14);
                        }
                    }
                }

                this.imi.scroll(true, var13, var14);
            } catch (Throwable var16) {
                var16.printStackTrace();
            }

        }
    }

    private void send() {
        this.imi.send(this.m);
    }

    public void send(String var1) {
        try {
            M mg = new M(this.info, this.user);
            mg.set(var1);
            mg.draw();
            this.send(mg);
        } catch (Throwable var3) {
            ;
        }

    }

    public void send(M var1) throws InterruptedException {
        this.imi.send(var1);
    }

    public void setA() {
        this.m.iAlpha2 = (int) (this.info.layers[this.m.iLayer].iAlpha * 255.0F) << 8 | (int) (this.info.layers[this.m.iLayerSrc].iAlpha * 255.0F);
    }

    private final void setM() {
        this.m.set(this.mgInfo);
        if (this.m.iPen == M.P_FUSION) {
            this.m.iLayerSrc = this.m.iLayer;
        }

        this.setA();
    }

    public void setSize(Dimension var1) {
        this.setSize(var1.width, var1.height);
    }

    public void setSize(int var1, int var2) {
        if (this.info == null) {
            super.setSize(var1, var2);
        } else {
            int var3 = this.info.imW * this.info.scale + this.sizeBar;
            int var4 = this.info.imH * this.info.scale + this.sizeBar;
            super.setSize(Math.min(var1 - this.getGapW(), var3), Math.min(var2 - this.getGapW(), var4));
        }

        this.repaint();
    }

    public final boolean transRect() {
        int var1 = this.ps[0];
        int var2 = var1 >> 16;
        short var3 = (short) var1;
        var1 = this.ps[1];
        int var4 = var1 >> 16;
        short var5 = (short) var1;
        int var6 = Math.max(Math.min(var2, var4), 0);
        int var7 = Math.min(Math.max(var2, var4), this.info.imW * this.info.scale);
        int var8 = Math.max(Math.min(var3, var5), 0);
        int var9 = Math.min(Math.max(var3, var5), this.info.imH * this.info.scale);
        if (var7 - var6 >= this.info.scale && var9 - var8 >= this.info.scale) {
            this.user.points[0] = var6 << 16 | var8 & '\uffff';
            this.user.points[1] = var7 << 16 | var9 & '\uffff';
            return true;
        } else {
            return false;
        }
    }

    public void up() {
        if (this.tab != null) {
            this.tab.repaint();
        }

    }
}
