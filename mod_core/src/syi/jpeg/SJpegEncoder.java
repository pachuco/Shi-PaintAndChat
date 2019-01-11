package syi.jpeg;

import java.io.IOException;
import java.io.OutputStream;

public class SJpegEncoder {
    OutputStream OUT;
    private int[] i_off;
    private int width;
    private int height;
    private int HV;
    private double[][] mCosT;
    private int[] mOldDC;
    private double kSqrt2 = 1.41421356D;
    private double kDisSqrt2;
    private double kPaiDiv16;
    private int bitSeek = 7;
    private int bitValue = 0;
    private int[] kYQuantumT = new int[64];
    private int[] kCQuantumT = new int[64];
    private final byte[] kYDcSizeT = new byte[]{2, 3, 3, 3, 3, 3, 4, 5, 6, 7, 8, 9};
    private final short[] kYDcCodeT = new short[]{0, 2, 3, 4, 5, 6, 14, 30, 62, 126, 254, 510};
    private final byte[] kCDcSizeT = new byte[]{2, 2, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    private final short[] kCDcCodeT = new short[]{0, 1, 2, 6, 14, 30, 62, 126, 254, 510, 1022, 2046};
    private final byte[] kYAcSizeT = new byte[]{4, 2, 2, 3, 4, 5, 7, 8, 10, 16, 16, 4, 5, 7, 9, 11, 16, 16, 16, 16, 16, 5, 8, 10, 12, 16, 16, 16, 16, 16, 16, 6, 9, 12, 16, 16, 16, 16, 16, 16, 16, 6, 10, 16, 16, 16, 16, 16, 16, 16, 16, 7, 11, 16, 16, 16, 16, 16, 16, 16, 16, 7, 12, 16, 16, 16, 16, 16, 16, 16, 16, 8, 12, 16, 16, 16, 16, 16, 16, 16, 16, 9, 15, 16, 16, 16, 16, 16, 16, 16, 16, 9, 16, 16, 16, 16, 16, 16, 16, 16, 16, 9, 16, 16, 16, 16, 16, 16, 16, 16, 16, 10, 16, 16, 16, 16, 16, 16, 16, 16, 16, 10, 16, 16, 16, 16, 16, 16, 16, 16, 16, 11, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 11, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16};
    private final short[] kYAcCodeT = new short[]{10, 0, 1, 4, 11, 26, 120, 248, 1014, -126, -125, 12, 27, 121, 502, 2038, -124, -123, -122, -121, -120, 28, 249, 1015, 4084, -119, -118, -117, -116, -115, -114, 58, 503, 4085, -113, -112, -111, -110, -109, -108, -107, 59, 1016, -106, -105, -104, -103, -102, -101, -100, -99, 122, 2039, -98, -97, -96, -95, -94, -93, -92, -91, 123, 4086, -90, -89, -88, -87, -86, -85, -84, -83, 250, 4087, -82, -81, -80, -79, -78, -77, -76, -75, 504, 32704, -74, -73, -72, -71, -70, -69, -68, -67, 505, -66, -65, -64, -63, -62, -61, -60, -59, -58, 506, -57, -56, -55, -54, -53, -52, -51, -50, -49, 1017, -48, -47, -46, -45, -44, -43, -42, -41, -40, 1018, -39, -38, -37, -36, -35, -34, -33, -32, -31, 2040, -30, -29, -28, -27, -26, -25, -24, -23, -22, -21, -20, -19, -18, -17, -16, -15, -14, -13, -12, 2041, -11, -10, -9, -8, -7, -6, -5, -4, -3, -2};
    private int kYEOBidx = 0;
    private int kYZRLidx = 151;
    private final byte[] kCAcSizeT = new byte[]{2, 2, 3, 4, 5, 5, 6, 7, 9, 10, 12, 4, 6, 8, 9, 11, 12, 16, 16, 16, 16, 5, 8, 10, 12, 15, 16, 16, 16, 16, 16, 5, 8, 10, 12, 16, 16, 16, 16, 16, 16, 6, 9, 16, 16, 16, 16, 16, 16, 16, 16, 6, 10, 16, 16, 16, 16, 16, 16, 16, 16, 7, 11, 16, 16, 16, 16, 16, 16, 16, 16, 7, 11, 16, 16, 16, 16, 16, 16, 16, 16, 8, 16, 16, 16, 16, 16, 16, 16, 16, 16, 9, 16, 16, 16, 16, 16, 16, 16, 16, 16, 9, 16, 16, 16, 16, 16, 16, 16, 16, 16, 9, 16, 16, 16, 16, 16, 16, 16, 16, 16, 9, 16, 16, 16, 16, 16, 16, 16, 16, 16, 11, 16, 16, 16, 16, 16, 16, 16, 16, 16, 14, 16, 16, 16, 16, 16, 16, 16, 16, 16, 10, 15, 16, 16, 16, 16, 16, 16, 16, 16, 16};
    private final short[] kCAcCodeT = new short[]{0, 1, 4, 10, 24, 25, 56, 120, 500, 1014, 4084, 11, 57, 246, 501, 2038, 4085, -120, -119, -118, -117, 26, 247, 1015, 4086, 32706, -116, -115, -114, -113, -112, 27, 248, 1016, 4087, -111, -110, -109, -108, -107, -106, 58, 502, -105, -104, -103, -102, -101, -100, -99, -98, 59, 1017, -97, -96, -95, -94, -93, -92, -91, -90, 121, 2039, -89, -88, -87, -86, -85, -84, -83, -82, 122, 2040, -81, -80, -79, -78, -77, -76, -75, -74, 249, -73, -72, -71, -70, -69, -68, -67, -66, -65, 503, -64, -63, -62, -61, -60, -59, -58, -57, -56, 504, -55, -54, -53, -52, -51, -50, -49, -48, -47, 505, -46, -45, -44, -43, -42, -41, -40, -39, -38, 506, -37, -36, -35, -34, -33, -32, -31, -30, -29, 2041, -28, -27, -26, -25, -24, -23, -22, -21, -20, 16352, -19, -18, -17, -16, -15, -14, -13, -12, -11, 1018, 32707, -10, -9, -8, -7, -6, -5, -4, -3, -2};
    private int kCEOBidx = 0;
    private int kCZRLidx = 151;
    private final byte[] kZigzag = new byte[]{0, 1, 8, 16, 9, 2, 3, 10, 17, 24, 32, 25, 18, 11, 4, 5, 12, 19, 26, 33, 40, 48, 41, 34, 27, 20, 13, 6, 7, 14, 21, 28, 35, 42, 49, 56, 57, 50, 43, 36, 29, 22, 15, 23, 30, 37, 44, 51, 58, 59, 52, 45, 38, 31, 39, 46, 53, 60, 61, 54, 47, 55, 62, 63};

    public SJpegEncoder() {
        this.kDisSqrt2 = 1.0D / this.kSqrt2;
        this.kPaiDiv16 = 0.19634954084936207D;
        this.mCosT = new double[8][8];

        for (int var1 = 0; var1 < 8; ++var1) {
            for (int var2 = 0; var2 < 8; ++var2) {
                this.mCosT[var1][var2] = Math.cos((double) ((2 * var2 + 1) * var1) * this.kPaiDiv16);
            }
        }

    }

    public void encode(OutputStream var1, int[] var2, int var3, int var4, int var5) {
        try {
            char var6 = '\uffd8';
            char var7 = '\uffd9';
            this.OUT = var1;
            this.mOldDC = new int[3];
            this.q(var5);
            this.i_off = var2;
            this.width = var3;
            this.height = var4;
            this.wShort(var6);
            this.mAPP0();
            this.mComment();
            this.mDQT();
            this.mDHT();
            this.mSOF();
            this.mSOS();
            this.mMCU();
            this.wShort(var7);
            this.OUT.flush();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

    }

    private void getYCC(int[] var1, int[] var2, int var3, int var4, int var5, int var6, int var7) {
        int var10 = 0;
        int var11 = 0;
        int var12 = var5 * 8;
        int var13 = var6 * 8;
        int var14 = var3 + var12;
        int var15 = var4 + var13;
        int[] var16 = new int[3];

        int var8;
        int var17;
        label102:
        for (var17 = var4; var17 < var15; ++var17) {
            int var18;
            if (var17 >= this.height) {
                try {
                    var18 = var10;

                    while (true) {
                        if (var18 >= var2.length) {
                            break label102;
                        }

                        var2[var18] = var2[var18 - var12];
                        ++var18;
                    }
                } catch (RuntimeException var24) {
                    break;
                }
            }

            int var9 = var17 * this.width + var3;

            for (var18 = var3; var18 < var14; ++var18) {
                var8 = var18 >= this.width ? var11 : this.i_off[var9];
                var11 = var8;
                var2[var10++] = var8;
                ++var9;
            }
        }

        int[] var25 = new int[3];
        var10 = 0;

        int var19;
        for (var19 = 0; var19 < var13; var19 += var6) {
            for (int var20 = 0; var20 < var12; var20 += var5) {
                var17 = 0;
                var25[0] = 0;
                var25[1] = 0;
                var25[2] = 0;

                int var21;
                for (var21 = 0; var21 < var6; ++var21) {
                    for (int var22 = 0; var22 < var5; ++var22) {
                        var8 = var2[(var19 + var21) * var12 + var20 + var22];

                        for (int var23 = 0; var23 < 3; ++var23) {
                            var25[var23] += var8 >> var23 * 8 & 255;
                        }

                        ++var17;
                    }
                }

                for (var21 = 0; var21 < 3; ++var21) {
                    var25[var21] = Math.min(Math.max(var25[var21] / var17, 0), 255);
                }

                var1[var10++] = var25[2] << 16 | var25[1] << 8 | var25[0];
            }
        }

        for (var19 = 0; var19 < 64; ++var19) {
            this.ycc(var16, var1[var19]);
            var1[var19] = var16[var7];
        }

    }

    private void mAPP0() throws IOException {
        char var1 = '￠';
        byte[] var2 = "JFIF\u0000".getBytes();
        this.wShort(var1);
        this.wShort(var2.length + 11);
        this.wArray(var2);
        this.w(1);
        this.w(2);
        this.w(1);
        this.wShort(72);
        this.wShort(72);
        this.w(0);
        this.w(0);
    }

    private final void mComment() throws IOException {
        char var1 = '\ufffe';
        String var2 = "(C)shi-chan 2001";
        byte[] var3 = (var2 + '\u0000').getBytes();
        this.wShort(var1);
        this.wShort(var3.length + 2);
        this.wArray(var3);
        System.out.println(var2);
    }

    private void mDHT() throws IOException {
        char var1 = 'ￄ';
        byte[] var2 = new byte[]{0, 0, 1, 5, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
        byte[] var3 = new byte[]{1, 0, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
        byte[] var4 = new byte[]{16, 0, 2, 1, 3, 3, 2, 4, 3, 5, 5, 4, 4, 0, 0, 1, 125, 1, 2, 3, 0, 4, 17, 5, 18, 33, 49, 65, 6, 19, 81, 97, 7, 34, 113, 20, 50, -127, -111, -95, 8, 35, 66, -79, -63, 21, 82, -47, -16, 36, 51, 98, 114, -126, 9, 10, 22, 23, 24, 25, 26, 37, 38, 39, 40, 41, 42, 52, 53, 54, 55, 56, 57, 58, 67, 68, 69, 70, 71, 72, 73, 74, 83, 84, 85, 86, 87, 88, 89, 90, 99, 100, 101, 102, 103, 104, 105, 106, 115, 116, 117, 118, 119, 120, 121, 122, -125, -124, -123, -122, -121, -120, -119, -118, -110, -109, -108, -107, -106, -105, -104, -103, -102, -94, -93, -92, -91, -90, -89, -88, -87, -86, -78, -77, -76, -75, -74, -73, -72, -71, -70, -62, -61, -60, -59, -58, -57, -56, -55, -54, -46, -45, -44, -43, -42, -41, -40, -39, -38, -31, -30, -29, -28, -27, -26, -25, -24, -23, -22, -15, -14, -13, -12, -11, -10, -9, -8, -7, -6};
        byte[] var5 = new byte[]{17, 0, 2, 1, 2, 4, 4, 3, 4, 7, 5, 4, 4, 0, 1, 2, 119, 0, 1, 2, 3, 17, 4, 5, 33, 49, 6, 18, 65, 81, 7, 97, 113, 19, 34, 50, -127, 8, 20, 66, -111, -95, -79, -63, 9, 35, 51, 82, -16, 21, 98, 114, -47, 10, 22, 36, 52, -31, 37, -15, 23, 24, 25, 26, 38, 39, 40, 41, 42, 53, 54, 55, 56, 57, 58, 67, 68, 69, 70, 71, 72, 73, 74, 83, 84, 85, 86, 87, 88, 89, 90, 99, 100, 101, 102, 103, 104, 105, 106, 115, 116, 117, 118, 119, 120, 121, 122, -126, -125, -124, -123, -122, -121, -120, -119, -118, -110, -109, -108, -107, -106, -105, -104, -103, -102, -94, -93, -92, -91, -90, -89, -88, -87, -86, -78, -77, -76, -75, -74, -73, -72, -71, -70, -62, -61, -60, -59, -58, -57, -56, -55, -54, -46, -45, -44, -43, -42, -41, -40, -39, -38, -30, -29, -28, -27, -26, -25, -24, -23, -22, -14, -13, -12, -11, -10, -9, -8, -7, -6};
        this.wShort(var1);
        this.wShort(var2.length + var4.length + var3.length + var5.length + 2);
        this.wArray(var2);
        this.wArray(var4);
        this.wArray(var3);
        this.wArray(var5);
    }

    private void mDQT() throws IOException {
        char var1 = 'ￛ';
        this.wShort(var1);
        this.wShort(132);
        this.w(0);

        int var2;
        for (var2 = 0; var2 < 64; ++var2) {
            this.w(this.kYQuantumT[this.kZigzag[var2]] & 255);
        }

        this.w(1);

        for (var2 = 0; var2 < 64; ++var2) {
            this.w(this.kCQuantumT[this.kZigzag[var2]] & 255);
        }

    }

    private void mMCU() throws IOException {
        try {
            int var1 = this.HV;
            int var2 = 8 * var1;
            int[] var3 = new int[64];
            int[] var4 = new int[64];
            int[] var5 = new int[var2 * var2];

            for (int var6 = 0; var6 < this.height; var6 += var2) {
                for (int var7 = 0; var7 < this.width; var7 += var2) {
                    for (int var8 = 0; var8 < var1; ++var8) {
                        for (int var9 = 0; var9 < var1; ++var9) {
                            this.getYCC(var3, var5, var7 + 8 * var9, var6 + 8 * var8, 1, 1, 0);
                            this.tDCT(var3, var4);
                            this.tQuantization(var4, 0);
                            this.tHuffman(var4, 0);
                        }
                    }

                    this.getYCC(var3, var5, var7, var6, this.HV, this.HV, 1);
                    this.tDCT(var3, var4);
                    this.tQuantization(var4, 1);
                    this.tHuffman(var4, 1);
                    this.getYCC(var3, var5, var7, var6, this.HV, this.HV, 2);
                    this.tDCT(var3, var4);
                    this.tQuantization(var4, 2);
                    this.tHuffman(var4, 2);
                }
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

    }

    private void mSOF() throws IOException {
        char var1 = '\uffc0';
        this.wShort(var1);
        this.wShort(17);
        this.w(8);
        this.wShort(this.height);
        this.wShort(this.width);
        this.w(3);

        for (int var3 = 0; var3 < 3; ++var3) {
            this.w(var3);
            int var2 = var3 == 0 ? this.HV : 1;
            this.w(var2 << 4 | var2);
            this.w(var3 == 0 ? 0 : 1);
        }

    }

    private void mSOS() throws IOException {
        char var1 = 'ￚ';
        this.wShort(var1);
        this.wShort(12);
        this.w(3);

        for (int var2 = 0; var2 < 3; ++var2) {
            this.w(var2);
            this.w(var2 == 0 ? 0 : 17);
        }

        this.w(0);
        this.w(63);
        this.w(0);
    }

    private void q(int var1) {
        byte[] var2 = new byte[]{16, 11, 10, 16, 24, 40, 51, 61, 12, 12, 14, 19, 26, 58, 60, 55, 14, 13, 16, 24, 40, 57, 69, 56, 14, 17, 22, 29, 51, 87, 80, 62, 18, 22, 37, 56, 68, 109, 103, 77, 24, 35, 55, 64, 81, 104, 113, 92, 49, 64, 78, 87, 103, 121, 120, 101, 72, 92, 95, 98, 112, 100, 103, 99};
        byte[] var3 = new byte[]{17, 18, 24, 47, 99, 99, 99, 99, 18, 21, 26, 66, 99, 99, 99, 99, 24, 26, 56, 99, 99, 99, 99, 99, 47, 66, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99};
        var1 = var1 < 1 ? 1 : (var1 > 100 ? 100 : var1);
        this.HV = 1;
        if (var1 >= 25) {
            this.HV = 2;
        }

        float var4 = (float) var1 / 50.0F;
        float var5 = (float) var1 / 50.0F;

        for (int var6 = 0; var6 < 64; ++var6) {
            this.kYQuantumT[var6] = Math.min(Math.max((int) ((float) var2[var6] * var4), 1), 127);
            this.kCQuantumT[var6] = Math.min(Math.max((int) ((float) var3[var6] * var5), 1), 127);
        }

    }

    private void tDCT(int[] var1, int[] var2) {
        try {
            int var3 = 0;
            boolean var4 = false;

            for (int var5 = 0; var5 < 8; ++var5) {
                double var6 = var5 > 0 ? 1.0D : this.kDisSqrt2;

                for (int var8 = 0; var8 < 8; ++var8) {
                    double var9 = var8 > 0 ? 1.0D : this.kDisSqrt2;
                    double var11 = 0.0D;
                    int var16 = 0;

                    for (int var13 = 0; var13 < 8; ++var13) {
                        for (int var14 = 0; var14 < 8; ++var14) {
                            var11 += (double) var1[var16++] * this.mCosT[var8][var14] * this.mCosT[var5][var13];
                        }
                    }

                    var2[var3++] = (int) (var11 * var9 * var6 / 4.0D);
                }
            }
        } catch (RuntimeException var15) {
            var15.printStackTrace();
        }

    }

    private void tHuffman(int[] var1, int var2) throws IOException {
        try {
            byte[] var3;
            byte[] var4;
            short[] var5;
            short[] var6;
            int var7;
            int var8;
            if (var2 == 0) {
                var4 = this.kYAcSizeT;
                var6 = this.kYAcCodeT;
                var3 = this.kYDcSizeT;
                var5 = this.kYDcCodeT;
                var7 = this.kYEOBidx;
                var8 = this.kYZRLidx;
            } else {
                var4 = this.kCAcSizeT;
                var6 = this.kCAcCodeT;
                var3 = this.kCDcSizeT;
                var5 = this.kCDcCodeT;
                var7 = this.kCEOBidx;
                var8 = this.kCZRLidx;
            }

            int var9 = var1[0] - this.mOldDC[var2];
            this.mOldDC[var2] = var1[0];
            int var10 = Math.abs(var9);

            byte var11;
            for (var11 = 0; var10 > 0; ++var11) {
                var10 >>= 1;
            }

            this.wBit(var5[var11], var3[var11]);
            if (var11 != 0) {
                if (var9 < 0) {
                    --var9;
                }

                this.wBit(var9, var11);
            }

            int var12 = 0;

            for (int var13 = 1; var13 < 64; ++var13) {
                var10 = Math.abs(var1[this.kZigzag[var13]]);
                if (var10 == 0) {
                    ++var12;
                } else {
                    while (var12 > 15) {
                        this.wBit(var6[var8], var4[var8]);
                        var12 -= 16;
                    }

                    for (var11 = 0; var10 > 0; ++var11) {
                        var10 >>= 1;
                    }

                    int var14 = var12 * 10 + var11 + (var12 == 15 ? 1 : 0);
                    this.wBit(var6[var14], var4[var14]);
                    var9 = var1[this.kZigzag[var13]];
                    if (var9 < 0) {
                        --var9;
                    }

                    this.wBit(var9, var11);
                    var12 = 0;
                }
            }

            if (var12 > 0) {
                this.wBit(var6[var7], var4[var7]);
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

    }

    private void tQuantization(int[] var1, int var2) {
        int[] var3 = var2 == 0 ? this.kYQuantumT : this.kCQuantumT;

        for (int var4 = 0; var4 < var1.length; ++var4) {
            var1[var4] /= var3[var4];
        }

    }

    private void w(int var1) throws IOException {
        this.wFullBit();
        this.OUT.write(var1);
    }

    private void wArray(byte[] var1) throws IOException {
        this.wFullBit();

        for (int var2 = 0; var2 < var1.length; ++var2) {
            this.w(var1[var2] & 255);
        }

    }

    private void wArray(int[] var1) throws IOException {
        this.wFullBit();

        for (int var2 = 0; var2 < var1.length; ++var2) {
            this.w(var1[var2] & 255);
        }

    }

    private void wBit(int var1, byte var2) throws IOException {
        --var2;

        for (byte var4 = var2; var4 >= 0; --var4) {
            int var3 = var1 >>> var4 & 1;
            this.bitValue |= var3 << this.bitSeek;
            if (--this.bitSeek <= -1) {
                this.OUT.write(this.bitValue);
                if (this.bitValue == 255) {
                    this.OUT.write(0);
                }

                this.bitValue = 0;
                this.bitSeek = 7;
            }
        }

    }

    private void wFullBit() throws IOException {
        if (this.bitSeek != 7) {
            this.wBit(255, (byte) (this.bitSeek + 1));
            this.bitValue = 0;
            this.bitSeek = 7;
        }

    }

    private void wShort(int var1) throws IOException {
        this.wFullBit();
        this.OUT.write(var1 >>> 8 & 255);
        this.OUT.write(var1 & 255);
    }

    private void ycc(int[] var1, int var2) {
        int var3 = var2 >>> 16 & 255;
        int var4 = var2 >>> 8 & 255;
        int var5 = var2 & 255;
        var1[0] = (int) (0.299F * (float) var3 + 0.587F * (float) var4 + 0.114F * (float) var5 - 128.0F);
        var1[1] = (int) (-(0.1687F * (float) var3) - 0.3313F * (float) var4 + 0.5F * (float) var5);
        var1[2] = (int) (0.5F * (float) var3 - 0.4187F * (float) var4 - 0.0813F * (float) var5);
    }
}
