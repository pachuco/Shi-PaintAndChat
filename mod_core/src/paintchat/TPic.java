package paintchat;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;

import static java.awt.event.MouseEvent.*;

import syi.awt.Awt;
import syi.awt.LComponent;

public class TPic extends LComponent implements SW {
    private ToolBox ts;
    private M.Info info;
    private M.User user;
    private M mg;
    private Res r_conf;
    private int iDrag;
    private int lastMask = -1;
    private Color[] cls;
    private static float[] fhsb = new float[3];

    private Image cMk() {
        int[] var1 = this.user.getBuffer();
        byte var2 = 64;
        int var5 = 0;
        float var7 = 0.0F;
        float var8 = 0.0F;
        float var9 = 1.0F / (float) var2;
        float var6 = fhsb[0];

        for (int var4 = 0; var4 < var2; ++var4) {
            var7 = 1.0F;

            for (int var3 = 0; var3 < var2; ++var3) {
                var1[var5++] = Color.HSBtoRGB(var6, var7 -= var9, var8);
            }

            var8 += var9;
        }

        return this.user.mkImage(var2, var2);
    }

    private Image cMkB() {
        int[] var1 = this.user.getBuffer();
        int var2 = (int) (64.0F * LComponent.Q);
        int var3 = (int) (22.0F * LComponent.Q);
        int[] var4 = var1;
        int var5 = 0;
        float var8 = 0.0F;
        float var9 = 1.0F / (float) var2;

        for (int var10 = 0; var10 < var2; ++var10) {
            int var6 = Color.HSBtoRGB(var8, 1.0F, 1.0F);

            for (int var7 = 0; var7 < var3; ++var7) {
                var4[var5++] = var6;
            }

            var8 += var9;
        }

        return this.user.mkImage(var3, var2);
    }

    private int getRGB() {
        return Color.HSBtoRGB(fhsb[0], fhsb[1], fhsb[2]) & 0xFFFFFF;
    }

    public void lift() {
    }

    public void mPack() {
        this.inParent();
    }

    public void mPaint() {
        try {
            Graphics g = this.getG();
            if (g == null) return;

            this.mPaint(g);
            g.dispose();
        } catch (Throwable var2) {
            var2.printStackTrace();
        }

    }

    public void mPaint(Graphics g) {
        Dimension var2 = this.getSize();
        int var3 = (int) (22.0F * LComponent.Q);
        int var4 = var3;
        byte var5 = 0;
        byte var6 = 0;
        int var7 = var2.width - var3 - 1;
        int var8 = var2.height - var3 - 1;
        int[] var9 = this.user.getBuffer();
        int var14;
        synchronized (var9) {
            Image var11 = this.cMk();
            g.drawImage(var11, var5, var6, var7, var8, Color.white, (ImageObserver) null);
            var11 = this.cMkB();
            g.drawImage(var11, var7 + 1, var6, var4, var8, Color.white, (ImageObserver) null);
            var14 = var6 + var8;
        }

        Awt.drawFrame(g, false, var5, var14 + 1, var7, var3);
        int var10 = (int) ((float) (var7 - 8) * 0.7F);
        this.lastMask = this.mg.iColorMask;
        g.setColor(new Color(this.lastMask));
        g.fillRect(var5 + var10 + 6, var14 + 4, (int) ((float) (var7 - 8) * 0.3F), var3 - 6);
        g.setColor(Color.getHSBColor(fhsb[0], fhsb[1], fhsb[2]));
        g.fillRect(var5 + 3, var14 + 4, var10, var3 - 6);
        g.setColor(Color.blue);
        g.setXORMode(Color.white);
        int var15 = Math.max((int) (10.0F * LComponent.Q), 2);
        int var12 = var15 >>> 1;
        g.setClip(var7 + 1, 0, var3, var8);
        g.drawOval(var7 + 1 + var3 / 2 - var12, (int) ((float) var8 * fhsb[0]) - var12, var15, var15);
        g.setClip(0, 0, var7, var8);
        g.drawOval((int) ((float) var7 * (1.0F - fhsb[1])) - var12, (int) ((float) var8 * fhsb[2]) - var12, var15, var15);
        g.setPaintMode();
        g.setClip(0, 0, var2.width, var2.height);
    }

    public void mSetup(ToolBox var1, M.Info var2, M.User var3, M var4, Res var5, Res var6) {
        this.ts = var1;
        this.info = var2;
        this.user = var3;
        this.mg = var4;
        this.r_conf = var6;
        this.setTitle(var6.getP("window_4"));
        this.setDimension(new Dimension((int) (66.0F * LComponent.Q), (int) (66.0F * LComponent.Q)), new Dimension((int) (128.0F * LComponent.Q), (int) (128.0F * LComponent.Q)), new Dimension((int) (284.0F * LComponent.Q), (int) (284.0F * LComponent.Q)));
    }

    public void paint2(Graphics g) {
        this.mPaint(g);
    }

    public void pMouse(MouseEvent me) {
        int meX = me.getX();
        int meY = me.getY();
        int var4 = (int) (22.0F * LComponent.Q);
        int var5 = (int) (25.0F * LComponent.Q);
        boolean var6 = false;
        Dimension var7 = this.getSize();
        int var8 = var7.width - var5 - 1;
        int var9 = var7.height - var4 - 1;
        if (me.getID() == MOUSE_PRESSED && meY > var9) {
            int var10 = (int) ((float) (var8 - 8) * 0.7F);
            if (meX > var10) {
                this.mg.iColorMask = this.mg.iColor;
                this.ts.up();
            }

        } else {
            meX = meX <= 0 ? 0 : (meX >= var8 ? var8 : meX);
            meY = meY <= 0 ? 0 : (meY >= var9 ? var9 : meY);
            switch (me.getID()) {
                case MOUSE_PRESSED:
                    this.iDrag = meX < var8 ? 0 : 1;
                    var6 = true;
                    break;
                case MOUSE_RELEASED:
                    if (this.iDrag >= 0) {
                        var6 = true;
                        this.iDrag = -1;
                    }
                case MOUSE_MOVED:
                case MOUSE_ENTERED:
                case MOUSE_EXITED:
                default:
                    break;
                case MOUSE_DRAGGED:
                    var6 = this.iDrag >= 0;
            }

            if (var6 && this.iDrag >= 0) {
                if (this.iDrag == 0) {
                    fhsb[1] = 1.0F - (float) meX / (float) var8;
                    fhsb[2] = (float) meY / (float) var9;
                } else {
                    fhsb[0] = (float) meY / (float) var9;
                }

                this.ts.setARGB(this.mg.iAlpha << 24 | this.getRGB());
                this.mPaint();
            }

        }
    }

    public void setColor(int var1) {
        Color.RGBtoHSB(var1 >>> 16 & 255, var1 >>> 8 & 255, var1 & 255, fhsb);
        this.mPaint();
    }

    public void up() {
        int var1 = this.getRGB();
        int var2 = this.mg.iColor;
        if (var1 != var2 || this.lastMask != this.mg.iColorMask) {
            this.setColor(var2);
        }

    }
}
