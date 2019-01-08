package paintchat;

public class LO {
    public int W;
    public int H;
    int offX; // both offX and offY seem to be always 0
    int offY;
    public int[] offset; // argb data for this layer
    public String name;
    public int iCopy; // blend mode
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
        this.iCopy = M.M_N;
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

    public final void draw(int[] var1, int x1, int y1, int x2, int y2, int var6) {
        if (this.offset != null && this.iAlpha > 0.0F) {
            float[] alphaLUT = M.getb255(); // relative alpha
            float layerAlpha = this.iAlpha;
            int layerWidth = this.W;
            x1 = Math.max(x1, 0);
            y1 = Math.max(y1, 0);
            x2 = Math.min(this.W, x2);
            y2 = Math.min(this.H, y2);
            int width = x2 - x1;
            int height = y2 - y1;
            int var17 = 0;
            int var18 = y1 * layerWidth + x1;

            int var9;
            int var10;
            float alpha;
            int r;
            int g;
            int b;
            switch (this.iCopy) {
                case M.M_M: // Multiply
                    for (int i = 0; i < height; ++i) {
                        for (int j = 0; j < width; ++j) {
                            var9 = this.offset[var18 + j];
                            var10 = var1[var17 + j];
                            alpha = alphaLUT[var9 >>> 24] * layerAlpha;
                            r = var10 >>> 16 & 0xFF;
                            g = var10 >>> 8 & 0xFF;
                            b = var10 & 0xFF;
                            if (alpha > 0.0F) {
                                var1[var17 + j] = (r - (int) (alphaLUT[r] * (float) (var9 >>> 16 & 0xFF ^ 0xFF) * alpha) << 16)
                                        + (g - (int) (alphaLUT[g] * (float) (var9 >>> 8 & 0xFF ^ 0xFF) * alpha) << 8)
                                        + (b - (int) (alphaLUT[b] * (float) (var9 & 0xFF ^ 0xFF) * alpha));
                            }
                        }

                        var17 += var6;
                        var18 += layerWidth;
                    }

                    return;
                case M.M_R: // Invert
                    for (int i = 0; i < height; ++i) {
                        for (int j = 0; j < width; ++j) {
                            var9 = this.offset[var18 + j] ^ 0xFFFFFF;
                            var10 = var1[var17 + j];
                            alpha = alphaLUT[var9 >>> 24] * layerAlpha;
                            if (alpha > 0.0F) {
                                r = var10 >>> 16 & 0xFF;
                                g = var10 >>> 8 & 0xFF;
                                b = var10 & 0xFF;
                                var1[var17 + j] = alpha == 1.0F ? var9 : r + (int) ((float) ((var9 >>> 16 & 0xFF) - r) * alpha) << 16 | g + (int) ((float) ((var9 >>> 8 & 0xFF) - g) * alpha) << 8 | b + (int) ((float) ((var9 & 0xFF) - b) * alpha);
                            }
                        }

                        var17 += var6;
                        var18 += layerWidth;
                    }

                    return;
                default: // Normal (M_N)
                    for (int i = 0; i < height; ++i) {
                        for (int j = 0; j < width; ++j) {
                            var9 = this.offset[var18 + j];
                            var10 = var1[var17 + j];
                            alpha = alphaLUT[var9 >>> 24] * layerAlpha;
                            if (alpha > 0.0F) {
                                r = var10 >>> 16 & 0xFF;
                                g = var10 >>> 8 & 0xFF;
                                b = var10 & 0xFF;
                                var1[var17 + j] = alpha == 1.0F ? var9 : r + (int) ((float) ((var9 >>> 16 & 0xFF) - r) * alpha) << 16 | g + (int) ((float) ((var9 >>> 8 & 0xFF) - g) * alpha) << 8 | b + (int) ((float) ((var9 & 0xFF) - b) * alpha);
                            }
                        }

                        var17 += var6;
                        var18 += layerWidth;
                    }

            }
        }
    }

    public void drawAlpha(int[] var1, int x1, int y1, int x2, int y2, int var6) {
        if (this.offset != null && this.iAlpha > 0.0F) {
            float[] var11 = M.getb255();
            float var12 = this.iAlpha;
            int var13 = this.W;
            x1 = Math.max(x1, 0);
            y1 = Math.max(y1, 0);
            x2 = Math.min(this.W, x2);
            y2 = Math.min(this.H, y2);
            int var14 = x2 - x1;
            int var15 = y2 - y1;
            int var16 = 0;
            int var17 = y1 * var13 + x1;
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

    public void dAdd(int[] var1, int x1, int y1, int x2, int y2, int[] var6) {
        if (this.offset != null) {
            float[] var14 = M.getb255();
            int[] var16 = this.offset;
            int var17 = this.W;
            int var18 = var17;
            x1 = Math.max(x1, 0);
            y1 = Math.max(y1, 0);
            x2 = Math.min(this.W, x2);
            y2 = Math.min(this.H, y2);
            int var19 = x2 - x1;
            int var20 = y2 - y1;
            int var21 = y1 * var17 + x1;
            int var22 = y1 * var17 + x1;
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

    public void copyTo(int x1, int y1, int x2, int y2, LO layer, int var6, int var7, int[] var8) {
        int[] var9 = layer.offset;
        if (this.offset != null || var9 != null) {
            if (var9 == null) {
                layer.reserve();
                var9 = layer.offset;
            }

            this.copyTo(x1, y1, x2, y2, var9, var6, var7, layer.W, layer.H, var8);
        }
    }

    public final void copyTo(int x1, int y1, int x2, int y2, int[] var5, int var6, int var7, int layerW, int layerH, int[] var10) {
        x1 = Math.max(x1, 0);
        y1 = Math.max(y1, 0);
        x2 = Math.min(x2, this.W);
        y2 = Math.min(y2, this.H);
        int var11 = Math.min(x2 - x1, layerW);
        int var12 = Math.min(y2 - y1, layerH);
        if (var6 < 0) {
            var11 += var6;
            x1 -= var6;
            var6 = 0;
        }

        if (var7 < 0) {
            var12 += var7;
            y1 -= var7;
            var7 = 0;
        }

        if (var6 + var11 >= layerW) {
            var11 = layerW - var6;
        }

        if (var7 + var12 >= layerH) {
            var12 = layerH - var7;
        }

        if (var11 > 0 && var12 > 0) {
            int var13;
            int var14;
            int i;
            if (this.offset == null) {
                for (var14 = var7; var14 < var7 + var12; ++var14) {
                    var13 = var14 * layerW + var6;

                    for (i = 0; i < var11; ++i) {
                        var5[var13++] = 0xFFFFFF;
                    }
                }
            } else {
                var13 = y1 * this.W + x1;
                if (this.offset != var5) {
                    var14 = var7 * layerW + var6;

                    for (i = 0; i < var12; ++i) {
                        System.arraycopy(this.offset, var13, var5, var14, var11);
                        var13 += this.W;
                        var14 += layerW;
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

                    var13 = var7 * layerW + var6;

                    for (i = 0; i < var12; ++i) {
                        System.arraycopy(var10, i * var11, var5, var13, var11);
                        var13 += layerW;
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

    /** Copies field values from another layer */
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
            int size = this.W * this.H;

            for (int i = 0; i < size; ++i) {
                int newDestVal = srcDat[i];
                srcDat[i] = destDat[i];
                destDat[i] = newDestVal;
            }
        } else {
            this.offset = destDat;
            destLay.offset = srcDat;
        }

    }
}
