package paintchat;

import jaba.applet.Applet;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;

import syi.awt.Awt;
import syi.util.ByteStream;

import static syi.C.EngineMg.*;

// Currently only used by PCHViewer when loading the older PCH format, similar to M
public class Mg {
    private Mg.Info info;
    private Mg.User user;
    public int iHint = H_FLINE;
    public int iPen = P_SOLID;
    public int iPenM = PM_PEN;
    public int iTT = 0; // selected texture index
    public int iColor = 0;
    public int iColorMask = 0;
    public int iAlpha = 255;
    public int iAlpha2;
    public int iSA = 0xFF00; // 2 bytes, max opacity - min opacity
    public int iLayer = 0;
    public int iLayerSrc = 1;
    public int iMask = M_N;
    public int iSize = 0;
    public int iSS = 0xFF00; // 2 bytes, max size - min size
    public int iCount = DEF_COUNT;
    public int iSOB;
    public boolean isAFix;
    public boolean isOver;
    public boolean isCount = true;
    public boolean isAnti;
    public boolean isAllL;
    public byte[] strHint; // fontname-[BOLD|ITALIC]-
    private int iSeek;
    private int iOffset;
    private byte[] offset;
    private static float[] b255 = new float[256];
    private static float[] b255d = new float[256];
    private static ColorModel color_model = null;
    private static final Mg mgDef = new Mg();

    public Mg() {
    }

    public Mg(Mg.Info var1, Mg.User var2) {
        this.info = var1;
        this.user = var2;
    }

    private final void ch(int var1, int var2) {
        int[][] var3 = this.info.getOffset();
        int[] var4 = var3[var1];
        int[] var5 = var3[var2];
        int var6 = this.info.W * this.info.H;

        for (int var8 = 0; var8 < var6; ++var8) {
            int var7 = var5[var8];
            var5[var8] = var4[var8];
            var4[var8] = var7;
        }

    }

    private final void copy(int[][] var1, int[][] var2) {
        for (int var3 = 0; var3 < var2.length; ++var3) {
            System.arraycopy(var1[var3], 0, var2[var3], 0, var2[var3].length);
        }

    }

    public final void dBuffer() {
        this.dBuffer(!this.user.isDirect, this.user.dX, this.user.dY, this.user.dW, this.user.dH);
    }

    private final void dBuffer(boolean isDirect, int x1, int y1, int x2, int y2) {
        try {
            int scale = this.info.scale;
            int quality = this.info.Q;
            int w = this.info.W;
            int h = this.info.H;
            int scaleX = this.info.scaleX;
            int scaleY = this.info.scaleY;
            boolean isUnscaled = scale == 1;
            int[] var14 = this.user.buffer;
            Color colorWhite = Color.white;
            Graphics g = this.info.g;
            if (g == null) {
                return;
            }

            x1 /= quality;
            y1 /= quality;
            x2 /= quality;
            y2 /= quality;
            x1 = x1 <= scaleX ? scaleX : x1;
            y1 = y1 <= scaleY ? scaleY : y1;
            int var12 = this.info.vWidth / scale + scaleX;
            x2 = x2 > var12 ? var12 : x2;
            x2 = x2 > w ? w : x2;
            var12 = this.info.vHeight / scale + scaleY;
            y2 = y2 > var12 ? var12 : y2;
            y2 = y2 > h ? h : y2;
            if (x2 <= x1 || y2 <= y1) {
                return;
            }

            w = x2 - x1;
            int var17 = w * scale;
            int var18 = (x1 - scaleX) * scale;
            int var19 = y1;
            var12 = var14.length / (w * quality * quality);

            while (true) {
                h = Math.min(var12, y2 - var19);
                if (h <= 0) {
                    break;
                }

                Image var20 = isDirect ? this.mkMPic(x1, var19, w, h, quality) : this.mkLPic((int[]) null, x1, var19, w, h, quality);
                if (isUnscaled) {
                    g.drawImage(var20, var18, var19 - scaleY, colorWhite, (ImageObserver) null);
                } else {
                    g.drawImage(var20, var18, (var19 - scaleY) * scale, var17, h * scale, colorWhite, (ImageObserver) null);
                }

                var19 += h;
            }
        } catch (RuntimeException ex) {
            ex.printStackTrace();
        }

    }

    /** Draws a bezier */
    private final void dBz(int[] points) throws InterruptedException {
        try {
            int point = points[0];
            int distXY = 0;


            for (int i = 1; i < 4; ++i) {
                distXY += Math.abs((points[i] >> 16) - (point >> 16)) + Math.abs((short) points[i] - (short) point);
                point = points[i];
            }

            if (distXY <= 0) {
                return;
            }

            int var10 = -1000;
            int var11 = -1000;
            int var12 = 0;
            boolean isAntialias = this.isAnti;
            int brushRadius = this.user.pW / 2;

            for (int i = distXY; i > 0; --i) {
                float var5 = (float) i / (float) distXY;
                float var4 = (float) Math.pow((double) (1.0F - var5), 3.0D);
                float pointX = var4 * (float) (points[3] >> 16);
                float pointY = var4 * (float) ((short) points[3]);
                var4 = 3.0F * (1.0F - var5) * (1.0F - var5) * var5;
                pointX += var4 * (float) (points[2] >> 16);
                pointY += var4 * (float) ((short) points[2]);
                var4 = 3.0F * var5 * var5 * (1.0F - var5);
                pointX += var4 * (float) (points[1] >> 16);
                pointY += var4 * (float) ((short) points[1]);
                var4 = var5 * var5 * var5;
                pointX += var4 * (float) (points[0] >> 16);
                pointY += var4 * (float) ((short) points[0]);
                int var8 = (int) pointX + brushRadius;
                int var17 = (int) pointY + brushRadius;
                if (var8 != var10 || var17 != var11) {
                    if (isAntialias) {
                        this.shift(var8, var17);
                        ++var12;
                        if (var12 >= 4) {
                            this.dFLine2(this.iSize);
                        }
                    } else {
                        this.dFLine(var8, var17, this.iSize);
                    }

                    var10 = var8;
                    var11 = var17;
                }
            }

            this.user.dX = this.user.dX - 1;
            this.user.dY = this.user.dY - 1;
            this.user.dW = this.user.dW + 2;
            this.user.dH = this.user.dH + 2;
        } catch (RuntimeException ex) {
            ex.printStackTrace();
        }

    }

    /** Clears all the layers */
    public void dClear(boolean var1) {
        if (var1) {
            this.getWork();
        }

        int var2 = this.info.W;
        int var3 = this.info.H;
        int[][] var4 = this.info.iOffs;
        int[] var5 = var4[0];
        int var6 = 0xFFFFFF;
        int var7 = var2 * var3;
        synchronized (var4) {
            int var9;
            for (var9 = 0; var9 < var2; ++var9) {
                var5[var9] = var6;
            }

            var9 = var2;

            while (true) {
                if (var9 >= var7) {
                    for (var9 = 1; var9 < var4.length; ++var9) {
                        System.arraycopy(var5, 0, var4[var9], 0, var5.length);
                    }
                    break;
                }

                System.arraycopy(var5, 0, var5, var9, var2);
                var9 += var2;
            }
        }

        this.user.isDirect = true;
        this.setD(0, 0, var2, var3);
        if (this.user.wait >= 0) {
            this.dBuffer();
        }

    }

    /** Copies a portion of the layer at a new position*/
    private void dCopy(int[] points) {
        int width = this.info.W;
        int height = this.info.H;
        int point = points[0];
        int x1 = point >> 16;
        int y1 = (short) point;
        point = points[1];
        int x2 = (point >> 16) - x1;
        int y2 = (short) point - y1;
        point = points[2];
        int destX = point >> 16;
        short destY = (short) point;
        if (x1 < 0) {
            x2 -= x1;
            x1 = 0;
        }

        if (x1 + x2 > width) {
            x1 = width - x2;
        }

        if (y1 < 0) {
            y2 -= y1;
            y1 = 0;
        }

        if (y1 + y2 > height) {
            y1 = height - y2;
        }

        if (destX < 0) {
            x1 -= destX;
            x2 += destX;
            destX = 0;
        }

        if (destY < 0) {
            y1 -= destY;
            y2 += destY;
            destY = 0;
        }

        if (destX + x2 >= width) {
            x2 = width - destX;
        }

        if (destY + y2 >= height) {
            y2 = height - destY;
        }

        if (x2 > 0 && y2 > 0 && destX < width && destY < height) {
            int[] var11 = x2 * y2 <= this.user.buffer.length ? this.user.buffer : new int[x2 * y2];
            int[] var12 = this.info.iOffs[this.iLayerSrc];

            for (int i = 0; i < y2; ++i) {
                System.arraycopy(var12, (y1 + i) * width + x1, var11, x2 * i, x2);
            }

            var12 = this.info.iOffs[this.iLayer];

            for (int i = 0; i < y2; ++i) {
                System.arraycopy(var11, x2 * i, var12, (destY + i) * width + destX, x2);
            }

            this.setD(destX, destY, destX + x2, destY + y2);
        }
    }

    public final void dEnd() throws InterruptedException {
        if (!this.user.isDirect) {
            this.dFlush();
        }

        ByteStream bs = this.info.workOut;
        if (bs.size() > 0) {
            this.offset = bs.writeTo(this.offset, 0);
            this.iOffset = bs.size();
        }

        if (this.user.wait == -1) {
            this.dBuffer();
        }

    }

    /** This is the fill used when drawing filled ovals */
    private void dFill(byte[] var1, int var2, int var3, int var4, int var5) {
        byte alpha = (byte) this.iAlpha;
        int width = this.info.W;

        try {
            for (int i = var4 - var2; var3 < var5; ++var3) {
                int var8 = var3 * width + var2;

                int var11;
                for (var11 = 0; var11 < i && var1[var8] != alpha; ++var11) {
                    ++var8;
                }

                while (var11 < i && var1[var8] == alpha) {
                    ++var8;
                    ++var11;
                }

                int var9 = var8;
                if (var11 < i) {
                    while (var11 < i && var1[var8] != alpha) {
                        ++var8;
                        ++var11;
                    }

                    int var10 = var8;
                    if (var11 < i) {
                        while (var9 < var10) {
                            var1[var9] = alpha;
                            ++var9;
                        }
                    }
                }
            }
        } catch (RuntimeException ex) {
            System.out.println(ex);
        }

    }

    /** This is the fill called by newInfo() */
    private void dFill(int[] var1, int var2, int var3, int var4, int var5) {
        int alpha = this.iAlpha;
        int width = this.info.W;

        try {
            for (int var11 = var4 - var2; var3 < var5; ++var3) {
                int var8 = var3 * width + var2;

                int var12;
                for (var12 = var8 + var11; var8 < var12 && var1[var8] != alpha; ++var8) {
                }

                if (var8 < var12 - 1) {
                    ++var8;

                    while (var8 < var12 && var1[var8] == alpha) {
                        ++var8;
                    }

                    if (var8 < var12 - 1) {
                        int var9;
                        for (var9 = var8++; var8 < var12 && var1[var8] != alpha; ++var8) {
                        }

                        if (var8 < var12) {
                            for (int var10 = var8; var9 < var10; ++var9) {
                                var1[var9] = alpha;
                            }
                        }
                    }
                }
            }
        } catch (RuntimeException ex) {
            System.out.println(ex);
        }

    }

    private void dFill(int x, int y) {
        int width = this.info.W;
        int height = this.info.H;
        byte alpha = (byte) this.iAlpha;
        byte[] var6 = this.info.iMOffs;

        try {
            int[] var7 = this.user.buffer;
            byte var8 = 0;
            if (x < 0 || x >= width || y < 0 || y >= height) {
                return;
            }

            int pixel = this.pix(x, y);
            int color = this.iAlpha << 24 | this.iColor;
            if (pixel == color) {
                return;
            }

            int var16 = var8 + 1;
            var7[var8] = this.s(pixel, x, y) << 16 | y;

            while (var16 > 0) {
                --var16;
                int var9 = var7[var16];
                x = var9 >>> 16;
                y = var9 & 0xFFFF;
                int var10 = width * y;
                boolean var11 = false;
                boolean var12 = false;

                while (true) {
                    var6[var10 + x] = alpha;
                    if (y > 0 && this.pix(x, y - 1) == pixel && var6[var10 - width + x] == 0) {
                        if (!var11) {
                            var11 = true;
                            var7[var16++] = this.s(pixel, x, y - 1) << 16 | y - 1;
                        }
                    } else {
                        var11 = false;
                    }

                    if (y < height - 1 && this.pix(x, y + 1) == pixel && var6[var10 + width + x] == 0) {
                        if (!var12) {
                            var12 = true;
                            var7[var16++] = this.s(pixel, x, y + 1) << 16 | y + 1;
                        }
                    } else {
                        var12 = false;
                    }

                    if (x <= 0 || this.pix(x - 1, y) != pixel || var6[var10 + x - 1] != 0) {
                        break;
                    }

                    --x;
                }
            }
        } catch (RuntimeException ex) {
            System.out.println(ex);
        }

        this.setD(0, 0, width, height);
        this.t();
    }

    /** Draws a freehand line with antialiasing */
    private final void dFLine(float var1, float var2, int var3) throws InterruptedException {
        int var4 = this.user.wait;
        float var5 = this.user.fX;
        float var6 = this.user.fY;
        float var7 = var1 - var5;
        float var8 = var2 - var6;
        float var9 = Math.max(Math.abs(var7), Math.abs(var8));
        int var12 = (int) var5;
        int var13 = (int) var6;
        int var14 = this.user.oX;
        int var15 = this.user.oY;
        float var16 = 0.25F;
        if (!this.isCount) {
            this.user.count = 0;
        }

        int var19 = this.ss(var3);
        int var20 = this.sa(var3);
        int var21 = Math.max(var19, this.iSize);
        float var22 = (float) this.iSize;
        float var23 = (float) this.iAlpha;
        float var24 = var9 == 0.0F ? 0.0F : ((float) var19 - var22) / var9;
        var24 = var24 >= 1.0F ? 1.0F : (var24 <= -1.0F ? -1.0F : var24);
        float var25 = var9 == 0.0F ? 0.0F : ((float) var20 - var23) / var9;
        var25 = var25 >= 1.0F ? 1.0F : (var25 <= -1.0F ? -1.0F : var25);
        float var26 = var7 == 0.0F ? 0.0F : var7 / var9;
        float var27 = var8 == 0.0F ? 0.0F : var8 / var9;
        float var28 = var5;
        float var29 = var6;
        if (var9 <= 0.0F) {
            ++var9;
        }

        var26 *= var16;
        var27 *= var16;
        var24 *= var16;
        var25 *= var16;
        int var30 = (int) (var9 / var16);

        int var31;
        for (var31 = 0; var31 < var30; ++var31) {
            if (var14 != var12 || var15 != var13) {
                this.user.count = this.user.count - 1;
                var14 = var12;
                var15 = var13;
            }

            if (this.user.count <= 0) {
                this.user.count = this.user.countMax;
                this.iSize = (int) var22;
                this.iAlpha = (int) var23;
                this.getPM();
                int var17 = var12 - (this.user.pW >>> 1);
                int var18 = var13 - (this.user.pW >>> 1);
                float var10 = var28 - (float) ((int) var28);
                float var11 = var29 - (float) ((int) var29);
                if (var10 < 0.0F) {
                    --var17;
                    var10 = 1.0F - var10;
                }

                if (var11 < 0.0F) {
                    --var18;
                    var11 = 1.0F - var11;
                }

                if (var10 != 1.0F && var11 != 1.0F) {
                    this.dPen(var17, var18, (1.0F - var10) * (1.0F - var11));
                }

                if (var10 != 0.0F) {
                    this.dPen(var17 + 1, var18, var10 * (1.0F - var11));
                }

                if (var11 != 0.0F) {
                    this.dPen(var17, var18 + 1, (1.0F - var10) * var11);
                }

                if (var10 != 0.0F && var11 != 0.0F) {
                    this.dPen(var17 + 1, var18 + 1, var10 * var11);
                }

                if (var4 > 0) {
                    this.dBuffer(!this.user.isDirect, var17, var18, var17 + this.user.pW, var18 + this.user.pW);
                    if (var4 > 1) {
                        Thread.currentThread();
                        Thread.sleep((long) var4);
                    }
                }
            }

            var12 = (int) (var28 += var26);
            var13 = (int) (var29 += var27);
            var22 += var24;
            var23 += var25;
        }

        this.user.fX = var28;
        this.user.fY = var29;
        this.user.oX = var14;
        this.user.oY = var15;
        var31 = (int) Math.sqrt((double) this.info.bPen[this.iPenM][var21].length) / 2;
        int var32 = (int) Math.min(var5, var1) - var31;
        int var33 = (int) Math.min(var6, var2) - var31;
        int var34 = (int) Math.max(var5, var1) + var31 + this.info.Q + 1;
        int var35 = (int) Math.max(var6, var2) + var31 + this.info.Q + 1;
        if (var4 == 0) {
            this.dBuffer(!this.user.isDirect, var32, var33, var34, var35);
        }

        this.addD(var32, var33, var34, var35);
    }

    private final void dFLine(int var1, int var2, int var3) throws InterruptedException {
        int var4 = this.user.wait;
        int var5 = (int) this.user.fX;
        int var6 = (int) this.user.fY;
        int var7 = var1 - var5;
        int var8 = var2 - var6;
        int var9 = Math.max(Math.abs(var7), Math.abs(var8));
        int var10 = var5;
        int var11 = var6;
        int var12 = this.user.oX;
        int var13 = this.user.oY;
        if (!this.isCount) {
            this.user.count = 0;
        }

        int var16 = this.ss(var3);
        int var17 = this.sa(var3);
        int var18 = Math.max(var16, this.iSize);
        float var19 = (float) this.iSize;
        float var20 = (float) this.iAlpha;
        float var21 = var9 == 0 ? 0.0F : ((float) var16 - var19) / (float) var9;
        var21 = var21 >= 1.0F ? 1.0F : (var21 <= -1.0F ? -1.0F : var21);
        float var22 = var9 == 0 ? 0.0F : ((float) var17 - var20) / (float) var9;
        var22 = var22 >= 1.0F ? 1.0F : (var22 <= -1.0F ? -1.0F : var22);
        float var23 = var7 == 0 ? 0.0F : (float) var7 / (float) var9;
        float var24 = var8 == 0 ? 0.0F : (float) var8 / (float) var9;
        float var25 = (float) var5;
        float var26 = (float) var6;
        if (var9 <= 0) {
            ++var9;
        }

        int var27;
        for (var27 = 0; var27 < var9; ++var27) {
            if (var12 != var10 || var13 != var11) {
                this.user.count = this.user.count - 1;
                var12 = var10;
                var13 = var11;
                if (this.user.count <= 0) {
                    this.user.count = this.user.countMax;
                    this.iSize = (int) var19;
                    this.iAlpha = (int) var20;
                    this.getPM();
                    int var14 = var10 - (this.user.pW >>> 1);
                    int var15 = var11 - (this.user.pW >>> 1);
                    this.dPen(var14, var15, 1.0F);
                    if (var4 > 0) {
                        this.dBuffer(!this.user.isDirect, var14, var15, var14 + this.user.pW, var15 + this.user.pW);
                        if (var4 > 1) {
                            Thread.currentThread();
                            Thread.sleep((long) var4);
                        }
                    }
                }
            }

            var10 = (int) (var25 += var23);
            var11 = (int) (var26 += var24);
            var19 += var21;
            var20 += var22;
        }

        this.user.fX = var25;
        this.user.fY = var26;
        this.user.oX = var12;
        this.user.oY = var13;
        var27 = (int) Math.sqrt((double) this.info.bPen[this.iPenM][var18].length) / 2;
        int var28 = Math.min(var5, var10) - var27;
        int var29 = Math.min(var6, var11) - var27;
        int var30 = Math.max(var5, var10) + var27 + this.info.Q;
        int var31 = Math.max(var6, var11) + var27 + this.info.Q;
        if (var4 == 0) {
            this.dBuffer(!this.user.isDirect, var28, var29, var30, var31);
        }

        this.addD(var28, var29, var30, var31);
    }

    /** Draws a smoothed line by interpolating the last 4 positions */
    private final void dFLine2(int pressure) throws InterruptedException {
        try {
            int pX0 = this.user.pX[0];
            int pY0 = this.user.pY[0];
            int pX1 = this.user.pX[1];
            int pY1 = this.user.pY[1];
            int pX2 = this.user.pX[2];
            int pY2 = this.user.pY[2];
            int pX3 = this.user.pX[3];
            int pY3 = this.user.pY[3];
            boolean isAntialiased = this.isAnti;
            float fX = this.user.fX;
            float fY = this.user.fY;
            int iX = (int) fX;
            int iY = (int) fY;
            int var27 = iX;
            int var28 = iY;
            int oX = this.user.oX;
            int oY = this.user.oY;
            int waitWas = this.user.wait;
            if (!this.isCount) {
                this.user.count = 0;
            }

            int var34 = 2 * pX1;
            int var35 = 2 * pY1;
            int var36 = 2 * pX0 - 5 * pX1 + 4 * pX2 - pX3;
            int var37 = 2 * pY0 - 5 * pY1 + 4 * pY2 - pY3;
            int var38 = -pX0 + 3 * pX1 - 3 * pX2 + pX3;
            int var39 = -pY0 + 3 * pY1 - 3 * pY2 + pY3;
            float var40 = (float) this.iSize;
            float fAlpha255 = (float) this.iAlpha;
            int var42 = this.ss(pressure);
            int var43 = this.sa(pressure);
            float var44 = (float) (var42 - this.iSize) * 0.25F;
            var44 = var44 <= -1.5F ? -1.5F : (var44 >= 1.5F ? 1.5F : var44);
            float var45 = (float) (var43 - this.iAlpha) * 0.25F;
            int var48 = (int) Math.sqrt((double) Math.max(this.info.getPenMask()[this.iPenM][this.iSize].length, this.info.getPenMask()[this.iPenM][var42].length));
            int quality = this.info.Q;

            int var12;
            for (float var50 = 0.0F; var50 < 1.0F; var50 += 0.25F) {
                float var21 = var50 * var50;
                float var22 = var21 * var50;
                float var16 = 0.5F * ((float) var34 + (float) (-pX0 + pX2) * var50 + (float) var36 * var21 + (float) var38 * var22);
                float var17 = 0.5F * ((float) var35 + (float) (-pY0 + pY2) * var50 + (float) var37 * var21 + (float) var39 * var22);
                if (isAntialiased) {
                    var16 -= 0.5F;
                    var17 -= 0.5F;
                }

                float var18 = Math.max(Math.abs(var16 - fX), Math.abs(var17 - fY));
                if (var18 >= 1.0F) {
                    float var19 = (var16 - fX) / var18 * 0.25F;
                    var19 = var19 <= -1.0F ? -1.0F : (var19 >= 1.0F ? 1.0F : var19);
                    float var20 = (var17 - fY) / var18 * 0.25F;
                    var20 = var20 <= -1.0F ? -1.0F : (var20 >= 1.0F ? 1.0F : var20);
                    int var11 = (int) (var18 / 0.25F);
                    if (var11 < 16) {
                        var11 = 1;
                    }

                    float var46 = var44 / (float) var11;
                    float var47 = var45 / (float) var11;
                    iX = Math.min(Math.min((int) fX, (int) var16), iX);
                    iY = Math.min(Math.min((int) fY, (int) var17), iY);
                    var27 = Math.max(Math.max((int) fX, (int) var16), var27);
                    var28 = Math.max(Math.max((int) fY, (int) var17), var28);

                    for (int var10 = 0; var10 < var11; ++var10) {
                        int var32 = (int) fX;
                        int var33 = (int) fY;
                        if (oX == var32 && oY == var33) {
                            var40 += var46;
                            fAlpha255 += var47;
                        } else {
                            oX = var32;
                            oY = var33;
                            this.user.count = this.user.count - 1;
                        }

                        if (this.user.count > 0) {
                            fX += var19;
                            fY += var20;
                        } else {
                            this.iSize = (int) var40;
                            this.iAlpha = (int) fAlpha255;
                            this.getPM();
                            var12 = this.user.pW / 2;
                            var32 -= var12;
                            var33 -= var12;
                            this.user.count = this.user.countMax;
                            if (isAntialiased) {
                                float var23 = fX - (float) ((int) fX);
                                float var24 = fY - (float) ((int) fY);
                                if (var23 < 0.0F) {
                                    --var32;
                                    var23 = 1.0F - var23;
                                }

                                if (var24 < 0.0F) {
                                    --var33;
                                    var24 = 1.0F - var24;
                                }

                                if (var23 != 1.0F && var24 != 1.0F) {
                                    this.dPen(var32, var33, (1.0F - var23) * (1.0F - var24));
                                }

                                if (var23 != 0.0F) {
                                    this.dPen(var32 + 1, var33, var23 * (1.0F - var24));
                                }

                                if (var24 != 0.0F) {
                                    this.dPen(var32, var33 + 1, (1.0F - var23) * var24);
                                }

                                if (var23 != 0.0F && var24 != 0.0F) {
                                    this.dPen(var32 + 1, var33 + 1, var23 * var24);
                                }
                            } else {
                                this.dPen(var32, var33, 1.0F);
                            }

                            if (waitWas > 0) {
                                this.dBuffer(!this.user.isDirect, var32, var33, var32 + var12 * 2, var33 + var12 * 2);
                                if (waitWas > 1) {
                                    Thread.currentThread();
                                    Thread.sleep((long) waitWas);
                                }
                            }

                            fX += var19;
                            fY += var20;
                        }
                    }
                }
            }

            this.user.oX = oX;
            this.user.oY = oY;
            this.user.fX = fX;
            this.user.fY = fY;
            var12 = var48 / 2;
            iX -= var12;
            iY -= var12;
            var27 += var12 + 1;
            var28 += var12 + 1;
            this.addD(iX, iY, var27, var28);
            if (this.user.wait == 0) {
                this.dBuffer(!this.user.isDirect, iX, iY, var27 + quality, var28 + quality);
            }
        } catch (RuntimeException ex) {
            ex.printStackTrace();
        }

    }

    private final void dFlush() {
        int width = this.info.W;
        int height = this.info.H;
        int x = this.user.dX <= 0 ? 0 : this.user.dX;
        int y = this.user.dY <= 0 ? 0 : this.user.dY;
        int x2 = this.user.dW >= width ? width : this.user.dW;
        int y2 = this.user.dH >= height ? height : this.user.dH;
        byte[] var9 = this.info.iMOffs;
        int[] var10 = this.info.iOffs[this.iLayer];
        int var1;
        int var2;
        int var11;
        int var12;
        int var13;
        int var14;
        int var15;
        int[] var17;
        int var23;
        int var25;
        int var26;
        switch (this.iPen) {
            case P_MOSAIC:
                var11 = this.iAlpha / 10 + 1;
                x = x / var11 * var11;
                y = y / var11 * var11;
                int[] var28 = this.user.argb;

                for (var12 = y; var12 < y2; var12 += var11) {
                    for (var2 = x; var2 < x2; var2 += var11) {
                        var15 = Math.min(var11, width - var2);
                        var23 = Math.min(var11, height - var12);

                        int var24;
                        for (var24 = 0; var24 < 4; ++var24) {
                            var28[var24] = 0;
                        }

                        var25 = 0;

                        for (var14 = 0; var14 < var23; ++var14) {
                            for (var13 = 0; var13 < var15; ++var13) {
                                var26 = this.pix(var2 + var13, var12 + var14);
                                var1 = (var12 + var14) * width + var2 + var13;

                                for (var24 = 0; var24 < 4; ++var24) {
                                    var28[var24] += var26 >>> var24 * 8 & 255;
                                }

                                ++var25;
                            }
                        }

                        var26 = var28[3] << 24 | var28[2] / var25 << 16 | var28[1] / var25 << 8 | var28[0] / var25;

                        for (var14 = var12; var14 < var12 + var23; ++var14) {
                            var1 = width * var14 + var2;

                            for (var13 = 0; var13 < var15; ++var13) {
                                if (var9[var1] != 0) {
                                    var9[var1] = 0;
                                    var10[var1] = var26;
                                }

                                ++var1;
                            }
                        }
                    }
                }

                if (this.user.wait >= 0) {
                    this.dBuffer(true, x, y, x2, y2);
                }
                break;
            case P_FILL:
            case P_LPEN:
            case P_UNKNOWN12:
            case P_UNKNOWN13:
            case P_NULL:
            case P_UNKNOWN15:
            case P_UNKNOWN16:
            default:
                while (y < y2) {
                    var1 = y * width + x;

                    for (var2 = x; var2 < x2; ++var2) {
                        var10[var1] = this.getM(var10[var1], var9[var1] & 255, var1);
                        var9[var1] = 0;
                        ++var1;
                    }

                    ++y;
                }
                break;
            case P_LR:
                for (var13 = (x2 - x) / 2 + 1; y < y2; ++y) {
                    var1 = y * width + x;
                    var12 = var1 + (x2 - x) - 1;

                    for (var2 = 0; var2 < var13; ++var2) {
                        var11 = var10[var1];
                        var10[var1] = var10[var12];
                        var10[var12] = var11;
                        var9[var1] = var9[var12] = 0;
                        ++var1;
                        --var12;
                        if (var1 > var12) {
                            break;
                        }
                    }
                }

                return;
            case P_UD:
                for (var13 = (y2 - y) / 2; x < x2; ++x) {
                    var1 = y * width + x;
                    var12 = var1 + (y2 - y - 1) * width;

                    for (var2 = 0; var2 < var13; ++var2) {
                        var11 = var10[var1];
                        var10[var1] = var10[var12];
                        var10[var12] = var11;
                        var9[var1] = var9[var12] = 0;
                        var1 += width;
                        var12 -= width;
                    }
                }

                return;
            case P_R:
                var12 = x2 - x;
                var13 = y2 - y;
                var14 = y * width + x;
                var15 = var12 * var13;
                var17 = new int[var15];

                for (var23 = 0; var23 < var13; ++var23) {
                    System.arraycopy(var10, var14 + width * var23, var17, var12 * var23, var12);
                }

                for (var23 = 0; var23 < var12; ++var23) {
                    var10[x + var23] = 0xFFFFFF;
                    var9[var14 + var23] = 0;
                }

                for (var23 = 1; var23 < var13; ++var23) {
                    System.arraycopy(var10, var14, var10, var14 + var23 * width, var12);
                    System.arraycopy(var9, var14, var9, var14 + var23 * width, var12);
                }

                boolean var22 = false;
                var15 = width * height;

                for (var25 = 0; var25 < var13; ++var25) {
                    var1 = var12 * var25;
                    var11 = var14 + var13 - var25;

                    for (var26 = 0; var26 < var12; ++var26) {
                        int var27 = var26 + x;
                        if (var27 <= width && var27 >= 0 && var11 < var15) {
                            var10[var11] = var17[var1];
                        }

                        var11 += width;
                        ++var1;
                    }
                }

                this.addD(x, y, x + Math.max(var12, var13), y + var12);
                break;
            case P_FUSION:
                if (this.iLayerSrc != this.iLayer) {
                    int[] var16 = this.info.iOffs[this.iLayerSrc];
                    var17 = var10;
                    int[] var18 = var16;
                    float var19 = b255[this.iAlpha2 >>> 8];
                    float var20 = b255[this.iAlpha2 & 255];
                    if (this.iLayer < this.iLayerSrc) {
                        var17 = var16;
                        var18 = var10;
                        var19 = var20;
                        var20 = b255[this.iAlpha2 >>> 8];
                    }

                    while (y < y2) {
                        var1 = y * width + x;

                        for (var2 = x; var2 < x2; ++var2) {
                            if (var9[var1] != 0) {
                                var13 = (int) ((float) (var17[var1] >>> 24) * var19);
                                var12 = (int) ((float) (var18[var1] >>> 24) * b255[255 - var13] * var20);
                                var11 = var17[var1];
                                var14 = var12 + var13;
                                if (var14 != 0) {
                                    var15 = var18[var1];
                                    float var21 = (float) var12 / (float) var14;
                                    var11 = var21 == 1.0F ? var15 : (var21 == 0.0F ? var11 : ((var11 & 16711680) + (int) ((float) ((var15 & 16711680) - (var11 & 16711680)) * var21) & 16711680) + ((var11 & '\uff00') + (int) ((float) ((var15 & '\uff00') - (var11 & '\uff00')) * var21) & '\uff00') + (var11 & 255) + (int) ((float) ((var15 & 255) - (var11 & 255)) * var21));
                                } else {
                                    var11 = 0xFFFFFF;
                                }

                                var10[var1] = var14 << 24 | var11 & 0xFFFFFF;
                                var16[var1] = 0xFFFFFF;
                                var9[var1] = 0;
                            }

                            ++var1;
                        }

                        ++y;
                    }
                }

                if (this.user.wait >= 0) {
                    this.dBuffer();
                }
        }

    }

    /** Draws next step of a stroke from buffer */
    private final boolean dNext() throws InterruptedException {
        if (this.iSeek >= this.iOffset) {
            return false;
        } else {
            int pointX = this.user.pX[3] + this.rPo();
            int pointY = this.user.pY[3] + this.rPo();
            int pressure = this.iSOB != 0 ? this.ru() : 0;
            this.shift(pointX, pointY);
            this.user.iDCount = this.user.iDCount + 1;
            if (this.iHint != H_SP) {
                if (this.isAnti) {
                    this.dFLine((float) pointX - 0.5F, (float) pointY - 0.5F, pressure);
                } else {
                    this.dFLine(pointX, pointY, pressure);
                }
            } else if (this.user.iDCount >= 2) {
                this.dFLine2(pressure);
            }

            return true;
        }
    }

    /** Draws next step of a stroke */
    public final void dNext(int x, int y, int pressure, int step) throws InterruptedException, IOException {
        int scale = this.info.scale;
        //this.user.pW;
        x = (x / scale + this.info.scaleX) * this.info.Q;
        y = (y / scale + this.info.scaleY) * this.info.Q;
        if (Math.abs(x - this.user.pX[3]) + Math.abs(y - this.user.pY[3]) >= step) {
            this.wPo(x - this.user.pX[3]);
            this.wPo(y - this.user.pY[3]);
            this.shift(x, y);
            this.user.iDCount = this.user.iDCount + 1;
            if (this.iSOB != 0) {
                this.info.workOut.write(pressure);
            }

            if (this.iHint == H_SP) {
                if (this.user.iDCount >= 2) {
                    this.dFLine2(pressure);
                }
            } else if (this.isAnti) {
                this.dFLine((float) x - 0.5F, (float) y - 0.5F, pressure);
            } else {
                this.dFLine(x, y, pressure);
            }

        }
    }

    private final void dPen(int var1, int var2, float var3) {
        if (this.iPen == P_SUISAI2) {
            this.dPY(var1, var2);
        } else {
            this.dPenM(var1, var2, var3);
            if (this.isOver) {
                this.dFlush();
            }

        }
    }

    private final void dPenM(int var1, int var2, float var3) {
        boolean var7 = false;
        int[] var10 = this.getPM();
        int var11 = this.info.W;
        int var12 = this.user.pW;
        int var13 = var12 * Math.max(-var2, 0) + Math.max(-var1, 0);
        int var14 = Math.min(var1 + var12, var11);
        int var15 = Math.min(var2 + var12, this.info.H);
        if (var14 > 0 && var15 > 0) {
            var1 = var1 <= 0 ? 0 : var1;
            var2 = var2 <= 0 ? 0 : var2;
            int[] var16 = this.info.iOffs[this.iLayer];
            byte[] var17 = this.info.iMOffs;

            for (int var5 = var2; var5 < var15; ++var5) {
                int var6 = var11 * var5 + var1;
                int var18 = var13;
                var13 += var12;

                for (int var4 = var1; var4 < var14; ++var4) {
                    if (this.isM(var16[var6])) {
                        ++var6;
                        ++var18;
                    } else {
                        int var8 = var17[var6] & 255;
                        int var9 = var10[var18++];
                        if (var9 == 0) {
                            ++var6;
                        } else {
                            switch (this.iPen) {
                                case P_PEN:
                                case P_FUSION:
                                    var17[var6++] = (byte) (var8 + (int) ((float) var9 * b255[255 - var8 >>> 1] * var3));
                                    break;
                                case P_SUISAI:
                                case P_SWHITE:
                                case P_LIGHT:
                                case P_DARK:
                                    if ((var9 = (int) ((float) var9 * this.getTT(var4, var5))) != 0) {
                                        var17[var6] = (byte) Math.min(var8 + Math.max((int) ((float) var9 * b255[255 - var8 >>> 2]), 1), 255);
                                    }

                                    ++var6;
                                    break;
                                default:
                                    var17[var6++] = (byte) Math.max((int) ((float) var9 * this.getTT(var4, var5)), var8);
                            }
                        }
                    }
                }
            }

        }
    }

    public void dPre(Graphics g, int[] var2) {
        try {
            int var4 = var2[0];
            int var5 = var2[1];
            int var6 = var2[2];
            int var7 = var2[3];
            int var3 = var2[0];
            int var8 = 0;

            int var9;
            for (var9 = 1; var9 < 4; ++var9) {
                var8 += Math.abs((var2[var9] >> 16) - (var3 >> 16)) + Math.abs((short) var2[var9] - (short) var3);
                var3 = var2[var9];
            }

            var8 /= 2;
            if (var8 <= 0) {
                return;
            }

            boolean var18 = true;
            boolean var10 = true;
            int var11 = var4 >> 16;
            int var12 = (short) var4;

            for (var3 = var8; var3 > 0; --var3) {
                float var14 = (float) var3 / (float) var8;
                float var13 = (float) Math.pow((double) (1.0F - var14), 3.0D);
                float var15 = var13 * (float) (var7 >> 16);
                float var16 = var13 * (float) ((short) var7);
                var13 = 3.0F * (1.0F - var14) * (1.0F - var14) * var14;
                var15 += var13 * (float) (var6 >> 16);
                var16 += var13 * (float) ((short) var6);
                var13 = 3.0F * var14 * var14 * (1.0F - var14);
                var15 += var13 * (float) (var5 >> 16);
                var16 += var13 * (float) ((short) var5);
                var13 = var14 * var14 * var14;
                var15 += var13 * (float) (var4 >> 16);
                var16 += var13 * (float) ((short) var4);
                var9 = (int) var15;
                int var19 = (int) var16;
                if (var9 != var11 || var19 != var12) {
                    g.fillRect(var9, var19, 1, 1);
                    var11 = var9;
                    var12 = var19;
                }
            }
        } catch (RuntimeException var17) {
            var17.printStackTrace();
        }

    }

    private final void dPY(int var1, int var2) {
        boolean var4 = false;
        int[] var6 = this.getPM();
        int var7 = this.info.W;
        int var8 = this.user.pW;
        int var9 = var8 * Math.max(-var2, 0) + Math.max(-var1, 0);
        int var10 = var9;
        int var11 = Math.min(var1 + var8, var7);
        int var12 = Math.min(var2 + var8, this.info.H);
        var1 = var1 <= 0 ? 0 : var1;
        var2 = var2 <= 0 ? 0 : var2;
        if (var11 - var1 > 0 && var12 - var2 > 0) {
            int[] var13 = this.info.iOffs[this.iLayer];
            int var14 = 0;
            int var19 = 0;
            int var20 = 0;
            int var21 = 0;
            int var22 = 0;

            int var3;
            int var23;
            int var25;
            int var26;
            int var29;
            for (var25 = var2; var25 < var12; ++var25) {
                var3 = var7 * var25 + var1;
                var29 = var10;
                var10 += var8;

                for (var26 = var1; var26 < var11; ++var26) {
                    int var10001 = var29++;
                    int var10000 = var6[var10001];
                    var10001 = var6[var10001];
                    if (var10000 != 0 && !this.isM(var23 = var13[var3++])) {
                        var19 += var23 >>> 24;
                        var20 += var23 >>> 16 & 255;
                        var21 += var23 >>> 8 & 255;
                        var22 += var23 & 255;
                        ++var14;
                    } else {
                        ++var3;
                    }
                }
            }

            if (var14 != 0) {
                var19 /= var14;
                var20 /= var14;
                var21 /= var14;
                var22 /= var14;
                if (this.iAlpha > 0) {
                    float var30 = b255[this.iAlpha] / 3.0F;
                    var26 = this.iColor >>> 16 & 255;
                    int var27 = this.iColor >>> 8 & 255;
                    int var28 = this.iColor & 255;
                    var19 = Math.max((int) ((float) var19 + (float) (255 - var19) * var30), 1);
                    var3 = (int) ((float) (var26 - var20) * var30);
                    var20 += var3 != 0 ? var3 : (var26 > var20 ? 1 : (var26 < var20 ? -1 : 0));
                    var3 = (int) ((float) (var27 - var21) * var30);
                    var21 += var3 != 0 ? var3 : (var27 > var21 ? 1 : (var27 < var21 ? -1 : 0));
                    var3 = (int) ((float) (var28 - var22) * var30);
                    var22 += var3 != 0 ? var3 : (var28 > var22 ? 1 : (var28 < var22 ? -1 : 0));
                }

                var10 = var9;

                for (var25 = var2; var25 < var12; ++var25) {
                    var3 = var7 * var25 + var1;
                    var29 = var10;
                    var10 += var8;

                    for (var26 = var1; var26 < var11; ++var26) {
                        int var5 = var6[var29++];
                        var23 = var13[var3];
                        float var24;
                        if (var5 != 0 && !this.isM(var23) && (var24 = this.getTT(var26, var25) * b255[var5]) != 0.0F) {
                            int var15 = var23 >>> 24;
                            int var16 = var23 >>> 16 & 255;
                            int var18 = var23 >>> 8 & 255;
                            int var17 = var23 & 255;
                            var14 = (int) ((float) (var19 - var15) * var24);
                            var15 += var14 != 0 ? var14 : (var19 > var15 ? 1 : (var19 < var15 ? -1 : 0));
                            var14 = (int) ((float) (var20 - var16) * var24);
                            var16 += var14 != 0 ? var14 : (var20 > var16 ? 1 : (var20 < var16 ? -1 : 0));
                            var14 = (int) ((float) (var21 - var18) * var24);
                            var18 += var14 != 0 ? var14 : (var21 > var18 ? 1 : (var21 < var18 ? -1 : 0));
                            var14 = (int) ((float) (var22 - var17) * var24);
                            var17 += var14 != 0 ? var14 : (var22 > var17 ? 1 : (var22 < var17 ? -1 : 0));
                            var13[var3++] = (var15 << 24) + (var16 << 16) + (var18 << 8) + var17;
                        } else {
                            ++var3;
                        }
                    }
                }

            }
        }
    }

    /** Performs a complete draw action, from stream or undo/redo */
    public final void draw() throws InterruptedException {
        try {
            if (this.info == null) {
                return;
            }

            this.iSeek = 0;
            label23:
            switch (this.iHint) {
                case H_FLINE:
                case H_LINE:
                case H_SP:
                    this.dStart();

                    while (true) {
                        if (!this.dNext()) {
                            break label23;
                        }
                    }
                case H_CLEAR:
                    this.dClear(false);
                    break;
                default:
                    this.dRetouch((int[]) null);
            }
        } catch (InterruptedException ex) {
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

        this.dEnd();
    }

    private void dRect(int x1, int y1, int x2, int y2) {
        int width = this.info.W;
        int height = this.info.H;
        byte[] var10 = this.info.iMOffs;
        int[] var12 = this.info.iOffs[this.iLayer];
        byte alpha = (byte) this.iAlpha;
        if (x1 < 0) {
            x1 = 0;
        }

        if (y1 < 0) {
            y1 = 0;
        }

        if (x2 > width) {
            x2 = width;
        }

        if (y2 > height) {
            y2 = height;
        }

        if (x1 < x2 && y1 < y2 && alpha != 0) {
            this.setD(x1, y1, x2, y2);
            int var7;
            int var9;
            int var13;
            int var14;
            int var15;
            int var16;
            int var17;
            int var18;
            label139:
            switch (this.iHint) {
                case H_RECT:
                    var13 = y1;

                    while (true) {
                        if (var13 >= y2) {
                            break label139;
                        }

                        var7 = var13 * width + x1;

                        for (var9 = x1; var9 < x2; ++var9) {
                            if (!this.isM(var12[var7])) {
                                var10[var7] = alpha;
                            }

                            ++var7;
                        }

                        ++var13;
                    }
                case H_FRECT:
                    var13 = x1;
                    var14 = y1;
                    var15 = x2;
                    var16 = y2;
                    var17 = 0;

                    while (true) {
                        if (var17 >= this.iSize + 1) {
                            break label139;
                        }

                        var7 = width * var14 + var13;
                        int var8 = width * (var16 - 1) + var13;

                        for (var9 = var13; var9 < var15; ++var9) {
                            if (!this.isM(var12[var7])) {
                                var10[var7] = alpha;
                            }

                            if (!this.isM(var12[var8])) {
                                var10[var8] = alpha;
                            }

                            ++var7;
                            ++var8;
                        }

                        var7 = width * var14 + var13;
                        var8 = width * var14 + var15 - 1;

                        for (var18 = var14; var18 < var16; ++var18) {
                            if (!this.isM(var12[var7])) {
                                var10[var7] = alpha;
                            }

                            if (!this.isM(var12[var8])) {
                                var10[var8] = alpha;
                            }

                            var7 += width;
                            var8 += width;
                        }

                        ++var13;
                        --var15;
                        ++var14;
                        --var16;
                        if (var15 <= var13 || var16 <= var14) {
                            break label139;
                        }

                        ++var17;
                    }
                case H_OVAL:
                case H_FOVAL:
                    var13 = x2 - x1 - 1;
                    var14 = y2 - y1 - 1;
                    var17 = var13 / 2;
                    var18 = var14 / 2;

                    for (float var19 = 0.0F; var19 < 8.0F; var19 = (float) ((double) var19 + 0.001D)) {
                        var15 = x1 + var17 + (int) Math.round(Math.cos((double) var19) * (double) var17);
                        var16 = y1 + var18 + (int) Math.round(Math.sin((double) var19) * (double) var18);
                        var10[width * var16 + var15] = alpha;
                    }

                    if (this.iHint == H_OVAL && var17 > 0 && var18 > 0) {
                        this.dFill(var10, x1, y1, x2, y2);
                    }
            }

            this.t();
        }
    }

    public void dRetouch(int[] var1) throws InterruptedException {
        this.dRetouch(var1, true);
    }

    public void dRetouch(int[] var1, boolean var2) throws InterruptedException {
        try {
            boolean var3 = var1 != null;
            byte var4 = 4;
            this.getPM();
            int brushRadius = this.user.pW / 2;
            this.user.setup(this);
            this.setD(0, 0, 0, 0);
            int[] points = this.user.points;
            int var7;
            int var9;
            int var13;
            int var18;
            if (var3) {
                if (var2) {
                    var7 = this.info.scale;
                    int var8 = this.info.Q;
                    int var11 = this.info.scaleX;
                    int var12 = this.info.scaleY;
                    var13 = this.iHint == 2 ? brushRadius : 0;

                    for (int var14 = 0; var14 < 4; ++var14) {
                        var9 = var1[var14] >> 16;
                        short var10 = (short) var1[var14];
                        var9 = (var9 / var7 + var11) * var8 - var13;
                        var18 = (var10 / var7 + var12) * var8 - var13;
                        points[var14] = var9 << 16 | var18 & 0xFFFF;
                    }
                } else {
                    System.arraycopy(var1, 0, points, 0, var1.length);
                }
            } else {
                var7 = 0;

                while (this.iSeek < this.iOffset) {
                    points[var7++] = (this.r2() & 0xFFFF) << 16 | this.r2() & '\uffff';
                    if (this.iHint == 8) {
                        break;
                    }
                }
            }

            var7 = points[0] >> 16;
            short var16 = (short) points[0];
            switch (this.iHint) {
                case H_BEZI:
                    var9 = this.user.wait;
                    this.user.wait = -2;
                    this.dStart(var7 + brushRadius, var16 + brushRadius, 0, false, false);
                    this.dBz(points);
                    this.user.wait = var9;
                    break;
                case 7:
                    this.dFill(var7, var16);
                    var4 = 1;
                    break;
                case 8:
                    String var21 = this.info.text;
                    String var20 = this.info.textOption;
                    if (!var3) {
                        String var22 = new String(this.offset, this.iSeek, this.iOffset - this.iSeek, "UTF8");
                        var13 = var22.indexOf(0);
                        var21 = var22.substring(var13 + 1);
                        var20 = var22.substring(0, var13);
                    }

                    this.dText(var21, var20, var7, var16);
                    var4 = 1;
                    break;
                case 9:
                    this.dCopy(points);
                    var4 = 3;
                    break;
                case 14:
                    int var10000 = this.info.W * this.info.H;
                    switch (var16) {
                        case 0:
                            this.ch(this.iLayerSrc, this.iLayer);
                            break;
                        case 1:
                            this.info.setL(points[1]);
                            break;
                        case 2:
                            this.info.delL(this.iLayerSrc);
                            break;
                        case 3:
                            if (this.iLayer > this.iLayerSrc) {
                                for (var18 = this.iLayerSrc; var18 < this.iLayer; ++var18) {
                                    this.ch(var18, var18 + 1);
                                }
                            }

                            if (this.iLayer < this.iLayerSrc) {
                                for (var18 = this.iLayerSrc; var18 > this.iLayer; --var18) {
                                    this.ch(var18, var18 - 1);
                                }
                            }
                            break;
                        case 4:
                            int[][] var19 = this.info.getOffset();
                            System.arraycopy(var19[this.iLayerSrc], 0, var19[this.iLayer], 0, var19[0].length);
                            break;
                        case 5:
                            this.info.visit[this.iLayerSrc] = (float) this.iAlpha / 255.0F;
                    }

                    this.setD(0, 0, this.info.W, this.info.H);
                    break;
                default:
                    this.dRect(var7, var16, points[1] >> 16, (short) points[1]);
                    var4 = 2;
            }

            if (var3) {
                ByteStream var17 = this.getWork();

                for (var18 = 0; var18 < var4; ++var18) {
                    var17.w((long) points[var18], 4);
                }

                if (this.iHint == 8) {
                    var17.write((this.info.textOption + '\u0000' + this.info.text).getBytes("UTF8"));
                }
            }

            if (this.user.wait >= 0) {
                this.dFlush();
                this.dBuffer();
                this.setD(0, 0, 0, 0);
                this.user.isDirect = true;
            } else {
                this.user.isDirect = false;
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

    }

    /** Starts drawing a brush stroke from buffer */
    private void dStart() {
        try {
            short pointX = this.r2();
            short pointY = this.r2();
            this.user.setup(this);
            int pressure = this.iSOB != 0 ? this.ru() : 0;
            if (this.iSOB != 0) {
                this.iSize = this.ss(pressure);
                this.iAlpha = this.sa(pressure);
            }

            this.memset(this.user.pX, pointX);
            this.memset(this.user.pY, pointY);
            int brushRadius = this.user.pW / 2;
            this.setD(pointX - brushRadius - 1, pointY - brushRadius - 1, pointX + brushRadius, pointY + brushRadius);
            this.user.fX = (float) pointX;
            this.user.fY = (float) pointY;
            if (this.isAnti) {
                Mg.User var10000 = this.user;
                var10000.fX = var10000.fX - 0.5F;
                var10000 = this.user;
                var10000.fY = var10000.fY - 0.5F;
            }

            if (this.iHint != H_SP && !this.isAnti) {
                this.dFLine(pointX, pointY, pressure);
            }
        } catch (RuntimeException ex) {
            ex.printStackTrace();
        } catch (InterruptedException ex) {
        }

    }

    /** Starts drawing */
    public void dStart(int x, int y, int pressure, boolean var4, boolean var5) {
        try {
            this.user.setup(this);
            this.iSize = this.ss(pressure);
            this.iAlpha = this.sa(pressure);
            this.user.setup(this);
            int var6;
            if (var5) {
                var6 = this.info.scale;
                x = (x / var6 + this.info.scaleX) * this.info.Q;
                y = (y / var6 + this.info.scaleY) * this.info.Q;
            }

            if (var4) {
                ByteStream var9 = this.getWork();
                var9.w((long) x, 2);
                var9.w((long) y, 2);
                if (this.iSOB != 0) {
                    var9.write(pressure);
                }
            }

            this.memset(this.user.pX, x);
            this.memset(this.user.pY, y);
            var6 = this.user.pW / 2;
            this.setD(x - var6 - 1, y - var6 - 1, x + var6, y + var6);
            this.user.fX = (float) x;
            this.user.fY = (float) y;
            if (this.isAnti) {
                Mg.User var10000 = this.user;
                var10000.fX = var10000.fX - 0.5F;
                var10000 = this.user;
                var10000.fY = var10000.fY - 0.5F;
            }

            if (this.iHint != H_SP && !this.isAnti) {
                this.dFLine(x, y, pressure);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

    }

    /** Draws text */
    private void dText(String text, String var2, int x, int y) {
        try {
            int var4 = this.info.W;
            int var5 = this.info.H;
            int[] var6 = this.info.iOffs[this.iLayer];
            byte[] var7 = this.info.iMOffs;
            float var8 = b255[this.iAlpha];
            if (var8 == 0.0F) {
                return;
            }

            if (text == null || text.length() <= 0) {
                return;
            }

            Font font = this.strHint != null ? Font.decode(new String(this.strHint, "UTF8")) : new Font("sansserif", 0, this.iSize);
            FontMetrics fontMetrics = this.info.component.getFontMetrics(font);
            int var12 = fontMetrics.stringWidth(text) + text.length();
            int var13 = fontMetrics.getMaxAscent() + fontMetrics.getMaxDescent() + 2;
            int var14 = var13 - fontMetrics.getMaxDescent();
            Image var15 = this.info.component.createImage(var12, var13);
            Graphics var16 = var15.getGraphics();
            var16.setFont(font);
            var16.setColor(Color.black);
            var16.fillRect(0, 0, var12, var13);
            var16.setColor(Color.blue);
            this.setD(x, y, x + var12, y + var13);
            var16.drawRect(0, 0, var12 - 1, var13 - 1);
            var16.drawString(text, fontMetrics.getLeading(), var14);
            var16.dispose();
            var16 = null;
            font = null;
            fontMetrics = null;
            PixelGrabber var17 = new PixelGrabber(var15, 0, 0, var12, var13, true);
            var17.grabPixels();
            int[] var18 = (int[]) var17.getPixels();
            var17 = null;
            var15.flush();
            var15 = null;
            boolean var19 = false;
            int var21 = Math.min(var4 - x, var12);
            int var22 = Math.min(var5 - y, var13);

            for (int var23 = 0; var23 < var22; ++var23) {
                int var26 = var23 * var12;
                int var20 = (var23 + y) * var4 + x;

                for (int var24 = 0; var24 < var21; ++var24) {
                    if (!this.isM(var6[var20])) {
                        var7[var20] = (byte) ((int) ((float) (var18[var26] & 255) * var8));
                    }

                    ++var26;
                    ++var20;
                }
            }

            this.setD(x, y, x + var12, y + var13);
            this.t();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /** Returns the alpha blended color */
    private final int fu(int pixel, int color, int alpha) {
        if (alpha == 0) {
            return pixel;
        } else {
            int pxAlpha = pixel >>> 24;
            int newAlpha = pxAlpha + (int) ((float) alpha * b255[255 - pxAlpha]);
            float fAlpha = b255[Math.min((int) ((float) alpha * b255d[newAlpha]), 255)];
            int r = pixel >>> 16 & 255;
            int g = pixel >>> 8 & 255;
            int b = pixel & 255;
            return newAlpha << 24 | r + (int) ((float) ((color >>> 16 & 255) - r) * fAlpha) << 16 | g + (int) ((float) ((color >>> 8 & 255) - g) * fAlpha) << 8 | b + (int) ((float) ((color & 255) - b) * fAlpha);
        }
    }

    public final void get(OutputStream out, ByteStream bs, Mg mg) {
        try {
            bs.reset();
            int var4 = 0;
            boolean shiftFlag = false; // Used to pack multiple flags in a single int
            int flags = this.getFlag(mg);
            int flags2 = flags >>> 8 & 255; // F2
            int flags3 = flags & 255;  // F3
            bs.write(flags >>> 16); // F1
            bs.write(flags2);
            bs.write(flags3);
            if ((flags2 & F2H) != 0) {
                var4 = this.iHint;
                shiftFlag = true;
            }

            if ((flags2 & F2PM) != 0) {
                if (shiftFlag) {
                    bs.write(var4 << 4 | this.iPenM);
                } else {
                    var4 = this.iPenM;
                }

                shiftFlag = !shiftFlag;
            }

            if ((flags2 & F2M) != 0) {
                if (shiftFlag) {
                    bs.write(var4 << 4 | this.iMask);
                } else {
                    var4 = this.iMask;
                }

                shiftFlag = !shiftFlag;
            }

            if (shiftFlag) {
                bs.write(var4 << 4);
            }

            if ((flags2 & F2P) != 0) {
                bs.write(this.iPen);
            }

            if ((flags2 & F2T) != 0) {
                bs.write(this.iTT);
            }

            if ((flags2 & F2L) != 0) {
                bs.write(this.iLayer);
            }

            if ((flags2 & F2LS) != 0) {
                bs.write(this.iLayerSrc);
            }

            if ((flags3 & F3A) != 0) {
                bs.write(this.iAlpha);
            }

            if ((flags3 & F3C) != 0) {
                bs.w((long) this.iColor, 3);
            }

            if ((flags3 & F3CM) != 0) {
                bs.w((long) this.iColorMask, 3);
            }

            if ((flags3 & F3S) != 0) {
                bs.write(this.iSize);
            }

            if ((flags3 & F3E) != 0) {
                bs.write(this.iCount);
            }

            if ((flags3 & F3SA) != 0) {
                bs.w((long) this.iSA, 2);
            }

            if ((flags3 & F3SS) != 0) {
                bs.w((long) this.iSS, 2);
            }

            if (this.iPen == P_FUSION) {
                bs.w2(this.iAlpha2);
            }

            if (this.iHint == H_TEXT) {
                if (this.strHint == null) {
                    bs.w2(0);
                } else {
                    bs.w2(this.strHint.length);
                    bs.write(this.strHint);
                }
            }

            if (this.offset != null && this.iOffset > 0) {
                bs.write(this.offset, 0, this.iOffset);
            }

            out.write(bs.size() >>> 8);
            out.write(bs.size() & 255);
            bs.writeTo(out);
        } catch (IOException var9) {
            var9.printStackTrace();
        } catch (RuntimeException var10) {
            var10.printStackTrace();
        }

    }

    private final int getFlag(Mg mg) {
        int flagGroup = 0;
        if (this.isAllL) {
            flagGroup |= F1_ALL_LAYERS;
        }

        if (this.isAFix) {
            flagGroup |= F1_AFIX;
        }

        if (this.isAnti) {
            flagGroup |= F1A;
        }

        if (this.isCount) {
            flagGroup |= F1C;
        }

        if (this.isOver) {
            flagGroup |= F1O;
        }

        flagGroup |= this.iSOB << 6; // F1S
        int flags = flagGroup << 16;
        if (mg == null) {
            return flags | 0xFFFF;
        } else {
            // F2 flags
            flagGroup = 0;
            if (this.iHint != mg.iHint) {
                flagGroup |= F2H;
            }

            if (this.iPenM != mg.iPenM) {
                flagGroup |= F2PM;
            }

            if (this.iMask != mg.iMask) {
                flagGroup |= F2M;
            }

            if (this.iPen != mg.iPen) {
                flagGroup |= F2P;
            }

            if (this.iTT != mg.iTT) {
                flagGroup |= F2T;
            }

            if (this.iLayer != mg.iLayer) {
                flagGroup |= F2L;
            }

            if (this.iLayerSrc != mg.iLayerSrc) {
                flagGroup |= F2LS;
            }

            flags |= flagGroup << 8;
            // F3 flags
            flagGroup = 0;
            if (this.iAlpha != mg.iAlpha) {
                flagGroup |= F3A;
            }

            if (this.iColor != mg.iColor) {
                flagGroup |= F3C;
            }

            if (this.iColorMask != mg.iColorMask) {
                flagGroup |= F3CM;
            }

            if (this.iSize != mg.iSize) {
                flagGroup |= F3S;
            }

            if (this.iCount != mg.iCount) {
                flagGroup |= F3E;
            }

            if (this.iSA != mg.iSA) {
                flagGroup |= F3SA;
            }

            if (this.iSS != mg.iSS) {
                flagGroup |= F3SS;
            }

            return flags | flagGroup;
        }
    }

    public Image getImage(int layerNumber, int offX, int offY, int width, int height) {
        offX = Math.round((float) (offX / this.info.scale)) + this.info.scaleX;
        offY = Math.round((float) (offY / this.info.scale)) + this.info.scaleY;
        width /= this.info.scale;
        height /= this.info.scale;
        int quality = this.info.Q;
        if (quality <= 1) {
            return this.info.component.createImage(new MemoryImageSource(width, height, this.info.iOffs[layerNumber], offY * this.info.W + offX, this.info.W));
        } else {
            Image img = this.info.component.createImage(new MemoryImageSource(width * quality, height * quality, this.info.iOffs[layerNumber], offY * quality * this.info.W + offX * quality, this.info.W));
            Image imgScaled = img.getScaledInstance(width, height, 2);
            img.flush();
            return imgScaled;
        }
    }

    private final int getM(int pixel, int alpha, int var3) {
        if (alpha == 0) {
            return pixel;
        } else {
            int pxG;
            int pxAlpha;
            int pxR;
            int pxB;
            float fAlpha;
            int var10000;
            switch (this.iPen) {
                case P_WHITE:
                case P_SWHITE:
                    pxAlpha = pixel >>> 24;
                    pxR = pxAlpha - (int) ((float) pxAlpha * b255[alpha]);
                    return pxR == 0 ? 0xFFFFFF : pxR << 24 | pixel & 0xFFFFFF;
                case P_LIGHT:
                    pxAlpha = pixel >>> 24;
                    pxR = pixel >>> 16 & 255;
                    pxG = pixel >>> 8 & 255;
                    pxB = pixel & 255;
                    var10000 = this.iColor;
                    fAlpha = b255[alpha];
                    return (pxAlpha << 24) + (Math.min(pxR + (int) ((float) pxR * fAlpha), 255) << 16) + (Math.min(pxG + (int) ((float) pxG * fAlpha), 255) << 8) + Math.min(pxB + (int) ((float) pxB * fAlpha), 255);
                case P_DARK:
                    pxAlpha = pixel >>> 24;
                    pxR = pixel >>> 16 & 255;
                    pxG = pixel >>> 8 & 255;
                    pxB = pixel & 255;
                    var10000 = this.iColor;
                    fAlpha = b255[alpha];
                    return (pxAlpha << 24) + (Math.max(pxR - (int) ((float) (255 - pxR) * fAlpha), 0) << 16) + (Math.max(pxG - (int) ((float) (255 - pxG) * fAlpha), 0) << 8) + Math.max(pxB - (int) ((float) (255 - pxB) * fAlpha), 0);
                case P_BOKASHI:
                    var10000 = pixel >>> 24;
                    var10000 = pixel >>> 16 & 255;
                    var10000 = pixel >>> 8 & 255;
                    var10000 = pixel & 255;
                    var10000 = this.iColor;
                    float var4 = b255[alpha];
                    int[] var5 = this.user.argb;
                    int[] var7 = this.info.iOffs[this.iLayer];
                    int var8 = this.info.W;

                    int i;
                    for (i = 0; i < 4; ++i) {
                        var5[i] = 0;
                    }

                    pxG = var3 % var8;
                    var3 += pxG == 0 ? 1 : (pxG == var8 - 1 ? -1 : 0);
                    var3 += var3 < var8 ? var8 : (var3 > var8 * (this.info.H - 1) ? -var8 : 0);

                    for (i = -1; i < 2; ++i) {
                        for (int var10 = -1; var10 < 2; ++var10) {
                            pxG = var7[var3 + var10 + i * var8];
                            var10000 = pxG >>> 24;

                            for (int var11 = 0; var11 < 4; ++var11) {
                                var5[var11] += pxG >>> (var11 << 3) & 255;
                            }
                        }
                    }

                    for (i = 0; i < 4; ++i) {
                        pxG = pixel >>> (i << 3) & 255;
                        var5[i] = pxG + (int) ((float) (var5[i] / 9 - pxG) * var4);
                    }

                    return var5[3] << 24 | var5[2] << 16 | var5[1] << 8 | var5[0];
                case P_MOSAIC:
                case P_FUSION:
                    if (alpha == 0) {
                        return pixel;
                    }

                    return alpha << 24 | 0xFF0000;
                case P_FILL:
                    return alpha << 24 | this.iColor;
                case P_LPEN:
                case P_UNKNOWN12:
                case P_UNKNOWN13:
                case P_NULL:
                case P_UNKNOWN15:
                case P_UNKNOWN16:
                case P_LR:
                case P_UD:
                case P_R:
                default:
                    return this.fu(pixel, this.iColor, alpha);
            }
        }
    }

    public final byte[] getOffset() {
        return this.offset;
    }

    private final int[] getPM() {
        if (this.iHint == H_TEXT) {
            return null;
        } else {
            int[] var1 = this.user.p;
            if (this.user.pM != this.iPenM || this.user.pA != this.iAlpha || this.user.pS != this.iSize) {
                int[][] var2 = this.info.bPen[this.iPenM];
                int[] var3 = var2[this.iSize];
                int var4 = var3.length;
                if (var1 == null || var1.length < var4) {
                    var1 = new int[var4];
                }

                float var5 = b255[this.iAlpha];

                for (int var6 = 0; var6 < var4; ++var6) {
                    var1[var6] = (int) ((float) var3[var6] * var5);
                }

                this.user.pW = (int) Math.sqrt((double) var4);
                this.user.pM = this.iPenM;
                this.user.pA = this.iAlpha;
                this.user.pS = this.iSize;
                this.user.p = var1;
                this.user.countMax = this.iCount >= 0 ? this.iCount : (int) ((float) this.user.pW / (float) Math.sqrt((double) var2[var2.length - 1].length) * (float) (-this.iCount));
                this.user.count = Math.min(this.user.countMax, this.user.count);
            }

            return var1;
        }
    }

    /** Returns the alpha for the current texture at x,y */
    private final float getTT(int x, int y) {
        if (this.iTT == 0) { // solid color
            return 1.0F;
        } else if (this.iTT < 12) { // halftones
            return (float) (isTone(this.iTT - 1, x, y) ? 0 : 1);
        } else { // textures
            int textureWidth = this.user.pTTW;
            return this.user.pTT[y % textureWidth * textureWidth + x % textureWidth];
        }
    }

    private final ByteStream getWork() {
        this.info.workOut.reset();
        return this.info.workOut;
    }

    private final boolean isM(int color) {
        if (this.iMask == M_N) {
            return false;
        } else {
            color &= 0xFFFFFF;
            return this.iMask == M_M ? this.iColorMask == color : (this.iMask == M_R ? this.iColorMask != color : false);
        }
    }

    /** Used to generate the dithered pattern */
    public static final boolean isTone(int tone, int x, int y) {
        switch (tone) {
            case 2:
                if ((x + 2) % 4 == 0 && (y + 4) % 4 == 0) {
                    break;
                }
            case 1:
                if ((x + 2) % 4 == 0 && (y + 2) % 4 == 0) {
                    break;
                }
            case 0:
                if (x % 4 != 0 || y % 4 != 0) {
                    return true;
                }
                break;
            case 4:
                if ((x + 1) % 4 == 0 && (y + 3) % 4 == 0) {
                    break;
                }
            case 3:
                if (x % 2 == 0 && y % 2 == 0) {
                    break;
                }

                return true;
            case 7:
                if ((x + 2) % 4 == 0 && (y + 3) % 4 == 0) {
                    break;
                }
            case 6:
                if (x % 4 == 0 && (y + 1) % 4 == 0) {
                    break;
                }
            case 5:
                if ((x + 1) % 2 != (y + 1) % 2) {
                    return true;
                }
                break;
            case 9:
                if ((x + 1) % 4 == 0 && (y + 2) % 4 == 0) {
                    break;
                }
            case 8:
                if (x % 2 != 0 && (y + 1) % 2 != 0) {
                    return true;
                }
                break;
            case 10:
                if ((x + 3) % 4 == 0 && (y + 2) % 4 == 0) {
                    return true;
                }
        }

        return false;
    }

    private int[] loadIm(Object var1, boolean var2) {
        try {
            Component var3 = this.info.component;
            Image var4 = var3.getToolkit().createImage((byte[]) this.info.cnf.getRes(var1));
            this.info.cnf.remove(var1);
            Awt.wait(var4);
            PixelGrabber var5 = new PixelGrabber(var4, 0, 0, var4.getWidth((ImageObserver) null), var4.getHeight((ImageObserver) null), true);
            var5.grabPixels();
            int[] var6 = (int[]) var5.getPixels();
            int var7 = var6.length;
            var4.flush();
            var4 = null;
            int var8;
            if (var2) {
                for (var8 = 0; var8 < var7; ++var8) {
                    var6[var8] = var6[var8] & 255 ^ 255;
                }
            } else {
                for (var8 = 0; var8 < var7; ++var8) {
                    var6[var8] &= 255;
                }
            }

            return var6;
        } catch (RuntimeException var9) {
        } catch (InterruptedException var10) {
        }

        return null;
    }

    public final void m_paint(int x1, int y1, int x2, int y2) {
        int scale = this.info.scale;
        int quality = this.info.Q;
        x1 = (x1 / scale + this.info.scaleX) * quality;
        y1 = (y1 / scale + this.info.scaleY) * quality;
        x2 = x2 / scale * quality;
        y2 = y2 / scale * quality;
        this.dBuffer(false, x1, y1, x1 + x2, y1 + y2);
    }

    public final void memset(float[] var1, float var2) {
        int var3 = var1.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            var1[var4] = var2;
        }

    }

    public final void memset(int[] var1, int var2) {
        int var3 = var1.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            var1[var4] = var2;
        }

    }

    public final Image mkLPic(int[] picture, int x, int y, int width, int height, int quality) {
        x *= quality;
        y *= quality;
        width *= quality;
        height *= quality;
        int var8 = this.info.L;
        int var10000 = quality * quality;
        float[] var9 = this.info.visit;
        int var13 = 0;
        int var15 = y + height;
        int var16 = this.info.W;
        int[][] var17 = this.info.iOffs;
        var10000 = var16 * this.info.H;
        boolean isEmpty = picture == null;
        float[] var20 = b255;
        if (isEmpty) {
            picture = this.user.buffer;
        }

        while (y < var15) {
            int var12 = var16 * y + x;

            for (int var14 = var12 + width; var12 < var14; ++var12) {
                int var11 = 0xFFFFFF;

                for (int var7 = 0; var7 < var8; ++var7) {
                    int var10 = var17[var7][var12];
                    float var18 = b255[var10 >>> 24] * var9[var7];
                    var11 = var18 == 1.0F ? var10 : (var18 == 0.0F ? var11 : ((var11 & 16711680) + (int) ((float) ((var10 & 16711680) - (var11 & 16711680)) * var18) & 16711680) + ((var11 & '\uff00') + (int) ((float) ((var10 & '\uff00') - (var11 & '\uff00')) * var18) & '\uff00') + (var11 & 255) + (int) ((float) ((var10 & 255) - (var11 & 255)) * var18));
                }

                picture[var13++] = var11;
            }

            ++y;
        }

        if (isEmpty) {
            this.user.raster.newPixels(this.user.image, width, height, quality);
        } else {
            this.user.raster.scale(picture, width, height, quality);
        }

        return this.user.image;
    }

    private final Image mkMPic(int x, int y, int width, int height, int quality) {
        x *= quality;
        y *= quality;
        width *= quality;
        height *= quality;
        int layerCount = this.info.L;
        int var8 = this.iLayer;
        int var10000 = quality * quality;
        float[] var9 = this.info.visit;
        int var14 = 0;
        int var16 = y + height;
        int var17 = this.info.W;
        int[][] var18 = this.info.iOffs;
        var10000 = var17 * this.info.H;
        int[] var19 = this.user.buffer;
        float[] var22 = b255;

        for (byte[] var20 = this.info.iMOffs; y < var16; ++y) {
            int var13 = var17 * y + x;

            for (int var15 = var13 + width; var13 < var15; ++var13) {
                int var12 = var20[var13] & 255;
                int var11 = 0xFFFFFF;

                for (int var6 = 0; var6 < layerCount; ++var6) {
                    int var10 = var6 != var8 ? var18[var6][var13] : this.getM(var18[var6][var13], var12, var13);
                    float var21 = b255[var10 >>> 24] * var9[var6];
                    var11 = var21 == 1.0F ? var10 : (var21 == 0.0F ? var11 : ((var11 & 16711680) + (int) ((float) ((var10 & 16711680) - (var11 & 16711680)) * var21) & 16711680) + ((var11 & '\uff00') + (int) ((float) ((var10 & '\uff00') - (var11 & '\uff00')) * var21) & '\uff00') + (var11 & 255) + (int) ((float) ((var10 & 255) - (var11 & 255)) * var21));
                }

                var19[var14++] = var11;
            }
        }

        this.user.raster.newPixels(this.user.image, width, height, quality);
        return this.user.image;
    }

    public Mg.Info newInfo(Applet app, Component component, Res cnf) {
        if (this.info != null) {
            return this.info;
        } else {
            this.info = new Mg.Info();
            this.info.cnf = cnf;
            this.info.component = component;
            Mg.Info info = this.info;
            Mg mg = this.info.m;
            float var10 = 3.1415927F;

            int i;
            for (i = 1; i < 256; ++i) {
                b255[i] = (float) i / 255.0F;
                b255d[i] = 255.0F / (float) i;
            }

            b255[0] = 0.0F;
            b255d[0] = 0.0F;
            int[][][] var11 = this.info.bPen;
            boolean var15 = false;
            int var17 = 1;
            short var19 = 255;
            mg.iAlpha = 255;
            this.set(mg);

            int var7;
            int var8;
            int var9;
            int[][] var12;
            int[] var13;
            int var18;
            for (int j = 0; j < 2; ++j) {
                var12 = new int[23][];

                for (i = 0; i < 23; ++i) {
                    var8 = var17 * var17;
                    if (var17 <= 6) {
                        var12[i] = var13 = new int[var8];
                        var7 = 0;

                        while (true) {
                            if (var7 >= var8) {
                                if (var17 >= 3) {
                                    var13[0] = var13[var17 - 1] = var13[var17 * (var17 - 1)] = var13[var8 - 1] = 0;
                                }
                                break;
                            }

                            var13[var7] = var7 >= var17 && var8 - var7 >= var17 && var7 % var17 != 0 && var7 % var17 != var17 - 1 ? mg.iAlpha : var19;
                            ++var7;
                        }
                    } else {
                        var18 = var17 + 1;
                        var12[i] = var13 = new int[var18 * var18];
                        int var16 = (var17 - 1) / 2;
                        var9 = (int) ((float) Math.round(2.0F * var10 * (float) var16) * 3.0F);

                        for (var7 = 0; var7 < var9; ++var7) {
                            int var14 = Math.min(var16 + (int) Math.round((double) var16 * Math.cos((double) var7)), var17);
                            int var29 = Math.min(var16 + (int) Math.round((double) var16 * Math.sin((double) var7)), var17);
                            var13[var29 * var18 + var14] = var19;
                        }

                        info.W = info.H = var18;
                        this.dFill((int[]) var13, 0, 0, var18, var18);
                    }

                    var17 += i <= 7 ? 1 : (i < 18 ? 2 : 4);
                }

                var11[j] = var12;
                mg.iAlpha = 110;
                var19 = 80;
                var17 = 1;
            }

            mg.iAlpha = 255;
            var12 = new int[32][];
            var12[0] = new int[]{128};
            var12[1] = new int[]{255};
            var12[2] = new int[]{0, 128, 0, 128, 255, 128, 0, 128, 0};
            var12[3] = new int[]{128, 174, 128, 174, 255, 174, 128, 174, 128};
            var12[4] = new int[]{174, 255, 174, 255, 255, 255, 174, 255, 174};
            var12[5] = new int[9];
            this.memset(var12[5], 255);
            var12[6] = new int[]{0, 128, 128, 0, 128, 255, 255, 128, 128, 255, 255, 128, 0, 128, 128, 0};
            var13 = var12[7] = new int[16];
            this.memset(var13, 255);
            var13[0] = var13[3] = var13[15] = var13[12] = 128;
            this.memset(var12[8] = new int[16], 255);
            var17 = 3;

            for (i = 9; i < 32; ++i) {
                var18 = var17 + 3;
                float var24 = (float) var17 / 2.0F;
                var12[i] = var13 = new int[var18 * var18];
                var9 = (int) ((float) Math.round(2.0F * var10 * var24) * (float) (2 + i / 16)) + i * 2;

                for (var7 = 0; var7 < var9; ++var7) {
                    float var30;
                    int var25 = (int) (var30 = var24 + 1.5F + var24 * (float) Math.cos((double) var7));
                    float var21;
                    int var26 = (int) (var21 = var24 + 1.5F + var24 * (float) Math.sin((double) var7));
                    float var22 = var30 - (float) var25;
                    float var23 = var21 - (float) var26;
                    int var27 = var26 * var18 + var25;
                    var13[var27] += (int) ((1.0F - var22) * 255.0F);
                    var13[var27 + 1] += (int) (var22 * 255.0F);
                    var13[var27 + var18] += (int) ((1.0F - var23) * 255.0F);
                    var13[var27 + var18 + 1] += (int) (var23 * 255.0F);
                }

                var8 = var18 * var18;

                for (var7 = 0; var7 < var8; ++var7) {
                    var13[var7] = Math.min(var13[var7], 255);
                }

                var17 += 2;
                info.W = info.H = var18;
                this.dFill((int[]) var13, 0, 0, var18, var18);
            }

            var11[2] = var12;
            this.set((Mg) null);
            mg.set((Mg) null);
            if (cnf != null) {
                for (i = 0; i < 16; ++i) {
                    for (var9 = 0; cnf.get("pm" + i + '/' + var9 + ".gif") != null; ++var9) {
                    }

                    if (var9 > 0) {
                        var11[i] = new int[var9][];

                        for (var7 = 0; var7 < var9; ++var7) {
                            var11[i][var7] = this.loadIm("pm" + i + '/' + var7 + ".gif", true);
                        }
                    }
                }

                this.info.bTT = new float[cnf.getP("tt_size", 31)][];
            }

            String var28 = app.getParameter("tt.zip");
            if (var28 != null && var28.length() > 0) {
                this.info.dirTT = var28;
            }

            return this.info;
        }
    }

    public Mg.User newUser(Component var1) {
        if (this.user == null) {
            this.user = new Mg.User();
            if (color_model == null) {
                color_model = new DirectColorModel(24, 16711680, 65280, 255);
            }

            this.user.raster = new SRaster(color_model, this.user.buffer, 128, 128);
            this.user.image = var1.createImage(this.user.raster);
        }

        return this.user;
    }

    /** Gets pixel from current layer or from all layers according to "isAllL" */
    public final int pix(int x, int y) {
        if (!this.isAllL) {
            return this.info.iOffs[this.iLayer][y * this.info.W + x];
        } else {
            int layerCount = this.info.L;
            int var5 = 0;
            int var7 = 0xFFFFFF;
            int var9 = this.info.W * y + x;

            for (int i = 0; i < layerCount; ++i) {
                int var8 = this.info.iOffs[i][var9];
                float fAlpha = b255[var8 >>> 24];
                if (fAlpha != 0.0F) {
                    if (fAlpha == 1.0F) {
                        var7 = var8;
                        var5 = 255;
                    }

                    var5 = (int) ((float) var5 + (float) (255 - var5) * fAlpha);
                    int var4 = 0;

                    for (int j = 16; j >= 0; j -= 8) {
                        int var6 = var7 >>> j & 255;
                        var4 |= var6 + (int) ((float) ((var8 >>> j & 255) - var6) * fAlpha) << j;
                    }

                    var7 = var4;
                }
            }

            return var5 << 24 | var7;
        }
    }

    /** Reads one byte from this.offset */
    private final byte r() {
        return this.offset[this.iSeek++];
    }

    /** Returns an int made of "length" bytes from "buffer" at "seek" position */
    private final int r(byte[] buffer, int seek, int length) {
        int out = 0;

        for (int i = length - 1; i >= 0; --i) {
            out |= (buffer[seek++] & 0xFF) << i * 8;
        }

        return out;
    }

    /** Returns a short */
    private final short r2() {
        return (short) ((this.ru() << 8) + this.ru());
    }

    public void reset() {
        byte[] var3 = this.info.iMOffs;
        int width = this.info.W;
        int x1 = Math.max(this.user.dX, 0);
        int y1 = Math.max(this.user.dY, 0);
        int x2 = Math.min(this.user.dW, width);
        int y2 = Math.min(this.user.dH, this.info.H);

        for (int i = y1; i < y2; ++i) {
            int offset = x1 + i * width;

            for (int j = x1; j < x2; ++j) {
                var3[offset++] = 0;
            }
        }

        this.dBuffer(false, x1, y1, x2, y2);
        this.setD(0, 0, 0, 0);
    }

    /** Reads a X or Y coordinate, must be used in pairs */
    private final int rPo() {
        byte b = this.r();
        return b != -128 ? b : this.r2();
    }

    /** Returns an unsigned byte */
    private final int ru() {
        return this.r() & 255;
    }

    private final int s(int var1, int var2, int var3) {
        byte[] var4 = this.info.iMOffs;
        int var5 = this.info.W - 1;

        for (int var6 = (var5 + 1) * var3 + var2; var2 < var5 && this.pix(var2 + 1, var3) == var1 && var4[var6 + 1] == 0; ++var2) {
            ++var6;
        }

        return var2;
    }

    private final int sa(int var1) {
        if ((this.iSOB & F1_ALL_LAYERS) == 0) {
            return this.iAlpha;
        } else {
            int var2 = this.iSA & 255;
            return var2 + (int) (b255[(this.iSA >>> 8) - var2] * (float) var1);
        }
    }

    /** Sets class data from a buffer starting from a offset */
    public final int set(byte[] buffer, int offset) {
        int var3 = (buffer[offset++] & 255) << 8 | buffer[offset++] & 255;
        int start = offset;
        if (var3 <= 2) {
            return var3 + 2;
        } else {
            try {
                int var5 = 0;
                boolean shiftFlag = false;
                int flags1 = buffer[offset++] & 255;
                int flags2 = buffer[offset++] & 255;
                int flags3 = buffer[offset++] & 255;
                this.isAllL = (flags1 & F1_ALL_LAYERS) != 0;
                this.isAFix = (flags1 & F1_AFIX) != 0;
                this.isOver = (flags1 & F1O) != 0;
                this.isCount = (flags1 & F1C) != 0;
                this.isAnti = (flags1 & F1A) != 0;
                this.iSOB = flags1 >>> 6; // F1S
                if ((flags2 & F2H) != 0) {
                    var5 = buffer[offset++] & 255;
                    shiftFlag = true;
                    this.iHint = var5 >>> 4;
                }

                if ((flags2 & F2PM) != 0) {
                    if (!shiftFlag) {
                        var5 = buffer[offset++] & 255;
                        this.iPenM = var5 >>> 4;
                    } else {
                        this.iPenM = var5 & 15;
                    }

                    shiftFlag = !shiftFlag;
                }

                if ((flags2 & F2M) != 0) {
                    if (!shiftFlag) {
                        var5 = buffer[offset++] & 255;
                        this.iMask = var5 >>> 4;
                    } else {
                        this.iMask = var5 & 15;
                    }

                    shiftFlag = !shiftFlag;
                }

                if ((flags2 & F2P) != 0) {
                    this.iPen = buffer[offset++] & 255;
                }

                if ((flags2 & F2T) != 0) {
                    this.iTT = buffer[offset++] & 255;
                }

                if ((flags2 & F2L) != 0) {
                    this.iLayer = buffer[offset++] & 255;
                }

                if ((flags2 & F2LS) != 0) {
                    this.iLayerSrc = buffer[offset++] & 255;
                }

                if ((flags3 & F3A) != 0) {
                    this.iAlpha = buffer[offset++] & 255;
                }

                if ((flags3 & F3C) != 0) {
                    this.iColor = this.r(buffer, offset, 3);
                    offset += 3;
                }

                if ((flags3 & F3CM) != 0) {
                    this.iColorMask = this.r(buffer, offset, 3);
                    offset += 3;
                }

                if ((flags3 & F3S) != 0) {
                    this.iSize = buffer[offset++] & 255;
                }

                if ((flags3 & F3E) != 0) {
                    this.iCount = buffer[offset++];
                }

                if ((flags3 & F3SA) != 0) {
                    this.iSA = this.r(buffer, offset, 2);
                    offset += 2;
                }

                if ((flags3 & F3SS) != 0) {
                    this.iSS = this.r(buffer, offset, 2);
                    offset += 2;
                }

                if (this.iPen == P_FUSION) {
                    this.iAlpha2 = this.r(buffer, offset, 2);
                    offset += 2;
                }

                if (this.iHint == H_TEXT) {
                    int strLength = this.r(buffer, offset, 2);
                    offset += 2;
                    if (strLength == 0) {
                        this.strHint = null;
                    } else {
                        this.strHint = new byte[strLength];
                        System.arraycopy(buffer, offset, this.strHint, 0, strLength);
                        offset += strLength;
                    }
                }

                start = var3 - (offset - start);
                if (start > 0) {
                    if (this.offset == null || this.offset.length < start) {
                        this.offset = new byte[start];
                    }

                    this.iOffset = start;
                    System.arraycopy(buffer, offset, this.offset, 0, start);
                } else {
                    this.iOffset = 0;
                }
            } catch (RuntimeException ex) {
                ex.printStackTrace();
                this.iOffset = 0;
            }

            return var3 + 2;
        }
    }

    /** Sets class fields to a certain value by parsing a string "key=value;key2=value2...@ignored_content" */
    public final void set(String commands) {
        try {
            if (commands == null || commands.length() == 0) {
                return;
            }

            Field[] fieldArr = this.getClass().getDeclaredFields();
            int cmdLength = commands.indexOf('@');
            if (cmdLength < 0) {
                cmdLength = commands.length();
            }

            int posEq;
            for (int i = 0; i < cmdLength; i = posEq + 1) {
                posEq = commands.indexOf('=', i);
                if (posEq == -1) {
                    break;
                }

                String key = commands.substring(i, posEq);
                i = posEq + 1;
                posEq = commands.indexOf(';', i);
                if (posEq < 0) {
                    posEq = cmdLength;
                }

                try {
                    for (int j = 0; j < fieldArr.length; ++j) {
                        Field field = fieldArr[j];
                        if (field.getName().equals(key)) {
                            String value = commands.substring(i, posEq);
                            Class fieldType = field.getType();
                            if (fieldType.equals(Integer.TYPE)) {
                                field.setInt(this, Integer.parseInt(value));
                            } else if (fieldType.equals(Boolean.TYPE)) {
                                field.setBoolean(this, Integer.parseInt(value) != 0);
                            } else {
                                field.set(this, value);
                            }
                            break;
                        }
                    }
                } catch (NumberFormatException ex) {
                } catch (IllegalAccessException ex) {
                }
            }

            if (cmdLength != commands.length()) {
                ByteStream bs = this.getWork();

                for (int j = cmdLength + 1; j < commands.length(); j += 2) {
                    bs.write(Character.digit(commands.charAt(j), 16) << 4 | Character.digit(commands.charAt(j + 1), 16));
                }

                this.offset = bs.toByteArray();
                this.iOffset = this.offset.length;
            }
        } catch (Throwable ex) {
        }

    }

    /** Copies data from another M instance */
    public final void set(Mg mg) {
        if (mg == null) {
            mg = mgDef;
        }

        this.iHint = mg.iHint;
        this.iPen = mg.iPen;
        this.iPenM = mg.iPenM;
        this.iTT = mg.iTT;
        this.iMask = mg.iMask;
        this.iSize = mg.iSize;
        this.iSS = mg.iSS;
        this.iCount = mg.iCount;
        this.isOver = mg.isOver;
        this.isCount = mg.isCount;
        this.isAFix = mg.isAFix;
        this.isAnti = mg.isAnti;
        this.isAllL = mg.isAllL;
        this.iAlpha = mg.iAlpha;
        this.iAlpha2 = mg.iAlpha2;
        this.iSA = mg.iSA;
        this.iColor = mg.iColor;
        this.iColorMask = mg.iColorMask;
        this.iLayer = mg.iLayer;
        this.iLayerSrc = mg.iLayerSrc;
        this.iSOB = mg.iSOB;
        this.strHint = mg.strHint;
        this.iOffset = 0;
    }

    private final void addD(int x, int y, int x2, int y2) {
        this.setD(Math.min(x, this.user.dX), Math.min(y, this.user.dY), Math.max(x2, this.user.dW), Math.max(y2, this.user.dH));
    }

    private final void setD(int x, int y, int x2, int y2) {
        this.user.dX = x;
        this.user.dY = y;
        this.user.dW = x2;
        this.user.dH = y2;
    }

    public void setInfo(Mg.Info info) {
        this.info = info;
    }

    public void setUser(Mg.User user) {
        this.user = user;
    }

    /** Updates the tracking of the previous mouse positions with a new one */
    private final void shift(int x, int y) {
        // shifts aways the oldest position and adds the new coordinates
        System.arraycopy(this.user.pX, 1, this.user.pX, 0, 3);
        System.arraycopy(this.user.pY, 1, this.user.pY, 0, 3);
        this.user.pX[3] = x;
        this.user.pY[3] = y;
    }

    private final int ss(int pressure) {
        if ((this.iSOB & F1_AFIX) == 0) {
            return this.iSize;
        } else {
            int minSize = this.iSS & 255;
            return (int) (((float) minSize + b255[(this.iSS >>> 8) - minSize] * (float) pressure) * this.user.pV);
        }
    }

    /** Applies the current texture to brush stroke */
    private final void t() {
        if (this.iTT != 0) {
            byte[] mOffs = this.info.iMOffs;
            int width = this.info.W;
            int x = this.user.dX;
            int y = this.user.dY;
            int x2 = this.user.dW;
            int y2 = this.user.dH;

            for (int j = y; j < y2; ++j) {
                int offset = width * j + x;

                for (int i = x; i < x2; ++i) {
                    mOffs[offset] = (byte) ((int) ((float) (mOffs[offset++] & 255) * this.getTT(i, j)));
                }
            }

        }
    }

    /** Writes a X or Y coordinate, must be obviously used in pairs */
    private final void wPo(int coord) throws IOException {
        ByteStream bs = this.info.workOut;
        // this must be an attempt at compressing data
        if (coord <= 127 && coord >= -127) {
            bs.write(coord);
        } else {
            bs.write(-128); // this is a magic value so rPo() will recognize that the next value is a long
            bs.w((long) coord, 2);
        }

    }

    public class User {
        private Image image = null;
        private SRaster raster = null;
        private int[] buffer = new int[65536];
        private int[] argb = new int[4];
        public int[] points = new int[6];
        private int[] ps2 = null;
        private int[] p = null;
        private int pW;
        private int pM = -1;
        private int pA = -1;
        private int pS = -1;
        private float pV = 1.0F;
        private float[] pTT = null; // texture data as an array of floats representing alpha
        private int pTTW;
        private boolean isDirect;
        public int wait = 0;
        private int[] pX = new int[4];
        private int[] pY = new int[4];
        private int oX;
        private int oY;
        private float fX;
        private float fY;
        private int iDCount;
        private int dX;
        private int dY;
        private int dW;
        private int dH;
        private int count = 0;
        private int countMax;

        private void setup(Mg mg) {
            this.pV = Mg.b255[mg.info.bPen[mg.iPenM].length - 1];
            mg.getPM();
            this.count = 0;
            this.iDCount = 0;
            this.oX = -1000;
            this.oY = -1000;
            this.isDirect = mg.iPen == P_SUISAI2 || mg.iHint == H_COPY || mg.isOver;
            if (mg.iTT >= 12) {
                this.pTT = Mg.this.info.getTT(mg.iTT);
                this.pTTW = (int) Math.sqrt((double) this.pTT.length);
            }

        }

        public void setIm(Mg mg) {
            if (mg.iHint != 8) {
                if (this.pM != mg.iPenM || this.pA != mg.iAlpha || this.pS != mg.iSize) {
                    int[] var2 = mg.info.bPen[mg.iPenM][mg.iSize];
                    int var3 = var2.length;
                    if (this.p == null || this.p.length < var3) {
                        this.p = new int[var3];
                    }

                    float var4 = Mg.b255[mg.iAlpha];

                    for (int var6 = 0; var6 < var3; ++var6) {
                        float var5 = (float) var2[var6] * var4;
                        this.p[var6] = var5 <= 1.0F && var5 > 0.0F ? 1 : (int) var5;
                    }

                    this.pW = mg.iPen = (int) Math.sqrt((double) var3);
                    this.pM = mg.iPenM;
                    this.pA = mg.iAlpha;
                    this.pS = mg.iSize;
                }

            }
        }

        public int getPixel(int x, int y) {
            int var3 = Mg.this.info.imW;
            if (x >= 0 && y >= 0 && x < var3 && y < Mg.this.info.imH) {
                Mg.this.mkLPic(this.buffer, x, y, 1, 1, Mg.this.info.Q);
                return Mg.this.info.iOffs[Mg.this.info.m.iLayer][var3 * y + x] & 0xFF000000 | this.buffer[0];
            } else {
                return 0;
            }
        }

        public int[] getBuffer() {
            return this.buffer;
        }

        public long getRect() {
            return (long) this.dX << 48 | (long) this.dY << 32 | (long) (this.dW << 16) | (long) this.dH;
        }

        public Image mkImage(int var1, int var2) {
            this.raster.newPixels(this.image, this.buffer, var1, var2);
            return this.image;
        }
    }

    public class Info {
        private ByteStream workOut = new ByteStream();
        private Res cnf;
        private String dirTT = null;
        public Graphics g = null;
        public String text = "";
        public String textOption = "";
        private int vWidth;
        private int vHeight;
        private Dimension vD = new Dimension();
        private Component component = null;
        public int Q = 1;
        public int L;
        public float[] visit;
        public int scale = 1;
        public int scaleX = 0;
        public int scaleY = 0;
        private int[][] iOffs;
        private byte[] iMOffs;
        public int imH;
        public int imW;
        public int W;
        public int H;
        private int[][][] bPen = new int[16][][];
        private float[][] bTT = new float[14][];
        public Mg m = new Mg();

        public void setSize(int width, int height, int quality) {
            if (width * quality != this.W || height * quality != this.H) {
                this.iOffs = null;
            }

            this.imW = width;
            this.imH = height;
            this.W = width * quality;
            this.H = height * quality;
            this.Q = quality;
            int size = this.W * this.H;
            if (this.iMOffs == null || this.iMOffs.length < size) {
                this.iMOffs = new byte[size];
            }

        }

        public void setIOffs(int[][] var1) {
            this.L = var1.length;
            this.iOffs = var1;
            if (this.visit == null || this.visit.length != this.L) {
                Mg.this.memset(this.visit = new float[this.L], 1.0F);
            }

        }

        public void setComponent(Component cmp, Graphics g, int width, int height) {
            this.component = cmp;
            this.vWidth = width;
            this.vHeight = height;
            this.g = g;
        }

        /** Changes the number of layers */
        public void setL(int n) {
            int layerCount = this.iOffs == null ? 0 : this.iOffs.length;
            Math.min(layerCount, n);
            if (layerCount != n) {
                float[] var3 = new float[n];
                int[][] var4 = new int[n][];
                int var5 = this.W * this.H;

                for (int i = 0; i < n; ++i) {
                    if (i >= layerCount) {
                        var3[i] = 1.0F;
                        Mg.this.memset(var4[i] = new int[var5], 0xFFFFFF);
                    } else {
                        var4[i] = this.iOffs[i];
                        var3[i] = this.visit[i];
                    }
                }

                this.visit = var3;
                this.iOffs = var4;
            }

            this.L = n;
        }

        /** Deletes a layer */
        public void delL(int layerNumber) {
            int layerCount = this.iOffs.length;
            if (layerNumber < layerCount) {
                int idx = layerCount - 1;
                float[] visit = new float[idx];
                int[][] iOffs = new int[idx][];
                idx = 0;

                for (int i = 0; i < layerCount; ++i) {
                    if (i != layerNumber) {
                        visit[idx] = this.visit[i];
                        iOffs[idx++] = this.iOffs[i];
                    }
                }

                this.visit = visit;
                this.iOffs = iOffs;
                this.L = layerCount - 1;
            }
        }

        public boolean addScale(int var1, boolean var2) {
            if (var2) {
                if (var1 <= 0) {
                    this.scale = 1;
                    this.setQuality(1 - var1);
                } else {
                    this.setQuality(1);
                    this.scale = var1;
                }

                return true;
            } else {
                int var3 = this.scale + var1;
                if (var3 > 32) {
                    return false;
                } else {
                    if (var3 <= 0) {
                        this.scale = 1;
                        this.setQuality(this.Q + 1 - var3);
                    } else if (this.Q >= 2) {
                        this.setQuality(this.Q - 1);
                    } else {
                        this.setQuality(1);
                        this.scale = var3;
                    }

                    return true;
                }
            }
        }

        /** Sets dots per pixel */
        public void setQuality(int quality) {
            this.Q = quality;
            this.imW = this.W / this.Q;
            this.imH = this.H / this.Q;
        }

        public Dimension getSize() {
            this.vD.setSize(this.vWidth, this.vHeight);
            return this.vD;
        }

        private void center(Point point) {
            point.x = point.x / this.scale + this.scaleX;
            point.y = point.y / this.scale + this.scaleY;
        }

        public int[][][] getPenMask() {
            return this.bPen;
        }

        public int getPenSize(Mg mg) {
            return (int) Math.sqrt((double) this.bPen[mg.iPenM][mg.iSize].length);
        }

        public int getPMMax() {
            return this.m.iHint == 8 ? 255 : this.bPen[this.m.iPenM].length;
        }

        public int[][] getOffset() {
            return this.iOffs;
        }

        public float[] getTT(int index) {
            index -= 12;
            if (this.bTT[index] == null) {
                if (this.dirTT != null) {
                    String dirTT = this.dirTT;
                    this.dirTT = null;

                    try {
                        this.cnf.loadZip(dirTT);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

                int[] texture = Mg.this.loadIm("tt/" + index + ".gif", false);
                if (texture == null) {
                    return null;
                }

                int textureLength = texture.length;
                float[] alphaTexture = new float[textureLength];

                for (int i = 0; i < textureLength; ++i) {
                    alphaTexture[i] = Mg.b255[texture[i]];
                }

                this.bTT[index] = alphaTexture;
            }

            return this.bTT[index];
        }
    }
}
