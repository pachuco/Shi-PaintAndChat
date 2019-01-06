package paintchat_server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import syi.util.Io;

public class PchOutputStream extends OutputStream {
    private OutputStream out;
    private boolean isWriteHeader = false;
    public static String OPTION_MGCOUNT = "mg_count";
    public static String OPTION_VERSION = "version";
    private int write_size = 0;

    public PchOutputStream(OutputStream var1, boolean var2) {
        this.out = var1;
        this.isWriteHeader = var2;
    }

    public void close() throws IOException {
        if (!this.isWriteHeader) {
            this.writeHeader();
        }

        this.out.close();
    }

    public void flush() throws IOException {
        if (!this.isWriteHeader) {
            this.writeHeader();
        }

        this.out.flush();
    }

    public void write(byte[] var1) throws IOException {
        this.write(var1, 0, var1.length);
    }

    public void write(byte[] var1, int var2, int var3) throws IOException {
        if (!this.isWriteHeader) {
            this.writeHeader();
        }

        Io.wShort(this.out, var3);
        this.out.write(var1, var2, var3);
    }

    public void write(int var1) throws IOException {
        if (!this.isWriteHeader) {
            this.writeHeader();
        }

        this.out.write(var1);
    }

    public void write(File var1) throws IOException {
        if (!this.isWriteHeader) {
            this.writeHeader();
        }

        byte var2 = 0;
        int var3 = (int) var1.length();
        byte[] var5 = (byte[]) null;
        if (var3 > 2) {
            FileInputStream var6 = null;

            try {
                var6 = new FileInputStream(var1);

                while (var2 < var3) {
                    int var4 = Io.readUShort(var6);
                    if (var5 == null || var5.length < var4) {
                        var5 = new byte[var4];
                    }

                    Io.rFull(var6, var5, 0, var4);
                    this.write(var5, 0, var4);
                }
            } catch (IOException var8) {
            }

            if (var6 != null) {
                try {
                    var6.close();
                } catch (IOException var7) {
                }
            }

        }
    }

    private void writeHeader() throws IOException {
        if (!this.isWriteHeader) {
            this.out.write(13);
            this.out.write(10);
            this.isWriteHeader = true;
        }
    }

    public void writeHeader(String var1, String var2) throws IOException {
        if (!this.isWriteHeader) {
            this.out.write((var1 + "=" + var2 + "\r\n").getBytes("UTF8"));
        }
    }

    public int size() {
        return this.write_size;
    }
}
