package syi.awt;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.lang.reflect.Method;

import paintchat.M;
import static paintchat.M.*;

public class Tab extends LComponent {
    private M mg;
    private int iDrag = -1;
    private int sizeBar;
    private int max = 0;
    private int strange;
    private Object tab;
    private Method mGet;
    private Method mPoll;
    private Method mEx;
    private byte iSOB;
    private final String[] STR = new String[]{"alpha", "size"};

    public Tab(Container var1, M.Info var2) throws Throwable {
        try {
            this.mg = var2.m;
            int var3 = this.sizeBar = (int) (16.0F * LComponent.Q);
            Dimension var4 = this.getSize();
            var4.setSize(var3 * 4, 8 + var3 * 6);
            this.setDimension(var4, var4, var4);
            Class var5 = Class.forName("cello.tablet.JTablet");
            this.tab = var5.newInstance();
            this.mGet = var5.getMethod("getPressure", (Class[]) null);
            this.mPoll = var5.getMethod("poll", (Class[]) null);
            this.mEx = var5.getMethod("getPressureExtent", (Class[]) null);
            this.setTitle("tablet");
            var1.add(this, 0);
        } catch (Throwable var6) {
            var6.printStackTrace();
        }

    }

    private int at(int var1) {
        if (var1 > this.getSize().height / 2) {
            var1 -= 5;
        }

        return var1 / this.sizeBar;
    }

    private void dBar(Graphics var1, int var2) {
        Dimension var3 = this.getSize();
        int var4 = this.sizeBar;
        int var5 = var2 * (var3.height / 2) + var4;
        float var7 = (float) (var3.width - 6) / 255.0F;

        for (int var8 = 0; var8 < 2; ++var8) {
            int var9 = var2 == 0 ? this.mg.iSA : this.mg.iSS;
            var1.setColor(super.clFrame);
            var1.drawRect(2, var5, (int) (var7 * 255.0F), var4);
            int var6 = (int) ((float) (var9 >>> var8 * 8 & 255) * var7);
            var1.setColor(this.getForeground());
            var1.fillRect(3, var5 + 1, var6, var4 - 1);
            var1.setColor(this.getBackground());
            var1.fillRect(var6 + 3, var5 + 1, var3.width - var6 - 7, var4 - 1);
            var5 += var4;
        }

    }

    private void drag(int var1) {
        int var2 = this.iDrag;
        if (var2 > 0 && var2 != 3) {
            boolean var3 = var2 >= 3;
            if ((this.iSOB & M.F1_ALL_LAYERS << (!var3 ? 0 : 1)) != 0) {
                var1 = (int) (255.0F / (float) this.getSize().width * (float) var1);
                var1 = var1 <= 0 ? 0 : (var1 >= 255 ? 255 : var1);
                int var4;
                if (var3) {
                    var4 = (var2 - 4) * 8;
                    this.mg.iSS = this.mg.iSS & 255 << 8 - var4 | var1 << var4;
                } else {
                    var4 = (var2 - 1) * 8;
                    this.mg.iSA = this.mg.iSA & 255 << 8 - var4 | var1 << var4;
                }

                Graphics var5 = this.getG();
                this.dBar(var5, var2 < 3 ? 0 : 1);
                var5.dispose();
            }
        }
    }

    public void paint2(Graphics g) {
        try {
            int sizeBar = this.sizeBar;
            Dimension dim = this.getSize();
            int x2 = dim.width - 1;
            int y2 = sizeBar * 3 + 4;

            for (int i = 0; i < 2; ++i) {
                boolean var9 = (this.iSOB & i + 1) != 0;
                int y = y2 * i;
                Awt.fillFrame(g, !var9, 0, y, x2, y2);
                Awt.fillFrame(g, var9, 0, y, sizeBar, sizeBar);
                g.setColor(this.getForeground());
                g.drawString(this.STR[i] + '.' + (var9 ? "On" : "Off"), sizeBar + 2, y + sizeBar - 2);
                this.dBar(g, i);
            }
        } catch (Throwable var10) {
            var10.printStackTrace();
        }

    }

    public void pMouse(MouseEvent var1) {
        int var2 = var1.getX();
        int var3 = var1.getY();
        int var4 = this.sizeBar;
        switch (var1.getID()) {
            case 501:
                if (this.iDrag < 0) {
                    int var5 = this.at(var3);
                    this.iDrag = var5;
                    if ((var5 == 0 || var5 == 3) && var2 <= var4) {
                        this.iSOB = (byte) (this.iSOB ^ M.F1_ALL_LAYERS << (var5 == 0 ? 0 : 1));
                        this.repaint();
                    } else {
                        this.drag(var2);
                    }
                }
                break;
            case 502:
                this.iDrag = -1;
            case 503:
            case 504:
            case 505:
            default:
                break;
            case 506:
                this.drag(var2);
        }

    }

    public final boolean poll() {
        if (this.iSOB == 0) {
            return false;
        } else {
            try {
                if ((Boolean) this.mPoll.invoke(this.tab, (Object[]) null)) {
                    this.mg.iSOB = this.iSOB;
                    if (this.max <= 0) {
                        this.max = (Integer) this.mEx.invoke(this.tab, (Object[]) null);
                        if (this.max != 0) {
                            this.mEx = null;
                        }
                    }

                    return true;
                }
            } catch (Throwable var1) {
                ;
            }

            return false;
        }
    }

    public final int strange() {
        try {
            if (this.poll()) {
                this.strange = (int) ((float) (Integer) this.mGet.invoke(this.tab, (Object[]) null) / (float) this.max * 255.0F);
            } else {
                this.strange = 0;
            }
        } catch (Throwable var2) {
            var2.printStackTrace();
        }

        return this.strange;
    }
}
