package paintchat;

import java.applet.Applet;
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

// Currently only used by PCHViewer when loading the older PCH format, similar to M
public class Mg {
    private Mg.Info info;
    private Mg.User user;
    public int iHint = H_FLINE;
    public int iPen = P_SOLID;
    public int iPenM = PM_PEN;
    public int iTT = 0;
    public int iColor = 0;
    public int iColorMask = 0;
    public int iAlpha = 255;
    public int iAlpha2;
    public int iSA = 65280;
    public int iLayer = 0;
    public int iLayerSrc = 1;
    public int iMask = M_N;
    public int iSize = 0;
    public int iSS = 65280;
    public int iCount = -8;
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
    public static final int H_L = 14;
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
    public static final int P_LR = 17;
    public static final int P_UD = 18;
    public static final int P_R = 19;
    public static final int P_FUSION = 20;
    public static final int PM_PEN = 0;
    public static final int PM_SUISAI = 1;
    public static final int PM_MANY = 2;
    public static final int M_N = 0;
    public static final int M_M = 1;
    public static final int M_R = 2;
    public static final int M_ADD = 3;
    public static final int M_SUB = 4;
    private static final int F1O = 4;
    private static final int F1C = 8;
    private static final int F1A = 16;
    private static final int F1S = 32;
    private static final int F2H = 1;
    private static final int F2PM = 2;
    private static final int F2M = 4;
    private static final int F2P = 8;
    private static final int F2T = 16;
    private static final int F2L = 32;
    private static final int F2LS = 64;
    private static final int F3A = 1;
    private static final int F3C = 2;
    private static final int F3CM = 4;
    private static final int F3S = 8;
    private static final int F3E = 16;
    private static final int F3SA = 32;
    private static final int F3SS = 64;
    private static final int DEF_COUNT = -8;
    private static final String ENCODE = "UTF8";
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

    private final void addD(int var1, int var2, int var3, int var4) {
        this.setD(Math.min(var1, this.user.dX), Math.min(var2, this.user.dY), Math.max(var3, this.user.dW), Math.max(var4, this.user.dH));
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

    private final void dBuffer(boolean var1, int var2, int var3, int var4, int var5) {
        try {
            int var6 = this.info.scale;
            int var7 = this.info.Q;
            int var8 = this.info.W;
            int var9 = this.info.H;
            int var10 = this.info.scaleX;
            int var11 = this.info.scaleY;
            boolean var13 = var6 == 1;
            int[] var14 = this.user.buffer;
            Color var15 = Color.white;
            Graphics var16 = this.info.g;
            if (var16 == null) {
                return;
            }

            var2 /= var7;
            var3 /= var7;
            var4 /= var7;
            var5 /= var7;
            var2 = var2 <= var10 ? var10 : var2;
            var3 = var3 <= var11 ? var11 : var3;
            int var12 = this.info.vWidth / var6 + var10;
            var4 = var4 > var12 ? var12 : var4;
            var4 = var4 > var8 ? var8 : var4;
            var12 = this.info.vHeight / var6 + var11;
            var5 = var5 > var12 ? var12 : var5;
            var5 = var5 > var9 ? var9 : var5;
            if (var4 <= var2 || var5 <= var3) {
                return;
            }

            var8 = var4 - var2;
            int var17 = var8 * var6;
            int var18 = (var2 - var10) * var6;
            int var19 = var3;
            var12 = var14.length / (var8 * var7 * var7);

            while (true) {
                var9 = Math.min(var12, var5 - var19);
                if (var9 <= 0) {
                    break;
                }

                Image var20 = var1 ? this.mkMPic(var2, var19, var8, var9, var7) : this.mkLPic((int[]) null, var2, var19, var8, var9, var7);
                if (var13) {
                    var16.drawImage(var20, var18, var19 - var11, var15, (ImageObserver) null);
                } else {
                    var16.drawImage(var20, var18, (var19 - var11) * var6, var17, var9 * var6, var15, (ImageObserver) null);
                }

                var19 += var9;
            }
        } catch (RuntimeException var21) {
            var21.printStackTrace();
        }

    }

    private final void dBz(int[] var1) throws InterruptedException {
        try {
            int var2 = var1[0];
            int var3 = 0;

            int var4;
            for (var4 = 1; var4 < 4; ++var4) {
                var3 += Math.abs((var1[var4] >> 16) - (var2 >> 16)) + Math.abs((short) var1[var4] - (short) var2);
                var2 = var1[var4];
            }

            if (var3 <= 0) {
                return;
            }

            boolean var16 = true;
            boolean var5 = true;
            int var6 = -1000;
            int var7 = -1000;
            int var12 = 0;
            boolean var13 = this.isAnti;
            int var14 = this.user.pW / 2;

            for (var2 = var3; var2 > 0; --var2) {
                float var9 = (float) var2 / (float) var3;
                float var8 = (float) Math.pow((double) (1.0F - var9), 3.0D);
                float var10 = var8 * (float) (var1[3] >> 16);
                float var11 = var8 * (float) ((short) var1[3]);
                var8 = 3.0F * (1.0F - var9) * (1.0F - var9) * var9;
                var10 += var8 * (float) (var1[2] >> 16);
                var11 += var8 * (float) ((short) var1[2]);
                var8 = 3.0F * var9 * var9 * (1.0F - var9);
                var10 += var8 * (float) (var1[1] >> 16);
                var11 += var8 * (float) ((short) var1[1]);
                var8 = var9 * var9 * var9;
                var10 += var8 * (float) (var1[0] >> 16);
                var11 += var8 * (float) ((short) var1[0]);
                var4 = (int) var10 + var14;
                int var17 = (int) var11 + var14;
                if (var4 != var6 || var17 != var7) {
                    if (var13) {
                        this.shift(var4, var17);
                        ++var12;
                        if (var12 >= 4) {
                            this.dFLine2(this.iSize);
                        }
                    } else {
                        this.dFLine(var4, var17, this.iSize);
                    }

                    var6 = var4;
                    var7 = var17;
                }
            }

            Mg.User var10000 = this.user;
            var10000.dX = var10000.dX - 1;
            var10000 = this.user;
            var10000.dY = var10000.dY - 1;
            var10000 = this.user;
            var10000.dW = var10000.dW + 2;
            var10000 = this.user;
            var10000.dH = var10000.dH + 2;
        } catch (RuntimeException var15) {
            var15.printStackTrace();
        }

    }

    public void dClear(boolean var1) {
        if (var1) {
            this.getWork();
        }

        int var2 = this.info.W;
        int var3 = this.info.H;
        int[][] var4 = this.info.iOffs;
        int[] var5 = var4[0];
        int var6 = 16777215;
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

    private void dCopy(int[] var1) {
        int var2 = this.info.W;
        int var3 = this.info.H;
        int var4 = var1[0];
        int var5 = var4 >> 16;
        int var6 = (short) var4;
        var4 = var1[1];
        int var7 = (var4 >> 16) - var5;
        int var8 = (short) var4 - var6;
        var4 = var1[2];
        int var9 = var4 >> 16;
        short var10 = (short) var4;
        if (var5 < 0) {
            var7 -= var5;
            var5 = 0;
        }

        if (var5 + var7 > var2) {
            var5 = var2 - var7;
        }

        if (var6 < 0) {
            var8 -= var6;
            var6 = 0;
        }

        if (var6 + var8 > var3) {
            var6 = var3 - var8;
        }

        if (var9 < 0) {
            var5 -= var9;
            var7 += var9;
            var9 = 0;
        }

        if (var10 < 0) {
            var6 -= var10;
            var8 += var10;
            var10 = 0;
        }

        if (var9 + var7 >= var2) {
            var7 = var2 - var9;
        }

        if (var10 + var8 >= var3) {
            var8 = var3 - var10;
        }

        if (var7 > 0 && var8 > 0 && var9 < var2 && var10 < var3) {
            int[] var11 = var7 * var8 <= this.user.buffer.length ? this.user.buffer : new int[var7 * var8];
            int[] var12 = this.info.iOffs[this.iLayerSrc];

            int var13;
            for (var13 = 0; var13 < var8; ++var13) {
                System.arraycopy(var12, (var6 + var13) * var2 + var5, var11, var7 * var13, var7);
            }

            var12 = this.info.iOffs[this.iLayer];

            for (var13 = 0; var13 < var8; ++var13) {
                System.arraycopy(var11, var7 * var13, var12, (var10 + var13) * var2 + var9, var7);
            }

            this.setD(var9, var10, var9 + var7, var10 + var8);
        }
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
            System.out.println(var13);
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
                }

                if (var8 < var12 - 1) {
                    ++var8;

                    while (var8 < var12 && var1[var8] == var6) {
                        ++var8;
                    }

                    if (var8 < var12 - 1) {
                        int var9;
                        for (var9 = var8++; var8 < var12 && var1[var8] != var6; ++var8) {
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
            System.out.println(var13);
        }

    }

    private void dFill(int var1, int var2) {
        int var3 = this.info.W;
        int var4 = this.info.H;
        int[] var10000 = this.info.iOffs[this.iLayer];
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
                var2 = var9 & '\uffff';
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
            System.out.println(var15);
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
                Mg.User var10000 = this.user;
                var10000.count = var10000.count - 1;
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
                Mg.User var10000 = this.user;
                var10000.count = var10000.count - 1;
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
                if (var13) {
                    var16 -= 0.5F;
                    var17 -= 0.5F;
                }

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
                            Mg.User var10000 = this.user;
                            var10000.count = var10000.count - 1;
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
        int var10000 = this.info.Q;
        int var3 = this.info.W;
        int var4 = this.info.H;
        int var5 = this.user.dX <= 0 ? 0 : this.user.dX;
        int var6 = this.user.dY <= 0 ? 0 : this.user.dY;
        int var7 = this.user.dW >= var3 ? var3 : this.user.dW;
        int var8 = this.user.dH >= var4 ? var4 : this.user.dH;
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
            case 9:
                var11 = this.iAlpha / 10 + 1;
                var5 = var5 / var11 * var11;
                var6 = var6 / var11 * var11;
                int[] var28 = this.user.argb;

                for (var12 = var6; var12 < var8; var12 += var11) {
                    for (var2 = var5; var2 < var7; var2 += var11) {
                        var15 = Math.min(var11, var3 - var2);
                        var23 = Math.min(var11, var4 - var12);

                        int var24;
                        for (var24 = 0; var24 < 4; ++var24) {
                            var28[var24] = 0;
                        }

                        var25 = 0;

                        for (var14 = 0; var14 < var23; ++var14) {
                            for (var13 = 0; var13 < var15; ++var13) {
                                var26 = this.pix(var2 + var13, var12 + var14);
                                var1 = (var12 + var14) * var3 + var2 + var13;

                                for (var24 = 0; var24 < 4; ++var24) {
                                    var28[var24] += var26 >>> var24 * 8 & 255;
                                }

                                ++var25;
                            }
                        }

                        var26 = var28[3] << 24 | var28[2] / var25 << 16 | var28[1] / var25 << 8 | var28[0] / var25;

                        for (var14 = var12; var14 < var12 + var23; ++var14) {
                            var1 = var3 * var14 + var2;

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
                    this.dBuffer(true, var5, var6, var7, var8);
                }
                break;
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            default:
                while (var6 < var8) {
                    var1 = var6 * var3 + var5;

                    for (var2 = var5; var2 < var7; ++var2) {
                        var10[var1] = this.getM(var10[var1], var9[var1] & 255, var1);
                        var9[var1] = 0;
                        ++var1;
                    }

                    ++var6;
                }
                break;
            case 17:
                for (var13 = (var7 - var5) / 2 + 1; var6 < var8; ++var6) {
                    var1 = var6 * var3 + var5;
                    var12 = var1 + (var7 - var5) - 1;

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
            case 18:
                for (var13 = (var8 - var6) / 2; var5 < var7; ++var5) {
                    var1 = var6 * var3 + var5;
                    var12 = var1 + (var8 - var6 - 1) * var3;

                    for (var2 = 0; var2 < var13; ++var2) {
                        var11 = var10[var1];
                        var10[var1] = var10[var12];
                        var10[var12] = var11;
                        var9[var1] = var9[var12] = 0;
                        var1 += var3;
                        var12 -= var3;
                    }
                }

                return;
            case 19:
                var12 = var7 - var5;
                var13 = var8 - var6;
                var14 = var6 * var3 + var5;
                var15 = var12 * var13;
                var17 = new int[var15];

                for (var23 = 0; var23 < var13; ++var23) {
                    System.arraycopy(var10, var14 + var3 * var23, var17, var12 * var23, var12);
                }

                for (var23 = 0; var23 < var12; ++var23) {
                    var10[var5 + var23] = 16777215;
                    var9[var14 + var23] = 0;
                }

                for (var23 = 1; var23 < var13; ++var23) {
                    System.arraycopy(var10, var14, var10, var14 + var23 * var3, var12);
                    System.arraycopy(var9, var14, var9, var14 + var23 * var3, var12);
                }

                boolean var22 = false;
                var15 = var3 * var4;

                for (var25 = 0; var25 < var13; ++var25) {
                    var1 = var12 * var25;
                    var11 = var14 + var13 - var25;

                    for (var26 = 0; var26 < var12; ++var26) {
                        int var27 = var26 + var5;
                        if (var27 <= var3 && var27 >= 0 && var11 < var15) {
                            var10[var11] = var17[var1];
                        }

                        var11 += var3;
                        ++var1;
                    }
                }

                this.addD(var5, var6, var5 + Math.max(var12, var13), var6 + var12);
                break;
            case 20:
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

                    while (var6 < var8) {
                        var1 = var6 * var3 + var5;

                        for (var2 = var5; var2 < var7; ++var2) {
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
                                    var11 = 16777215;
                                }

                                var10[var1] = var14 << 24 | var11 & 16777215;
                                var16[var1] = 16777215;
                                var9[var1] = 0;
                            }

                            ++var1;
                        }

                        ++var6;
                    }
                }

                if (this.user.wait >= 0) {
                    this.dBuffer();
                }
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
            Mg.User var10000 = this.user;
            var10000.iDCount = var10000.iDCount + 1;
            if (this.iHint != 11) {
                if (this.isAnti) {
                    this.dFLine((float) var1 - 0.5F, (float) var2 - 0.5F, var3);
                } else {
                    this.dFLine(var1, var2, var3);
                }
            } else if (this.user.iDCount >= 2) {
                this.dFLine2(var3);
            }

            return true;
        }
    }

    public final void dNext(int var1, int var2, int var3, int var4) throws InterruptedException, IOException {
        int var5 = this.info.scale;
        //this.user.pW;
        var1 = (var1 / var5 + this.info.scaleX) * this.info.Q;
        var2 = (var2 / var5 + this.info.scaleY) * this.info.Q;
        if (Math.abs(var1 - this.user.pX[3]) + Math.abs(var2 - this.user.pY[3]) >= var4) {
            this.wPo(var1 - this.user.pX[3]);
            this.wPo(var2 - this.user.pY[3]);
            this.shift(var1, var2);
            Mg.User var10000 = this.user;
            var10000.iDCount = var10000.iDCount + 1;
            if (this.iSOB != 0) {
                this.info.workOut.write(var3);
            }

            if (this.iHint == 11) {
                if (this.user.iDCount >= 2) {
                    this.dFLine2(var3);
                }
            } else if (this.isAnti) {
                this.dFLine((float) var1 - 0.5F, (float) var2 - 0.5F, var3);
            } else {
                this.dFLine(var1, var2, var3);
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
        int var10000 = this.info.Q;
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
                                case 1:
                                case 20:
                                    var17[var6++] = (byte) (var8 + (int) ((float) var9 * b255[255 - var8 >>> 1] * var3));
                                    break;
                                case 2:
                                case 5:
                                case 6:
                                case 7:
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

    public void dPre(Graphics var1, int[] var2) {
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
                    var1.fillRect(var9, var19, 1, 1);
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

    public final void draw() throws InterruptedException {
        try {
            if (this.info == null) {
                return;
            }

            this.iSeek = 0;
            label23:
            switch (this.iHint) {
                case 0:
                case 1:
                case 11:
                    this.dStart();

                    while (true) {
                        if (!this.dNext()) {
                            break label23;
                        }
                    }
                case 10:
                    this.dClear(false);
                    break;
                default:
                    this.dRetouch((int[]) null);
            }
        } catch (InterruptedException var2) {
        } catch (Throwable var3) {
            var3.printStackTrace();
        }

        this.dEnd();
    }

    private void dRect(int var1, int var2, int var3, int var4) {
        int var5 = this.info.W;
        int var6 = this.info.H;
        byte[] var10 = this.info.iMOffs;
        int[] var11 = this.info.iOffs[this.iLayer];
        byte var12 = (byte) this.iAlpha;
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

        if (var1 < var3 && var2 < var4 && var12 != 0) {
            this.setD(var1, var2, var3, var4);
            int var7;
            int var9;
            int var13;
            int var14;
            int var15;
            int var16;
            int var17;
            int var18;
            label108:
            switch (this.iHint) {
                case 3:
                    var13 = var2;

                    while (true) {
                        if (var13 >= var4) {
                            break label108;
                        }

                        var7 = var13 * var5 + var1;

                        for (var9 = var1; var9 < var3; ++var9) {
                            if (!this.isM(var11[var7])) {
                                var10[var7] = var12;
                            }

                            ++var7;
                        }

                        ++var13;
                    }
                case 4:
                    var13 = var1;
                    var14 = var2;
                    var15 = var3;
                    var16 = var4;
                    var17 = 0;

                    while (true) {
                        if (var17 >= this.iSize + 1) {
                            break label108;
                        }

                        var7 = var5 * var14 + var13;
                        int var8 = var5 * (var16 - 1) + var13;

                        for (var9 = var13; var9 < var15; ++var9) {
                            if (!this.isM(var11[var7])) {
                                var10[var7] = var12;
                            }

                            if (!this.isM(var11[var8])) {
                                var10[var8] = var12;
                            }

                            ++var7;
                            ++var8;
                        }

                        var7 = var5 * var14 + var13;
                        var8 = var5 * var14 + var15 - 1;

                        for (var18 = var14; var18 < var16; ++var18) {
                            if (!this.isM(var11[var7])) {
                                var10[var7] = var12;
                            }

                            if (!this.isM(var11[var8])) {
                                var10[var8] = var12;
                            }

                            var7 += var5;
                            var8 += var5;
                        }

                        ++var13;
                        --var15;
                        ++var14;
                        --var16;
                        if (var15 <= var13 || var16 <= var14) {
                            break label108;
                        }

                        ++var17;
                    }
                case 5:
                case 6:
                    var13 = var3 - var1 - 1;
                    var14 = var4 - var2 - 1;
                    var17 = var13 / 2;
                    var18 = var14 / 2;

                    for (float var19 = 0.0F; var19 < 8.0F; var19 = (float) ((double) var19 + 0.001D)) {
                        var15 = var1 + var17 + (int) Math.round(Math.cos((double) var19) * (double) var17);
                        var16 = var2 + var18 + (int) Math.round(Math.sin((double) var19) * (double) var18);
                        var10[var5 * var16 + var15] = var12;
                    }

                    if (this.iHint == 5 && var17 > 0 && var18 > 0) {
                        this.dFill(var10, var1, var2, var3, var4);
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
            int var5 = this.user.pW / 2;
            this.user.setup(this);
            this.setD(0, 0, 0, 0);
            int[] var6 = this.user.points;
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
                    var13 = this.iHint == 2 ? var5 : 0;

                    for (int var14 = 0; var14 < 4; ++var14) {
                        var9 = var1[var14] >> 16;
                        short var10 = (short) var1[var14];
                        var9 = (var9 / var7 + var11) * var8 - var13;
                        var18 = (var10 / var7 + var12) * var8 - var13;
                        var6[var14] = var9 << 16 | var18 & '\uffff';
                    }
                } else {
                    System.arraycopy(var1, 0, var6, 0, var1.length);
                }
            } else {
                var7 = 0;

                while (this.iSeek < this.iOffset) {
                    var6[var7++] = (this.r2() & '\uffff') << 16 | this.r2() & '\uffff';
                    if (this.iHint == 8) {
                        break;
                    }
                }
            }

            var7 = var6[0] >> 16;
            short var16 = (short) var6[0];
            switch (this.iHint) {
                case 2:
                    var9 = this.user.wait;
                    this.user.wait = -2;
                    this.dStart(var7 + var5, var16 + var5, 0, false, false);
                    this.dBz(var6);
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
                    this.dCopy(var6);
                    var4 = 3;
                    break;
                case 14:
                    int var10000 = this.info.W * this.info.H;
                    switch (var16) {
                        case 0:
                            this.ch(this.iLayerSrc, this.iLayer);
                            break;
                        case 1:
                            this.info.setL(var6[1]);
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
                    this.dRect(var7, var16, var6[1] >> 16, (short) var6[1]);
                    var4 = 2;
            }

            if (var3) {
                ByteStream var17 = this.getWork();

                for (var18 = 0; var18 < var4; ++var18) {
                    var17.w((long) var6[var18], 4);
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
        } catch (Throwable var15) {
            var15.printStackTrace();
        }

    }

    private void dStart() {
        try {
            short var1 = this.r2();
            short var2 = this.r2();
            this.user.setup(this);
            int var3 = this.iSOB != 0 ? this.ru() : 0;
            if (this.iSOB != 0) {
                this.iSize = this.ss(var3);
                this.iAlpha = this.sa(var3);
            }

            this.memset(this.user.pX, var1);
            this.memset(this.user.pY, var2);
            int var4 = this.user.pW / 2;
            this.setD(var1 - var4 - 1, var2 - var4 - 1, var1 + var4, var2 + var4);
            this.user.fX = (float) var1;
            this.user.fY = (float) var2;
            if (this.isAnti) {
                Mg.User var10000 = this.user;
                var10000.fX = var10000.fX - 0.5F;
                var10000 = this.user;
                var10000.fY = var10000.fY - 0.5F;
            }

            if (this.iHint != 11 && !this.isAnti) {
                this.dFLine(var1, var2, var3);
            }
        } catch (RuntimeException var5) {
            var5.printStackTrace();
        } catch (InterruptedException var6) {
        }

    }

    public void dStart(int var1, int var2, int var3, boolean var4, boolean var5) {
        try {
            this.user.setup(this);
            this.iSize = this.ss(var3);
            this.iAlpha = this.sa(var3);
            this.user.setup(this);
            int var6;
            if (var5) {
                var6 = this.info.scale;
                var1 = (var1 / var6 + this.info.scaleX) * this.info.Q;
                var2 = (var2 / var6 + this.info.scaleY) * this.info.Q;
            }

            if (var4) {
                ByteStream var9 = this.getWork();
                var9.w((long) var1, 2);
                var9.w((long) var2, 2);
                if (this.iSOB != 0) {
                    var9.write(var3);
                }
            }

            this.memset(this.user.pX, var1);
            this.memset(this.user.pY, var2);
            var6 = this.user.pW / 2;
            this.setD(var1 - var6 - 1, var2 - var6 - 1, var1 + var6, var2 + var6);
            this.user.fX = (float) var1;
            this.user.fY = (float) var2;
            if (this.isAnti) {
                Mg.User var10000 = this.user;
                var10000.fX = var10000.fX - 0.5F;
                var10000 = this.user;
                var10000.fY = var10000.fY - 0.5F;
            }

            if (this.iHint != 11 && !this.isAnti) {
                this.dFLine(var1, var2, var3);
            }
        } catch (IOException var7) {
            var7.printStackTrace();
        } catch (InterruptedException var8) {
            var8.printStackTrace();
        }

    }

    private void dText(String var1, String var2, int var3, int var4) {
        try {
            int var5 = this.info.W;
            int var6 = this.info.H;
            int[] var7 = this.info.iOffs[this.iLayer];
            byte[] var8 = this.info.iMOffs;
            float var9 = b255[this.iAlpha];
            if (var9 == 0.0F) {
                return;
            }

            if (var1 == null || var1.length() <= 0) {
                return;
            }

            Font var10 = this.strHint != null ? Font.decode(new String(this.strHint, "UTF8")) : new Font("sansserif", 0, this.iSize);
            FontMetrics var11 = this.info.component.getFontMetrics(var10);
            int var12 = var11.stringWidth(var1) + var1.length();
            int var13 = var11.getMaxAscent() + var11.getMaxDescent() + 2;
            int var14 = var13 - var11.getMaxDescent();
            Image var15 = this.info.component.createImage(var12, var13);
            Graphics var16 = var15.getGraphics();
            var16.setFont(var10);
            var16.setColor(Color.black);
            var16.fillRect(0, 0, var12, var13);
            var16.setColor(Color.blue);
            this.setD(var3, var4, var3 + var12, var4 + var13);
            var16.drawRect(0, 0, var12 - 1, var13 - 1);
            var16.drawString(var1, var11.getLeading(), var14);
            var16.dispose();
            var16 = null;
            var10 = null;
            var11 = null;
            PixelGrabber var17 = new PixelGrabber(var15, 0, 0, var12, var13, true);
            var17.grabPixels();
            int[] var18 = (int[]) var17.getPixels();
            var17 = null;
            var15.flush();
            var15 = null;
            boolean var19 = false;
            int var21 = Math.min(var5 - var3, var12);
            int var22 = Math.min(var6 - var4, var13);

            for (int var23 = 0; var23 < var22; ++var23) {
                int var26 = var23 * var12;
                int var20 = (var23 + var4) * var5 + var3;

                for (int var24 = 0; var24 < var21; ++var24) {
                    if (!this.isM(var7[var20])) {
                        var8[var20] = (byte) ((int) ((float) (var18[var26] & 255) * var9));
                    }

                    ++var26;
                    ++var20;
                }
            }

            this.setD(var3, var4, var3 + var12, var4 + var13);
            this.t();
        } catch (Exception var25) {
            var25.printStackTrace();
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

    public final void get(OutputStream var1, ByteStream var2, Mg var3) {
        try {
            var2.reset();
            int var4 = 0;
            boolean var5 = false;
            int var6 = this.getFlag(var3);
            int var7 = var6 >>> 8 & 255;
            int var8 = var6 & 255;
            var2.write(var6 >>> 16);
            var2.write(var7);
            var2.write(var8);
            if ((var7 & 1) != 0) {
                var4 = this.iHint;
                var5 = true;
            }

            if ((var7 & 2) != 0) {
                if (var5) {
                    var2.write(var4 << 4 | this.iPenM);
                } else {
                    var4 = this.iPenM;
                }

                var5 = !var5;
            }

            if ((var7 & 4) != 0) {
                if (var5) {
                    var2.write(var4 << 4 | this.iMask);
                } else {
                    var4 = this.iMask;
                }

                var5 = !var5;
            }

            if (var5) {
                var2.write(var4 << 4);
            }

            if ((var7 & 8) != 0) {
                var2.write(this.iPen);
            }

            if ((var7 & 16) != 0) {
                var2.write(this.iTT);
            }

            if ((var7 & 32) != 0) {
                var2.write(this.iLayer);
            }

            if ((var7 & 64) != 0) {
                var2.write(this.iLayerSrc);
            }

            if ((var8 & 1) != 0) {
                var2.write(this.iAlpha);
            }

            if ((var8 & 2) != 0) {
                var2.w((long) this.iColor, 3);
            }

            if ((var8 & 4) != 0) {
                var2.w((long) this.iColorMask, 3);
            }

            if ((var8 & 8) != 0) {
                var2.write(this.iSize);
            }

            if ((var8 & 16) != 0) {
                var2.write(this.iCount);
            }

            if ((var8 & 32) != 0) {
                var2.w((long) this.iSA, 2);
            }

            if ((var8 & 64) != 0) {
                var2.w((long) this.iSS, 2);
            }

            if (this.iPen == P_FUSION) {
                var2.w2(this.iAlpha2);
            }

            if (this.iHint == 8) {
                if (this.strHint == null) {
                    var2.w2(0);
                } else {
                    var2.w2(this.strHint.length);
                    var2.write(this.strHint);
                }
            }

            if (this.offset != null && this.iOffset > 0) {
                var2.write(this.offset, 0, this.iOffset);
            }

            var1.write(var2.size() >>> 8);
            var1.write(var2.size() & 255);
            var2.writeTo(var1);
        } catch (IOException var9) {
            var9.printStackTrace();
        } catch (RuntimeException var10) {
            var10.printStackTrace();
        }

    }

    private final int getFlag(Mg var1) {
        int var3 = 0;
        if (this.isAllL) {
            var3 |= 1;
        }

        if (this.isAFix) {
            var3 |= 2;
        }

        if (this.isAnti) {
            var3 |= 16;
        }

        if (this.isCount) {
            var3 |= 8;
        }

        if (this.isOver) {
            var3 |= 4;
        }

        var3 |= this.iSOB << 6;
        int var2 = var3 << 16;
        if (var1 == null) {
            return var2 | '\uffff';
        } else {
            var3 = 0;
            if (this.iHint != var1.iHint) {
                var3 |= 1;
            }

            if (this.iPenM != var1.iPenM) {
                var3 |= 2;
            }

            if (this.iMask != var1.iMask) {
                var3 |= 4;
            }

            if (this.iPen != var1.iPen) {
                var3 |= 8;
            }

            if (this.iTT != var1.iTT) {
                var3 |= 16;
            }

            if (this.iLayer != var1.iLayer) {
                var3 |= 32;
            }

            if (this.iLayerSrc != var1.iLayerSrc) {
                var3 |= 64;
            }

            var2 |= var3 << 8;
            var3 = 0;
            if (this.iAlpha != var1.iAlpha) {
                var3 |= 1;
            }

            if (this.iColor != var1.iColor) {
                var3 |= 2;
            }

            if (this.iColorMask != var1.iColorMask) {
                var3 |= 4;
            }

            if (this.iSize != var1.iSize) {
                var3 |= 8;
            }

            if (this.iCount != var1.iCount) {
                var3 |= 16;
            }

            if (this.iSA != var1.iSA) {
                var3 |= 32;
            }

            if (this.iSS != var1.iSS) {
                var3 |= 64;
            }

            return var2 | var3;
        }
    }

    public Image getImage(int var1, int var2, int var3, int var4, int var5) {
        var2 = Math.round((float) (var2 / this.info.scale)) + this.info.scaleX;
        var3 = Math.round((float) (var3 / this.info.scale)) + this.info.scaleY;
        var4 /= this.info.scale;
        var5 /= this.info.scale;
        int var6 = this.info.Q;
        if (var6 <= 1) {
            return this.info.component.createImage(new MemoryImageSource(var4, var5, this.info.iOffs[var1], var3 * this.info.W + var2, this.info.W));
        } else {
            Image var7 = this.info.component.createImage(new MemoryImageSource(var4 * var6, var5 * var6, this.info.iOffs[var1], var3 * var6 * this.info.W + var2 * var6, this.info.W));
            Image var8 = var7.getScaledInstance(var4, var5, 2);
            var7.flush();
            return var8;
        }
    }

    private final int getM(int var1, int var2, int var3) {
        if (var2 == 0) {
            return var1;
        } else {
            int var6;
            int var12;
            int var13;
            int var14;
            float var15;
            int var10000;
            switch (this.iPen) {
                case 4:
                case 5:
                    var12 = var1 >>> 24;
                    var13 = var12 - (int) ((float) var12 * b255[var2]);
                    return var13 == 0 ? 16777215 : var13 << 24 | var1 & 16777215;
                case 6:
                    var12 = var1 >>> 24;
                    var13 = var1 >>> 16 & 255;
                    var6 = var1 >>> 8 & 255;
                    var14 = var1 & 255;
                    var10000 = this.iColor;
                    var15 = b255[var2];
                    return (var12 << 24) + (Math.min(var13 + (int) ((float) var13 * var15), 255) << 16) + (Math.min(var6 + (int) ((float) var6 * var15), 255) << 8) + Math.min(var14 + (int) ((float) var14 * var15), 255);
                case 7:
                    var12 = var1 >>> 24;
                    var13 = var1 >>> 16 & 255;
                    var6 = var1 >>> 8 & 255;
                    var14 = var1 & 255;
                    var10000 = this.iColor;
                    var15 = b255[var2];
                    return (var12 << 24) + (Math.max(var13 - (int) ((float) (255 - var13) * var15), 0) << 16) + (Math.max(var6 - (int) ((float) (255 - var6) * var15), 0) << 8) + Math.max(var14 - (int) ((float) (255 - var14) * var15), 0);
                case 8:
                    var10000 = var1 >>> 24;
                    var10000 = var1 >>> 16 & 255;
                    var10000 = var1 >>> 8 & 255;
                    var10000 = var1 & 255;
                    var10000 = this.iColor;
                    float var4 = b255[var2];
                    int[] var5 = this.user.argb;
                    int[] var7 = this.info.iOffs[this.iLayer];
                    int var8 = this.info.W;

                    int var9;
                    for (var9 = 0; var9 < 4; ++var9) {
                        var5[var9] = 0;
                    }

                    var6 = var3 % var8;
                    var3 += var6 == 0 ? 1 : (var6 == var8 - 1 ? -1 : 0);
                    var3 += var3 < var8 ? var8 : (var3 > var8 * (this.info.H - 1) ? -var8 : 0);

                    for (var9 = -1; var9 < 2; ++var9) {
                        for (int var10 = -1; var10 < 2; ++var10) {
                            var6 = var7[var3 + var10 + var9 * var8];
                            var10000 = var6 >>> 24;

                            for (int var11 = 0; var11 < 4; ++var11) {
                                var5[var11] += var6 >>> (var11 << 3) & 255;
                            }
                        }
                    }

                    for (var9 = 0; var9 < 4; ++var9) {
                        var6 = var1 >>> (var9 << 3) & 255;
                        var5[var9] = var6 + (int) ((float) (var5[var9] / 9 - var6) * var4);
                    }

                    return var5[3] << 24 | var5[2] << 16 | var5[1] << 8 | var5[0];
                case 9:
                case 20:
                    if (var2 == 0) {
                        return var1;
                    }

                    return var2 << 24 | 16711680;
                case 10:
                    return var2 << 24 | this.iColor;
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 16:
                case 17:
                case 18:
                case 19:
                default:
                    return this.fu(var1, this.iColor, var2);
            }
        }
    }

    public final byte[] getOffset() {
        return this.offset;
    }

    private final int[] getPM() {
        if (this.iHint == 8) {
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
            var1 &= 16777215;
            return this.iMask == M_M ? this.iColorMask == var1 : (this.iMask == M_R ? this.iColorMask != var1 : false);
        }
    }

    public static final boolean isTone(int var0, int var1, int var2) {
        switch (var0) {
            case 2:
                if ((var1 + 2) % 4 == 0 && (var2 + 4) % 4 == 0) {
                    break;
                }
            case 1:
                if ((var1 + 2) % 4 == 0 && (var2 + 2) % 4 == 0) {
                    break;
                }
            case 0:
                if (var1 % 4 != 0 || var2 % 4 != 0) {
                    return true;
                }
                break;
            case 4:
                if ((var1 + 1) % 4 == 0 && (var2 + 3) % 4 == 0) {
                    break;
                }
            case 3:
                if (var1 % 2 == 0 && var2 % 2 == 0) {
                    break;
                }

                return true;
            case 7:
                if ((var1 + 2) % 4 == 0 && (var2 + 3) % 4 == 0) {
                    break;
                }
            case 6:
                if (var1 % 4 == 0 && (var2 + 1) % 4 == 0) {
                    break;
                }
            case 5:
                if ((var1 + 1) % 2 != (var2 + 1) % 2) {
                    return true;
                }
                break;
            case 9:
                if ((var1 + 1) % 4 == 0 && (var2 + 2) % 4 == 0) {
                    break;
                }
            case 8:
                if (var1 % 2 != 0 && (var2 + 1) % 2 != 0) {
                    return true;
                }
                break;
            case 10:
                if ((var1 + 3) % 4 == 0 && (var2 + 2) % 4 == 0) {
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

    public final void m_paint(int var1, int var2, int var3, int var4) {
        int var5 = this.info.scale;
        int var6 = this.info.Q;
        var1 = (var1 / var5 + this.info.scaleX) * var6;
        var2 = (var2 / var5 + this.info.scaleY) * var6;
        var3 = var3 / var5 * var6;
        var4 = var4 / var5 * var6;
        this.dBuffer(false, var1, var2, var1 + var3, var2 + var4);
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

    public final Image mkLPic(int[] var1, int var2, int var3, int var4, int var5, int var6) {
        var2 *= var6;
        var3 *= var6;
        var4 *= var6;
        var5 *= var6;
        int var8 = this.info.L;
        int var10000 = var6 * var6;
        float[] var9 = this.info.visit;
        int var13 = 0;
        int var15 = var3 + var5;
        int var16 = this.info.W;
        int[][] var17 = this.info.iOffs;
        var10000 = var16 * this.info.H;
        boolean var19 = var1 == null;
        float[] var20 = b255;
        if (var19) {
            var1 = this.user.buffer;
        }

        while (var3 < var15) {
            int var12 = var16 * var3 + var2;

            for (int var14 = var12 + var4; var12 < var14; ++var12) {
                int var11 = 16777215;

                for (int var7 = 0; var7 < var8; ++var7) {
                    int var10 = var17[var7][var12];
                    float var18 = b255[var10 >>> 24] * var9[var7];
                    var11 = var18 == 1.0F ? var10 : (var18 == 0.0F ? var11 : ((var11 & 16711680) + (int) ((float) ((var10 & 16711680) - (var11 & 16711680)) * var18) & 16711680) + ((var11 & '\uff00') + (int) ((float) ((var10 & '\uff00') - (var11 & '\uff00')) * var18) & '\uff00') + (var11 & 255) + (int) ((float) ((var10 & 255) - (var11 & 255)) * var18));
                }

                var1[var13++] = var11;
            }

            ++var3;
        }

        if (var19) {
            this.user.raster.newPixels(this.user.image, var4, var5, var6);
        } else {
            this.user.raster.scale(var1, var4, var5, var6);
        }

        return this.user.image;
    }

    private final Image mkMPic(int var1, int var2, int var3, int var4, int var5) {
        var1 *= var5;
        var2 *= var5;
        var3 *= var5;
        var4 *= var5;
        int var7 = this.info.L;
        int var8 = this.iLayer;
        int var10000 = var5 * var5;
        float[] var9 = this.info.visit;
        int var14 = 0;
        int var16 = var2 + var4;
        int var17 = this.info.W;
        int[][] var18 = this.info.iOffs;
        var10000 = var17 * this.info.H;
        int[] var19 = this.user.buffer;
        float[] var22 = b255;

        for (byte[] var20 = this.info.iMOffs; var2 < var16; ++var2) {
            int var13 = var17 * var2 + var1;

            for (int var15 = var13 + var3; var13 < var15; ++var13) {
                int var12 = var20[var13] & 255;
                int var11 = 16777215;

                for (int var6 = 0; var6 < var7; ++var6) {
                    int var10 = var6 != var8 ? var18[var6][var13] : this.getM(var18[var6][var13], var12, var13);
                    float var21 = b255[var10 >>> 24] * var9[var6];
                    var11 = var21 == 1.0F ? var10 : (var21 == 0.0F ? var11 : ((var11 & 16711680) + (int) ((float) ((var10 & 16711680) - (var11 & 16711680)) * var21) & 16711680) + ((var11 & '\uff00') + (int) ((float) ((var10 & '\uff00') - (var11 & '\uff00')) * var21) & '\uff00') + (var11 & 255) + (int) ((float) ((var10 & 255) - (var11 & 255)) * var21));
                }

                var19[var14++] = var11;
            }
        }

        this.user.raster.newPixels(this.user.image, var3, var4, var5);
        return this.user.image;
    }

    public Mg.Info newInfo(Applet var1, Component var2, Res var3) {
        if (this.info != null) {
            return this.info;
        } else {
            this.info = new Mg.Info();
            this.info.cnf = var3;
            this.info.component = var2;
            Mg.Info var4 = this.info;
            Mg var5 = this.info.m;
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

            int var7;
            int var8;
            int var9;
            int[][] var12;
            int[] var13;
            int var18;
            for (int var20 = 0; var20 < 2; ++var20) {
                var12 = new int[23][];

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

                var11[var20] = var12;
                var5.iAlpha = 110;
                var19 = 80;
                var17 = 1;
            }

            var5.iAlpha = 255;
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

            for (var6 = 9; var6 < 32; ++var6) {
                var18 = var17 + 3;
                float var24 = (float) var17 / 2.0F;
                var12[var6] = var13 = new int[var18 * var18];
                var9 = (int) ((float) Math.round(2.0F * var10 * var24) * (float) (2 + var6 / 16)) + var6 * 2;

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
                var4.W = var4.H = var18;
                this.dFill((int[]) var13, 0, 0, var18, var18);
            }

            var11[2] = var12;
            this.set((Mg) null);
            var5.set((Mg) null);
            if (var3 != null) {
                for (var6 = 0; var6 < 16; ++var6) {
                    for (var9 = 0; var3.get("pm" + var6 + '/' + var9 + ".gif") != null; ++var9) {
                    }

                    if (var9 > 0) {
                        var11[var6] = new int[var9][];

                        for (var7 = 0; var7 < var9; ++var7) {
                            var11[var6][var7] = this.loadIm("pm" + var6 + '/' + var7 + ".gif", true);
                        }
                    }
                }

                this.info.bTT = new float[var3.getP("tt_size", 31)][];
            }

            String var28 = var1.getParameter("tt.zip");
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

    public final int pix(int var1, int var2) {
        if (!this.isAllL) {
            return this.info.iOffs[this.iLayer][var2 * this.info.W + var1];
        } else {
            int var3 = this.info.L;
            int var5 = 0;
            int var7 = 16777215;
            int var9 = this.info.W * var2 + var1;

            for (int var11 = 0; var11 < var3; ++var11) {
                int var8 = this.info.iOffs[var11][var9];
                float var10 = b255[var8 >>> 24];
                if (var10 != 0.0F) {
                    if (var10 == 1.0F) {
                        var7 = var8;
                        var5 = 255;
                    }

                    var5 = (int) ((float) var5 + (float) (255 - var5) * var10);
                    int var4 = 0;

                    for (int var12 = 16; var12 >= 0; var12 -= 8) {
                        int var6 = var7 >>> var12 & 255;
                        var4 |= var6 + (int) ((float) ((var8 >>> var12 & 255) - var6) * var10) << var12;
                    }

                    var7 = var4;
                }
            }

            return var5 << 24 | var7;
        }
    }

    private final byte r() {
        return this.offset[this.iSeek++];
    }

    private final int r(byte[] var1, int var2, int var3) {
        int var4 = 0;

        for (int var5 = var3 - 1; var5 >= 0; --var5) {
            var4 |= (var1[var2++] & 255) << var5 * 8;
        }

        return var4;
    }

    private final short r2() {
        return (short) ((this.ru() << 8) + this.ru());
    }

    public void reset() {
        byte[] var2 = this.info.iMOffs;
        int var3 = this.info.W;
        int var4 = Math.max(this.user.dX, 0);
        int var5 = Math.max(this.user.dY, 0);
        int var6 = Math.min(this.user.dW, var3);
        int var7 = Math.min(this.user.dH, this.info.H);

        for (int var9 = var5; var9 < var7; ++var9) {
            int var1 = var4 + var9 * var3;

            for (int var8 = var4; var8 < var6; ++var8) {
                var2[var1++] = 0;
            }
        }

        this.dBuffer(false, var4, var5, var6, var7);
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
        if ((this.iSOB & 1) == 0) {
            return this.iAlpha;
        } else {
            int var2 = this.iSA & 255;
            return var2 + (int) (b255[(this.iSA >>> 8) - var2] * (float) var1);
        }
    }

    public final int set(byte[] var1, int var2) {
        int var3 = (var1[var2++] & 255) << 8 | var1[var2++] & 255;
        int var4 = var2;
        if (var3 <= 2) {
            return var3 + 2;
        } else {
            try {
                int var5 = 0;
                boolean var6 = false;
                int var7 = var1[var2++] & 255;
                int var8 = var1[var2++] & 255;
                int var9 = var1[var2++] & 255;
                this.isAllL = (var7 & 1) != 0;
                this.isAFix = (var7 & 2) != 0;
                this.isOver = (var7 & 4) != 0;
                this.isCount = (var7 & 8) != 0;
                this.isAnti = (var7 & 16) != 0;
                this.iSOB = var7 >>> 6;
                if ((var8 & 1) != 0) {
                    var5 = var1[var2++] & 255;
                    var6 = true;
                    this.iHint = var5 >>> 4;
                }

                if ((var8 & 2) != 0) {
                    if (!var6) {
                        var5 = var1[var2++] & 255;
                        this.iPenM = var5 >>> 4;
                    } else {
                        this.iPenM = var5 & 15;
                    }

                    var6 = !var6;
                }

                if ((var8 & 4) != 0) {
                    if (!var6) {
                        var5 = var1[var2++] & 255;
                        this.iMask = var5 >>> 4;
                    } else {
                        this.iMask = var5 & 15;
                    }

                    var6 = !var6;
                }

                if ((var8 & 8) != 0) {
                    this.iPen = var1[var2++] & 255;
                }

                if ((var8 & 16) != 0) {
                    this.iTT = var1[var2++] & 255;
                }

                if ((var8 & 32) != 0) {
                    this.iLayer = var1[var2++] & 255;
                }

                if ((var8 & 64) != 0) {
                    this.iLayerSrc = var1[var2++] & 255;
                }

                if ((var9 & 1) != 0) {
                    this.iAlpha = var1[var2++] & 255;
                }

                if ((var9 & 2) != 0) {
                    this.iColor = this.r(var1, var2, 3);
                    var2 += 3;
                }

                if ((var9 & 4) != 0) {
                    this.iColorMask = this.r(var1, var2, 3);
                    var2 += 3;
                }

                if ((var9 & 8) != 0) {
                    this.iSize = var1[var2++] & 255;
                }

                if ((var9 & 16) != 0) {
                    this.iCount = var1[var2++];
                }

                if ((var9 & 32) != 0) {
                    this.iSA = this.r(var1, var2, 2);
                    var2 += 2;
                }

                if ((var9 & 64) != 0) {
                    this.iSS = this.r(var1, var2, 2);
                    var2 += 2;
                }

                if (this.iPen == P_FUSION) {
                    this.iAlpha2 = this.r(var1, var2, 2);
                    var2 += 2;
                }

                if (this.iHint == 8) {
                    int var10 = this.r(var1, var2, 2);
                    var2 += 2;
                    if (var10 == 0) {
                        this.strHint = null;
                    } else {
                        this.strHint = new byte[var10];
                        System.arraycopy(var1, var2, this.strHint, 0, var10);
                        var2 += var10;
                    }
                }

                var4 = var3 - (var2 - var4);
                if (var4 > 0) {
                    if (this.offset == null || this.offset.length < var4) {
                        this.offset = new byte[var4];
                    }

                    this.iOffset = var4;
                    System.arraycopy(var1, var2, this.offset, 0, var4);
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

    public final void set(String var1) {
        try {
            if (var1 == null || var1.length() == 0) {
                return;
            }

            Field[] var2 = this.getClass().getDeclaredFields();
            int var3 = var1.indexOf(64);
            if (var3 < 0) {
                var3 = var1.length();
            }

            int var5;
            int var10;
            for (int var4 = 0; var4 < var3; var4 = var5 + 1) {
                var5 = var1.indexOf(61, var4);
                if (var5 == -1) {
                    break;
                }

                String var6 = var1.substring(var4, var5);
                var4 = var5 + 1;
                var5 = var1.indexOf(59, var4);
                if (var5 < 0) {
                    var5 = var3;
                }

                try {
                    for (var10 = 0; var10 < var2.length; ++var10) {
                        Field var9 = var2[var10];
                        if (var9.getName().equals(var6)) {
                            String var7 = var1.substring(var4, var5);
                            Class var8 = var9.getType();
                            if (var8.equals(Integer.TYPE)) {
                                var9.setInt(this, Integer.parseInt(var7));
                            } else if (var8.equals(Boolean.TYPE)) {
                                var9.setBoolean(this, Integer.parseInt(var7) != 0);
                            } else {
                                var9.set(this, var7);
                            }
                            break;
                        }
                    }
                } catch (NumberFormatException var11) {
                } catch (IllegalAccessException var12) {
                }
            }

            if (var3 != var1.length()) {
                ByteStream var14 = this.getWork();

                for (var10 = var3 + 1; var10 < var1.length(); var10 += 2) {
                    var14.write(Character.digit(var1.charAt(var10), 16) << 4 | Character.digit(var1.charAt(var10 + 1), 16));
                }

                this.offset = var14.toByteArray();
                this.iOffset = this.offset.length;
            }
        } catch (Throwable var13) {
        }

    }

    public final void set(Mg var1) {
        if (var1 == null) {
            var1 = mgDef;
        }

        this.iHint = var1.iHint;
        this.iPen = var1.iPen;
        this.iPenM = var1.iPenM;
        this.iTT = var1.iTT;
        this.iMask = var1.iMask;
        this.iSize = var1.iSize;
        this.iSS = var1.iSS;
        this.iCount = var1.iCount;
        this.isOver = var1.isOver;
        this.isCount = var1.isCount;
        this.isAFix = var1.isAFix;
        this.isAnti = var1.isAnti;
        this.isAllL = var1.isAllL;
        this.iAlpha = var1.iAlpha;
        this.iAlpha2 = var1.iAlpha2;
        this.iSA = var1.iSA;
        this.iColor = var1.iColor;
        this.iColorMask = var1.iColorMask;
        this.iLayer = var1.iLayer;
        this.iLayerSrc = var1.iLayerSrc;
        this.iSOB = var1.iSOB;
        this.strHint = var1.strHint;
        this.iOffset = 0;
    }

    private final void setD(int var1, int var2, int var3, int var4) {
        this.user.dX = var1;
        this.user.dY = var2;
        this.user.dW = var3;
        this.user.dH = var4;
    }

    public void setInfo(Mg.Info var1) {
        this.info = var1;
    }

    public void setUser(Mg.User var1) {
        this.user = var1;
    }

    private final void shift(int var1, int var2) {
        System.arraycopy(this.user.pX, 1, this.user.pX, 0, 3);
        System.arraycopy(this.user.pY, 1, this.user.pY, 0, 3);
        this.user.pX[3] = var1;
        this.user.pY[3] = var2;
    }

    private final int ss(int var1) {
        if ((this.iSOB & 2) == 0) {
            return this.iSize;
        } else {
            int var2 = this.iSS & 255;
            return (int) (((float) var2 + b255[(this.iSS >>> 8) - var2] * (float) var1) * this.user.pV);
        }
    }

    private final void t() {
        if (this.iTT != 0) {
            byte[] var1 = this.info.iMOffs;
            int var4 = this.info.W;
            int var5 = this.user.dX;
            int var6 = this.user.dY;
            int var7 = this.user.dW;
            int var8 = this.user.dH;

            for (int var9 = var6; var9 < var8; ++var9) {
                int var3 = var4 * var9 + var5;

                for (int var2 = var5; var2 < var7; ++var2) {
                    var1[var3] = (byte) ((int) ((float) (var1[var3++] & 255) * this.getTT(var2, var9)));
                }
            }

        }
    }

    private final void wPo(int var1) throws IOException {
        ByteStream var2 = this.info.workOut;
        if (var1 <= 127 && var1 >= -127) {
            var2.write(var1);
        } else {
            var2.write(-128);
            var2.w((long) var1, 2);
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
        private float[] pTT = null;
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

        private void setup(Mg var1) {
            this.pV = Mg.b255[var1.info.bPen[var1.iPenM].length - 1];
            var1.getPM();
            this.count = 0;
            this.iDCount = 0;
            this.oX = -1000;
            this.oY = -1000;
            this.isDirect = var1.iPen == P_SUISAI2 || var1.iHint == H_COPY || var1.isOver;
            if (var1.iTT >= 12) {
                this.pTT = Mg.this.info.getTT(var1.iTT);
                this.pTTW = (int) Math.sqrt((double) this.pTT.length);
            }

        }

        public void setIm(Mg var1) {
            if (var1.iHint != 8) {
                if (this.pM != var1.iPenM || this.pA != var1.iAlpha || this.pS != var1.iSize) {
                    int[] var2 = var1.info.bPen[var1.iPenM][var1.iSize];
                    int var3 = var2.length;
                    if (this.p == null || this.p.length < var3) {
                        this.p = new int[var3];
                    }

                    float var4 = Mg.b255[var1.iAlpha];

                    for (int var6 = 0; var6 < var3; ++var6) {
                        float var5 = (float) var2[var6] * var4;
                        this.p[var6] = var5 <= 1.0F && var5 > 0.0F ? 1 : (int) var5;
                    }

                    this.pW = var1.iPen = (int) Math.sqrt((double) var3);
                    this.pM = var1.iPenM;
                    this.pA = var1.iAlpha;
                    this.pS = var1.iSize;
                }

            }
        }

        public int getPixel(int var1, int var2) {
            int var3 = Mg.this.info.imW;
            if (var1 >= 0 && var2 >= 0 && var1 < var3 && var2 < Mg.this.info.imH) {
                Mg.this.mkLPic(this.buffer, var1, var2, 1, 1, Mg.this.info.Q);
                return Mg.this.info.iOffs[Mg.this.info.m.iLayer][var3 * var2 + var1] & -16777216 | this.buffer[0];
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

        public void setSize(int var1, int var2, int var3) {
            if (var1 * var3 != this.W || var2 * var3 != this.H) {
                this.iOffs = null;
            }

            this.imW = var1;
            this.imH = var2;
            this.W = var1 * var3;
            this.H = var2 * var3;
            this.Q = var3;
            int var4 = this.W * this.H;
            if (this.iMOffs == null || this.iMOffs.length < var4) {
                this.iMOffs = new byte[var4];
            }

        }

        public void setIOffs(int[][] var1) {
            this.L = var1.length;
            this.iOffs = var1;
            if (this.visit == null || this.visit.length != this.L) {
                Mg.this.memset(this.visit = new float[this.L], 1.0F);
            }

        }

        public void setComponent(Component var1, Graphics var2, int var3, int var4) {
            this.component = var1;
            this.vWidth = var3;
            this.vHeight = var4;
            this.g = var2;
        }

        public void setL(int var1) {
            int var2 = this.iOffs == null ? 0 : this.iOffs.length;
            Math.min(var2, var1);
            if (var2 != var1) {
                float[] var3 = new float[var1];
                int[][] var4 = new int[var1][];
                int var5 = this.W * this.H;

                for (int var6 = 0; var6 < var1; ++var6) {
                    if (var6 >= var2) {
                        var3[var6] = 1.0F;
                        Mg.this.memset(var4[var6] = new int[var5], 16777215);
                    } else {
                        var4[var6] = this.iOffs[var6];
                        var3[var6] = this.visit[var6];
                    }
                }

                this.visit = var3;
                this.iOffs = var4;
            }

            this.L = var1;
        }

        public void delL(int var1) {
            int var2 = this.iOffs.length;
            if (var1 < var2) {
                int var3 = var2 - 1;
                float[] var4 = new float[var3];
                int[][] var5 = new int[var3][];
                var3 = 0;

                for (int var6 = 0; var6 < var2; ++var6) {
                    if (var6 != var1) {
                        var4[var3] = this.visit[var6];
                        var5[var3++] = this.iOffs[var6];
                    }
                }

                this.visit = var4;
                this.iOffs = var5;
                this.L = var2 - 1;
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

        public void setQuality(int var1) {
            this.Q = var1;
            this.imW = this.W / this.Q;
            this.imH = this.H / this.Q;
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

        public int getPenSize(Mg var1) {
            return (int) Math.sqrt((double) this.bPen[var1.iPenM][var1.iSize].length);
        }

        public int getPMMax() {
            return this.m.iHint == 8 ? 255 : this.bPen[this.m.iPenM].length;
        }

        public int[][] getOffset() {
            return this.iOffs;
        }

        public float[] getTT(int var1) {
            var1 -= 12;
            if (this.bTT[var1] == null) {
                if (this.dirTT != null) {
                    String var2 = this.dirTT;
                    this.dirTT = null;

                    try {
                        this.cnf.loadZip(var2);
                    } catch (IOException var6) {
                        var6.printStackTrace();
                    }
                }

                int[] var7 = Mg.this.loadIm("tt/" + var1 + ".gif", false);
                if (var7 == null) {
                    return null;
                }

                int var3 = var7.length;
                float[] var4 = new float[var3];

                for (int var5 = 0; var5 < var3; ++var5) {
                    var4[var5] = Mg.b255[var7[var5]];
                }

                this.bTT[var1] = var4;
            }

            return this.bTT[var1];
        }
    }
}
