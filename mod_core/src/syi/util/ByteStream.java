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

    public ByteStream(byte[] buffer) {
        this.last = 0;
        this.buffer = buffer;
    }

    public ByteStream(int bufferSize) {
        this.last = 0;
        this.buffer = new byte[bufferSize <= 0 ? 512 : bufferSize];
    }

    /** Increases the buffer size */
    public final void addSize(int size) {
        int newLength = this.last + size;
        if (this.buffer.length < newLength) {
            byte[] dest = new byte[Math.max((int) ((float) this.buffer.length * 1.5F), newLength) + 1];
            System.arraycopy(this.buffer, 0, dest, 0, this.buffer.length);
            this.buffer = dest;
        }

    }

    /** Trims down the buffer up to this.last */
    public void gc() {
        if (this.buffer.length != this.last) {
            byte[] dest = new byte[this.last];
            if (this.last != 0) {
                System.arraycopy(this.buffer, 0, dest, 0, this.last);
            }

            this.buffer = dest;
        }
    }

    public byte[] getBuffer() {
        return this.buffer;
    }

    public final void insert(int pos, int val) {
        this.buffer[pos] = (byte) val;
    }

    public void reset() {
        this.last = 0;
    }

    public void reset(int pos) {
        int last = this.last;
        this.reset();
        if (pos < last) {
            this.write(this.buffer, pos, last - pos);
        }

    }

    public void seek(int pos) {
        this.last = pos;
    }

    public final int size() {
        return this.last;
    }

    public byte[] toByteArray() {
        byte[] dest = new byte[this.last];
        if (this.last > 0) {
            System.arraycopy(this.buffer, 0, dest, 0, this.last);
        }

        return dest;
    }

    /** Writes the byteNum byte of val */
    public final void w(long val, int byteNum) throws IOException {
        for (int bitShift = byteNum - 1; bitShift >= 0; --bitShift) {
            this.write((int) (val >>> (bitShift << 3)) & 255);
        }
    }

    /** Writes two bytes as separate integers from the input value (higher byte first) */
    public final void w2(int twoBytes) throws IOException {
        this.write(twoBytes >>> 8 & 255);
        this.write(twoBytes & 255);
    }

    public final void write(byte[] src) {
        this.write(src, 0, src.length);
    }

    public final void write(byte[] src, int srcPos, int length) {
        this.addSize(length);
        System.arraycopy(src, srcPos, this.buffer, this.last, length);
        this.last += length;
    }

    public final void write(int val) throws IOException {
        this.addSize(1);
        this.buffer[this.last++] = (byte) val;
    }

    public void write(InputStream in) throws IOException {
        this.addSize(128);

        int i;
        try {
            while ((i = in.read(this.buffer, this.last, this.buffer.length - this.last)) != -1) {
                this.last += i;
                if (this.last >= this.buffer.length) {
                    this.addSize(256);
                }
            }
        } catch (IOException ex) {
            ;
        }

    }

    public void write(InputStream in, int length) throws IOException {
        if (length != 0) {
            this.addSize(length);
            int offset = 0;

            int i;
            while ((i = in.read(this.buffer, this.last, length - offset)) != -1) {
                this.last += i;
                offset += i;
                if (offset >= length) {
                    break;
                }
            }

            if (offset < length) {
                throw new EOFException();
            }
        }
    }

    public byte[] writeTo(byte[] dest, int offset) {
        int length = offset + this.last;
        if (dest == null) {
            dest = new byte[length];
        }

        if (dest.length < length) {
            byte[] tmp = new byte[length];
            System.arraycopy(dest, 0, tmp, 0, dest.length);
            dest = tmp;
        }

        System.arraycopy(this.buffer, 0, dest, offset, this.last);
        return dest;
    }

    public void writeTo(OutputStream out) throws IOException {
        if (this.last != 0) {
            out.write(this.buffer, 0, this.last);
        }
    }
}
