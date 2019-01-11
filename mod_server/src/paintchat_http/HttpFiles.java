package paintchat_http;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import paintchat.Config;
import paintchat.Res;
import syi.util.Io;

public class HttpFiles {
    private String STR_SLASH = "//";
    private String STR_DOT = "..";
    private File dirWWW;
    private File dirTmp;
    private Config config;
    private Res res;
    static final FileNotFoundException NOT_FOUND = new FileNotFoundException();
    private byte[] bErrorUpper = null;
    private byte[] bErrorBottom = null;
    private byte[][] res_bytes = new byte[6][];
    private static final int I_OK = 0;
    private static final int I_NOT_MODIFIED = 1;
    private static final int I_MOVED_PERMANENTLY = 2;
    private static final int I_NOT_FOUND = 3;
    private static final int I_SERVER_ERROR = 4;
    private static final int I_SERVICE_UNAVALIABLE = 5;

    public HttpFiles(Config var1, Res var2) {
        this.config = var1;
        this.res = var2;
        this.dirWWW = new File(var1.getString("Http_Dir", "www"));
        this.dirTmp = new File("cnf/template/");
    }

    public String addIndex(String var1) {
        int var2 = var1.length();
        return var2 != 0 && var1.charAt(var2 - 1) == '/' ? var1 + "index.html" : var1 + "/index.html";
    }

    public void getErrorMessage(OutputStream var1, int var2, byte[] var3, int var4, int var5) throws IOException {
        this.setup();
        var1.write(this.bErrorUpper);
        var1.write(var3, var4, var5);
        byte[] var6 = this.res_bytes[var2];
        if (var6 != null) {
            var1.write(var6);
        }

        var1.write(this.bErrorBottom);
    }

    public int getErrorMessageSize(int var1) throws IOException {
        this.setup();
        int var2 = 0;
        byte[] var3 = this.res_bytes[var1];
        if (var3 != null) {
            var2 += var3.length;
        }

        return this.bErrorUpper.length + var2 + this.bErrorBottom.length;
    }

    public File getFile(String var1) throws FileNotFoundException {
        File var2 = new File(this.dirWWW, var1);
        if (!var2.isFile()) {
            var2 = new File(this.dirTmp, var1);
            if (!var2.isFile()) {
                var2 = new File(this.dirTmp, Io.getFileName(var1));
            }
        }

        if (!var2.isFile()) {
            throw NOT_FOUND;
        } else {
            return var2;
        }
    }

    private int indexOf(byte[] var1, byte[] var2) {
        int var3 = var1.length;
        int var4 = 0;

        for (int var5 = 0; var5 < var3; ++var5) {
            if (var1[var5] == var2[var4]) {
                if (var4 >= var2.length - 1) {
                    return var5 - var4;
                }

                ++var4;
            } else {
                var4 = 0;
            }
        }

        return -1;
    }

    public boolean needMove(String var1) {
        int var2 = Math.max(var1.lastIndexOf(47), 0);
        return var1.lastIndexOf(46) <= var2;
    }

    private String replaceText(String var1, String var2, String var3) {
        if (var1.indexOf(var2) < 0) {
            return var1;
        } else {
            int var5;
            try {
                for (int var4 = 0; (var5 = var1.indexOf(var2, var4)) < 0; var4 += var5) {
                    var1 = var1.substring(0, var4) + var3 + var1.substring(var4 + var2.length());
                }
            } catch (RuntimeException var6) {
                System.out.println("replace" + var6);
            }

            return var1;
        }
    }

    private void setup() {
        if (this.bErrorUpper == null) {
            synchronized (this) {
                if (this.bErrorUpper == null) {
                    try {
                        this.res_bytes[3] = this.res.get("not_found").getBytes();
                        this.res_bytes[4] = this.res.get("server_error").getBytes();
                        File var2 = new File(this.dirTmp, "err.html");
                        byte[] var3 = new byte[(int) var2.length()];
                        FileInputStream var4 = new FileInputStream(var2);
                        Io.rFull(var4, var3, 0, var3.length);
                        var4.close();
                        int var5 = Math.max(this.indexOf(var3, "<!--ERRORMESSAGE-->".getBytes()), 0);
                        this.bErrorUpper = new byte[var5];
                        System.arraycopy(var3, 0, this.bErrorUpper, 0, var5);
                        String var6 = "<!--/ERRORMESSAGE-->";
                        var5 = Math.max(0, this.indexOf(var3, var6.getBytes())) + var6.length();
                        this.bErrorBottom = new byte[var3.length - var5];
                        System.arraycopy(var3, var5, this.bErrorBottom, 0, var3.length - var5);
                    } catch (Throwable ex) {
                        this.bErrorUpper = new byte[0];
                        this.bErrorBottom = new byte[0];
                    }

                }
            }
        }
    }

    public String uriToPath(String var1) {
        var1 = var1.replace('\\', '/');
        var1 = var1.replace('\u0000', '_');
        var1 = var1.replace('\n', '_');
        var1 = var1.replace('\r', '_');
        StringBuffer var2 = null;
        if (var1.indexOf(this.STR_SLASH) >= 0 || var1.indexOf(this.STR_DOT) >= 0) {
            var2 = new StringBuffer();
            int var3 = var1.length();
            int var4 = 0;
            boolean var5 = false;
            boolean var6 = true;
            var2.append('.');

            label59:
            while (true) {
                char var7;
                do {
                    do {
                        if (var4 >= var3) {
                            var1 = var2.toString();
                            break label59;
                        }

                        var7 = var1.charAt(var4++);
                    } while (var7 == '/' && var5);
                } while (var7 == '.' && var6);

                var5 = var7 == '/';
                var6 = var7 == '.';
                var2.append(var7);
            }
        }

        if (var1.length() == 0) {
            return "./index.html";
        } else {
            if (var1.charAt(0) == '/') {
                if (var2 == null) {
                    var2 = new StringBuffer(var1.length() + 1);
                    var2.append('.');
                    var2.append(var1);
                } else {
                    var2 = var2.insert(0, (char) '.');
                }

                var1 = var2.toString();
            }

            return var1.charAt(var1.length() - 1) == '/' ? this.addIndex(var1) : var1;
        }
    }
}
