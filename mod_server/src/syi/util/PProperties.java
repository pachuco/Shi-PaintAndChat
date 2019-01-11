package syi.util;

import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.Hashtable;

public class PProperties extends Hashtable {
    private static final String str_empty = "";

    public final boolean getBool(String var1) {
        return this.getBool(var1, false);
    }

    public final boolean getBool(String var1, boolean var2) {
        try {
            var1 = (String) this.get(var1);
            if (var1 == null || var1.length() <= 0) {
                return var2;
            }

            char var3 = Character.toLowerCase(var1.charAt(0));
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
        } catch (Exception var4) {
        }

        return var2;
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
            String var3 = (String) this.get(var1);
            if (var3 != null && var3.length() > 0) {
                return Integer.decode(var3);
            }
        } catch (Throwable ex) {
        }

        return var2;
    }

    public final String getString(String var1) {
        return this.getString(var1, "");
    }

    public final String getString(String var1, String var2) {
        if (var1 == null) {
            return var2;
        } else {
            Object var3 = this.get(var1);
            return var3 == null ? var2 : var3.toString();
        }
    }

    public synchronized boolean load(InputStream var1) {
        this.clear();
        return this.loadPut(var1);
    }

    public synchronized boolean load(Reader var1) {
        this.clear();
        return this.loadPut(var1);
    }

    public synchronized void load(String var1) {
        if (var1 != null && var1.length() > 0) {
            this.load((Reader) (new StringReader(var1)));
        }
    }

    public synchronized boolean loadPut(InputStream var1) {
        return this.loadPut((Reader) (new InputStreamReader(var1)));
    }

    public synchronized boolean loadPut(Reader var1) {
        try {
            Object var2 = var1 instanceof StringReader ? var1 : new BufferedReader(var1, 1024);
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

                                var6 = var5.substring(0, var4);
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
            System.out.println("load" + var8.getMessage());
            return false;
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

    public void save(OutputStream var1) throws Exception {
        this.save(var1, (Hashtable) null);
    }

    public synchronized void save(OutputStream var1, Hashtable var2) throws IOException {
        PrintWriter var6 = new PrintWriter(new OutputStreamWriter(var1), false);
        boolean var7 = var2 != null;
        Enumeration var8 = this.keys();

        while (true) {
            String var4;
            do {
                do {
                    do {
                        if (!var8.hasMoreElements()) {
                            var6.flush();
                            var6.close();
                            var1.close();
                            return;
                        }

                        var4 = (String) var8.nextElement();
                    } while (var4 == null);
                } while (var4.length() <= 0);
            } while (var4.charAt(0) == '#');

            if (var7) {
                String var5 = (String) var2.get(var4);
                if (var5 != null && var5.length() > 0) {
                    StringReader var9 = new StringReader(var5);

                    try {
                        while (true) {
                            String var10 = this.readLine(var9);
                            var6.print('#');
                            var6.println(var10);
                        }
                    } catch (EOFException var11) {
                    }
                }
            }

            String var3 = this.getString(var4);
            var6.print(var4);
            var6.print('=');
            if (var3 != null && var3.length() > 0) {
                var6.print(var3);
            }

            var6.println();
            var6.println();
        }
    }
}
