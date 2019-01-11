package paintchat.pro;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;

import syi.awt.Awt;
import syi.awt.LComponent;

public class TPic extends LComponent {
    private Tools tools;
    public Component tColor;
    private int iDrag = -1;
    private int sizePalette = 20;
    private int selPalette = 0;
    private int oldColor = 0;
    private Color[] cls;
    private int isRGB = 1;
    private int iColor;
    private static float[] fhsb = new float[3];

    public TPic(Tools var1) {
        this.setDimension(new Dimension((int) (66.0F * LComponent.Q), (int) (66.0F * LComponent.Q)), new Dimension((int) (128.0F * LComponent.Q), (int) (128.0F * LComponent.Q)), new Dimension((int) (284.0F * LComponent.Q), (int) (284.0F * LComponent.Q)));
        this.tools = var1;
    }

    private Image cMk() {
        int[] var1 = this.tools.iBuffer;
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

        return this.tools.mkImage(var2, var2);
    }

    private Image cMkB() {
        int[] var1 = this.tools.iBuffer;
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

        return this.tools.mkImage(var3, var2);
    }

    private int getRGB() {
        return Color.HSBtoRGB(fhsb[0], fhsb[1], fhsb[2]);
    }

    public void mPaint() {
        try {
            Graphics g = this.getG();
            if (g == null) return;

            this.mPaint(g);
            g.dispose();
        } catch (RuntimeException var2) {
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
        int var13;
        synchronized (this.tools.iBuffer) {
            Image var10 = this.cMk();
            g.drawImage(var10, var5, var6, var7, var8, Color.white, (ImageObserver) null);
            var10 = this.cMkB();
            g.drawImage(var10, var7 + 1, var6, var4, var8, Color.white, (ImageObserver) null);
            var13 = var6 + var8;
        }

        Awt.drawFrame(g, false, var5, var13 + 1, var7, var3);
        int var14 = (int) ((float) (var7 - 8) * 0.7F);
        g.setColor(new Color(this.tools.info.m.iColorMask));
        g.fillRect(var5 + var14 + 6, var13 + 4, (int) ((float) (var7 - 8) * 0.3F), var3 - 6);
        g.setColor(Color.getHSBColor(fhsb[0], fhsb[1], fhsb[2]));
        g.fillRect(var5 + 3, var13 + 4, var14, var3 - 6);
        g.setColor(Color.blue);
        g.setXORMode(Color.white);
        int var15 = Math.max((int) (10.0F * LComponent.Q), 2);
        int var11 = var15 >>> 1;
        g.setClip(var7 + 1, 0, var3, var8);
        g.drawOval(var7 + 1 + var3 / 2 - var11, (int) ((float) var8 * fhsb[0]) - var11, var15, var15);
        g.setClip(0, 0, var7, var8);
        g.drawOval((int) ((float) var7 * (1.0F - fhsb[1])) - var11, (int) ((float) var8 * fhsb[2]) - var11, var15, var15);
        g.setPaintMode();
        g.setClip(0, 0, var2.width, var2.height);
    }

    public void paint2(Graphics g) {
        this.mPaint(g);
    }

    public void pMouse(MouseEvent event) {
        int mouseX = event.getX();
        int mouseY = event.getY();
        int var4 = (int) (22.0F * LComponent.Q);
        int var5 = (int) (25.0F * LComponent.Q);
        boolean var6 = false;
        Dimension var7 = this.getSize();
        int var8 = var7.width - var5 - 1;
        int var9 = var7.height - var4 - 1;
        boolean var10 = (event.getModifiers() & 4) != 0 || event.isShiftDown() || event.isControlDown();
        if (event.getID() == MouseEvent.MOUSE_PRESSED && mouseY > var9) {
            int var11 = (int) ((float) (var8 - 8) * 0.7F);
            if (mouseX > var11) {
                this.tools.setMask(this, this.getRGB(), mouseX, mouseY, var10);
            }

        } else {
            mouseX = mouseX <= 0 ? 0 : (mouseX >= var8 ? var8 : mouseX);
            mouseY = mouseY <= 0 ? 0 : (mouseY >= var9 ? var9 : mouseY);
            switch (event.getID()) {
                case MouseEvent.MOUSE_PRESSED:
                    this.iDrag = mouseX < var8 ? 0 : 1;
                    var6 = true;
                    break;
                case MouseEvent.MOUSE_RELEASED:
                    if (this.iDrag >= 0) {
                        var6 = true;
                        this.iDrag = -1;
                        this.tools.setRGB(Color.HSBtoRGB(fhsb[0], fhsb[1], fhsb[2]));
                    }
                case MouseEvent.MOUSE_MOVED:
                case MouseEvent.MOUSE_ENTERED:
                case MouseEvent.MOUSE_EXITED:
                default:
                    break;
                case MouseEvent.MOUSE_DRAGGED:
                    var6 = this.iDrag >= 0;
            }

            if (var6 && this.iDrag >= 0) {
                if (this.iDrag == 0) {
                    fhsb[1] = 1.0F - (float) mouseX / (float) var8;
                    fhsb[2] = (float) mouseY / (float) var9;
                } else {
                    fhsb[0] = (float) mouseY / (float) var9;
                }

                this.mPaint();
            }

        }
    }

    public void setColor(int var1) {
        Color.RGBtoHSB(var1 >>> 16 & 255, var1 >>> 8 & 255, var1 & 255, fhsb);
        this.mPaint();
    }
}
