package syi.util;

import java.io.IOException;
import java.io.Writer;

public class CharStream extends Writer {
    private char[] buffer;
    private int count;
    private static char[] separator;

    public CharStream() {
        this(512);
    }

    public CharStream(int var1) {
        this.count = 0;
        this.buffer = new char[var1 <= 0 ? 512 : var1];
        if (separator == null) {
            String var2 = System.getProperty("line.separator", "\n");
            separator = var2.toCharArray();
        }

    }

    public final void addSize(int var1) {
        int var2 = this.count + var1;
        if (this.buffer.length < var2) {
            char[] var3 = new char[Math.max((int) ((float) this.buffer.length * 1.25F), var2) + 1];
            System.arraycopy(this.buffer, 0, var3, 0, this.buffer.length);
            this.buffer = var3;
        }

    }

    public void close() {
    }

    public void flush() {
    }

    public void gc() {
        if (this.buffer.length != this.count) {
            char[] var1 = new char[this.count];
            if (this.count != 0) {
                System.arraycopy(this.buffer, 0, var1, 0, this.count);
            }

            this.buffer = var1;
        }
    }

    public char[] getBuffer() {
        return this.buffer;
    }

    public final void insert(int var1, int var2) {
        this.buffer[var1] = (char) var2;
    }

    public void reset() {
        this.count = 0;
    }

    public void seek(int var1) {
        this.count = var1;
    }

    public final int size() {
        return this.count;
    }

    public char[] toCharArray() {
        char[] var1 = new char[this.count];
        if (this.count > 0) {
            System.arraycopy(this.buffer, 0, var1, 0, this.count);
        }

        return var1;
    }

    public final void write(char[] var1) {
        this.write((char[]) var1, 0, var1.length);
    }

    public final void write(char[] var1, int var2, int var3) {
        this.addSize(var3);
        System.arraycopy(var1, var2, this.buffer, this.count, var3);
        this.count += var3;
    }

    public final void write(int var1) throws IOException {
        this.addSize(1);
        this.buffer[this.count++] = (char) var1;
    }

    public final void write(String var1) {
        this.write((String) var1, 0, var1.length());
    }

    public final void write(String var1, int var2, int var3) {
        this.addSize(var3);
        var1.getChars(var2, var2 + var3, this.buffer, this.count);
        this.count += var3;
    }

    public final void writeln(String var1) {
        this.write(var1);
        this.write(separator);
    }

    public void writeTo(char[] var1, int var2) {
        int var3 = var2 + this.count;
        if (var2 < var3) {
            if (var1.length < var3) {
                char[] var4 = new char[var3];
                System.arraycopy(var1, 0, var4, 0, var1.length);
                var1 = var4;
            }

            System.arraycopy(this.buffer, 0, var1, var2, this.count);
        }
    }

    public void writeTo(Writer var1) throws IOException {
        if (this.count != 0) {
            var1.write((char[]) this.buffer, 0, this.count);
        }
    }
}
