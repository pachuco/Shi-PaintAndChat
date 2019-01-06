package syi.png;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.CRC32;
import java.util.zip.Deflater;

import syi.util.ByteStream;

public class SPngEncoder {
    private final String STR_C = "(C)shi-chan 2001-2003";
    private OutputStream OUT;
    private int width;
    private int height;
    private int[] i_off;
    private CRC32 crc = new CRC32();
    private Deflater deflater;
    private byte[] b;
    private byte[] bGet = new byte[1025];
    private int seek = 0;
    private int[] iF = new int[3];
    private int[] iFNow = new int[3];
    private int[] iFLOld = new int[3];
    private int[] iFL;
    private ByteStream work;
    private int image_type = 2;
    private byte image_filter;
    private boolean isProgress = false;

    public SPngEncoder(ByteStream var1, ByteStream var2, Deflater var3) {
        System.out.println("(C)shi-chan 2001-2003");
        this.work = var1;
        this.b = var2.getBuffer();
        this.deflater = var3;
    }

    private void bFilter() throws IOException {
        this.bW(this.image_filter);
        this.zero(this.iF);
    }

    private void bW(byte var1) throws IOException {
        this.b[this.seek++] = var1;
        if (this.seek >= this.b.length) {
            this.wCompress();
        }

    }

    public void encode(OutputStream var1, int[] var2, int var3, int var4, int var5) {
        try {
            this.OUT = var1;
            this.width = var3;
            this.height = var4;
            this.i_off = var2;
            this.image_filter = (byte) var5;
            if (this.image_filter > 1) {
                if (this.iFL == null || this.iFL.length < this.width) {
                    this.iFL = new int[this.width];
                }

                this.zero(this.iFL);
                this.zero(this.iFLOld);
            }

            var1.write(new byte[]{-119, 80, 78, 71, 13, 10, 26, 10});
            this.mIHDR();
            this.mEXt("Title", "Shi-Tools Oekaki Data");
            this.mEXt("Copyright", "(C)shi-chan 2001-2003");
            this.mEXt("Software", "Shi-Tools");
            this.mIDAT();
            this.mIEND();
            var1.flush();
        } catch (Throwable var7) {
            var7.printStackTrace();
        }

    }

    public void fencode(ByteStream var1, int[] var2, int var3, int var4) {
        var1.reset();
        int[] var5 = new int[4];

        int var6;
        for (var6 = 0; var6 < 4; ++var6) {
            var1.reset();
            this.encode(var1, var2, var3, var4, var6);
            var5[var6] = var1.size();
        }

        for (var6 = 0; var6 < 4; ++var6) {
            int var7;
            for (var7 = var6 + 1; var7 < 4 && var5[var6] <= var5[var7]; ++var7) {
            }

            if (var7 >= 4) {
                break;
            }
        }

        if (var6 != 3) {
            var1.reset();
            this.encode(var1, var2, var3, var4, var6);
        }

    }

    private void getFPic(int var1, int var2) {
        int var3;
        for (var3 = 0; var3 < 3; ++var3) {
            this.iFNow[var3] = var1 >>> (var3 << 3) & 255;
        }

        label39:
        switch (this.image_filter) {
            case 0:
                return;
            case 1:
                var3 = 0;

                while (true) {
                    if (var3 >= 3) {
                        break label39;
                    }

                    this.iF[var3] = this.iFNow[var3] - this.iF[var3];
                    ++var3;
                }
            case 2:
                for (var3 = 0; var3 < 3; ++var3) {
                    this.iF[var3] = this.iFNow[var3] - (this.iFL[var2] >>> (var3 << 3) & 255);
                }

                this.iFL[var2] = var1;
                break;
            case 3:
                for (var3 = 0; var3 < 3; ++var3) {
                    this.iF[var3] = this.iFNow[var3] - (this.iF[var3] + (this.iFL[var2] >>> (var3 << 3) & 255) >>> 1);
                }

                this.iFL[var2] = var1;
            case 4:
        }

        int[] var4 = this.iF;
        this.iF = this.iFNow;
        this.iFNow = var4;
    }

    private void mEXt(String var1, String var2) throws IOException {
        int var3 = 1950701684;
        this.wCh(var3);
        this.wArray(var1.getBytes());
        this.w(0);
        this.wArray(var2.getBytes());
        this.wChA();
    }

    private void mIDAT() throws IOException {
        int var1 = 1229209940;
        this.wCh(var1);
        this.wImage();
        this.wChA();
    }

    private void mIEND() throws IOException {
        int var1 = 1229278788;
        this.wCh(var1);
        this.wChA();
    }

    private void mIHDR() throws IOException {
        int var1 = 1229472850;
        this.wCh(var1);
        this.wInt(this.width);
        this.wInt(this.height);
        this.w(8);
        this.w(2);
        this.w(0);
        this.w(0);
        this.w(this.isProgress ? 1 : 0);
        this.wChA();
    }

    public void setInterlace(boolean var1) {
        this.isProgress = var1;
    }

    private void w(int var1) throws IOException {
        this.work.write(var1);
    }

    private void wArray(byte[] var1) throws IOException {
        this.wArray(var1, var1.length);
    }

    private void wArray(byte[] var1, int var2) throws IOException {
        if (var2 > 0) {
            this.work.write(var1, 0, var2);
        }

    }

    private void wCh(int var1) throws IOException {
        this.work.reset();
        this.wInt(var1);
    }

    private void wChA() throws IOException {
        int var1 = this.work.size();
        this.crc.reset();
        this.crc.update(this.work.getBuffer(), 0, var1);
        this.wInt((int) this.crc.getValue());
        var1 -= 4;

        for (int var2 = 24; var2 >= 0; var2 -= 8) {
            this.OUT.write(var1 >>> var2 & 255);
        }

        this.work.writeTo(this.OUT);
    }

    private void wCompress() throws IOException {
        if (this.seek != 0) {
            this.deflater.setInput(this.b, 0, this.seek);
            this.seek = 0;

            while (!this.deflater.needsInput()) {
                int var1 = this.deflater.deflate(this.bGet, 0, this.bGet.length - 1);
                if (var1 != 0) {
                    this.wArray(this.bGet, var1);
                }
            }

        }
    }

    private void wImage() throws IOException {
        int var1 = 0;
        this.deflater.reset();
        this.seek = 0;
        int var2;
        int var3;
        int var4;
        if (!this.isProgress) {
            for (var3 = 0; var3 < this.height; ++var3) {
                this.bFilter();

                for (var2 = 0; var2 < this.width; ++var2) {
                    this.getFPic(this.i_off[var1++], var2);

                    for (var4 = 2; var4 >= 0; --var4) {
                        this.bW((byte) this.iFNow[var4]);
                    }
                }
            }
        } else {
            boolean var7 = false;
            byte[][][] var9 = new byte[][][]{{new byte[1]}, {{4}}, {{32, 36}}, {{2, 6}, {34, 38}}, {{16, 18, 20, 22}, {48, 50, 52, 54}}, {{1, 3, 5, 7}, {17, 19, 21, 23}, {33, 35, 37, 39}, {49, 51, 53, 55}}, {{8, 9, 10, 11, 12, 13, 14, 15}, {24, 25, 26, 27, 28, 29, 30, 31}, {40, 41, 42, 43, 44, 45, 46, 47}, {56, 57, 58, 59, 60, 61, 62, 63}}};

            for (int var10 = 0; var10 < var9.length; ++var10) {
                this.zero(this.iFL);
                this.zero(this.iF);

                for (var3 = 0; var3 < this.height; var3 += 8) {
                    for (int var11 = 0; var11 < var9[var10].length; ++var11) {
                        boolean var8 = false;

                        for (var2 = 0; var2 < this.width; var2 += 8) {
                            for (int var12 = 0; var12 < var9[var10][var11].length; ++var12) {
                                byte var13 = var9[var10][var11][var12];
                                int var5 = var2 + var13 % 8;
                                int var6 = var3 + var13 / 8;
                                if (var5 < this.width && var6 < this.height) {
                                    if (!var8) {
                                        this.bFilter();
                                        var8 = true;
                                    }

                                    this.getFPic(this.i_off[this.width * var6 + var5], var5);

                                    for (var4 = 2; var4 >= 0; --var4) {
                                        this.bW((byte) this.iFNow[var4]);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        this.wCompress();
        this.deflater.finish();

        while (!this.deflater.finished()) {
            var1 = this.deflater.deflate(this.bGet, 0, this.bGet.length - 1);
            this.wArray(this.bGet, var1);
        }

    }

    private void wInt(int var1) throws IOException {
        for (int var2 = 24; var2 >= 0; var2 -= 8) {
            this.w(var1 >>> var2 & 255);
        }

    }

    private void zero(int[] var1) {
        if (var1 != null) {
            for (int var2 = 0; var2 < var1.length; ++var2) {
                var1[var2] = 0;
            }

        }
    }
}
