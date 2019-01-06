package syi.util;

import java.io.InputStream;

public class BitInputStream {
    private byte[] data;
    private int seekMax;
    private int seekByte = 0;
    private int seekBit = 7;
    private byte nowByte;
    private InputStream in;

    public BitInputStream() {
    }

    public BitInputStream(byte[] var1) {
        this.setArray(var1);
    }

    public BitInputStream(InputStream var1) {
    }

    public final int r() {
        if (this.seekByte >= this.seekMax) {
            return -1;
        } else {
            int var1 = this.nowByte >> this.seekBit & 1;
            if (--this.seekBit < 0) {
                this.seekBit = 7;
                ++this.seekByte;
                if (this.seekByte < this.seekMax) {
                    this.nowByte = this.data[this.seekByte];
                }
            }

            return var1;
        }
    }

    public final int rByte() {
        int var1 = 0;

        for (int var2 = 7; var2 >= 0; --var2) {
            if (this.r() == -1) {
                return var2 == 7 ? -1 : var1;
            }

            var1 |= this.r() << var2;
        }

        return var1;
    }

    public void setArray(byte[] var1) {
        this.setArray(var1, var1.length);
    }

    public void setArray(byte[] var1, int var2) {
        this.setArray(var1, 0, var2);
    }

    public void setArray(byte[] var1, int var2, int var3) {
        this.data = var1;
        this.seekByte = var2;
        this.seekBit = 7;
        this.seekMax = var3;
        this.nowByte = this.data[var2];
    }
}
