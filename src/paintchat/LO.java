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

    public static LO getLO(int var0, int var1) {
        LO var2 = new LO(var0, var1);
        return var2;
    }

    public LO() {
        this(0, 0);
    }

    public LO(int var1, int var2) {
        this.offX = 0;
        this.offY = 0;
        this.offset = null;
        this.iCopy = 0;
        this.iAlpha = 1.0F;
        this.isDraw = false;
        this.W = var1;
        this.H = var2;
    }

    public void setSize(int var1, int var2) {
        if ((var1 != this.W || var2 != this.H) && this.offset != null) {
            int var3 = Math.min(var1, this.W);
            int var4 = Math.min(var2, this.H);
            int[] var5 = new int[var1 * var2];

            int var6;
            for (var6 = 0; var6 < var5.length; ++var6) {
                var5[var6] = 16777215;
            }

            for (var6 = 0; var6 < var4; ++var6) {
                System.arraycopy(this.offset, var6 * this.W, var5, var6 * var1, var3);
            }

            this.offset = var5;
        }

        this.W = var1;
        this.H = var2;
    }

    public void reserve() {
        if (this.offset == null) {
            int var1 = this.W * this.H;
            this.offset = new int[var1];
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
            int var7;
            int var8;
            int var9;
            int var10;
            float var12;
            int var19;
            int var20;
            int var21;
            switch (this.iCopy) {
                case 1:
                    for (var7 = 0; var7 < var16; ++var7) {
                        for (var8 = 0; var8 < var15; ++var8) {
                            var9 = this.offset[var18 + var8];
                            var10 = var1[var17 + var8];
                            var12 = var11[var9 >>> 24] * var13;
                            var19 = var10 >>> 16 & 255;
                            var20 = var10 >>> 8 & 255;
                            var21 = var10 & 255;
                            if (var12 > 0.0F) {
                                var1[var17 + var8] = ((var10 >>> 16 & 255) - (int) (var11[var10 >>> 16 & 255] * (float) (var9 >>> 16 & 255 ^ 255) * var12) << 16) + ((var10 >>> 8 & 255) - (int) (var11[var10 >>> 8 & 255] * (float) (var9 >>> 8 & 255 ^ 255) * var12) << 8) + ((var10 & 255) - (int) (var11[var10 & 255] * (float) (var9 & 255 ^ 255) * var12));
                            }
                        }

                        var17 += var6;
                        var18 += var14;
                    }

                    return;
                case 2:
                    for (var7 = 0; var7 < var16; ++var7) {
                        for (var8 = 0; var8 < var15; ++var8) {
                            var9 = this.offset[var18 + var8] ^ 16777215;
                            var10 = var1[var17 + var8];
                            var12 = var11[var9 >>> 24] * var13;
                            if (var12 > 0.0F) {
                                var19 = var10 >>> 16 & 255;
                                var20 = var10 >>> 8 & 255;
                                var21 = var10 & 255;
                                var1[var17 + var8] = var12 == 1.0F ? var9 : var19 + (int) ((float) ((var9 >>> 16 & 255) - var19) * var12) << 16 | var20 + (int) ((float) ((var9 >>> 8 & 255) - var20) * var12) << 8 | var21 + (int) ((float) ((var9 & 255) - var21) * var12);
                            }
                        }

                        var17 += var6;
                        var18 += var14;
                    }

                    return;
                default:
                    for (var7 = 0; var7 < var16; ++var7) {
                        for (var8 = 0; var8 < var15; ++var8) {
                            var9 = this.offset[var18 + var8];
                            var10 = var1[var17 + var8];
                            var12 = var11[var9 >>> 24] * var13;
                            if (var12 > 0.0F) {
                                var19 = var10 >>> 16 & 255;
                                var20 = var10 >>> 8 & 255;
                                var21 = var10 & 255;
                                var1[var17 + var8] = var12 == 1.0F ? var9 : var19 + (int) ((float) ((var9 >>> 16 & 255) - var19) * var12) << 16 | var20 + (int) ((float) ((var9 >>> 8 & 255) - var20) * var12) << 8 | var21 + (int) ((float) ((var9 & 255) - var21) * var12);
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

            for (int var7 = 0; var7 < var15; ++var7) {
                for (int var8 = 0; var8 < var14; ++var8) {
                    int var9 = (int) ((float) (var18[var17 + var8] >>> 24) * var12);
                    int var10 = (int) ((float) (var1[var16 + var8] >>> 24) * var11[255 - var9]);
                    var1[var16 + var8] = var9 + var10 << 24 | var1[var16 + var8] & 16777215;
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
            int var7;
            int var8;
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
                    for (var7 = 0; var7 < var20; ++var7) {
                        for (var8 = 0; var8 < var19; ++var8) {
                            var9 = var16[var22 + var8];
                            var10 = var1[var21 + var8];
                            var11 = var9 >>> 24;
                            var12 = var10 >>> 24;
                            var13 = var11 + (int) ((float) var12 * var14[255 - var11]);
                            int var27 = var6[var23 + var8] & 16777215;
                            var24 = var10 >>> 16 & 255;
                            var25 = var10 >>> 8 & 255;
                            var26 = var10 & 255;
                            var15 = 1.0F - (float) var12 / (float) var13;
                            var24 += (int) ((float) ((var27 >>> 16 & 255) - var24) * var15);
                            var25 += (int) ((float) ((var27 >>> 8 & 255) - var25) * var15);
                            var26 += (int) ((float) ((var27 & 255) - var26) * var15);
                            var10 = var24 << 16 | var25 << 8 | var26;
                            if (var12 <= 0) {
                                var1[var21 + var8] = var9;
                            } else {
                                var15 = var14[var11] + var14[255 - var11] * var14[255 - var12];
                                var1[var21 + var8] = var13 << 24 | (var10 >>> 16 & 255) - (int) (var14[var10 >>> 16 & 255] * (float) (var9 >>> 16 & 255 ^ 255) * var15) << 16 | (var10 >>> 8 & 255) - (int) (var14[var10 >>> 8 & 255] * (float) (var9 >>> 8 & 255 ^ 255) * var15) << 8 | (var10 & 255) - (int) (var14[var10 & 255] * (float) (var9 & 255 ^ 255) * var15);
                            }
                        }

                        var21 += var18;
                        var22 += var17;
                        var23 += var18;
                    }

                    return;
                case 2:
                    for (var7 = 0; var7 < var20; ++var7) {
                        for (var8 = 0; var8 < var19; ++var8) {
                            var9 = var16[var22 + var8];
                            var10 = var1[var21 + var8];
                            var11 = var9 >>> 24;
                            var12 = (int) ((float) (var10 >>> 24) * var14[255 - var11]);
                            var13 = var11 + var12;
                            var9 ^= 16777215;
                            if (var13 == 0) {
                                var1[var21 + var8] = 16777215;
                            } else {
                                var15 = (float) var11 / (float) var13;
                                var24 = var10 >>> 16 & 255;
                                var25 = var10 >>> 8 & 255;
                                var26 = var10 & 255;
                                var1[var21 + var8] = var15 == 1.0F ? var9 : var13 << 24 | var24 + (int) ((float) ((var9 >>> 16 & 255) - var24) * var15) << 16 | var25 + (int) ((float) ((var9 >>> 8 & 255) - var25) * var15) << 8 | var26 + (int) ((float) ((var9 & 255) - var26) * var15);
                            }
                        }

                        var21 += var18;
                        var22 += var17;
                    }

                    return;
                default:
                    for (var7 = 0; var7 < var20; ++var7) {
                        for (var8 = 0; var8 < var19; ++var8) {
                            var9 = var16[var22 + var8];
                            var10 = var1[var21 + var8];
                            var11 = var9 >>> 24;
                            var12 = (int) ((float) (var10 >>> 24) * var14[255 - var11]);
                            var13 = var11 + var12;
                            if (var13 == 0) {
                                var1[var21 + var8] = 16777215;
                            } else {
                                var15 = (float) var11 / (float) var13;
                                var24 = var10 >>> 16 & 255;
                                var25 = var10 >>> 8 & 255;
                                var26 = var10 & 255;
                                var1[var21 + var8] = var15 == 1.0F ? var9 : var13 << 24 | var24 + (int) ((float) ((var9 >>> 16 & 255) - var24) * var15) << 16 | var25 + (int) ((float) ((var9 >>> 8 & 255) - var25) * var15) << 8 | var26 + (int) ((float) ((var9 & 255) - var26) * var15);
                            }
                        }

                        var21 += var18;
                        var22 += var17;
                    }

            }
        }
    }

    public void normalize(float var1) {
        this.normalize(var1, 0, 0, this.W, this.H);
    }

    public void normalize(float var1, int var2, int var3, int var4, int var5) {
        if (this.offset != null) {
            for (int var6 = var4 - var2; var3 < var5; ++var3) {
                int var9 = var3 * this.W + var2;

                for (int var8 = 0; var8 < var6; ++var8) {
                    int var7 = this.offset[var9];
                    this.offset[var9] = (int) ((float) (var7 >>> 24) * var1) << 24 | var7 & 16777215;
                    ++var9;
                }
            }

        }
    }

    public final int getPixel(int var1, int var2) {
        return this.offset != null && var1 >= 0 && var2 >= 0 && var1 < this.W && var2 < this.H ? this.offset[var2 * this.W + var1] : 16777215;
    }

    public final void setPixel(int var1, int var2, int var3) {
        if (var2 >= 0 && var3 >= 0 && var2 < this.W && var3 < this.H && this.offset != null) {
            this.offset[var3 * this.W + var2] = var1;
        }
    }

    public final void clear() {
        this.clear(0, 0, this.W, this.H);
    }

    public void clear(int var1, int var2, int var3, int var4) {
        if (this.offset != null) {
            int var5 = var2 * this.W + var1;
            int var6 = var3 - var1;

            for (int var7 = 0; var7 < var6; ++var7) {
                this.offset[var5 + var7] = 16777215;
            }

            var5 += this.W;
            ++var2;

            while (var2 < var4) {
                System.arraycopy(this.offset, var5 - this.W, this.offset, var5, var6);
                var5 += this.W;
                ++var2;
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
            int var15;
            if (this.offset == null) {
                for (var14 = var7; var14 < var7 + var12; ++var14) {
                    var13 = var14 * var8 + var6;

                    for (var15 = 0; var15 < var11; ++var15) {
                        var5[var13++] = 16777215;
                    }
                }
            } else {
                var13 = var2 * this.W + var1;
                if (this.offset != var5) {
                    var14 = var7 * var8 + var6;

                    for (var15 = 0; var15 < var12; ++var15) {
                        System.arraycopy(this.offset, var13, var5, var14, var11);
                        var13 += this.W;
                        var14 += var8;
                    }
                } else {
                    var14 = var11 * var12;
                    if (var10 == null || var10.length < var14) {
                        var10 = new int[var14];
                    }

                    for (var15 = 0; var15 < var12; ++var15) {
                        System.arraycopy(this.offset, var13, var10, var15 * var11, var11);
                        var13 += this.W;
                    }

                    var13 = var7 * var8 + var6;

                    for (var15 = 0; var15 < var12; ++var15) {
                        System.arraycopy(var10, var15 * var11, var5, var13, var11);
                        var13 += var8;
                    }
                }
            }

        }
    }

    public void setLayer(LO var1) {
        this.setField(var1);
        this.setImage(var1);
    }

    public void setImage(LO var1) {
        int var2 = var1.W;
        int var3 = var1.H;
        int var4 = var2 * var3;
        if (this.offset != null || var1.offset != null) {
            this.reserve();
            if (var1.offset == null) {
                for (int var5 = 0; var5 < var4; ++var5) {
                    this.offset[var5] = 16777215;
                }
            } else {
                System.arraycopy(var1.offset, 0, this.offset, 0, var4);
            }

            this.W = var2;
            this.H = var3;
        }
    }

    public void setField(LO var1) {
        this.name = var1.name;
        this.iAlpha = var1.iAlpha;
        this.iCopy = var1.iCopy;
        this.offX = var1.offX;
        this.offY = var1.offY;
        this.isDraw = var1.isDraw;
    }

    public void makeName(String var1) {
        this.name = var1 + iL++;
    }

    public void toCopy(int var1, int var2, int[] var3, int var4, int var5) {
        if (this.offset == null) {
            this.reserve();
        }

        int var6 = 0;
        int var7 = 0;
        int var8 = var1;
        if (var4 < 0) {
            var6 = -var4;
            var1 += var4;
            var4 = 0;
        }

        if (var4 + var1 > this.W) {
            var1 = this.W - var4;
        }

        if (var5 < 0) {
            var7 = -var5;
            var2 += var5;
            var5 = 0;
        }

        if (var5 + var2 > this.H) {
            var2 = this.H - var5;
        }

        if (var1 > 0 && var2 > 0) {
            int var9 = var7 * var8 + var6;
            int var10 = var5 * this.W + var4;

            for (int var11 = 0; var11 < var2; ++var11) {
                System.arraycopy(var3, var9, this.offset, var10, var1);
                var9 += var1;
                var10 += this.W;
            }

        }
    }

    public void dLR(int var1, int var2, int var3, int var4) {
        if (this.offset != null) {
            for (; var2 < var4; ++var2) {
                int var7 = var3 - 1;

                for (int var6 = var1; var6 < var7; ++var6) {
                    int var5 = this.getPixel(var6, var2);
                    this.setPixel(this.getPixel(var7, var2), var6, var2);
                    this.setPixel(var5, var7, var2);
                    --var7;
                    if (var6 + 1 >= var7) {
                        break;
                    }
                }
            }

        }
    }

    public void dUD(int var1, int var2, int var3, int var4) {
        if (this.offset != null) {
            for (; var1 < var3; ++var1) {
                int var7 = var4 - 1;

                for (int var6 = var2; var6 < var7; ++var6) {
                    int var5 = this.getPixel(var1, var6);
                    this.setPixel(this.getPixel(var1, var7), var1, var6);
                    this.setPixel(var5, var1, var7);
                    --var7;
                    if (var6 + 1 >= var7) {
                        break;
                    }
                }
            }

        }
    }

    public void dR(int var1, int var2, int var3, int var4, int[] var5) {
        if (this.offset != null) {
            int var6 = this.W;
            int var7 = this.H;
            int var8 = var3 - var1;
            int var9 = var4 - var2;
            int var12 = var2 * var6 + var1;
            int var13 = var8 * var9;
            if (var5 == null || var5.length < var13) {
                var5 = new int[var13];
            }

            int var14;
            for (var14 = 0; var14 < var9; ++var14) {
                System.arraycopy(this.offset, var12 + var6 * var14, var5, var8 * var14, var8);
            }

            for (var14 = 0; var14 < var8; ++var14) {
                this.offset[var12 + var14] = 16777215;
            }

            for (var14 = 1; var14 < var9; ++var14) {
                System.arraycopy(this.offset, var12, this.offset, var12 + var14 * var6, var8);
            }

            var13 = var6 * var7;

            for (int var15 = 0; var15 < var9; ++var15) {
                int var10 = var8 * var15;
                int var11 = var12 + var9 - var15;

                for (int var16 = 0; var16 < var8; ++var16) {
                    int var17 = var16 + var1;
                    if (var17 <= var6 && var17 >= 0 && var11 < var13) {
                        this.offset[var11] = var5[var10];
                    }

                    var11 += var6;
                    ++var10;
                }
            }

        }
    }

    public void swap(LO var1) {
        LO var2 = new LO(this.W, this.H);
        var2.setField(this);
        this.setField(var1);
        var1.setField(var2);
        int[] var3 = this.offset;
        int[] var4 = var1.offset;
        if (var3 != null && var4 != null) {
            int var5 = this.W * this.H;

            for (int var7 = 0; var7 < var5; ++var7) {
                int var6 = var3[var7];
                var3[var7] = var4[var7];
                var4[var7] = var6;
            }
        } else {
            this.offset = var4;
            var1.offset = var3;
        }

    }
}
