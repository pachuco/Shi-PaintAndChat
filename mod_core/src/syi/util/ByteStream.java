package syi.util;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ByteStream extends OutputStream {
    private byte[] buffer;
    private int last;

    public ByteStream() {
        this(512);
    }

    public ByteStream(byte[] var1) {
        this.last = 0;
        this.buffer = var1;
    }

    public ByteStream(int var1) {
        this.last = 0;
        this.buffer = new byte[var1 <= 0 ? 512 : var1];
    }

    public final void addSize(int var1) {
        int var2 = this.last + var1;
        if (this.buffer.length < var2) {
            byte[] var3 = new byte[Math.max((int) ((float) this.buffer.length * 1.5F), var2) + 1];
            System.arraycopy(this.buffer, 0, var3, 0, this.buffer.length);
            this.buffer = var3;
        }

    }

    public void gc() {
        if (this.buffer.length != this.last) {
            byte[] var1 = new byte[this.last];
            if (this.last != 0) {
                System.arraycopy(this.buffer, 0, var1, 0, this.last);
            }

            this.buffer = var1;
        }
    }

    public byte[] getBuffer() {
        return this.buffer;
    }

    public final void insert(int var1, int var2) {
        this.buffer[var1] = (byte) var2;
    }

    public void reset() {
        this.last = 0;
    }

    public void reset(int var1) {
        int var2 = this.last;
        this.reset();
        if (var1 < var2) {
            this.write(this.buffer, var1, var2 - var1);
        }

    }

    public void seek(int var1) {
        this.last = var1;
    }

    public final int size() {
        return this.last;
    }

    public byte[] toByteArray() {
        byte[] var1 = new byte[this.last];
        if (this.last > 0) {
            System.arraycopy(this.buffer, 0, var1, 0, this.last);
        }

        return var1;
    }

    public final void w(long var1, int var3) throws IOException {
        for (int var4 = var3 - 1; var4 >= 0; --var4) {
            this.write((int) (var1 >>> (var4 << 3)) & 255);
        }

    }

    public final void w2(int var1) throws IOException {
        this.write(var1 >>> 8 & 255);
        this.write(var1 & 255);
    }

    public final void write(byte[] var1) {
        this.write(var1, 0, var1.length);
    }

    public final void write(byte[] var1, int var2, int var3) {
        this.addSize(var3);
        System.arraycopy(var1, var2, this.buffer, this.last, var3);
        this.last += var3;
    }

    public final void write(int var1) throws IOException {
        this.addSize(1);
        this.buffer[this.last++] = (byte) var1;
    }

    public void write(InputStream var1) throws IOException {
        this.addSize(128);

        int var2;
        try {
            while ((var2 = var1.read(this.buffer, this.last, this.buffer.length - this.last)) != -1) {
                this.last += var2;
                if (this.last >= this.buffer.length) {
                    this.addSize(256);
                }
            }
        } catch (IOException var3) {
            ;
        }

    }

    public void write(InputStream var1, int var2) throws IOException {
        if (var2 != 0) {
            this.addSize(var2);
            int var4 = 0;

            int var3;
            while ((var3 = var1.read(this.buffer, this.last, var2 - var4)) != -1) {
                this.last += var3;
                var4 += var3;
                if (var4 >= var2) {
                    break;
                }
            }

            if (var4 < var2) {
                throw new EOFException();
            }
        }
    }

    public byte[] writeTo(byte[] var1, int var2) {
        int var3 = var2 + this.last;
        if (var1 == null) {
            var1 = new byte[var3];
        }

        if (var1.length < var3) {
            byte[] var4 = new byte[var3];
            System.arraycopy(var1, 0, var4, 0, var1.length);
            var1 = var4;
        }

        System.arraycopy(this.buffer, 0, var1, var2, this.last);
        return var1;
    }

    public void writeTo(OutputStream var1) throws IOException {
        if (this.last != 0) {
            var1.write(this.buffer, 0, this.last);
        }
    }
}
