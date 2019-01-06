package syi.util;

import java.io.IOException;
import java.io.OutputStream;

public class BitOutputStream extends OutputStream {
    private int bit_count = 0;
    private int bit;
    private OutputStream out;

    public BitOutputStream(OutputStream var1) {
        this.out = var1;
    }

    public void close() throws IOException {
        if (this.bit_count > 0) {
            this.wBit(0, 8 - this.bit_count);
        }

        this.flush();
        this.out.close();
    }

    public void flush() throws IOException {
        this.out.flush();
    }

    public void wBit(int var1, int var2) throws IOException {
        while (var2 > 0) {
            int var3;
            if (this.bit_count == 0 && var2 >= 8) {
                var3 = 8;
                this.out.write(var1 & 255);
            } else {
                var3 = Math.min(8 - this.bit_count, var2);
                this.bit |= var1 << this.bit_count;
                this.bit_count += var3;
                if (this.bit_count >= 8) {
                    this.out.write(this.bit);
                    this.bit = 0;
                    this.bit_count = 0;
                }
            }

            var1 >>>= var3;
            var2 -= var3;
        }

    }

    public void write(int var1) throws IOException {
        this.wBit(var1, 8);
    }
}
