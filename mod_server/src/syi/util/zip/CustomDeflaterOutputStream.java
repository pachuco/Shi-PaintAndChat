package syi.util.zip;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.CRC32;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import syi.util.ByteStream;
import syi.util.Io;

public class CustomDeflaterOutputStream {
    private ByteStream stm;
    private ByteStream work;
    private byte[] buffer;
    private OutputStream Out;
    private boolean isOriginal;
    private ZipOutputStream OutZip;
    private CRC32 crc;

    public CustomDeflaterOutputStream(OutputStream var1) {
        this(var1, false);
    }

    public CustomDeflaterOutputStream(OutputStream var1, boolean var2) {
        this.stm = new ByteStream();
        this.work = new ByteStream();
        this.buffer = new byte[32767];
        this.isOriginal = false;
        this.crc = new CRC32();
        this.Out = var1;
        this.isOriginal = var2;
        if (!var2) {
            this.OutZip = new ZipOutputStream(this.stm);
            this.OutZip.setMethod(0);
            this.OutZip.setLevel(0);
        }

    }

    public void close() throws IOException {
        this.close((String) null);
    }

    public void close(String var1) throws IOException {
        if (!this.isOriginal) {
            this.OutZip.close();
            ZipOutputStream var2 = new ZipOutputStream(this.Out);
            var2.setLevel(9);
            var2.putNextEntry(new ZipEntry(var1 != null && var1.length() != 0 ? var1 : "0.zip"));
            this.stm.writeTo(var2);
            var2.flush();
            var2.closeEntry();
            var2.close();
        } else {
            Deflater var3 = new Deflater(9, true);
            var3.setInput(this.stm.getBuffer(), 0, this.stm.size());
            var3.finish();

            while (!var3.finished()) {
                int var5 = var3.deflate(this.buffer, 0, 1);
                if (var5 > 0) {
                    this.Out.write(this.buffer, 0, var5);
                }
            }
        }

        try {
            this.Out.flush();
            this.Out.close();
        } catch (IOException var4) {
            var4.printStackTrace();
        }

    }

    public static void compress(File var0, File var1) {
        try {
            CustomDeflaterOutputStream var2 = new CustomDeflaterOutputStream(new FileOutputStream(var1), false);
            var2.write(var0);
            var2.close(var0.getName());
        } catch (Throwable var3) {
            var3.printStackTrace();
        }

    }

    public static void main(String[] var0) {
        try {
            File var1 = new File(var0[0] + ".tmp");
            File var2 = new File(var0[0]);
            compress(var2, var1);
            var2.delete();
            var1.renameTo(var2);
        } catch (Throwable var3) {
            var3.printStackTrace();
        }

        System.exit(0);
    }

    public void write(File var1) throws IOException {
        ZipInputStream var2 = new ZipInputStream(new FileInputStream(var1));
        ZipEntry var3;
        int var4;
        if (this.isOriginal) {
            while ((var3 = var2.getNextEntry()) != null) {
                this.work.reset();
                this.work.write(var3.getName().getBytes());
                this.work.write(0);

                while ((var4 = var2.read(this.buffer)) != -1) {
                    this.work.write(this.buffer, 0, var4);
                }

                if (this.work.size() > 0) {
                    Io.wShort(this.stm, this.work.size() & '\uffff');
                    this.work.writeTo(this.stm);
                }
            }
        } else {
            while ((var3 = var2.getNextEntry()) != null) {
                this.work.reset();

                while ((var4 = var2.read(this.buffer)) != -1) {
                    this.work.write(this.buffer, 0, var4);
                }

                if (this.work.size() > 0) {
                    this.crc.reset();
                    this.crc.update(this.work.getBuffer(), 0, this.work.size());
                    ZipEntry var5 = new ZipEntry(var3.getName());
                    var5.setSize((long) this.work.size());
                    var5.setCrc(this.crc.getValue());
                    this.OutZip.putNextEntry(var5);
                    this.work.writeTo(this.OutZip);
                    this.OutZip.flush();
                    this.OutZip.closeEntry();
                }
            }
        }

        var2.close();
    }

    public void write(ByteStream var1) throws IOException {
        ZipInputStream var2 = new ZipInputStream(new ByteArrayInputStream(var1.getBuffer(), 0, var1.size()));
        ZipEntry var3;
        int var4;
        if (this.isOriginal) {
            while ((var3 = var2.getNextEntry()) != null) {
                this.work.reset();
                this.work.write(var3.getName().getBytes());
                this.work.write(0);

                while ((var4 = var2.read(this.buffer)) != -1) {
                    this.work.write(this.buffer, 0, var4);
                }

                if (this.work.size() > 0) {
                    Io.wShort(var1, this.work.size() & '\uffff');
                    this.work.writeTo(var1);
                }
            }
        } else {
            while ((var3 = var2.getNextEntry()) != null) {
                this.work.reset();

                while ((var4 = var2.read(this.buffer)) != -1) {
                    this.work.write(this.buffer, 0, var4);
                }

                if (this.work.size() > 0) {
                    this.crc.reset();
                    this.crc.update(this.work.getBuffer(), 0, this.work.size());
                    ZipEntry var5 = new ZipEntry(var3.getName());
                    var5.setSize((long) this.work.size());
                    var5.setCrc(this.crc.getValue());
                    this.OutZip.putNextEntry(var5);
                    this.work.writeTo(this.OutZip);
                    this.OutZip.flush();
                    this.OutZip.closeEntry();
                }
            }
        }

        var2.close();
    }
}
