package paintchat;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/** Currently only used by PCHViewer */
public class MgLine {
    public int head = 0;
    private int line_size = 1;
    private int i_color = 0;
    private int i_mask = 0;
    private int i_alpha = 255;
    private int layer = 0;
    private int layer_end = 0;
    private int mask = 110;
    private Rectangle rect;
    private short firstX = 0;
    private short firstY = 0;
    private byte[] offset;
    public static final byte M_LINE = 0;
    public static final byte M_SUISAI = 1;
    public static final byte M_TEXT = 2;
    public static final byte M_X = 5;
    public static final byte M_XX = 6;
    public static final byte M_TONE = 7;
    public static final byte M_BOKASHI = 8;
    public static final byte M_LIGHT = 9;
    public static final byte M_DARK = 10;
    public static final byte M_WHITE = 19;
    public static final byte M_RECT = 20;
    public static final byte M_FRECT = 21;
    public static final byte M_OVAL = 22;
    public static final byte M_FOVAL = 23;
    public static final byte M_RWHITE = 39;
    public static final byte M_MOVE = 40;
    public static final byte M_BRECT = 41;
    public static final byte M_ABS_LR = 42;
    public static final byte M_ABS_TB = 43;
    public static final byte M_LIE = 44;
    public static final byte M_FUSION = 45;
    public static final byte M_V_F = 60;
    public static final byte M_V_L = 61;
    public static final byte M_V_B = 62;
    public static final byte M_PASTE = -2;
    public static final byte M_DELETE = 100;
    public static final byte M_EMPTY = 101;
    public static final byte M_HEADER = 102;
    public static final byte M_ARCHIVE = 103;
    public static final byte M_PING = 104;
    public static final byte M_PAINTBBS = 105;
    public static final byte M_PRELINE = 106;
    public static final byte M_PAINTCHAT = 107;
    public static final byte M_IMAGE = 108;
    public static final byte M_OUT = 110;
    public static final byte N = 110;
    public static final byte M = 111;
    public static final byte R = 112;
    public static final byte ADD = 113;
    public static final byte SUB = 114;
    public static final byte MK_M = 1;
    public static final byte MK_S = 2;
    public static final byte MK_C = 4;
    public static final byte MK_MC = 8;
    public static final byte MK_A = 16;
    public static final byte MK_L = 32;
    private static float[] b255 = new float[256];
    private static int[][] b_arc = null;
    private static ColorModel color_model = null;
    private static SRaster[] raster = null;
    private static Image[] imBuffer = null;
    private static int[][] b_d;
    private int visit0 = 255;
    private int visit1 = 255;
    private int alpha;
    private int d_x;
    private int d_y;
    private int d_w;
    private int d_h;
    private int[] buffer_work;
    private int vWidth;
    private int vHeight;
    public Component component;
    private int userNum = 0;

    public MgLine() {
    }

    public MgLine(int var1) {
        this.userNum = var1;
    }

    private final int aM(int var1, int var2) {
        return this.mask == 113 ? Math.min(var1, var2) : (this.mask == 114 ? Math.max(var1, var2) : var1);
    }

    public short[] bezier(Point[] var1) {
        int var2 = var1.length - 1;
        int var5 = 0;

        try {
            short[] var16 = new short[600];

            for (int var3 = 0; var3 < 300; ++var3) {
                double var10 = (double) var3 / 300.0D;
                double var12 = 0.0D;
                double var14 = 0.0D;

                for (int var4 = 0; var4 <= var2; ++var4) {
                    double var8 = (double) this.kei(var2, var4) * Math.pow(var10, (double) var4) * Math.pow(1.0D - var10, (double) (var2 - var4));
                    var12 += var8 * (double) var1[var2 - var4].x;
                    var14 += var8 * (double) var1[var2 - var4].y;
                }

                short var6 = (short) ((int) Math.round(var12));
                short var7 = (short) ((int) Math.round(var14));
                if (var5 == 0 || var16[var5 - 2] != var6 || var16[var5 - 1] != var7) {
                    var16[var5] = var6;
                    var16[var5 + 1] = var7;
                    var5 += 2;
                }
            }

            if (var5 <= 2) {
                var16[2] = var16[0];
                var16[3] = var16[1];
                var5 = 2;
            }

            if (var5 < var16.length) {
                short[] var17 = new short[var5];
                System.arraycopy(var16, 0, var17, 0, var5);
                return var17;
            } else {
                return var16;
            }
        } catch (Exception var18) {
            var18.printStackTrace();
            return null;
        }
    }

    private void clear(Graphics var1, int[][] var2, int var3, int var4) {
        int[] var5 = var2[0];
        int var6 = 16777215;
        int var7 = var3 * var4;
        synchronized (var5) {
            int var9 = 0;

            while (true) {
                if (var9 >= var3) {
                    for (var9 = var3; var9 < var7; var9 += var3) {
                        System.arraycopy(var5, 0, var5, var9, var3);
                    }

                    System.arraycopy(var5, 0, var2[1], 0, var5.length);
                    break;
                }

                var5[var9] = var6;
                ++var9;
            }
        }

        this.d_x = 0;
        this.d_y = 0;
        this.d_w = var3;
        this.d_h = var4;
    }

    public void create(int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
        this.head = var1;
        this.line_size = var3 <= 0 ? 1 : var3;
        this.mask = var2 == 0 ? 110 : var2;
        this.i_mask = var5 & 16777215;
        this.i_alpha = var6 == 0 ? 255 : var6;
        this.i_color = var4 & 16777215;
        this.layer = var7;
    }

    private final void dBuffer(Graphics var1, Graphics var2, int[][] var3, int var4, int var5, int var6, int var7, int var8) {
        try {
            int var9 = this.d_x <= 1 ? 0 : this.d_x - 1;
            int var10 = this.d_y <= 1 ? 0 : this.d_y - 1;
            int var11 = this.d_w + this.line_size + 1;
            int var12 = this.d_h + this.line_size + 1;
            var11 = var11 >= var4 ? var4 - var9 : var11 - var9;
            var12 = var12 >= var5 ? var5 - var10 : var12 - var10;
            if (var11 <= 0 || var12 <= 0 || this.vWidth < (var9 - var6) * var8 || this.vHeight < (var10 - var7) * var8) {
                return;
            }

            int[] var13 = b_d[this.userNum];
            int var14 = var12 + var10;
            int var15 = (var9 - var6) * var8;
            var12 = var13.length / var11;

            do {
                var12 = Math.min(var12, var14 - var10);
                Image var16 = this.makeLPic((int[]) null, var3, var4, var5, var9, var10, var11, var12);
                if (var1 != null) {
                    var1.drawImage(var16, var15, (var10 - var7) * var8, var11 * var8, var12 * var8, Color.white, (ImageObserver) null);
                }

                if (var2 != null) {
                    var2.drawImage(var16, var15, (var10 - var7) * var8, var11 * var8, var12 * var8, Color.white, (ImageObserver) null);
                }
            } while ((var10 += var12) < var14);
        } catch (RuntimeException var17) {
            var17.printStackTrace();
        }

    }

    private void dFusion(int[][] var1, int var2, int var3) {
        Rectangle var4 = (new Rectangle(var2, var3)).intersection(this.rect);
        if (!var4.isEmpty()) {
            int var5 = var4.x + var4.width;
            int var6 = var4.y + var4.height;
            int[] var19 = var1[this.layer];
            int[] var20 = var1[this.layer == 0 ? 1 : 0];

            for (int var21 = var4.y; var21 < var6; ++var21) {
                int var7 = var21 * var2 + var4.x;

                for (int var22 = var4.x; var22 < var5; ++var22) {
                    int var12 = var1[0][var7];
                    int var13 = var1[1][var7];
                    int var9 = var13 >>> 24;
                    int var8 = (int) (b255[255 - var9] * (float) (var12 >>> 24));
                    int var15 = var9 + var8 << 24;
                    double var17 = (double) (var9 + var8 == 0 ? 0.0F : 255.0F / (float) (var9 + var8));
                    int var11 = (int) ((double) var9 * var17);
                    int var10 = (int) ((double) var8 * var17);
                    int var14 = 255 - var11 - var10;

                    for (int var23 = 16; var23 >= 0; var23 -= 8) {
                        int var16 = (int) (b255[var13 >>> var23 & 255] * (float) var11) + (int) (b255[var12 >>> var23 & 255] * (float) var10) + var14;
                        var16 = var16 < 0 ? 0 : (var16 > 255 ? 255 : var16);
                        var15 |= var16 << var23;
                    }

                    var19[var7] = var15;
                    var20[var7] = 16777215;
                    ++var7;
                }
            }

            this.d_x = var4.x;
            this.d_y = var4.y;
            this.d_w = var4.x + var4.width;
            this.d_h = var4.y + var4.height;
        }
    }

    private void dLine(Graphics var1, Graphics var2, int[][] var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10) throws InterruptedException {
        try {
            int var16 = Math.max(this.line_size >>> 3, 1);
            int var19 = this.firstX;
            int var20 = this.firstY;
            int var23 = this.offset.length;
            boolean var26 = var9 >= 0;
            Thread var27 = var9 > 0 ? Thread.currentThread() : null;
            int[] var28 = var3[this.layer];

            for (int var12 = 0; var12 < var23; var12 += 2) {
                int var17 = var19;
                int var18 = var20;
                var19 += this.offset[var12];
                var20 += this.offset[var12 + 1];
                this.d_x = this.d_x > var19 ? var19 : this.d_x;
                this.d_y = this.d_y > var20 ? var20 : this.d_y;
                this.d_w = this.d_w < var19 ? var19 : this.d_w;
                this.d_h = this.d_h < var20 ? var20 : this.d_h;
                int var14 = var19 - var17;
                int var15 = var20 - var18;
                int var21 = var14 < 0 ? -var14 : var14;
                int var22 = var15 < 0 ? -var15 : var15;
                int var13;
                float var24;
                float var25;
                if (var21 >= var22) {
                    var13 = var21;
                    var24 = (float) (var14 < 0 ? -1 : 1);
                    if (var15 != 0) {
                        var25 = (float) var15 / (float) var21;
                    } else {
                        var25 = 0.0F;
                    }
                } else {
                    var13 = var22;
                    var25 = (float) (var15 < 0 ? -1 : 1);
                    if (var15 != 0) {
                        var24 = (float) var14 / (float) var22;
                    } else {
                        var24 = 0.0F;
                    }
                }

                int var11;
                label129:
                switch (var10) {
                    case 0:
                        var11 = 0;

                        while (true) {
                            if (var11 > var13) {
                                break label129;
                            }

                            this.dPen(var28, var4, var5, var17 + Math.round(var24 * (float) var11), var18 + Math.round(var25 * (float) var11));
                            ++var11;
                        }
                    case 1:
                        var11 = 0;

                        while (true) {
                            if (var11 >= var13) {
                                break label129;
                            }

                            if (var11 % var16 == 0) {
                                this.dPen(var28, var4, var5, var17 + (int) (var24 * (float) var11), var18 + (int) (var25 * (float) var11));
                            }

                            ++var11;
                        }
                    case 2:
                        var11 = 0;

                        while (true) {
                            if (var11 >= var13) {
                                break label129;
                            }

                            if (this.line_size <= 2) {
                                var1.fillRect(var17 + (int) (var24 * (float) var11), var18 + (int) (var25 * (float) var11), this.line_size, this.line_size);
                            } else {
                                var1.fillOval(var17 + (int) (var24 * (float) var11), var18 + (int) (var25 * (float) var11), this.line_size, this.line_size);
                            }

                            ++var11;
                        }
                    case 3:
                        for (var11 = 0; var11 < var13; ++var11) {
                            this.dPen(var28, var4, var5, var17 + Math.round(var24 * (float) var11), var18 + Math.round(var25 * (float) var11));
                        }
                }

                if (var26) {
                    this.dBuffer(var1, var2, var3, var4, var5, var6, var7, var8);
                    this.d_x = var19;
                    this.d_y = var20;
                    this.d_w = var19;
                    this.d_h = var20;
                }

                if (var27 != null) {
                    Thread.sleep((long) var9);
                }
            }
        } catch (RuntimeException var29) {
            var29.printStackTrace();
        }

    }

    private void dMove(int[][] var1, int var2, int var3, int var4, int var5, int var6) {
        try {
            int var7 = Math.max(0, this.firstX);
            int var8 = Math.max(0, this.firstY);
            int var9 = this.firstX < 0 ? this.rect.width + this.firstX : (var7 + this.rect.width >= var2 ? this.rect.width - (var7 + this.rect.width - var2) : this.rect.width);
            int var10 = this.firstY < 0 ? this.rect.height + this.firstY : (var8 + this.rect.height >= var3 ? this.rect.height - (var8 + this.rect.height - var3) : this.rect.height);
            int var11 = this.rect.x + (this.firstX <= 0 ? this.rect.width - var9 : 0);
            int var12 = this.rect.y + (this.firstY <= 0 ? this.rect.height - var10 : 0);
            if (var9 <= 0 || var10 <= 0 || var7 >= var2 || var8 >= var3) {
                return;
            }

            int var13 = var2 * var12 + var11;
            int var14 = var2 * var8 + var7;
            int var15 = var9 * var10;
            int[] var16 = new int[var15];

            int var17;
            for (var17 = 0; var17 < var10; ++var17) {
                System.arraycopy(var1[this.layer], var13 + var2 * var17, var16, var9 * var17, var9);
            }

            for (var17 = 0; var17 < var10; ++var17) {
                System.arraycopy(var16, var9 * var17, var1[this.layer_end], var14 + var2 * var17, var9);
            }

            this.d_x = Math.min(var7, var11);
            this.d_y = Math.min(var8, var12);
            this.d_w = Math.max(var7, var11) + var9;
            this.d_h = Math.max(var8, var12) + var10;
            var16 = (int[]) null;
        } catch (RuntimeException var18) {
            System.out.println(var18);
        }

    }

    private final void dPen(int[] var1, int var2, int var3, int var4, int var5) {
        int var6 = 0;
        int var8 = this.line_size * this.line_size;
        int var9 = var2 * var3;
        int var10 = var5 * var2 + var4 - 1;
        int var11 = -1;
        int var12 = var4 - 1;
        int var13 = var5;
        int var7;
        int var14;
        int var15;
        int var17;
        int var18;
        int var19;
        int var28;
        int var29;
        switch (this.head) {
            case 0:
                for (; var6 < var8; ++var6) {
                    ++var11;
                    if (var11 >= this.line_size) {
                        var11 = 0;
                        var12 = var4;
                        ++var13;
                        var10 += var2 - this.line_size + 1;
                    } else {
                        ++var12;
                        ++var10;
                    }

                    if (!this.isArc(var6) && var10 >= 0 && var10 < var9 && var12 >= 0 && var12 < var2 && !this.isM(var1[var10]) && !this.isAm(this.i_color, var1[var10])) {
                        var1[var10] = this.getAPix(var1[var10], this.i_color);
                    }
                }

                return;
            case 1:
                for (; var6 < var8; ++var6) {
                    ++var11;
                    if (var11 >= this.line_size) {
                        var11 = 0;
                        var12 = var4;
                        var10 += var2 - this.line_size + 1;
                    } else {
                        ++var12;
                        ++var10;
                    }

                    if (var10 >= 0 && var10 < var9 && var12 >= 0 && var12 < var2) {
                        var7 = var1[var10];
                        if (!this.isArc(var6) && !this.isM(var7)) {
                            var15 = var7 >> 24 & 255;
                            var19 = Math.max((255 - var15) / this.alpha, 1);
                            var15 = Math.min(var15 + var19, 255) << 24;

                            for (var14 = 16; var14 >= 0; var14 -= 8) {
                                int var32 = var7 >> var14 & 255;
                                var17 = this.i_color >> var14 & 255;
                                var18 = var17 - var32;
                                var18 = var18 < 0 ? (var18 >= -this.alpha ? var32 - 1 : Math.max(var32 + var18 / this.alpha * var19, var17)) : (var18 > 0 ? (var18 <= this.alpha ? var32 + 1 : Math.min(var32 + var18 / this.alpha * var19, var17)) : var32);
                                var15 |= this.aM(var18 < 0 ? 0 : (var18 > 255 ? 255 : var18), var32) << var14;
                            }

                            var1[var10] = var15;
                        }
                    }
                }
                break;
            case 7:
                for (; var6 < var8; ++var6) {
                    ++var11;
                    if (var11 >= this.line_size) {
                        var11 = 0;
                        var12 = var4;
                        ++var13;
                        var10 += var2 - this.line_size + 1;
                    } else {
                        ++var12;
                        ++var10;
                    }

                    if (!this.isArc(var6) && var10 >= 0 && var10 < var9 && var12 >= 0 && var12 < var2 && !this.isM(var1[var10]) && !isTone(this.alpha, var12, var13)) {
                        var1[var10] = -16777216 | this.i_color & 16777215;
                    }
                }

                return;
            case 8:
                var14 = Math.min(var5 + this.line_size, var3);
                var15 = Math.min(var12 + this.line_size, var2);
                var12 = Math.max(var12, 0);
                var13 = Math.max(var5, 0);
                if (var15 - var12 > 0 && var14 - var13 > 0) {
                    int[] var16 = new int[4];
                    float var25 = Math.max((float) (255 - this.i_alpha) / 16.0F, 1.0F);
                    int var27 = 0;

                    int var23;
                    int var24;
                    for (var28 = var13; var28 < var14; ++var28) {
                        int var26 = var28 * var2 + var12;

                        for (var29 = var12; var29 < var15; ++var29) {
                            if (this.isM(var1[var26])) {
                                this.buffer_work[var27++] = var1[var26++];
                            } else {
                                for (var6 = 0; var6 < 4; ++var6) {
                                    var19 = var6 << 3;
                                    var17 = var1[var26] >>> var19 & 255;
                                    var18 = 0;
                                    int var20 = 0;

                                    for (var24 = -2; var24 <= 2; ++var24) {
                                        for (var23 = -2; var23 <= 2; ++var23) {
                                            int var21 = var29 + var23;
                                            int var22 = var28 + var24;
                                            if (var21 >= 0 && var21 < var2 && var22 >= 0 && var22 < var3) {
                                                var18 += var1[var2 * var22 + var21] >>> var19 & 255;
                                                ++var20;
                                            }
                                        }
                                    }

                                    var17 -= (int) (((float) var17 - (float) var18 / (float) var20) / var25);
                                    var16[var6] = (var17 < 0 ? 0 : (var17 > 255 ? 255 : var17)) << var19;
                                }

                                this.buffer_work[var27++] = var16[3] | var16[2] | var16[1] | var16[0];
                                ++var26;
                            }
                        }
                    }

                    var23 = var15 - var12;
                    var24 = var14 - var13;

                    for (var6 = 0; var6 < var24; ++var6) {
                        System.arraycopy(this.buffer_work, var6 * var23, var1, var2 * (var6 + var13) + var12, var23);
                    }
                }
                break;
            case 9:
            case 10:
                float var31 = (float) Math.max(255 - this.i_alpha, 1);

                for (var6 = 0; var6 < var8; ++var6) {
                    ++var11;
                    if (var11 >= this.line_size) {
                        var11 = 0;
                        var10 += var2 - this.line_size + 1;
                        var12 = var4;
                    } else {
                        ++var10;
                        ++var12;
                    }

                    if (var10 >= 0 && var10 < var9 && var12 >= 0 && var12 < var2) {
                        var7 = var1[var10];
                        if (!this.isArc(var6) && !this.isM(var7)) {
                            var28 = var7 >> 16 & 255;
                            var29 = var7 >> 8 & 255;
                            int var30 = var7 & 255 & 255;
                            if (this.head == 9) {
                                var28 += Math.round((float) var28 / var31);
                                var29 += Math.round((float) var29 / var31);
                                var30 += Math.round((float) var30 / var31);
                            } else {
                                var28 -= Math.round((float) (255 - var28) / var31);
                                var29 -= Math.round((float) (255 - var29) / var31);
                                var30 -= Math.round((float) (255 - var30) / var31);
                            }

                            var1[var10] = var7 & -16777216 | (var28 > 0 ? (var28 <= 255 ? var28 : 255) : 0) << 16 | (var29 > 0 ? (var29 <= 255 ? var29 : 255) : 0) << 8 | (var30 > 0 ? (var30 <= 255 ? var30 : 255) : 0);
                        }
                    }
                }

                return;
            case 19:
                for (; var6 < var8; ++var6) {
                    ++var11;
                    if (var11 >= this.line_size) {
                        var11 = 0;
                        var12 = var4;
                        ++var13;
                        var10 += var2 - this.line_size + 1;
                    } else {
                        ++var12;
                        ++var10;
                    }

                    if (!this.isArc(var6) && var10 >= 0 && var10 < var9 && var12 >= 0 && var12 < var2 && !this.isM(var1[var10]) && !this.isAm(this.i_color, var1[var10])) {
                        var1[var10] = this.getWPix(var1[var10]);
                    }
                }
        }

    }

    public final void draw(Component var1, Graphics var2, Graphics var3, int[][] var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11, int var12) {
        try {
            this.d_x = this.firstX;
            this.d_y = this.firstY;
            this.d_w = this.d_x;
            this.d_h = this.d_y;
            if (var1 != null) {
                this.component = var1;
                Dimension var13 = var1.getSize();
                this.vWidth = var13.width;
                this.vHeight = var13.height;
            }

            this.visit0 = var10;
            this.visit1 = var11;
            switch (this.head) {
                case -2:
                    if (var12 >= 0) {
                        var12 = -1;
                    }

                    this.paste(var4[this.layer], var5, var6);
                    break;
                case 0:
                case 19:
                    this.dLine(var2, var3, var4, var5, var6, var7, var8, var9, var12, this.i_alpha == 255 ? 0 : 3);
                    break;
                case 2:
                    if (var12 >= 0) {
                        var12 = -1;
                    }

                    this.alpha = this.i_alpha;
                    this.dText(var4[this.layer], var5, var6);
                    break;
                case 7:
                    this.alpha = this.i_alpha / 23;
                    this.dLine(var2, var3, var4, var5, var6, var7, var8, var9, var12, 0);
                    break;
                case 8:
                    this.buffer_work = b_d[this.userNum];
                case 1:
                case 9:
                case 10:
                    this.alpha = (255 - this.i_alpha) / 2 + 1;
                    this.dLine(var2, var3, var4, var5, var6, var7, var8, var9, var12, 1);
                    break;
                case 20:
                case 21:
                case 22:
                case 23:
                case 39:
                    this.alpha = this.i_alpha;
                    if (var12 >= 0) {
                        var12 = -1;
                    }

                    this.dRectOval(var4[this.layer], var5, var6, var7, var8, var9);
                    break;
                case 40:
                    if (var12 >= 0) {
                        var12 = -1;
                    }

                    this.dMove(var4, var5, var6, var7, var8, var9);
                    break;
                case 41:
                case 42:
                case 43:
                case 44:
                    if (var12 >= 0) {
                        var12 = -1;
                    }

                    this.alpha = this.i_alpha;
                    this.replace(var4, var5, var6);
                    break;
                case 45:
                    if (var12 >= 0) {
                        var12 = -1;
                    }

                    this.dFusion(var4, var5, var6);
                    break;
                case 100:
                    if (var12 >= 0) {
                        var12 = -1;
                    }

                    this.clear(var2, var4, var5, var6);
                case 101:
                default:
                    break;
                case 106:
                    var12 = -2;
                    this.dLine(var2, var3, var4, var5, var6, var7, var8, var9, var12, 2);
            }
        } catch (RuntimeException var14) {
            var14.printStackTrace();
        } catch (InterruptedException var15) {
        }

        if (var12 == -1 && var2 != null) {
            this.dBuffer(var2, var3, var4, var5, var6, var7, var8, var9);
        }

    }

    private void dRectOval(int[] var1, int var2, int var3, int var4, int var5, int var6) throws RuntimeException {
        try {
            Rectangle var7 = (new Rectangle(var2, var3)).intersection(this.rect);
            if (var7.isEmpty()) {
                return;
            }

            int var8;
            int var9;
            int var10;
            int var15;
            int var16;
            int var17;
            label159:
            switch (this.head) {
                case 20:
                case 39:
                    for (var9 = 0; var9 < var7.height; ++var9) {
                        var8 = var2 * (var7.y + var9) + var7.x;

                        for (var10 = 0; var10 < var7.width; ++var10) {
                            if (!this.isM(var1[var8])) {
                                var1[var8] = this.head == 39 ? this.getWPix(var1[var8]) : this.getAPix(var1[var8], this.i_color);
                            }

                            ++var8;
                        }
                    }
                    break;
                case 21:
                    var9 = var7.x;
                    var10 = var7.y;
                    int var23 = var7.width;
                    int var24 = var7.height - 1;
                    int var26 = 0;

                    while (true) {
                        if (var26 >= this.line_size) {
                            break label159;
                        }

                        var8 = var2 * var10 + var9;
                        int var28 = var8;

                        for (var15 = 0; var15 < 2; ++var15) {
                            for (var16 = 0; var16 < var23; ++var16) {
                                if (!this.isM(var1[var28])) {
                                    var1[var28] = this.getAPix(var1[var28], this.i_color);
                                }

                                ++var28;
                            }

                            var28 = var8 + var2 * var24;
                        }

                        var28 = var8 + var2;

                        for (var15 = 0; var15 < 2; ++var15) {
                            for (var16 = 0; var16 < var24 - 1; ++var16) {
                                if (!this.isM(var1[var28])) {
                                    var1[var28] = this.getAPix(var1[var28], this.i_color);
                                }

                                var28 += var2;
                            }

                            var28 = var8 + var2 + var23 - 1;
                        }

                        ++var9;
                        ++var10;
                        var23 -= 2;
                        var24 -= 2;
                        if (var23 <= 0 || var24 <= 0) {
                            break label159;
                        }

                        ++var26;
                    }
                case 22:
                    try {
                        var9 = var7.width;
                        var10 = var7.height;
                        Image var11 = this.component.createImage(var9, var10);
                        Graphics var12 = var11.getGraphics();
                        var12.setColor(Color.black);
                        var12.fillRect(0, 0, var9, var10);
                        var12.setColor(Color.white);
                        var12.fillOval(1, 1, var9 - 2, var10 - 2);
                        var12.dispose();
                        PixelGrabber var25 = new PixelGrabber(var11, 0, 0, var9, var10, true);
                        var25.grabPixels();
                        int[] var27 = (int[]) var25.getPixels();
                        var11.flush();
                        var11 = null;
                        var8 = var2 * var7.y + var7.x;
                        var15 = 0;
                        var16 = 0;

                        while (true) {
                            if (var16 >= var10) {
                                break label159;
                            }

                            for (var17 = 0; var17 < var9; ++var17) {
                                if (!this.isM(var1[var8]) && (var27[var15] & 16777215) != 0) {
                                    var1[var8] = this.getAPix(var1[var8], this.i_color);
                                }

                                ++var8;
                                ++var15;
                            }

                            var8 += var2 - var9;
                            ++var16;
                        }
                    } catch (InterruptedException var21) {
                        break;
                    }
                case 23:
                    try {
                        Image var13 = this.component.createImage(var7.width, var7.height);
                        Graphics var14 = var13.getGraphics();
                        var14.setColor(Color.black);
                        var14.fillRect(0, 0, var7.width, var7.height);
                        var14.setColor(Color.white);

                        for (var15 = 0; var15 < this.line_size; ++var15) {
                            for (var16 = 0; var16 < this.line_size; ++var16) {
                                var14.drawOval(var16 + 1, var15 + 1, var7.width - var16 * 2 - 2, var7.height - var15 * 2 - 2);
                            }
                        }

                        var14.dispose();
                        PixelGrabber var29 = new PixelGrabber(var13, 0, 0, var7.width, var7.height, true);
                        var29.grabPixels();
                        int[] var30 = (int[]) var29.getPixels();
                        var13.flush();
                        var13 = null;
                        var8 = var2 * var7.y + var7.x;
                        var17 = 0;

                        for (int var18 = 0; var18 < var7.height; ++var18) {
                            for (int var19 = 0; var19 < var7.width; ++var19) {
                                if (!this.isM(var1[var8]) && var30[var17] >= -7829368) {
                                    var1[var8] = this.getAPix(var1[var8], this.i_color);
                                }

                                ++var8;
                                ++var17;
                            }

                            var8 += var2 - var7.width;
                        }
                    } catch (InterruptedException var20) {
                    }
            }

            this.d_x = var7.x;
            this.d_y = var7.y;
            this.d_w = var7.x + var7.width;
            this.d_h = var7.y + var7.height;
        } catch (RuntimeException var22) {
            System.out.println("oval" + var22);
        }

    }

    private void dText(int[] var1, int var2, int var3) {
        try {
            Font var4 = new Font("sansserif", 0, this.line_size);
            FontMetrics var5 = Toolkit.getDefaultToolkit().getFontMetrics(var4);
            String var6 = new String(this.offset, "UTF8");
            if (var6.length() <= 0) {
                return;
            }

            int var7 = var5.stringWidth(var6) + var6.length();
            int var8 = var5.getMaxAscent() + var5.getMaxDescent();
            int var9 = var8 - var5.getMaxDescent();
            this.d_x = this.firstX;
            this.d_y = this.firstY - var8;
            this.d_w = this.firstX + var7;
            this.d_h = this.firstY;
            Image var10 = this.component.createImage(var7, var8);
            Graphics var11 = var10.getGraphics();
            var11.setFont(var4);
            var11.setColor(Color.black);
            var11.fillRect(0, 0, var7, var8);
            var11.setColor(Color.white);
            var11.drawString(var6, var5.getLeading(), var9);
            var11.dispose();
            var11 = null;
            var4 = null;
            var5 = null;
            PixelGrabber var12 = new PixelGrabber(var10, 0, 0, var7, var8, true);
            var12.grabPixels();
            int[] var13 = (int[]) var12.getPixels();
            var12 = null;
            var10.flush();
            var10 = null;
            int var14 = var2 * this.firstY - var2 * var9 + this.firstX;
            int var15 = 0;
            int[] var17 = new int[3];
            int[] var18 = new int[3];
            int var21 = var2 * var3;

            int var22;
            for (var22 = 0; var22 < 3; ++var22) {
                var17[var22] = this.i_color >>> (var22 << 3) & 255;
            }

            for (var22 = 0; var22 < var8; ++var22) {
                for (int var23 = 0; var23 < var7; ++var23) {
                    int var20;
                    if (var14 >= 0 && var14 < var21 && this.firstX + var23 < var2 && (var20 = var13[var15] & 255) != 0) {
                        int var16;
                        for (int var24 = 0; var24 < 3; ++var24) {
                            int var19 = var1[var14] >>> (var24 << 3) & 255;
                            var16 = var19 + (int) ((float) (var17[var24] - var19) / 255.0F * (float) var20);
                            var18[var24] = var16 < 0 ? 0 : (var16 > 255 ? 255 : var16);
                        }

                        var16 = var20 << 24 | var18[2] << 16 | var18[1] << 8 | var18[0];
                        if (!this.isM(var1[var14])) {
                            var1[var14] = this.getAPix(var1[var14], var16);
                        }

                        ++var15;
                        ++var14;
                    } else {
                        ++var15;
                        ++var14;
                    }
                }

                var14 += var2 - var7;
            }
        } catch (InterruptedException var25) {
        } catch (UnsupportedEncodingException var26) {
        } catch (RuntimeException var27) {
            var27.printStackTrace();
        }

    }

    private final int getAPix(int var1, int var2) {
        if (this.i_alpha == 255) {
            return -16777216 | var2;
        } else {
            int var3 = var1 >>> 24;
            int var6 = Math.max(255 - this.i_alpha >>> 3, 1);
            var3 = Math.min(var3 + 1 + (255 - var3) / var6, 255) << 24;

            for (int var7 = 16; var7 >= 0; var7 -= 8) {
                int var4 = var1 >>> var7 & 255;
                int var5 = var2 >>> var7 & 255;
                var3 |= (var4 == var5 ? var5 : (var4 > var5 ? ((var6 = var4 - this.i_alpha) < var5 ? var5 : var6) : ((var6 = var4 + this.i_alpha) > var5 ? var5 : var6))) << var7;
            }

            return var3;
        }
    }

    public void getData(OutputStream var1) {
        try {
            if (this.mask == 110) {
                this.i_mask = 0;
            }

            int var3 = this.layer_end << 4 | this.layer;
            var1.write(107);
            var1.write(this.head);
            int var2 = 0;
            if (this.mask != 110) {
                var2 |= 1;
            }

            if (this.line_size > 1) {
                var2 |= 2;
            }

            if (this.i_color != 0) {
                var2 |= 4;
            }

            if (this.i_mask != 0) {
                var2 |= 8;
            }

            if (this.i_alpha != 255) {
                var2 |= 16;
            }

            if (var3 != 0) {
                var2 |= 32;
            }

            var1.write(var2);
            boolean var6 = false;
            if (this.mask != 110) {
                var1.write(this.mask);
            }

            if (this.line_size > 1) {
                var1.write(this.line_size);
            }

            if (this.i_color != 0) {
                this.w(24, this.i_color, var1);
            }

            if (this.i_mask != 0) {
                this.w(24, this.i_mask, var1);
            }

            if (this.i_alpha != 255) {
                var1.write(this.i_alpha);
            }

            if (var3 != 0) {
                var1.write(var3);
            }

            switch (this.head) {
                case 0:
                case 1:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 19:
                    this.w(16, this.firstX, var1);
                    this.w(16, this.firstY, var1);
                    this.w(16, this.offset.length, var1);
                    var1.write(this.offset);
                    break;
                case 2:
                    this.w(16, this.firstX, var1);
                    this.w(16, this.firstY, var1);
                case 100:
                case 102:
                case 103:
                    if (this.offset == null) {
                        this.w(32, 0, var1);
                    } else {
                        this.w(32, this.offset.length, var1);
                        if (this.offset.length > 0) {
                            var1.write(this.offset);
                        }
                    }
                    break;
                case 40:
                    this.w(16, this.firstX, var1);
                    this.w(16, this.firstY, var1);
                default:
                    if (this.rect == null) {
                        this.rect = new Rectangle();
                    }

                    this.w(16, this.rect.x, var1);
                    this.w(16, this.rect.y, var1);
                    this.w(16, this.rect.width, var1);
                    this.w(16, this.rect.height, var1);
            }
        } catch (IOException var4) {
            var4.printStackTrace();
        } catch (RuntimeException var5) {
            var5.printStackTrace();
        }

    }

    public byte[] getOffset() {
        return this.offset;
    }

    private final int getWPix(int var1) {
        if (this.i_alpha == 255) {
            return 16777215;
        } else {
            int var2 = var1 >> 24 & 255;
            var2 = Math.max(var2 - ((var2 / (255 - this.i_alpha) << 1) + 1), 0);
            int var3 = var2 << 24;
            var2 = (255 - var2) / 255;

            for (int var4 = 16; var4 >= 0; var4 -= 8) {
                var3 |= Math.min((var1 >> var4 & 255) + var2, 255) << var4;
            }

            return var3;
        }
    }

    private final boolean isAm(int var1, int var2) {
        if (this.mask != 113 && this.mask != 114) {
            return false;
        } else {
            for (int var5 = 0; var5 < 24; var5 += 8) {
                int var4 = var1 >>> var5 & 255;
                int var3 = var2 >>> var5 & 255;
                if (this.mask == 113 && var4 > var3) {
                    return true;
                }

                if (this.mask == 114 && var4 < var3) {
                    return true;
                }
            }

            return false;
        }
    }

    private final boolean isArc(int var1) {
        return b_arc[this.line_size][var1] == 0;
    }

    private final boolean isM(int var1) {
        var1 &= 16777215;
        return this.mask == 111 ? var1 == this.i_mask : (this.mask == 112 ? var1 != this.i_mask : false);
    }

    public static boolean isTone(int var0, int var1, int var2) {
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

    private final int k(int var1) {
        int var2 = 1;

        for (int var3 = var1; var3 > 1; --var3) {
            var2 *= var3;
        }

        return var2;
    }

    private final int kei(int var1, int var2) {
        int var3 = this.k(var1) / (this.k(var2) * this.k(var1 - var2));
        return var3;
    }

    public short[] line(Point var1, Point var2) {
        short[] var3 = (short[]) null;

        try {
            int var5 = var1.x;
            int var6 = var1.y;
            int var7 = var2.x - var5;
            int var8 = var2.y - var6;
            int var9 = var7 < 0 ? -var7 : var7;
            int var10 = var8 < 0 ? -var8 : var8;
            int var4;
            float var11;
            float var12;
            if (var9 >= var10) {
                var4 = var9;
                var11 = (float) (var7 < 0 ? -1 : 1);
                if (var8 != 0) {
                    var12 = (float) var8 / (float) var9;
                } else {
                    var12 = 0.0F;
                }
            } else {
                var4 = var10;
                var12 = (float) (var8 < 0 ? -1 : 1);
                if (var7 != 0) {
                    var11 = (float) var7 / (float) var10;
                } else {
                    var11 = 0.0F;
                }
            }

            var3 = new short[var4 * 2];
            int var13 = 0;

            for (int var14 = 0; var14 < var4; ++var14) {
                var3[var13++] = (short) (var5 + Math.round(var11 * (float) var14));
                var3[var13++] = (short) (var6 + Math.round(var12 * (float) var14));
            }

            return var3;
        } catch (RuntimeException var15) {
            var15.printStackTrace();
            return null;
        }
    }

    public final Image makeLPic(int[] var1, int[][] var2, int var3, int var4, int var5, int var6, int var7, int var8) {
        int var10 = 0;
        int var15 = var6 + var8;
        boolean var18 = var1 == null;

        try {
            if (var1 == null) {
                var1 = b_d[this.userNum];
            }

            while (var6 < var15) {
                int var9 = var3 * var6 + var5;

                for (int var14 = var9 + var7; var9 < var14; ++var9) {
                    int var11 = var2[0][var9];
                    int var12 = var2[1][var9];
                    float var16 = b255[var12 >>> 24 & this.visit1];
                    float var17 = b255[var11 >>> 24 & this.visit0] * (1.0F - var16);
                    int var13 = (int) ((1.0F - var17 - var16) * 255.0F);
                    var1[var10++] = (int) ((float) (var12 >>> 16 & 255) * var16) + (int) ((float) (var11 >>> 16 & 255) * var17) + var13 << 16 | (int) ((float) (var12 >>> 8 & 255) * var16) + (int) ((float) (var11 >>> 8 & 255) * var17) + var13 << 8 | (int) ((float) (var12 & 255) * var16) + (int) ((float) (var11 & 255) * var17) + var13;
                }

                ++var6;
            }
        } catch (RuntimeException var20) {
            var20.printStackTrace();
        }

        if (!var18) {
            return null;
        } else {
            raster[this.userNum].newPixels(imBuffer[this.userNum], var7, var8);
            return imBuffer[this.userNum];
        }
    }

    private void paste(int[] var1, int var2, int var3) {
        try {
            int var4 = this.rect.x;
            int var5 = this.rect.y;
            int[] var6 = b_d[this.userNum];
            byte var7 = 0;
            if (var4 < 0 || var4 >= var2 || var5 < 0 || var5 >= var3) {
                return;
            }

            int var14 = var1[var5 * var2 + var4];
            int var15 = this.i_alpha << 24 | this.i_color & 16777215;
            if (var14 == var15) {
                return;
            }

            int var10;
            for (var10 = var2 * var5 + 1; var4 < var2 - 1 && var1[var10 + var4] == var14; ++var4) {
            }

            int var17 = var7 + 1;
            var6[var7] = var4 << 16 | var5;

            while (var17 > 0) {
                --var17;
                int var8 = var6[var17];
                var4 = var8 >>> 16;
                var5 = var8 & '\uffff';
                var10 = var2 * var5;
                boolean var12 = false;
                boolean var13 = false;

                while (true) {
                    var1[var10 + var4] = var15;
                    int var9;
                    int var11;
                    if (var5 > 0 && var1[var10 - var2 + var4] == var14) {
                        if (!var12) {
                            var12 = true;
                            var9 = var4;

                            for (var11 = var2 * (var5 - 1); var9 < var2 - 1 && var1[var11 + var9 + 1] == var14; ++var9) {
                            }

                            var6[var17++] = var9 << 16 | var5 - 1 & '\uffff';
                        }
                    } else {
                        var12 = false;
                    }

                    if (var5 < var3 - 1 && var1[var10 + var2 + var4] == var14) {
                        if (!var13) {
                            var13 = true;
                            var9 = var4;

                            for (var11 = var2 * (var5 + 1); var9 < var2 - 1 && var1[var11 + var9 + 1] == var14; ++var9) {
                            }

                            var6[var17++] = var9 << 16 | var5 + 1 & '\uffff';
                        }
                    } else {
                        var13 = false;
                    }

                    if (var4 <= 0 || var1[var10 + var4 - 1] != var14) {
                        break;
                    }

                    --var4;
                }
            }
        } catch (RuntimeException var16) {
            System.out.println(var16);
        }

        this.d_x = 0;
        this.d_y = 0;
        this.d_w = var2;
        this.d_h = var3;
    }

    private int r(int var1, InputStream var2) throws IOException {
        int var3 = 0;

        for (int var5 = var1 - 8; var5 >= 0; var5 -= 8) {
            int var4 = var2.read();
            if (var4 == -1) {
                throw new EOFException();
            }

            var3 |= var4 << var5;
        }

        return var3;
    }

    private void replace(int[][] var1, int var2, int var3) throws InterruptedException {
        int var4 = 0;
        int var8 = this.rect.width;
        int var9 = this.rect.height;
        int[] var13 = var1[this.layer];
        if (this.rect.x < var2 && this.rect.y < var3 && this.rect.x >= 0 && this.rect.y >= 0) {
            if (var9 + this.rect.y > var3) {
                var9 = var3 - this.rect.y;
            }

            if (var8 + this.rect.x > var2) {
                var8 = var2 - this.rect.x;
            }

            try {
                synchronized (var13) {
                    int var5;
                    int var7;
                    int var15;
                    int[] var16;
                    int var18;
                    int var19;
                    int var20;
                    int var33;
                    int var34;
                    label175:
                    switch (this.head) {
                        case 41:
                            float var35 = Math.max((float) (255 - this.i_alpha) / 48.0F, 1.0F);
                            if (var8 < 3 || var9 < 3) {
                                break;
                            }

                            var16 = new int[4];
                            int[] var36 = new int[var8 * var9];
                            var18 = var2 * this.rect.y + this.rect.x;
                            int var24 = var8;
                            int var25 = var9;
                            synchronized (var1) {
                                this.makeLPic(var36, var1, var2, var3, this.rect.x, this.rect.y, var8, var9);
                            }

                            int var22;
                            int var26;
                            int var27;
                            for (var26 = 0; var26 < var9; ++var26) {
                                for (var27 = 0; var27 < var8; ++var27) {
                                    var22 = var26 * var8 + var27;
                                    int var21 = var36[var22];
                                    var36[var22] = var13[var2 * (this.rect.y + var26) + this.rect.x + var27] & -16777216 | var21 & 16777215;
                                }
                            }

                            var26 = 0;

                            while (true) {
                                if (var26 >= var25) {
                                    break label175;
                                }

                                var27 = var26 * var8;
                                int var28 = var18 + var2 * var26;

                                for (int var29 = 0; var29 < var24; ++var29) {
                                    for (var4 = 0; var4 < 4; ++var4) {
                                        var19 = var4 << 3;
                                        var20 = 0;
                                        int var23 = var36[var27] >> var19 & 255;
                                        var22 = 0;
                                        if (var26 > 0) {
                                            var22 = (int) ((float) var22 + (float) (var23 - (var36[var27 - var8] >>> var19 & 255)) / 255.0F * (float) (var36[var27 - var8] >>> 24));
                                            ++var20;
                                        }

                                        if (var26 + 1 < var25) {
                                            var22 = (int) ((float) var22 + (float) (var23 - (var36[var27 + var8] >>> var19 & 255)) / 255.0F * (float) (var36[var27 + var8] >>> 24));
                                            ++var20;
                                        }

                                        if (var29 > 0) {
                                            var22 = (int) ((float) var22 + (float) (var23 - (var36[var27 - 1] >>> var19 & 255)) / 255.0F * (float) (var36[var27 - 1] >>> 24));
                                            ++var20;
                                        }

                                        if (var29 + 1 < var24) {
                                            var22 = (int) ((float) var22 + (float) (var23 - (var36[var27 + 1] >>> var19 & 255)) / 255.0F * (float) (var36[var27 + 1] >>> 24));
                                            ++var20;
                                        }

                                        var22 = var23 - (int) ((float) var22 / (float) var20 / var35);
                                        var22 = var22 < 0 ? 0 : (var22 > 255 ? 255 : var22);
                                        var16[var4] = (var4 == 3 ? Math.max(var23, var22) : var22) << var19;
                                    }

                                    if (!this.isM(var13[var28])) {
                                        var13[var28] = var16[3] | var16[2] | var16[1] | var16[0];
                                    }

                                    ++var28;
                                    ++var27;
                                }

                                ++var26;
                            }
                        case 42:
                            int var10 = var8 / 2;
                            int var10000 = var8 * var9;
                            var15 = 0;

                            while (true) {
                                if (var15 >= var9) {
                                    break label175;
                                }

                                var33 = (this.rect.y + var15) * var2 + this.rect.x;
                                var7 = var33 + var8 - 1;

                                for (var34 = 0; var34 < var10; ++var34) {
                                    var5 = var13[var33];
                                    var13[var33] = var13[var7];
                                    var13[var7] = var5;
                                    ++var33;
                                    --var7;
                                }

                                ++var15;
                            }
                        case 43:
                            int var11 = var9 / 2;
                            --var9;
                            var15 = 0;

                            while (true) {
                                if (var15 >= var8) {
                                    break label175;
                                }

                                var33 = this.rect.y * var2 + this.rect.x + var15;
                                var7 = var33 + var2 * var9;

                                for (var34 = 0; var34 < var11; ++var34) {
                                    var5 = var13[var33];
                                    var13[var33] = var13[var7];
                                    var13[var7] = var5;
                                    var33 += var2;
                                    var7 -= var2;
                                }

                                ++var15;
                            }
                        case 44:
                            var15 = this.rect.y * var2 + this.rect.x;
                            int var12 = var8 * var9;

                            for (var16 = new int[var12]; var4 < var9; ++var4) {
                                System.arraycopy(var13, var15 + var2 * var4, var16, var8 * var4, var8);
                            }

                            int var17 = this.component.getBackground().getRGB();

                            for (var4 = 0; var4 < var8; ++var4) {
                                var13[this.rect.x + var4] = var17;
                            }

                            for (var4 = 1; var4 < var9; ++var4) {
                                System.arraycopy(var13, var15, var13, var15 + var4 * var2, var8);
                            }

                            boolean var6 = false;
                            var12 = var2 * var3;

                            for (var18 = 0; var18 < var9; ++var18) {
                                var33 = var8 * var18;
                                var7 = var15 + var9 - var18;

                                for (var19 = 0; var19 < var8; ++var19) {
                                    var20 = var19 + this.rect.x;
                                    if (var20 <= var2 && var20 >= 0 && var7 < var12) {
                                        var13[var7] = var16[var33];
                                    }

                                    var7 += var2;
                                    ++var33;
                                }
                            }

                            var8 = Math.max(var8, var9);
                            var9 = var8;
                    }
                }
            } catch (RuntimeException var32) {
                var32.printStackTrace();
            }

            this.d_x = this.rect.x;
            this.d_y = this.rect.y;
            this.d_w = this.rect.x + var8;
            this.d_h = this.rect.y + var9;
        }
    }

    public void setC(Component var1) {
        this.component = var1;
    }

    public void setData(byte[] var1) {
        this.offset = var1;
    }

    public Point setData(short[] var1, int var2) {
        int var3 = -((this.line_size - 1) / 2);

        try {
            if (var2 == 0) {
                var2 = var1.length;
            }

            for (int var4 = 0; var4 < var2; ++var4) {
                var1[var4] = (short) (var1[var4] + var3);
            }

            this.firstX = var1[0];
            this.firstY = var1[1];
            Point var12 = new Point(this.firstX, this.firstY);
            var2 -= 2;
            this.offset = new byte[var2];
            short var5 = var1[0];
            short var6 = var1[1];
            int var7 = 2;

            for (int var10 = 0; var10 < var2; var10 += 2) {
                int var8 = var1[var7] - var5;
                var8 = var8 < -128 ? -128 : (var8 > 127 ? 127 : var8);
                int var9 = var1[var7 + 1] - var6;
                var9 = var9 < -128 ? -128 : (var9 > 127 ? 127 : var9);
                this.offset[var10] = (byte) var8;
                this.offset[var10 + 1] = (byte) var9;
                var12.translate(var8, var9);
                var5 = var1[var7];
                var6 = var1[var7 + 1];
                var7 += 2;
            }

            return new Point(var12.x - var3, var12.y - var3);
        } catch (RuntimeException var11) {
            return new Point(var1[var1.length - 1] - var3, var1[var1.length - 2] - var3);
        }
    }

    public void setData(int var1) {
        this.head = var1;
        this.rect = null;
        this.offset = null;
    }

    public void setData(Point var1, String var2) {
        try {
            if (var1 != null) {
                this.firstX = (short) var1.x;
                this.firstY = (short) var1.y;
            } else {
                this.firstX = 0;
                this.firstY = 0;
            }

            this.offset = var2.getBytes("UTF8");
        } catch (Exception var4) {
            var4.printStackTrace();
            this.offset = null;
        }

    }

    public void setData(Rectangle var1) {
        this.rect = var1 != null ? var1 : new Rectangle();
    }

    public void setData(Rectangle var1, Point var2, int var3, int var4) {
        this.rect = var1 != null ? var1 : new Rectangle();
        if (var2 != null) {
            this.firstX = (short) var2.x;
            this.firstY = (short) var2.y;
        } else {
            this.firstX = 0;
            this.firstY = 0;
        }

        this.layer = var3;
        this.layer_end = var4;
    }

    public void setData(DataInputStream var1) throws EOFException, IOException, InterruptedIOException {
        while (true) {
            try {
                if (var1.readByte() != 107) {
                    continue;
                }

                this.head = var1.readByte();
                byte var3 = var1.readByte();
                this.mask = (var3 & 1) != 0 ? var1.readByte() : 110;
                this.line_size = (var3 & 2) != 0 ? var1.readByte() : 1;
                this.i_color = (var3 & 4) != 0 ? this.r(24, var1) : 0;
                this.i_mask = (var3 & 8) != 0 ? this.r(24, var1) : 0;
                this.i_alpha = (var3 & 16) != 0 ? var1.readUnsignedByte() : 255;
                int var6;
                if ((var3 & 32) != 0) {
                    var6 = var1.readUnsignedByte();
                    this.layer = var6 & 15;
                    this.layer_end = var6 >> 4 & 15;
                } else {
                    this.layer = 0;
                    this.layer_end = 0;
                }

                var6 = 0;
                switch (this.head) {
                    case 0:
                    case 1:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    case 19:
                        this.firstX = var1.readShort();
                        this.firstY = var1.readShort();
                        short var5 = var1.readShort();
                        if (var5 <= 10000) {
                            for (this.offset = new byte[var5]; var6 < var5; var6 += var1.read(this.offset, var6, var5 - var6)) {
                            }

                            return;
                        } else {
                            while (var6 < var5) {
                                var6 = (int) ((long) var6 + var1.skip((long) (var5 - var6)));
                            }

                            this.offset = new byte[2];
                            break;
                        }
                    case 2:
                        this.firstX = var1.readShort();
                        this.firstY = var1.readShort();
                    case 100:
                    case 102:
                    case 103:
                        int var2 = this.r(32, var1);
                        if (var2 < 500000) {
                            if (var2 == 0) {
                                this.offset = new byte[1];
                            } else {
                                for (this.offset = new byte[var2]; var6 < var2; var6 += var1.read(this.offset, var6, var2 - var6)) {
                                }

                                return;
                            }
                        } else {
                            while (var6 < var2) {
                                var6 = (int) ((long) var6 + var1.skip((long) (var2 - var6)));
                            }

                            this.offset = new byte[1];
                        }
                        break;
                    case 40:
                        this.firstX = var1.readShort();
                        this.firstY = var1.readShort();
                    default:
                        this.rect = new Rectangle(var1.readShort(), var1.readShort(), var1.readShort(), var1.readShort());
                }
            } catch (RuntimeException var4) {
                var4.printStackTrace();
            }

            return;
        }
    }

    public void setPreData(short[] var1, int var2, int var3) {
        this.head = 106;
        this.line_size = var3;
        this.setData(var1, var2);
    }

    public static void setup(Component var0, int var1) {
        if (b_arc == null) {
            color_model = new DirectColorModel(24, 16711680, 65280, 255);

            int var2;
            for (var2 = 1; var2 < 256; ++var2) {
                b255[var2] = (float) var2 / 255.0F;
            }

            b_d = new int[var1][16384];
            raster = new SRaster[var1];
            imBuffer = new Image[var1];

            for (var2 = 0; var2 < var1; ++var2) {
                raster[var2] = new SRaster(color_model, b_d[var2], 128, 128);
                imBuffer[var2] = var0.createImage(raster[var2]);
            }

            MgLine var3 = new MgLine();
            var3.rect = new Rectangle();
            var3.i_color = 255;
            var3.i_alpha = 0;
            b_arc = new int[31][];

            for (var2 = 0; var2 < 31; ++var2) {
                b_arc[var2] = new int[var2 * var2];
            }

            for (var2 = 1; var2 < 3; ++var2) {
                for (int var9 = 0; var9 < b_arc[var2].length; ++var9) {
                    b_arc[var2][var9] = 255;
                }
            }

            int var6 = 0;

            int var8;
            int var10;
            for (int[] var12 = new int[]{186, 28662, 4685252}; var2 < 6; ++var2) {
                var8 = var12[var6++];
                boolean var5 = false;

                for (var10 = 0; var10 < b_arc[var2].length; ++var10) {
                    if ((var8 >> var10 & 1) != 0) {
                        b_arc[var2][var10] = 255;
                    }
                }
            }

            while (var2 < 31) {
                int[] var4 = b_arc[var2];
                var8 = var2 / 2 - 1;
                int var7 = (int) (6.283185307179586D * (double) var8);

                for (var10 = 0; var10 < var7 * 4; ++var10) {
                    int var11 = Math.min(var8 + (int) Math.round((double) var8 * Math.cos((double) var10)), var2 - 1);
                    var6 = Math.min(var8 + (int) Math.round((double) var8 * Math.sin((double) var10)), var2 - 1);
                    var4[var6 * var2 + var11] = 255;
                }

                var3.rect.setLocation(var8, var8);
                var3.paste(b_arc[var2], var2, var2);
                ++var2;
            }
        }

    }

    public void setVisit(int var1, int var2) {
        this.visit0 = var1;
        this.visit1 = var2;
    }

    private void w(int var1, int var2, OutputStream var3) throws IOException {
        for (int var4 = var1 - 8; var4 >= 0; var4 -= 8) {
            var3.write(var2 >>> var4 & 255);
        }

    }
}
