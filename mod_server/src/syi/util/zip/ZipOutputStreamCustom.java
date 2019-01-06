package syi.util.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import syi.util.ByteStream;
import syi.util.Io;

public class ZipOutputStreamCustom extends ZipOutputStream {
    private boolean is_deflate = true;
    private CRC32 crc32 = null;

    public ZipOutputStreamCustom(OutputStream var1) {
        super(var1);
    }

    public static void main(String[] var0) {
        try {
            File var1 = new File("C:\\IBMVJava\\Ide\\project_resources\\PaintChat\\sub\\bbs\\pbbs\\");
            ZipOutputStreamCustom var2 = new ZipOutputStreamCustom(new FileOutputStream(new File(var1, "_PaintBBS.jar")));
            var2.setMethod(8);
            var2.setLevel(9);
            var2.putZip(new ZipInputStream(new FileInputStream(new File(var1, "PaintBBS.jar"))));
            var2.finish();
            var2.close();
        } catch (Throwable var3) {
            var3.printStackTrace();
        }

        System.exit(0);
    }

    public void putBytes(byte[] var1, int var2, int var3, String var4) throws IOException {
        ZipEntry var5 = new ZipEntry(var4);
        if (!this.is_deflate) {
            if (this.crc32 == null) {
                this.crc32 = new CRC32();
            } else {
                this.crc32.reset();
            }

            this.crc32.update(var1);
            var5.setSize((long) var1.length);
            var5.setCrc(this.crc32.getValue());
        }

        this.putNextEntry(var5);
        this.write(var1, var2, var3);
        this.closeEntry();
    }

    public void putDirectory(File var1, String var2) throws IOException {
        File var3 = new File(var1, var2);
        if (var3.isFile()) {
            this.putFile(var1, var2);
        } else if (var3.exists()) {
            String[] var4 = var3.list();

            for (int var5 = 0; var5 < var4.length; ++var5) {
                new File(var3, var4[var5]);
                this.putDirectory(var1, var2 + '/' + var4[var5]);
            }

        }
    }

    public void putFile(File var1, String var2) throws IOException {
        byte[] var3 = (byte[]) null;
        File var4 = new File(var1, var2);
        if (var4.isDirectory()) {
            this.putDirectory(var1, var2);
        } else if (var4.exists()) {
            try {
                var3 = new byte[(int) var4.length()];
                FileInputStream var5 = new FileInputStream(var4);
                Io.rFull(var5, var3, 0, var3.length);
                var5.close();
            } catch (IOException var6) {
                var6.printStackTrace();
                var3 = (byte[]) null;
            }

            if (var3 != null) {
                this.putBytes(var3, 0, var3.length, var2);
            }
        }
    }

    public void putFile(String var1, String var2) throws IOException {
        this.putFile(new File(var1), var2);
    }

    public void putZip(ZipInputStream var1) throws IOException {
        ByteStream var3 = new ByteStream();
        byte[] var4 = new byte[512];

        ZipEntry var2;
        while ((var2 = var1.getNextEntry()) != null) {
            int var5;
            while ((var5 = var1.read(var4)) != -1) {
                var3.write(var4, 0, var5);
            }

            var1.closeEntry();
            this.putBytes(var3.getBuffer(), 0, var3.size(), var2.getName());
            var3.reset();
        }

        var1.close();
    }

    public void setMethod(int var1) {
        this.is_deflate = var1 == 8;
        super.setMethod(var1);
    }
}
