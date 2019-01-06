package paintchat;

import java.applet.Applet;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.CharArrayWriter;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import syi.awt.Awt;
import syi.util.ByteStream;

public class Res extends Hashtable {
    private Object resBase;
    private Applet applet;
    private ByteStream work;
    private static final String EMPTY = "";

    public Res() {
        this((Applet) null, (Object) null, (ByteStream) null);
    }

    public Res(Applet var1, Object var2, ByteStream var3) {
        this.resBase = var2;
        this.applet = var1;
        this.work = var3;
    }

    public final String get(String var1) {
        return this.get(var1, "");
    }

    public final String get(String var1, String var2) {
        if (var1 == null) {
            return var2;
        } else {
            String var3 = (String) super.get(var1);
            return var3 == null ? var2 : var3;
        }
    }

    public final boolean getBool(String var1) {
        return this.getBool(var1, false);
    }

    public final boolean getBool(String var1, boolean var2) {
        try {
            var1 = this.get(var1);
            if (var1 == null || var1.length() <= 0) {
                return var2;
            }

            char var3 = var1.charAt(0);
            switch (var3) {
                case '0':
                case 'f':
                case 'n':
                case 'x':
                    return false;
                case '1':
                case 'o':
                case 't':
                case 'y':
                    return true;
            }
        } catch (RuntimeException var4) {
        }

        return var2;
    }

    public ByteStream getBuffer() {
        if (this.work == null) {
            this.work = new ByteStream();
        } else {
            this.work.reset();
        }

        return this.work;
    }

    public final int getInt(String var1) {
        try {
            return this.getInt(var1, 0);
        } catch (Exception var2) {
            return 0;
        }
    }

    public final int getInt(String var1, int var2) {
        try {
            String var3 = this.get(var1);
            if (var3 != null && var3.length() > 0) {
                return parseInt(var3);
            }
        } catch (Throwable var4) {
        }

        return var2;
    }

    public String getP(String var1) {
        String var2 = this.p(var1);
        return var2 != null ? var2 : this.get(var1, (String) null);
    }

    public final int getP(String var1, int var2) {
        String var3 = this.p(var1);
        if (var3 != null) {
            this.put(var1, var3);
        }

        return this.getInt(var1, var2);
    }

    public String getP(String var1, String var2) {
        String var3 = this.p(var1);
        return var3 != null && var3.length() > 0 ? var3 : this.get(var1, var2);
    }

    public boolean getP(String var1, boolean var2) {
        String var3 = this.p(var1);
        if (var3 != null) {
            this.put(var1, var3);
        }

        return this.getBool(var1, var2);
    }

    public final Object getRes(Object var1) {
        try {
            Object var2 = this.get(var1);
            if (var2 == null) {
                ByteStream var3 = this.getBuffer();
                var3.write(Awt.openStream(this.resBase instanceof String ? new URL(this.applet.getCodeBase(), (String) this.resBase + (String) var1) : new URL((URL) this.resBase, (String) var1)));
                return var3.toByteArray();
            } else {
                return var2;
            }
        } catch (IOException var4) {
            return null;
        }
    }

    public boolean load(InputStream var1) {
        try {
            return this.load((Reader) (new InputStreamReader(var1, "UTF8")));
        } catch (UnsupportedEncodingException var2) {
            return false;
        }
    }

    public boolean load(Reader var1) {
        try {
            Object var2 = var1 instanceof StringReader ? var1 : new BufferedReader(var1, 512);
            CharArrayWriter var3 = new CharArrayWriter();
            String var6 = null;

            try {
                while (true) {
                    while (true) {
                        String var5 = this.readLine((Reader) var2);
                        if (var5 != null) {
                            int var4 = var5.indexOf(61);
                            if (var4 > 0) {
                                if (var6 != null) {
                                    this.put(var6, var3.toString());
                                    var6 = null;
                                }

                                var6 = var5.substring(0, var4).trim();
                                var3.reset();
                                if (var4 + 1 < var5.length()) {
                                    var3.write(var5.substring(var4 + 1));
                                }
                            } else if (var6 != null) {
                                var3.write(10);
                                var3.write(var5);
                            }
                        }
                    }
                }
            } catch (EOFException var7) {
                if (var6 != null && var3.size() > 0) {
                    this.put(var6, var3.toString());
                }

                ((Reader) var2).close();
                return true;
            }
        } catch (IOException var8) {
            var8.printStackTrace();
            return false;
        }
    }

    public void load(String var1) {
        if (var1 != null && var1.length() > 0) {
            this.load((Reader) (new StringReader(var1)));
        }
    }

    public void loadResource(Res var1, String var2, String var3) {
        boolean var10000;
        if (var3 != null && var3.equals("ja")) {
            var10000 = true;
        } else {
            var10000 = false;
        }

        String var5 = var2 + (var3 != null && var3.length() != 0 ? '_' + var3 : "") + ".txt";

        for (int var6 = 0; var6 < 2; ++var6) {
            try {
                byte[] var7 = (byte[]) var1.getRes(var5);
                if (var7 != null) {
                    ByteArrayInputStream var4 = new ByteArrayInputStream(var7);
                    this.load((Reader) (new InputStreamReader(var4, "UTF8")));
                    break;
                }
            } catch (RuntimeException var8) {
            } catch (UnsupportedEncodingException var9) {
            }

            var5 = var2 + ".txt";
        }

    }

    public void loadZip(InputStream var1) throws IOException {
        ByteStream var2 = this.getBuffer();
        ZipInputStream var3 = new ZipInputStream(var1);

        ZipEntry var4;
        while ((var4 = var3.getNextEntry()) != null) {
            var2.reset();
            var2.write((InputStream) var3);
            this.r(var2, var4.getName());
        }

        var3.close();
    }

    public void loadZip(String var1) throws IOException {
        try {
            InputStream var2 = this.getClass().getResourceAsStream('/' + var1);
            if (var2 != null) {
                this.loadZip(var2);
                return;
            }
        } catch (Throwable var3) {
        }

        this.loadZip(Awt.openStream(new URL(this.applet.getCodeBase(), var1)));
    }

    private String p(String var1) {
        return this.applet.getParameter(var1);
    }

    public static final int parseInt(String var0) {
        int var1 = var0.length();
        if (var1 <= 0) {
            return 0;
        } else {
            byte var2 = 0;
            if (var0.charAt(0) == '0') {
                var2 = 2;
            } else if (var0.charAt(0) == '#') {
                var2 = 1;
            }

            if (var2 == 0) {
                return Integer.parseInt(var0);
            } else {
                int var3 = 0;
                var1 -= var2;

                for (int var4 = 0; var4 < var1; ++var4) {
                    var3 |= Character.digit((char) var0.charAt(var4 + var2), 16) << (var1 - 1 - var4) * 4;
                }

                return var3;
            }
        }
    }

    private void r(ByteStream var1, String var2) throws IOException {
        String var3 = var2.toLowerCase();
        if (var3.endsWith("zip")) {
            this.loadZip((InputStream) (new ByteArrayInputStream(var1.toByteArray())));
        } else {
            this.put(var2, var1.toByteArray());
        }

    }

    private final String readLine(Reader var1) throws EOFException, IOException {
        int var2 = var1.read();
        if (var2 == -1) {
            throw new EOFException();
        } else if (var2 != 13 && var2 != 10) {
            if (var2 == 35) {
                do {
                    var2 = var1.read();
                } while (var2 != 13 && var2 != 10 && var2 != -1);

                return null;
            } else {
                StringBuffer var3 = new StringBuffer();
                var3.append((char) var2);

                while ((var2 = var1.read()) != -1 && var2 != 13 && var2 != 10) {
                    var3.append((char) var2);
                }

                return var3.toString();
            }
        } else {
            return null;
        }
    }

    public final String res(String var1) {
        return this.getP(var1, var1);
    }

    public void put(Res var1) {
        Enumeration var3 = var1.keys();

        while (var3.hasMoreElements()) {
            Object var2 = var3.nextElement();
            this.put(var2, var1.get(var2));
        }

    }
}
