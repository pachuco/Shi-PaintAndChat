package syi.util;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Hashtable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ClassLoaderCustom extends ClassLoader {
    private Hashtable cash = new Hashtable();
    private ClassLoader cl = this.getClass().getClassLoader();
    // $FF: synthetic field
    static Class class$0;

    public boolean loadArchive(String var1) {
        try {
            Object var2;
            if (var1.indexOf("://") != -1) {
                var2 = (new URL(var1)).openStream();
            } else {
                var2 = new FileInputStream(new File(Io.getCurrent(), var1));
            }

            ZipInputStream var3 = new ZipInputStream((InputStream) var2);
            byte[] var5 = new byte[4000];
            ByteArrayOutputStream var7 = new ByteArrayOutputStream(4000);

            while (true) {
                String var10;
                do {
                    ZipEntry var4;
                    if ((var4 = var3.getNextEntry()) == null) {
                        var3.close();
                        return true;
                    }

                    var10 = this.replace(var4.getName(), '/', '.', false);
                } while (!var10.endsWith(".class"));

                var7.reset();

                int var8;
                while ((var8 = var3.read(var5)) != -1) {
                    var7.write(var5, 0, var8);
                }

                var7.flush();
                var3.closeEntry();
                byte[] var6 = var7.toByteArray();
                String var9 = var10.substring(0, var10.length() - 6);
                Class var11 = this.defineClass(var9, var6, 0, var6.length);
                var6 = (byte[]) null;
                if (var11 != null) {
                    this.cash.put(var9, var11);
                }

                var11 = null;
            }
        } catch (Exception var12) {
            System.out.println(var12.getMessage());
            return false;
        }
    }

    public Class loadClass(String var1, String var2, boolean var3) {
        Class var4 = (Class) this.cash.get(var1);
        if (var4 == null) {
            if (var2 != null && var2.startsWith("http://")) {
                var4 = this.loadURL(var1, var2);
            } else {
                var4 = this.loadLocal(var1, var2);
            }

            if (var4 == null) {
                Class var10000 = class$0;
                if (var10000 == null) {
                    try {
                        var10000 = Class.forName("java.lang.Object");
                    } catch (ClassNotFoundException var5) {
                        throw new NoClassDefFoundError(var5.getMessage());
                    }

                    class$0 = var10000;
                }

                var4 = var10000;
            }

            this.cash.put(var1, var4);
        }

        if (var3) {
            this.resolveClass(var4);
        }

        return var4;
    }

    protected Class loadClass(String var1, boolean var2) throws ClassNotFoundException {
        Class var3 = (Class) this.cash.get(var1);
        if (var3 == null) {
            return this.findSystemClass(var1);
        } else {
            if (var2) {
                this.resolveClass(var3);
            }

            return var3;
        }
    }

    private Class loadLocal(String var1, String var2) {
        Class var3;
        try {
            if (var2 == null || var2.length() <= 0) {
                var3 = this.findSystemClass(var1);
                if (var3 != null) {
                    return var3;
                }
            }

            File var4 = new File(var2, this.replace(var1, '.', File.separatorChar, true));
            if (!var4.isFile()) {
                return null;
            }

            byte[] var5 = new byte[(int) var4.length()];
            DataInputStream var6 = new DataInputStream(new FileInputStream(var4));
            var6.readFully(var5);
            var6.close();
            var3 = this.defineClass(var1, var5, 0, var5.length);
        } catch (IOException var7) {
            var3 = null;
        } catch (ClassNotFoundException var8) {
            var3 = null;
        }

        return var3;
    }

    private Class loadURL(String var1, String var2) {
        Class var3;
        try {
            String var4 = this.replace(var1, '\\', '/', true);
            URL var5;
            if (var2.charAt(var2.length() - 1) != '/') {
                var5 = new URL(var2 + '/' + var4);
            } else {
                var5 = new URL(var2 + var4);
            }

            var4 = null;
            URLConnection var6 = var5.openConnection();
            var6.connect();
            int var7 = var6.getContentLength();
            byte[] var8 = new byte[var7];
            int var9 = 0;
            InputStream var10 = var6.getInputStream();

            while ((var9 += var10.read(var8, var9, var7 - var9)) < var7) {
            }

            var10.close();
            var3 = this.defineClass(var1, var8, 0, var7);
        } catch (Exception var11) {
            var3 = null;
        }

        return var3;
    }

    public void putClass(Class var1) {
        this.cash.put(var1.getName(), var1);
    }

    private String replace(String var1, char var2, char var3, boolean var4) {
        StringBuffer var5 = new StringBuffer(var1);
        int var6 = var5.length();

        for (int var7 = 0; var7 < var6; ++var7) {
            if (var5.charAt(var7) == var2) {
                var5.setCharAt(var7, var3);
            }
        }

        if (var4) {
            var5.append(".class");
        }

        return var5.toString();
    }
}
