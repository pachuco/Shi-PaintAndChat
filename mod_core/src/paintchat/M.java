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
import java.awt.Toolkit;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.MemoryImageSource;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;

import syi.awt.Awt;
import syi.util.ByteStream;

public class M {
    private M.Info info;
    private M.User user;
    public int iHint = H_FLINE;
    public int iPen = P_SOLID;
    public int iPenM = PM_PEN;
    public int iTT = 0; // selected texture index
    public int iColor = 0;
    public int iColorMask = 0;
    public int iAlpha = 255;
    public int iAlpha2;
    public int iSA = 0xFF00;
    public int iLayer = 0;
    public int iLayerSrc = 1;
    public int iMask = M_N;
    public int iSize = 0;
    public int iSS = 0xFF00;
    public int iCount = DEF_COUNT;
    public int iSOB;
    public boolean isAFix;
    public boolean isOver;
    public boolean isCount = true;
    public boolean isAnti;
    public boolean isAllL;
    public byte[] strHint;
    private int iSeek;
    private int iOffset;
    private byte[] offset;
    // iHint
    public static final int H_FLINE = 0;
    public static final int H_LINE = 1;
    public static final int H_BEZI = 2;
    public static final int H_RECT = 3;
    public static final int H_FRECT = 4;
    public static final int H_OVAL = 5;
    public static final int H_FOVAL = 6;
    public static final int H_FILL = 7;
    public static final int H_TEXT = 8;
    public static final int H_COPY = 9;
    public static final int H_CLEAR = 10;
    public static final int H_SP = 11;
    public static final int H_VTEXT = 12;
    public static final int H_UNKNOWN13 = 13;
    public static final int H_L = 14;
    // iPen
    public static final int P_SOLID = 0;
    public static final int P_PEN = 1;
    public static final int P_SUISAI = 2;
    public static final int P_SUISAI2 = 3;
    public static final int P_WHITE = 4;
    public static final int P_SWHITE = 5;
    public static final int P_LIGHT = 6;
    public static final int P_DARK = 7;
    public static final int P_BOKASHI = 8;
    public static final int P_MOSAIC = 9;
    public static final int P_FILL = 10;
    public static final int P_LPEN = 11;
    public static final int P_UNKNOWN12 = 12;
    public static final int P_UNKNOWN13 = 13;
    public static final int P_NULL = 14;
    public static final int P_UNKNOWN15 = 15;
    public static final int P_UNKNOWN16 = 16;
    public static final int P_LR = 17;
    public static final int P_UD = 18;
    public static final int P_R = 19;
    public static final int P_FUSION = 20;
    // iPenM
    public static final int PM_PEN = 0;
    public static final int PM_SUISAI = 1; // Suisai means watercolor
    public static final int PM_MANY = 2;
    // iMask
    public static final int M_N = 0; // Normal
    public static final int M_M = 1; // Multiply
    public static final int M_R = 2; // Invert
    public static final int M_ADD = 3; // Additive
    public static final int M_SUB = 4; // Subtract
    // Flags group 1 (iSOB)
    public static final int F1_ALL_LAYERS = 1;
    private static final int F1_AFIX = 2;
    private static final int F1O = 4;
    private static final int F1C = 8;
    private static final int F1A = 16;
    private static final int F1S = 32;
    // Flags group 2
    private static final int F2H = 1;
    private static final int F2PM = 2;
    private static final int F2M = 4;
    private static final int F2P = 8;
    private static final int F2T = 16;
    private static final int F2L = 32;
    private static final int F2LS = 64;
    // Flags group 3
    private static final int F3A = 1;
    private static final int F3C = 2;
    private static final int F3CM = 4;
    private static final int F3S = 8;
    private static final int F3E = 16;
    private static final int F3SA = 32;
    private static final int F3SS = 64;

    private static final int DEF_COUNT = -8;
    private static final String ENCODE = "UTF8";
    private static float[] b255 = new float[256]; // byte to float alpha LUT
    static float[] b255d = new float[256]; // inverse byte to float alpha LUT
    private static ColorModel color_model = null;
    private static final M mgDef = new M();

    public M() {
    }

    public M(M.Info info, M.User user) {
        this.info = info;
        this.user = user;
    }

    private final void copy(int[][] src, int[][] dest) {
        for (int offset = 0; offset < dest.length; ++offset) {
            System.arraycopy(src[offset], 0, dest[offset], 0, dest[offset].length);
        }

                                        
                         
                         
    }

    public final void dBuffer() {
        this.dBuffer(!this.user.isDirect, this.user.X, this.user.Y, this.user.X2, this.user.Y2);
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

            int distXY = 0;

            float pointX;
            float pointY;
            int var8;
            for (var8 = 1; var8 < 4; ++var8) {
                pointX = (float) (points[var8] >> 16);
                pointY = (float) ((short) points[var8]);
                distXY = (int) ((double) distXY + Math.sqrt((double) (pointX * pointX + pointY * pointY)));
                int var10000 = points[var8];
            }

            if (distXY <= 0) {
                return;
            }

            boolean var16 = true;
            boolean var9 = true;
            int var10 = -1000;
            int var11 = -1000;
            int var12 = 0;
            boolean isAntialias = this.isAnti;
            int var14 = this.user.pW / 2;

            for (int i = distXY; i > 0; --i) {
                float var5 = (float) i / (float) distXY;
                float var4 = (float) Math.pow((double) (1.0F - var5), 3.0D);
                pointX = var4 * (float) (points[3] >> 16);
                pointY = var4 * (float) ((short) points[3]);
                var4 = 3.0F * (1.0F - var5) * (1.0F - var5) * var5;
                pointX += var4 * (float) (points[2] >> 16);
                pointY += var4 * (float) ((short) points[2]);
                var4 = 3.0F * var5 * var5 * (1.0F - var5);
                pointX += var4 * (float) (points[1] >> 16);
                pointY += var4 * (float) ((short) points[1]);
                var4 = var5 * var5 * var5;
                pointX += var4 * (float) (points[0] >> 16);
                pointY += var4 * (float) ((short) points[0]);
                var8 = (int) pointX + var14;
                int var17 = (int) pointY + var14;
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

            this.user.X = this.user.X - 1;
            this.user.Y = this.user.Y - 1;
            this.user.X2 = this.user.X2 + 2;
            this.user.Y2 = this.user.Y2 + 2;
        } catch (RuntimeException var15) {
            var15.printStackTrace();
        }

    }

    public void dClear() {
        if (this.iPen != P_NULL) {
            for (int var1 = 0; var1 < this.info.L; ++var1) {
                if (var1 >= 64 || (this.info.unpermission & (long) (1 << var1)) == 0L) {
                    this.info.layers[var1].clear();
                }
            }

            this.user.isDirect = true;
            this.setD(0, 0, this.info.W, this.info.H);
            if (this.user.wait >= 0) {
                this.dBuffer();
            }

        }
    }

    private void dFusion(byte[] var1) {
        LO[] var2 = this.info.layers;
        LO var4 = new LO();
        LO var6 = new LO();
        int var7 = this.info.W;
        int var8 = var1.length / 4;
        int[] var9 = this.user.buffer;
        int var10 = var9.length / var7;

        LO var3;
        int var13;
        for (int var12 = 0; var12 < this.info.H; var12 += var10) {
            int var11 = Math.min(this.info.H - var12, var10);
            var13 = 0;
            LO var5 = null;

            for (int var14 = 0; var14 < var8; ++var14) {
                var3 = var2[var1[var13++]];
                var4.setField(var3);
                var3.iAlpha = b255[var1[var13++] & 255];
                var3.iCopy = var1[var13++];
                ++var13;
                var3.normalize(var3.iAlpha, 0, var12, var7, var12 + var11);
                if (var5 == null) {
                    var5 = var3;
                    var6.setField(var4);
                    var3.reserve();
                } else {
                    if (var3.iCopy == 1) {
                        this.memset(var9, 0xFFFFFF);

                        for (int var15 = 0; var15 < var14 - 2; ++var15) {
                            var2[var15].draw(var9, 0, var12, var7, var12 + var11, var7);
                        }
                    }

                    var3.dAdd(var5.offset, 0, var12, var7, var12 + var11, var9);
                    var3.clear(0, var12, var7, var12 + var11);
                    var3.setField(var4);
                }
            }

            if (var5 != var2[this.iLayer]) {
                var5.copyTo(0, var12, var7, var12 + var11, var2[this.iLayer], 0, var12, (int[]) null);
                var5.clear(0, var12, var7, var12 + var11);
            }
        }

        var4.iAlpha = 1.0F;
        var4.iCopy = 0;
        var4.isDraw = true;

        for (var13 = 0; var13 < var8; ++var13) {
            var3 = var2[var1[var13 * 4]];
            var4.name = var3.name;
            var3.setField(var4);
        }

    }

    private void dCopy(int[] var1) {
        int var2 = var1[0];
        int var3 = var2 >> 16;
        short var4 = (short) var2;
        var2 = var1[1];
        int var5 = var2 >> 16;
        short var6 = (short) var2;
        var2 = var1[2];
        int var7 = var2 >> 16;
        short var8 = (short) var2;
        this.info.layers[this.iLayerSrc].copyTo(var3, var4, var5, var6, this.info.layers[this.iLayer], var7, var8, this.user.buffer);
        this.setD(var7, var8, var7 + (var5 - var3), var8 + (var6 - var4));
    }

    public final void dEnd() throws InterruptedException {
        if (!this.user.isDirect) {
            this.dFlush();
        }

        ByteStream var1 = this.info.workOut;
        if (var1.size() > 0) {
            this.offset = var1.writeTo(this.offset, 0);
            this.iOffset = var1.size();
        }

        if (this.user.wait == -1) {
            this.dBuffer();
        }

    }

    private void dFill(byte[] var1, int var2, int var3, int var4, int var5) {
        byte var6 = (byte) this.iAlpha;
        int var7 = this.info.W;

        try {
            for (int var12 = var4 - var2; var3 < var5; ++var3) {
                int var8 = var3 * var7 + var2;

                int var11;
                for (var11 = 0; var11 < var12 && var1[var8] != var6; ++var11) {
                    ++var8;
                }

                while (var11 < var12 && var1[var8] == var6) {
                    ++var8;
                    ++var11;
                }

                int var9 = var8;
                if (var11 < var12) {
                    while (var11 < var12 && var1[var8] != var6) {
                        ++var8;
                        ++var11;
                    }

                    int var10 = var8;
                    if (var11 < var12) {
                        while (var9 < var10) {
                            var1[var9] = var6;
                            ++var9;
                        }
                    }
                }
            }
        } catch (RuntimeException var13) {
            System.out.println((Object) var13);
        }

    }

    private void dFill(int[] var1, int var2, int var3, int var4, int var5) {
        int var6 = this.iAlpha;
        int var7 = this.info.W;

        try {
            for (int var11 = var4 - var2; var3 < var5; ++var3) {
                int var8 = var3 * var7 + var2;

                int var12;
                for (var12 = var8 + var11; var8 < var12 && var1[var8] != var6; ++var8) {
                    ;
                }

                if (var8 < var12 - 1) {
                    ++var8;

                    while (var8 < var12 && var1[var8] == var6) {
                        ++var8;
                    }

                    if (var8 < var12 - 1) {
                        int var9;
                        for (var9 = var8++; var8 < var12 && var1[var8] != var6; ++var8) {
                            ;
                        }

                        if (var8 < var12) {
                            for (int var10 = var8; var9 < var10; ++var9) {
                                var1[var9] = var6;
                            }
                        }
                    }
                }
            }
        } catch (RuntimeException var13) {
            System.out.println((Object) var13);
        }

    }

    private void dFill(int var1, int var2) {
        int var3 = this.info.W;
        int var4 = this.info.H;
        byte var5 = (byte) this.iAlpha;
        byte[] var6 = this.info.iMOffs;

        try {
            int[] var7 = this.user.buffer;
            byte var8 = 0;
            if (var1 < 0 || var1 >= var3 || var2 < 0 || var2 >= var4) {
                return;
            }

            int var13 = this.pix(var1, var2);
            int var14 = this.iAlpha << 24 | this.iColor;
            if (var13 == var14) {
                return;
            }

            int var16 = var8 + 1;
            var7[var8] = this.s(var13, var1, var2) << 16 | var2;

            while (var16 > 0) {
                --var16;
                int var9 = var7[var16];
                var1 = var9 >>> 16;
                var2 = var9 & 0xFFFF;
                int var10 = var3 * var2;
                boolean var11 = false;
                boolean var12 = false;

                while (true) {
                    var6[var10 + var1] = var5;
                    if (var2 > 0 && this.pix(var1, var2 - 1) == var13 && var6[var10 - var3 + var1] == 0) {
                        if (!var11) {
                            var11 = true;
                            var7[var16++] = this.s(var13, var1, var2 - 1) << 16 | var2 - 1;
                        }
                    } else {
                        var11 = false;
                    }

                    if (var2 < var4 - 1 && this.pix(var1, var2 + 1) == var13 && var6[var10 + var3 + var1] == 0) {
                        if (!var12) {
                            var12 = true;
                            var7[var16++] = this.s(var13, var1, var2 + 1) << 16 | var2 + 1;
                        }
                    } else {
                        var12 = false;
                    }

                    if (var1 <= 0 || this.pix(var1 - 1, var2) != var13 || var6[var10 + var1 - 1] != 0) {
                        break;
                    }

                    --var1;
                }
            }
        } catch (RuntimeException var15) {
            System.out.println((Object) var15);
        }

        this.setD(0, 0, var3, var4);
        this.t();
    }

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
                    ++var10;
                }

                if (var11 < 0.0F) {
                    --var18;
                    ++var11;
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
        var22 = var22 >= 5.0F ? 5.0F : (var22 <= -10.0F ? -10.0F : var22);
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

        this.user.fX = var25 - var23;
        this.user.fY = var26 - var24;
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

    private final void dFLine2(int var1) throws InterruptedException {
        try {
            int var2 = this.user.pX[0];
            int var3 = this.user.pY[0];
            int var4 = this.user.pX[1];
            int var5 = this.user.pY[1];
            int var6 = this.user.pX[2];
            int var7 = this.user.pY[2];
            int var8 = this.user.pX[3];
            int var9 = this.user.pY[3];
            boolean var13 = this.isAnti;
            float var14 = this.user.fX;
            float var15 = this.user.fY;
            int var25 = (int) var14;
            int var26 = (int) var15;
            int var27 = var25;
            int var28 = var26;
            int var29 = this.user.oX;
            int var30 = this.user.oY;
            int var31 = this.user.wait;
            if (!this.isCount) {
                this.user.count = 0;
            }

            int var34 = 2 * var4;
            int var35 = 2 * var5;
            int var36 = 2 * var2 - 5 * var4 + 4 * var6 - var8;
            int var37 = 2 * var3 - 5 * var5 + 4 * var7 - var9;
            int var38 = -var2 + 3 * var4 - 3 * var6 + var8;
            int var39 = -var3 + 3 * var5 - 3 * var7 + var9;
            float var40 = (float) this.iSize;
            float var41 = (float) this.iAlpha;
            int var42 = this.ss(var1);
            int var43 = this.sa(var1);
            float var44 = (float) (var42 - this.iSize) * 0.25F;
            var44 = var44 <= -1.5F ? -1.5F : (var44 >= 1.5F ? 1.5F : var44);
            float var45 = (float) (var43 - this.iAlpha) * 0.25F;
            int var48 = (int) Math.sqrt((double) Math.max(this.info.getPenMask()[this.iPenM][this.iSize].length, this.info.getPenMask()[this.iPenM][var42].length));
            int var49 = this.info.Q;

            int var12;
            for (float var50 = 0.0F; var50 < 1.0F; var50 += 0.25F) {
                float var21 = var50 * var50;
                float var22 = var21 * var50;
                float var16 = 0.5F * ((float) var34 + (float) (-var2 + var6) * var50 + (float) var36 * var21 + (float) var38 * var22);
                float var17 = 0.5F * ((float) var35 + (float) (-var3 + var7) * var50 + (float) var37 * var21 + (float) var39 * var22);
                float var18 = Math.max(Math.abs(var16 - var14), Math.abs(var17 - var15));
                if (var18 >= 1.0F) {
                    float var19 = (var16 - var14) / var18 * 0.25F;
                    var19 = var19 <= -1.0F ? -1.0F : (var19 >= 1.0F ? 1.0F : var19);
                    float var20 = (var17 - var15) / var18 * 0.25F;
                    var20 = var20 <= -1.0F ? -1.0F : (var20 >= 1.0F ? 1.0F : var20);
                    int var11 = (int) (var18 / 0.25F);
                    if (var11 < 16) {
                        var11 = 1;
                    }

                    float var46 = var44 / (float) var11;
                    float var47 = var45 / (float) var11;
                    var25 = Math.min(Math.min((int) var14, (int) var16), var25);
                    var26 = Math.min(Math.min((int) var15, (int) var17), var26);
                    var27 = Math.max(Math.max((int) var14, (int) var16), var27);
                    var28 = Math.max(Math.max((int) var15, (int) var17), var28);

                    for (int var10 = 0; var10 < var11; ++var10) {
                        int var32 = (int) var14;
                        int var33 = (int) var15;
                        if (var29 == var32 && var30 == var33) {
                            var40 += var46;
                            var41 += var47;
                        } else {
                            var29 = var32;
                            var30 = var33;
                            this.user.count = this.user.count - 1;
                        }

                        if (this.user.count > 0) {
                            var14 += var19;
                            var15 += var20;
                        } else {
                            this.iSize = (int) var40;
                            this.iAlpha = (int) var41;
                            this.getPM();
                            var12 = this.user.pW / 2;
                            var32 -= var12;
                            var33 -= var12;
                            this.user.count = this.user.countMax;
                            if (var13) {
                                float var23 = var14 - (float) ((int) var14);
                                float var24 = var15 - (float) ((int) var15);
                                if (var23 < 0.0F) {
                                    --var32;
                                    ++var23;
                                }

                                if (var24 < 0.0F) {
                                    --var33;
                                    ++var24;
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

                            if (var31 > 0) {
                                this.dBuffer(!this.user.isDirect, var32, var33, var32 + var12 * 2, var33 + var12 * 2);
                                if (var31 > 1) {
                                    Thread.currentThread();
                                    Thread.sleep((long) var31);
                                }
                            }

                            var14 += var19;
                            var15 += var20;
                        }
                    }
                }
            }

            this.user.oX = var29;
            this.user.oY = var30;
            this.user.fX = var14;
            this.user.fY = var15;
            var12 = var48 / 2;
            var25 -= var12;
            var26 -= var12;
            var27 += var12 + 1;
            var28 += var12 + 1;
            this.addD(var25, var26, var27, var28);
            if (this.user.wait == 0) {
                this.dBuffer(!this.user.isDirect, var25, var26, var27 + var49, var28 + var49);
            }
        } catch (RuntimeException var51) {
            var51.printStackTrace();
        }

    }

    private final void dFlush() {
        if (!this.user.isPre) {
            int width = this.info.W;
            int height = this.info.H;
            int x = this.user.X <= 0 ? 0 : this.user.X;
            int y = this.user.Y <= 0 ? 0 : this.user.Y;
            int x2 = this.user.X2 >= width ? width : this.user.X2;
            int y2 = this.user.Y2 >= height ? height : this.user.Y2;
            if (x2 - x > 0 && y2 - y > 0 && this.iLayer < this.info.L) {
                byte[] var9 = this.info.iMOffs;
                LO currentLayer = this.info.layers[this.iLayer];
                int var1;
                int var2;
                int[] var22;
                label156:
                switch (this.iPen) {
                    case P_SUISAI2:
                        this.dCMask(x, y, x2, y2);
                        break;
                    case P_MOSAIC:
                        currentLayer.reserve();
                        var22 = currentLayer.offset;
                        int var23 = this.iAlpha / 10 + 1;
                        x = x / var23 * var23;
                        y = y / var23 * var23;
                        int[] var21 = this.user.argb;
                        int var13 = y;

                        while (true) {
                            if (var13 >= y2) {
                                break label156;
                            }

                            for (var2 = x; var2 < x2; var2 += var23) {
                                int var26 = Math.min(var23, width - var2);
                                int var27 = Math.min(var23, height - var13);

                                int var18;
                                for (var18 = 0; var18 < 4; ++var18) {
                                    var21[var18] = 0;
                                }

                                int var19 = 0;

                                int pixel;
                                int var24;
                                int var25;
                                for (var25 = 0; var25 < var27; ++var25) {
                                    for (var24 = 0; var24 < var26; ++var24) {
                                        pixel = this.pix(var2 + var24, var13 + var25);
                                        var1 = (var13 + var25) * width + var2 + var24;

                                        for (var18 = 0; var18 < 4; ++var18) {
                                            var21[var18] += pixel >>> var18 * 8 & 255;
                                        }

                                        ++var19;
                                    }
                                }

                                pixel = var21[3] << 24 | var21[2] / var19 << 16 | var21[1] / var19 << 8 | var21[0] / var19;

                                for (var25 = var13; var25 < var13 + var27; ++var25) {
                                    var1 = width * var25 + var2;

                                    for (var24 = 0; var24 < var26; ++var24) {
                                        if (var9[var1] != 0) {
                                            var9[var1] = 0;
                                            var22[var1] = pixel;
                                        }

                                        ++var1;
                                    }
                                }
                            }

                            var13 += var23;
                        }
                    case P_LR:
                        currentLayer.dLR(x, y, x2, y2);
                        this.dCMask(x, y, x2, y2);
                        break;
                    case P_UD:
                        currentLayer.dUD(x, y, x2, y2);
                        this.dCMask(x, y, x2, y2);
                        break;
                    case P_R:
                        currentLayer.dR(x, y, x2, y2, (int[]) null);
                        this.dCMask(x, y, x2, y2);
                        this.addD(x, y, x + Math.max(x2 - x, y2 - y), y + Math.max(x2 - x, y2 - y));
                        break;
                    case P_FUSION:
                        byte var11 = this.iOffset > 8 ? this.offset[8] : 0;
                        LO var12 = this.info.layers[this.iLayerSrc];
                        var12.normalize(b255[this.iAlpha2 & 255], x, y, x2, y2);
                        currentLayer.normalize(b255[this.iAlpha2 >>> 8], x, y, x2, y2);
                        if (var12.offset == null) {
                            this.dCMask(x, y, x2, y2);
                        } else {
                            currentLayer.reserve();
                            LO var14 = currentLayer;
                            LO var15 = var12;
                            if (this.iLayer < this.iLayerSrc) {
                                var14 = var12;
                                var15 = currentLayer;
                            }

                            LO var16 = new LO();
                            LO var17 = new LO();
                            var16.setField(var14);
                            var17.setField(var15);
                            var14.iCopy = var11;
                            var15.reserve();
                            var14.dAdd(var15.offset, x, y, x2, y2, (int[]) null);
                            if (currentLayer != var15) {
                                var15.copyTo(x, y, x2, y2, var14, x, y, (int[]) null);
                            }

                            var12.clear(x, y, x2, y2);
                            var12.isDraw = true;
                            this.dCMask(x, y, x2, y2);
                            var14.setField(var16);
                            var15.setField(var17);
                        }
                        break;
                    default:
                        if (this.iHint != H_L && this.iHint != H_COPY) {
                            currentLayer.reserve();

                            for (var22 = currentLayer.offset; y < y2; ++y) {
                                var1 = y * width + x;

                                for (var2 = x; var2 < x2; ++var2) {
                                    var22[var1] = this.getM(var22[var1], var9[var1] & 255, var1);
                                    var9[var1] = 0;
                                    ++var1;
                                }
                            }
                        } else {
                            this.dCMask(x, y, x2, y2);
                        }
                }

                if (this.user.wait >= 0) {
                    this.dBuffer();
                }

            }
        }
    }

    private final void dCMask(int var1, int var2, int var3, int var4) {
        int var5 = var3 - var1;
        int var6 = this.info.W;
        int var7 = var2 * var6 + var1;
        byte[] var8 = this.info.iMOffs;

        int var9;
        for (var9 = 0; var9 < var5; ++var9) {
            var8[var7 + var9] = 0;
        }

        ++var2;
        var9 = var7;

        for (var7 += var6; var2 < var4; ++var2) {
            System.arraycopy(var8, var9, var8, var7, var5);
            var7 += var6;
        }

    }

    private final boolean dNext() throws InterruptedException {
        if (this.iSeek >= this.iOffset) {
            return false;
        } else {
            int var1 = this.user.pX[3] + this.rPo();
            int var2 = this.user.pY[3] + this.rPo();
            int var3 = this.iSOB != 0 ? this.ru() : 0;
            this.shift(var1, var2);
            this.user.iDCount = this.user.iDCount + 1;
            if (this.iHint != H_SP) {
                if (this.isAnti) {
                    this.dFLine((float) var1, (float) var2, var3);
                } else {
                    this.dFLine(var1, var2, var3);
                }
            } else if (this.user.iDCount >= 2) {
                this.dFLine2(var3);
            }

            return true;
        }
    }

    public final void dNext(int x, int y, int pressure, int step) throws InterruptedException, IOException {
        int var5 = this.info.scale;
        x = (x / var5 + this.info.scaleX) * this.info.Q;
        y = (y / var5 + this.info.scaleY) * this.info.Q;
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
                this.dFLine((float) x, (float) y, pressure);
            } else {
                this.dFLine(x, y, pressure);
            }

        }
    }

    private final void dPen(int var1, int var2, float var3) {
        if (this.iPen == P_SUISAI2) {
            if (!this.user.isPre) {
                this.dPY(var1, var2);
            }

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
            int[] var16 = this.info.layers[this.iLayer].offset;
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
                                    var9 = Math.max((int) ((float) var9 * b255[255 - var8 >>> 1] * var3), 1);
                                    var17[var6++] = (byte) Math.min(var8 + var9, 255);
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

    private final void dPY(int var1, int var2) {
        this.info.layers[this.iLayer].reserve();
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
            int[] var13 = this.info.layers[this.iLayer].offset;
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
                    this.dClear();
                    break;
                default:
                    this.dRetouch();
            }
        } catch (InterruptedException ex) {
            ;
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

        this.dEnd();
    }

    private void dRect(int var1, int var2, int var3, int var4) {
        int var5 = this.info.W;
        int var6 = this.info.H;
        byte[] var10 = this.info.iMOffs;
        byte var11 = (byte) this.iAlpha;
        if (var1 < 0) {
            var1 = 0;
        }

        if (var2 < 0) {
            var2 = 0;
        }

        if (var3 > var5) {
            var3 = var5;
        }

        if (var4 > var6) {
            var4 = var6;
        }

        if (var1 < var3 && var2 < var4 && var11 != 0) {
            this.setD(var1, var2, var3, var4);
            this.info.layers[this.iLayer].reserve();
            int[] var12 = this.info.layers[this.iLayer].offset;
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
                    var13 = var2;

                    while (true) {
                        if (var13 >= var4) {
                            break label139;
                        }

                        var7 = var13 * var5 + var1;

                        for (var9 = var1; var9 < var3; ++var9) {
                            if (!this.isM(var12[var7])) {
                                var10[var7] = var11;
                            }

                            ++var7;
                        }

                        ++var13;
                    }
                case H_FRECT:
                    var13 = var1;
                    var14 = var2;
                    var15 = var3;
                    var16 = var4;
                    var17 = 0;

                    while (true) {
                        if (var17 >= this.iSize + 1) {
                            break label139;
                        }

                        var7 = var5 * var14 + var13;
                        int var8 = var5 * (var16 - 1) + var13;

                        for (var9 = var13; var9 < var15; ++var9) {
                            if (!this.isM(var12[var7])) {
                                var10[var7] = var11;
                            }

                            if (!this.isM(var12[var8])) {
                                var10[var8] = var11;
                            }

                            ++var7;
                            ++var8;
                        }

                        var7 = var5 * var14 + var13;
                        var8 = var5 * var14 + var15 - 1;

                        for (var18 = var14; var18 < var16; ++var18) {
                            if (!this.isM(var12[var7])) {
                                var10[var7] = var11;
                            }

                            if (!this.isM(var12[var8])) {
                                var10[var8] = var11;
                            }

                            var7 += var5;
                            var8 += var5;
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
                    var13 = var3 - var1 - 1;
                    var14 = var4 - var2 - 1;
                    var17 = var13 / 2;
                    var18 = var14 / 2;
                    int var19 = Math.min(Math.min(this.iSize + 1, var17), var18);

                    int var20;
                    for (var20 = 0; var20 < var19; ++var20) {
                        for (float var21 = 0.0F; var21 < 7.0F; var21 = (float) ((double) var21 + 0.001D)) {
                            var15 = var1 + var17 + (int) Math.round(Math.cos((double) var21) * (double) (var17 - var20));
                            var16 = var2 + var18 + (int) Math.round(Math.sin((double) var21) * (double) (var18 - var20));
                            var10[var5 * var16 + var15] = var11;
                        }
                    }

                    if (this.iHint == H_OVAL && var17 > 0 && var18 > 0) {
                        var20 = this.iColor;
                        this.iColor = var11;
                        this.dFill(var10, var1, var2, var3, var4);
                        this.iColor = var20;
                    }

                    for (var20 = var2; var20 < var4; ++var20) {
                        var7 = var20 * var5 + var1;

                        for (var9 = var1; var9 < var3; ++var9) {
                            if (this.isM(var12[var7])) {
                                var10[var7] = 0;
                            }

                            ++var7;
                        }
                    }
            }

            this.t();
        }
    }

    public void dRetouch() throws InterruptedException {
        try {
            this.getPM();
            this.user.setup(this);
            int var1 = this.user.pW / 2;
            int var2 = this.info.W;
            int var3 = this.info.H;
            LO[] layers = this.info.layers;
            this.setD(0, 0, 0, 0);
            int[] points = this.user.points; // coordinates of two short x,y for each int -OR- a set of parameters
            int var6 = this.isText() ? 1 : 4;

            int pointX;
            for (pointX = 0; pointX < var6 && this.iSeek < this.iOffset; ++pointX) {
                points[pointX] = (this.r2() & 0xFFFF) << 16 | this.r2() & '\uffff';
            }

            label99:
            {
                pointX = points[0] >> 16;
                short pointY = (short) points[0];
                switch (this.iHint) {
                    case H_BEZI:
                        int var9 = this.user.wait;
                        this.user.wait = -2;
                        this.dStart(pointX + var1, pointY + var1, 0, false, false);
                        this.dBz(points);
                        this.user.wait = var9;
                        break label99;
                    case H_RECT:
                    case H_FRECT:
                    case H_OVAL:
                    case H_FOVAL:
                    case H_CLEAR:
                    case H_SP:
                    case H_UNKNOWN13:
                    default:
                        this.dRect(pointX, pointY, points[1] >> 16, (short) points[1]);
                        break label99;
                    case H_FILL:
                        this.dFill(pointX, pointY);
                        break label99;
                    case H_TEXT:
                    case H_VTEXT:
                        String var18 = new String(this.offset, this.iSeek, this.iOffset - this.iSeek, ENCODE);
                        int var21 = var18.indexOf(0);
                        this.dText(var18.substring(var21 + 1), pointX, pointY);
                        break label99;
                    case H_COPY:
                        this.dCopy(points);
                        break label99;
                    case H_L:
                }

                LO layer = layers[this.iLayer];
                switch (pointY) { // here pointY is a command
                    case 0: // swap layers
                        this.info.swapL(this.iLayerSrc, this.iLayer);
                        break;
                    case 1: // set number of layers
                        this.info.setL(points[1]);
                        break;
                    case 2: // delete layer
                        this.info.delL(this.iLayerSrc);
                        break;
                    case 3: // shift layers
                        //if the layer being dragged is above the destination, shift up the in-between
                        if (this.iLayer > this.iLayerSrc) {
                            for (int i = this.iLayerSrc; i < this.iLayer; ++i) {
                                this.info.swapL(i, i + 1);
                            }
                        }
                        // if I'm trying to move it above, shift down the in-between
                        if (this.iLayer < this.iLayerSrc) {
                            for (int i = this.iLayerSrc; i > this.iLayer; --i) {
                                this.info.swapL(i, i - 1);
                            }
                        }
                    case 4:
                    default:
                        break;
                    case 5:
                    case 8: // change layer alpha
                        layer.iAlpha = b255[this.offset[4] & 255];
                        break;
                    case 6:
                        try {
                            Toolkit toolkit = this.info.component.getToolkit();
                            pointX = points[1] >> 16;
                            pointY = (short) points[1];
                            Image img;
                            if ((points[2] & 255) == 1) {
                                img = toolkit.createImage(this.offset, this.iSeek, this.iOffset - this.iSeek);
                            } else {
                                img = toolkit.createImage((byte[]) this.info.cnf.getRes(new String(this.offset, this.iSeek, this.iOffset - this.iSeek, ENCODE)));
                            }

                            if (img != null) {
                                Awt.wait(img);
                                int imgWidth = img.getWidth((ImageObserver) null);
                                int imgHeight = img.getHeight((ImageObserver) null);
                                int[] srgb = Awt.getPix(img);
                                img.flush();
                                img = null; // probably for the garbage collector
                                if (imgWidth > 0 && imgHeight > 0) {
                                    layers[this.iLayer].toCopy(imgWidth, imgHeight, srgb, pointX, pointY);
                                }
                            }
                        } catch (Throwable ex) {
                            ex.printStackTrace();
                        }
                        break;
                    case 7: // merge visible layers
                        byte width = this.offset[4];
                        byte[] dest = new byte[width * 4];
                        System.arraycopy(this.offset, 6, dest, 0, width * 4);
                        this.dFusion(dest);
                        break;
                    case 9: // set blending mode
                        layer.iCopy = this.offset[4];
                        break;
                    case 10: // set layer name
                        layer.name = new String(this.offset, 4, this.iOffset - 4, ENCODE);
                }

                this.setD(0, 0, var2, var3);
            }

            if (this.isOver) {
                this.dFlush();
            }

            if (this.user.wait >= 0) {
                this.dBuffer();
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

    }

    private void dStart() {
        try {
            short var1 = this.r2();
            short var2 = this.r2();
            this.user.setup(this);
            this.info.layers[this.iLayer].reserve();
            int var3 = this.iSOB != 0 ? this.ru() : 0;
            if (this.iSOB != 0) {
                this.iSize = this.ss(var3);
                this.iAlpha = this.sa(var3);
            }

            this.memset((int[]) this.user.pX, (int) var1);
            this.memset((int[]) this.user.pY, (int) var2);
            int var4 = this.user.pW / 2;
            this.setD(var1 - var4 - 1, var2 - var4 - 1, var1 + var4, var2 + var4);
            this.user.fX = (float) var1;
            this.user.fY = (float) var2;
            if (this.iHint != H_SP && !this.isAnti) {
                this.dFLine(var1, var2, var3);
            }
        } catch (RuntimeException var5) {
            var5.printStackTrace();
        } catch (InterruptedException var6) {
            ;
        }

    }

    /** Starts drawing */
    public void dStart(int x, int y, int var3, boolean var4, boolean var5) {
        try {
            this.user.setup(this);
            this.info.layers[this.iLayer].reserve();
            this.iSize = this.ss(var3);
            this.iAlpha = this.sa(var3);
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
                    var9.write(var3);
                }
            }

            this.memset(this.user.pX, x);
            this.memset(this.user.pY, y);
            var6 = this.user.pW / 2;
            this.setD(x - var6 - 1, y - var6 - 1, x + var6, y + var6);
            this.user.fX = (float) x;
            this.user.fY = (float) y;
            if (this.iHint != H_SP && !this.isAnti) {
                this.dFLine(x, y, var3);
            }
        } catch (IOException var7) {
            var7.printStackTrace();
        } catch (InterruptedException var8) {
            var8.printStackTrace();
        }

    }

    private void dText(String var1, int var2, int var3) {
        try {
            int var4 = this.info.W;
            int var5 = this.info.H;
            int[] var6 = this.info.layers[this.iLayer].offset;
            byte[] var7 = this.info.iMOffs;
            float var8 = b255[this.iAlpha];
            if (var8 == 0.0F) {
                return;
            }

            Font var9 = this.getFont(this.iSize);
            FontMetrics var10 = this.info.component.getFontMetrics(var9);
            if (var1 == null || var1.length() <= 0) {
                return;
            }

            this.info.layers[this.iLayer].reserve();
            boolean var11 = this.iHint == H_TEXT;
            int var12 = var10.getMaxAdvance();
            int var13 = var10.getMaxAscent() + var10.getMaxDescent() + var10.getLeading() + 2;
            int var14 = var10.getMaxAscent() + var10.getLeading() / 2 + 1;
            int var17 = var1.length();
            int var15;
            int var16;
            if (var11) {
                var15 = var12 * (var17 + 1) + 2;
                var16 = var13;
            } else {
                var12 = var10.getMaxAdvance();
                var15 = var12 + 2;
                var16 = (var13 + this.iCount) * (var17 + 1);
            }

            var15 = Math.min(var15, var4);
            var16 = Math.min(var16, var5);
            this.setD(var2, var3, var2 + var15, var3 + var16);
            Image var18 = this.info.component.createImage(var15, var16);
            Graphics var19 = var18.getGraphics();
            var19.setFont(var9);
            var19.setColor(Color.black);
            var19.fillRect(0, 0, var15, var16);
            var19.setColor(Color.blue);
            int var21;
            if (var11) {
                var19.drawString((String) var1, 1, var14);
            } else {
                int var20 = var14;

                for (var21 = 0; var21 < var17; ++var21) {
                    var19.drawString((String) String.valueOf(var1.charAt(var21)), 1, var20);
                    var20 += var13 + this.iCount;
                }
            }

            var19.dispose();
            var19 = null;
            var9 = null;
            var10 = null;
            int[] var28 = Awt.getPix(var18);
            var18.flush();
            var18 = null;
            boolean var29 = false;
            int var23 = Math.min(var4 - var2, var15);
            int var24 = Math.min(var5 - var3, var16);

            for (int var25 = 0; var25 < var24; ++var25) {
                var21 = var25 * var15;
                int var22 = (var25 + var3) * var4 + var2;

                for (int var26 = 0; var26 < var23; ++var26) {
                    //FIXME: var6 can be null the first time if the user uses text before any other tool
                    if (!this.isM(var6[var22])) {
                        var7[var22] = (byte) ((int) ((float) (var28[var21] & 255) * var8));
                    }

                    ++var21;
                    ++var22;
                }
            }

            this.setD(var2, var3, var2 + var15, var3 + var16);
            this.t();
        } catch (Exception var27) {
            var27.printStackTrace();
        }

    }

    private final int fu(int var1, int var2, int var3) {
        if (var3 == 0) {
            return var1;
        } else {
            int var4 = var1 >>> 24;
            int var5 = var4 + (int) ((float) var3 * b255[255 - var4]);
            float var6 = b255[Math.min((int) ((float) var3 * b255d[var5]), 255)];
            int var7 = var1 >>> 16 & 255;
            int var8 = var1 >>> 8 & 255;
            int var9 = var1 & 255;
            return var5 << 24 | var7 + (int) ((float) ((var2 >>> 16 & 255) - var7) * var6) << 16 | var8 + (int) ((float) ((var2 >>> 8 & 255) - var8) * var6) << 8 | var9 + (int) ((float) ((var2 & 255) - var9) * var6);
        }
    }

    public final void get(OutputStream out, ByteStream bs, M mg) {
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

            if (this.isText()) {
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

    private final int getFlag(M mg) {
        // F1 flags
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
            return this.info.component.createImage(new MemoryImageSource(width, height, this.info.layers[layerNumber].offset, offY * this.info.W + offX, this.info.W));
        } else {
            Image img = this.info.component.createImage(new MemoryImageSource(width * quality, height * quality, this.info.layers[layerNumber].offset, offY * quality * this.info.W + offX * quality, this.info.W));
            Image imgScaled = img.getScaledInstance(width, height, 2);
            img.flush();
            return imgScaled;
        }
    }

    private final int getM(int pixel, int alpha, int var3) {
        if (alpha == 0) {
            return pixel;
        } else {
            // TODO: the compiled code was probably reusing variables, clean up later
            int pxG;
            int pxAlpha;
            int pxR;
            int pxB;
            float fAlpha;
            switch (this.iPen) {
                case P_WHITE:
                case P_SWHITE:
                    pxAlpha = pixel >>> 24;
                    pxR = pxAlpha - (int) ((float) pxAlpha * b255[alpha]);
                    return pxR == 0 ? 0xFFFFFF : pxR << 24 | pixel & 0xFFFFFF;
                case P_LIGHT:
                case P_LPEN:
                    pxAlpha = pixel >>> 24;
                    pxR = pixel >>> 16 & 255;
                    pxG = pixel >>> 8 & 255;
                    pxB = pixel & 255;
                    fAlpha = b255[alpha];
                    return (pxAlpha << 24) + (Math.min(pxR + (int) ((float) pxR * fAlpha), 255) << 16) + (Math.min(pxG + (int) ((float) pxG * fAlpha), 255) << 8) + Math.min(pxB + (int) ((float) pxB * fAlpha), 255);
                case P_DARK:
                    pxAlpha = pixel >>> 24;
                    pxR = pixel >>> 16 & 255;
                    pxG = pixel >>> 8 & 255;
                    pxB = pixel & 255;
                    fAlpha = b255[alpha];
                    return (pxAlpha << 24) + (Math.max(pxR - (int) ((float) (255 - pxR) * fAlpha), 0) << 16) + (Math.max(pxG - (int) ((float) (255 - pxG) * fAlpha), 0) << 8) + Math.max(pxB - (int) ((float) (255 - pxB) * fAlpha), 0);
                case P_BOKASHI:
                    float var4 = b255[alpha];
                    int[] var5 = this.user.argb;
                    int[] var7 = this.info.layers[this.iLayer].offset;
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
        if (!this.isText() && (this.iHint < H_RECT || this.iHint > H_FOVAL)) {
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
        } else {
            return null;
        }
    }

    private final float getTT(int var1, int var2) {
        if (this.iTT == 0) {
            return 1.0F;
        } else if (this.iTT < 12) {
            return (float) (isTone(this.iTT - 1, var1, var2) ? 0 : 1);
        } else {
            int var3 = this.user.pTTW;
            return this.user.pTT[var2 % var3 * var3 + var1 % var3];
        }
    }

    private final ByteStream getWork() {
        this.info.workOut.reset();
        return this.info.workOut;
    }

    private final boolean isM(int var1) {
        if (this.iMask == M_N) {
            return false;
        } else {
            var1 &= 0xFFFFFF;
            return this.iMask == M_M ? this.iColorMask == var1 : (this.iMask == M_R ? this.iColorMask != var1 : false);
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
            int[] var5 = Awt.getPix(var4);
            int var6 = var5.length;
            var4.flush();
            var4 = null;
            int var7;
            if (var2) {
                for (var7 = 0; var7 < var6; ++var7) {
                    var5[var7] = var5[var7] & 255 ^ 255;
                }
            } else {
                for (var7 = 0; var7 < var6; ++var7) {
                    var5[var7] &= 255;
                }
            }

            return var5;
        } catch (RuntimeException var8) {
            return null;
        }
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
        int var3 = var1.length >>> 1;

        for (int var4 = 0; var4 < var3; ++var4) {
            var1[var4] = var2;
        }

        System.arraycopy(var1, 0, var1, var3 - 1, var3);
        var1[var3 + var3 - 1] = var2;
    }

    public final void memset(int[] var1, int var2) {
        int var3 = var1.length >>> 1;

        for (int var4 = 0; var4 < var3; ++var4) {
            var1[var4] = var2;
        }

        System.arraycopy(var1, 0, var1, var3 - 1, var3);
        var1[var3 + var3 - 1] = var2;
    }

    public final void memset(byte[] var1, byte var2) {
        int var3 = var1.length >>> 1;

        for (int var4 = 0; var4 < var3; ++var4) {
            var1[var4] = var2;
        }

        System.arraycopy(var1, 0, var1, var3 - 1, var3);
        var1[var3 + var3 - 1] = var2;
    }

    public final Image mkLPic(int[] picture, int x, int y, int width, int height, int quality) {
        x *= quality;
        y *= quality;
        width *= quality;
        height *= quality;
        boolean isEmpty = picture == null;
        int var8 = this.info.L;
        LO[] var9 = this.info.layers;
        if (isEmpty) {
            picture = this.user.buffer;
        }

        this.memset(picture, 0xFFFFFF);

        for (int var10 = 0; var10 < var8; ++var10) {
            var9[var10].draw(picture, x, y, x + width, y + height, width);
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
        int[] var6 = this.user.buffer;
        int var7 = this.info.L;
        LO[] var8 = this.info.layers;
        this.memset(var6, 0xFFFFFF);

        label87:
        for (int var9 = 0; var9 < var7; ++var9) {
            if (var9 != this.iLayer) {
                var8[var9].draw(var6, x, y, x + width, y + height, width);
            } else {
                byte[] var10 = this.info.iMOffs;
                int[] var14 = var8[var9].offset;
                int var19 = this.info.W;
                float var22 = var8[var9].iAlpha;
                if (var14 != null) {
                    int var11;
                    int var12;
                    int var13;
                    int var15;
                    int var16;
                    int var17;
                    int var18;
                    int var20;
                    int var21;
                    float var23;
                    switch (var8[var9].iCopy) {
                        case 1:
                            var18 = 0;

                            while (true) {
                                if (var18 >= height) {
                                    continue label87;
                                }

                                var15 = var19 * (var18 + y) + x;
                                var16 = width * var18;

                                for (var17 = 0; var17 < width; ++var17) {
                                    var21 = var6[var16];
                                    var20 = this.getM(var14[var15], var10[var15] & 255, var15);
                                    var23 = b255[var20 >>> 24] * var22;
                                    if (var23 > 0.0F) {
                                        var6[var16] = ((var21 >>> 16 & 255) - (int) (b255[var21 >>> 16 & 255] * (float) (var20 >>> 16 & 255 ^ 255) * var23) << 16) + ((var21 >>> 8 & 255) - (int) (b255[var21 >>> 8 & 255] * (float) (var20 >>> 8 & 255 ^ 255) * var23) << 8) + ((var21 & 255) - (int) (b255[var21 & 255] * (float) (var20 & 255 ^ 255) * var23));
                                    }

                                    ++var16;
                                    ++var15;
                                }

                                ++var18;
                            }
                        case 2:
                            var18 = 0;

                            while (true) {
                                if (var18 >= height) {
                                    continue label87;
                                }

                                var15 = var19 * (var18 + y) + x;
                                var16 = width * var18;

                                for (var17 = 0; var17 < width; ++var17) {
                                    var21 = var6[var16];
                                    var20 = this.getM(var14[var15], var10[var15] & 255, var15);
                                    var23 = b255[var20 >>> 24] * var22;
                                    var20 ^= 0xFFFFFF;
                                    var11 = var21 >>> 16 & 255;
                                    var12 = var21 >>> 8 & 255;
                                    var13 = var21 & 255;
                                    var6[var16++] = var23 == 1.0F ? var20 : var11 + (int) ((float) ((var20 >>> 16 & 255) - var11) * var23) << 16 | var12 + (int) ((float) ((var20 >>> 8 & 255) - var12) * var23) << 8 | var13 + (int) ((float) ((var20 & 255) - var13) * var23);
                                    ++var15;
                                }

                                ++var18;
                            }
                        default:
                            var18 = 0;
                    }

                    while (var18 < height) {
                        var15 = var19 * (var18 + y) + x;
                        var16 = width * var18;

                        for (var17 = 0; var17 < width; ++var17) {
                            var21 = var6[var16];
                            var20 = this.getM(var14[var15], var10[var15] & 255, var15);
                            var23 = b255[var20 >>> 24] * var22;
                            if (var23 == 1.0F) {
                                var6[var16++] = var20;
                            } else {
                                var11 = var21 >>> 16 & 255;
                                var12 = var21 >>> 8 & 255;
                                var13 = var21 & 255;
                                var6[var16++] = var11 + (int) ((float) ((var20 >>> 16 & 255) - var11) * var23) << 16 | var12 + (int) ((float) ((var20 >>> 8 & 255) - var12) * var23) << 8 | var13 + (int) ((float) ((var20 & 255) - var13) * var23);
                            }

                            ++var15;
                        }

                        ++var18;
                    }
                }
            }
        }

        this.user.raster.newPixels(this.user.image, width, height, quality);
        return this.user.image;
    }

    public M.Info newInfo(Applet app, Component component, Res cnf) {
        if (this.info != null) {
            return this.info;
        } else {
            this.info = new M.Info();
            this.info.cnf = cnf;
            this.info.component = component;
            M.Info var4 = this.info;
            M var5 = this.info.m;
            float var10 = 3.1415927F;

            int var6;
            for (var6 = 1; var6 < 256; ++var6) {
                b255[var6] = (float) var6 / 255.0F;
                b255d[var6] = 255.0F / (float) var6;
            }

            b255[0] = 0.0F;
            b255d[0] = 0.0F;
            int[][][] var11 = this.info.bPen;
            boolean var15 = false;
            int var17 = 1;
            short var19 = 255;
            var5.iAlpha = 255;
            this.set(var5);
            int[][] var12 = new int[23][];

            int var7;
            int var8;
            int var9;
            int[] var13;
            int var18;
            for (var6 = 0; var6 < 23; ++var6) {
                var8 = var17 * var17;
                if (var17 <= 6) {
                    var12[var6] = var13 = new int[var8];
                    var7 = 0;

                    while (true) {
                        if (var7 >= var8) {
                            if (var17 >= 3) {
                                var13[0] = var13[var17 - 1] = var13[var17 * (var17 - 1)] = var13[var8 - 1] = 0;
                            }
                            break;
                        }

                        var13[var7] = var7 >= var17 && var8 - var7 >= var17 && var7 % var17 != 0 && var7 % var17 != var17 - 1 ? var5.iAlpha : var19;
                        ++var7;
                    }
                } else {
                    var18 = var17 + 1;
                    var12[var6] = var13 = new int[var18 * var18];
                    int var16 = (var17 - 1) / 2;
                    var9 = (int) ((float) Math.round(2.0F * var10 * (float) var16) * 3.0F);

                    for (var7 = 0; var7 < var9; ++var7) {
                        int var14 = Math.min(var16 + (int) Math.round((double) var16 * Math.cos((double) var7)), var17);
                        int var29 = Math.min(var16 + (int) Math.round((double) var16 * Math.sin((double) var7)), var17);
                        var13[var29 * var18 + var14] = var19;
                    }

                    var4.W = var4.H = var18;
                    this.dFill((int[]) var13, 0, 0, var18, var18);
                }

                var17 += var6 <= 7 ? 1 : (var6 < 18 ? 2 : 4);
            }

            var11[0] = var12;
            var5.iAlpha = 255;
            var12 = new int[32][];
            var12[0] = new int[]{128};
            var12[1] = new int[]{255};
            var12[2] = new int[]{0, 128, 0, 128, 255, 128, 0, 128, 0};
            var12[3] = new int[]{128, 174, 128, 174, 255, 174, 128, 174, 128};
            var12[4] = new int[]{174, 255, 174, 255, 255, 255, 174, 255, 174};
            var12[5] = new int[9];
            this.memset((int[]) var12[5], (int) 255);
            var12[6] = new int[]{0, 128, 128, 0, 128, 255, 255, 128, 128, 255, 255, 128, 0, 128, 128, 0};
            var13 = var12[7] = new int[16];
            this.memset((int[]) var13, (int) 255);
            var13[0] = var13[3] = var13[15] = var13[12] = 128;
            this.memset((int[]) (var12[8] = new int[16]), (int) 255);
            var17 = 3;

            for (var6 = 9; var6 < 32; ++var6) {
                var18 = var17 + 3;
                float var24 = (float) var17 / 2.0F;
                var12[var6] = var13 = new int[var18 * var18];
                var9 = (int) ((float) Math.round(2.0F * var10 * var24) * (float) (2 + var6 / 16)) + var6 * 2;

                for (var7 = 0; var7 < var9; ++var7) {
                    float var20;
                    int var25 = (int) (var20 = var24 + 1.5F + var24 * (float) Math.cos((double) var7));
                    float var21;
                    int var26 = (int) (var21 = var24 + 1.5F + var24 * (float) Math.sin((double) var7));
                    float var22 = var20 - (float) var25;
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
                var4.W = var4.H = var18;
                this.dFill((int[]) var13, 0, 0, var18, var18);
            }

            var11[1] = var12;
            this.set((M) null);
            var5.set((M) null);
            if (cnf != null) {
                for (var6 = 0; var6 < 16; ++var6) {
                    for (var9 = 0; cnf.get((Object) ("pm" + var6 + '/' + var9 + ".gif")) != null; ++var9) {
                        ;
                    }

                    if (var9 > 0) {
                        var11[var6] = new int[var9][];

                        for (var7 = 0; var7 < var9; ++var7) {
                            var11[var6][var7] = this.loadIm("pm" + var6 + '/' + var7 + ".gif", true);
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

    public M.User newUser(Component var1) {
        if (this.user == null) {
            this.user = new M.User();
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
            return this.info.layers[this.iLayer].getPixel(x, y);
        } else {
            int var3 = this.info.L;
            int var5 = 0;
            int var7 = 0xFFFFFF;

            for (int var10 = 0; var10 < var3; ++var10) {
                int var8 = this.info.layers[var10].getPixel(x, y);
                float var9 = b255[var8 >>> 24];
                if (var9 != 0.0F) {
                    if (var9 == 1.0F) {
                        var7 = var8;
                        var5 = 255;
                    }

                    var5 = (int) ((float) var5 + (float) (255 - var5) * var9);
                    int var4 = 0;

                    for (int var11 = 16; var11 >= 0; var11 -= 8) {
                        int var6 = var7 >>> var11 & 255;
                        var4 |= var6 + (int) ((float) ((var8 >>> var11 & 255) - var6) * var9) << var11;
                    }

                    var7 = var4;
                }
            }

            return var5 << 24 | var7;
        }
    }

    /** Reads one byte from this.offset */
    private final byte r() {
        return this.iSeek >= this.iOffset ? 0 : this.offset[this.iSeek++];
    }

    /** Returns an int made of "length" bytes from "buffer" at "seek" position */
    private final int r(byte[] buffer, int seek, int length) {
        int out = 0;

        for (int i = length - 1; i >= 0; --i) {
            out |= (buffer[seek++] & 0xFF) << i * 8;
        }

        return out;
    }

    private final short r2() {
        return (short) ((this.ru() << 8) + this.ru());
    }

    public void reset(boolean var1) {
        byte[] var3 = this.info.iMOffs;
        int var4 = this.info.W;
        int var5 = Math.max(this.user.X, 0);
        int var6 = Math.max(this.user.Y, 0);
        int var7 = Math.min(this.user.X2, var4);
        int var8 = Math.min(this.user.Y2, this.info.H);

        for (int var10 = var6; var10 < var8; ++var10) {
            int var2 = var5 + var10 * var4;

            for (int var9 = var5; var9 < var7; ++var9) {
                var3[var2++] = 0;
            }
        }

        if (var1) {
            this.dBuffer(false, var5, var6, var7, var8);
        }

        this.setD(0, 0, 0, 0);
    }

    private final int rPo() {
        byte var1 = this.r();
        return var1 != -128 ? var1 : this.r2();
    }

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

    /** Sets class data from a buffer */
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

                if (this.isText()) {
                    int var10 = this.r(buffer, offset, 2);
                    offset += 2;
                    if (var10 == 0) {
                        this.strHint = null;
                    } else {
                        this.strHint = new byte[var10];
                        System.arraycopy(buffer, offset, this.strHint, 0, var10);
                        offset += var10;
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
            } catch (RuntimeException var11) {
                var11.printStackTrace();
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

            //Field[] var2 = this.getClass().getDeclaredFields();
            Field[] fieldArr = this.getClass().getFields();
            int cmdLength = commands.indexOf('@');
            if (cmdLength < 0) {
                cmdLength = commands.length();
            }

            int posEq;
            //TODO: could be simplified with a StringTokenizer
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
                    ;
                } catch (IllegalAccessException ex) {
                    ;
                }
            }

            if (cmdLength != commands.length()) {
                ByteStream bs = this.getWork();

                for (int j = cmdLength + 1; j < commands.length(); j += 2) {
                    bs.write(Character.digit((char) commands.charAt(j), 16) << 4 | Character.digit((char) commands.charAt(j + 1), 16));
                }

                this.offset = bs.toByteArray();
                this.iOffset = this.offset.length;
            }
        } catch (Throwable ex) {
            ;
        }

    }

    /** Copies data from another M instance */
    public final void set(M mg) {
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

    public final int set(ByteStream bs) {
        return this.set(bs.getBuffer(), 0);
    }

    public void setRetouch(int[] points, byte[] buffer, int length, boolean isDrawing) {
        try {
            int pointCount = 4;
            int scale = this.info.scale;
            int quality = this.info.Q;
            int scaleX = this.info.scaleX;
            int scaleY = this.info.scaleY;
            this.getPM();
            // calculations for bezier curves
            int halfSqrtSize = this.user.pW / 2; // half square root of the pen size
            int bezierOffset = this.iHint == H_BEZI ? halfSqrtSize : 0;

            int[] userPoints = this.user.points;
            switch (this.iHint) {
                case H_BEZI:
                case H_FILL:
                case H_L:
                    break; // 4 points
                case H_RECT:
                case H_FRECT:
                case H_OVAL:
                case H_FOVAL:
                case H_SP:
                case H_UNKNOWN13:
                default:
                    pointCount = 2; // area:(x,y)(x2,y2)
                    break;
                case H_TEXT:
                case H_VTEXT:
                    pointCount = 1; // position:(x,y)
                    break;
                case H_COPY:
                    pointCount = 3; // source:(x,y)(x2,y2) destination:(x3,y3)
                    break;
                case H_CLEAR:
                    pointCount = 0;
            }

            if (points != null) {
                pointCount = Math.min(pointCount, points.length);
            }

            for (int i = 0; i < pointCount; ++i) {
                int posX = points[i] >> 16;
                int posY = (short) points[i];
                if (isDrawing) {
                    // adjust with scaling, quality and... the bezier thing?
                    posX = (posX / scale + scaleX) * quality - bezierOffset;
                    posY = (posY / scale + scaleY) * quality - bezierOffset;
                }

                userPoints[i] = posX << 16 | posY & 0xFFFF;
            }

            ByteStream bs = this.getWork();

            for (int i = 0; i < pointCount; ++i) {
                bs.w((long) userPoints[i], 4);
            }

            if (buffer != null && length > 0) {
                bs.write(buffer, 0, length);
            }

            this.offset = bs.writeTo(this.offset, 0);
            this.iOffset = bs.size();
            bs.reset();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

    }

    private final void addD(int x, int y, int x2, int y2) {
        this.user.addRect(x, y, x2, y2);
    }

    private final void setD(int x, int y, int x2, int y2) {
        this.user.setRect(x, y, x2, y2);
    }

    public void setInfo(M.Info info) {
        this.info = info;
    }

    public void setUser(M.User user) {
        this.user = user;
    }

    /** Shifts left the arrays pX,pY then adds x,y at their 4th position */
    private final void shift(int x, int y) {
        System.arraycopy(this.user.pX, 1, this.user.pX, 0, 3);
        System.arraycopy(this.user.pY, 1, this.user.pY, 0, 3);
        this.user.pX[3] = x;
        this.user.pY[3] = y;
    }

    private final int ss(int var1) {
        if ((this.iSOB & F1_AFIX) == 0) {
            return this.iSize;
        } else {
            int var2 = this.iSS & 255;
            return (int) (((float) var2 + b255[(this.iSS >>> 8) - var2] * (float) var1) * this.user.pV);
        }
    }

    private final void t() {
        if (this.iTT != 0) {
            byte[] mOffs = this.info.iMOffs;
            int width = this.info.W;
            int x = this.user.X;
            int y = this.user.Y;
            int x2 = this.user.X2;
            int y2 = this.user.Y2;

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

    public boolean isText() {
        return this.iHint == H_TEXT || this.iHint == H_VTEXT;
    }

    public Font getFont(int var1) {
        try {
            if (this.strHint != null) {
                return Font.decode(new String(this.strHint, ENCODE) + var1);
            }
        } catch (IOException ex) {
            ;
        }

        return new Font("sansserif", 0, this.iSize);
    }

    public static float[] getb255() {
        return b255;
    }

    public boolean eq(M var1) {
        Field[] var2 = this.getClass().getFields();

        try {
            for (int var4 = 0; var4 < var2.length; ++var4) {
                Field var3 = var2[var4];
                if (var3.getName().charAt(0) == 'i' && var3.get(this) != null && var3.get(var1) != null && !var3.get(this).equals(var3.get(var1))) {
                    return false;
                }
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

        return true;
    }

    public class User {
        private Image image = null;
        private SRaster raster = null;
        private int[] buffer = new int[65536];
        private int[] argb = new int[4];
        public int[] points = new int[6];
        private int[] p = null;
        private int pW;
        private int pM = -1;
        private int pA = -1;
        private int pS = -1;
        private float pV = 1.0F;
        private float[] pTT = null;
        private int pTTW;
        private boolean isDirect;
        public int wait = 0;
        public boolean isPre = false;
        private int[] pX = new int[4];
        private int[] pY = new int[4];
        private int oX;
        private int oY;
        private float fX;
        private float fY;
        private int iDCount;
        private int X;
        private int Y;
        private int X2;
        private int Y2;
        private int count = 0;
        private int countMax;

        private void setup(M mg) {
            this.pV = M.b255[mg.info.bPen[mg.iPenM].length - 1];
            mg.getPM();
            this.count = 0;
            this.iDCount = 0;
            this.oX = -1000;
            this.oY = -1000;
            this.isDirect = mg.iPen == P_SUISAI2 || mg.iHint == H_COPY || mg.isOver;
            if (M.this.info.L <= mg.iLayer) {
                M.this.info.setL(mg.iLayer + 1);
            }

            M.this.info.layers[mg.iLayer].isDraw = true;
            if (mg.iTT >= 12) {
                this.pTT = M.this.info.getTT(mg.iTT);
                this.pTTW = (int) Math.sqrt((double) this.pTT.length);
            }

        }

        public void setIm(M mg) {
            if (!mg.isText()) {
                if (this.pM != mg.iPenM || this.pA != mg.iAlpha || this.pS != mg.iSize) {
                    int[] var2 = mg.info.bPen[mg.iPenM][mg.iSize];
                    int var3 = var2.length;
                    if (this.p == null || this.p.length < var3) {
                        this.p = new int[var3];
                    }

                    float var4 = M.b255[mg.iAlpha];

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
            int var3 = M.this.info.imW;
            if (x >= 0 && y >= 0 && x < var3 && y < M.this.info.imH) {
                int quality = M.this.info.Q;
                M.this.mkLPic(this.buffer, x, y, 1, 1, quality);
                LO[] var5 = M.this.info.layers;
                x *= quality;
                y *= quality;
                float var6 = 0.0F;

                for (int i = M.this.info.m.iLayer; i >= 0; --i) {
                    var6 += (1.0F - var6) * M.b255[var5[i].getPixel(x, y) >>> 24] * var5[i].iAlpha;
                    if (var6 >= 1.0F) {
                        break;
                    }
                }

                return ((int) (var6 * 255.0F) << 24) + (this.buffer[0] & 0xFFFFFF);
            } else {
                return 0;
            }
        }

        public int[] getBuffer() {
            return this.buffer;
        }

        public long getRect() {
            return (long) (this.X <= 0 ? 0 : this.X) << 48 | (long) (this.Y <= 0 ? 0 : this.Y) << 32 | (long) (this.X2 << 16) | (long) this.Y2;
        }

        public void setRect(int x, int y, int x2, int y2) {
            this.X = x;
            this.Y = y;
            this.X2 = x2;
            this.Y2 = y2;
        }

        public final void addRect(int x, int y, int x2, int y2) {
            this.setRect(Math.min(x, this.X), Math.min(y, this.Y), Math.max(x2, this.X2), Math.max(y2, this.Y2));
        }

        public Image mkImage(int var1, int var2) {
            this.raster.newPixels(this.image, this.buffer, var1, var2);
            return this.image;
        }
    }

    public class Info {
        private ByteStream workOut = new ByteStream();
        public boolean isLEdit = false;
        public boolean isFill = false;
        public boolean isClean = false;
        public long permission = -1L;
        public long unpermission = 0L;
        private Res cnf;
        private String dirTT = null;
        public Graphics g = null;
        private int vWidth;
        private int vHeight;
        private Dimension vD = new Dimension();
        private Component component = null;
        public int Q = 1;
        public int L;
        public LO[] layers = null;
        public int scale = 1;
        public int scaleX = 0;
        public int scaleY = 0;
        private byte[] iMOffs;
        public int imH;
        public int imW;
        public int W;
        public int H;
        private int[][][] bPen = new int[16][][];
        private float[][] bTT = new float[14][];
        public M m = new M();

        public void setSize(int width, int height, int quality) {
            int actualWidth = width * quality;
            int actualHeight = height * quality;
            if (actualWidth != this.W || actualHeight != this.H) {
                for (int i = 0; i < this.L; ++i) {
                    this.layers[i].setSize(actualWidth, actualHeight);
                }
            }

            this.imW = width;
            this.imH = height;
            this.W = actualWidth;
            this.H = actualHeight;
            this.Q = quality;
            int size = this.W * this.H;
            if (this.iMOffs == null || this.iMOffs.length < size) {
                this.iMOffs = new byte[size];
            }

        }

        public void setLayers(LO[] layers) {
            this.L = layers.length;
            this.layers = layers;
        }

        public void setComponent(Component cmp, Graphics g, int width, int height) {
            this.component = cmp;
            this.vWidth = width;
            this.vHeight = height;
            this.g = g;
        }

        /** Changes the number of layers */
        public void setL(int n) {
            int layerCount = this.layers == null ? 0 : this.layers.length;
            int amountToCopy = Math.min(layerCount, n);
            if (layerCount != n) {
                LO[] destLayers = new LO[n];
                if (this.layers != null) {
                    System.arraycopy(this.layers, 0, destLayers, 0, amountToCopy);
                }

                for (int i = 0; i < n; ++i) {
                    if (destLayers[i] == null) {
                        destLayers[i] = LO.getLO(this.W, this.H);
                    }
                }

                this.layers = destLayers;
            }

            this.L = n;
        }

        /** Deletes a layer */
        public void delL(int layerNumber) {
            int layerCount = this.layers.length;
            if (layerNumber < layerCount) {
                LO[] destLayers = new LO[layerCount - 1];
                int j = 0;

                for (int i = 0; i < layerCount; ++i) {
                    if (i != layerNumber) {
                        destLayers[j++] = this.layers[i];
                    }
                }

                this.layers = destLayers;
                this.L = layerCount - 1;
            }
        }

        /** Swaps two layers */
        public void swapL(int layer1, int layer2) {
            int maxL = Math.max(layer1, layer2);
            if (maxL >= this.L) {
                this.setL(maxL);
            }

            this.layers[layer1].isDraw = true;
            this.layers[layer2].isDraw = true;
            this.layers[layer1].swap(this.layers[layer2]);
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
        public void setQuality(int var1) {
            this.Q = var1;
            this.imW = this.W / this.Q;
            this.imH = this.H / this.Q;
        }

        public Dimension getCanvasSize() {
            return new Dimension(imW, imH);
        }

        public Dimension getSize() {
            this.vD.setSize(this.vWidth, this.vHeight);
            return this.vD;
        }

        private void center(Point var1) {
            var1.x = var1.x / this.scale + this.scaleX;
            var1.y = var1.y / this.scale + this.scaleY;
        }

        public int[][][] getPenMask() {
            return this.bPen;
        }

        public int getPenSize(M var1) {
            return (int) Math.sqrt((double) this.bPen[var1.iPenM][var1.iSize].length);
        }

        public int getPMMax() {
            return !this.m.isText() && (this.m.iHint < H_RECT || this.m.iHint > H_FOVAL) ? this.bPen[this.m.iPenM].length : 255;
        }

        public float[] getTT(int index) {
            index -= 12;
            if (this.bTT[index] == null) {
                if (this.dirTT != null) {
                    String dirTT = this.dirTT;
                    this.dirTT = null;

                    try {
                        this.cnf.loadZip(dirTT);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                int[] texture = M.this.loadIm("tt/" + index + ".gif", false);
                if (texture == null) {
                    return null;
                }

                int textureLength = texture.length;
                float[] alphaTexture = new float[textureLength];

                for (int i = 0; i < textureLength; ++i) {
                    alphaTexture[i] = M.b255[texture[i]];
                }

                this.bTT[index] = alphaTexture;
            }

            return this.bTT[index];
        }
    }
}
