package paintchat;

public class LO {
    public int W;
    public int H;
    int offX;
    int offY;
    public int[] offset;
    public String name;
    public int iCopy;
    public float iAlpha;
    public boolean isDraw;
    public static int iL = 0;

    public static LO getLO(int w, int h) {
        return new LO(w, h);
    }

    public LO() {
        this(0, 0);
    }

    public LO(int w, int h) {
        this.offX = 0;
        this.offY = 0;
        this.offset = null;
        this.iCopy = 0;
        this.iAlpha = 1.0F;
        this.isDraw = false;
        this.W = w;
        this.H = h;
    }

    public void setSize(int w, int h) {
        if ((w != this.W || h != this.H) && this.offset != null) {
            int minW = Math.min(w, this.W);
            int minH = Math.min(h, this.H);
            int[] dat = new int[w * h];

            for (int i = 0; i < dat.length; ++i) {
                dat[i] = 0xFFFFFF;
            }

            for (int i = 0; i < minH; ++i) {
                System.arraycopy(this.offset, i * this.W, dat, i * w, minW);
            }

            this.offset = dat;
        }

        this.W = w;
        this.H = h;
    }

    public void reserve() {
        if (this.offset == null) {
            this.offset = new int[this.W * this.H];
            this.clear();
        }

    }

    public final void draw(int[] var1, int var2, int var3, int var4, int var5, int var6) {
        if (this.offset != null && this.iAlpha > 0.0F) {
            float[] var11 = M.getb255();
            float var13 = this.iAlpha;
            int var14 = this.W;
            var2 = Math.max(var2, 0);
            var3 = Math.max(var3, 0);
            var4 = Math.min(this.W, var4);
            var5 = Math.min(this.H, var5);
            int var15 = var4 - var2;
            int var16 = var5 - var3;
            int var17 = 0;
            int var18 = var3 * var14 + var2;

            int var9;
            int var10;
            float var12;
            int var19;
            int var20;
            int var21;
            switch (this.iCopy) {
                case 1:
                    for (int i = 0; i < var16; ++i) {
                        for (int j = 0; j < var15; ++j) {
                            var9 = this.offset[var18 + j];
                            var10 = var1[var17 + j];
                            var12 = var11[var9 >>> 24] * var13;
                            var19 = var10 >>> 16 & 0xFF;
                            var20 = var10 >>> 8 & 0xFF;
                            var21 = var10 & 0xFF;
                            if (var12 > 0.0F) {
                                var1[var17 + j] = ((var10 >>> 16 & 0xFF) - (int) (var11[var10 >>> 16 & 0xFF] * (float) (var9 >>> 16 & 0xFF ^ 0xFF) * var12) << 16) + ((var10 >>> 8 & 0xFF) - (int) (var11[var10 >>> 8 & 0xFF] * (float) (var9 >>> 8 & 0xFF ^ 0xFF) * var12) << 8) + ((var10 & 0xFF) - (int) (var11[var10 & 0xFF] * (float) (var9 & 0xFF ^ 0xFF) * var12));
                            }
                        }

                        var17 += var6;
                        var18 += var14;
                    }

                    return;
                case 2:
                    for (int i = 0; i < var16; ++i) {
                        for (int j = 0; j < var15; ++j) {
                            var9 = this.offset[var18 + j] ^ 0xFFFFFF;
                            var10 = var1[var17 + j];
                            var12 = var11[var9 >>> 24] * var13;
                            if (var12 > 0.0F) {
                                var19 = var10 >>> 16 & 0xFF;
                                var20 = var10 >>> 8 & 0xFF;
                                var21 = var10 & 0xFF;
                                var1[var17 + j] = var12 == 1.0F ? var9 : var19 + (int) ((float) ((var9 >>> 16 & 0xFF) - var19) * var12) << 16 | var20 + (int) ((float) ((var9 >>> 8 & 0xFF) - var20) * var12) << 8 | var21 + (int) ((float) ((var9 & 0xFF) - var21) * var12);
                            }
                        }

                        var17 += var6;
                        var18 += var14;
                    }

                    return;
                default:
                    for (int i = 0; i < var16; ++i) {
                        for (int j = 0; j < var15; ++j) {
                            var9 = this.offset[var18 + j];
                            var10 = var1[var17 + j];
                            var12 = var11[var9 >>> 24] * var13;
                            if (var12 > 0.0F) {
                                var19 = var10 >>> 16 & 0xFF;
                                var20 = var10 >>> 8 & 0xFF;
                                var21 = var10 & 0xFF;
                                var1[var17 + j] = var12 == 1.0F ? var9 : var19 + (int) ((float) ((var9 >>> 16 & 0xFF) - var19) * var12) << 16 | var20 + (int) ((float) ((var9 >>> 8 & 0xFF) - var20) * var12) << 8 | var21 + (int) ((float) ((var9 & 0xFF) - var21) * var12);
                            }
                        }

                        var17 += var6;
                        var18 += var14;
                    }

            }
        }
    }

    public void drawAlpha(int[] var1, int var2, int var3, int var4, int var5, int var6) {
        if (this.offset != null && this.iAlpha > 0.0F) {
            float[] var11 = M.getb255();
            float var12 = this.iAlpha;
            int var13 = this.W;
            var2 = Math.max(var2, 0);
            var3 = Math.max(var3, 0);
            var4 = Math.min(this.W, var4);
            var5 = Math.min(this.H, var5);
            int var14 = var4 - var2;
            int var15 = var5 - var3;
            int var16 = 0;
            int var17 = var3 * var13 + var2;
            int[] var18 = this.offset;

            for (int i = 0; i < var15; ++i) {
                for (int j = 0; j < var14; ++j) {
                    int var9 = (int) ((float) (var18[var17 + j] >>> 24) * var12);
                    int var10 = (int) ((float) (var1[var16 + j] >>> 24) * var11[0xFF - var9]);
                    var1[var16 + j] = var9 + var10 << 24 | var1[var16 + j] & 0xFFFFFF;
                }

                var16 += var6;
                var17 += var13;
            }

        }
    }

    public void dAdd(int[] var1, int var2, int var3, int var4, int var5, int[] var6) {
        if (this.offset != null) {
            float[] var14 = M.getb255();
            int[] var16 = this.offset;
            int var17 = this.W;
            int var18 = var17;
            var2 = Math.max(var2, 0);
            var3 = Math.max(var3, 0);
            var4 = Math.min(this.W, var4);
            var5 = Math.min(this.H, var5);
            int var19 = var4 - var2;
            int var20 = var5 - var3;
            int var21 = var3 * var17 + var2;
            int var22 = var3 * var17 + var2;
            int var23 = 0;
            int var9;
            int var10;
            int var11;
            int var12;
            int var13;
            float var15;
            int var24;
            int var25;
            int var26;
            switch (this.iCopy) {
                case 1:
                    for (int i = 0; i < var20; ++i) {
                        for (int j = 0; j < var19; ++j) {
                            var9 = var16[var22 + j];
                            var10 = var1[var21 + j];
                            var11 = var9 >>> 24;
                            var12 = var10 >>> 24;
                            var13 = var11 + (int) ((float) var12 * var14[0xFF - var11]);
                            int var27 = var6[var23 + j] & 0xFFFFFF;
                            var24 = var10 >>> 16 & 0xFF;
                            var25 = var10 >>> 8 & 0xFF;
                            var26 = var10 & 0xFF;
                            var15 = 1.0F - (float) var12 / (float) var13;
                            var24 += (int) ((float) ((var27 >>> 16 & 0xFF) - var24) * var15);
                            var25 += (int) ((float) ((var27 >>> 8 & 0xFF) - var25) * var15);
                            var26 += (int) ((float) ((var27 & 0xFF) - var26) * var15);
                            var10 = var24 << 16 | var25 << 8 | var26;
                            if (var12 <= 0) {
                                var1[var21 + j] = var9;
                            } else {
                                var15 = var14[var11] + var14[0xFF - var11] * var14[0xFF - var12];
                                var1[var21 + j] = var13 << 24 | (var10 >>> 16 & 0xFF) - (int) (var14[var10 >>> 16 & 0xFF] * (float) (var9 >>> 16 & 0xFF ^ 0xFF) * var15) << 16 | (var10 >>> 8 & 0xFF) - (int) (var14[var10 >>> 8 & 0xFF] * (float) (var9 >>> 8 & 0xFF ^ 0xFF) * var15) << 8 | (var10 & 0xFF) - (int) (var14[var10 & 0xFF] * (float) (var9 & 0xFF ^ 0xFF) * var15);
                            }
                        }

                        var21 += var18;
                        var22 += var17;
                        var23 += var18;
                    }

                    return;
                case 2:
                    for (int i = 0; i < var20; ++i) {
                        for (int j = 0; j < var19; ++j) {
                            var9 = var16[var22 + j];
                            var10 = var1[var21 + j];
                            var11 = var9 >>> 24;
                            var12 = (int) ((float) (var10 >>> 24) * var14[0xFF - var11]);
                            var13 = var11 + var12;
                            var9 ^= 0xFFFFFF;
                            if (var13 == 0) {
                                var1[var21 + j] = 0xFFFFFF;
                            } else {
                                var15 = (float) var11 / (float) var13;
                                var24 = var10 >>> 16 & 0xFF;
                                var25 = var10 >>> 8 & 0xFF;
                                var26 = var10 & 0xFF;
                                var1[var21 + j] = var15 == 1.0F ? var9 : var13 << 24 | var24 + (int) ((float) ((var9 >>> 16 & 0xFF) - var24) * var15) << 16 | var25 + (int) ((float) ((var9 >>> 8 & 0xFF) - var25) * var15) << 8 | var26 + (int) ((float) ((var9 & 0xFF) - var26) * var15);
                            }
                        }

                        var21 += var18;
                        var22 += var17;
                    }

                    return;
                default:
                    for (int i = 0; i < var20; ++i) {
                        for (int j = 0; j < var19; ++j) {
                            var9 = var16[var22 + j];
                            var10 = var1[var21 + j];
                            var11 = var9 >>> 24;
                            var12 = (int) ((float) (var10 >>> 24) * var14[0xFF - var11]);
                            var13 = var11 + var12;
                            if (var13 == 0) {
                                var1[var21 + j] = 0xFFFFFF;
                            } else {
                                var15 = (float) var11 / (float) var13;
                                var24 = var10 >>> 16 & 0xFF;
                                var25 = var10 >>> 8 & 0xFF;
                                var26 = var10 & 0xFF;
                                var1[var21 + j] = var15 == 1.0F ? var9 : var13 << 24 | var24 + (int) ((float) ((var9 >>> 16 & 0xFF) - var24) * var15) << 16 | var25 + (int) ((float) ((var9 >>> 8 & 0xFF) - var25) * var15) << 8 | var26 + (int) ((float) ((var9 & 0xFF) - var26) * var15);
                            }
                        }

                        var21 += var18;
                        var22 += var17;
                    }

            }
        }
    }

    public void normalize(float alpha) {
        this.normalize(alpha, 0, 0, this.W, this.H);
    }

    public void normalize(float alpha, int x1, int y1, int x2, int y2) {
        if (this.offset != null) {
            for (int i = x2 - x1; y1 < y2; ++y1) {
                int var9 = y1 * this.W + x1;

                for (int j = 0; j < i; ++j) {
                    int var7 = this.offset[var9];
                    this.offset[var9] = (int) ((float) (var7 >>> 24) * alpha) << 24 | var7 & 0xFFFFFF;
                    ++var9;
                }
            }

        }
    }

    public final int getPixel(int x, int y) {
        return this.offset != null && x >= 0 && y >= 0 && x < this.W && y < this.H ? this.offset[y * this.W + x] : 0xFFFFFF;
    }

    public final void setPixel(int val, int x, int y) {
        if (x >= 0 && y >= 0 && x < this.W && y < this.H && this.offset != null) {
            this.offset[y * this.W + x] = val;
        }
    }

    public final void clear() {
        this.clear(0, 0, this.W, this.H);
    }

    public void clear(int x1, int y1, int x2, int y2) {
        if (this.offset != null) {
            int var5 = y1 * this.W + x1;
            int var6 = x2 - x1;

            for (int var7 = 0; var7 < var6; ++var7) {
                this.offset[var5 + var7] = 0xFFFFFF;
            }

            var5 += this.W;
            ++y1;

            while (y1 < y2) {
                System.arraycopy(this.offset, var5 - this.W, this.offset, var5, var6);
                var5 += this.W;
                ++y1;
            }

        }
    }

    public void copyTo(int var1, int var2, int var3, int var4, LO var5, int var6, int var7, int[] var8) {
        int[] var9 = var5.offset;
        if (this.offset != null || var9 != null) {
            if (var9 == null) {
                var5.reserve();
                var9 = var5.offset;
            }

            this.copyTo(var1, var2, var3, var4, var9, var6, var7, var5.W, var5.H, var8);
        }
    }

    public final void copyTo(int var1, int var2, int var3, int var4, int[] var5, int var6, int var7, int var8, int var9, int[] var10) {
        var1 = Math.max(var1, 0);
        var2 = Math.max(var2, 0);
        var3 = Math.min(var3, this.W);
        var4 = Math.min(var4, this.H);
        int var11 = Math.min(var3 - var1, var8);
        int var12 = Math.min(var4 - var2, var9);
        if (var6 < 0) {
            var11 += var6;
            var1 -= var6;
            var6 = 0;
        }

        if (var7 < 0) {
            var12 += var7;
            var2 -= var7;
            var7 = 0;
        }

        if (var6 + var11 >= var8) {
            var11 = var8 - var6;
        }

        if (var7 + var12 >= var9) {
            var12 = var9 - var7;
        }

        if (var11 > 0 && var12 > 0) {
            int var13;
            int var14;
            int i;
            if (this.offset == null) {
                for (var14 = var7; var14 < var7 + var12; ++var14) {
                    var13 = var14 * var8 + var6;

                    for (i = 0; i < var11; ++i) {
                        var5[var13++] = 0xFFFFFF;
                    }
                }
            } else {
                var13 = var2 * this.W + var1;
                if (this.offset != var5) {
                    var14 = var7 * var8 + var6;

                    for (i = 0; i < var12; ++i) {
                        System.arraycopy(this.offset, var13, var5, var14, var11);
                        var13 += this.W;
                        var14 += var8;
                    }
                } else {
                    var14 = var11 * var12;
                    if (var10 == null || var10.length < var14) {
                        var10 = new int[var14];
                    }

                    for (i = 0; i < var12; ++i) {
                        System.arraycopy(this.offset, var13, var10, i * var11, var11);
                        var13 += this.W;
                    }

                    var13 = var7 * var8 + var6;

                    for (i = 0; i < var12; ++i) {
                        System.arraycopy(var10, i * var11, var5, var13, var11);
                        var13 += var8;
                    }
                }
            }

        }
    }

    public void setLayer(LO layer) {
        this.setField(layer);
        this.setImage(layer);
    }

    public void setImage(LO layer) {
        int w = layer.W;
        int h = layer.H;
        int wh = w * h;
        if (this.offset != null || layer.offset != null) {
            this.reserve();
            if (layer.offset == null) {
                for (int i = 0; i < wh; ++i) {
                    this.offset[i] = 0xFFFFFF;
                }
            } else {
                System.arraycopy(layer.offset, 0, this.offset, 0, wh);
            }

            this.W = w;
            this.H = h;
        }
    }

    public void setField(LO layer) {
        this.name = layer.name;
        this.iAlpha = layer.iAlpha;
        this.iCopy = layer.iCopy;
        this.offX = layer.offX;
        this.offY = layer.offY;
        this.isDraw = layer.isDraw;
    }

    public void makeName(String str) {
        this.name = str + iL++;
    }

    public void toCopy(int w, int h, int[] src, int x, int y) {
        //copy from
        if (this.offset == null) {
            this.reserve();
        }

        int var6 = 0;
        int var7 = 0;
        int var8 = w;
        if (x < 0) {
            var6 = -x;
            w += x;
            x = 0;
        }

        if (x + w > this.W) {
            w = this.W - x;
        }

        if (y < 0) {
            var7 = -y;
            h += y;
            y = 0;
        }

        if (y + h > this.H) {
            h = this.H - y;
        }

        if (w > 0 && h > 0) {
            int var9 = var7 * var8 + var6;
            int var10 = y * this.W + x;

            for (int i = 0; i < h; ++i) {
                System.arraycopy(src, var9, this.offset, var10, w);
                var9 += w;
                var10 += this.W;
            }

        }
    }

    public void dLR(int x1, int y1, int x2, int y2) {
        //flip horizontal
        if (this.offset != null) {
            for (; y1 < y2; ++y1) {
                int var7 = x2 - 1;

                for (int j = x1; j < var7; ++j) {
                    int var5 = this.getPixel(j, y1);
                    this.setPixel(this.getPixel(var7, y1), j, y1);
                    this.setPixel(var5, var7, y1);
                    --var7;
                    if (j + 1 >= var7) {
                        break;
                    }
                }
            }

        }
    }

    public void dUD(int x1, int y1, int x2, int y2) {
        //flip vertical
        if (this.offset != null) {
            for (; x1 < x2; ++x1) {
                int var7 = y2 - 1;

                for (int j = y1; j < var7; ++j) {
                    int var5 = this.getPixel(x1, j);
                    this.setPixel(this.getPixel(x1, var7), x1, j);
                    this.setPixel(var5, x1, var7);
                    --var7;
                    if (j + 1 >= var7) {
                        break;
                    }
                }
            }

        }
    }

    public void dR(int x1, int y1, int x2, int y2, int[] is) {
        //rotate
        if (this.offset != null) {
            int var6 = this.W;
            int var7 = this.H;
            int var8 = x2 - x1;
            int var9 = y2 - y1;
            int var12 = y1 * var6 + x1;
            int var13 = var8 * var9;
            if (is == null || is.length < var13) {
                is = new int[var13];
            }

            for (int i = 0; i < var9; ++i) {
                System.arraycopy(this.offset, var12 + var6 * i, is, var8 * i, var8);
            }

            for (int i = 0; i < var8; ++i) {
                this.offset[var12 + i] = 0xFFFFFF;
            }

            for (int i = 1; i < var9; ++i) {
                System.arraycopy(this.offset, var12, this.offset, var12 + i * var6, var8);
            }

            var13 = var6 * var7;

            for (int i = 0; i < var9; ++i) {
                int var10 = var8 * i;
                int var11 = var12 + var9 - i;

                for (int j = 0; j < var8; ++j) {
                    int var17 = j + x1;
                    if (var17 <= var6 && var17 >= 0 && var11 < var13) {
                        this.offset[var11] = is[var10];
                    }

                    var11 += var6;
                    ++var10;
                }
            }

        }
    }

    public void swap(LO destLay) {
        LO temp = new LO(this.W, this.H);
        temp.setField(this);
        this.setField(destLay);
        destLay.setField(temp);
        int[] srcDat = this.offset;
        int[] destDat = destLay.offset;
        if (srcDat != null && destDat != null) {
            int var5 = this.W * this.H;

            for (int var7 = 0; var7 < var5; ++var7) {
                int var6 = srcDat[var7];
                srcDat[var7] = destDat[var7];
                destDat[var7] = var6;
            }
        } else {
            this.offset = destDat;
            destLay.offset = srcDat;
        }

    }
}
