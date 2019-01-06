package syi.util;

import java.awt.Component;
import java.awt.Image;
import java.awt.MediaTracker;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Vector;

public class Io {
    public static void close(Object var0) {
        if (var0 != null) {
            try {
                if (var0 instanceof InputStream) {
                    ((InputStream) var0).close();
                    return;
                }

                if (var0 instanceof OutputStream) {
                    ((OutputStream) var0).close();
                    return;
                }

                if (var0 instanceof Reader) {
                    ((Reader) var0).close();
                    return;
                }

                if (var0 instanceof Writer) {
                    ((Writer) var0).close();
                    return;
                }

                if (var0 instanceof RandomAccessFile) {
                    ((RandomAccessFile) var0).close();
                    return;
                }
            } catch (IOException var4) {
            }

            try {
                var0.getClass().getMethod("close").invoke(var0);
            } catch (NoSuchMethodException var1) {
            } catch (IllegalAccessException var2) {
            } catch (InvocationTargetException var3) {
            }

        }
    }

    public static void copyDirectory(File var0, File var1) {
        copyDirectory(var0, var1, (Vector) null);
    }

    public static void copyDirectory(File var0, File var1, Vector var2) {
        if (!var1.isDirectory()) {
            var1.mkdirs();
        }

        String[] var3 = var0.list();

        for (int var5 = 0; var5 < var3.length; ++var5) {
            File var4 = new File(var0, var3[var5]);
            if (var4.isFile()) {
                copyFile(new File(var0, var3[var5]), var1, var2);
            } else {
                copyDirectory(new File(var0, var3[var5]), new File(var1, var3[var5]), var2);
            }
        }

    }

    public static void copy(File var0, File var1, Vector var2) {
        if (var0.isFile()) {
            copyFile(var0, var1, var2);
        } else if (var0.isDirectory()) {
            copyDirectory(var0, var1, var2);
        }

    }

    public static boolean copyFile(File var0, File var1) {
        try {
            File var2 = getDirectory(var1);
            if (!var2.isDirectory()) {
                var2.mkdirs();
            }

            if (var1.isDirectory()) {
                var1 = new File(var1, getFileName(var0.toString()));
            }

            byte[] var3 = new byte[512];
            FileInputStream var5 = new FileInputStream(var0);
            FileOutputStream var6 = new FileOutputStream(var1);

            int var4;
            while ((var4 = var5.read(var3)) != -1) {
                var6.write(var3, 0, var4);
            }

            var6.flush();
            var6.close();
            var5.close();
            return true;
        } catch (IOException var7) {
            var7.printStackTrace();
            return false;
        }
    }

    public static boolean copyFile(File var0, File var1, Vector var2) {
        try {
            if (var0.isDirectory()) {
                throw new IOException("src is directory");
            } else {
                if (var2 != null) {
                    boolean var3 = false;
                    String var4 = getFileName(var0.getCanonicalPath()).toLowerCase();
                    Enumeration var6 = var2.elements();

                    label28:
                    {
                        String var5;
                        do {
                            if (!var6.hasMoreElements()) {
                                break label28;
                            }

                            var5 = var6.nextElement().toString();
                        } while (!var5.endsWith("*") && !var4.endsWith(var5));

                        var3 = true;
                    }

                    if (!var3) {
                        return false;
                    }
                }

                copyFile(var0, var1);
                return true;
            }
        } catch (IOException var7) {
            var7.printStackTrace();
            return false;
        }
    }

    public static void copyFiles(File[] var0, File var1) {
        for (int var2 = 0; var2 < var0.length; ++var2) {
            copyFile(var0[var2], var1);
        }

    }

    public static void copyFiles(String[] var0, String var1) {
        for (int var2 = 0; var2 < var0.length; ++var2) {
            copyFile(new File(var0[var2]), new File(var1));
        }

    }

    public static String getCurrent() {
        return System.getProperty("user.dir", "/");
    }

    public static final String getDateString(String var0, String var1, String var2) {
        String var3 = '.' + var1;
        String var4 = null;
        File var5 = new File(var2);
        if (!var5.isDirectory()) {
            var5.mkdirs();
        }

        try {
            GregorianCalendar var6 = new GregorianCalendar();
            String var7 = var0 + var6.get(2) + '-' + var6.get(5) + '_';

            int var8;
            for (var8 = 0; var8 < 256; ++var8) {
                File var9 = new File(var2, var7 + var8 + var3);
                if (!var9.isFile()) {
                    var4 = var2 + "/" + var7 + var8 + var3;
                    break;
                }
            }

            if (var8 >= 32767) {
                var4 = var2 + "/" + var0 + "over_file255" + var3;
            }
        } catch (RuntimeException var10) {
            var4 = var0 + "." + var1;
        }

        return var4;
    }

    public static File getDirectory(File var0) {
        try {
            return new File(getDirectory(var0.getCanonicalPath()));
        } catch (IOException var1) {
            return null;
        }
    }

    public static String getDirectory(String var0) {
        if (var0 != null && var0.length() > 0) {
            if (var0.indexOf(92) >= 0) {
                var0 = var0.replace('\\', '/');
            }

            int var1 = var0.lastIndexOf(47);
            if (var1 < 0) {
                return "./";
            } else {
                int var2 = var0.indexOf(46, var1);
                if (var2 < var1) {
                    if (var0.charAt(var0.length() - 1) != '/') {
                        var0 = var0 + '/';
                    }
                } else {
                    var0 = var0.substring(0, var1 + 1);
                }

                return var0;
            }
        } else {
            return "./";
        }
    }

    public static String getFileName(String var0) {
        if (var0.lastIndexOf(46) < 0) {
            return "";
        } else {
            int var1 = var0.lastIndexOf(47);
            if (var1 < 0) {
                var1 = var0.lastIndexOf(92);
            }

            return var1 < 0 ? var0 : var0.substring(var1 + 1);
        }
    }

    public static void initFile(File var0) {
        try {
            File var1 = getDirectory(var0);
            if (!var1.isDirectory()) {
                var1.mkdirs();
            }
        } catch (RuntimeException var2) {
        }

    }

    public static Image loadImageNow(Component var0, String var1) {
        Image var2 = null;

        try {
            var2 = var0.getToolkit().getImage(var1);
            MediaTracker var3 = new MediaTracker(var0);
            var3.addImage(var2, 0);
            var3.waitForID(0, 10000L);
            var3.removeImage(var2);
            var3 = null;
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return var2;
    }

    public static String loadString(File var0) throws IOException {
        StringBuffer var1 = new StringBuffer();
        BufferedReader var2 = new BufferedReader(new FileReader(var0));

        int var3;
        while ((var3 = var2.read()) != -1) {
            var1.append((char) var3);
        }

        var2.close();
        return var1.toString();
    }

    public static File makeFile(String var0, String var1) {
        File var2 = toDir(var0);
        if (!var2.exists()) {
            var2.mkdirs();
        }

        return new File(var2, var1);
    }

    public static final void moveFile(File var0, File var1) throws Throwable {
        if (!var0.renameTo(var1)) {
            copyFile(var0, var1);
            var0.delete();
        }

    }

    public static final int r(InputStream var0) throws IOException {
        int var1 = var0.read();
        if (var1 == -1) {
            throw new EOFException();
        } else {
            return var1;
        }
    }

    public static final int readInt(InputStream var0) throws IOException {
        int var1 = var0.read();
        int var2 = var0.read();
        int var3 = var0.read();
        int var4 = var0.read();
        if ((var1 | var2 | var3 | var4) < 0) {
            throw new EOFException();
        } else {
            return (var1 << 24) + (var2 << 16) + (var3 << 8) + var4;
        }
    }

    public static final short readShort(InputStream var0) throws IOException {
        int var1 = var0.read();
        int var2 = var0.read();
        if ((var1 | var2) < 0) {
            throw new EOFException();
        } else {
            return (short) ((var1 << 8) + var2);
        }
    }

    public static final int readUShort(InputStream var0) throws IOException {
        int var1 = var0.read();
        int var2 = var0.read();
        if ((var1 | var2) < 0) {
            throw new EOFException();
        } else {
            return (var1 << 8) + var2;
        }
    }

    public static final void rFull(InputStream var0, byte[] var1, int var2, int var3) throws EOFException, IOException {
        int var4;
        for (var3 += var2; var2 < var3; var2 += var4) {
            var4 = var0.read(var1, var2, var3 - var2);
            if (var4 == -1) {
                throw new EOFException();
            }
        }

    }

    public static File toDir(String var0) {
        if (var0 != null && var0.length() > 0) {
            if (var0.indexOf(92) >= 0) {
                var0 = var0.replace('\\', '/');
            }

            if (var0.charAt(var0.length() - 1) != '/') {
                var0 = var0 + '/';
            }

            return new File(var0);
        } else {
            return new File("./");
        }
    }

    public static final void wInt(OutputStream var0, int var1) throws IOException {
        var0.write(var1 >>> 24);
        var0.write(var1 >>> 16 & 255);
        var0.write(var1 >>> 8 & 255);
        var0.write(var1 & 255);
    }

    public static final void wShort(OutputStream var0, int var1) throws IOException {
        var0.write(var1 >>> 8 & 255);
        var0.write(var1 & 255);
    }
}
